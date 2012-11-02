package tasktracker.controller;

import java.io.Serializable;
import java.util.List;

/**
 * An interface that manages the local file storage of objects.
 * 
 * @author jbonot
 * 
 */
public interface LocalObjectController<T extends Serializable> {
	
	/**
	 * Delete the TaskTracker's file, if it exists.
	 * 
	 * @return Returns true if the file was successfully deleted, false
	 *         otherwise.
	 */
	public boolean deleteFile();

	public void writeFile(T object);

	public List<T> readFile();

}
