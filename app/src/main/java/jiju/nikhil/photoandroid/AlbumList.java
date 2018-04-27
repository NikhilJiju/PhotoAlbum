package jiju.nikhil.photoandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class AlbumList extends AppCompatActivity {
    static int deletepos;
    static Album previous;
   // static Photo deleted; //previous.photos.get(deletepos);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        final ListView albumlist= (ListView) findViewById(R.id.albumlist);
        ListAdapter albumadapter= new ArrayAdapter<Album>(AlbumList.this, android.R.layout.simple_list_item_1, MainActivity.user.albums);
        albumlist.setAdapter(albumadapter);
        albumlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlbumList.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to move this photo?");
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
                        boolean unoriginal= false;
                        Album next= MainActivity.user.albums.get(position);
                        for(int i=0; i<next.photos.size(); i++){
                            if(previous.photos.get(deletepos).photo.equals(next.photos.get(i).photo)){
                                unoriginal= true;
                                AlertDialog alertDialog = new AlertDialog.Builder(AlbumList.this).create();
                                alertDialog.setMessage("This photo already exists in selected Album");
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                        if(!unoriginal){
                            next.photos.add(previous.photos.get(deletepos));
                            previous.photos.remove(deletepos);
                            //System.out.println("CHECK THIS OUT " + deleted);
                            AlbumPhoto.album= next;
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
                            Intent photoIntent= new Intent(getApplicationContext(), AlbumPhoto.class);
                            startActivity(photoIntent);
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent= new Intent(getApplicationContext(), DisplayPhoto.class);
                startActivity(photoIntent);
            }
        });

    }
}
