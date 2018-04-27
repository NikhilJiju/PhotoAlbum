package jiju.nikhil.photoandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jiju.nikhil.photoandroid.MainActivity.fileName;

public class adding_photo extends AppCompatActivity {

    public Uri selectedImage;
    public ArrayList<String> tagNames = new ArrayList<String>();
    public ArrayList<String> tagValues = new ArrayList<String>();
    public String tagtype;
    File F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_photo);


        Button browse = (Button) findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto,1);
            }
        });
        Button add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText path = (EditText) findViewById(R.id.path);
                Photo photo = new Photo(path.getText().toString(),adding_photo.getPathFromURI(adding_photo.this,selectedImage),tagNames,tagValues);
                AlbumPhoto.album.photos.add(photo);
                try {
                    File userInfo = new File(MainActivity.fileName);
                    FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    ObjectOutputStream os= new ObjectOutputStream(fileOutputStream);
                    os.writeObject(MainActivity.user);
                    os.close();
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("LIST OF SIZE IN ADDING IS " + AlbumPhoto.album.photos.size());
                Intent photoIntent= new Intent(getApplicationContext(), AlbumPhoto.class);
                startActivity(photoIntent);
            }
        });
        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView preview = (ImageView) findViewById(R.id.preview);
                EditText path = (EditText) findViewById(R.id.path);
                preview.setImageResource(R.drawable.doggy);
                path.setText("");
                Button add = (Button) findViewById(R.id.add);
                add.setEnabled(false);
            }
        });

        //add options for tag types.
        String[] options = new String[] {"Person:", "Location:"};
        Spinner tagtypes = (Spinner) findViewById(R.id.tagtype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagtypes.setAdapter(adapter);


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

                    SimpleAdapter adapter = new SimpleAdapter(adding_photo.this,data,android.R.layout.simple_expandable_list_item_2,new String[]{"type","value"},new int[]{android.R.id.text1,android.R.id.text2});
                    ListView tags = (ListView) findViewById(R.id.tags);
                    tags.setAdapter(adapter);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(adding_photo.this,android.R.layout.simple_list_item_1,new ArrayList<String>());
                ListView tags = (ListView) findViewById(R.id.tags);
                tags.setAdapter(adapter);
                EditText tagtext = (EditText) findViewById(R.id.tagvalue);
                tagtext.setText("");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK) {
            //Uri selectedImage = imageReturnedIntent.getData();
            selectedImage = imageReturnedIntent.getData();
            boolean duplicate = false;
            for(int x=0;x<AlbumPhoto.album.photos.size();x++){
                if(selectedImage.compareTo(Uri.parse(AlbumPhoto.album.photos.get(x).photo))==0){
                    //duplicate photo
                    duplicate = true;
                    break;
                }
            }
            if(!duplicate) {
                ImageView preview = (ImageView) findViewById(R.id.preview);
                EditText path = (EditText) findViewById(R.id.path);
                System.out.println(selectedImage.getPath());
                path.setText(selectedImage.getPath().substring(selectedImage.getPath().lastIndexOf('/'), selectedImage.getPath().length()));
                //preview.setImageURI(selectedImage);
                String testing = selectedImage.getPath();


//                System.out.println(testing.substring(testing.indexOf("content")));
  //              File n = new File(testing.substring(testing.indexOf("content")));
                String filePath = adding_photo.getPathFromURI(this,selectedImage);
                //long size = ThemedSpinnerAdapter.Helper.GetFileSize(getActivity(),filePath);
                F = new File(filePath);
                System.out.println("--------------||||"+filePath+"||||------------------");
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    System.out.println("PERMISSION IS GRANTED CORRECTLY!");
                    System.out.println("Permission GRANTED again");
                    preview.setImageURI(Uri.fromFile(F));
                }else{
                    System.out.println("PERMISSION IS NOT!");
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }

               // Bitmap showBitmap = BitmapFactory.decodeFile(F.getAbsolutePath());
                //preview.setImageBitmap(showBitmap);
                Button add = (Button) findViewById(R.id.add);
                add.setEnabled(true);
            }else{
                //don't add photo
                Toast.makeText(getBaseContext(),"Duplicate Photo. Select Another",Toast.LENGTH_SHORT).show();
            }
        }
    }

    static String getPathFromURI(Context context, Uri contentUri) {
        Cursor mediaCursor = null;
        try {
            String[] dataPath = { MediaStore.Images.Media.DATA };
            mediaCursor = context.getContentResolver().query(contentUri,  dataPath, null, null, null);
            int column_index = mediaCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            mediaCursor.moveToFirst();
            return mediaCursor.getString(column_index);
        } finally {
            if (mediaCursor != null) {
                mediaCursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(requestCode==0){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                System.out.println("Permission GRANTED again");
                ImageView preview = (ImageView) findViewById(R.id.preview);
                preview.setImageURI(Uri.fromFile(F));
            }else{
                System.out.println("Permission denied again");
            }
        }
    }

}