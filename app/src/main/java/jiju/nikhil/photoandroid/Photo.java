package jiju.nikhil.photoandroid;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jiju on 4/21/2018.
 */

public class Photo implements Serializable{

    private static final long serialVersionUID = -6601624970743555402L;

    public String name;
    public String photo;
    ArrayList<String> tagNames;
    ArrayList<String> tagValues;

    public Photo(String name, String photo,ArrayList<String> tagNames,ArrayList<String> tagValues){
        this.name= name;
        this.photo=photo;
        this.tagNames = tagNames;
        this.tagValues = tagValues;
    }

    public String toString(){
        return name;
    }

}
