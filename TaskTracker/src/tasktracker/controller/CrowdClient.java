package tasktracker.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import tasktracker.model.elements.Task;

import com.google.gson.Gson;

/**
 * CrowdSource Service Client (Teaser)
 * 
 * @author Victor Guana - guana[at]ualberta.ca University of Alberta, Department
 *         of Computing Science
 */
public class CrowdClient {

	// Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();

	// POST Request
	HttpPost httpPost = new HttpPost(
			"http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/");

	/*
	 * To convert the InputStream to String we use the BufferedReader.readLine()
	 * method. We iterate until the BufferedReader return null which means
	 * there's no more data to read. Each line will appended to a StringBuilder
	 * and returned as String. (c) public domain:
	 * http://senior.ceng.metu.edu.tr/
	 * 2009/praeda/2009/01/11/a-simple-restful-client-at-android/
	 */
	private String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * Consumes the LIST operation of the service
	 * 
	 * @return JSON representation of the task list
	 * @throws Exception
	 */
	public String listTasks() throws Exception {

		String jsonStringVersion = new String();
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("action", "list"));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = httpclient.execute(httpPost);

		String status = response.getStatusLine().toString();
		HttpEntity entity = response.getEntity();

		System.out.println(status);

		if (entity != null) {
			InputStream is = entity.getContent();
			jsonStringVersion = convertStreamToString(is);
		}

		// and ensure it is fully consumed
		// EntityUtils.consume(entity);
		entity.consumeContent();
		return jsonStringVersion;
	}

	/**
	 * Consumes the GET operation of the service
	 * 
	 * @return Task object given the id idP
	 * @throws Exception
	 */
	public Task getTask(String idP) throws Exception {

		Task responseTask = new Task();
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("action", "get"));
		nvps.add(new BasicNameValuePair("id", idP));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = httpclient.execute(httpPost);

		String status = response.getStatusLine().toString();
		HttpEntity entity = response.getEntity();

		System.out.println(status);

		if (entity != null) {
			InputStream is = entity.getContent();
			String jsonStringVersion = convertStreamToString(is);
			Type taskType = Task.class;
			responseTask = gson.fromJson(jsonStringVersion, taskType);
		}
		// EntityUtils.consume(entity);
		entity.consumeContent();
		return responseTask;

	}

	/**
	 * Consumes the POST/Insert operation of the service
	 * 
	 * @return JSON representation of the task created
	 * @throws Exception
	 */
	public Task insertTask(Task taskP) throws Exception {

		Task newTask = new Task();
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("action", "post"));
		nvps.add(new BasicNameValuePair("summary", taskP.getName()));
		nvps.add(new BasicNameValuePair("content", gson.toJson(taskP)));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = httpclient.execute(httpPost);

		String status = response.getStatusLine().toString();
		HttpEntity entity = response.getEntity();

		System.out.println(status);

		if (entity != null) {
			InputStream is = entity.getContent();
			String jsonStringVersion = convertStreamToString(is);
			Type taskType = Task.class;
			newTask = gson.fromJson(jsonStringVersion, taskType);
		}
		// EntityUtils.consume(entity);
		entity.consumeContent();
		return newTask;
	}

	public void nukeAll() throws Exception {

		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("action", "nuke"));
		nvps.add(new BasicNameValuePair("key", "judgedredd"));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = httpclient.execute(httpPost);

		String status = response.getStatusLine().toString();
		HttpEntity entity = response.getEntity();

		System.out.println(status);
	}

	public void testServiceMethods() {

		// Example Simple Task
		Task task = initializeTask();

		try {
			Task newT = this.insertTask(task);
			System.out.println("Inserted Task -> " + newT.getID());

			Task newTClone = this.getTask(newT.getID());
			System.out.println("Double Checking by Listing -> "
					+ newTClone.getID());

			String lot = this.listTasks();
			System.out.println("List of Tasks in the CrowdSourcer -> " + lot);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// httpPost.releaseConnection();
		}
	}

	/**
	 * Initializes a simple mock task
	 * 
	 * @return
	 */
	private Task initializeTask() {
		Task t = new Task("Creator", "Task Name", "Task Description", true,
				false);
		return t;
	}
}