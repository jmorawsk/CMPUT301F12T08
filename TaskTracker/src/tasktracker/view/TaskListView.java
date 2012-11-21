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
//import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.Editable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import tasktracker.controller.TaskController;
import tasktracker.model.PreferencesManager;
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
	private List<Task> taskList;
	// private List<String> tasks;
	private String[] tasks = new String[0];

	private PreferencesManager preferences = new PreferencesManager();
	
	private WebDBManager webManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list_view);

		//Changed to a global variable
		/*
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			_user = extras.getString("USER");
		}
		*/

		// Assign ListView and its on item click listener.
		taskListView = (ListView) findViewById(R.id.taskList);
		taskListView.setOnItemClickListener(new handleList_Click());

		// TODO: read from database and display
		// String[][] webTasks = webManager.listTasksAsArrays();
		// for(int n=0;n<webTasks.length;n++){
		// tasks.add(webTasks[n][0]);
		// }
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(this,R.layout.list_item, tasks);
		// taskListView.setAdapter(adapter);

		Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
		Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);
		buttonMyTasks.setEnabled(false);

		buttonCreate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						CreateTaskView.class);
				startActivity(intent);
			}
		});

		buttonNotifications.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						NotificationListView.class);
				startActivity(intent);

			}
		});

		setDebugStuff();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.account_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.change_name:
	            changeName();
	            return true;
	        case R.id.help:
	            showHelp();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void changeName() {
		// TODO Auto-generated method stub
		//showToast("Change name clicked");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Change Account Name");
		alert.setMessage("Old Account Name was '" + preferences.getUsername(getBaseContext()) + "'.");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  Editable value = input.getText();
		  preferences.setUsername(getBaseContext(), value.toString());
		  // Do something with value!
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

	private void showHelp() {
		// TODO Auto-generated method stub
		showToast("Show help clicked");
	}


	void setDebugStuff() {
		Button deleteFile = (Button) findViewById(R.id.debugButton);

		deleteFile.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (TaskController.deleteFile()) {
					showToast("Deleted file on SD");
				} else {
					showToast("Failed to delete file from SD");
				}
			}
		});
	}

	private void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

	protected void onStart() {
		super.onStart();
		taskList = TaskController.readFile();
		if (taskList.size() == 0)
			taskList = createDummies();
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
				R.layout.list_item, taskList);
		taskListView.setAdapter(adapter);

	}

	/** Creates a dummy array if SD fails */
	List<Task> createDummies() {
		List<Task> list = new ArrayList<Task>();
		list.add(new Task("Me", "Not from SD", "Task Description"));
		list.add(new Task("Me", "Still not from SD", "Task Description"));
		list.add(new Task("Me", "Why isn't it from SD?", "Task Description"));
		list.add(new Task("Me", "Help", "Task Description"));
		list.add(new Task("now", "This should be working", "Task Description"));
		list.add(new Task("You", "No", "Task Description"));
		return list;
	}

	protected void onStop() {
		super.onStop();

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
			Task task = taskList.get(myItemInt);
			Intent intent = new Intent(getApplicationContext(),
					TaskView.class);
			intent.putExtra("TASK", task);
			startActivity(intent);
			
			
//			showItemMenu(myView, myItemInt);
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
			menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			
		}

	}
}
