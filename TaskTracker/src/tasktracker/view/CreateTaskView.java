package tasktracker.view;

import android.os.Bundle;
import android.app.Activity;
import java.text.*;
import java.util.Calendar;

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
		public void onClick(View arg0) {
		
			TaskElement = new TaskElement();
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
		}
		
	}
	
	
}
