package tasktracker.view;

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

import java.util.*;

import android.os.AsyncTask;
//import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import tasktracker.controller.TaskController;
import tasktracker.model.WebDBManager;
import tasktracker.model.elements.*;

/**
 * An activity that displays a list of tasks that a user can view and fulfill.
 * 
 * @author Jeanine Bonot
 * 
 */
public class TaskListView extends Activity {

    private ListView taskListView;
    private List<Task> taskList;
    public List<Task> webTaskList;
    // private List<String> tasks;
    private String[] tasks = new String[0];

    /** The current app user */
    private String _user;

    private WebDBManager webManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        webManager = new WebDBManager();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _user = extras.getString("USER");
        }

        // Assign ListView and its on item click listener.
        taskListView = (ListView) findViewById(R.id.taskList);
        taskListView.setOnItemClickListener(new handleList_Click());

        // TODO: read from database and display
        // String[][] webTasks = webManager.listTasksAsArrays();
        // for(int n=0;n<webTasks.length;n++){
        // tasks.add(webTasks[n][0]);
        // }
        // ArrayAdapter<String> adapter = new
        // ArrayAdapter<String>(this,R.layout.list_item, tasks);
        // taskListView.setAdapter(adapter);

        Button buttonMyTasks = (Button) findViewById(R.id.buttonMyTasks);
        Button buttonCreate = (Button) findViewById(R.id.buttonCreateTask);
        Button buttonNotifications = (Button) findViewById(R.id.buttonNotifications);
        buttonMyTasks.setEnabled(false);

        buttonCreate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        CreateTaskView.class);
                intent.putExtra("USER", _user);
                startActivity(intent);
            }
        });

        buttonNotifications.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        NotificationListView.class);
                intent.putExtra("USER", _user);
                startActivity(intent);

            }
        });

        setDebugStuff();

    }

    void setDebugStuff() {
        Button deleteFile = (Button) findViewById(R.id.debugButton);

        deleteFile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (TaskController.deleteFile()) {
                    loadTasks();
                    showToast("Deleted file on SD");
                } else {
                    showToast("Failed to delete file from SD");
                }
            }
        });
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    protected void onStart() {
        super.onStart();
        loadTasks();
        contactWebserver webRequest = new contactWebserver();
        webRequest.execute();
    }

    private void loadTasks() {

        taskList = TaskController.readFile();
        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
                R.layout.list_item, taskList);
        taskListView.setAdapter(adapter);
    }

    /**
     * A handler for clicking on a task item. Shows a menu of possible controls.
     * 
     * @author Jeanine Bonot
     * 
     */
    class handleList_Click implements OnItemClickListener {

        public void onItemClick(AdapterView<?> myAdapter, View myView,
                int myItemInt, long mylng) {
            Task task = taskList.get(myItemInt);
            Intent intent = new Intent(getApplicationContext(), TaskView.class);
            intent.putExtra("TASK", task);
            startActivity(intent);

            // showItemMenu(myView, myItemInt);
        }

        /**
         * Displays the menu for a given task.
         * 
         * @param view
         *            The selected task element.
         * @param index
         *            The index of the task in the ListView.
         */
        private void showItemMenu(View view, final int index) {
            PopupMenu menu = new PopupMenu(TaskListView.this, view);
            // menu.getMenuInflater().inflate(R.menu.popup, menu.getMenu());

            // TODO: Create menu content, set OnMenuItemClickListener, update
            // tasks upon deletion
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                public boolean onMenuItemClick(MenuItem item) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });

        }

    }

    private void update(){
        loadTasks();
        taskList.addAll(webTaskList);
        ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this,
                R.layout.list_item, taskList);
        taskListView.setAdapter(adapter);
        
    }
    
    private class contactWebserver extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... temp) {

            webTaskList = new ArrayList<Task>();
            System.out.println("testing");
            String[][] results = webManager.listTasksAsArrays();
            String id;
            Task newTask;
            for(int n=0; n<results.length; n++)
            {
                if(results[n].length>1)
                {
                    System.out.println("index =" +n);
                    id = results[n][1];
                    newTask = webManager.getTask(id);
                    webTaskList.add(newTask);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused){
            //update UI with my objects
            //taskList.addAll(webTaskList);
            update();
        }

    }
}
