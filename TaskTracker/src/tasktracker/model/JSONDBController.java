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
import java.net.URL;



public class JSONDBController
{
    static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";

    //static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/Team1/";

    public static String[][] listTasks(){
        String listCommand = "?action=" + "list";
        return JSONDBParser.parseJSONArray(executeAction(listCommand));
    }

    public static String[] insertTask(String summary, String description){
        String insertCommand = "?action=" + "post"
                + "&summary=" + summary.replace(' ', '+')
                + "&description=" + description.replace(' ', '+');
        return JSONDBParser.parseJSONObject(executeAction(insertCommand));
    }

    public static String[] updateTask(String newSummary, String newDescription, String id){
        String updateCommand = "?action=" + "update"
                + "&summary=" + newSummary.replace(' ', '+')
                + "&description=" + newDescription .replace(' ', '+')
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(updateCommand));
    }

    public static String[] getTask(String id){
        String getCommand = "?action=" + "update"
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(getCommand));
    }
    
    public static String[] removeTask(String id){
        String getCommand = "?action=" + "remove"
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(getCommand));
    }

    protected static String nukeAll(){
        String nukeCommand = "?action=" + "nuke"
                + "&key=" + "judgedredd";
        return executeAction(nukeCommand);
    }
    
    protected static String executeAction(String action){
        return readFromURL(webAddress + action);
    }

    protected static String readFromURL(String URL){
        String readLine = null;
        try
        {
            URL webServer = new URL(URL);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(webServer.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null){
                readLine = inputLine;
                //System.out.println(readLine);
            }

            in.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return readLine;
    }
}
