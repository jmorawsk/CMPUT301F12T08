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
