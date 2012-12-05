package tasktracker.controller;

import static org.junit.Assert.*;

import org.junit.*;

import tasktracker.model.elements.Task;
import tasktracker.view.Login;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class DatabaseAdapterTests extends ActivityInstrumentationTestCase2 {
	
	DatabaseAdapter dbHelper;
	
	public DatabaseAdapterTests(){
		super(Login.class);
//		dbHelper = new DatabaseAdapter(getApplicationContext());
	}
	
	 @Before
	    public void setUp() {
	        this.dbHelper.open();
	        this.dbHelper.resetDatabase();
	    }
	
	@Test
	public void test_storesTaskByUniqueID() {
		Task task1 = new Task("Creator1", "Task Name 1", "Task Description");
		task1.setID("abc");
		Task task2 = new Task("Creator1", "Task Name 1", "Task Description");
		task2.setID("def");
		Task task3 = new Task("DifferentCreator", "Different Name", "Different Description");
		task3.setID("abc");

		Assert.assertTrue(this.dbHelper.createTask(task1) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task2) > 0);
		Assert.assertFalse(this.dbHelper.createTask(task3) > 0);
		
	}
	
	@After
    public void tearDown() {
        this.dbHelper.close();
    }

}
