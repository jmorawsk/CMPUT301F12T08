package tasktracker.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A class for getting and setting app preferences.
 * Used for consistency for default values
 * @author Mike Dardis
 */

public class PreferencesManager {
	private static final String PREF_NAME = "Preferences";
	
	//Variables for quicker access, ie without accessing the Preferences file
	private static String username = "";
	
	/**
	 * Returns the current name being used for the preferences file
	 * @return	preferences file name
	 */
	public String getPreferencesFileName(){
		return PREF_NAME;
	}
	
	/**
	 * Gets the user name setting
	 * @param context 		the Activity context (use getBaseContext())
	 * @return				the user name
	 */
	public String getUsername(Context context){
		if (username == ""){
			//If username not loaded yet, load it
			SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		    username = settings.getString("username", "New Account");
		}
		return username;
	}
	
	/**
	 * Changes the user name setting in the preferences file and the cached variable
	 * @param context 	the Activity context (use getBaseContext())
	 * @param value 	the new user name string
	 */
	public void setUsername(Context context, String value){
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", value);
		editor.commit();
		username = value;	//Update the quick access variable
	}
	
}
