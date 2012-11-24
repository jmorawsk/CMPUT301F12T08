package tasktracker.view;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class that opens android camera. Takes and saves a photo.
 * 
 * @author Katherine Jasniewski
 * 
 */

public class Camera extends Activity {

	Uri imageFileUri;

	//Button to take a photo initialized
	ImageButton takeAPhotoButton;
	
	String check = "PHOTO";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_photo_view);

		Button acceptButton = (Button) findViewById(R.id.SavePhoto);
		Button retakeButton = (Button) findViewById(R.id.RetakePhoto);
		Button cancelButton = (Button) findViewById(R.id.CancelPhoto);

		takeAPhotoButton = (ImageButton) findViewById(R.id.TakeAPhoto);
		takeAPhotoButton.setOnClickListener(new OnClickListener(){


			public void onClick(View v){

				takePhoto();
			}
		});

		//Photo save button
		OnClickListener saveListener = new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//acceptPhoto();
				 Toast.makeText(Camera.this, "photo saved", 2000).show();
				
				 //Bitmap newPhoto = BitmapFactory.decodeFile(imageFileUri.getPath());
				 Intent intent = new Intent();
				 intent.putExtra("photo", imageFileUri.getPath());
				 //setResult();
				 finish();
			}

		};

		acceptButton.setOnClickListener(saveListener);

		//Photo retake button
		OnClickListener retakeListener = new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(check.equals("PHOTO_TAKEN")){
				File fdelete = new File(imageFileUri.getPath());
				fdelete.delete();
				
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
				
				check = "PHOTO_DELETED";
				}
				takePhoto();
				//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			}

		};

		retakeButton.setOnClickListener(retakeListener); 

		//Photo cancel button
		OnClickListener cancelListener = new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//cancelBogoPic();
				
				File fdelete = new File(imageFileUri.getPath());
				fdelete.delete();
				
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
				
				finish();
			}

		};
		cancelButton.setOnClickListener(cancelListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	//Takes a photo and saves it in a file
	public void takePhoto(){
		
		if(check.equals("PHOTO_TAKEN")){
			File fdelete = new File(imageFileUri.getPath());
			fdelete.delete();
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);

		//if file doesn't exist create it
		if(!folderF.exists()){

			folderF.mkdir();
		}

		//save file with the current time
		String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis() + ".jpg");
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		//intent has information about image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

	}


	protected void onActivityResult(int requestCode, int result, Intent data){
		
		if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){


			if(result == RESULT_OK){
				
				check = "PHOTO_TAKEN";
				ImageButton button = (ImageButton) findViewById(R.id.TakeAPhoto);
				button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
				

			}
			else if(result == RESULT_CANCELED){

				//TODO: Delete a photo that has been taken
				//getContentResolver().delete(imageFileUri, null, null);

			}
			else{

			}
		}

	}

}
