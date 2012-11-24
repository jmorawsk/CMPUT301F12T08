package tasktracker.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.elements.Task;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
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
	private Button _fulfillmentButton;
	private Button _expandButton;
	private Button _collapseButton;
	private Button _photoButton;
	private EditText _textFulfillment;
	private ListView _fulfillmentList;
	private ScrollView _scrollview;

	// Task Info
	private long _taskID;
	private String _taskName;
	private String _taskCreator;
	private boolean _requiresText;
	private boolean _requiresPhoto;

	// DB stuff
	private DatabaseAdapter _dbHelper;
	private Cursor _cursor;

	private boolean _hasFulfillments;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);

		_dbHelper = new DatabaseAdapter(this);
		_user = getIntent().getStringExtra("USER");

		_taskID = getIntent().getLongExtra("TASK_ID", -1);

		if (_taskID == -1)
			return;

		_textFulfillment = (EditText) findViewById(R.id.edit_textFulfillment);
		_fulfillmentList = (ListView) findViewById(R.id.list_fulfillments);
		_fulfillmentButton = (Button) findViewById(R.id.fulfillButton);
		_photoButton = (Button) findViewById(R.id.button_photo);
		_scrollview = (ScrollView) findViewById(R.id.scrollview);
		_expandButton = (Button) findViewById(R.id.button_expand);
		_collapseButton = (Button) findViewById(R.id.button_collapse);

		_expandButton.setOnClickListener(new ExpandButtonSetup());
		_collapseButton.setOnClickListener(new CollapseButtonSetup());

		setupToolbarButtons();

		_textFulfillment.addTextChangedListener(new TextFulfillmentSetup());

		_photoButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(PhotoPicker.class);
			}

		});
		
		_fulfillmentButton.setText("Add Fulfillment");
		_fulfillmentButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (requirementsFulfilled()) {
					// _task.markAsFulfilled(_user);

//					sendEmailToCreator();
					String date = new SimpleDateFormat("MMM dd, yyyy | HH:mm")
							.format(Calendar.getInstance().getTime());

					_dbHelper.createFulfillment(_taskName, date, _user,
							_textFulfillment.getText().toString());

					// TODO send report to creator
					showToast("\"" + _taskName + "\" was fulfilled!");

					finish();
				}
			}

		});

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

	protected void onStart() {
		super.onStart();

		_dbHelper.open();

		setTaskInfo();
		setMembersList();
		setFulfillmentsList();
	}

	protected void onStop() {
		super.onStop();

		_dbHelper.close();
		_cursor.close();
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

	private void setTaskInfo() {
		_cursor = _dbHelper.fetchTask(_taskID);

		if (!_cursor.moveToFirst())
			return;
		TextView name = (TextView) findViewById(R.id.taskName);
		TextView description = (TextView) findViewById(R.id.description);
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
		creationInfo
				.setText("Created on " + date + " by " + _taskCreator + ".");

		CheckBox textRequirement = (CheckBox) findViewById(R.id.checkbox_text);
		CheckBox photoRequirement = (CheckBox) findViewById(R.id.checkbox_photo);

		textRequirement.setChecked(_requiresText);
		photoRequirement.setChecked(_requiresPhoto);
	}
	
	private void setFulfillmentsList() {
		_cursor = _dbHelper.fetchFulfillment(_taskName);

		startManagingCursor(_cursor);

		String[] from = new String[] { DatabaseAdapter.TEXT };
		int[] to = new int[] { R.id.text };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.simple_list_item, _cursor, from, to);
		int count = adapter.getCount();
		_hasFulfillments = count > 0;
		Log.d("Has fulfillments", Boolean.toString(_hasFulfillments));
		_fulfillmentList.setAdapter(adapter);
		stopManagingCursor(_cursor);
		
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

	private void handleFulfilledTask(Cursor cursor) {
		final String text = cursor.getString(cursor
				.getColumnIndex(DatabaseAdapter.TEXT));
		final String date = cursor.getString(cursor
				.getColumnIndex(DatabaseAdapter.DATE));

		_fulfillmentButton.setText("View fulfillment report");
		_fulfillmentButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Show "Ok" button on toast.
				showToast("This task was fulfilled on [date] by [list of fulfillers].");
			}

		});
	}

	private void sendEmailToCreator() {

		_cursor = _dbHelper.fetchUser(_taskCreator);

		if (!_cursor.moveToFirst()) {
			showToast("Could not find creator");
			return;
		}
		String email = _cursor.getString(_cursor
				.getColumnIndexOrThrow(DatabaseAdapter.EMAIL));

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		i.putExtra(Intent.EXTRA_SUBJECT,
				"TaskTracker : Task Fulfillment Report");
		i.putExtra(Intent.EXTRA_TEXT, "\"" + _taskName + "\" was fulfilled by "
				+ _user);
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.",
					Toast.LENGTH_SHORT).show();
		}
	}

	private boolean requirementsFulfilled() {

		boolean ready = true;

		if (_requiresPhoto) {
			// TODO: check if text has been input
			if (true) {
				showToast("You must add a photo before marking this task as fulfilled.");
				ready = false;
			}
		}

		return ready;
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

	class TextFulfillmentSetup implements TextWatcher {
		public void afterTextChanged(Editable s) {
			_fulfillmentButton.setEnabled(s.length() > 0);
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Do nothing.

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// Do nothing.

		}
	}

	class ExpandButtonSetup implements OnClickListener {

		public void onClick(View v) {
			_collapseButton.setVisibility(View.VISIBLE);
			if (_hasFulfillments) {
				// TODO if has fulfillments
				_fulfillmentList.setVisibility(View.VISIBLE);
			}
			if (_requiresText) {
				_textFulfillment.setVisibility(View.VISIBLE);
				_textFulfillment.requestFocus();
			}

			if (_requiresPhoto)
				_photoButton.setVisibility(View.VISIBLE);

			_fulfillmentButton.setVisibility(View.VISIBLE);
			_expandButton.setVisibility(View.INVISIBLE);
			_scrollview.post(new Runnable() {

				public void run() {
					_scrollview.fullScroll(ScrollView.FOCUS_DOWN);
				}

			});
		}
	}

	class CollapseButtonSetup implements OnClickListener {

		public void onClick(View v) {
			_expandButton.setVisibility(View.VISIBLE);
			_fulfillmentList.setVisibility(View.GONE);
			_fulfillmentButton.setVisibility(View.GONE);
			_textFulfillment.setVisibility(View.GONE);
			_photoButton.setVisibility(View.GONE);
			_collapseButton.setVisibility(View.GONE);
		}
	}

}
