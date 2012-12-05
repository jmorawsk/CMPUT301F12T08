package tasktracker.controller;

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

import java.util.ArrayList;
import java.util.List;
import tasktracker.model.DatabaseModel;
import tasktracker.model.elements.Task;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
 * @author Kurtis Morin: kmorin1
 * @author Jeanine Bonot<br>
 * <br>
 * 
 */
// October 2012 - J Bonot - Modified Database Adapter to suit TaskTracker
// project
public class DatabaseAdapter {

	public DatabaseModel databaseModel = new DatabaseModel();
	public static final String ID = "_id";
	public static final String DATE = "date";
	public static final String TASK = "task";
	public static final String TASK_ID = "task_id";
	public static final String PHOTO = "photo";
	public static final String USER = "user";
	public static final String USER_ID = "user_id";
	public static final String TEXT = "text";
	public static final String REQS_PHOTO = "requiresPhoto";
	public static final String REQS_TEXT = "requiresText";
	public static final String MEMBERS = "members";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String PRIVATE = "private";
	public static final String COUNT = "count";
	public static final String DOWNLOADED = "downloaded";

	private SQLiteDatabase mDb;

	private static final String TABLE_PHOTOS = "photos";
	private static final String TABLE_TASKS = "tasks";
	private static final String TABLE_MEMBERS = "members";
	private static final String TABLE_USERS = "users";
	private static final String TABLE_FULFILLMENTS = "fulfillments";
	private static final String TABLE_NOTIFICATIONS = "notifications";
	private static final String TABLE_VOTES = "votes";

	private static final String[] cuteWords = new String[] { "kitty", "kitten",
			"cat", "baby", "puppy", "teddy", "cuddly", "aww" };

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created and gives it to the model
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public DatabaseAdapter(Context ctx) {
		Log.d("DEBUG", "DatabaseAdapter(Context)");
		databaseModel.setMCtx(ctx);
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
	 *            the date (in yyyy-MM-dd | hh:mm format)
	 * @param folder
	 *            the folder the photo is in
	 * @param tag
	 *            the tag the photo is under
	 * @param photo
	 *            the photo in byte array format
	 * @return rowId or -1 if failed
	 */
	public long createPhoto(String date, String taskID, byte[] photo) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(DATE, date);
		initialValues.put(PHOTO, photo);
		initialValues.put(TASK_ID, taskID);

		return mDb.insert(TABLE_PHOTOS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createTask(Task task) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ID, task.getID());
		initialValues.put(TASK, task.getName());
		initialValues.put(DATE, task.getDateCreated());
		initialValues.put(USER, task.getCreator());
		initialValues.put(TEXT, task.getDescription());
		initialValues.put(REQS_PHOTO, task.requiresPhoto() ? 1 : 0);
		initialValues.put(REQS_TEXT, task.requiresText() ? 1 : 0);
		initialValues.put(PRIVATE, task.isPrivate() ? 1 : 0);
		initialValues.put(DOWNLOADED, task.getDownloaded());

		for (int n = 0; n < task.getLikes(); n++)
			createVote(task.getID(), "" + n);

		return mDb.insert(TABLE_TASKS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createNotification(String taskID, String recipient,
			String message, String date) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(ID, date);
		initialValues.put(TASK_ID, taskID);
		initialValues.put(USER, recipient);
		initialValues.put(TEXT, message);

		return mDb.insert(TABLE_NOTIFICATIONS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createUser(String user, String email, String password, String id) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(ID, id);
		initialValues.put(USER, user);
		initialValues.put(EMAIL, email);
		initialValues.put(PASSWORD, password);

		return mDb.insert(TABLE_USERS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createUser(String userID, String username) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(ID, userID);
		initialValues.put(USER, username);
		return mDb.insert(TABLE_USERS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createUser(String user, String email, String password) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(USER, user);
		initialValues.put(EMAIL, email);
		initialValues.put(PASSWORD, password);

		return mDb.insert(TABLE_USERS, null, initialValues);
	}

	public long updateUser(String user, String username, String email,
			String password) {
		ContentValues newValues = new ContentValues();

		newValues.put(USER, user);
		newValues.put(EMAIL, email);
		newValues.put(PASSWORD, password);

		return mDb.update(TABLE_USERS, newValues, USER + "='" + user + "'",
				null);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createFulfillment(String taskID, String date, String fulfiller,
			String text) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(TASK_ID, taskID);
		initialValues.put(TEXT, text);
		initialValues.put(USER, fulfiller);
		initialValues.put(DATE, date);

		return mDb.insert(TABLE_FULFILLMENTS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createMember(String taskID, String user) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(TASK_ID, taskID);
		initialValues.put(USER, user);

		return mDb.insert(TABLE_MEMBERS, null, initialValues);
	}

	/**
	 * Create a new entry using the information provided. If the entry is
	 * successfully created return the new rowId for that entry, otherwise
	 * return a -1 to indicate failure.
	 * 
	 * @param folder
	 *            the folder
	 * @return rowId or -1 if failed
	 */
	public long createVote(String taskID, String user) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(TASK_ID, taskID);
		initialValues.put(USER, user);

		return mDb.insert(TABLE_VOTES, null, initialValues);
	}

	public boolean deleteVote(String taskID, String user) {
		return mDb.delete(TABLE_VOTES, TASK_ID + "= ? AND " + USER + "= ?",
				new String[] { taskID, user }) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePhoto(long rowId) {
		return mDb.delete(TABLE_PHOTOS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete the entry with the given rowId
	 * 
	 * @param rowId
	 *            id of entry to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteTask(long rowId) {
		return mDb.delete(TABLE_TASKS, ID + "=" + rowId, null) > 0;
	}

	/**
	 * Delete all of the photos in the given folder Id of folder in folder table
	 * is given to streamline deletion process.
	 * 
	 * @param task
	 *            name of folder to delete photos from
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePhotosInTask(String taskID) {
		return mDb.delete(TABLE_PHOTOS, TASK_ID + "=" + taskID, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all tasks in the table
	 * 
	 * @return Cursor over all folders
	 */
	public Cursor fetchAllTasks() {
		return mDb.query(TABLE_TASKS, new String[] { ID, TASK, USER, DATE },
				null, null, null, null, null);
	}

	/**
	 * Return a Cursor over the list of all tasks in the table that are either
	 * public or created by the user. Tasks will be filtered based on the list
	 * of filtered words provided by the user on an AND basis. If the filter
	 * words contains a key category word (e.g., "cute"), the additional words
	 * will be added to the filter on an OR basis.
	 * 
	 * @return Cursor over all folders
	 */
	public Cursor fetchTasksAvailableToUser(String user, String[] filterWords) {
		String[] selectionArgs;
		String keywordFilter = "";

		if (filterWords.length > 0) {

			Log.d("DatabaseAdapter", "here1");
			List<String[]> wordGroups = new ArrayList<String[]>();
			int groupWordsCount = getSimilarWords(filterWords, wordGroups);

			// args has two iterations of keyword (like comparison) and the user
			// info
			selectionArgs = new String[(filterWords.length + groupWordsCount) * 2 + 1];
			keywordFilter = produceConditions(filterWords, wordGroups,
					selectionArgs, user);

		} else {
			selectionArgs = new String[] { user };
		}

		for (String word : filterWords) {
			Log.d("DatabaseAdapter", "word: " + word);
		}

		String availableToUser = "(SELECT t._id, t.task, t.user, t.date, t.downloaded, t.text"
				+ " FROM tasks as t"
				+ " WHERE t.private = 0 OR (t.private = 1 AND t.user = ?)"
				+ ") as available";

		String taskVoteCount = "(SELECT v.task_id as task_id, COUNT(v.user) as count"
				+ " FROM tasks as t, votes as v"
				+ " WHERE t._id = v.task_id GROUP BY v.task_id) as votecount";

		return mDb
				.rawQuery(
						"SELECT DISTINCT _id, task, user, date, downloaded, text, CASE WHEN count IS NULL THEN 0 ELSE count END as count"
								+ " FROM "
								+ availableToUser
								+ " LEFT JOIN "
								+ taskVoteCount
								+ " ON available._id = votecount.task_id "
								+ keywordFilter + " ORDER BY date DESC",
						selectionArgs);
	}

	/**
	 * Return a Cursor over the list of all notifications belonging to the user.
	 * 
	 * @return Cursor over all folders
	 */
	public Cursor fetchUserNotifications(String recipient) {
		return mDb.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE "
				+ USER + " = ? ORDER BY " + ID + " DESC",
				new String[] { recipient });
	}

	/**
	 * Get the list of text fulfillments of a task.
	 * 
	 * @param taskID
	 *            the hexadecimal string ID of a task.
	 * @return a cursor over the list of text fulfillments for a task
	 */
	public Cursor fetchFulfillment(String taskID) {

		return mDb.query(TABLE_FULFILLMENTS, new String[] { ID, TASK_ID, DATE,
				USER, TEXT }, TASK_ID + "='" + taskID + "'", null, null, null,
				null, null);
	}

	/**
	 * Get the cursor for the task with the provided task ID
	 * 
	 * @param taskID
	 *            the hexadecimal string ID of the task in the table
	 * @return a cursor over a list of tasks with the provided task ID
	 */
	public Cursor fetchTask(String taskID) {
		return mDb.query(TABLE_TASKS, new String[] { ID, TASK, DATE, USER,
				TEXT, REQS_PHOTO, REQS_TEXT, PRIVATE }, ID + "='" + taskID
				+ "'", null, null, null, null, null);
	}

	/**
	 * Get the cursor for the user with the provided row ID
	 * 
	 * @param rowId
	 *            the row ID of the user in the table
	 * @return a cursor over a list of users with the given rowId
	 */
	public Cursor fetchUserViaID(String rowId) {
		return mDb.query(TABLE_USERS, new String[] { ID, USER, EMAIL }, ID
				+ "='" + rowId + "'", null, null, null, null, null);
	}

	/**
	 * Return a cursor over the list of users matching the username and
	 * password. Should contain at most one entry.
	 * 
	 * @param user
	 *            the login username
	 * @param password
	 *            the login password
	 * @return a cursor over the list of user matching the provided username and
	 *         password
	 */
	public Cursor fetchUser(String user, String password) {
		return mDb.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + USER
				+ " = ? AND " + PASSWORD + " = ?", new String[] { user,
				password });
	}

	/**
	 * To check if a username is available
	 * 
	 * @param user
	 * @return
	 */
	public Cursor fetchUserViaName(String user) {
		return mDb.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + USER
				+ " = ?", new String[] { user });
	}

	/**
	 * Returns a cursor that points to data with the requested tag
	 * 
	 * @param task
	 *            retrieve photos with given task
	 * @return Cursor that traverses photos with given task
	 */
	public Cursor fetchPhotosUnderTask(String taskID) {
		Cursor mCursor = mDb.query(true, TABLE_PHOTOS, new String[] { ID, DATE,
				TASK_ID, PHOTO }, TASK_ID + "=" + taskID, null, null, null,
				null, null);

		return mCursor;
	}

	/**
	 * Return a cursor over the list of members of a task.
	 * 
	 * @param taskID
	 *            the hexadecimal string ID of a task
	 * @return a cursor over the list of members of a task
	 */
	public Cursor fetchTaskMembers(String taskID) {
		return mDb.rawQuery("SELECT * FROM " + TABLE_MEMBERS + " WHERE "
				+ TASK_ID + " = ?", new String[] { taskID });
	}

	/**
	 * Return a Cursor over the the list of votes that a user has made for a
	 * task (should contain at most one entry).
	 * 
	 * @return Cursor over the the list of votes that a user has made for a task
	 *         (should contain at most one entry).
	 */
	public Cursor fetchVote(String taskID, String user) {
		return mDb.rawQuery("SELECT * FROM " + TABLE_VOTES + " WHERE "
				+ TASK_ID + " = ?" + " AND " + USER + " = ?", new String[] {
				taskID, user });
	}

	/**
	 * Return a cursor over the count of votes for a task.
	 * 
	 * @param taskID
	 *            the task ID has a hexadecimal string
	 * @return a cursor over the count of votes for a task.
	 */
	public Cursor countAllVotes(String taskID) {
		return mDb.rawQuery("SELECT COUNT(*) FROM " + TABLE_VOTES + " WHERE "
				+ TASK_ID + " = ?", new String[] { taskID });
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

	/**
	 * Clear all the tables and restart them.
	 */
	public void resetDatabase() {

		for (String table : DatabaseModel.TABLE_NAMES) {
			mDb.execSQL("DROP TABLE IF EXISTS " + table);
			Log.d("RESET", table);
		}

		for (String createTable : DatabaseModel.CREATE_TABLES) {
			mDb.execSQL(createTable);
			Log.d("CREATE", createTable);
		}
	}

	/**
	 * 
	 * @param keywordFilter the condition that will be added to the SQL query
	 * @param filterWords the list of words/terms the user has supplied for filtering
	 * @param wordGroups the groups of words that are relevane to the user's search
	 * @param selectionArgs the list of selection arguments that will be used for binding
	 * @param user the current app user
	 */
	private String produceConditions( String[] filterWords,
			List<String[]> wordGroups, String[] selectionArgs, String user) {

		String likeComparison = "(" + TASK + " LIKE '%'|| ? || '%' OR " + TEXT
				+ " LIKE '%'|| ? || '%'" + ")";

		String keywordFilter = "WHERE " + likeComparison;

		int argsIndex = 0;
		selectionArgs[argsIndex++] = user;
		selectionArgs[argsIndex++] = filterWords[0];
		selectionArgs[argsIndex++] = filterWords[0];

		for (int i = 1; i < filterWords.length; i++) {
			keywordFilter += " AND " + likeComparison;
			selectionArgs[argsIndex++] = filterWords[i];
			selectionArgs[argsIndex++] = filterWords[i];
		}

		for (String[] group : wordGroups) {
			for (String word : group) {
				keywordFilter += " OR " + likeComparison;
				selectionArgs[argsIndex++] = word;
				selectionArgs[argsIndex++] = word;
			}
		}
		
		return keywordFilter;
	}

	/**
	 * Parses through each comma-delimited string from the user and checks if
	 * any match a category word. If a match is found, then the words in the
	 * matching category are added to the list of wordGroups. This method can be
	 * expanded to other word categories (e.g., science, music).
	 * 
	 * @param keywords
	 *            the comma-delimited words input from the user
	 * @param wordGroups
	 *            the list of word groups that will be added to the keyword
	 *            filter
	 * @return the total number of words that have been added to the list
	 */
	private int getSimilarWords(String[] keywords, List<String[]> wordGroups) {
		int count = 0;

		for (int i = 0; i < keywords.length; i++) {
			if (keywords[i].equalsIgnoreCase("cute")) {
				wordGroups.add(cuteWords);
				count += cuteWords.length;
			}
		}

		return count;

	}

}
