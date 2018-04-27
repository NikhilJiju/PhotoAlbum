package jiju.nikhil.photoandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

public class DisplayPhoto extends AppCompatActivity {
    static ArrayList<Photo> photos= new ArrayList<Photo>();
    static int position;
    ViewPager viewPager;
    public String tagtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photo);

        System.out.println("----------------------------"+photos.get(position).tagNames.size());
        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        for(int i=0;i<photos.get(position).tagNames.size();i++){
            Map<String,String> dataMaps = new HashMap<String,String>(2);
            dataMaps.put("type",photos.get(position).tagNames.get(i));
            dataMaps.put("value",photos.get(position).tagValues.get(i));
            data.add(dataMaps);
            System.out.println("GOT HERE!!!!!");
        }

        SimpleAdapter adapter = new SimpleAdapter(DisplayPhoto.this,data,android.R.layout.simple_expandable_list_item_2,new String[]{"type","value"},new int[]{android.R.id.text1,android.R.id.text2});
        ListView tags = (ListView) findViewById(R.id.tags);
        tags.setAdapter(adapter);

        viewPager= (ViewPager) findViewById(R.id.picslide);
        ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
        TextView texter= (TextView) findViewById(R.id.texter);
        texter.setText(photos.get(position).name);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                TextView texter= (TextView) findViewById(R.id.texter);
                texter.setText(photos.get(position).name);
                System.out.println("----------------------------"+photos.get(position).tagNames.size());
                List<Map<String,String>> data = new ArrayList<Map<String,String>>();
                for(int i=0;i<photos.get(position).tagNames.size();i++){
                    Map<String,String> dataMaps = new HashMap<String,String>(2);
                    dataMaps.put("type",photos.get(position).tagNames.get(i));
                    dataMaps.put("value",photos.get(position).tagValues.get(i));
                    data.add(dataMaps);
                    System.out.println("GOT HERE!!!!!");
                }

                SimpleAdapter adapter = new SimpleAdapter(DisplayPhoto.this,data,android.R.layout.simple_expandable_list_item_2,new String[]{"type","value"},new int[]{android.R.id.text1,android.R.id.text2});
                ListView tags = (ListView) findViewById(R.id.tags);
                tags.setAdapter(adapter);

            }
        });


        Button addtag = (Button) findViewById(R.id.addtag);
        addtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button submit = (Button) findViewById(R.id.submit);
                Button cancel = (Button) findViewById(R.id.cancel);
                EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
                Spinner tagtypes = (Spinner) findViewById(R.id.tagtypes);
                Button addtag = (Button) findViewById(R.id.addtag);
                Button deletetag = (Button) findViewById(R.id.deletetag);
                submit.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                tagvalue.setVisibility(View.VISIBLE);
                tagtypes.setVisibility(View.VISIBLE);
                addtag.setVisibility(View.INVISIBLE);
                deletetag.setVisibility(View.INVISIBLE);
            }
        });

        //add options for tag types.
        String[] options = new String[] {"Person:", "Location:"};
        Spinner tagtypes = (Spinner) findViewById(R.id.tagtypes);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagtypes.setAdapter(adapter2);


        tagtypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner tagtypes = (Spinner) findViewById(R.id.tagtypes);
                tagtype = tagtypes.getSelectedItem().toString();
                //Toast.makeText(getBaseContext(),tagtype,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getBaseContext(),"Nothing selected",Toast.LENGTH_SHORT).show();
            }
        });

        //only once they type in tag value, so add tag get enabled
        EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
        tagvalue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
                Button submit = (Button) findViewById(R.id.submit);
                if(tagvalue.getText().toString().trim().compareTo("")!=0){ // text is not empty
                    //enable add tag button
                    submit.setEnabled(true);
                }else{
                    //disable add tag button
                    submit.setEnabled(false);
                }
            }
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
                boolean repeat = false;
                for(int i=0;i<photos.get(position).tagNames.size();i++){
                    if(photos.get(position).tagNames.get(i).compareTo(tagtype)==0){
                        if (photos.get(position).tagValues.get(i).compareTo(tagvalue.getText().toString())==0){
                            repeat = true;
                            break;
                        }
                    }
                }
                if(!repeat){
                    photos.get(position).tagValues.add(tagvalue.getText().toString());
                    photos.get(position).tagNames.add(tagtype);
                    List<Map<String,String>> data = new ArrayList<Map<String,String>>();
                    for(int i=0;i<photos.get(position).tagNames.size();i++){
                        Map<String,String> dataMaps = new HashMap<String,String>(2);
                        dataMaps.put("type",photos.get(position).tagNames.get(i));
                        dataMaps.put("value",photos.get(position).tagValues.get(i));
                        data.add(dataMaps);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(DisplayPhoto.this,data,android.R.layout.simple_expandable_list_item_2,new String[]{"type","value"},new int[]{android.R.id.text1,android.R.id.text2});
                    ListView tags = (ListView) findViewById(R.id.tags);
                    tags.setAdapter(adapter);
                    Button submit = (Button) findViewById(R.id.submit);
                    Button cancel = (Button) findViewById(R.id.cancel);
                    //EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
                    Spinner tagtypes = (Spinner) findViewById(R.id.tagtypes);
                    Button addtag = (Button) findViewById(R.id.addtag);
                    Button deletetag = (Button) findViewById(R.id.deletetag);
                    tagvalue.setText("");
                    submit.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.INVISIBLE);
                    tagvalue.setVisibility(View.INVISIBLE);
                    tagtypes.setVisibility(View.INVISIBLE);
                    addtag.setVisibility(View.VISIBLE);
                    deletetag.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getBaseContext(),"Tag already inserted",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button submit = (Button) findViewById(R.id.submit);
                Button cancel = (Button) findViewById(R.id.cancel);
                EditText tagvalue = (EditText) findViewById(R.id.tagvalue);
                Spinner tagtypes = (Spinner) findViewById(R.id.tagtypes);
                Button addtag = (Button) findViewById(R.id.addtag);
                Button deletetag = (Button) findViewById(R.id.deletetag);
                tagvalue.setText("");
                submit.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                tagvalue.setVisibility(View.INVISIBLE);
                tagtypes.setVisibility(View.INVISIBLE);
                addtag.setVisibility(View.VISIBLE);
                deletetag.setVisibility(View.VISIBLE);
            }
        });


        //

        Button deletetag = (Button) findViewById(R.id.deletetag);
        deletetag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView tags= (ListView) findViewById(R.id.tags);
                tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListView tags= (ListView) findViewById(R.id.tags);
                        String tempType = tags.getItemAtPosition(position).toString().split(",")[0].substring(tags.getItemAtPosition(position).toString().split(",")[0].indexOf("=")+1);
                        String tempValue = tags.getItemAtPosition(position).toString().split(",")[1].substring(tags.getItemAtPosition(position).toString().split(",")[1].indexOf("=")+1,tags.getItemAtPosition(position).toString().split(",")[1].length()-1);
                        //System.out.println(tempType);
                        //System.out.println(tempValue);
                        tags.setOnItemClickListener(null);
                        for(int i=0;i<photos.get(position).tagNames.size();i++){
                            if(photos.get(position).tagNames.get(i).compareTo(tempType)==0){
                                if (photos.get(position).tagValues.get(i).compareTo(tempValue)==0){
                                    photos.get(position).tagNames.remove(i);
                                    photos.get(position).tagValues.remove(i);
                                    break;
                                }
                            }
                        }
                        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
                        for(int i=0;i<photos.get(position).tagNames.size();i++){
                            Map<String,String> dataMaps = new HashMap<String,String>(2);
                            dataMaps.put("type",photos.get(position).tagNames.get(i));
                            dataMaps.put("value",photos.get(position).tagValues.get(i));
                            data.add(dataMaps);
                        }

                        SimpleAdapter adapter = new SimpleAdapter(DisplayPhoto.this,data,android.R.layout.simple_expandable_list_item_2,new String[]{"type","value"},new int[]{android.R.id.text1,android.R.id.text2});
                        //ListView tags = (ListView) findViewById(R.id.tags);
                        tags.setAdapter(adapter);
                    }
                });
            }
        });


    }

    public class ViewPagerAdapter extends PagerAdapter{
        Context context;
        LayoutInflater layoutInflater;
        ArrayList<Photo> photos= DisplayPhoto.photos;

        public ViewPagerAdapter(Context context){
            this.context= context;
        }

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view= layoutInflater.inflate(R.layout.slider,container,false);
            ImageView imageView= (ImageView) view.findViewById(R.id.pic);
            imageView.setImageURI(Uri.parse(photos.get(position).photo));
            ViewPager vp= (ViewPager) container;
            vp.addView(view,0); //here is where we update
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp= (ViewPager) container;
            View view= (View) object;
            vp.removeView(view);
        }
    }

}
