package tasktracker.model;


import android.content.Context;
import tasktracker.model.elements.Task;
import tasktracker.model.elements.User;

/**
 * A class for interacting with a web database (webserver)
 * Currently set up to interact with a JSON database
 * Does this by calling JSONDBController
 * @author Jason
 *
 */
public class WebDBManager extends DBManager
{
    DBManager controller = new JSONDBController();
    /**
     * Queries the webserver for all tasks.
     * Returns an array of string arrays. Index first by the task
     * you want to look at, and then by the property of the task.
     * Order of properties should conform to task class.
     * Returns null if there are no tasks stored
     * @return an array of arrays of task property values
     */
    @Override
    public String[][] listTasksAsArrays(){
        return controller.listTasksAsArrays();
    } 
    /**
     * Queries the database for all tasks.
     * Returns an array of tasks.
     * Returns null if there are no tasks stored
     * @return an array of tasks
     */
    @Override
    public Task[] listTasks(){
        return controller.listTasks();
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
    @Override
    public String[] insertTask(String summary, String description){
        return controller.insertTask(summary,description);
    }
    
    //TODO: info TODO: Why is there info at EVERY step of inherritance? Sooo much redundancy!!
    @Override
    public void insertUser(User user, Context context){
    	controller.insertUser(user, context);
    }
    
    /**
     * Adds a task to the webserver.
     * Returns a string array.
     * The array contains the properties of the added task.
     * Order of returned properties should conform to task class.
     * @param task        the new task to be added 
     * @return  an array of task property values.
     */
    @Override
    public String[] insertTask(Task task){
        return controller.insertTask(task);
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
    @Override
    public String[] updateTask(String newSummary, String newDescription, String id){
        return controller.updateTask(newSummary,newDescription,id);
    }

    /**
     * Updates a task on the database.
     * Returns the updated task
     * The array contains the properties of the updated task.
     * Order of returned properties should conform to task class.
     * @param task        the updated task  
     * @return  the task
     */
    @Override
    public Task updateTask(Task task){
        return controller.updateTask(task);
    }
    
    /**
     * Gets a task from the webserver.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    @Override
    public String[] getTaskAsArray(String id){
        return controller.getTaskAsArray(id);
    }
    
    /**
     * Gets a task from the database.
     * Returns a string array.
     * The array contains the properties of the task.
     * Order of returned properties should conform to task class.
     * @param id       the id of the task to be updated    
     * @return  an array of task property values.
     */
    @Override
    public Task getTask(String id){
        return controller.getTask(id);
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
    @Override
    public String[] removeTask(String id){
        return controller.removeTask(id);
    }

    /**
     * Removes all tasks from the database.
     */
    @Override
    public String nukeAll(){
        return controller.nukeAll();
    }
}