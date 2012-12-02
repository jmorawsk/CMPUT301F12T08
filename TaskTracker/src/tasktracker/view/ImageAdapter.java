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
 * A class that contains an array list of photos and returns them to
 * the grid view of photo picker layout.
 * 
 * @author Katherine Jasniewski
 * 
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> photos;
    int count = 0;

    public ImageAdapter(Context c) {
        mContext = c;
        photos = new ArrayList<Bitmap>();
    }
    
    public void addPhoto(Bitmap photo){
    	
    	//photo.compress(format, quality, stream);
    	photos.add(photo);
    	count++;
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
    
    public int getNumber(){
    	
    	return count;
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

    private void getImages(){
    	
    	
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
    
    public Bitmap[] getPhotos(){
    	return photos.toArray(new Bitmap[photos.size()]);
    }
    public ArrayList<Bitmap> getPhotoList(){
    	return photos;
    }
}
