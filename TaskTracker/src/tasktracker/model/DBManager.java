package tasktracker.model;

import tasktracker.model.elements.Task;

/**
 * An abstract class for interacting with a database
 * These methods should be implemented by classes that
 * interact with some type of database
 * @author Jason
 *
 */
public abstract class DBManager
{
    /**
     * Queries the database for all tasks.
     * Returns an array of string arrays. Index first by the task
     * you want to look at, and then by the property of the task.
     * Order of properties should conform to task class.
     * Returns null if there are no tasks stored
     * @return an array of arrays of task property values
     */
    public abstract String[][] listTasksAsArrays();
    
    /**
     * Queries the database for all tasks.
     * Returns an array of tasks.
     * Returns null if there are no tasks stored
     * @return an array of tasks
     */
    public abstract Task[] listTasks();

    
    /**
     * Adds a task to the database.
     * Returns a string array.
     * The array contains the properties of the added task.
     * Order of returned properties should conform to task class.
     * @param summary           the summary for the task
     * @param description       the description of the task
     * @return  an array of task property values.
     */
    public abstract String[] insertTask(String summary, String description);
    
    /**
     * Adds a task to the database.
     * Returns a string array.
     * The array contains the properties of the added task.
     * Order of returned properties should conform to task class.
     * @param task           the task to be added
     * @return  an array of task property values.
     */
    public abstract String[] insertTask(Task task);

    /**
     * Updates a task on the database.
     * Returns a string array.
     * The array contains the properties of the updated task.
     * Order of returned properties should conform to task class.
     * @param newSummary        the new summary for the task
     * @param newDescription    the new description of the task
     * @param id                the id of the task to be updated    
     * @return  an array of task property values.
     */
    public abstract String[] updateTask(String newSummary, String newDescription, String id);

    /**
     * Updates a task on the database.
     * Returns the updated task
     * The array contains the properties of the updated task.
     * Order of returned properties should conform to task class.
     * @param task        the updated task  
     * @return  the task
     */
    public abstract Task updateTask(Task task);
    
    /**
     * Gets a task from the database.
     * Returns the task.
     * @param id       the id of the task to be updated    
     * @return  the desired task.
     */
    public abstract Task getTask(String id);
    
    /**
     * Gets a task from the database.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    public abstract String[] getTaskAsArray(String id);
    
    /**
     * Removes a task from the database.
     * Returns a string array.
     * The array contains the id the task in index 0.
     * The array contains a message at index 1
     * -the message is 'removed' if this was succesful
     * Order of returned properties should conform to task class.
     * @param id
     * @return a string array with completion message
     */
    public abstract String[] removeTask(String id);

    /**
     * Removes all tasks from the database.
     */
    public abstract String nukeAll();
}