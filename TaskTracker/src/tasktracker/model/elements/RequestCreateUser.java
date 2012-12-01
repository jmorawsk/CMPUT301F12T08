package tasktracker.model.elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.google.gson.Gson;

import android.content.Context;
import android.widget.Toast;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import tasktracker.model.Preferences;

/*
 * Creates an object to add a Task to Crowdsourcer when passed to ReadFromURL, THEN adds
 * the given task to the local SQL database with Crowdsourcer's returned ID.
 * 
 * Run by creating an instance.
 */
public class RequestCreateUser implements NetworkRequestModel {
	private Context context;
	private User user;
	private String requestString;

    static final Gson gson = new Gson();
    /** index of 'content' for objects in database */
    
	public RequestCreateUser(Context contex, User use){
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

		_dbHelper.close();
		
		Toast toast = Toast.makeText(context, "Win! User added to crowdSourcer: " + user.getID(), Toast.LENGTH_SHORT);
		toast.show();
		
	}
}