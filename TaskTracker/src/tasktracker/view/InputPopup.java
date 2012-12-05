package tasktracker.view;

import tasktracker.model.Preferences;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.widget.EditText;

/*
 * Allows one to create popups with a single text input field, and ok/cancel.
 * Only works for pre-defined string-handling methods triggered by parameter
 * @author Mike Dardis
 */
public class InputPopup {

	public enum Type {
		username, password, email
	}
	
	/*
	 * Call make to create a popup, Activity will continue to execute underneath.
	 * When the user finally responds to it a method will be executed according to the
	 * value in the type parameter.
	 * 
	 * @param Title
	 * 					title of the popup
	 * @param Message
	 * 					text below the title
	 * @param context
	 * 					activity context, call getBaseContext()
	 * @param type
	 * 					indicates which method to callup upon user input, for
	 * 					example InputPopup.Type.username for username input
	 */
	public static void make(String title, String message,  final Context context, final Type type) {
	//public static void make(String Title, String Message,  Context context, final StringBuffer returnValue) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle(title);
		alert.setMessage(message);

		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				// Do something with value!
				//returnValue.append(value.toString());	//Old experiment, REFACTOR MAYBE
				
				/* Used switch statement instead of polymorphic interfaces because
				 * that would require overhead of creating objects for EVERY 'set' method
				 * called here. Also, case statements are only a bad smell if they're
				 * duplicated in several places.
				 */
				switch (type) {
				case username:
					Preferences.setUsername(context, value.toString(), true);
				case password:
					Preferences.setPassword(context, value.toString(), true);
				case email:
					Preferences.setEmail(context, value.toString(), true);
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				//returnValue.append("");
			}
		});

		alert.show();

	}
}
