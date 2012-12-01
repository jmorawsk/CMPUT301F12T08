package tasktracker.model.elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import tasktracker.model.Preferences;
import tasktracker.view.Login;

/*
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

    //static final Gson gson = new Gson();
    /** index of 'content' for objects in database */
    
	public RequestGetAUser(Context contex, String userid, boolean setToCurrentUser1){
		userID = userid;
		userName = "";
		context = contex;
		setToCurrentUser = setToCurrentUser1;
		
		//http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/?action=get&id=d5b6af608d1d5008887a10d2da5095779a6e257f
        String command = "action=" + "get&id="
        		+ userID;
		
		//TODO using ID, search based on ID

        requestString = AccessURL.turnCommandIntoURL(command);

		AccessURL access = new AccessURL(this);
		access.execute(getCrowdsourcerCommand());
	}
	
	public RequestGetAUser(Context contex, boolean setToCurrentUser1, String username){
		userName = username;
		context = contex;
		setToCurrentUser = setToCurrentUser1;
		
		//ID not known, start by listing all of the items and searching for an ID
        String command = "action=" + "list";

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
		
		if (userName != ""){
			boolean foundUser = false;
			String id, aFoundName;
			int pos = 0;
			int i = 0;
			//Search based on userName for userID
			while(pos < line.length()){
				i++;
				pos = line.indexOf("{\"summary\":\"", pos);
				//pos = line.indexOf("s", pos);
				if (pos == -1) {
					//Toast toast = Toast.makeText(context, "FOUND ENTRY AT " + i, Toast.LENGTH_SHORT);
					//Toast toast = Toast.makeText(context, "FOUND ENTRY AT " + line, Toast.LENGTH_SHORT);
					
					//toast.show();
					//System.out.println("Bug = " + line);
					break;
				}
					
				pos = pos + "{\"summary\":\"".length();
				
				//Toast toast = Toast.makeText(context, "FOUND ENTRY AT " + pos, Toast.LENGTH_SHORT);
				//toast.show();
				
				aFoundName = AccessURL.getTag("<User>", line, pos);
				if (aFoundName != null){
					//Toast toast = Toast.makeText(context, "SEARCH FOUND USER '" + aFoundName + "' at " + pos, Toast.LENGTH_SHORT);
					//toast.show();
					if (aFoundName.equals(userName)){
						foundUser = true;
	
						break;
					} //end of if statement
				}
			}	//end of while statement
			if (foundUser){
				//Run new search to get details for this userID and add to local DB etc
				id = AccessURL.getTag(",\"id\":\"", line, line.indexOf('}', 0) + 1);
				//Toast toast = Toast.makeText(context, "User " + userName + "found, ID = " + id, Toast.LENGTH_SHORT);
				//toast.show();
				
				RequestGetAUser getDetails = new RequestGetAUser(context, id, setToCurrentUser);
				
				
			} else {
				Toast toast = Toast.makeText(context, "ERROR: Username " + userName + " not found in online Database", Toast.LENGTH_SHORT);
				toast.show();
				
				Intent intent = new Intent(context, Login.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				//TODO user not found! Create?
			}
		} else {
			if (userID == ""){
				//TODO report error here, no userName or userID given
				Toast toast = Toast.makeText(context, "ERROR: No username or ID given for crowdsourcer search.", Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			//Get user info based on on userID
			
			String email, password, name;
			User user = new User("nulllet");
			
			name = AccessURL.getTag("<User>", line, 0);
			email = AccessURL.getTag("\"email\":", line, 0);
			password = AccessURL.getTag("\"password\":", line, 0);
			
			
			if (name != "" && email != "" && password != ""){
				//Valid user info
				user.setName(name);
				user.setEmail(email);
				user.setPassword(password);
				
				//Add user to local SQL db
				DatabaseAdapter _dbHelper = new DatabaseAdapter(context);
				_dbHelper.open();
				_dbHelper.createUser(user.getName(), user.getEmail(), user.getPassword(), userID);
				_dbHelper.close();
				//Set as current user?
				if (setToCurrentUser) Preferences.setPreferences(context, user.getName(), user.getEmail(), user.getPassword(), userID, true);
				
				Toast toast = Toast.makeText(context, "Downloaded user info: " + user.getName() + ". Added to login list.", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				//User gotten but not enough info
				//TODO error?
				Toast toast = Toast.makeText(context, "ERROR: User found in crowdsourcer, but lacking required info.", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		
	}
	
	
}