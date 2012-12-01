//package tasktracker.model;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//
//import android.os.AsyncTask;
//
////TODO: Depricated, replaced with AccessURL. Jason, should we remove this or do you still need it?
///*
// * @author Jason (Mike removed his additions into Request classes)
// * 
// * Create a new instance and run .execute(String URL) to access a URL
// * 
// */
//public class ReadFromURL extends AsyncTask<String,Void,String>
//{
//	
//    protected String doInBackground(String... urls)
//    {
//        String readLine = null;
//        try
//        {
//            URL webServer;
//            webServer =new URL(urls[0]);
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(webServer.openStream()));
//
//            String inputLine;
//            while ((inputLine = in.readLine()) != null){
//                readLine = inputLine;
//            }
//
//            in.close();
//        } catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return readLine;
//    }
//    
//    protected void onPostExecute(String line) {
//        // TODO: check this.exception 
//        // TODO: do something with the feed
//    	//ToastCreator.showLongToast(getBaseContext(), "Task created! DB summary: n/a");
//    		//TODO: Find some way to show a popup without requiring passing a Context through 15 methods
//    	
//    	}
//    }
//    