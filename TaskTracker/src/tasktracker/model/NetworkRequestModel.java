package tasktracker.model;

import android.content.Context;

/*
 * @author Mike
 * 
 * Making requests to the network often requires passing the AccessURL class a method to execute after
 * the read has completed (it cannot return values as it's an asynchronous task). Thus, it 
 * must be run by creating a Request class inherriting from this and and passing it the object.
 * 
 * Note: Passing the request to AccessURL is usually done in the Constructor
 */
public interface NetworkRequestModel {
	public final String crowdsourcerAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";
	
	public Context getContext();
	
	public String getCrowdsourcerCommand();
	
	public void runAfterExecution(String line);
	
}

