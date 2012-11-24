package tasktracker.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.elements.Notification;
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
import android.widget.SimpleCursorAdapter.ViewBinder;

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

	private ToastCreator _toaster;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);

		_dbHelper = new DatabaseAdapter(this);

		_user = getIntent().getStringExtra("USER");
		_taskID = getIntent().getLongExtra("TASK_ID", -1);

		if (_taskID == -1)
			return;

		_toaster = new ToastCreator(this);
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
				if (requirementsFulfilled()) {

					String message = Notification.getMessage(_user, _taskName,
							Notification.Type.FulfillmentReport);
					// sendEmailToCreator(message);
					String date = new SimpleDateFormat("MMM dd, yyyy | HH:mm")
							.format(Calendar.getInstance().getTime());

					long id = _dbHelper.createFulfillment(_taskName, date,
							_user, _textFulfillment.getText().toString());

					Log.d("Task Fulfillment",
							"fulfillment id: " + Long.toString(id));

					_dbHelper.createNotification(_taskName, _taskCreator,
							message);

					// TODO send report to creator
					_toaster.showLongToast("\"" + _taskName
							+ "\" was fulfilled!");

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
			_toaster.showLongToast("Edit");
			return true;

		case R.id.menu_delete:
			_toaster.showLongToast("Delete");
			return true;
		case R.id.menu_logout:
			_user = null;
			_toaster.showLongToast("Logged out");
			startActivity(Login.class);

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

	/**
	 * Fills the layout views with information on the task.
	 */
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

	/**
	 * Sets the fulfillment list with data from the SQL database.
	 */
	private void setFulfillmentsList() {
		_cursor = _dbHelper.fetchFulfillment(_taskName);

		startManagingCursor(_cursor);

		String[] from = new String[] { DatabaseAdapter.USER,
				DatabaseAdapter.TEXT };
		int[] to = new int[] { R.id.text };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.simple_list_item, _cursor, from, to);
		int count = adapter.getCount();
		_hasFulfillments = count > 0;
		Log.d("Task Fulfillment",
				"number of fulfillments = " + Integer.toString(count));

		adapter.setViewBinder(new ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {

				if (true) {
					String fulfiller = cursor.getString(cursor
							.getColumnIndex(DatabaseAdapter.USER));
					String date = cursor.getString(cursor
							.getColumnIndex(DatabaseAdapter.DATE));
					TextView textView = (TextView) view;
					textView.setText("Fulfilled by " + fulfiller + " on "
							+ date);
					return true;
				}

				return false;
			}
		});
		_fulfillmentList.setAdapter(adapter);
		stopManagingCursor(_cursor);

	}

	/**
	 * Sets the member list with data from the SQL database.
	 */
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

	/**
	 * Send an email report of the task fulfillment to the creator;
	 */
	private void sendEmailToCreator(String message) {

		_cursor = _dbHelper.fetchUser(_taskCreator);

		if (!_cursor.moveToFirst()) {
			_toaster.showLongToast("Could not find creator information");
			return;
		}

		String email = _cursor.getString(_cursor
				.getColumnIndexOrThrow(DatabaseAdapter.EMAIL));

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		i.putExtra(Intent.EXTRA_SUBJECT,
				"TaskTracker : Task Fulfillment Report");
		i.putExtra(Intent.EXTRA_TEXT, message);
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "There are no email clients installed.",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Soon to be deleted. Makes sure that the requirements have been completed
	 * before fulfilling a task.
	 * 
	 * @return True if the requirements we fulfilled; otherwise false.
	 */
	private boolean requirementsFulfilled() {

		boolean ready = true;

		if (_requiresPhoto) {
			// TODO: check if text has been input
			if (true) {
				_toaster.showLongToast("You must add a photo before marking this task as fulfilled.");
				ready = false;
			}
		}

		return ready;
	}

	/**
	 * Start a new activity while passing the user's information.
	 * 
	 * @param destination
	 *            The activity class destination.
	 */
	private <T extends Activity> void startActivity(Class<T> destination) {
		Intent intent = new Intent(getApplicationContext(), destination);
		intent.putExtra("USER", _user);
		startActivity(intent);
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

	/**
	 * OnClickListener for the expand button. When the user clicks the expand
	 * button, the fields that are required for task fulfillment are displayed,
	 * as well as the fulfillment button.
	 */
	class ExpandButtonSetup implements OnClickListener {

		/**
		 * Sets views a visible if they are required for fulfillment.
		 */
		public void onClick(View v) {
			_collapseButton.setVisibility(View.VISIBLE);
			_expandButton.setVisibility(View.GONE);

			Log.d("Task Fulfillment",
					"_hasFulfillments = " + Boolean.toString(_hasFulfillments));

			if (_hasFulfillments) {
				_fulfillmentList.setVisibility(View.VISIBLE);
			}

			if (_requiresText) {
				_textFulfillment.setVisibility(View.VISIBLE);
				_textFulfillment.requestFocus();
			}

			if (_requiresPhoto)
				_photoButton.setVisibility(View.VISIBLE);

			_fulfillmentButton.setVisibility(View.VISIBLE);

			// Scroll down so the user can quickly complete the task
			// requirements.
			_scrollview.post(new Runnable() {

				public void run() {
					_scrollview.fullScroll(ScrollView.FOCUS_DOWN);
				}

			});
		}
	}

	/**
	 * OnClickListener for the collapse button. Hides all views that are related
	 * to task fulfillment.
	 */
	class CollapseButtonSetup implements OnClickListener {

		public void onClick(View v) {
			_expandButton.setVisibility(View.VISIBLE);
			_collapseButton.setVisibility(View.GONE);
			_fulfillmentList.setVisibility(View.GONE);
			_fulfillmentButton.setVisibility(View.GONE);
			_textFulfillment.setVisibility(View.GONE);
			_photoButton.setVisibility(View.GONE);
		}
	}

}
