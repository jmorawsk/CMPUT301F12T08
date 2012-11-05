package tasktracker.model;

import tasktracker.model.elements.Task;

public abstract class DBManager
{
    String webAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";

    //static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/Team1/";

    /**
     * Queries the database for all tasks.
     * Returns an array of string arrays. Index first by the task
     * you want to look at, and then by the property of the task.
     * Order of properties should conform to task class.
     * Returns null if there are no tasks stored
     * @return an array of arrays of task property values
     */
    public abstract String[][] listTasksAsArrays();
    
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
     * Gets a task from the database.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    public abstract Task getTask(String id);
    
    /**
     * Removes a task from the database.
     * Returns a string array.
     * The array contains the id the task in index 0.
     * The array contains a message at index 1
     * -the message is 'removed' if this was succesful
     * Order of returned properties should conform to task class.
     * @param id
     * @return
     */
    public abstract String[] removeTask(String id);

    protected abstract String nukeAll();
}