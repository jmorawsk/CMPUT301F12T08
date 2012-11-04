package tasktracker.view;

import android.os.Bundle;
import android.app.Activity;
import java.text.*;
import java.util.*;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import tasktracker.elements.*;
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
	
	// Create dummy user for production.
	private static final User CREATOR = new User("DebugUser");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task_view);

		name = (EditText) findViewById(R.id.name);
		deadline = (EditText) findViewById(R.id.deadline);
		description = (EditText) findViewById(R.id.description);
		otherMembers = (EditText) findViewById(R.id.otherMembers);

		Button saveButton = (Button) findViewById(R.id.saveButton);
	}

	protected void onResume(){
		super.onResume();		
	}
	
	/**
	 * 
	 * @return True if a required field has been left empty, false otherwise.
	 */
	private boolean hasEmptyFields(){
		if (name.getText().toString() == "")
			return true;
		if (deadline.getText().toString() == "")
			return true;
		if (description.getText().toString() == "")
			return true;
		return false;
	}
	
	class handleButton_Save implements OnClickListener{

		@Override
		public void onClick(View view) {
			
			if (CreateTaskView.this.hasEmptyFields())
			{
				// TODO: Unable to save
				return;				
			}
			
			TaskElement task = createTask();
			// TODO: Save task
		}
		
		private TaskElement createTask(){
			String name = CreateTaskView.this.name.getText().toString();
			String descr = CreateTaskView.this.name.getText().toString();
			Date dateCreated = Calendar.getInstance().getTime();
			List<Requirement> reqs = new ArrayList<Requirement>();
			// TODO: add requirements
			
			//TODO: Find out how to quickly access user information
			return new TaskElement(CREATOR, name, dateCreated, descr, reqs);
		}
		
	}
	
	
}
