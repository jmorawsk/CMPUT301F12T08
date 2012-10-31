package dbmanager;

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
