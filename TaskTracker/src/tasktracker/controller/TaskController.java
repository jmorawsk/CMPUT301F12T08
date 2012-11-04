package tasktracker.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

import tasktracker.model.elements.TaskElement;

import android.os.Environment;

/**
 * Manages the passing of information between the application and the database.
 * @author jbonot
 *
 */
public final class TaskController implements LocalObjectController<TaskElement>{

	// TODO: Need to set up so that tests are saved differently from final product.
	private static final String FILENAME = "file.sav";
	private static final String DIRECTORIES = "/TaskTracker/Tasks";
	
	/**
	 * Delete the TaskTracker's file, if it exists.
	 * 
	 * @return Returns true if the file was successfully deleted, false
	 *         otherwise.
	 */
	public boolean deleteFile() {
		File sdDir = Environment.getExternalStorageDirectory();
		File file = new File(sdDir.getAbsolutePath() + DIRECTORIES, FILENAME);
		return file.delete();
	}
	
	/**
	 * Read the log entries from the file.
	 * @return A list of TaskElement items from the file.
	 */
	public List<TaskElement> readFile() {
		ArrayList<TaskElement> entries = new ArrayList<TaskElement>();

		try {
			FileInputStream fis = new FileInputStream(TaskController.getFile());
			ObjectInputStream ois = new ObjectInputStream(fis);

			TaskElement entry;
			while ((entry = (TaskElement) ois.readObject()) != null)
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
	 * @param task The task to be saved.
	 */
	public void writeFile(TaskElement element) {
		try {
			ObjectOutputStream oos = getOOS(TaskController.getFile());
			oos.writeObject(element);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gets the file for reading/writing.
	 * @return The file for reading/writing.
	 */
	private static File getFile() {
		File sdDir = Environment.getExternalStorageDirectory();
		File dir = new File(sdDir.getAbsolutePath() + DIRECTORIES);
		dir.mkdirs();
		return new File(dir, FILENAME);
	}
	
	/**
	 * Get the proper ObjectOutputStream for writing.
	 * @param storageFile The file that will be used for serialization.
	 * @return The proper ObjectOutputStream depending on whether a file is being appended to an existing file.
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
	 * An ObjectOutputStream class that does not create a new header.  Used to append objects to an existing file.
	 * Source: StackOverflow on StreamCorruptedException and appending to files.
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
