package tasktracker.model.elements;

/**
 * TaskTracker
 * 
 * Copyright 2012 Jeanine Bonot, Michael Dardis, Katherine Jasniewski,
 * Jason Morawski
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */

import com.google.gson.Gson;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;
import android.content.Context;

/**
 * Initiates request to load a webtask
 * Incomplete - content will be too large with photos
 * Need to compress content such that it can be sent over html
 * @author Jason
 *
 */
public class RequestModifyTask implements NetworkRequestModel {
    private Context context;
    private Task task;
    private String requestString;

    static final Gson gson = new Gson();


    public RequestModifyTask(Context contex, Task theTask) {
        context = contex;
        task = theTask;
        String content = gson.toJson(task);
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
