package tasktracker.view;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.*;
import tasktracker.elements.*;
import java.util.*;

/**
 * An activity that displays the user's tasks. The user is able to see the
 * details of each task, add a new task, or delete a task.
 * 
 * @author Jeanine Bonot
 * 
 */
public class TaskListView extends Activity {

	private ListView taskList;
	private List<TaskElement> tasks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
