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

/**
 * An application user. The user has a username (currently optional) and a
 * unique ID number. The user is able to fulfill tasks of which they are a
 * member.
 * 
 * @author Jeanine Bonot
 * 
 */
public class User {

	private String name;
	private String email;
	private String password;
	private String id;

	/**
	 * Creates a new instance of the User class. A unique ID number is assigned
	 * and the name is set to a default value.
	 */
	public User() {
		//this.id = ++_userCount;	//TODO: Change to crowdsourcer return value
		this.id = "ID NOT SET YET";
		this.name = "user" + this.id;
		this.email = "";
		this.password = "";
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
			//this.id = ++_userCount;	//TODO: Change to crowdsourcer return value
			this.id = "ID NOT SET YET";
		}
		this.email = "";
		this.password = "";
	}

	/**
	 * Gets the name of the user.
	 * 
	 * @return The name of the user.
	 */
	public String getName() {
		return this.name;
	}
	
	/** Gets the ID number of the user */
	public String getID() {
		return this.id;
	}

	public String getEmail(){
		return this.email;
	}
	
	public String getPassword(){
		return this.password;
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
	
	/*
	 * Sets the user email
	 * NOTE: Does not check if email if valid!
	 */
	public void setEmail(String value){
		email = value;
	}

	public void setPassword(String value){
		password = value;
	}

	public void setID(String value){
		id = value;
	}
	
//	/**
//	 * Fulfill a task. Checks that the user is a member of the task, then goes
//	 * through each of the task's requirements and fulfills them.
//	 * 
//	 * @param task
//	 *            The task to fulfill.
//	 */
//	public void fulfillTask(Task task) {
//		if (!task.getMembers().contains(this)) {
//			// User is not a member, cannot fulfill this task.
//			// TODO: Notify user that they are unable to fulfill task.
//			return;
//		}
//
//		if (task.fulfill()) {
//			// Task has been fulfilled, notify the creator.
//			Notification notification = new Notification(
//					this.name, task, Notification.Type.FulfillmentReport);
//			// TODO: Send notification.
//		}
//	}

	/**
	 * Checks if objects (Users) are equal based on their ID number.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() == obj.getClass())
			return false;

		User other = (User) obj;
		if (this.id != other.id)
			return false;
		return true;
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
