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

import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;

/**
 * A class that represents a task. Every task has a creator, and is to be
 * fulfilled by a task member.
 * 
 * @author Katherine Jasniewski
 * @author Jeanine Bonot
 * 
 */
public class Task implements Serializable {

	public static final String DATABASE_TABLE = "tasks"; // For SQL table
	private static final long serialVersionUID = 1L;

	private final String _creator;

	// Properties that may change
	private String _id; // Instantiated by database.
	private String _name;
	private String _creationDate;
	private String _description;
	private boolean _requiresText;
	private boolean _requiresPhoto;
	private List<PhotoRequirement> _photos;
	private TextRequirement _text;

	public Task() {
		_creator = null;
		// Do nothing.
	}

	/**
	 * Creates a new instance of the TaskElement class.
	 * 
	 * @param creator
	 *            The task creator.
	 */
	public Task(String creator) {
		this(creator, "Untitled", "", true, false);
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
	public Task(String creator, String name, String description,
			boolean requiresText, boolean requiresPhoto) {

		Date date = Calendar.getInstance().getTime();
		_creationDate = new SimpleDateFormat("yyyy-MM-dd").format(date
				.getTime());

		_creator = creator;
		_name = name;

	}

	/**
	 * Sets the task ID if it has not yet been done so already.
	 * 
	 * @return true if the ID was set, false if the ID has already been set
	 */
	public void setID(String value) {
		_id = value;

	}

	public String getCreator() {
		return _creator;
	}

	/** Gets the task ID */
	public String getID() {
		return _id;
	}

	public void setName(String value) {
		_name = value;
	}

	public String getName() {
		return _name;
	}

	public String getDate() {
		return _creationDate;
	}

	public void setDescription(String value) {
		_description = value;
	}

	public String getDescription() {
		return _description;
	}

	public void setTextRequirement(boolean value) {
		_requiresText = value;
	}

	public boolean requiresText() {
		return _requiresText;
	}

	public void setPhotoRequirement(boolean value) {
		_requiresPhoto = value;
	}

	public boolean requiresPhoto() {
		return _requiresPhoto;
	}

	public void addPhotos(PhotoRequirement value) {
		_photos.add(value);
	}

	public List<PhotoRequirement> getPhotos() {
		return _photos;
	}

	public static String[] getMemberList(String string) {
		String[] members = string.split("(\\s+)?,(\\s+)?");
		return members;
	}

}