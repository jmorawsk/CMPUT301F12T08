package tasktracker.model;

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

import android.content.Context;

/**
 * Model for handling network requests
 * 
 * Making requests to the network often requires passing the AccessURL class a method to execute after
 * the read has completed (it cannot return values as it's an asynchronous task). Thus, it 
 * must be run by creating a Request class inherriting from this and and passing it the object.
 * 
 * Note: Passing the request to AccessURL is usually done in the Constructor
 * 
 * @author Mike
 */
public interface NetworkRequestModel {
    
        /**
         * The address of our webserver
         */
	public final String crowdsourcerAddress = 
	        "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";
	
	/**
	 * Get the context of the request
	 * @return Context: the context of the request
	 */
	public Context getContext();
	
	/**
	 * Returns a string representing the command
	 * @return String: a string representing the command
	 */
	public String getCrowdsourcerCommand();
	
	/**
	 * What the request should do after receiving a respons from
	 * the webserver
	 * @param line: the string that should be executed
	 */
	public void runAfterExecution(String line);
}

