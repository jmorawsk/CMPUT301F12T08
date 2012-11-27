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

import android.os.AsyncTask;
//import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.Preferences;
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
	public List<Task> webTaskList;
	public List<Task> oldWebTaskList;
	// private List<String> tasks;
	private String[] tasks = new String[0];
	private String _user;

	// private PreferencesManager preferences;

	private WebDBManager webManager;
	private DatabaseAdapter _dbHelper;
	private Cursor _cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		webManager = new WebDBManager();
		oldWebTaskList = new ArrayList<Task>();
		webTaskList = new ArrayList<Task>();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list_view);

		_user = Preferences.getUsername(this);
		_dbHelper = new DatabaseAdapter(this);

		setupToolbarButtons();
		setupTaskList();
	}

	protected void onStart() {
		super.onStart();
		_dbHelper.open();
		fillData();
		// loadTasks();
		// contactWebserver webRequest = new contactWebserver();
		// webRequest.execute();
	}

	protected void onStop() {
		super.onStop();
		_dbHelper.close();
		stopManagingCursor(_cursor);
		_cursor.close();
	}

	private void setupToolbarButtons() {

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
		case R.id.logout:

			Intent intent = new Intent(getApplicationContext(), Login.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void changeName() {
		/*
		 * InputPopup popup = new InputPopup(); String result =
		 * popup.makePopup("Test title", "Test message", getBaseContext());
		 * showToast(result);
		 */
		// inputPopup("Test title", "Test message", this);
		// showToast(resultValue);
	}

	/*
	 * String resultValue; public void inputPopup(String Title, String Message,
	 * Context context) { AlertDialog.Builder alert = new
	 * AlertDialog.Builder(context);
	 * 
	 * alert.setTitle(Title); alert.setMessage(Message);
	 * 
	 * // Set an EditText view to get user input final EditText input = new
	 * EditText(context); alert.setView(input);
	 * 
	 * alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) { Editable
	 * value = input.getText(); //preferences.setUsername(getBaseContext(),
	 * value.toString()); // Do something with value! resultValue =
	 * value.toString(); } });
	 * 
	 * alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) { //
	 * Canceled. resultValue = ""; } });
	 * 
	 * alert.show(); >>>>>>> branch 'master' of
	 * https://github.com/jmorawsk/CMPUT301F12T08.git
	 * 
	 * <<<<<<< HEAD }); }
	 * 
	 * // }
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater
	 * inflater = getMenuInflater(); inflater.inflate(R.menu.account_menu,
	 * menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * item selection switch (item.getItemId()) { case R.id.change_name:
	 * changeName(); return true; case R.id.help: showHelp(); return true;
	 * default: return super.onOptionsItemSelected(item); } }
	 * 
	 * // private void changeName() { // showToast("Change name clicked");
	 * AlertDialog.Builder alert = new AlertDialog.Builder(this);
	 * 
	 * alert.setTitle("Change Account Name");
	 * alert.setMessage("Old Account Name was '" +
	 * preferences.getUsername(getBaseContext()) + "'.");
	 * 
	 * // Set an EditText view to get user input final EditText input = new
	 * EditText(this); alert.setView(input);
	 * 
	 * alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) { Editable
	 * value = input.getText(); preferences.setUsername(getBaseContext(),
	 * value.toString()); // Do something with value! } });
	 * 
	 * alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) { //
	 * Canceled. } });
	 * 
	 * alert.show(); }
	 * 
	 * private void showHelp() { // TODO Auto-generated method stub
	 * showToast("Show help clicked"); }
	 * 
	 * private void setupTaskList() { ======= }
	 */

	/*
	 * String mystring = ""; private void changeName() {
	 * //showToast("Change name clicked"); AlertDialog.Builder alert = new
	 * AlertDialog.Builder(this);
	 * 
	 * alert.setTitle("Change Account Name");
	 * alert.setMessage("Old Account Name was '" +
	 * preferences.getUsername(getBaseContext()) + "'.");
	 * 
	 * // Set an EditText view to get user input final EditText input = new
	 * EditText(this); alert.setView(input);
	 * 
	 * alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) { Editable
	 * value = input.getText(); //preferences.setUsername(getBaseContext(),
	 * value.toString()); mystring = value.toString(); // Do something with
	 * value! } });
	 * 
	 * alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int whichButton) { //
	 * Canceled. } });
	 * 
	 * alert.show(); }
	 */

	private void showHelp() {
		// TODO Auto-generated method stub
		showToast("Show help clicked");
	}

	private void setupTaskList() {
		// Assign ListView and its on item click listener.
		taskListView = (ListView) findViewById(R.id.taskList);
		taskListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int i, long id) {
				Intent intent = new Intent(getApplicationContext(),
						TaskView.class);
				intent.putExtra("TASK_ID", id);
				startActivity(intent);
			}

		});
	}

	private void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.show();
	}

	private void fillData() {
		_cursor = _dbHelper.fetchTasksAvailableToUser(_user);
		startManagingCursor(_cursor);

		String[] from = new String[] { DatabaseAdapter.TASK,
				DatabaseAdapter.USER, DatabaseAdapter.DATE, DatabaseAdapter.COUNT };
		int[] to = new int[] { R.id.item_title, R.id.item_text,
				R.id.item_date_bottom, R.id.item_vote_count};

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.list_item, _cursor, from, to);

		taskListView.setAdapter(adapter);
	}

	private void update() {
		taskList.addAll(webTaskList);
		ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
				R.layout.list_item, taskList);
		taskListView.setAdapter(adapter);
		oldWebTaskList = new ArrayList<Task>();
		for (Task t : webTaskList) {
			oldWebTaskList.add(t);
		}
	}

	private class contactWebserver extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... temp) {

			webTaskList = new ArrayList<Task>();
			System.out.println("testing");
			String[][] results = webManager.listTasksAsArrays();
			String id;
			Task newTask;
			for (int n = 0; n < results.length; n++) {
				if (results[n].length > 1) {
					System.out.println("index =" + n);
					id = results[n][1];
					newTask = webManager.getTask(id);
					webTaskList.add(newTask);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			// update UI with my objects
			// taskList.addAll(webTaskList);

			update();
		}

	}
}
