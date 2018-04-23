package jiju.nikhil.photoandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;

public class AlbumPhoto extends AppCompatActivity {
    static Album album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_photo);
        setTitle(album.albumName);
        Button submit= (Button) findViewById(R.id.submit);
        Button cancel= (Button) findViewById(R.id.cancel);
        EditText name= (EditText) findViewById(R.id.name);
        submit.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);

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
}
