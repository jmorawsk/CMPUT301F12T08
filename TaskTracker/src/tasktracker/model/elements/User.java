package tasktracker.model.elements;

/**
 * An application user.
 * 
 * @author Jeanine Bonot
 * 
 */
public class User {
	private static int _userCount = 0;
	
	private String name;
	private int id;
	
	/**
	 * Creates a new instance of the User class and assigns an ID number.
	 */
	public User(){
		this.id = ++_userCount;
	}
	
	/**
	 * Creates a new instance of the User class.
	 * 
	 * @param name
	 *            The name of the user.
	 */
	public User(String name) {
		if (!this.assignName(name)){
			// Assignment was unsuccessful, notify app user.
		}
		else
		{
			this.id = ++_userCount;
		}

	}

	/**
	 * Gets the name of the user.
	 * @return The name of the user.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the user name if the new name is available.
	 * 
	 * @param name
	 *            The user name.
	 * @return True if the new name was set, false otherwise.
	 */
	public void setName(String name) {
		
		if (!this.assignName(name)){
			// Notify user that nothing was changed.
		}

	}
	
	public int getID(){
		return this.id;
	}

	/**
	 * Sets the name if the name is available in the system.
	 * 
	 * @param name
	 *            The name to be validated.
	 * @return True if the name was successfully set, false otherwise.
	 */
	private boolean assignName(String name) {
		// TODO: Check online database.
		this.name = name;
		return true;
	}
}
