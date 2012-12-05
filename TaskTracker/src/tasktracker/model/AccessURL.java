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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import android.os.AsyncTask;

/**
 * Asynchronous task for accessing webserver
 * 
 * @author Mike, copied base code from Jason's old ReadFromURL
 * 
 * DO NOT RUN THIS CLASS DIRECTLY, go through AccessURLRequest
 */
public class AccessURL extends AsyncTask<String, Void, String> {
	NetworkRequestModel request;

	public AccessURL(NetworkRequestModel reques) {
		request = reques;
	}

	protected String doInBackground(String... urls) {
		String readLine = null;
		try {
			URL webServer;
			webServer = new URL(urls[0]);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					webServer.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				readLine = inputLine;
			}

			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readLine;
	}

	/**
	 * Method to turn a command int a valid URL
	 * 
	 * Static method.
	 * 
	 * @param command: Hand this method a command like 'action=post&summary=" ...'
	 *  (note the lack of a preceeding '&') to be execute on webserver
	 * 
	 * @return String: returns a valid URL (string) to run it on
         * crowdsourcer.
	 */
	public static String turnCommandIntoURL(String command) {
		URI uri = null;
		try {
			uri = new URI("http", "crowdsourcer.softwareprocess.es",
					"/F12/CMPUT301F12T08/", command, null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	        System.out.println(uri.toASCIIString());
		return uri.toASCIIString();
	}

	/**
	 * This will return the string after a given tag. ie, if input =
	 * "<task> This is my Task <date> November 29 <creator> Mike Dardis", and
	 * tag = "<date>", this will return " November 29 ".
	 * 
	 * Search quits upon finding a '}' or end of input string. Output string
	 * ends at: '<', '"', or end of input string.
	 * 
	 * Returns null if not found
	 * 
	 * @param tag The tag preceeding the input you want
	 * 
	 * @param input The string of input to look in
	 * 
	 * @param start The character number to start searching at
	 * 
	 * @return The found string or null if not found
	 */
	public static String getTag(String tag, String input, int start) {
		// Not tested yet
		String value = "";
		int pos = -1;
		tag = tag; // TODO not finding tag
		pos = input.indexOf(tag, start);
		if (pos == -1)
			return null; // Not found

		if (pos > input.indexOf('}', start))
			return null; // String found outside of entry brackets

		pos = pos + tag.length();
		// Record string until end state found

		while (pos < input.length()) {
			value = value + input.charAt(pos);
			pos += 1;
			if (input.charAt(pos) == '<' || input.charAt(pos) == '\"')
				break;
		}
		return value;
	}

	public static String readNextTag(String input, int pos) {
		String tag = "";
		int upperBound = input.length();

		while (input.charAt(pos) != '<' && pos < upperBound)
			pos++;

		if (pos == upperBound)
			return null;

		while (input.charAt(pos) != '>' && pos < upperBound)
			tag += input.charAt(pos++);

		if (pos == upperBound)
			return null;

		return tag;
	}

	// Must be read after reading the tag.
	public static String readNextValue(String input, int pos) {
		String value = "";
		int upperBound = input.length();

		while (input.charAt(pos) != '<' && pos < upperBound) {
			value += input.charAt(pos++);
		}

		if (pos == upperBound)
			return null;

		return value;
	}

	protected void onPostExecute(String line) {
		// TODO: check this.exception
		// TODO: do something with the feed

		request.runAfterExecution(line);

	}

	/**
	 * This will return the string after a given tag. ie, if input =
	 * "<task> This is my Task <date> November 29 <creator> Mike Dardis", and
	 * tag = "<date>", this will return " November 29 ".
	 * 
	 * Search quits upon finding a '}' or end of input string. Output string
	 * ends at: '<', '"', or end of input string.
	 * 
	 * Returns null if not found
	 * 
	 * @param tag The tag preceeding the input you want
	 * 
	 * @param input The string of input to look in
	 * 
	 * @param start The character number to start searching at
	 * 
	 * @return The found string or null if not found
	 */
	private String getTaggedText(String tag, String input, int start) {
		// Not tested yet
		String value = "";
		int pos = -1;
		tag = tag; // TODO not finding tag
		pos = input.indexOf(tag, start);
		if (pos == -1)
			return null; // Not found
		if (pos > input.indexOf('}', start))
			return null; // String found outside of entry brackets
		pos = pos + tag.length();
		// Record string until end state found
		while (pos < input.length()) {
			value = value + input.charAt(pos);
			pos += 1;
			if (input.charAt(pos) == '<' || input.charAt(pos) == '\"')
				break;
		}
		return value;
	}

}
