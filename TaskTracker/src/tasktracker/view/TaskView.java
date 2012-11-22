package tasktracker.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tasktracker.controller.DatabaseAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * And activity that displays an existing task. From here, a user may fulfill a
 * task after completing the requirements.
 * 
 * @author Jeanine Bonot
 * 
 */
public class TaskView extends Activity {

	// The current user
	private String _user;

	// Activity Items
	private Button _textRequirement;
	private Button _photoRequirement;
	private Button _fulfillment;

	// Task Info
	private long _taskID;
	private String _taskName;
	private String _taskCreator;
	private boolean _requiresText;
	private boolean _requiresPhoto;

	// Task Fulfillment
	private String _fulfillmentText;

	// DB stuff
	private DatabaseAdapter _dbHelper;
	private Cursor _cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);

		_user = getIntent().getStringExtra("USER");

		_textRequirement = (Button) findViewById(R.id.button_text);
		_photoRequirement = (Button) findViewById(R.id.button_photo);
		_fulfillment = (Button) findViewById(R.id.fulfillButton);

		setupToolbarButtons();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.task, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_edit:
			showToast("Edit");
			return true;

		case R.id.menu_delete:
			showToast("Delete");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
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

	protected void onStart() {
		super.onStart();

		_taskID = getIntent().getLongExtra("TASK_ID", -1);

		if (_taskID == -1)
			return;

		_dbHelper = new DatabaseAdapter(this);
		_dbHelper.open();
		_cursor = _dbHelper.fetchTask(_taskID);

		if (!_cursor.moveToFirst())
			return;

		TextView name = (TextView) findViewById(R.id.taskName);
		TextView description = (TextView) findViewById(R.id.description);
		// TextView members = (TextView) findViewById(R.id.members);
		TextView status = (TextView) findViewById(R.id.status);
		TextView creationInfo = (TextView) findViewById(R.id.creationInfo);

		_taskCreator = _cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.USER));
		String date = _cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.DATE));

		_requiresText = _cursor.getInt(_cursor
				.getColumnIndex(DatabaseAdapter.REQS_TEXT)) == 1;
		_requiresPhoto = _cursor.getInt(_cursor
				.getColumnIndex(DatabaseAdapter.REQS_PHOTO)) == 1;
		_taskName = _cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.TASK));

		name.setText(_taskName);
		description.setText(_cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.TEXT)));
		// members.setText(_cursor.getString(_cursor
		// .getColumnIndex(DatabaseAdapter.MEMBERS)));
		creationInfo
				.setText("Created on " + date + " by " + _taskCreator + ".");

		Button textRequirement = (Button) findViewById(R.id.button_text);
		Button photoRequirement = (Button) findViewById(R.id.button_photo);
		textRequirement.setEnabled(_requiresText);
		photoRequirement.setEnabled(_requiresPhoto);

		setMembersList();

		Cursor cursor = _dbHelper.fetchFulfillment(_taskName);
		boolean fulfilled = cursor.moveToFirst();

		status.setText(fulfilled ? "Fulfilled" : "Unfulfilled");
		if (fulfilled)
			handleFulfilledTask(cursor);
		else
			handleUnfulfilledTask();

		cursor.close();

	}

	private void setMembersList() {
		ListView members = (ListView) findViewById(R.id.membersList);
		_cursor = _dbHelper.fetchTaskMembers(_taskName);

		startManagingCursor(_cursor);

		String[] from = new String[] { DatabaseAdapter.USER };
		int[] to = new int[] { R.id.text };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.simple_list_item, _cursor, from, to);
		int count = adapter.getCount();
		members.setAdapter(adapter);
		stopManagingCursor(_cursor);
		TextView headline = (TextView) findViewById(R.id.heading_Members);

		if (count == 1) {
			headline.setText(count + " Member:");
		} else {
			headline.setText(count + " Members:");
		}
	}

	protected void onStop() {
		super.onStop();

		_dbHelper.close();
		_cursor.close();
	}

	private void handleFulfilledTask(Cursor cursor) {
		final String text = cursor.getString(cursor
				.getColumnIndex(DatabaseAdapter.TEXT));
		final String date = cursor.getString(cursor
				.getColumnIndex(DatabaseAdapter.DATE));

		_fulfillment.setText("View fulfillment report");
		_textRequirement.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showToast(text + "\n\n" + date);
			}

		});

		_fulfillment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Show "Ok" button on toast.
				showToast("This task was fulfilled on [date] by [list of fulfillers].");
			}

		});
	}

	
	private void sendEmail() {

		_cursor = _dbHelper.fetchUser(_taskCreator);
		
		if (!_cursor.moveToFirst()) {
			showToast("Could not find creator");
			return;
		}
		String email = _cursor.getString(_cursor
				.getColumnIndexOrThrow(DatabaseAdapter.EMAIL));

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
		i.putExtra(Intent.EXTRA_SUBJECT, "TaskTracker : Task Fulfillment Report");
		i.putExtra(Intent.EXTRA_TEXT   , "\"" + _taskName
				+ "\" was fulfilled by " + _user);
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

	private void handleUnfulfilledTask() {

		_fulfillment.setText("Mark as fulfilled");
		_fulfillment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (requirementsFulfilled()) {
					// _task.markAsFulfilled(_user);

						sendEmail();
						String date = new SimpleDateFormat(
								"MMM dd, yyyy | HH:mm").format(Calendar
								.getInstance().getTime());

					 _dbHelper.createFulfillment(_taskName, date, _user,
					 _fulfillmentText);
					
					 // TODO send report to creator
					 showToast("\"" + _taskName + "\" was fulfilled!");

						finish();
				}
			}

		});

		final Dialog textRequirementDialog = textRequirementDialog();
		_textRequirement.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				textRequirementDialog.show();
			}

		});

		_photoRequirement.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(Camera.class);
			}

		});

	}

	private boolean requirementsFulfilled() {

		boolean ready = true;

		if (_requiresText
				&& (_fulfillmentText == null || _fulfillmentText.equals(""))) {

			showToast("You must add text before marking this task as fulfilled.");
			ready = false;

		}

		if (_requiresPhoto) {
			// TODO: check if text has been input
			if (true) {
				showToast("You must add a photo before marking this task as fulfilled.");
				ready = false;
			}
		}

		return ready;
	}

	private Dialog textRequirementDialog() {
		final EditText input = new EditText(this);
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setView(input);
		dialog.setTitle(_taskName);
		dialog.setMessage("Enter you fulfillment text.");
		dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				_fulfillmentText = input.getText().toString();

				// TODO save on SD/web db
			}
		});

		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}

				});

		return dialog.create();
	}

	private void startActivity(Class<?> destination) {
		Intent intent = new Intent(getApplicationContext(), destination);
		intent.putExtra("USER", _user);
		startActivity(intent);
	}

	private void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
