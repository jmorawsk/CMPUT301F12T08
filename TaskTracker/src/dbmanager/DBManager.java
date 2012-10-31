package dbmanager;
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