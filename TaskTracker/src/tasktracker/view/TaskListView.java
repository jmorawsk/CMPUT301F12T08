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

import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import tasktracker.model.elements.*;

public class TaskListView extends Activity {

	private ListView taskListView;
	private List<Task> tasks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		taskListView = (ListView) findViewById(R.id.taskList);

		taskListView.setOnItemClickListener(new handleList_Click());

	}

	/**
	 * A handler for clicking on a task item. Shows a menu of possible controls.
	 * 
	 * @author Jeanine Bonot
	 * 
	 */
	class handleList_Click implements OnItemClickListener {

		@Override
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
			menu.getMenuInflater().inflate(R.menu.popup, menu.getMenu());

			// TODO: Create menu content, set OnMenuItemClickListener, update
			// tasks upon deletion
		}

	}
}
