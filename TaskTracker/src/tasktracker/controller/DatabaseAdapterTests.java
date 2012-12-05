package tasktracker.controller;

import org.junit.*;

import tasktracker.model.elements.Task;
import tasktracker.view.CreateTaskView;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;

public class DatabaseAdapterTests extends
		ActivityInstrumentationTestCase2<CreateTaskView> {

	DatabaseAdapter dbHelper;

	public DatabaseAdapterTests() {
		super(CreateTaskView.class);
		dbHelper = new DatabaseAdapter(getActivity());
	}

	@Before
	public void setUp() {
		this.dbHelper.open();
		this.dbHelper.resetDatabase();
	}

	@Test
	/**
	 * Tasks may have the same information, but they must have unique IDs
	 */
	public void test_storesTaskByUniqueID() {
		Task task1 = new Task("Creator1", "Task Name 1", "Task Description");
		task1.setID("abc");
		Task task2 = new Task("Creator1", "Task Name 1", "Task Description");
		task2.setID("def");
		Task task3 = new Task("DifferentCreator", "Different Name",
				"Different Description");
		task3.setID("abc");

		Assert.assertTrue(this.dbHelper.createTask(task1) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task2) > 0);
		Assert.assertFalse(this.dbHelper.createTask(task3) > 0);

	}

	@Test
	/**
	 * Test that only public tasks are viewable to other users.
	 */
	public void test_fetchesTasksAvailableToUser() {
		Task task1 = new Task("Creator1", "Task1", "Description");
		task1.setID("abc");
		task1.setIsPrivate(false);
		Task task2 = new Task("Creator1", "Task2", "Description");
		task2.setID("def");
		task1.setIsPrivate(true);

		Assert.assertTrue(this.dbHelper.createTask(task1) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task2) > 0);

		Cursor cursor = this.dbHelper.fetchTasksAvailableToUser("Creator1",
				new String[0]);
		Assert.assertTrue(cursor.moveToFirst());
		Assert.assertTrue(cursor.getString(
				cursor.getColumnIndex(DatabaseAdapter.TASK)).equals("Task1"));
		Assert.assertFalse(cursor.moveToNext());
	}

	@Test
	/**
	 * Test that searches on an 'and' basis.
	 */
	public void test_fetchesTasksWithSimilarWords() {
		Task task1 = new Task("Creator1", "Dog cat sheep", "Lion rat pig");
		task1.setID("a");
		Task task2 = new Task("Creator1", "Puppy", "Doggie");
		task2.setID("b");
		Task task3 = new Task("Creator1", "Cute cats", "kitty");
		task3.setID("c");
		Task task4 = new Task("Creator1", "math science", "computers logic");
		task4.setID("d");

		Assert.assertTrue(this.dbHelper.createTask(task1) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task2) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task3) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task4) > 0);

		Cursor cursor = this.dbHelper.fetchTasksAvailableToUser("Creator1",
				new String[] { "dog" });
		Assert.assertEquals(2, cursor.getCount());
		while (cursor.moveToNext()) {
			String taskName = cursor.getString(cursor
					.getColumnIndex(DatabaseAdapter.TASK));
			Assert.assertTrue(taskName.equalsIgnoreCase("Dog cat sheep")
					|| taskName.equalsIgnoreCase("Puppy"));
		}

		// Filters on an AND basis.
		cursor = this.dbHelper.fetchTasksAvailableToUser("Creator1",
				new String[] { "dog", "cat" });
		Assert.assertEquals(1, cursor.getCount());
		Assert.assertTrue(cursor.moveToNext());
		String taskName = cursor.getString(cursor
				.getColumnIndex(DatabaseAdapter.TASK));
		Assert.assertTrue(taskName.equalsIgnoreCase("Dog cat sheep"));

	}

	@Test
	/**
	 * Tests that cute filter performs an inclusive-or search.
	 */
	public void test_fetchTasksWithCuteFilter() {
		Task task1 = new Task("Creator1", "Dog cat sheep", "Lion rat pig");
		task1.setID("a");
		Task task2 = new Task("Creator1", "Puppy", "Doggie");
		task2.setID("b");
		Task task3 = new Task("Creator1", "Cute cats", "kitty");
		task3.setID("c");
		Task task4 = new Task("Creator1", "math science", "computers logic");
		task4.setID("d");
		Task task5 = new Task("Creator1", "Bear", "CUDDLY");
		task5.setID("e");

		Assert.assertTrue(this.dbHelper.createTask(task1) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task2) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task3) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task4) > 0);
		Assert.assertTrue(this.dbHelper.createTask(task5) > 0);

		Cursor cursor = this.dbHelper.fetchTasksAvailableToUser("Creator1",
				new String[] { "cute" });
		Assert.assertEquals(3, cursor.getCount());
		while (cursor.moveToNext()) {
			String taskName = cursor.getString(cursor
					.getColumnIndex(DatabaseAdapter.TASK));
			Assert.assertTrue(taskName.equalsIgnoreCase("Dog cat sheep")
					|| taskName.equalsIgnoreCase("Puppy")
					|| taskName.equalsIgnoreCase("Cute cats")
					|| taskName.equalsIgnoreCase("Bear"));
		}
	}

	@After
	public void tearDown() {
		this.dbHelper.close();
	}

}
