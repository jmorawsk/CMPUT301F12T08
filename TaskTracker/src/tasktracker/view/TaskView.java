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
		Task task = (Task) getIntent().getSerializableExtra("TASK");

		if (task != null) {

			// Assign Text fields
			TextView name = (TextView) findViewById(R.id.taskName);
			TextView creationInfo = (TextView) findViewById(R.id.creationInfo);
			TextView description = (TextView) findViewById(R.id.description);
			TextView status = (TextView) findViewById(R.id.status);
			TextView members = (TextView) findViewById(R.id.members);

			name.setText(task.getName());
			status.setText(task.isFulfilled() ? "Fulfilled" : "Unfulfilled");
			description.setText(task.getDescription());
			members.setText(task.getMembers());
			creationInfo.setText("Created on " + task.getDateCreated() + " by "
					+ task.getCreator());

			// Assign CheckBoxes
			CheckBox text = (CheckBox) findViewById(R.id.checkBoxText);
			CheckBox photo = (CheckBox) findViewById(R.id.checkBoxPhoto);

			text.setChecked(task.requiresText());
			photo.setChecked(task.requiresPhoto());
			
			// TODO: Handle whether the task is fulfilled.

		}
	}
}
