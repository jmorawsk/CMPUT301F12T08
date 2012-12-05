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

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.*;
import tasktracker.model.elements.Notification;
import tasktracker.model.elements.RequestCreateNotification;
import tasktracker.model.elements.RequestCreateTask;
import tasktracker.model.elements.Task;

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
	private Button _saveButton;
	// private WebDBManager _webManager;

	private String _user;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_view);

		_user = Preferences.getUsername(this);

		// Assign EditText fields
		_name = (EditText) findViewById(R.id.taskName);
		_description = (EditText) findViewById(R.id.editDescription);
		_otherMembers = (EditText) findViewById(R.id.otherMembers);
		_text = (CheckBox) findViewById(R.id.checkbox_text);
		_photo = (CheckBox) findViewById(R.id.checkbox_photo);
		_private = (CheckBox) findViewById(R.id.checkbox_private);
		_saveButton = (Button) findViewById(R.id.saveButton);

		setupTextFilters();

		_saveButton.setOnClickListener(new SaveOnClickListener());
		_name.addTextChangedListener(new SaveButtonEnabler());
		_description.addTextChangedListener(new SaveButtonEnabler());

		setupToolbarButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.account_menu, menu);

		MenuItem account = menu.findItem(R.id.Account_menu);
		account.setTitle(_user);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.logout:

			Intent intent = new Intent(getApplicationContext(), Login.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupToolbarButtons() {

		Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
		Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);
		buttonCreate.setEnabled(false);

		Context context = getApplicationContext();
		
		buttonMyTasks.setOnClickListener(new ActivityNagivator(context,
				TaskListView.class));
		buttonNotifications.setOnClickListener(new ActivityNagivator(context,
				NotificationListView.class));
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
		task.setIsPrivate(_private.isChecked());
		task.setIsDownloaded("Yes");
		// task.setCreatorID(Preferences.getUserID(getBaseContext()));

		return task;
	}

	/**
	 * Start a new activity while passing the user's information.
	 * 
	 * @param destination
	 *            The activity class destination.
	 */
	private <T extends Activity> void startActivity(Class<T> destination) {
		Intent intent = new Intent(getApplicationContext(), destination);
		startActivity(intent);
	}

	/**
	 * 
	 * @author jbonot
	 * 
	 */
	class SaveOnClickListener implements OnClickListener {

		public void onClick(View v) {

			Task task = createTask();

			RequestCreateTask createTask = new RequestCreateTask(
					getBaseContext(), task);
			// ToastCreator.showLongToast(CreateTaskView.this, "Task created!");
			finish();
			startActivity(TaskListView.class);

		}
	}

	/**
	 * Enables the save button only when the task has a name and a description.
	 * Task name cannot contain only whitespace characters.
	 */
	class SaveButtonEnabler implements TextWatcher {

		public void afterTextChanged(Editable s) {

			_saveButton.setEnabled(_name.getText().length() > 0
					&& _description.getText().length() > 0
					&& !(_name.getText().toString().matches("\\s+")));
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

	class StartActivityHandler<T extends Activity> implements OnClickListener {

		Class<T> destination;

		StartActivityHandler(Class<T> theClass) {
			this.destination = theClass;
		}

		public void onClick(View v) {
			startActivity(this.destination);
		}
	}

	public void setupTextFilters() {
		_name.setFilters(new InputFilter[] { Filters.specialCharFilter() });
		_description
				.setFilters(new InputFilter[] { Filters.specialCharFilter() });
		_otherMembers.setFilters(new InputFilter[] { Filters
				.specialCharFilter() });
	}
}
