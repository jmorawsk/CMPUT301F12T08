package tasktracker.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A class that shows all photos currently attached to task.
 * Allows user to choose to attach a pre existing photo or take a new photo.
 * 
 * @author Katherine Jasniewski
 * 
 */

public class PhotoPicker extends Activity {
	
	private String[] imageUrls;

	//private DisplayImageOptions options;

	public static final int PICK_PICTURE = 1; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_picker_view);
		
		//initializes buttons on layout
		Button galleryPhoto = (Button) findViewById(R.id.galleryPhoto);
		Button takePhoto = (Button) findViewById(R.id.takeAPhoto);
		
		galleryPhoto.setOnClickListener(new OnClickListener(){


			public void onClick(View v){

				selectPhoto();
			}
		});
		
		//Take a photo option
		OnClickListener retakeListener = new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				takeAPhoto();
			}

		};

		takePhoto.setOnClickListener(retakeListener);

		
		GridView gridView = (GridView) findViewById(R.id.gridView);
		//gridView.setAdapter(new ImageAdapter(this));
		
		//gridView.setOnItemClickListener(new OnItemClickListener() {
//		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//		            Toast.makeText(PhotoPicker.this, "" + position, Toast.LENGTH_SHORT).show();
//		        }
//		    });
		
	}
	
	//User can select a photo from the android gallery
	public void selectPhoto(){
	
		Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		startActivityForResult(intent, PICK_PICTURE);
		
	}
	
	//TODO: Should start the camera class.
	public void takeAPhoto(){
		
		Intent intent = new Intent(getApplicationContext(), Camera.class);
		//startActivityForResult(intent,101);
		startActivity(intent);
	}
	

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	
		switch(requestCode) { 
        case PICK_PICTURE:
            if(resultCode == RESULT_OK){  
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();


                Bitmap theSelectedImage = BitmapFactory.decodeFile(filePath);

                Toast.makeText(PhotoPicker.this, "photo selected", 2000).show();
            }
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
