package tasktracker.model.elements;

/**
 * TaskTracker
 * 
 * Copyright 2012 Jeanine Bonot, Michael Dardis, Katherine Jasniewski,
 * Jason Morawski
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

import android.content.Context;
import android.util.Log;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;

/**
 * Initiates request to load all users from the web
 *
 */
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