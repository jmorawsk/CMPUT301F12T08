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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.ContentValues;

/**
 * A class that represents a task. Every task has a creator, and is to be
 * fulfilled by a task member.
 * 
 * @author Katherine Jasniewski
 * @author Jeanine Bonot
 * 
 */
public class Task implements Serializable{

	public static final String DATABASE_TABLE = "tasks";  // For SQL table
	private static final long serialVersionUID = 1L;

	public static final String ID = "_id";
	public static final String TASK = "task";
	public static final String DATE = "date";
	public static final String CREATOR = "creator";
	public static final String TEXT = "text";
	public static final String REQUIRESTEXT = "requiresText";
	public static final String REQUIRESPHOTO = "requiresPhoto";
	public static final String OTHERMEMBERS = "otherMembers";
	public static final String PHOTO = "photo";
	public static final String STATUS = "status";

	private final String _creationDate;
	private final User _creator;

	// Properties that may change
	private String _id; // Instantiated by database.
	private String _name;
	private String _description;
	private boolean _requiresText;
	private boolean _requiresPhoto;
	private List<User> _otherMembers;
	private boolean _idSet;

	/**
	 * Creates a new instance of the TaskElement class.
	 * 
	 * @param creator
	 *            The task creator.
	 */
	public Task(User creator) {
		this(creator, "Untitled", Calendar.getInstance().getTime(), "", true,
				false, "");
	}

	/**
	 * Creates a new instance of the TaskElement class.
	 * 
	 * @param creator
	 *            The task creator.
	 * @param name
	 *            The task name.
	 * @param date
	 *            The task creation date.
	 * @param description
	 *            A description of the task.
	 * @param requirements
	 *            The list of task requirements.
	 */
	public Task(User creator, String name, Date date, String description,
			boolean requiresText, boolean requiresPhoto, String otherMembers) {

		// Required Elements
		_creator = creator;
		_creationDate = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());

		// id should be null until set by db
		_idSet = false;

		// Changeable elements
		_name = name;
		_description = description;
		_requiresText = requiresText;
		_requiresPhoto = requiresPhoto;
		_otherMembers = null;

	}

	/** Gets the task name */
	public String getName() {
		return _name;
	}

	/** Sets the task name */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * Sets the task ID if it has not yet been done so already.
	 * 
	 * @return true if the ID was set, false if the ID has already been set
	 */
	public boolean setID(String newID) {
		if (!_idSet) {
			_id = newID;
			return true;
		}
		return false;
	}

	/** Gets the task ID */
	public String getID() {
		return _id;
	}

	/** Gets the date in the format of a String */
	public String stringDate() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		return dateformat.format(_creationDate).toString();
	}

	/** Gets the text description of the task */
	public String getDescription() {
		return _description;
	}

	/** Sets the text description of the task */
	public void setDescription(String description) {
		_description = description;
	}

	/** Gets the list of task requirements */
	/** Gets the members of the task, including the task creator */
	public List<User> getMembers() {
		return _otherMembers;
	}

	/** Sets the members of the task, including the task creator */
	public void setMembers(List<User> otherMembers) {
		_otherMembers = otherMembers;
		_otherMembers.add(_creator);
	}

	/**
	 * Fulfill a task by completing the task's requirements.
	 * 
	 * @return True if the task was successfully fulfilled, false otherwise.
	 */
	public boolean fulfill() {
//		for (Requirement req : _requirements) {
//			if (!req.fulfill())
//				return false;
//		}

		return true;
	}

	/**
	 * Checks if objects (Tasks) are equal based on their ID number.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() == obj.getClass())
			return false;

		Task other = (Task) obj;
		if (_id != other._id)
			return false;
		return true;
	}

	public String toString() {
		String string = "";
		string.concat("\"" + this.getName() + "\" ");
		string.concat("by " + _creator + "\n");
		string.concat("Deadline: " + this.stringDate());
		return string;
	}

	public ContentValues getContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TASK, _name);
		initialValues.put(DATE, _creationDate);
		initialValues.put(CREATOR, _creator.getName());
		initialValues.put(TEXT, _description);
		initialValues.put(REQUIRESPHOTO, _requiresPhoto);
		initialValues.put(REQUIRESTEXT, _requiresText);
		return initialValues;
	}

}