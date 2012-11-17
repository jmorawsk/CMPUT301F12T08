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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoPicker extends Activity {

	public static final int PICK_PICTURE = 1; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_picker_view);
		
		Button galleryPhoto = (Button) findViewById(R.id.galleryPhoto);
		Button takePhoto = (Button) findViewById(R.id.takeAPhoto);
		
		galleryPhoto.setOnClickListener(new OnClickListener(){


			public void onClick(View v){

				selectPhoto();
			}
		});
		
		//galleryPhoto.setOnClickListener(saveListener);
		
		//Take a photo option
		OnClickListener retakeListener = new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//cancelBogoPic();
				takeAPhoto();
			}

		};

		takePhoto.setOnClickListener(retakeListener);

	}
	
	public void selectPhoto(){
	
		Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		startActivityForResult(intent, PICK_PICTURE);
		
		//intent.setType("image/*");
		//intent.setAction(Intent.ACTION_GET_CONTENT);
		
		//startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PICTURE);
		
	}
	
	public void takeAPhoto(){
		
		Intent intent = new Intent(getApplicationContext(), PhotoPicker.class);
		startActivityForResult(intent,101);
		
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


                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

                Toast.makeText(PhotoPicker.this, "selected", 2000).show();
            }
		}
//		
		//switch (requestCode) {

//		case 1:
//		{
//
//			if (resultCode == RESULT_OK)
//			{
//				Uri photoUri = data.getData();
//
//				if (photoUri != null)
//				{
//
//					try {
//						String[] filePathColumn = {MediaStore.Images.Media.DATA};
//						Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null); 
//						cursor.moveToFirst();
//
//						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//
//						String filePath = cursor.getString(columnIndex);
//						cursor.close();
//
//						Bitmap bMap = BitmapFactory.decodeFile(filePath);
//
//
//						ImageView img = new ImageView(this);
//						img.setImageBitmap(bMap);
//
//
//					}catch(Exception e)
//					{}
//				}
//			}// resultCode
//		}// case 1
//		}// switch, request code
	}// public void onActivityResult


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
