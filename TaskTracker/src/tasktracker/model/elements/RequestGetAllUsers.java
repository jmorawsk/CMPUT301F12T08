package tasktracker.model.elements;

import android.content.Context;
import android.util.Log;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;

public class RequestGetAllUsers implements NetworkRequestModel {
	private Context context;
	private String requestString;

	public RequestGetAllUsers(Context contex) {
		this.context = contex;
		this.requestString = AccessURL.turnCommandIntoURL("action=list");

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
		DatabaseAdapter dbHelper = new DatabaseAdapter(this.context);
		dbHelper.open();

		int pos = 0;

		int upperBound = line.length();
		while (pos < upperBound) {
			pos = line.indexOf("{\"summary\":\"", pos);
			if (pos == -1)
				break;
			pos += "{\"summary\":\"".length();
			String username = AccessURL.getTag("<User>", line, pos);
			
			if (username == null)
				continue;

			Log.d("RequestGetAllUsers", "username: " + username);
			String id = AccessURL.getTag(",\"id\":\"", line, pos);
			Log.d("RequestGetAllUsers", "id: " + id);

			long rowId = dbHelper.createUser(id, username);
			Log.d("RequestGetAllUsers", "rowId: " + rowId);
		}
		
		dbHelper.close();

	}

}