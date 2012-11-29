package tasktracker.view.tests;

import tasktracker.view.Login;
import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MainActivity extends ActivityInstrumentationTestCase2 {

	// Button Indices / Names
	private static final String NAME_TOOLBAR_CREATE = "Create a Task"; // "Create a Task"
	private static final int INDEX_TASK_CREATE = 6; // "Create Task"
	
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "tasktracker.view.Login";
	private static Class launcherActivityClass;

	private Solo _solo;

	static {
		try {
			launcherActivityClass = Class
					.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public MainActivity() throws ClassNotFoundException {
		super(Login.class);
	}

	@Override
	protected void setUp() throws Exception {
		_solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testTaskCreate() {
		
		// Typical Case
		_solo.clickOnButton("Enter Debug Mode");
		_solo.clickOnButton(NAME_TOOLBAR_CREATE);
		_solo.enterText(0, "Test Task Name");
		_solo.enterText(1, "Test task description.");
		_solo.clickOnButton(INDEX_TASK_CREATE);
		Assert.assertTrue(_solo.searchText("Test Task Name"));
		
		// Does not input task name
		_solo.clickOnButton(NAME_TOOLBAR_CREATE);
		_solo.enterText(1, "Description.");
		Button createButton = _solo.getButton(INDEX_TASK_CREATE);
		Assert.assertFalse(createButton.isEnabled());
		_solo.enterText(0, "Did not have name.");
//		
		
//		Assert.assertTrue(_solo.searchText("Test Folder"));
//		_solo.clickOnButton("Add Folder");
//		_solo.enterText(0, "NEWLINE\n\n");
//		_solo.clickOnButton(0);
//		Assert.assertFalse(_solo.searchText("NEWLINE"));
//		_solo.clickOnButton("Add Folder");
//		_solo.enterText(0, "SINGLEQUOTE '' '' '''' '' '");
//		_solo.clickOnButton(0);
//		Assert.assertFalse(_solo.searchText("SINGLEQUOTE"));
//		_solo.clickOnButton("Add Folder");
//		_solo.enterText(
//				0,
//				"TOOLONG asjdhflakjsdhflkajshdlfkjahskjdhflkashjdflakjshdflkjahsdlfkjahsldkjfhalskdj");
//		_solo.clickOnButton(0);
//		Assert.assertFalse(_solo.searchText("TOOLONG"));
//		_solo.clickLongInList(2, 0);
//		_solo.clickOnButton(0);
	}
	
	public void testTaskFulfillment(){
		
	}

	@Override
	public void tearDown() throws Exception {
		_solo.finishOpenedActivities();
	}
}