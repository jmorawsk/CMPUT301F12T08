package tasktracker.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.elements.Notification;
import tasktracker.model.elements.Task;
import tasktracker.model.elements.User;
import tasktracker.view.CreateTaskView;
import tasktracker.view.ToastCreator;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/*
 * @author Mike, copied base code from Jason's old ReadFromURL
 * 
 * DO NOT RUN THIS CLASS DIRECTLY, go through AccessURLRequest
 */
public class AccessURL extends AsyncTask<String, Void, String> {
	NetworkRequestModel request;

	public AccessURL(NetworkRequestModel reques) {
		request = reques;
	}

	protected String doInBackground(String... urls) {
		String readLine = null;
		try {
			URL webServer;
			webServer = new URL(urls[0]);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					webServer.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				readLine = inputLine;
			}

			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readLine;
	}

	/*
	 * Hand this method a command like 'action=post&summary=" ...' (note the
	 * lack of a preceeding '&'), this will hand back a valid URL to run it on
	 * crowdsourcer.
	 * 
	 * Static method.
	 */
	public static String turnCommandIntoURL(String command) {
		URI uri = null;
		try {
			uri = new URI("http", "crowdsourcer.softwareprocess.es",
					"/F12/CMPUT301F12T08/", command, null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri.toASCIIString();
	}

	/*
	 * This will return the string after a given tag. ie, if input =
	 * "<task> This is my Task <date> November 29 <creator> Mike Dardis", and
	 * tag = "<date>", this will return " November 29 ".
	 * 
	 * Search quits upon finding a '}' or end of input string. Output string
	 * ends at: '<', '"', or end of input string.
	 * 
	 * Returns null if not found
	 * 
	 * @param tag The tag preceeding the input you want
	 * 
	 * @param input The string of input to look in
	 * 
	 * @param start The character number to start searching at
	 * 
	 * @return The found string or null if not found
	 */
	public static String getTag(String tag, String input, int start) {
		// Not tested yet
		String value = "";
		int pos = -1;
		tag = tag; // TODO not finding tag
		pos = input.indexOf(tag, start);
		if (pos == -1)
			return null; // Not found

		if (pos > input.indexOf('}', start))
			return null; // String found outside of entry brackets

		pos = pos + tag.length();
		// Record string until end state found

		while (pos < input.length()) {
			value = value + input.charAt(pos);
			pos += 1;
			if (input.charAt(pos) == '<' || input.charAt(pos) == '\"')
				break;
		}
		return value;
	}

	public static String readNextTag(String input, int pos) {
		String tag = "";
		int upperBound = input.length();

		while (input.charAt(pos) != '<' && pos < upperBound)
			pos++;

		if (pos == upperBound)
			return null;

		while (input.charAt(pos) != '>' && pos < upperBound)
			tag += input.charAt(pos++);

		if (pos == upperBound)
			return null;

		return tag;
	}

	// Must be read after reading the tag.
	public static String readNextValue(String input, int pos) {
		String value = "";
		int upperBound = input.length();

		while (input.charAt(pos) != '<' && pos < upperBound) {
			value += input.charAt(pos++);
		}

		if (pos == upperBound)
			return null;

		return value;
	}

	protected void onPostExecute(String line) {
		// TODO: check this.exception
		// TODO: do something with the feed

		request.runAfterExecution(line);

		/*
		 * //ToastCreator.showLongToast(getBaseContext(),
		 * "Task created! DB summary: n/a"); //TODO: Find some way to show a
		 * popup without requiring passing a Context through 15 methods switch
		 * (followUpMethod){ case 0: if (contextSet){ Toast toast =
		 * Toast.makeText(context, "URL accessed", Toast.LENGTH_SHORT);
		 * toast.show(); } case 1: if (contextSet)
		 * addDownloadedTaskSummariesToDatabase(line); case 2: if (contextSet)
		 * addUserToSQL(line); default: if (contextSet){ Toast toast =
		 * Toast.makeText(context,
		 * "Bad followUpMethod number chosen (ReadFromURL): " + followUpMethod,
		 * Toast.LENGTH_SHORT); toast.show(); } }
		 */
	}

	/*
	 * This will return the string after a given tag. ie, if input =
	 * "<task> This is my Task <date> November 29 <creator> Mike Dardis", and
	 * tag = "<date>", this will return " November 29 ".
	 * 
	 * Search quits upon finding a '}' or end of input string. Output string
	 * ends at: '<', '"', or end of input string.
	 * 
	 * Returns null if not found
	 * 
	 * @param tag The tag preceeding the input you want
	 * 
	 * @param input The string of input to look in
	 * 
	 * @param start The character number to start searching at
	 * 
	 * @return The found string or null if not found
	 */
	private String getTaggedText(String tag, String input, int start) {
		// Not tested yet
		String value = "";
		int pos = -1;
		tag = tag; // TODO not finding tag
		pos = input.indexOf(tag, start);
		if (pos == -1)
			return null; // Not found
		if (pos > input.indexOf('}', start))
			return null; // String found outside of entry brackets
		pos = pos + tag.length();
		// Record string until end state found
		while (pos < input.length()) {
			value = value + input.charAt(pos);
			pos += 1;
			if (input.charAt(pos) == '<' || input.charAt(pos) == '\"')
				break;
		}
		return value;
	}

	/*
	 * private void addDownloadedTaskSummariesToDatabase(String line){
	 * //DatabaseAdapter _dbHelper = new DatabaseAdapter(context); // Add to SQL
	 * server //_dbHelper.open();
	 * 
	 * Task task = new Task("null"); String taskName, description, date,
	 * creatorID, likes, content, id = ""; boolean requiresPhoto = false,
	 * requiresText = false; int pos = 0;
	 * 
	 * //Get all of the Tasks from the downloaded string while(pos <
	 * line.length()){ pos = line.indexOf("{\"summary\":\"", pos); if (pos ==
	 * -1) break; pos = pos + "{\"summary\":\"".length();
	 * 
	 * taskName = getTaggedText("<Task>", line, pos);
	 * 
	 * if (taskName != null){ //Task found, parse for its summary... creatorID =
	 * getTaggedText("<CreatorID>", line, pos); description =
	 * getTaggedText("<Description>", line, pos); date = getTaggedText("<Date>",
	 * line, pos); likes = getTaggedText("<Likes>", line, pos); //content =
	 * getTaggedText("<Content>", line, pos); id = getTaggedText(",\"id\":\"",
	 * line, pos); if (getTaggedText("<requiresPhoto>", line, pos) == "true")
	 * requiresPhoto = true; if (getTaggedText("<requiresText>", line, pos) ==
	 * "true") requiresText = true; //Create object... if (creatorID != null &&
	 * description != null && date != null && likes != null){ //TODO retrieve
	 * the user's name via their ID from the users list task = new
	 * Task("the internet"); task.setName(taskName);
	 * task.setDescription(description); task.setID(id);
	 * task.setIsDownloaded("No"); task.setDate(date);
	 * task.setPhotoRequirement(requiresPhoto);
	 * task.setTextRequirement(requiresText); //TODO set likes //TODO set other
	 * members? Is this relevent for a downloaded task?
	 * task.setOtherMembers(""); task.setIsPrivate(false); //Add to local SQL
	 * database //addNewTask(task, task.getCreator(), _dbHelper); } //break; } }
	 * Toast toast = Toast.makeText(context, "First item found at " + pos +
	 * ", it starts with " + task.getID(), Toast.LENGTH_SHORT); toast.show();
	 * 
	 * _dbHelper.close(); }
	 * 
	 * private void addNewTask(Task task, String _user, DatabaseAdapter
	 * _dbHelper){ List<String> others = task.getOtherMembers();
	 * 
	 * // Add to SQL server _dbHelper.open(); long taskID =
	 * _dbHelper.createTask(task);
	 * 
	 * String taskName = task.getName(); String message =
	 * Notification.getMessage(_user, taskName,
	 * Notification.Type.InformMembership);
	 * 
	 * _dbHelper.createMember(taskID, Preferences.getUsername(context));
	 * 
	 * for (String member : others) { _dbHelper.createMember(taskID, member);
	 * _dbHelper.createNotification(taskID, member, message); } }
	 * 
	 * /* Puts the new user into the SQL table with the crowdsourcer ID as the
	 * user ID
	 */
	/*
	 * private void addUserToSQL(String line){ if (!userSet){ return; } else {
	 * DatabaseAdapter _dbHelper = new DatabaseAdapter(context); // Add to SQL
	 * server _dbHelper.open();
	 * 
	 * //TODO: Put the user object in the database with the ID from crowdsourcer
	 * 
	 * //_dbHelper.createUser(username, email, password); Toast toast =
	 * Toast.makeText(context, "Added user: " + user.getName(),
	 * Toast.LENGTH_SHORT); toast.show();
	 * 
	 * _dbHelper.close(); } }
	 */
}
