package jiju.nikhil.photoandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;

public class DisplayPhoto extends AppCompatActivity {
    static ArrayList<Photo> photos= new ArrayList<Photo>();
    static int position;
    static Album currentalbum;
    static Photo currentphoto;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photo);

        Button move = (Button) findViewById(R.id.move);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumList.deletepos= position;
                AlbumList.previous= currentalbum;
                AlbumList.deleted= currentphoto;
                Intent photoIntent= new Intent(getApplicationContext(), AlbumList.class);
                startActivity(photoIntent);
            }
        });

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
                currentphoto= photos.get(position);
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
