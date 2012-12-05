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

import com.google.gson.Gson;
import android.content.Context;
import tasktracker.model.AccessURL;
import tasktracker.model.NetworkRequestModel;

/**
 * Creates an object to add a Notification to Crowdsourcer
 * 
 * Run by creating an instance.
 */
public class RequestCreateNotification implements NetworkRequestModel {
        private Context context;
        private String requestString;
        static final Gson gson = new Gson();

        /**
         * 
         * @param context the current context
         * @param notification the notification to create
         */
        public RequestCreateNotification(Context context, Notification notification) {
                this.context = context;
                //construct the command
		String command = "action=" + "post" + "&summary=" + "<Notification>"
				+ notification.getMessage() + "<TaskID>" 
				+ notification.getTaskId() + "<Date>" + notification.getDate() + "<Recipients>"
				+ notification.getRecipientsString();
		//turn command to url
		requestString = AccessURL.turnCommandIntoURL(command);
		//send url
		AccessURL access = new AccessURL(this);
		access.execute(getCrowdsourcerCommand());
	}

	public Context getContext() {
		return context;
	}

	public String getCrowdsourcerCommand() {
		return requestString;
	}

	public void runAfterExecution(String line) {
		// Do nothing.
	}
}
