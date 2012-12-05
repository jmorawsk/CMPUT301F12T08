package tasktracker.model.elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import tasktracker.model.Preferences;

/**
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

		task.setID(taskID);

		// Add to SQL server
		_dbHelper.open();
		long value = _dbHelper.createTask(task);
		Log.d("RequestCreateTask", "create: " + value);
		_dbHelper.close();

	}

	private void createNotifications() {
		String taskName = task.getName();
		String message = Notification.getMessage(
				Preferences.getUsername(context), taskName,
				Notification.Type.InformMembership);
		Notification notification = new Notification(message);
		notification.setRecipients(task.getOtherMembers());
		notification.setTaskId(task.getID());

		RequestCreateNotification request = new RequestCreateNotification(
				this.context, notification);
	}

	private void createMembers(DatabaseAdapter dbHelper) {
		dbHelper.open();
		dbHelper.createMember(task.getID(), Preferences.getUsername(context));
		dbHelper.close();
	}

	public static String compress(String input) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(input.getBytes("UTF-8"));
			input = out.toString();
			gzip.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
}
