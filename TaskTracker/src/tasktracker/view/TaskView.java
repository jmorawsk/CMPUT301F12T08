package tasktracker.view;

import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.elements.Task;
import tasktracker.model.elements.TextRequirement;
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
	private TextView _status;
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

		_user = "FulfillDebugger";

		// Assign Text fields
		// _name = (TextView) findViewById(R.id.taskName);
		// _creationInfo = (TextView) findViewById(R.id.creationInfo);
		// _description = (TextView) findViewById(R.id.description);
		// _members = (TextView) findViewById(R.id.members);
		// _status = (TextView) findViewById(R.id.status);
		//
		_textRequirement = (Button) findViewById(R.id.button_text);
		_photoRequirement = (Button) findViewById(R.id.button_photo);
		_fulfillment = (Button) findViewById(R.id.fulfillButton);

		// Assign Buttons
		Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
		Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);

		buttonMyTasks.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						TaskListView.class);
				startActivity(intent);
			}
		});

		buttonCreate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						CreateTaskView.class);
				startActivity(intent);
			}
		});

		buttonNotifications.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						NotificationListView.class);
				startActivity(intent);
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
		TextView members = (TextView) findViewById(R.id.members);
		TextView status = (TextView) findViewById(R.id.status);
		TextView creationInfo = (TextView) findViewById(R.id.creationInfo);

		String creator = _cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.USER));
		String date = _cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.DATE));

		boolean fulfilled = _cursor.getInt(_cursor
				.getColumnIndex(DatabaseAdapter.FULFILLED)) == 1;
		_requiresText = _cursor.getInt(_cursor
				.getColumnIndex(DatabaseAdapter.REQS_TEXT)) == 1;
		_requiresPhoto = _cursor.getInt(_cursor
				.getColumnIndex(DatabaseAdapter.REQS_PHOTO)) == 1;

		name.setText(_cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.TASK)));
		description.setText(_cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.TEXT)));
		members.setText(_cursor.getString(_cursor
				.getColumnIndex(DatabaseAdapter.MEMBERS)));
		creationInfo.setText("Created on " + date + " by " + creator + ".");
		status.setText(fulfilled ? "Fulfilled" : "Unfulfilled");

		Button textRequirement = (Button) findViewById(R.id.button_text);
		Button photoRequirement = (Button) findViewById(R.id.button_photo);
		textRequirement.setEnabled(_requiresText);
		photoRequirement.setEnabled(_requiresPhoto);

		if (fulfilled)
			handleFulfilledTask();
		else
			handleUnfulfilledTask();

	}

	protected void onStop() {
		super.onStop();

		_dbHelper.close();
		_cursor.close();
	}

	private void handleFulfilledTask() {

		_fulfillment.setText("View fulfillment report");
		_fulfillment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Show "Ok" button on toast.
				showToast("This task was fulfilled on [date] by [list of fulfillers].");
			}

		});
	}

	private void handleUnfulfilledTask() {

		_fulfillment.setText("Mark as fulfilled");
		_fulfillment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (requirementsFulfilled()) {
					// _task.markAsFulfilled(_user);
					// TODO Update SD
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

	}

	private boolean requirementsFulfilled() {

		boolean ready = true;

		if (_requiresText && (_fulfillmentText == null ||_fulfillmentText.equals(""))) {
			
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
				_fulfillmentText.replaceAll("'", "''"); // For SQL queries

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

	private void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
