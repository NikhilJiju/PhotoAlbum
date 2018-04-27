package jiju.nikhil.photoandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPhoto extends AppCompatActivity {

    public ArrayList<String> tagNames = new ArrayList<String>();
    public ArrayList<String> tagValues = new ArrayList<String>();
    public String tagtype;
    public String logicChosen;
    public ArrayList<Photo> matches = new ArrayList<Photo>();
    public boolean matched = false;
    public boolean matched2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        matches.clear();
        matched=false;
        matched2=false;

        //add options for tag types.
        String[] options = new String[] {"Person:", "Location:"};
        Spinner tagtypes = (Spinner) findViewById(R.id.tagtype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagtypes.setAdapter(adapter);

        //making sure on tag type is selected
        tagtypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner tagtypes = (Spinner) findViewById(R.id.tagtype);
                tagtype = tagtypes.getSelectedItem().toString();
                //Toast.makeText(getBaseContext(),tagtype,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getBaseContext(),"Nothing selected",Toast.LENGTH_SHORT).show();
            }
        });

        //add logic options for tags
        options = new String[] {"and", "or"};
        Spinner logic = (Spinner) findViewById(R.id.logic);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logic.setAdapter(adapter);

        //making sure a logic type is selected
        logic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner logic = (Spinner) findViewById(R.id.logic);
                logicChosen = logic.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),logicChosen,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getBaseContext(),"Nothing selected",Toast.LENGTH_SHORT).show();
            }
        });

        //only once they type in tag value, so add tag get enabled
        EditText tagtext = (EditText) findViewById(R.id.tagvalue);
        Button addtag = (Button) findViewById(R.id.addtag);
        tagtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
                Button addtag = (Button) findViewById(R.id.addtag);
                if(tagvalue.getText().toString().compareTo("")!=0){ // text is not empty
                    //enable add tag button
                    addtag.setEnabled(true);
                }else{
                    //disable add tag button
                    addtag.setEnabled(false);
                }
            }
        });

        //adding tags
        ListView tags = (ListView) findViewById(R.id.tags);
        addtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
                boolean repeat = false;
                for(int i=0;i<tagNames.size();i++){
                    if(tagNames.get(i).compareTo(tagtype)==0){
                        if (tagValues.get(i).compareTo(tagvalue.getText().toString())==0){
                            repeat = true;
                            break;
                        }
                    }
                }
                if(!repeat){
                    tagValues.add(tagvalue.getText().toString());
                    tagNames.add(tagtype);
                    List<Map<String,String>> data = new ArrayList<Map<String,String>>();
                    for(int i=0;i<tagNames.size();i++){
                        Map<String,String> dataMaps = new HashMap<String,String>(2);
                        dataMaps.put("type",tagNames.get(i));
                        dataMaps.put("value",tagValues.get(i));
                        data.add(dataMaps);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(SearchPhoto.this,data,android.R.layout.simple_expandable_list_item_2,new String[]{"type","value"},new int[]{android.R.id.text1,android.R.id.text2});
                    ListView tags = (ListView) findViewById(R.id.tags);
                    tags.setAdapter(adapter);
                    Spinner logic = (Spinner) findViewById(R.id.logic);
                    logic.setEnabled(false);
                    Button search = (Button) findViewById(R.id.search);
                    search.setEnabled(true);
                }else{
                    Toast.makeText(getBaseContext(),"Tag already inserted",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //clear all tags
        Button cleartag = (Button) findViewById(R.id.cleartag);
        cleartag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagNames.clear();
                tagValues.clear();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchPhoto.this,android.R.layout.simple_list_item_1,new ArrayList<String>());
                ListView tags = (ListView) findViewById(R.id.tags);
                tags.setAdapter(adapter);
                EditText tagtext = (EditText) findViewById(R.id.tagvalue);
                tagtext.setText("");
                Spinner logic = (Spinner) findViewById(R.id.logic);
                logic.setEnabled(true);
                Button search = (Button) findViewById(R.id.search);
                search.setEnabled(false);
            }
        });


        //searching for photos by tag
        //boolean match = false;
        Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matches.clear();
                matched=false;
                matched2=false;
                for(int i=0;i<AlbumPhoto.album.photos.size();i++){ //iterate through all photos in album
                    Photo photo = AlbumPhoto.album.photos.get(i);
                    if(logicChosen.compareTo("or")==0){ //OR LOGIC
                        for(int j =0;j<photo.tagNames.size();j++){ //iterate through all tags for a photo
                            String checkName = photo.tagNames.get(j);
                            String checkValue = photo.tagValues.get(j);
                            for(int k = 0;k<tagNames.size();k++){ //iterate through search tags to look for a match
                                if(tagNames.get(k).compareTo(checkName)==0){
                                    if(tagValues.get(k).compareTo(checkValue)==0){
                                        //photo meets the requirement
                                        matches.add(photo);
                                        matched=true;
                                        break;
                                    }
                                }
                            }
                            if(matched){
                                matched=false; //reset matched
                                break;  //move on to the next photo
                            }
                        }
                    }else{  //AND LOGIC
                        for(int j =0;j<tagNames.size();j++){ //iterate through all search tags
                            String checkName = tagNames.get(j);
                            String checkValue = tagValues.get(j);
                            for(int k = 0;k<photo.tagNames.size();k++){ //iterate through all tags for a photo
                                if(photo.tagNames.get(k).compareTo(checkName)==0){
                                    if(photo.tagValues.get(k).compareTo(checkValue)==0){
                                        //photo meets the requirement
                                        matched2=true;
                                        break;
                                    }
                                }
                            }
                            if(matched2){
                                //continue to move on to next tag
                            }else{
                                matched2=false;
                                break;
                                //didn't meet one of the tag requirements, so break
                            }
                        }
                        if(matched2){
                            matches.add(photo);
                        }
                    }
                }

                System.out.println(matches.size());
                System.out.println("------------------------------------------");
                for(int l=0;l<matches.size();l++){
                    System.out.println(matches.get(l).name);
                }
                ListView thephotos= (ListView) findViewById(R.id.results);
                SearchPhoto.MyAdapter myAdapter= new SearchPhoto.MyAdapter(SearchPhoto.this,matches);
                thephotos.setAdapter(myAdapter);
            }
        });


    }

    public class MyAdapter extends ArrayAdapter<Photo>{
        ArrayList<Photo> photos;
        Context mContext;
        public MyAdapter(Context context, ArrayList<Photo> photos){
            super(context,R.layout.listview_item);
            this.photos= photos;
            this.mContext= context;
        }

        @Override
        public int getCount(){
            return photos.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            SearchPhoto.MyAdapter.ViewHolder holder = new SearchPhoto.MyAdapter.ViewHolder();
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.listview_item, parent, false);
                holder.photo = (ImageView) convertView.findViewById(R.id.imageView);
                holder.caption = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            }
            else{
                holder= (SearchPhoto.MyAdapter.ViewHolder) convertView.getTag();
            }
            holder.photo.setImageURI(Uri.parse(photos.get(position).photo));
            holder.caption.setText(photos.get(position).name);
            return convertView;
        }

        class ViewHolder{
            ImageView photo;
            TextView caption;
        }

    }
}