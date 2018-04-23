package jiju.nikhil.photoandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    static String fileName= "theuser.bin";
    static User user;
    Album thisAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(MainActivity.class.getName(),"boo");
        //initialize user
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            ObjectInputStream in= new ObjectInputStream(fileInputStream);
			user= (User) in.readObject();
			in.close();
		}catch (ClassNotFoundException c){
			c.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user!=null){
            ListView useralbums= (ListView) findViewById(R.id.albums);
            ListAdapter albumadapter= new ArrayAdapter<Album>(MainActivity.this, android.R.layout.simple_list_item_1, user.albums);
            useralbums.setAdapter(albumadapter);
        }
        else{
            user= new User();
        }
        Button open = (Button) findViewById(R.id.open);
        open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent= new Intent(getApplicationContext(), AlbumPhoto.class);
                startActivity(photoIntent);
                //setContentView(R.layout.album_photo);
            }
        });

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText albumname= (EditText) findViewById(R.id.albumname);
                String name;
                if(albumname.getText()==null){
                    name= "";
                }
                else{
                    name= albumname.getText().toString();
                }
                boolean unoriginal= false;
                thisAlbum= new Album(name);
                //Check to see if song has already been added before
                for (int i=0; i<user.albums.size(); i++){
                    if(user.albums.get(i).albumName.equals(name)){
                        unoriginal=true;
                        //insert alert here
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
                }
                if(!unoriginal && !name.equals("")){
                    //Alert alert = new Alert(AlertType.CONFIRMATION, "Add this album: " + aname + "?", ButtonType.YES, ButtonType.NO);
                    //alert.showAndWait();

                    //if (alert.getResult() == ButtonType.YES) {
                      //  System.out.println(thisAlbum);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure?");
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            dialog.dismiss();
                            user.albums.add(thisAlbum);
                            Collections.sort(user.albums,Album.Comparators.NAME);
                            ListView useralbums= (ListView) findViewById(R.id.albums);
                            ListAdapter albumadapter= new ArrayAdapter<Album>(MainActivity.this, android.R.layout.simple_list_item_1, user.albums);
                            useralbums.setAdapter(albumadapter);
                            albumname.setText("");
                            try {
                                File userInfo = new File(fileName);
                                FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                ObjectOutputStream os= new ObjectOutputStream(fileOutputStream);
                                os.writeObject(user);
                                os.close();
                            }catch (FileNotFoundException e){
                                e.printStackTrace();
                            }catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                    //}
                }
                /*if(name!=null && name!="") {
                    Album album = new Album(name);
                    MainActivity.user.albums.add(album);
                    //Log.d("album",album.toString());
                    ListView useralbums= (ListView) findViewById(R.id.albums);
                    ListAdapter albumadapter= new ArrayAdapter<Album>(MainActivity.this, android.R.layout.simple_list_item_1, user.albums);
                    useralbums.setAdapter(albumadapter);
                }*/
            }
        });

        final ListView useralbums= (ListView) findViewById(R.id.albums);
        useralbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumPhoto.album= (Album) useralbums.getItemAtPosition(position);
                Intent photoIntent= new Intent(getApplicationContext(), AlbumPhoto.class);
                startActivity(photoIntent);
            }
        });

        Button search = (Button) findViewById(R.id.search);
        Button rename = (Button) findViewById(R.id.rename);
        Button delete = (Button) findViewById(R.id.delete);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            ObjectInputStream in= new ObjectInputStream(fileInputStream);
            user= (User) in.readObject();
            in.close();
        }catch (ClassNotFoundException c){
            c.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(user!=null){
            ListView useralbums= (ListView) findViewById(R.id.albums);
            ListAdapter albumadapter= new ArrayAdapter<Album>(MainActivity.this, android.R.layout.simple_list_item_1, user.albums);
            useralbums.setAdapter(albumadapter);
        }
    }




}