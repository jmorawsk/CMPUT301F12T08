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


public class DBManager
{
    static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";

    //static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/Team1/";

    public static String[][] listTasks(){
        return JSONDBController.listTasks();
    }

    public static String[] insertTask(String summary, String description){
        return JSONDBController.insertTask(summary,description);
    }

    public static String[] updateTask(String newSummary, String newDescription, String id){
        return JSONDBController.updateTask(newSummary,newDescription,id);
    }

    public static String[] getTask(String id){
        return JSONDBController.getTask(id);
    }
    
    public static String[] removeTask(String id){
        return JSONDBController.removeTask(id);
    }

    protected static String nukeAll(){
        return JSONDBController.nukeAll();
    }
}