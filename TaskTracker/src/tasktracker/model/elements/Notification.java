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

import java.util.*;

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

	private long timeInMillis;
	private boolean viewed;
	private String message;
	private Task task;
	private String sender;

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
		this.sender = sender;
		this.viewed = false;
		this.timeInMillis = Calendar.getInstance().getTimeInMillis();
		this.setMessage(type);
	}

	/** Mark the notification as viewed by the receiver */
	public void markAsViewed() {
		this.viewed = true;
	}

	/** Checks if the receiver viewed the notification */
	public boolean hasBeenViewed() {
		return this.viewed;
	}

	/**
	 * Gets the time the notification was created in milliseconds. Used for
	 * sorting multiple notifications.
	 */
	public long getTimeInMillis() {
		return this.timeInMillis;
	}

	/** Gets the task associated with the notification */
	public Task getTaskElement() {
		return this.task;
	}

	/** Gets the notification's message string */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the notification's message string according to the notification's
	 * type.
	 * 
	 * @param type
	 *            The type of notification being sent
	 */
	private void setMessage(Type type) {
		switch (type) {
		case FulfillmentReport:
			// TODO: add date
			this.message = String.format(
					"\"%s\" was fulfilled by %s on <Date>",
					this.task.getName(), this.sender);
			break;
		case InformDelete:
			// TODO: Set message
			break;
		case InformEdit:
			// TODO: Set message
			break;
		case InformMembership:
			// TODO: Set message
			break;
		}
	}
}
