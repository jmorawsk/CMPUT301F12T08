package tasktracker.model;

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

	// Variables for quicker access, ie without accessing the Preferences file
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
			username = settings.getString("username", "New Account");
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

}
