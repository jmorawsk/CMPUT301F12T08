package tasktracker.model.elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.google.gson.Gson;

import android.content.Context;
import android.widget.Toast;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import tasktracker.model.Preferences;

/*
 * Creates an object to add a Task to Crowdsourcer when passed to ReadFromURL, THEN adds
 * the given task to the local SQL database with Crowdsourcer's returned ID.
 * 
 * Run by creating an instance.
 */
public class RequestCreateTask implements NetworkRequestModel {
	private Context context;
	private Task task;
	private String requestString;

	static final Gson gson = new Gson();

	/** index of 'content' for objects in database */

	public RequestCreateTask(Context contex, Task theTask) {
		context = contex;
		task = theTask;
		String content = gson.toJson(task);
		String command = "action=" + "post" + "&summary=" + task.getSummary()
				+ "&content=" + content.toString();

		requestString = AccessURL.turnCommandIntoURL(command);

		AccessURL access = new AccessURL(this);
		access.execute(getCrowdsourcerCommand());
	}

	public Context getContext() {
		return context;
	}

	public String getCrowdsourcerCommand() {
		// System.out.println("Request to network: " + requestString);
		return requestString;
	}

	public void runAfterExecution(String line) {
		DatabaseAdapter _dbHelper = new DatabaseAdapter(context);

		String taskID = AccessURL.getTag("id\":\"", line,
				line.indexOf('}', 0) + 1);
		// long taskID;

		// Save task to local SQL
		List<String> others = task.getOtherMembers();

		// Add to SQL server
		_dbHelper.open();

		task.setDescription(taskID);

		// taskID = _dbHelper.createTask(task);dfd
		_dbHelper.createTask(task);

		String taskName = task.getName();
		String message = Notification.getMessage(
				Preferences.getUsername(context), taskName,
				Notification.Type.InformMembership);

		// TODO: Waiting on refactor, taskID needs to be type String not long

		_dbHelper.createMember(taskID, Preferences.getUsername(context));

		for (String member : others) {
			_dbHelper.createMember(taskID, member);
			_dbHelper.createNotification(taskID, member, message);
		}

		_dbHelper.close();

		Toast toast = Toast.makeText(context,
				"Win! Task added to crowdSourcer: " + taskID,
				Toast.LENGTH_SHORT);
		toast.show();
	}
}
