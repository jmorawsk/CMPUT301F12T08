package tasktracker.view;

/**
 * TaskTracker
 * 
 * Copyright 2012 Jeanine Bonot, Michael Dardis, Katherine Jasniewski,
 * Jason Morawski
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A class that contains an array list of bitmaps and returns them to
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
		//adds a bitmap to array list of bitmaps
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
			//sets the layout parameters
			imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(0, 0, 0, 0);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageBitmap(photos.get(position));
		return imageView;
	}

	//Compresses the photos bitmaps
	//Compression reduces size for sending through web
	public ArrayList<byte[]> getCompressedPhotos(){
		ArrayList<byte[]> compressed = new ArrayList<byte[]>();
		for(Bitmap photo:photos){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Bitmap scaled = Bitmap.createScaledBitmap(photo, 400, 400, true);
			//compress the bitmap into a JPEG format at 100 to keep quality
			scaled.compress(Bitmap.CompressFormat.JPEG, 100, out);
			//add the scaled image as a byte array to the array list of byte arrays
			compressed.add(out.toByteArray());
		}
		return compressed;

	}
	
	//decompresses the photos
	public void decompressPhotos(ArrayList<byte[]> compressed){
		photos = new ArrayList<Bitmap>();
		for(byte[] photo:compressed){
			//turns the decoded byte array into a bitmap
			photos.add(BitmapFactory.decodeByteArray(photo, 0, photo.length));
			
		}
		
	}

	public Bitmap[] getPhotos(){
		return photos.toArray(new Bitmap[photos.size()]);
	}
	public ArrayList<Bitmap> getPhotoList(){
		return photos;
	}
}
