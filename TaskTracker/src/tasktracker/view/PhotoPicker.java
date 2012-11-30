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
import android.widget.AdapterView.OnItemClickListener;
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
	private ImageAdapter myAdapter = new ImageAdapter(this);
	private GridView gridView;
	//private DisplayImageOptions options;

	public static final int PICK_PICTURE_FROM_GALLERY = 1; 
	public static final int TAKE_PICTURE = 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_picker_view);

		//initializes buttons on layout
		Button galleryPhoto = (Button) findViewById(R.id.galleryPhoto);
		Button takePhoto = (Button) findViewById(R.id.takeAPhoto);

		setupToolbarButtons();

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




		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(myAdapter);

		//		gridView.setAdapter(new ImageAdapter(this));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(PhotoPicker.this, "" + position, Toast.LENGTH_SHORT).show();

			}
		});

	}

	private void setupToolbarButtons() {
		// Assign Buttons
		Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
		Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);

		buttonMyTasks.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(TaskListView.class);
			}
		});

		buttonCreate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(CreateTaskView.class);
			}
		});

		buttonNotifications.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(NotificationListView.class);
			}
		});
	}

	private void startActivity(Class<?> destination) {
		Intent intent = new Intent(getApplicationContext(), destination);
		startActivity(intent);
	}

	//User can select a photo from the android gallery
	public void selectPhoto(){

		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(intent, PICK_PICTURE_FROM_GALLERY);

	}

	//TODO: Should start the camera class.
	public void takeAPhoto(){

		Intent intent = new Intent(getApplicationContext(), Camera.class);
		startActivityForResult(intent,TAKE_PICTURE);
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Toast.makeText(PhotoPicker.this, "Res: "+resultCode + " Req: "+requestCode + "Data: " +data, 2000).show();
		if (data!=null){

			//Toast.makeText(PhotoPicker.this, "B", 2000).show();
			//super.onActivityResult(requestCode, resultCode, data);


			switch(requestCode) { 
			case PICK_PICTURE_FROM_GALLERY:{
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
					myAdapter.addPhoto(theSelectedImage);

					gridView.setAdapter(myAdapter);
				}
				break;

			}

			case TAKE_PICTURE:{

				Toast.makeText(PhotoPicker.this, "B", 2000).show();
				Toast.makeText(PhotoPicker.this, resultCode, 2000).show();
				if(resultCode == RESULT_OK){

					Toast.makeText(PhotoPicker.this, "Reached", 2000).show();

					String path = data.getExtras().getString("photo");
					Bitmap newPhoto = BitmapFactory.decodeFile(path);
					myAdapter.addPhoto(newPhoto);

					gridView.setAdapter(myAdapter);
				}
			}
			}
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
