package tasktracker.model;

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

import tasktracker.controller.DatabaseAdapter;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.Context;

/**
 * DatabaseModel<br>
 * Creates the tables in the database and opens and closes it to allow it to be
 * queried.
 * 
 * Much of the code of this class is from the Notepad Tutorial on the Android
 * Developer website.<br>
 * Found at: http://developer.android.com/resources/tutorials/notepad/index.html
 * 
 * @author Andrea Budac: abudac
 * @author Christian Jukna: jukna
 * @author Kurtis Morin: kmorin1<br>
 * <br>
 * @author Jeanine Bonot
 */
// April 06 - Created
// November 2012 - Jeanine Bonot - Modified to generate tables for TaskTracker.
public class DatabaseModel {
	private DatabaseHelper mDbHelper;
	private Context mContext;

	/**
	 * Database creation SQL statement.
	 */

	private static final String DATABASE_CREATE_TASKS = "CREATE TABLE tasks("
			+ "_id integer primary key autoincrement, " + "task TEXT NOT NULL, "
			+ "date TEXT NOT NULL, " + "user TEXT NOT NULL, "
			+ "text TEXT, " + "requiresPhoto INTEGER, "
			+ "requiresText INTEGER, " + "status INTEGER);";

	private static final String DATABASE_CREATE_FULFILLMENTS = "CREATE TABLE fulfillments("
			+ "_id integer primary key autoincrement, "
			+ "task TEXT NOT NULL,"
			+ "user TEXT NOT NULL, "
			+ "date TEXT NOT NULL "
			+ "text TEXT);";

	private static final String DATABASE_CREATE_PHOTOS = "CREATE TABLE photos("
			+ "_id integer primary key autoincrement, "
			+ "photo BLOB NOT NULL, " + "task TEXT NOT NULL "
			+ "date TEXT NOT NULL);";

	private static final String DATABASE_CREATE_MEMBERS = "CREATE TABLE members("
			+ "_id integer primary key autoincrement, "
			+ "task TEXT NOT NULL,"
			+ "user TEXT NOT NULL);";

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 2;

	private static final String NAME = "dbAdapter";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_TASKS);
			db.execSQL(DATABASE_CREATE_MEMBERS);
			db.execSQL(DATABASE_CREATE_FULFILLMENTS);
			db.execSQL(DATABASE_CREATE_PHOTOS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(NAME, "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS tasks");
			db.execSQL("DROP TABLE IF EXISTS members");
			db.execSQL("DROP TABLE IF EXISTS fulfillments");
			db.execSQL("DROP TABLE IF EXISTS photos");
			onCreate(db);
		}
	}

	/**
	 * Gets the database.
	 * 
	 * @return mDbHelper
	 */
	public DatabaseHelper getMDbHelper() {
		return mDbHelper;
	}

	/**
	 * Sets the database
	 * 
	 * @param mDbHelper
	 */
	public void setMDbHelper(DatabaseHelper mDbHelper) {
		this.mDbHelper = mDbHelper;
	}

	/**
	 * Gets the context of the activity
	 * 
	 * @return mCtx the context of the activity
	 */
	public Context getMContext() {
		return mContext;
	}

	/**
	 * Sets the context.
	 * 
	 * @param mContext
	 *            the context of the activity
	 */
	public void setMContext(Context mContext) {
		this.mContext = mContext;
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
	public DatabaseAdapter open(DatabaseAdapter databaseAdapter)
			throws SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		databaseAdapter.setMDb(mDbHelper.getWritableDatabase());
		return databaseAdapter;
	}

	/**
	 * Closes the database
	 */
	public void close() {
		mDbHelper.close();
	}
}