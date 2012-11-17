package tasktracker.view;

import tasktracker.model.elements.Task;
import tasktracker.model.elements.TextRequirement;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

	private Task _task;
	private String _user;
	private TextView _name;
	private TextView _creationInfo;
	private TextView _description;
	private TextView _members;
	private TextView _status;
	private Button _textRequirement;
	private Button _photoRequirement;
	private Button _fulfillment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);

		_user = "FulfillDebugger";

		// Assign Text fields
		_name = (TextView) findViewById(R.id.taskName);
		_creationInfo = (TextView) findViewById(R.id.creationInfo);
		_description = (TextView) findViewById(R.id.description);
		_members = (TextView) findViewById(R.id.members);
		_status = (TextView) findViewById(R.id.status);

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
		_task = (Task) getIntent().getSerializableExtra("TASK");

		if (_task == null)
			return;

		_name.setText(_task.getName());
		_description.setText(_task.getDescription());
		_members.setText(_task.getMembers());
		_creationInfo.setText("Created on " + _task.getDateCreated() + " by "
				+ _task.getCreator());

		if (_task.isFulfilled())
			handleFulfilledTask();
		else
			handleUnfulfilledTask();

		_textRequirement.setEnabled(_task.requiresText());
		_photoRequirement.setEnabled(_task.requiresPhoto());

		// TODO: Handle whether the task is fulfilled.

	}

	private void handleFulfilledTask() {

		_status.setText("Fulfilled");
		_fulfillment.setText("View fulfillment report");
		
		
		_fulfillment.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Show "Ok" button on toast.
				showToast("This task was fulfilled on [date] by "
						+ _task.getFulfiller() + ".");
			}

		});
	}

	private void handleUnfulfilledTask() {

		_status.setText("Unfulfilled");

		_fulfillment.setText("Mark as fulfilled");
		_fulfillment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (requirementsFulfilled()) {
					_task.markAsFulfilled(_user);
					// TODO Update SD
					showToast("\"" + _task.getName() + "\" was fulfilled!");

					finish();
				}
			}

		});

		final Dialog textRequirementDialog = textRequirementDialog();
		_textRequirement.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				textRequirementDialog.show();
			}
			
		});

	}

	private boolean requirementsFulfilled() {

		boolean ready = true;

		if (_task.requiresText()) {
			// TODO: check if text has been input
			if (_task.getText() == null) {
				showToast("You must add text before marking this task as fulfilled.");
				ready = false;
			}
		}

		if (_task.requiresPhoto()) {
			// TODO: check if text has been input
			if (true) {
				showToast("You must add a photo before marking this task as fulfilled.");
				ready = false;
			}
		}

		return ready;
	}
	
	private Dialog textRequirementDialog(){
		final EditText input = new EditText(this);
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setView(input);
		dialog.setTitle(_task.getName());
		dialog.setMessage("Enter you fulfillment text.");
		dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				String text = input.getText().toString();

				text.replaceAll("'", "''"); // For SQL queries
				
				// TODO Save text
				_task.setText(new TextRequirement(text));
				
				// TODO save on SD/web db
			}
		});
		
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

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
