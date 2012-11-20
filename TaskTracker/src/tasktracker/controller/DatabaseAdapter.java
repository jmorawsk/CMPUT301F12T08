package tasktracker.controller;

import tasktracker.model.DatabaseModel;
import tasktracker.model.elements.Task;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAdapter {

	public DatabaseModel databaseModel = new DatabaseModel();
	public static final String ID = "_id";
	public static final String DATE = "date";
	public static final String TASK = "task";
	public static final String PHOTO = "photo";
	public static final String USER = "user";
	public static final String TEXT = "text";
	public static final String REQS_PHOTO = "requiresPhoto";
	public static final String REQS_TEXT = "requiresText";
	public static final String MEMBERS = "members";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";

	private SQLiteDatabase mDb;

	private static final String TABLE_PHOTOS = "photos";
	private static final String TABLE_TASKS = "tasks";
	private static final String TABLE_MEMBERS = "members";
	private static final String TABLE_USERS = "users";
	private static final String TABLE_FULFILLMENTS = "fulfillments";

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
	 *            the date (in yyyy-mm-dd format)
	 * @param folder
	 *            the folder the photo is in
	 * @param tag
	 *            the tag the photo is under
	 * @param photo
	 *            the photo in byte array format
	 * @return rowId or -1 if failed
	 */
	public long createPhotoEntry(String date, String task, byte[] photo) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(DATE, date);
		initialValues.put(PHOTO, photo);
		initialValues.put(TASK, task);

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

		initialValues.put(TASK, task.getName());
		initialValues.put(DATE, task.getDateCreated());
		initialValues.put(USER, task.getCreator());
		initialValues.put(TEXT, task.getDescription());
		initialValues.put(MEMBERS, task.getMembers());
		initialValues.put(REQS_PHOTO, task.requiresPhoto() ? 1 : 0);
		initialValues.put(REQS_TEXT, task.requiresText() ? 1 : 0);

		return mDb.insert(TABLE_TASKS, null, initialValues);
	}

	public long createUser(String user, String email, String password) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(USER, user);
		initialValues.put(EMAIL, email);
		initialValues.put(PASSWORD, password);

		return mDb.insert(TABLE_USERS, null, initialValues);
	}

	public long createFulfillment(String task, String date, String fulfiller, String text){
		ContentValues initialValues = new ContentValues();

		initialValues.put(TASK, task);
		initialValues.put(TEXT, text);
		initialValues.put(USER, fulfiller);
		initialValues.put(DATE, date);

		return mDb.insert(TABLE_FULFILLMENTS, null, initialValues);
	}
	
	public long createMember(String task, String user){
		ContentValues initialValues = new ContentValues();

		initialValues.put(TASK, task);
		initialValues.put(USER, user);

		return mDb.insert(TABLE_MEMBERS, null, initialValues);
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
	public boolean deleteFolder(long rowId) {
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
	public boolean deletePhotosInTask(String task) {
		return mDb.delete(TABLE_PHOTOS, TASK + "='" + task + "'", null) > 0;
	}

	/**
	 * Return a Cursor over the list of all folders in the table
	 * 
	 * @return Cursor over all folders
	 */
	public Cursor fetchAllTasks() {
		return mDb.query(TABLE_TASKS, new String[] { ID, TASK, USER, DATE },
				null, null, null, null, null);
	}
	
	public Cursor fetchAllFulfillments(){
		return mDb.query(TABLE_FULFILLMENTS, new String[] { ID, TASK, USER, DATE },
				null, null, null, null, null);
	}
	
	public Cursor fetchFulfillment(String task){
		return mDb.query(TABLE_FULFILLMENTS, new String[] { ID, TASK, DATE, USER,
				TEXT }, TASK + "='"
				+ task + "'", null, null, null, null, null);
	}

	public Cursor fetchTask(long rowId) {
		return mDb.query(TABLE_TASKS, new String[] { ID, TASK, DATE, USER,
				TEXT, REQS_PHOTO, REQS_TEXT, MEMBERS }, ID + "="
				+ rowId, null, null, null, null, null);
	}

	
	public Cursor fetchUser(long rowId){
		return mDb.query(TABLE_USERS, new String[] { ID, USER, EMAIL }, ID + "="
				+ rowId, null, null, null, null, null);
	}
	
	/**
	 * To validate login
	 * @param user
	 * @param password
	 * @return
	 */
	public Cursor fetchUser(String user, String password) {
		return mDb.query(TABLE_USERS, new String[] { ID, USER, PASSWORD }, USER
				+ "='" + user + "' AND " + PASSWORD + "='" + password + "'",
				null, null, null, null);
	}

	/**
	 * To check if a username is available
	 * @param user
	 * @return
	 */
	public Cursor fetchUser(String user) {
		return mDb.query(TABLE_USERS, new String[] { ID, USER }, USER + "='"
				+ user + "'", null, null, null, null);
	}

	/**
	 * Returns a cursor that points to data with the requested tag
	 * 
	 * @param task
	 *            retrieve photos with given task
	 * @return Cursor that traverses photos with given task
	 */
	public Cursor fetchPhotosUnderTask(String task) {
		Cursor mCursor = mDb.query(true, TABLE_PHOTOS, new String[] { ID, DATE,
				TASK, PHOTO }, TASK + "='" + task + "'", null, null, null,
				null, null);

		return mCursor;
	}
	
	public Cursor fetchTaskMembers(String task){
		return mDb.query(true, TABLE_MEMBERS, new String[] { ID, TASK,
				USER }, TASK + "='" + task + "'", null, null, null,
				null, null);
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
