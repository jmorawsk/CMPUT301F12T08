package tasktracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.widget.EditText;

public class InputPopup {
	
	String returnValue = "default";
	
	public String makePopup(String Title, String Message,  Context context) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		
		alert.setTitle(Title);
		alert.setMessage(Message);
	
		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		alert.setView(input);
	
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  Editable value = input.getText();
		  //preferences.setUsername(getBaseContext(), value.toString());
		  // Do something with value!
		  returnValue = value.toString();
		  }
		});
	
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
	
		alert.show();
		
		return "done";//returnValue;
	}
}
