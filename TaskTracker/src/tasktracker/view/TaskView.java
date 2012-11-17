package tasktracker.view;

import tasktracker.model.elements.Task;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * And activity that displays an existing task. From here, a user may fulfill a
 * task after completing the requirements.
 * 
 * @author Jeanine Bonot
 * 
 */
public class TaskView extends Activity {

	private Task _task;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);

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

			// Assign Text fields
			TextView name = (TextView) findViewById(R.id.taskName);
			TextView creationInfo = (TextView) findViewById(R.id.creationInfo);
			TextView description = (TextView) findViewById(R.id.description);
			TextView members = (TextView) findViewById(R.id.members);
			
			name.setText(_task.getName());
			description.setText(_task.getDescription());
			members.setText(_task.getMembers());
			creationInfo.setText("Created on " + _task.getDateCreated() + " by "
					+ _task.getCreator());
			
			
			if (_task.isFulfilled())
				handleFulfilledTask();
			else
				handleUnfulfilledTask();

			// Assign CheckBoxes
			Button text = (Button) findViewById(R.id.button_text);
			Button photo = (Button) findViewById(R.id.button_photo);

			text.setEnabled(_task.requiresText());
			photo.setEnabled(_task.requiresPhoto());
			
			// TODO: Handle whether the task is fulfilled.
			
		
	}
	
	private void handleFulfilledTask(){
		
		TextView status = (TextView) findViewById(R.id.status);
		status.setText("Fulfilled");
		
		Button fulfillment = (Button) findViewById(R.id.fulfillButton);
		fulfillment.setText("View fulfillment report");
		fulfillment.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void handleUnfulfilledTask(){
		
		TextView status = (TextView) findViewById(R.id.status);
		status.setText("Unfulfilled");
		
		Button fulfillment = (Button) findViewById(R.id.fulfillButton);
		fulfillment.setText("Mark as fulfilled");
		fulfillment.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	
}
