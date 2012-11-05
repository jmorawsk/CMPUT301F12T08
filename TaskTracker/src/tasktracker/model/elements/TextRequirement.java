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

/**
 * A class representing a text requirement.
 * 
 * @author Jeanine Bonot
 * 
 */
public class TextRequirement extends Requirement {

	private static final long serialVersionUID = 1L;

	private String text;
	
	/** Gets the text string */
	public String getText(){
		return this.text;
	}
	
	/** Sets the text string */
	public void setText(String text){
		this.text = text;
	}
	
	/**
	 * Fulfill the requirement by adding text.
	 * 
	 * @return true if the
	 */
	@Override
    public boolean fulfill() {
		// TODO: Prompt user to add text.
		// TODO: Text must not be left empty (or contain only whitespace)
		return super.fulfill();
	}
}
