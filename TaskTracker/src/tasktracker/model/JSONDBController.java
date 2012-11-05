package tasktracker.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.google.gson.Gson;

import tasktracker.model.elements.Task;

/**
 * A class for interacting with a JSON web database (webserver)
 * @author Jason
 *
 */
public class JSONDBController
{
    // JSON Utilities
    private static Gson gson = new Gson();
    //index of 'content' for objects in database
    static int contentIndex = 1;
    //location of our webserver
    static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";

    /**
     * Queries the webserver for all tasks.
     * Returns an array of string arrays. Index first by the task
     * you want to look at, and then by the property of the task.
     * Order of properties should conform to task class.
     * Returns null if there are no tasks stored
     * @return an array of arrays of task property values
     */
    public static String[][] listTasksAsArrays(){
        //command in JSON
        String listCommand = "action=" + "list";
        return JSONDBParser.parseJSONArray(executeAction(listCommand));
    } 
    /**
     * Queries the database for all tasks.
     * Returns an array of tasks.
     * Returns null if there are no tasks stored
     * @return an array of tasks
     */
    public static Task[] listTasks(){
        //command in JSON
        String listCommand = "action=" + "list";
        //TODO
        //return JSONDBParser.parseJSONArray(executeAction(listCommand));
        return null;
    }


    /**
     * Adds a task to the webserver.
     * Returns a string array.
     * The array contains the properties of the added task.
     * Order of returned properties should conform to task class.
     * @param summary           the summary for the task
     * @param description       the description of the task
     * @return  an array of task property values.
     */
    public static String[] insertTask(String summary, String description){
        //command in JSON
        String insertCommand = "action=" + "post"
                + "&summary=" + summary.replace(' ', '+')
                + "&description=" + description.replace(' ', '+');
        return JSONDBParser.parseJSONObject(executeAction(insertCommand));
    }
    /**
     * A method for adding a task to the JSON db
     * @param task the task to be added
     * @return a string array of what was added to the db
     *          0       summary
     *          1       content
     *          2       id
     *          3       description
     */
    public static String[] insertTask(Task task){
        String content = null;
        content = gson.toJson(task);
        String insertCommand = "action=" + "post"
                + "&summary=" + task.getDescription().replace(' ', '+')
                + "&content=" + content
                + "&description=" + task.getDescription().replace(' ', '+');
        return JSONDBParser.parseJSONObject(executeAction(insertCommand));
    }

    /**
     * Updates a task on the webserver.
     * Returns a string array.
     * The array contains the properties of the updated task.
     * Order of returned properties should conform to task class.
     * @param newSummary        the new summary for the task
     * @param newDescription    the new description of the task
     * @param id                the id of the task to be updated    
     * @return  an array of task property values.
     */
    public static String[] updateTask(String newSummary, String newDescription, String id){
        String updateCommand = "action=" + "update"
                + "&summary=" + newSummary.replace(' ', '+')
                + "&description=" + newDescription .replace(' ', '+')
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(updateCommand));
    }

    /**
     * Gets a task from the database.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    public static Task getTask(String id){
        Task myTask = null;
        String myContent = getTaskAsArray(id)[contentIndex];

        myTask = gson.fromJson(myContent, Task.class);
        
        return myTask;
    }
    /**
     * Gets a task from the webserver.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    public static String[] getTaskAsArray(String id){
        String getCommand = "action=" + "get"
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(getCommand));
    }
    
    /**
     * Removes a task from the webserver.
     * Returns a string array.
     * The array contains the id the task in index 0.
     * The array contains a message at index 1
     * -the message is 'removed' if this was succesful
     * Order of returned properties should conform to task class.
     * @param id        the id of the task to be removed
     * @return
     */
    public static String[] removeTask(String id){
        String getCommand = "action=" + "remove"
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(getCommand));
    }

    /**
     * Removes all tasks from the database
     */
    protected static String nukeAll(){
        String nukeCommand = "action=" + "nuke"
                + "&key=" + "judgedredd";
        return executeAction(nukeCommand);
    }
    /**
     * A method to execute an action on a webpage
     * @param action    the action to execute on the page
     * @return a string for the result of the action
     */
    protected static String executeAction(String action){
        URI uri = null;
        //construct our uri
        try
        {
            uri = new URI(
                    "http",
                    "crowdsourcer.softwareprocess.es",
                    "/F12/CMPUT301F12T08/",
                    action,
                    null
                    );
        } catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //actually make web request
        return readFromURL(uri.toASCIIString());

    }
    protected static String oldExecuteAction(String action){
        return readFromURL(webAddress + "?" + action);

    }
    /**
     * Method to read a string from a webpage
     * @param URL the string of the url for the webpage
     * @return a string of what is on the webpage
     */
    protected static String readFromURL(String url){
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
    }
}
