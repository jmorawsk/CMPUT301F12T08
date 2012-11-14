package tasktracker.controller;

/**
 * Copyright (C) 2012 Andrea Budac, Kurtis Morin, Christian Jukna
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import tasktracker.model.DatabaseModel;
import tasktracker.model.elements.Task;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * DatabaseAdapter<br>
 * A simple SQLite database helper class. Gives the abilities needed by the main
 * application to access photos and available folders.
 * 
 * 
 * Much of the code of this class is from the Notepad Tutorial on the Android
 * Developer website. Found at:
 * http://developer.android.com/resources/tutorials/notepad/index.html
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1 <br>
 * <br>
 * @author Jeanine Bonot
 */
// April 06 - Created
// November 2012 - Jeanine Bonot - Modified to generate tables for TaskTracker.
public class DatabaseAdapter {
	public DatabaseModel databaseModel = new DatabaseModel();
	public static final String ID = "_id";
	public static final String TASK = "task";
	public static final String DATE = "date";
	public static final String USER = "user";
	public static final String TEXT = "text";
	public static final String REQUIRESTEXT = "requiresText";
	public static final String REQUIRESPHOTO = "requiresPhoto";
	public static final String OTHERMEMBERS = "otherMembers";
	public static final String PHOTO = "photo";
	public static final String VIEWED = "viewed";

	private SQLiteDatabase db;

	private static final String DATABASE_TABLE_PHOTOS = "photos";
	private static final String DATABASE_TABLE_TASKS = "tasks";
	private static final String DATABASE_TABLE_FULFILLMENTS = "fulfillments";
	private static final String DATABASE_TABLE_MEMBERS = "members";
	private static final String DATABASE_TABLE_NOTIFICATIONS = "notifications";

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created and gives it to the model
	 * 
	 * @param context
	 *            the Context within which to work
	 */
	public DatabaseAdapter(Context context) {
		databaseModel.setMContext(context);
	}

	/**
	 * Open the entries database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public DatabaseAdapter open() throws SQLException {
		return databaseModel.open(this);
	}

	/**
	 * Closes the database
	 */
	public void close() {
		databaseModel.close();
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param date
	 *            the date (in yyyy-mm-dd format)
	 * @param task
	 *            the name of the task
	 * @param photo
	 *            the photo in byte array format
	 * @return rowId or -1 if failed
	 */
	public long insertPhoto(String date, String task, byte[] photo) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(TASK, task);
		initialValues.put(DATE, date);
		initialValues.put(PHOTO, photo);

		return db.insert(DATABASE_TABLE_PHOTOS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param task
	 *            the name of the task
	 * @return rowId or -1 if failed
	 */
	public long insertTask(String task, String date, String creator,
			String text, int reqsPhoto, int reqsText) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(TASK, task);
		initialValues.put(DATE, date);
		initialValues.put(USER, creator);
		initialValues.put(TEXT, text);
		initialValues.put(REQUIRESPHOTO, reqsPhoto);
		initialValues.put(REQUIRESTEXT, reqsText);

		return db.insert(DATABASE_TABLE_TASKS, null, initialValues);
	}
	
	public long insertTask(Task task){
		ContentValues initialValues = task.getContentValues();
		return db.insert(DATABASE_TABLE_TASKS, null, initialValues);
	}
	

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param task
	 * @param fulfiller
	 * @param date
	 * @param text
	 * @return rowId of -1 if failed
	 */
	public long insertFulfillment(String task, String fulfiller, String date,
			String text) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TASK, task);
		initialValues.put(USER, fulfiller);
		initialValues.put(DATE, date);
		initialValues.put(TEXT, text);

		return db.insert(DATABASE_TABLE_FULFILLMENTS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param task
	 * @param fulfiller
	 * @param date
	 * @param text
	 * @return rowId of -1 if failed
	 */
	public long insertNotification(String task, String date, String sender,
			String message) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DatabaseAdapter.TASK, task);
		initialValues.put(DatabaseAdapter.DATE, date);
		initialValues.put(DatabaseAdapter.USER, sender);
		initialValues.put(DatabaseAdapter.TEXT, message);
		initialValues.put(DatabaseAdapter.VIEWED, false);

		return db.insert(DATABASE_TABLE_NOTIFICATIONS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param task
	 * @param fulfiller
	 * @param date
	 * @param text
	 * @return rowId of -1 if failed
	 */
	public long insertMember(String task, String member) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TASK, task);
		initialValues.put(USER, member);

		return db.insert(DATABASE_TABLE_MEMBERS, null, initialValues);
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePhoto(long rowId) {
		return db.delete(DATABASE_TABLE_PHOTOS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteTask(long rowId) {
		return db.delete(DATABASE_TABLE_TASKS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteFulfillment(long rowId) {
		return db.delete(DATABASE_TABLE_FULFILLMENTS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteMember(long rowId) {
		return db.delete(DATABASE_TABLE_MEMBERS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNotification(long rowId) {
		return db.delete(DATABASE_TABLE_NOTIFICATIONS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete all of the photos in the given folder Id of folder in folder table
	 * is given to streamline deletion process.
	 * 
	 * @param task
	 *            name of folder to delete photos from
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePhotosInFolder(String task) {
		return db.delete(DATABASE_TABLE_PHOTOS, TASK + "='" + task + "'", null) > 0;
	}

	/**
	 * Return a Cursor over the list of all of the tasks of which the user is a member.
	 * 
	 * @return Cursor over all of the user's tasks.
	 */
	public Cursor fetchAllTasks() {
		return db.query(DATABASE_TABLE_TASKS, new String[] { ID, TASK }, null,
				null, null, null, null);
	}

	/**
	 * Return a Cursor over the list of all folders in the table
	 * 
	 * @return Cursor over all folders
	 */
	public Cursor fetchUserNotifications(String receiver) {
		String condition = USER + "='" + receiver + "'";
		return db.query(DATABASE_TABLE_NOTIFICATIONS, new String[] { ID, TASK,
				USER, TEXT, VIEWED }, condition, null, null, null,
				null);
	}

	/**
	 * Return a Cursor positioned at the entry that matches the given rowId
	 * 
	 * @param rowId
	 *            id of entry to retrieve
	 * @return Cursor positioned to matching entry, if found
	 * @throws SQLException
	 *             if entry could not be found/retrieved
	 */
	public Cursor fetchPhoto(long rowId) throws SQLException {
		// TODO: Modify to match TaskTracker
		Cursor mCursor = null;
		// Cursor mCursor = mDb.query(true, DATABASE_TABLE_PHOTOS, new String[]
		// {
		// ID, DATE, FOLDER, TAG, ANNOTATE, PHOTO }, ID + "=" + rowId,
		// null, null, null, null, null);
		// if (mCursor != null) {
		// mCursor.moveToFirst();
		// }
		return mCursor;
	}

	/**
	 * Sets the database
	 * 
	 * @param mDb
	 *            Takes in the SQL database
	 */
	public void setMDb(SQLiteDatabase mDb) {
		this.db = mDb;
	}

}