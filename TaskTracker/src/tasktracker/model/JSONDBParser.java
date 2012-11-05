package tasktracker.model;

import java.io.Console;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A class for parsing JSON objects
 * @author Jason
 *
 */
public class JSONDBParser
{
    //Initialize the JSONParser from the JSON_simple jar
    static JSONParser parser = new JSONParser();
    
    /**
     * This method takes in a string that represents a JSONObject and
     * parses it, returning an array of the objects values
     * @param objectString      a string that can be cast as JSONObject
     * @return  a string array of the values of the JSONObject
     */
    public static String[] parseJSONObject(String objectString){
        //initialize our output array
        String[] myTask = null;
        try
        {
            //create a JSONObject from our input string
            JSONObject myJSONObject=(JSONObject)parser.parse(objectString);
            //get the keys from our JSONObject
            Object[] keys = myJSONObject.keySet().toArray();
            myTask = new String[keys.length];
            //for each key, add the value to the output array
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
    /**
     * 
     * @param objectString      a string that can be cast to a JSONArray
     * @return an array of string arrays, first index is selecting the
     * JSONObject, the second index is the values of the object
     */
    public static String[][] parseJSONArray(String objectString){
        //initialize our output matrix
        String[][] parsedString = null;
        Object myTask;
        try
        {
            //create our JSONArray
            myTask = parser.parse(objectString);
            JSONArray array = (JSONArray)myTask;
            if (array.size()==0)
                return null;
            JSONObject myJSONObject;
            parsedString = new String[array.size()][];
            //for each JSONObject in the array, we want to parse
            for (int n = 0; n<array.size();n++){
                myJSONObject=(JSONObject)array.get(n);
                parsedString[n] = parseJSONObject(myJSONObject.toJSONString());
                //System.out.println("Line " +n+ ": " +parsedString[n]);
            }
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return parsedString;
    }
}
