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
import java.util.*;

/**
 * A class representing a task requirement. Currently, each requirement has one
 * item, but will be expanded to allow for multiple items (photos).
 * 
 * @author Katherine Jasniewski
 * @author Jeanine Bonot
 * 
 */
public abstract class Requirement implements Serializable {

	// TODO: Add abstract content property to allow for the viewing
	// of our requirements.

	private static final long serialVersionUID = 1L;
	protected Date timeStamp;

	/**
	 * Creates a new instance of the Requirement class and assigns a default
	 * value to the time stamp.
	 */
	public Requirement() {
		this.timeStamp = Calendar.getInstance().getTime();
	}

	/**
	 * Gets the time stamp of the requirement.
	 * 
	 * @return The date the requirement was fulfilled,
	 */
	public Date getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * Fulfill a requirement with a time stamp. Should be overwritten by each
	 * subclass for further implementation.
	 * 
	 * @return True if the user has fulfilled the requirement, false otherwise.
	 */
	public boolean fulfill() {
		this.timeStamp = new Date();
		return true;
	}

}
