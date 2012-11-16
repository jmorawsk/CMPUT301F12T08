package tasktrackertests.model;

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

import org.junit.*;

import tasktracker.model.*;

/**
 * Tests for the webdbmanager
 * @author Jason
 *
 */
public class DBManagerTests {
    WebDBManager myDBManager;
    @Before
    public void setUp() throws Exception
    {

        myDBManager = new WebDBManager();
    }
    /** Tests nuke all method */
    @Test
    public void testNukeAll(){
        myDBManager.nukeAll();
        String[][] result = myDBManager.listTasksAsArrays();
        Assert.assertEquals(result,null);
    }
    @Test
    public void testAddAndGetTask(){
        myDBManager.nukeAll();
        String[][] result = myDBManager.listTasksAsArrays();


//        Date theDate = new Date();
//        Task task1 = new Task("Task 1", theDate, "This is a task that was created", false);
        Assert.assertEquals(result,null);

    }
}
