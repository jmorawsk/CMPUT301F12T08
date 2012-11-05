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
import tasktracker.model.WebDBManager;
import tasktracker.model.elements.*;

/**
 * An activity that allows a user to create a task.
 * 
 * @author Jeanine Bonot
 * 
 */
public class CreateTaskView extends Activity {

	private EditText name;
	private EditText deadline;
	private EditText description;
	private EditText otherMembers;
	private WebDBManager webManager;

	// Create dummy user for production.
	private static final User CREATOR = new User("DebugUser");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_view);

		//Initialize our webManager
		webManager = new WebDBManager();
		
		// Assign EditText fields
		name = (EditText) findViewById(R.id.name);
		deadline = (EditText) findViewById(R.id.deadline);
		description = (EditText) findViewById(R.id.description);
		otherMembers = (EditText) findViewById(R.id.otherMembers);

		// Assign listener to Save button
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new handleButton_Save());
	}

	/**
	 * Parses through the string of task members and puts the members into a
	 * list.
	 * 
	 * @return The list of members.
	 */
	private List<User> parseOtherMembers() {
		List<User> members = new ArrayList<User>();

		// TODO: Parse through string for user information, run checks
		return members;
	}

	/**
	 * Checks if any of the required fields has been left empty.
	 * 
	 * @return True if a required field has been left empty, false otherwise.
	 */
	private boolean hasEmptyFields() {
		if (name.getText().toString() == "")
			return true;
		if (deadline.getText().toString() == "")
			return true;
		if (description.getText().toString() == "")
			return true;
		return false;
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
		@Override
		public void onClick(View view) {

			if (CreateTaskView.this.hasEmptyFields()) {
				// TODO: Unable to save
				return;
			}

			Task task = createTask();
			// TODO: Save task
			//call this
			webManager.insertTask(task);
		}

		/**
		 * Create a task based on the creator's input.
		 * 
		 * @return The task created with the creator's input.
		 */
		private Task createTask() {

			// TODO: Find out how to quickly access user information
			Task task = new Task(CREATOR);

			task.setDescription(CreateTaskView.this.name.getText().toString());
			task.setName(CreateTaskView.this.name.getText().toString());
			// TODO: add requirements
			// TODO: add other members

			return task;
		}

	}

}
