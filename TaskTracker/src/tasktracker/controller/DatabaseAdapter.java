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
	public static final String CREATOR = "creator";
	public static final String FULFILLER = "fulfiller";
	public static final String TEXT = "text";
	public static final String REQUIRESTEXT = "requiresText";
	public static final String REQUIRESPHOTO = "requiresPhoto";
	public static final String OTHERMEMBERS = "otherMembers";
	public static final String PHOTO = "photo";
	public static final String STATUS = "status";

	private SQLiteDatabase mDb;

	private static final String DATABASE_TABLE_PHOTOS = "photos";
	private static final String DATABASE_TABLE_TASKS = "tasks";
	private static final String DATABASE_TABLE_TASKFULFILLMENTS = "taskFulfillments";

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
	public long createPhotoEntry(String date, String task, byte[] photo) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(DATE, date);
		initialValues.put(TASK, task);
		initialValues.put(PHOTO, photo);

		return mDb.insert(DATABASE_TABLE_PHOTOS, null, initialValues);
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
	public long createTask(String task, String date, String creator,
			String text, int requiresPhoto, int requiresText, int status,
			String otherMembers) {

		// TODO: Too many parameters --> Send an object instead?
		ContentValues initialValues = new ContentValues();
		initialValues.put(TASK, task);
		initialValues.put(DATE, date);
		initialValues.put(CREATOR, creator);
		initialValues.put(TEXT, text);
		initialValues.put(REQUIRESPHOTO, requiresPhoto);
		initialValues.put(REQUIRESTEXT, requiresText);
		initialValues.put(STATUS, status);
		initialValues.put(OTHERMEMBERS, otherMembers);

		return mDb.insert(DATABASE_TABLE_TASKS, null, initialValues);
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
	public long createTaskFulfillment(String task, String fulfiller,
			String date, String text) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TASK, task);
		initialValues.put(FULFILLER, fulfiller);
		initialValues.put(DATE, date);
		initialValues.put(TEXT, text);

		return mDb.insert(DATABASE_TABLE_TASKFULFILLMENTS, null, initialValues);
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePhoto(long rowId) {
		return mDb.delete(DATABASE_TABLE_PHOTOS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteTask(long rowId) {
		return mDb.delete(DATABASE_TABLE_TASKS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteTaskFulfillment(long rowId) {
		return mDb.delete(DATABASE_TABLE_TASKFULFILLMENTS, ID + "=" + rowId,
				null) > 0;
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
		return mDb.delete(DATABASE_TABLE_PHOTOS, TASK + "='" + task + "'",
				null) > 0;
	}

	/**
	 * Return a Cursor over the list of all folders in the table
	 * 
	 * @return Cursor over all folders
	 */
	public Cursor fetchAllTasks() {
		return mDb.query(DATABASE_TABLE_TASKS, new String[] { ID, TASK },
				null, null, null, null, null);
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
//		Cursor mCursor = mDb.query(true, DATABASE_TABLE_PHOTOS, new String[] {
//				ID, DATE, FOLDER, TAG, ANNOTATE, PHOTO }, ID + "=" + rowId,
//				null, null, null, null, null);
//		if (mCursor != null) {
//			mCursor.moveToFirst();
//		}
		return mCursor;
	}

	/**
	 * Returns a Cursor that points to data with the requested folder name
	 * 
	 * @param folder
	 *            retrieve photos with given folder name
	 * @return Cursor that traverses photos with given folder name
	 */
	public Cursor fetchPhotosInFolder(String folder) {
		Cursor mCursor = null;
		// TODO: Modify to match TaskTracker
//		mCursor = mDb.query(DATABASE_TABLE_PHOTOS, new String[] { ID,
//				DATE, FOLDER, TAG, ANNOTATE, PHOTO }, FOLDER + "='" + folder
//				+ "'", null, null, null, null, null);

		return mCursor;
	}

	/**
	 * Sets the database
	 * 
	 * @param mDb
	 *            Takes in the SQL database
	 */
	public void setMDb(SQLiteDatabase mDb) {
		this.mDb = mDb;
	}

}