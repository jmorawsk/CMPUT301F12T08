package tasktracker.model.elements;

import com.google.gson.Gson;

import android.content.Context;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;

/**
 * Creates an object to add a Task to Crowdsourcer when passed to ReadFromURL, THEN adds
 * the given task to the local SQL database with Crowdsourcer's returned ID.
 * 
 * @param changeCurrentUser 	Whether to change the current user settigns/Preferences to this
 * newly created user
 * 
 * Run by creating an instance.
 */
public class RequestCreateNotification implements NetworkRequestModel {
	private Context context;
	private String requestString;
	static final Gson gson = new Gson();

	/** index of 'content' for objects in database */

	public RequestCreateNotification(Context context, Notification notification) {
		this.context = context;

		String command = "action=" + "post" + "&summary=" + "<Notification>"
				+ notification.getMessage() + "<TaskID>" 
				+ notification.getTaskId() + "<Date>" + notification.getDate() + "<Recipients>"
				+ notification.getRecipientsString();

		requestString = AccessURL.turnCommandIntoURL(command);

		AccessURL access = new AccessURL(this);
		access.execute(getCrowdsourcerCommand());
	}

	public Context getContext() {
		return context;
	}

	public String getCrowdsourcerCommand() {
		return requestString;
	}

	public void runAfterExecution(String line) {
		// Do nothing.
	}
}
