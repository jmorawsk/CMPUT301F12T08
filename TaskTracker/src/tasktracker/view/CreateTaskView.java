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
import java.util.*;

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

	private static final TaskController TASK_MANAGER = new TaskController();

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
		_creator = "DebugginCreator";
		
		// Initialize our webManager
		_webManager = new WebDBManager();

		// Assign EditText fields
		_name = (EditText) findViewById(R.id.taskName);
		_description = (EditText) findViewById(R.id.editDescription);
		_otherMembers = (EditText) findViewById(R.id.otherMembers);
		_text = (CheckBox) findViewById(R.id.checkBoxText);
		_photo = (CheckBox) findViewById(R.id.checkBoxPhoto);

		// Assign listener to Save button
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new handleButton_Save());
	}

	/**
	 * Checks if any of the required fields has been left empty.
	 * 
	 * @return True if a required field has been left empty, false otherwise.
	 */
	private boolean hasEmptyFields() {
		if (_name.getText().toString() == "")
			return true;
		if (_description.getText().toString() == "")
			return true;
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

	/**
	 * A handler for the save button.
	 * 
	 * @author Jeanine Bonot
	 * 
	 */
	class handleButton_Save implements OnClickListener {

		/**
		 * Creates and saves a new task.
		 */
		public void onClick(View view) {

			if (CreateTaskView.this.hasEmptyFields()) {
				// TODO: Unable to save
				return;
			}

			Task task = createTask();
			TASK_MANAGER.writeFile(task);

			// Only add to web database if Creator has added members
			String[] members = task.getMemberList();
			if (members.length > 1) {
				_webManager.insertTask(task);
			}

			finish();
		}

	}

}
