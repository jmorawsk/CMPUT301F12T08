package tasktracker.model.elements;

import java.util.*;
import tasktracker.enums.*;
import tasktracker.model.enums.NotificationType;

/**
 * A notification
 * @author jbonot
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
