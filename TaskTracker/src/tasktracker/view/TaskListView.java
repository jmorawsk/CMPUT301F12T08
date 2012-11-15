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

import java.util.*;

//import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import tasktracker.model.WebDBManager;
import tasktracker.model.elements.*;

/**
 * An activity that displays a list of tasks that a user can view and fulfill.
 * 
 * @author Jeanine Bonot
 * 
 */
public class TaskListView extends Activity {

	private ListView taskListView;
	// private List<Task> tasks;
	// private List<String> tasks;
	private String[] tasks = new String[0];

	private WebDBManager webManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);

		// Assign ListView and its on item click listener.
		taskListView = (ListView) findViewById(R.id.taskList);

		// TODO: read from database and display
		// String[][] webTasks = webManager.listTasksAsArrays();
		// for(int n=0;n<webTasks.length;n++){
		// tasks.add(webTasks[n][0]);
		// }
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(this,R.layout.list_item, tasks);
		// taskListView.setAdapter(adapter);

		taskListView.setOnItemClickListener(new handleList_Click());
		
		Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
		Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);
		buttonMyTasks.setActivated(false);
		
		buttonCreate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						CreateTaskView.class);
				startActivity(i);
			}
		});

		buttonNotifications.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						NotificationListView.class);
				startActivity(i);

			}
		});

	}

	protected void onStart() {
		super.onStart();
		fillData();

	}

	protected void onStop() {
		super.onStop();

	}

	/** Function call to retrieve all the task names from the database.
	 * Utilizes the simple cursor adapter to bind it to the listview */
	private void fillData() {

		// TODO: Get alternative for deprecated methods/constructors
		

	}

	/**
	 * A handler for clicking on a task item. Shows a menu of possible controls.
	 * 
	 * @author Jeanine Bonot
	 * 
	 */
	class handleList_Click implements OnItemClickListener {

		public void onItemClick(AdapterView<?> myAdapter, View myView,
				int myItemInt, long mylng) {
			// TODO Auto-generated method stub
			showItemMenu(myView, myItemInt);
		}

		/**
		 * Displays the menu for a given task.
		 * 
		 * @param view
		 *            The selected task element.
		 * @param index
		 *            The index of the task in the ListView.
		 */
		private void showItemMenu(View view, final int index) {
			PopupMenu menu = new PopupMenu(TaskListView.this, view);
			// menu.getMenuInflater().inflate(R.menu.popup, menu.getMenu());

			// TODO: Create menu content, set OnMenuItemClickListener, update
			// tasks upon deletion
		}

	}
}
