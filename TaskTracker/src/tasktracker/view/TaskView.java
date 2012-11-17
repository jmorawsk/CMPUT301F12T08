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

	private TextView _name;
	private TextView _description;
	private TextView _members;
	private CheckBox _text;
	private CheckBox _photo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);

		Task task = (Task) getIntent().getSerializableExtra("TASK");

		if (task != null) {

			// Assign EditText fields
			_name = (TextView) findViewById(R.id.taskName);
			_name.setText(task.getName());

			_description = (TextView) findViewById(R.id.description);
			_description.setText(task.getDescription());

			_members = (TextView) findViewById(R.id.members);
			// TODO: Get other members (from database?). Need to parse into a
			// string.

			_text = (CheckBox) findViewById(R.id.checkBoxText);
			_text.setChecked(task.requiresText());

			_photo = (CheckBox) findViewById(R.id.checkBoxPhoto);
			_photo.setChecked(task.requiresPhoto());
			
		}

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
}
