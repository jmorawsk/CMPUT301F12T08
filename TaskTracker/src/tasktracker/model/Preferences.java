package tasktracker.model;

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
import android.content.SharedPreferences;

/**
 * A class for getting and setting app preferences. Used for consistency for
 * default values
 * 
 * @author Mike Dardis
 */

public class Preferences {
	private static final String PREF_NAME = "Preferences";
	public static final String INVALID_ACCOUNT = "<ACCOUNT_NOT_SET>";
	
	// Variables for quicker access, ie without accessing the Preferences file
	private static String userID = "";
	private static String username = "";
	private static String password = "";
	private static String email = "";
	

	/**
	 * Returns the current name being used for the preferences file
	 * 
	 * @return preferences file name
	 */
	public String getPreferencesFileName() {
		return PREF_NAME;
	}

	/**
	 * Gets the user ID setting
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @return the user ID
	 */
	public static String getUserID(Context context){
		if (userID == "") {
			// If username not loaded yet, load it
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			userID = settings.getString("userID", INVALID_ACCOUNT);
		}
		return username;
	}
	
	/**
	 * Gets the user name setting
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @return the user name
	 */
	public static String getUsername(Context context) {
		if (username == "") {
			// If username not loaded yet, load it
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			username = settings.getString("username", INVALID_ACCOUNT);
		}
		return username;
	}

	/**
	 * Gets the user password setting
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @return the user password
	 */
	public static String getPassword(Context context) {
		if (password == "") {
			// If username not loaded yet, load it
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			password = settings.getString("password", "New Account");
		}
		return password;
	}

	/**
	 * Gets the user password setting
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @return the user email
	 */
	public static String getEmail(Context context) {
		if (email == "") {
			// If username not loaded yet, load it
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			email = settings.getString("email", "New Account");
		}
		return email;
	}

	
	//Setters
	/**
	 * Changes the user ID setting in the preferences file and the cached
	 * variable
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @param value
	 *            the new user ID string
	 * @param changePreferences
	 * 			  whether to change the preferences file value, or leave it and use this value as a temporary session
	 */
	public static void setUserID(Context context, String value,
			boolean changePreferences) {
		if (changePreferences) {
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("userID", value);
			editor.commit();
			
			//TODO Change the SQL database...
			//updateUser()
			//TODO and change the user item in the crowdsourcer server
		}
		userID = value; // Update the quick access variable
	}

	
	/**
	 * Changes the user name setting in the preferences file and the cached
	 * variable
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @param value
	 *            the new user name string
	 * @param changePreferences
	 * 			  whether to change the preferences file value, or leave it and use this value as a temporary session
	 */
	public static void setUsername(Context context, String value,
			boolean changePreferences) {
		if (changePreferences) {
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("username", value);
			editor.commit();
			
			//TODO Change the SQL database...
			//updateUser()
			//TODO and change the user item in the crowdsourcer server
		}
		username = value; // Update the quick access variable
	}

	/**
	 * Changes the user password setting in the preferences file and the cached
	 * variable
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @param value
	 *            the new user password string
	 * @param changePreferences
	 * 			  whether to change the preferences file value, or leave it and use this value as a temporary session
	 */
	public static void setPassword(Context context, String value,
			boolean changePreferences) {
		if (changePreferences) {
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("password", value);
			editor.commit();
		}
		password = value; // Update the quick access variable
	}

	/**
	 * Changes the user email setting in the preferences file and the cached
	 * variable
	 * 
	 * @param context
	 *            the Activity context (use getBaseContext())
	 * @param value
	 *            the new user email string
	 * @param changePreferences
	 * 			  whether to change the preferences file value, or leave it and use this value as a temporary session
	 */
	public static void setEmail(Context context, String value,
			boolean changePreferences) {
		if (changePreferences) {
			SharedPreferences settings = context.getSharedPreferences(
					PREF_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("email", value);
			editor.commit();
		}
		email = value; // Update the quick access variable
	}
	
	/*
	 * Set multiple preferences at once.
	 */
	public static void setPreferences(Context context, String user, String email, String password, String id,
			boolean save) {
		setUsername(context, user, save);
		setPassword(context, email, save);
		setEmail(context, password, save);
		setUserID(context, id, save);
	}

}
