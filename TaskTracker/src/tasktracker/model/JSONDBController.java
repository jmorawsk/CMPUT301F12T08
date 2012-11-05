package tasktracker.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import tasktracker.model.elements.Task;

/**
 * A class for interacting with a JSON web database (webserver)
 * @author Jason
 *
 */
public class JSONDBController
{
    static int contentIndex = 1;
    static String webAddress = "http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T08/";

    public static String[][] listTasksAsArrays(){
        String listCommand = "action=" + "list";
        return JSONDBParser.parseJSONArray(executeAction(listCommand));
    }
    public static Task[] listTasks(){
        String listCommand = "action=" + "list";
        //return JSONDBParser.parseJSONArray(executeAction(listCommand));
        return null;
    }

    public static String[] insertTask(String summary, String description){
        String insertCommand = "action=" + "post"
                + "&summary=" + summary.replace(' ', '+')
                + "&description=" + description.replace(' ', '+');
        return JSONDBParser.parseJSONObject(executeAction(insertCommand));
    }
    
    //TODO
    public static String[] insertTask(Task task){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(task);
            oos.flush();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String insertCommand = "action=" + "post"
                + "&summary=" + task.getDescription().replace(' ', '+')
                + "&content=" + baos.toByteArray()
                + "&description=" + task.getDescription().replace(' ', '+');
//        System.out.println("Test:" + baos.toString());
//        System.out.println("Output:" + baos.toByteArray().toString());
//        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//        System.out.println("Input:" + bais.toString());
        

        return JSONDBParser.parseJSONObject(executeAction(insertCommand));
    }

    public static String[] updateTask(String newSummary, String newDescription, String id){
        String updateCommand = "action=" + "update"
                + "&summary=" + newSummary.replace(' ', '+')
                + "&description=" + newDescription .replace(' ', '+')
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(updateCommand));
    }

    public static Task getTask(String id){
        Task myTask = null;
        String myContent = getTaskAsArray(id)[contentIndex];
        ByteArrayInputStream bais = new ByteArrayInputStream(myContent.getBytes());
        System.out.println("Content:" + bais.toString());

        try
        {
            ObjectInputStream ois = new ObjectInputStream(bais);
            myTask = (Task) ois.readObject();
        } catch (IOException e)
        {
            System.out.println("Error 1");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {

            System.out.println("Error 2");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return JSONDBParser.parseJSONObject(executeAction(getCommand));
        return myTask;
    }
    
    public static String[] getTaskAsArray(String id){
        String getCommand = "action=" + "get"
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(getCommand));
    }
    
    
    public static String[] removeTask(String id){
        String getCommand = "action=" + "remove"
                + "&id=" + id;
        return JSONDBParser.parseJSONObject(executeAction(getCommand));
    }

    protected static String nukeAll(){
        String nukeCommand = "action=" + "nuke"
                + "&key=" + "judgedredd";
        return executeAction(nukeCommand);
    }
    
    protected static String executeAction(String action){
        URI uri = null;
        try
        {
            uri = new URI(
                    "http",
                    "crowdsourcer.softwareprocess.es",
                    "/F12/CMPUT301F12T08/",
                    action,
                    null
                    );
        } catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return readFromURL(uri.toASCIIString());

    }
    protected static String oldexecuteAction(String action){
        return readFromURL(webAddress + action);

    }
    protected static String readFromURL(String URL){
        String readLine = null;
        try
        {
            System.out.println(URL);
            URL webServer;
            webServer =new URL(URL);
            //System.out.println(URLEncoder.encode(URL,"UTF-8"));
            //webServer = new URL(URLEncoder.encode(URL,"UTF-8"));
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
