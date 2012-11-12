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

import java.text.SimpleDateFormat;
import java.util.*;

import tasktracker.controller.DatabaseAdapter;

import android.content.ContentValues;

/**
 * NotificationElement class
 * 
 * A class representing a notification. A notification is sent to a user
 * regarding a given task. Notifications can be sent to inform a creator that
 * their task has been fulfilled, to inform members that a task has been edited
 * or deleted, or to inform a user that they have become a member of a task.
 * 
 * @author Jeanine Bonot
 * 
 */
public class Notification {

	/** Indicates the type of notification */
	public enum Type {
		// Task has been fulfilled.
		FulfillmentReport,

		// Task has been deleted.
		InformDelete,

		// Task has been edited.
		InformEdit,

		// User has become a member of a task.
		InformMembership
	};

	private String _date;
	private boolean _viewed;
	private Task _task;
	private String _sender;
	private Type _type;

	/**
	 * Creates a new instance of the Notification class.
	 * 
	 * @param sender
	 *            The name of the user sending the notification.
	 * @param task
	 *            The task that the notification regards.
	 * @param type
	 *            The type of notification.
	 */
	public Notification(String sender, Task task, Type type) {
		Date date = Calendar.getInstance().getTime();

		_sender = sender;
		_viewed = false;
		_date = new SimpleDateFormat("yyyy-MM-dd").format(date);
		_type = type;
	}

	/** Mark the notification as viewed by the receiver */
	public void markAsViewed() {
		_viewed = true;
	}

	/** Checks if the receiver viewed the notification */
	public boolean hasBeenViewed() {
		return _viewed;
	}

	/** Gets the task associated with the notification */
	public Task getTaskElement() {
		return _task;
	}

	/**
	 * Sets the notification's message string according to the notification's
	 * type.
	 * 
	 * @param type
	 *            The type of notification being sent
	 */
	public String getMessage() {
		switch (_type) {
		case FulfillmentReport:
			return String.format("\"%s\" was fulfilled by %s on %s.",
					_task.getName(), _sender, _date);
		case InformDelete:
			return String.format("%s deleted \"%s\".", _sender, _task.getName());
		case InformEdit:
			return String.format("%s made changes to \"%s\".", _sender, _task.getName());
		case InformMembership:
			return String.format("%s has made you a member of \"%s\".", _sender, _task.getName());
		default:
			return String.format("Unknown notification for \"%s\".", _task.getName());
		}
	}

	public ContentValues getContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseAdapter.TASK, _task.getName());
		initialValues.put(DatabaseAdapter.DATE, _date);
		initialValues.put(DatabaseAdapter.USER, _sender);
		initialValues.put(DatabaseAdapter.TYPE, _type.ordinal());
		initialValues.put(DatabaseAdapter.VIEWED, false);
		return initialValues;
	}

}
