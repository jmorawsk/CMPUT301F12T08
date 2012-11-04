package tasktracker.controller.tests;
import org.junit.*;
import static org.junit.Assert.*;

import tasktracker.model.elements.*;
import tasktracker.controller.*;

public class TaskControllerTests {
	
	private TaskController controller;
	
	/**
	 * This method is run before each test.
	 * @throws Exception
	 */
	@Before
	public void  setUp() throws Exception {
		controller = new TaskController();
	}
	
}