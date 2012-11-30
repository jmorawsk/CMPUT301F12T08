package tasktracker.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.elements.Notification;
import tasktracker.model.elements.Task;
import tasktracker.view.CreateTaskView;
import tasktracker.view.ToastCreator;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/*
 * @author Mostly Jason, modified by Mike
 * 
 * Create a new instance and run .execute(String URL) to access a URL
 * 
 * To run something on the returned data, change the followUpMethod int to that of a pre-entered method.
 * 0=run nothing,
 * 1=on an instance of DatabaseAdapter, call addDownloadedTaskSummariesToDatabase() (assuming a querry was run)
 *
 * WARNING: 
 */
public class ReadFromURL extends AsyncTask<String,Void,String>
{

	public int followUpMethod = 0;
	private Context context;
	private boolean contextSet = false;
	
	/*
	 * Context is required to display result toasts and for many followUpMethods
	 */
	public void setContext(Context cont){
		context = cont;
		contextSet = true;
	}
	
    protected String doInBackground(String... urls)
    {
        String readLine = null;
        try
        {
            URL webServer;
            webServer =new URL(urls[0]);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(webServer.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null){
                readLine = inputLine;
            }

            in.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return readLine;
    }
    
    protected void onPostExecute(String line) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    	//ToastCreator.showLongToast(getBaseContext(), "Task created! DB summary: n/a");
    		//TODO: Find some way to show a popup without requiring passing a Context through 15 methods
    	switch (followUpMethod){
    	case 0:
        	if (contextSet){
	    		Toast toast = Toast.makeText(context, "URL accessed", Toast.LENGTH_SHORT);
	            toast.show();
        	}
    	case 1:
    		if (contextSet) addDownloadedTaskSummariesToDatabase(line);
    	}
    }
    
    /*
     * This will return the string after a given tag.
     * ie, if input = "<task> This is my Task <date> November 29 <creator> Mike Dardis", and
     * tag = "<date>", this will return " November 29 ".
     * 
     * Search quits upon finding a '}' or end of input string.
     * Output string ends at: '<', '"', or end of input string.
     * 
     * Returns null if not found
     * 
     * @param tag 		The tag preceeding the input you want
     * @param input		The string of input to look in
     * @param start		The character number to start searching at
     * @return			The found string or null if not found
     */
    private String getTaggedText(String tag, String input, int start){
    	//Not tested yet
    	String value = "";
    	int pos = -1;
    	tag = tag;	//TODO not finding tag
    	pos = input.indexOf(tag, start);
    	if (pos == -1) return null;	//Not found
    	if (pos > input.indexOf('}', start)) return null;	//String found outside of entry brackets
    	pos = pos + tag.length();
    	//Record string until end state found
    	while(pos < input.length()){
    		value = value + input.charAt(pos);
    		pos += 1;
    		if (input.charAt(pos) == '<' || input.charAt(pos) == '\"') break;
    	}
    	return value;
    }
    
    private void addDownloadedTaskSummariesToDatabase(String line){
    	DatabaseAdapter _dbHelper = new DatabaseAdapter(context);
		// Add to SQL server
		_dbHelper.open();
		
		Task task = new Task("null");
		String taskName, description, date, creatorID, likes, content, id = "";
		boolean requiresPhoto = false, requiresText = false;
		int pos = 0;
		
		//Get all of the Tasks from the downloaded string
		while(pos < line.length()){
			pos = line.indexOf("{\"summary\":\"", pos);
			if (pos == -1) break;
			pos = pos + "{\"summary\":\"".length();
			
			taskName = getTaggedText("<Task>", line, pos);
			
			if (taskName != null){
				//Task found, parse for its summary...
				creatorID = getTaggedText("<CreatorID>", line, pos);
				description = getTaggedText("<Description>", line, pos);
				date = getTaggedText("<Date>", line, pos);
				likes = getTaggedText("<Likes>", line, pos);
				//content = getTaggedText("<Content>", line, pos);
				id = getTaggedText(",\"id\":\"", line, pos);
				if (getTaggedText("<requiresPhoto>", line, pos) == "true") requiresPhoto = true;
				if (getTaggedText("<requiresText>", line, pos) == "true") requiresText = true;
				//Create object...
				if (creatorID != null
					&& description != null
					&& date != null
					&& likes != null){
					//TODO retrieve the user's name via their ID from the users list
					task = new Task("the internet");
					task.setName(taskName);
					task.setDescription(description);
					task.setID(id);
					task.setIsDownloaded("No");
					task.setDate(date);
					task.setPhotoRequirement(requiresPhoto);
					task.setTextRequirement(requiresText);
					//TODO set likes
					//TODO set other members?
					task.setOtherMembers("");
					task.setIsPrivate(false);
					//Add to local SQL database
					addNewTask(task, task.getCreator(), _dbHelper);
				}
				//break;
			}
		}
		Toast toast = Toast.makeText(context, "First item found at " + pos + ", it starts with " + task.getID(), Toast.LENGTH_SHORT);
		toast.show();
		
		/*
		Task task = new Task(Preferences.getUsername(getBaseContext()));

		task.setDescription(_description.getText().toString());
		task.setName(_name.getText().toString());
		task.setPhotoRequirement(_photo.isChecked());
		task.setTextRequirement(_text.isChecked());
		task.setOtherMembers(_otherMembers.getText().toString());
		task.setIsPrivate(_private.isChecked());
		
		task.setIsDownloaded("Yes");	//Since it was created on this phone, it's already in the SQL table
		*/
		
		_dbHelper.close();
    }
    
    private void addNewTask(Task task, String _user, DatabaseAdapter _dbHelper){
		List<String> others = task.getOtherMembers();
	
		// Add to SQL server
		_dbHelper.open();
		long taskID = _dbHelper.createTask(task);
	
		String taskName = task.getName();
		String message = Notification.getMessage(_user, taskName,
				Notification.Type.InformMembership);
	
		_dbHelper.createMember(taskID,
				Preferences.getUsername(context));
	
		for (String member : others) {
			_dbHelper.createMember(taskID, member);
			_dbHelper.createNotification(taskID, member, message);
		}
	}
    
}
