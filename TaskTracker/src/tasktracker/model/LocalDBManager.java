package tasktracker.model;


import tasktracker.model.elements.Task;

/**
 * A class for interacting with a local database
 * Currently set up to interact with a JSON database  (webserver)
 * Incomplete: This should be changed to a local db (SQL?)
 * @author Jason
 *
 */
public class LocalDBManager extends DBManager
{

    /**
     * Queries the webserver for all tasks.
     * Returns an array of string arrays. Index first by the task
     * you want to look at, and then by the property of the task.
     * Order of properties should conform to task class.
     * Returns null if there are no tasks stored
     * @return an array of arrays of task property values
     */
    public String[][] listTasksAsArrays(){
        return JSONDBController.listTasksAsArrays();
    } 
    /**
     * Queries the database for all tasks.
     * Returns an array of tasks.
     * Returns null if there are no tasks stored
     * @return an array of tasks
     */
    public Task[] listTasks(){
        return JSONDBController.listTasks();
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
    public String[] insertTask(String summary, String description){
        return JSONDBController.insertTask(summary,description);
    }
    
    /**
     * Adds a task to the webserver.
     * Returns a string array.
     * The array contains the properties of the added task.
     * Order of returned properties should conform to task class.
     * @param task        the new task to be added 
     * @return  an array of task property values.
     */
    public String[] insertTask(Task task){
        return JSONDBController.insertTask(task);
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
    public String[] updateTask(String newSummary, String newDescription, String id){
        return JSONDBController.updateTask(newSummary,newDescription,id);
    }

    /**
     * Gets a task from the webserver.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    public String[] getTaskAsArray(String id){
        return JSONDBController.getTaskAsArray(id);
    }
    
    /**
     * Gets a task from the database.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    public Task getTask(String id){
        return JSONDBController.getTask(id);
    }
    /**
     * Removes a task from the webserver.
     * Returns a string array.
     * The array contains the id the task in index 0.
     * The array contains a message at index 1
     * -the message is 'removed' if this was succesful
     * Order of returned properties should conform to task class.
     * @param id
     * @return
     */
    public String[] removeTask(String id){
        return JSONDBController.removeTask(id);
    }

    /**
     * Removes all tasks from the database.
     */
    protected String nukeAll(){
        return JSONDBController.nukeAll();
    }
}