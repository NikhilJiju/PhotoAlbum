package jiju.nikhil.photoandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import static jiju.nikhil.photoandroid.DisplayPhoto.position;

public class AlbumPhoto extends AppCompatActivity {
    static Album album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }*/
        System.out.println("HFSDKF " + album.photos.size());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_photo);
        setTitle(album.albumName);
        Button submit= (Button) findViewById(R.id.submit);
        Button cancel= (Button) findViewById(R.id.cancel);
        EditText name= (EditText) findViewById(R.id.name);
        submit.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);

        //setting listview
        /*Photo one= new Photo("barnacles",R.drawable.doggy);
        Photo two= new Photo("artists",R.drawable.artist);
        Photo three= new Photo("melo", R.drawable.melo);
        Photo four= new Photo("cousins",R.drawable.cousins);
        album.photos.add(one);
        album.photos.add(two);
        album.photos.add(three);
        album.photos.add(four);*/
        final ListView thephotos= (ListView) findViewById(R.id.thephotos);
        MyAdapter myAdapter= new MyAdapter(AlbumPhoto.this,album.photos);
        thephotos.setAdapter(myAdapter);

        Button rename = (Button) findViewById(R.id.rename);
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button submit= (Button) findViewById(R.id.submit);
                Button cancel= (Button) findViewById(R.id.cancel);
                EditText name= (EditText) findViewById(R.id.name);
                name.setText(album.albumName);
                submit.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                Button rename = (Button) findViewById(R.id.rename);
                rename.setVisibility(View.INVISIBLE);
                Button delete = (Button) findViewById(R.id.delete);
                delete.setVisibility(View.INVISIBLE);

            }
        });

        ////////ADD CODE HERE
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent= new Intent(getApplicationContext(), adding_photo.class);
                startActivity(photoIntent);
                //setContentView(R.layout.album_photo);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button submit= (Button) findViewById(R.id.submit);
                Button cancel= (Button) findViewById(R.id.cancel);
                EditText name= (EditText) findViewById(R.id.name);
                submit.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                Button rename = (Button) findViewById(R.id.rename);
                rename.setVisibility(View.VISIBLE);
                Button delete = (Button) findViewById(R.id.delete);
                delete.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button submit= (Button) findViewById(R.id.submit);
                Button cancel= (Button) findViewById(R.id.cancel);
                EditText name= (EditText) findViewById(R.id.name);
                String renamer = name.getText().toString();
                boolean unoriginal = false;
                int found = -1;
                if (!renamer.equals("")) {
                    for (int i = 0; i < MainActivity.user.albums.size(); i++) {
                        if (MainActivity.user.albums.get(i).albumName.equals(renamer)) {
                            unoriginal = true;
                            AlertDialog alertDialog = new AlertDialog.Builder(AlbumPhoto.this).create();
                            alertDialog.setMessage("This album already exists");
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            break;
                        }
                        if (MainActivity.user.albums.get(i).albumName.equals(album.albumName)) {
                            found = i;
                        }
                    }
                }
                if (!unoriginal) {
                    MainActivity.user.albums.get(found).albumName=renamer;
                    Collections.sort(MainActivity.user.albums, Album.Comparators.NAME);
                    //save action
                    try {
                        File userInfo = new File(MainActivity.fileName);
                        FileOutputStream fileOutputStream = openFileOutput(MainActivity.fileName, Context.MODE_PRIVATE);
                        ObjectOutputStream os= new ObjectOutputStream(fileOutputStream);
                        os.writeObject(MainActivity.user);
                        os.close();
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    setTitle(renamer);
                    submit.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    name.setText("");
                    Button rename = (Button) findViewById(R.id.rename);
                    rename.setVisibility(View.VISIBLE);
                    Button delete = (Button) findViewById(R.id.delete);
                    delete.setVisibility(View.VISIBLE);
                }
            }
        });

        final ListView photos= (ListView) findViewById(R.id.thephotos);
        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DisplayPhoto.photos= album.photos;
                DisplayPhoto.position = position;
                DisplayPhoto.currentalbum= album;
                Intent photoIntent= new Intent(getApplicationContext(), DisplayPhoto.class);
                startActivity(photoIntent);
            }
        });



        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlbumPhoto.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete this album?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        for(int i=0;i<MainActivity.user.albums.size(); i++){
                            if(MainActivity.user.albums.get(i).albumName.equals(album.albumName)){
                                MainActivity.user.albums.remove(i);
                                Intent mainact= new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainact);

                                //save action
                                try {
                                    File userInfo = new File(MainActivity.fileName);
                                    FileOutputStream fileOutputStream = openFileOutput(MainActivity.fileName, Context.MODE_PRIVATE);
                                    ObjectOutputStream os= new ObjectOutputStream(fileOutputStream);
                                    os.writeObject(MainActivity.user);
                                    os.close();
                                }catch (FileNotFoundException e){
                                    e.printStackTrace();
                                }catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.listview_item, parent, false);
                holder.photo = (ImageView) convertView.findViewById(R.id.imageView);
                holder.caption = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            }
            else{
                holder= (ViewHolder) convertView.getTag();
            }
            //System.out.print(photos.get(position).photo);
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                System.out.println("we have permission");
                //String filePath = adding_photo.getPathFromURI(AlbumPhoto.this,Uri.parse(photos.get(position).photo));
                //long size = ThemedSpinnerAdapter.Helper.GetFileSize(getActivity(),filePath);
                System.out.println("THIS CRAZY NUMBER " + photos.get(position));
                File F = new File(photos.get(position).photo);
                holder.photo.setImageURI(Uri.fromFile(F));
            }else{
                System.out.println("PERMISSION IS NOT!");
                ActivityCompat.requestPermissions(AlbumPhoto.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
            //holder.photo.setImageURI(Uri.parse(photos.get(position).photo));
            holder.caption.setText(photos.get(position).name);
            return convertView;
        }

        class ViewHolder{
            ImageView photo;
            TextView caption;
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
