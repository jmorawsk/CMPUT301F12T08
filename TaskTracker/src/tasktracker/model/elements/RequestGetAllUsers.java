package tasktracker.model.elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import tasktracker.model.Preferences;
import tasktracker.view.Login;

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
			Log.d("RequestGetAllUsers", "username: " + username);
			
			if (username == null)
				continue;

			String id = AccessURL.getTag(",\"id\":\"", line, pos);
			Log.d("RequestGetAllUsers", "id: " + id);

			long rowId = dbHelper.createUser(id, username);
			Log.d("RequestGetAllUsers", "rowId: " + rowId);
		}
		
		dbHelper.close();

	}

}