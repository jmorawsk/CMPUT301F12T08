package tasktracker.view;

import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

import java.util.*;
import tasktracker.elements.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

/**
 * An activity that displays the user's notifications.
 * 
 * @author Jeanine
 * 
 */
public class NotificationListView extends Activity {

	private ListView notificationsList;
	private List<NotificationElement> notifications;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_list_view);

		this.notificationsList = (ListView) findViewById(R.id.notificationsList);
		this.notificationsList.setOnItemClickListener(new handleList_Click());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_notification_list_view, menu);
		return true;
	}

	/**
	 * A handler for the ListView. Shows a popup menu for a given notification.
	 */
	class handleList_Click implements OnItemClickListener {

		@Override
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
			menu.getMenuInflater().inflate(R.menu.popup, menu.getMenu());

			// TODO: Create menu content, set OnMenuItemClickListener, update
			// notifications upon deletion
		}

	}
}
