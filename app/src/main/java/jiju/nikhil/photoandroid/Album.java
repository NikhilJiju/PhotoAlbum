package jiju.nikhil.photoandroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Jiju on 4/21/2018.
 */

public class Album implements Serializable{

    private static final long serialVersionUID = -1503934156878954986L;

    String albumName;
    /**
     * list of photos which the album contains
     */
    ArrayList<Photo> photos;
    /**
     * constructor for creating an album object with a given name
     * @param name
     */
    public Album(String name){
        albumName= name;
    }

    /**
     * method for giving a string representation which is used by the Observable list
     */
    public String toString(){
     /*   int size;
        Calendar min= null;
        Calendar max= null;
        String more;
        if(photos==null || photos.size()==0){
            size= 0;
            more="";
        }
        else{
            size= photos.size();
            for(int i=0; i<photos.size(); i++){
                if(min==null){
                    min= photos.get(i).date;
                }
                if(max==null){
                    max= photos.get(i).date;
                }
                if(photos.get(i).date.before(min)){
                    min= photos.get(i).date;
                }
                if(photos.get(i).date.after(max)){
                    max= photos.get(i).date;
                }
            }
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            more= "\nFrom: " + min.getTime() + " to " + max.getTime();
        }
        String representation= albumName + "\n" + "Photos: " + size + more;
        return representation;*/
        return albumName;
    }

    public int compareTo(Album album){
        return this.albumName.compareTo(album.albumName);
    }

    /**
     * used by Observable list for sorting
     *
     */
    public static class Comparators{
        public static Comparator<Album> NAME= new Comparator<Album>(){
            @Override
            public int compare(Album a, Album b){
                return a.albumName.compareTo(b.albumName);
            }
        };
    }

}
