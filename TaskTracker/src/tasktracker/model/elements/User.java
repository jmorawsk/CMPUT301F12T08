package tasktracker.model.elements;

import tasktracker.model.enums.NotificationType;

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

/**
 * An application user. The user has a username (currently optional) and a
 * unique ID number. The user is able to fulfill tasks of which they are a
 * member.
 * 
 * @author Jeanine Bonot
 * 
 */
public class User {
	private static int _userCount = 0;

	private String name;
	private int id;

	/**
	 * Creates a new instance of the User class. A unique ID number is assigned
	 * and the name is set to a default value.
	 */
	public User() {
		this.id = ++_userCount;
		this.name = "user" + this.id;
	}

	/**
	 * Creates a new instance of the User class.
	 * 
	 * @param name
	 *            The name of the user.
	 */
	public User(String name) {
		if (!this.assignName(name)) {
			// Assignment was unsuccessful, notify app user.
		} else {
			this.id = ++_userCount;
		}

	}

	/**
	 * Gets the name of the user.
	 * 
	 * @return The name of the user.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the user name if the new name is available.
	 * 
	 * @param name
	 *            The user name.
	 * @return True if the new name was set, false otherwise.
	 */
	public void setName(String name) {

		if (!this.assignName(name)) {
			// Notify user that nothing was changed.
		}

	}

	/** Gets the ID number of the user */
	public int getID() {
		return this.id;
	}

	/**
	 * Fulfill a task. Checks that the user is a member of the task, then goes
	 * through each of the task's requirements and fulfills them.
	 * 
	 * @param task
	 *            The task to fulfill.
	 */
	public void fulfillTask(TaskElement task) {
		if (!task.getMembers().contains(this)) {
			// User is not a member, cannot fulfill this task.
			// TODO: Notify user that they are unable to fulfill task.
			return;
		}

		if (task.fulfill()) {
			// Task has been fulfilled, notify the creator.
			NotificationElement notification = new NotificationElement(
					this.name, task, NotificationType.FulfillmentReport);
			// TODO: Send notification.
		}
	}

	/**
	 * Sets the name if the name is available in the system.
	 * 
	 * @param name
	 *            The name to be validated.
	 * @return True if the name was successfully set, false otherwise.
	 */
	private boolean assignName(String name) {
		// TODO: Check online database.
		
		// TODO: Must check that the user has not tried to use name "user<int>"
		this.name = name;
		return true;
	}
}
