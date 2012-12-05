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
import android.content.Intent;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import tasktracker.model.Preferences;
import tasktracker.view.Login;

/**
 * Creates an object to locate a user in the Crowdsourcer database via either their username
 * or userID (depending on which constructor is called), then adds that user to the local user
 * SQL table and optionally sets the current user to them.
 * 
 * Constructors:
 * RequestGetAUser(Context contex, String userid, boolean setToCurrentUser1)
 *   Looks for the user based on userID
 *   
 * RequestGetAUser(Context contex, boolean setToCurrentUser1, String username)
 *   Looks for the user based on the userName (Much slower)
 * 
 * Run by creating an instance.
 */
public class RequestGetAUser implements NetworkRequestModel {
	private Context context;

	private String requestString;

	private String userID = "";
	private String userName = "";
	private boolean setToCurrentUser;

	/** index of 'content' for objects in database */

	public RequestGetAUser(Context contex, String userid,
			boolean setToCurrentUser1) {
		userID = userid;
		userName = "";
		context = contex;
		setToCurrentUser = setToCurrentUser1;

		String command = "action=" + "get&id=" + userID;


		requestString = AccessURL.turnCommandIntoURL(command);

		AccessURL access = new AccessURL(this);
		access.execute(getCrowdsourcerCommand());
	}

	public RequestGetAUser(Context contex, boolean setToCurrentUser1,
			String username) {
		userName = username;
		context = contex;
		setToCurrentUser = setToCurrentUser1;

		String command = "action=" + "list";

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

		if (userName != "") {
			boolean foundUser = false;
			String id = "", aFoundName;
			int pos = 0;
			// Search based on userName for userID
			while (pos < line.length()) {
				pos = line.indexOf("{\"summary\":\"", pos);
				if (pos == -1)
					break;

				pos = pos + "{\"summary\":\"".length();

				aFoundName = AccessURL.getTag("<User>", line, pos);
				if (aFoundName != null) {
					if (aFoundName.equals(userName)) {
						foundUser = true;

						System.out.println("Buggy Name=" + aFoundName + " pos="
								+ pos);

						// Run new search to get details for this userID and add
						// to local DB etc
						id = AccessURL.getTag(",\"id\":\"", line, pos);

						break;
					}
				}
			}
			if (foundUser) {


				System.out.println("Buggy ID=" + id);
				RequestGetAUser getDetails = new RequestGetAUser(context, id,
						setToCurrentUser);

			} else {

				Intent intent = new Intent(context, Login.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// TODO user not found! Create?
			}
		} else {
			if (userID == "") {
				// TODO report error here, no userName or userID given
				return;
			}
			// Get user info based on on userID

			String email, password, name;
			User user = new User("nulllet");

			name = AccessURL.getTag("<User>", line, 0);
			email = AccessURL.getTag("\"email\":", line, 0);
			password = AccessURL.getTag("\"password\":", line, 0);

			if (name != null && email != null && password != null) {
				// Valid user info
				user.setName(name);
				user.setEmail(email);
				user.setPassword(password);

				// Add user to local SQL db
				DatabaseAdapter _dbHelper = new DatabaseAdapter(context);
				_dbHelper.open();
				_dbHelper.createUser(user.getName(), user.getEmail(),
						user.getPassword(), userID);
				_dbHelper.close();
				// Set as current user?
				if (setToCurrentUser)
					Preferences.setPreferences(context, user.getName(),
							user.getEmail(), user.getPassword(), userID, true);

				// TODO Refresh the current page, probably TaskListView, as the
				// username has changed
			} else {
				// User gotten but not enough info
				// TODO error?
			}
		}

	}

}