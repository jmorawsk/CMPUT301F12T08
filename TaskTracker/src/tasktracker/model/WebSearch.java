package tasktracker.model;

import java.util.ArrayList;

import tasktracker.model.elements.Task;



public class WebSearch
{
    ArrayList<Task> webTaskList;
    final WebDBManager webManager;
    final String[] keywords;
    public WebSearch(final String[] keywords, final WebDBManager manager){
        this.webManager = manager;
        this.keywords = keywords;
    }

    public ArrayList<Task> run() {
        webTaskList = new ArrayList<Task>();
        System.out.println("testing");
        String[][] results = webManager.listTasksAsArrays();
        String id;
        for (int n = 0; n < results.length; n++) {
            if (results[n].length > 1) {

                id = results[n][1];
                if (keywords.length!=0){
                    for (String keyword:keywords){
                        String summary = results[n][0];
                        if (summary.indexOf(keyword)!=-1){
                            getAndAddTask(id);
                        }
                    }
                }
                else
                {
                    getAndAddTask(id);
                }
            }
        }
        return webTaskList;
        
        
    }

        private void getAndAddTask(String id){
            System.out.println("index =");
            Task newTask = webManager.getTask(id);
            webTaskList.add(newTask);
        }
    }
