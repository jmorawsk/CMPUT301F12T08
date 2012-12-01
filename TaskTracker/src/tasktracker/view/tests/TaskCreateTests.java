package tasktracker.view.tests;

import tasktracker.view.Login;
import junit.framework.Assert;
import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TaskCreateTests extends ActivityInstrumentationTestCase2 {

	// Button Indices / Names
	private static final String NAME_TOOLBAR_CREATE = "Create a Task"; // "Create a Task"
	private static final String TASK_CREATE = "Create Task"; // "Create Task"
	private static final int TASK_NAME = 0;
	private static final int TASK_DESCRIPTION = 1;

	private static final String STRING_255 = "fdsasdhfjia;jiwojeijkl;fdjskfsdfsafds fsdafsdfsdfhjsdlfhjsdlafsadlfjk;sad7"
			+ "fjp2u348ifdsjaknc jkfjk;fjk jjksd;fjasdlfhjsdlafsadlfjkjjfdsaf9d0f89"
			+ "0sadfi9sdfjksdlfjksad;lfjksad;lfj9090dsfjksda;fjkd;slfajksa;dlfjasf09d"
			+ "989-859-05fkjsfk;ljksad;fj23482390-48239-j";

	private Solo _solo;
	private Button _createButton;

	public TaskCreateTests() throws ClassNotFoundException {

		super(Login.class);
	}

	@Override
	protected void setUp() throws Exception {

		_solo = new Solo(getInstrumentation(), getActivity());
		_solo.clickOnButton("Enter Debug Mode");
		_solo.clickOnButton("Clear SQL Database");
		_solo.clickOnButton(NAME_TOOLBAR_CREATE);
	}

	private void clearDatabase() {

		_solo.clickOnButton("Enter Debug Mode");
		Button clear = _solo.getButton("Clear SQL Database");
		_solo.clickOnButton("Clear SQL Database");
	}

	public void _testCreateButtonEnable() {
		_createButton = _solo.getButton(TASK_CREATE);
		// Name, No Description
		_solo.enterText(TASK_NAME, "Name with no description.");
		Assert.assertFalse(_createButton.isEnabled());

		// Description / No Name
		_solo.enterText(TASK_NAME, "");
		_solo.enterText(TASK_DESCRIPTION, "Description.");
		Assert.assertFalse(_createButton.isEnabled());

		_solo.enterText(TASK_NAME, "Name/Description Enable");
		Assert.assertTrue(_createButton.isEnabled());

		_solo.clickOnButton(TASK_CREATE);
		Assert.assertTrue(_solo.searchText("Name/Description Enable"));
	}

	public void _testTypicalCase() {

		this.createBasicTask("Typical Case", "Test task description.");
		Assert.assertTrue(_solo.searchText("Typical Case"));
	}

	public void _testTooLongInput() {
		String moreThan60Chars = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefg";
		this.createBasicTask(moreThan60Chars, STRING_255);

	}

	public void _jtestForPotentialSQLinjection() {

		this.createBasicTask("';SELECT * FROM members'",
				"';SELECT * FROM members' '' ' '''''");
		Assert.assertTrue(_solo.searchText("SELECT"));

	}

	public void testForRegex() {

		this.createBasicTask("\\nhello\\t", "fdsfds");
		Assert.assertTrue(_solo.searchText("hello\\\\t"));

	}

	public void _testPrivacy() {
		_solo.enterText(TASK_NAME, "Private Task");
		_solo.enterText(TASK_DESCRIPTION, "Description");
		_solo.clickOnCheckBox(2); // Click privacy check box
	}

	@Override
	public void tearDown() throws Exception {
		_solo.finishOpenedActivities();
	}

	private void createBasicTask(String taskName, String taskDescription) {
		_solo.enterText(TASK_NAME, taskName);
		_solo.enterText(TASK_DESCRIPTION, taskDescription);
		_solo.clickOnButton(TASK_CREATE);
	}
}