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

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.controller.TaskController;
import tasktracker.model.Preferences;
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
	private CheckBox _private;
	private WebDBManager _webManager;

	private DatabaseAdapter _dbHelper;
	private ToastCreator _toaster;
	private String _user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_view);

		// Initialize our webManager
		_webManager = new WebDBManager();
		_dbHelper = new DatabaseAdapter(this);
		_toaster = new ToastCreator(this);
		_user = Preferences.getUsername(this);

		// Assign EditText fields
		_name = (EditText) findViewById(R.id.taskName);
		_description = (EditText) findViewById(R.id.editDescription);
		_otherMembers = (EditText) findViewById(R.id.otherMembers);
		_text = (CheckBox) findViewById(R.id.checkbox_text);
		_photo = (CheckBox) findViewById(R.id.checkbox_photo);
		_private = (CheckBox) findViewById(R.id.checkbox_private);

		setupToolbarButtons();
		setupSaveButton();
	}

	private void setupToolbarButtons() {
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
	}

	private void setupSaveButton() {

		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				if (hasEmptyFields()) {
					return;
				}

				Task task = createTask();
				List<String> others = task.getOtherMembers();

				// Add to SQL server
				_dbHelper.open();
				_dbHelper.createTask(task);
				
				String taskName = task.getName();
				String message = Notification.getMessage(_user, taskName, Notification.Type.InformMembership);
				
				_dbHelper.createMember(taskName,
						Preferences.getUsername(getBaseContext()));
				_dbHelper.createNotification(taskName, _user, message);

				for (String member : others) {
					_dbHelper.createMember(taskName, member);
					_dbHelper.createNotification(taskName, member, message);
				}
				
				_dbHelper.close();

				// Only add to web database if Creator has added members,
				// otherwise save to SD
				// if (others != null && others.size() > 0) {
				// Log.d("DEBUG", "others.size()\t" + others.size());
				// contactWebserver webRequest = new contactWebserver();
				// webRequest.execute(task);
				// } else {
				// // TaskController.writeFile(task);
				//
				// }

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
		if (_name.getText().toString().matches("")) {
			_toaster.showLongToast("Your task must have a name");
			return true;
		}

		if (_description.getText().toString().matches("")) {
			_toaster.showLongToast("Your task must have a description");
			return true;
		}

		return false;
	}

	/**
	 * Create a task based on the creator's input.
	 * 
	 * @return The task created with the creator's input.
	 */
	private Task createTask() {

		// TODO: Find out how to quickly access user information
		Task task = new Task(Preferences.getUsername(getBaseContext()));

		task.setDescription(_description.getText().toString());
		task.setName(_name.getText().toString());
		task.setPhotoRequirement(_photo.isChecked());
		task.setTextRequirement(_text.isChecked());
		task.setOtherMembers(_otherMembers.getText().toString());

		return task;
	}

	private class contactWebserver extends AsyncTask<Task, Void, Void> {
		@Override
		protected Void doInBackground(Task... tasks) {

			for (Task task : tasks) {
				_webManager.insertTask(task);
			}
			// webTaskList = new ArrayList<Task>();
			// System.out.println("testing");
			// String[][] results = webManager.listTasksAsArrays();
			// String id;
			// Task newTask;
			// for(int n=0; n<results.length; n++)
			// {
			// if(results[n].length>1)
			// {
			// System.out.println("index =" +n);
			// id = results[n][1];
			// newTask = webManager.getTask(id);
			// webTaskList.add(newTask);
			// }
			// }
			return null;
		}
	}

}
