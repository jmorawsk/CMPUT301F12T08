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

import android.util.Log;

/**
 * A class that represents a task. Every task has a creator, and is to be
 * fulfilled by a task member.
 * 
 * @author Katherine Jasniewski
 * @author Jeanine Bonot
 * 
 */
public class Task implements Serializable {

	private TaskMembers taskMembers = new TaskMembers();
    public static final String DATABASE_TABLE = "tasks"; // For SQL table
	private static final long serialVersionUID = 1L;

	private final String _creator;

	// Properties that may change
	private String _id;
	private String _name;
	private String _creationDate;
	private String _description;
	private boolean _requiresText;
	private boolean _requiresPhoto;
	private boolean _private;
	private String _isDownloaded; // added nov29 by Mike
	private int _likes;
	private ArrayList<byte[]> _photos; 
	private ArrayList<String[]> _texts;
	// SQL ids
	private String _creatorID;

	/**
	 * Creates a new instance of the TaskElement class.
	 * 
	 * @param creator
	 *            The task creator.
	 */
	public Task(String creator) {
		this(creator, "Untitled", "", true, false);
	}

	public Task(String creator, String name, String description) {
		this(creator, name, description, true, false);
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

		_creationDate = new SimpleDateFormat("MMM dd, yyyy | HH:mm")
				.format(Calendar.getInstance().getTime());

		_creator = creator;
		_name = name;
		_requiresText = requiresText;
		_requiresPhoto = requiresPhoto;
		taskMembers.set_otherMembersList(new ArrayList<String>());
		_private = false;
		_likes = 0;
		_photos = new ArrayList<byte[]>();
		_texts = new ArrayList<String[]>();
		_description = description;
	}

	// Setters
	/**
	 * Sets the task ID if it has not yet been done so already.
	 * 
	 * @return true if the ID was set, false if the ID has already been set
	 */
	public void setID(String value) {
		_id = value;
	}

	public void setName(String value) {
		_name = value;
	}
	
	public void setCreatorID(String value){
		_creatorID = value;
	}
	
	public void setDate(String value){
		_creationDate = value;
	}

	public void setDescription(String value) {
		_description = value;
	}

	public void setTextRequirement(boolean value) {
		_requiresText = value;
	}

	public void setPhotoRequirement(boolean value) {
		_requiresPhoto = value;
	}

	public void setIsPrivate(boolean value) {
		_private = value;
	}

	public void setIsDownloaded(String value) {
		_isDownloaded = value;
	}

	public void setLikes(int value) {
		_likes = value;
	}
	
	public void setPhotos(ArrayList<byte[]> photos){
		_photos = new ArrayList<byte[]>();
		_photos.addAll(photos);
	}
	public void setTextReqs(ArrayList<String[]> texts){
            _texts = new ArrayList<String[]>();
            _texts.addAll(texts);
        }

	// Getters
	public String getCreator() {
		return _creator;
	}

	public String getCreatorID() {
		return _creatorID;
	}

	/** Gets the task ID */
	public String getID() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public String getDateCreated() {
		return _creationDate;
	}

	public String getDescription() {
		return _description;
	}

	public String getDownloaded() {
		return _isDownloaded;
	}

	public boolean requiresText() {
		return _requiresText;
	}

	public boolean requiresPhoto() {
		return _requiresPhoto;
	}

	public boolean isPrivate() {
		return _private;
	}

	public int getLikes() {
		return _likes;
	}
	
	public ArrayList<byte[]> getPhotos(){
		return _photos;
	}
	public ArrayList<String[]> getTextReqs(){
            return _texts;
        }

	/** Gets the string summary for CrowdSourcer */
	public String getSummary() {
		return "<Task>" + _name + "<Creator>" + _creator + "<Date>"
				+ _creationDate + "<Likes>" + _likes + "<Description>"
				+ _description + "<RequiresPhoto>" + _requiresPhoto
				+ "<RequiresText>" + _requiresText;

	}

	/**
	 * Gets the string of members, separates them with comma delimiters, then
	 * adds each member to the members list.
	 * 
	 * @param value
	 *            The string of members.
	 */
	public void setOtherMembers(String value) {

		taskMembers.setOtherMembers(value);
	}

	public List<String> getOtherMembers() {
		return taskMembers.get_otherMembersList();
	}

}