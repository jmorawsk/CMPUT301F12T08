package tasktracker.view;

/**
 * TaskTracker
 * 
 * Copyright 2012 Jeanine Bonot, Michael Dardis, Katherine Jasniewski,
 * Jason Morawski
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

/**
 * TaskTracker
 * 
 * Copyright 2012 Jeanine Bonot, Michael Dardis, Katherine Jasniewski,
 * Jason Morawski
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import tasktracker.controller.TaskController;
import tasktracker.model.WebDBManager;
import tasktracker.model.elements.*;

/**
 * An activity that allows a user to create a task.
 * 
 * @author Jeanine Bonot
 * 
 */
public class CreateTaskView extends Activity {

	private EditText _name;
	private EditText _description;
	private EditText _otherMembers;
	private CheckBox _text;
	private CheckBox _photo;
	private WebDBManager _webManager;

	private String _creator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_view);

		// TODO: Get creator information
		_creator = "Debugger";

		// Initialize our webManager
		_webManager = new WebDBManager();

		// Assign EditText fields
		_name = (EditText) findViewById(R.id.taskName);
		_description = (EditText) findViewById(R.id.editDescription);
		_otherMembers = (EditText) findViewById(R.id.otherMembers);
		_text = (CheckBox) findViewById(R.id.checkBoxText);
		_photo = (CheckBox) findViewById(R.id.checkBoxPhoto);

		// Assign Buttons
		Button saveButton = (Button) findViewById(R.id.saveButton);
		Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
		Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);
		buttonCreate.setEnabled(false);

		buttonMyTasks.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						TaskListView.class);
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

		// Assign listener to Save button
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				if (hasEmptyFields()) {
					showToast("You have empty fields");
					return;
				}

				Task task = createTask();
				if (TaskController.writeFile(task)) {
					showToast("Wrote to file");
				} else {
					showToast("Failed to write to file.");
				}
				
				// Only add to web database if Creator has added members
				String[] members = task.getMemberList();
				if (members.length > 1) {
					_webManager.insertTask(task);
				}

				finish();
			}

		});
	}

	/**
	 * Checks if any of the required fields has been left empty.
	 * 
	 * @return True if a required field has been left empty, false otherwise.
	 */
	private boolean hasEmptyFields() {
		String name = _name.getText().toString();
		String description = _description.getText().toString();
		if (name == "")
			return true;
		if (description == "")
			return true;

		showToast("Name: " + name + "\nDescription: " + description);
		return false;
	}

	/**
	 * Create a task based on the creator's input.
	 * 
	 * @return The task created with the creator's input.
	 */
	private Task createTask() {

		// TODO: Find out how to quickly access user information
		Task task = new Task(_creator);

		task.setDescription(_description.getText().toString());
		task.setName(_name.getText().toString());
		task.setPhotoRequirement(_photo.isChecked());
		task.setTextRequirement(_text.isChecked());
		task.setOtherMembers(_otherMembers.toString());

		return task;
	}

	private void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

}
