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
import tasktracker.model.enums.NotificationType;

/**
 * A class representing a notification
 * @author Jeanine Bonot
 *
 */
public class NotificationElement{
	
	private long timeInMillis;
	private NotificationType type;
	private boolean viewed;
	private String message;
	private TaskElement task;
	private String sender;

	/**
	 * Creates a new instance of the Notification class.
	 */
	public NotificationElement(String sender, TaskElement task, NotificationType type)
	{
		this.sender = sender;
		this.viewed = false;
		this.timeInMillis = Calendar.getInstance().getTimeInMillis();
		this.setMessage();
	}
	
	public void markAsViewed(){
		this.viewed = true;
	}
	
	public boolean hasBeenViewed(){
		return this.viewed;
	}
	
	public long getTimeInMillis(){
		return this.timeInMillis;
	}
	
	public TaskElement getTaskElement(){
		return this.task;
	}
	
	private void setMessage(){
		switch (this.type){
		case FulfillmentReport :
			// TODO: add date
			this.message = String.format("\"%s\" was fulfilled by %s on <Date>", this.task.getName(), this.sender);
			break;
		case InformDelete :
			break;
		case InformEdit :
			break;
		case InformMembership :
			break;
		}
	}
}
