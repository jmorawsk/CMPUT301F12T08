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

import com.google.gson.Gson;
import android.content.Context;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import tasktracker.model.Preferences;

/**
 * Creates a request to add a user to the webserver
 *  
 */
public class RequestCreateUser implements NetworkRequestModel {
	private Context context;
	private User user;
	private String requestString;
	private boolean changeCurrentUser;

    static final Gson gson = new Gson();
    /** index of 'content' for objects in database */
    
	public RequestCreateUser(Context contex, User use, boolean changeCurrentUse){
		changeCurrentUser = changeCurrentUse;
		context = contex;
		user = use;
    	String content = gson.toJson(user);
    	String command = "action=" + "post" + "&summary=" 
    			+ "<User>" + user.getName()
    			+ "&content=" + content.toString();

        requestString = AccessURL.turnCommandIntoURL(command);
    
		AccessURL access = new AccessURL(this);
		access.execute(getCrowdsourcerCommand());
	}
	
	public Context getContext(){
		return context;
	}
	
	public String getCrowdsourcerCommand(){
		//System.out.println("Request to network: " + requestString);
		return requestString;
	}
	
	public void runAfterExecution(String line){
		DatabaseAdapter _dbHelper = new DatabaseAdapter(context);
		_dbHelper.open();
		
		String taskIDString = AccessURL.getTag("id\":\"", line, line.indexOf('}', 0) + 1);
		long taskID;
		
		user.setID(taskIDString);
		
		_dbHelper.createUser(user.getName(), user.getEmail(), user.getPassword(), taskIDString);

		if (changeCurrentUser){
			Preferences.setPreferences(context, user.getName(), user.getEmail(), user.getPassword(), taskIDString, true);
		}
		_dbHelper.close();
		
//		Toast toast = Toast.makeText(context, "User added to crowdSourcer: " + user.getName(), Toast.LENGTH_SHORT);
//		toast.show();
		
	}
}
