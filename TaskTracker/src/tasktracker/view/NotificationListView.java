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

//import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.*;

import tasktracker.model.Preferences;
import tasktracker.controller.DatabaseAdapter;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

/**
 * An activity that displays the user's notifications.
 * 
 * @author Jeanine
 * 
 */
public class NotificationListView extends Activity {

	private ListView _notificationsList;

	private DatabaseAdapter _dbHelper;
	private Cursor _cursor;
	private String _user;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_list_view);

		_dbHelper = new DatabaseAdapter(this);
		_user = Preferences.getUsername(this);
		Log.d("Notifications", "user = " + _user);

		// Assign ListView and its item click listener
		// this.notificationsList.setOnItemClickListener(new
		// handleList_Click());

		setupToolbarButtons();

	}

	protected void onStart() {

		super.onStart();
		_dbHelper.open();
		setupNotificationList();
		fillData();
	}

	protected void onStop() {

		super.onStop();
		_dbHelper.close();
		stopManagingCursor(_cursor);
		_cursor.close();
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
			startActivity(intent);
			return true;
		default:
			ToastCreator.showShortToast(this, "Not Yet Implemented");
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupNotificationList() {

		_notificationsList = (ListView) findViewById(R.id.notificationsList);
		_notificationsList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int i, long id) {

				// TODO Click leads to Task
				Intent intent = new Intent(getApplicationContext(),
						TaskView.class);
				TextView taskID = (TextView) ((RelativeLayout) v)
						.findViewById(R.id.id);
				intent.putExtra("TASK_ID",
						Integer.parseInt(taskID.getText().toString()));
				startActivity(intent);
			}

		});
	}

	private void fillData() {

		_cursor = _dbHelper.fetchUserNotifications(_user);
		startManagingCursor(_cursor);

		String[] from = new String[] { DatabaseAdapter.TEXT,
				DatabaseAdapter.TASK_ID, DatabaseAdapter.USER };
		int[] to = new int[] { R.id.text, R.id.id };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.simple_list_item, _cursor, from, to);
		Log.d("Notifications", "Count = " + adapter.getCount());
		_notificationsList.setAdapter(adapter);

	}

	private void setupToolbarButtons() {

		Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
		Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
		Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);
		buttonNotifications.setEnabled(false);

		buttonMyTasks.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						TaskListView.class);
				startActivity(intent);

			}
		});

		buttonCreate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						CreateTaskView.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * A handler for the ListView. Shows a popup menu for a given notification.
	 */
	class handleList_Click implements OnItemClickListener {

		public void onItemClick(AdapterView<?> myAdapter, View myView,
				int myItemInt, long mylng) {

			// TODO Auto-generated method stub
			showItemMenu(myView, myItemInt);
		}

		/**
		 * Displays the menu for a notification.
		 * 
		 * @param view
		 *            The selected notification item.
		 * @param index
		 *            The index of the notification in the ListView.
		 */
		private void showItemMenu(View view, final int index) {

			PopupMenu menu = new PopupMenu(NotificationListView.this, view);
			// menu.getMenuInflater().inflate(R.menu.popup, menu.getMenu());

			// TODO: Create menu content, set OnMenuItemClickListener, update
			// notifications upon deletion
		}

	}
}
