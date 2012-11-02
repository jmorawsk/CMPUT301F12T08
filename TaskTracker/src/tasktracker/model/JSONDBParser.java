package tasktracker.model;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONDBParser
{
    static JSONParser parser = new JSONParser();
    
    public static String[] parseJSONObject(String objectString){
        String[] myTask = null;
        try
        {
            JSONObject myJSONObject=(JSONObject)parser.parse(objectString);
            Object[] keys = myJSONObject.keySet().toArray();
            myTask = new String[keys.length];
            for (int i = 0; i<keys.length;i++){

                myTask[i] = myJSONObject.get(keys[i]).toString();
            }
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return myTask;
    }
    
    public static String[][] parseJSONArray(String objectString){
        String[][] parsedString = null;
        Object myTask;
        try
        {
            myTask = parser.parse(objectString);
            JSONArray array = (JSONArray)myTask;
            if (array.size()==0)
                return null;
            JSONObject myJSONObject;
            parsedString = new String[array.size()][];
            for (int n = 0; n<array.size();n++){
                myJSONObject=(JSONObject)array.get(n);
                parsedString[n] = parseJSONObject(myJSONObject.toJSONString());
            }
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return parsedString;
    }
}
