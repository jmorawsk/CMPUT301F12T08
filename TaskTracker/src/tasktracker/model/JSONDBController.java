package tasktracker.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import tasktracker.model.elements.Task;
import tasktracker.view.CreateTaskView;
import tasktracker.view.ToastCreator;

/**
 * A class for interacting with a JSON web database (webserver)
 * 
 * @author Jason
 * 
 */
public class JSONDBController extends DBManager {
    // Initialize the JSONParser from the JSON_simple jar
    static final JSONParser parser = new JSONParser();
    // JSON Utilities
    static final Gson gson = new Gson();
    /** index of 'content' for objects in database */
    static final int contentIndex = 1;
    /** index of 'id' for objects in database */
    static final int idIndex = 2;
    // location of our webserver
    static final String webAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";

    /**
     * This method takes in a string that represents a JSONObject and parses it,
     * returning an array of the objects values
     * 
     * @param objectString
     *            a string that can be cast as JSONObject
     * @return a string array of the values of the JSONObject
     */
    public static String[] parseJSONObject(String objectString) {
        // initialize our output array
        String[] myTask = null;
        try {
            // create a JSONObject from our input string
            JSONObject myJSONObject = (JSONObject) parser.parse(objectString);
            // get the keys from our JSONObject
            Object[] keys = myJSONObject.keySet().toArray();
            myTask = new String[keys.length];
            // for each key, add the value to the output array
            for (int i = 0; i < keys.length; i++) {

                myTask[i] = myJSONObject.get(keys[i]).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myTask;
    }

    /**
     * 
     * @param objectString
     *            a string that can be cast to a JSONArray
     * @return an array of string arrays, first index is selecting the
     *         JSONObject, the second index is the values of the object
     */
    public static String[][] parseJSONArray(String objectString) {
        // initialize our output matrix
        String[][] parsedString = null;
        Object myTask;
        try {
            // create our JSONArray
            myTask = parser.parse(objectString);
            JSONArray array = (JSONArray) myTask;
            if (array.size() == 0)
                return null;
            JSONObject myJSONObject;
            parsedString = new String[array.size()][];
            // for each JSONObject in the array, we want to parse
            for (int n = 0; n < array.size(); n++) {
                myJSONObject = (JSONObject) array.get(n);
                parsedString[n] = parseJSONObject(myJSONObject.toJSONString());
                // System.out.println("Line " +n+ ": " +parsedString[n]);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(parsedString);
        return parsedString;
    }

    /**
     * Queries the webserver for all tasks. Returns an array of string arrays.
     * Index first by the task you want to look at, and then by the property of
     * the task. Order of properties should conform to task class. Returns null
     * if there are no tasks stored
     * 
     * @return an array of arrays of task property values 0 summary 1 id
     */
    @Override
    public String[][] listTasksAsArrays() {
        // command in JSON
        String listCommand = "action=" + "list";
        return parseJSONArray(executeAction(listCommand));
    }

    /**
     * INCOMPLETE: DO NOT USE Queries the database for all tasks. Returns an
     * array of tasks. Returns null if there are no tasks stored
     * 
     * @return an array of tasks
     */
    @Override
    public Task[] listTasks() {
        // command in JSON
        String listCommand = "action=" + "list";
        // TODO
        // return parseJSONArray(executeAction(listCommand));
        return null;
    }

    /**
     * Adds a task to the webserver. Returns a string array. The array contains
     * the properties of the added task. Order of returned properties should
     * conform to task class.
     * 
     * @param summary
     *            the summary for the task
     * @param description
     *            the description of the task
     * @return an array of task property values. 0 summary 1 content 2 id 3
     *         description
     */
    @Override
    public String[] insertTask(String summary, String description) {
        // command in JSON
        String insertCommand = "action=" + "post" + "&summary="
                + summary.replace(' ', '+') + "&description="
                + description.replace(' ', '+');
        //System.out.println(insertCommand);
        return parseJSONObject(executeAction(insertCommand));
    }

    /**
     * A method for adding a task to the JSON db
     * Nov 28: Changing this to add tags to the summary of each task entry
     * 
     * @param task
     *            the task to be added
     * @return a string array of what was added to the db 0-summary 1-content 2-id
     *         3-description
     */
    @Override
    public String[] insertTask(Task task) {
        String content = gson.toJson(task);
        String insertCommand = "action=" + "post" + "&summary=" + "<Task>" + task.getName()
                + "&content=" + content.toString() + "&description="
                + task.getName().replace(' ', '+');
        //System.out.println("*********** insertTask in JSONDBController was called!");
        return parseJSONObject(executeAction(insertCommand));
    }

    /**
     * Updates a task on the webserver. Returns a string array. The array
     * contains the properties of the updated task. Order of returned properties
     * should conform to task class.
     * 
     * @param newSummary
     *            the new summary for the task
     * @param newDescription
     *            the new description of the task
     * @param id
     *            the id of the task to be updated
     * @return an array of task property values.
     */
    @Override
    public String[] updateTask(String newSummary, String newDescription,
            String id) {
        String updateCommand = "action=" + "update" + "&summary="
                + newSummary.replace(' ', '+') + "&description="
                + newDescription.replace(' ', '+') + "&id=" + id;
        return parseJSONObject(executeAction(updateCommand));
    }

    /**
     * Updates a task on the database. Returns the updated task The array
     * contains the properties of the updated task. Order of returned properties
     * should conform to task class.
     * 
     * @param task
     *            the updated task
     * @return the task
     */
    @Override
    public Task updateTask(Task task) {
        String myContent = null;
        Task myTask = null;

        String updateCommand = "action=" + "update" + "&summary=task"
                + "&content=" + myContent + "&description="
                + task.getName().replace(' ', '+') + "&id=" + task.getID();
        myContent = parseJSONObject(executeAction(updateCommand))[contentIndex];

        myTask = gson.fromJson(myContent, Task.class);

        return myTask;
    }

    /**
     * Gets a task from the database. Returns the task
     * 
     * @param id
     *            the id of the task to be updated
     * @return the task, with updated id.
     */
    @Override
    public Task getTask(String id) {
        Task myTask = null;
        String[] taskArray = getTaskAsArray(id);
        String myContent = taskArray[contentIndex];
        String taskID = taskArray[idIndex];

        myTask = gson.fromJson(myContent, Task.class);
        myTask.setID(taskID);
        return myTask;
    }

    /**
     * Gets a task from the webserver. Returns a string array. The array
     * contains the properties of the task. Order of returned properties should
     * conform to task class.
     * 
     * @param id
     *            the id of the task to be updated
     * @return an array of task property values. 0 summary 1 content 2 id 3
     *         description
     */
    @Override
    public String[] getTaskAsArray(String id) {
        String getCommand = "action=" + "get" + "&id=" + id;
        return parseJSONObject(executeAction(getCommand));
    }

    /**
     * Removes a task from the webserver. Returns a string array. The array
     * contains the id the task in index 0. The array contains a message at
     * index 1 -the message is 'removed' if this was succesful Order of returned
     * properties should conform to task class.
     * 
     * @param id
     *            the id of the task to be removed
     * @return
     */
    @Override
    public String[] removeTask(String id) {
        String getCommand = "action=" + "remove" + "&id=" + id;
        return parseJSONObject(executeAction(getCommand));
    }

    /**
     * Removes all tasks from the database
     */
    @Override
    public String nukeAll() {
        String nukeCommand = "action=" + "nuke" + "&key=" + "judgedredd";
        return executeAction(nukeCommand);
    }

    /**
     * A method to execute an action on a webpage
     * 
     * @param action
     *            the action to execute on the page
     * @return a string for the result of the action
     */
    protected String executeAction(String action) {
        URI uri = null;
        // construct our uri
        try {
            uri = new URI("http", "crowdsourcer.softwareprocess.es",
                    "/F12/CMPUT301F12T08/", action, null);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // actually make web request
        System.out.println(uri);
        	//Jasons original
        //return readFromURL(uri.toASCIIString());
        	//Mikes new (nov 28)
			ReadFromURL myReadFromURL = new ReadFromURL();
			myReadFromURL.execute(uri.toASCIIString());
			return "";
    }

    protected String oldExecuteAction(String action) {
        return readFromURL(webAddress + "?" + action);

    }

    /**
     * Method to read a string from a webpage
     * 
     * @param URL
     *            the string of the url for the webpage
     * @return a string of what is on the webpage
     */
    protected String readFromURL(String url) {
        String readLine = null;
        try
        {
            URL webServer;
            webServer =new URL(url);

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
//        		ReadFromURL myReadFromURL = new ReadFromURL();
//        		myReadFromURL.execute(url);
//        
//        		String result = null;
//        		try {
//        			result = myReadFromURL.get();
//        		} catch (InterruptedException e) {
//        			e.printStackTrace();
//        		} catch (ExecutionException e) {
//        			e.printStackTrace();
//        		}
//        		return result;
    }

}
