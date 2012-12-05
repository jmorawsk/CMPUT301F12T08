package tasktracker.model.elements;

import android.content.Context;
import android.util.Log;
import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;

public class RequestGetAllNotifications implements NetworkRequestModel {
	private Context context;
	private String requestString;

	public RequestGetAllNotifications(Context contex) {
		this.context = contex;
		this.requestString = AccessURL.turnCommandIntoURL("action=list");

		AccessURL access = new AccessURL(this);
		access.execute(getCrowdsourcerCommand());
	}

	public Context getContext() {
		return context;
	}

	public String getCrowdsourcerCommand() {
		return requestString;
	}

	public void runAfterExecution(String line) {
		DatabaseAdapter dbHelper = new DatabaseAdapter(this.context);
		dbHelper.open();

		int pos = 0;

		int upperBound = line.length();
		while (pos < upperBound) {
			pos = line.indexOf("{\"summary\":\"", pos);

			if (pos == -1)
				break;

			pos += "{\"summary\":\"".length();

			String message = AccessURL.getTag("<Notification>", line, pos);
			String taskId = AccessURL.getTag("<TaskID>", line, pos);
			String date = AccessURL.getTag("<Date>", line, pos);
			String recipientsString = AccessURL.getTag("<Recipients>", line,
					pos);
			if (recipientsString == null){
				Log.d("RequestGetAllNotifications", "null recipients string");
				continue;
			}
			
			Notification notification = new Notification(message);
			notification.setTaskId(taskId);
			notification.setRecipients(recipientsString);

			String[] recipients = notification.getRecipientsArray();
			for (String recipient : recipients) {
				long rowId = dbHelper.createNotification(taskId, recipient,
						message, date);
				Log.d("RequestGetAllNotifications", "recipient: " + recipient
						+ " rowId: " + rowId);
			}
		}

		dbHelper.close();

	}

}