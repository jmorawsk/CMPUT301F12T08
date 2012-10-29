package dbmanager;
import java.net.*;
import java.io.*;

public class dbmanager
{
    static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";

    //static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/Team1/";

    public static String listTasks(){
        String listCommand = "?action=" + "list";
        return executeAction(listCommand);
    }

    public static String insertTask(String summary, String description){
        String insertCommand = "?action=" + "post"
                + "&summary=" + summary
                + "&description=" + description;
        return executeAction(insertCommand);
    }

    public static String updateTask(String newSummary, String newDescription, String id){
        String updateCommand = "?action=" + "update"
                + "&summary=" + newSummary
                + "&description=" + newDescription 
                + "&id=" + id;
        return executeAction(updateCommand);
    }

    public static String getTask(String id){
        String getCommand = "?action=" + "update"
                + "&id=" + id;
        return executeAction(getCommand);
    }
    
    public static String removeTask(String id){
        String getCommand = "?action=" + "remove"
                + "&id=" + id;
        return executeAction(getCommand);
    }

    public static String nukeAll(){
        String nukeCommand = "?action=" + "nuke"
                + "&key=" + "judgedredd";
        return executeAction(nukeCommand);
    }
    
    public static String executeAction(String action){
        return readFromURL(webAddress + action);
    }

    public static String readFromURL(String URL){
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