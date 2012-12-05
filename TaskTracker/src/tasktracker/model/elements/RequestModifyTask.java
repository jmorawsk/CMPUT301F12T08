package tasktracker.model.elements;

import com.google.gson.Gson;

import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import android.content.Context;


public class RequestModifyTask implements NetworkRequestModel {
    private Context context;
    private Task task;
    private String requestString;

    static final Gson gson = new Gson();


    public RequestModifyTask(Context contex, Task theTask) {
        context = contex;
        task = theTask;
        String content = gson.toJson(task);
        //String content = gson.toJson(task.getPhotos());
        String command = "action=" + "update" + "&summary=" + task.getSummary()
                + "&content=" + content.toString()
                + "&id=" + task.getID();
        System.out.println(command);
        requestString = AccessURL.turnCommandIntoURL(command);

        AccessURL access = new AccessURL(this);
        access.execute(getCrowdsourcerCommand());
    }

    public Context getContext()
    {
        return context;
    }
    public String getCrowdsourcerCommand()
    {
        // System.out.println("Request to network: " + requestString);
        return requestString;
    }
    public void runAfterExecution(String line) {
        /*
        DatabaseAdapter _dbHelper = new DatabaseAdapter(context);

        String taskID = AccessURL.getTag("id\":\"", line,
                line.indexOf('}', 0) + 1);

        // Save task to local SQL
        List<String> others = task.getOtherMembers();

        // Add to SQL server
        _dbHelper.open();

        task.setID(taskID);

        //taskID = _dbHelper.createTask(task);
        _dbHelper.createTask(task);

        String taskName = task.getName();
        String message = Notification.getMessage(
                Preferences.getUsername(context), taskName,
                Notification.Type.InformMembership);

        //TODO: Waiting on refactor, taskID needs to be type String not long

        _dbHelper.createMember(taskID,
                Preferences.getUsername(context));

        for (String member : others) {
            _dbHelper.createMember(taskID, member);
            _dbHelper.createNotification(taskID, member, message);
        }

        _dbHelper.close();
        */

		// Toast toast = Toast.makeText(context,
		// "Updated crowdSourcer task: " + task.getName(),
		// Toast.LENGTH_SHORT);
		// toast.show();
    }
}
