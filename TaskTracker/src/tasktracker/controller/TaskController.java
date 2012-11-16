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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

import tasktracker.model.elements.Task;

import android.os.Environment;
import android.util.Log;

/**
 * Manages the passing of information between the application and the database.
 * 
 * @author Jeanine Bonot
 * 
 */
public class TaskController {

	// TODO: Need to set up so that tests are saved differently from final
	// product.
	private static final String FILENAME = "file.sav";
	private static final String DIRECTORIES = "/TaskTracker/Tasks";

	/**
	 * Delete the TaskTracker's file, if it exists.
	 * 
	 * @return Returns true if the file was successfully deleted, false
	 *         otherwise.
	 */
	public static boolean deleteFile() {
		File sdDir = Environment.getExternalStorageDirectory();
		File file = new File(sdDir.getAbsolutePath() + DIRECTORIES, FILENAME);

		boolean result = file.delete();
		Log.d("DeleteFile", String.valueOf(result));

		return result;
	}

	/**
	 * Read the log entries from the file.
	 * 
	 * @return A list of TaskElement items from the file.
	 */
	public static List<Task> readFile() {
		ArrayList<Task> entries = new ArrayList<Task>();

		try {
			FileInputStream fis = new FileInputStream(TaskController.getFile());
			ObjectInputStream ois = new ObjectInputStream(fis);

			Task entry;
			while ((entry = (Task) ois.readObject()) != null)
				entries.add(entry);

			ois.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return entries;
	}

	/**
	 * Serialize the task element into the file system.
	 * 
	 * @param task
	 *            The task to be saved.
	 * @return True if the write was successful, false otherwise.
	 */
	public static boolean writeFile(Task element) {

		try {
			ObjectOutputStream oos = getOOS(TaskController.getFile());
			oos.writeObject(element);
			oos.close();
			return true;
		} catch (IOException e) {
			Log.e("TaskController - WriteFile", e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Gets the file for reading/writing.
	 * 
	 * @return The file for reading/writing.
	 */
	private static File getFile() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			Log.d("Test", "sdcard mounted and writable");
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Log.d("Test", "sdcard mounted readonly");
		} else {
			Log.d("Test", "sdcard state: " + state);
		}

		File sdDir = Environment.getExternalStorageDirectory();
		File dir = new File(sdDir.getAbsolutePath() + DIRECTORIES);
		dir.mkdirs();
		File file = new File(dir, FILENAME);
		state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			Log.d("Test", "sdcard mounted and writable");
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Log.d("Test", "sdcard mounted readonly");
		} else {
			Log.d("Test", "sdcard state: " + state);
		}
		return file;
	}

	/**
	 * Get the proper ObjectOutputStream for writing.
	 * 
	 * @param storageFile
	 *            The file that will be used for serialization.
	 * @return The proper ObjectOutputStream depending on whether a file is
	 *         being appended to an existing file.
	 * @throws IOException
	 */
	private static ObjectOutputStream getOOS(File storageFile)
			throws IOException {

		if (storageFile.exists())
			return new AppendableObjectOutputStream(new FileOutputStream(
					storageFile, true));
		else
			return new ObjectOutputStream(new FileOutputStream(storageFile));
	}

	/**
	 * An ObjectOutputStream class that does not create a new header. Used to
	 * append objects to an existing file. Source: StackOverflow on
	 * StreamCorruptedException and appending to files.
	 */
	private static class AppendableObjectOutputStream extends
			ObjectOutputStream {

		public AppendableObjectOutputStream(OutputStream out)
				throws IOException {
			super(out);
		}

		@Override
		protected void writeStreamHeader() throws IOException {
			// do not write a header
			reset();
		}
	}
}
