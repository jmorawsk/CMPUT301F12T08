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
 <<<<<<< HEAD
 =======
 * @param changeCurrentUser 	Whether to change the current user settigns/Preferences to this
 * newly created user
 * 
 >>>>>>> Recommitting previous commit with ignored files (in elements folder) added.
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
				+ notification.getTaskId() + "<Recipients>"
				+ notification.getRecipientsString();

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
		// Do nothing.
	}
}
