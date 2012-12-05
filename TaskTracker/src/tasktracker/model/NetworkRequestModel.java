package tasktracker.model;

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

