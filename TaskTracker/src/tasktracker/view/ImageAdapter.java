package tasktracker.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A class that contains a list of photos and places them in
 * the grid view of photo picker layout.
 * 
 * @author Katherine Jasniewski
 * 
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> photos;

    public ImageAdapter(Context c) {
        mContext = c;
        photos = new ArrayList<Bitmap>();
    }
    
    public void addPhoto(Bitmap photo){
    	photos.add(photo);
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(photos.get(position));
        return imageView;
    }

    private void get_images(){
//        File directory = new File(Variables.PATH_FOTOS);   
//
//        File[] archivos =directory.listFiles();
//        mis_fotos= new Bitmap[archivos.length];
//
//        for (int cont=0; cont<archivos.length;cont++){
//
//            File imgFile = new  File(archivos[cont].toString());                
//            mis_fotos[cont] = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//        }   
    }

    // references to our images
//    private Integer[] mThumbIds = {
//            R.drawable.sample_2, R.drawable.sample_3,
//            R.drawable.sample_4, R.drawable.sample_5,
//            R.drawable.sample_6, R.drawable.sample_7,
//            R.drawable.sample_0, R.drawable.sample_1,
//            R.drawable.sample_2, R.drawable.sample_3,
//            R.drawable.sample_4, R.drawable.sample_5,
//            R.drawable.sample_6, R.drawable.sample_7,
//            R.drawable.sample_0, R.drawable.sample_1,
//            R.drawable.sample_2, R.drawable.sample_3,
//            R.drawable.sample_4, R.drawable.sample_5,
//            R.drawable.sample_6, R.drawable.sample_7
//    };

}
