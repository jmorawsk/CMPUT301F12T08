package tasktracker.view.tests;

import tasktracker.view.Login;
import junit.framework.Assert;
import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TaskCreateTests extends ActivityInstrumentationTestCase2 {

	// Button Indices / Names
	private static final String NAME_TOOLBAR_CREATE = "Create"; //
	// "Create a Task"
	private static final String TASK_CREATE = "Create Task"; // "Create Task"
	private static final int TASK_NAME = 0;
	private static final int TASK_DESCRIPTION = 1;

	private static final String STRING_255 = "fdsasdhfjia;jiwojeijkl;fdjskfsdfsafds fsdafsdfsdfhjsdlfhjsdlafsadlfjk;sad7"
			+ "fjp2u348ifdsjaknc jkfjk;fjk jjksd;fjasdlfhjsdlafsadlfjkjjfdsaf9d0f89"
			+ "0sadfi9sdfjksdlfjksad;lfjksad;lfj9090dsfjksda;fjkd;slfajksa;dlfjasf09d"
			+ "989-859-05fkjsfk;ljksad;fj23482390-48239-j";

	private Solo _solo;
	private Button _createButton;

	private static final String USER2 = "jellybeans";
	private static final String USER3 = "carrots";
	private static final String EMAIL = "cmput301f12t08@gmail.com";

	public TaskCreateTests() throws ClassNotFoundException {

		super(Login.class);
	}

	@Override
	protected void setUp() throws Exception {

		_solo = new Solo(getInstrumentation(), getActivity());
		_solo.clickOnButton("Enter Debug Mode");
		_solo.clickOnButton("Clear SQL Database");
	}

	public void testCreateButtonEnable() {
		_solo.clickOnButton(NAME_TOOLBAR_CREATE);
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

	public void testTaskNameInputs() {

		String moreThan60Chars = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijk";

		this.createBasicTask("Typical Case", "Test task description.");
		this.createBasicTask(moreThan60Chars, STRING_255);
		this.createBasicTask("';SELECT * FROM members'",
				"';SELECT * FROM members' '' ' '''''");
		this.createBasicTask("\\nhello\\t", "fdsfds");
		this.createBasicTask("%&action=list", "%\\&");

		// Typical Case
		Assert.assertTrue(_solo.searchText("Typical Case"));

		// Can only find the first 60 chars typed in, since edit text only
		// allows max 60 chars
		Assert.assertFalse(_solo.searchText(moreThan60Chars));
		Assert.assertTrue(_solo
				.searchText("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefgh"));

		// Does not allow SQL injection
		Assert.assertTrue(_solo.searchText("\\';SELECT \\* FROM members\\'"));

		// Does not treat backslashes as escape characters
		Assert.assertTrue(_solo.searchText("\\\\nhello\\\\t"));

		// Does not crash web server
		Assert.assertTrue(_solo.searchText("%\\&action=list"));

	}

	public void testPrivacyAndMembership() {
		String privateTask = "Private Task";
		String privateTaskWithMember = "Private with member: " + USER2;
		String publicTask = "Public to All";

		// Public
		this.createBasicTask(publicTask, "Description");

		// Private to Creator
		_solo.clickOnButton(NAME_TOOLBAR_CREATE);
		_solo.enterText(TASK_NAME, privateTask);
		_solo.enterText(TASK_DESCRIPTION, "Description");
		_solo.clickOnCheckBox(2); // Click privacy check box
		_solo.clickOnButton(TASK_CREATE);

		// Private with Member
		_solo.clickOnButton(NAME_TOOLBAR_CREATE);
		_solo.enterText(TASK_NAME, privateTaskWithMember);
		_solo.enterText(TASK_DESCRIPTION, "Description");
		_solo.enterText(2, USER2);
		_solo.clickOnCheckBox(2); // Click privacy check box
		_solo.clickOnButton(TASK_CREATE);

		Assert.assertTrue(_solo.searchText(privateTask));
		Assert.assertTrue(_solo.searchText(publicTask));
		Assert.assertTrue(_solo.searchText(privateTaskWithMember));

		this.logout("Debugger");

		// this.login(USER2);
		// Login Page
		_solo.enterText(2, USER2);
		_solo.enterText(3, EMAIL);
		_solo.clickOnButton(2);

		// Task List
		Assert.assertFalse(_solo.searchText(privateTask));
		Assert.assertTrue(_solo.searchText(publicTask));
		Assert.assertTrue(_solo.searchText(privateTaskWithMember));

		this.logout(USER2);

		// Login Page
		_solo.enterText(2, USER3);
		_solo.enterText(3, EMAIL);
		_solo.clickOnButton(2);

		// this.login(USER3);

		// Task List
		Assert.assertFalse(_solo.searchText(privateTask));
		Assert.assertTrue(_solo.searchText(publicTask));
		Assert.assertFalse(_solo.searchText(privateTaskWithMember));

	}

	@Override
	public void tearDown() throws Exception {
		_solo.finishOpenedActivities();
	}

	private void createBasicTask(String taskName, String taskDescription) {
		_solo.clickOnButton(NAME_TOOLBAR_CREATE);
		_solo.enterText(TASK_NAME, taskName);
		_solo.enterText(TASK_DESCRIPTION, taskDescription);
		_solo.clickOnButton(TASK_CREATE);
	}

	private void login(String user) {
		_solo.enterText(0, user);
		_solo.enterText(1, "");
		_solo.clickOnButton(1);
	}

	private void logout(String user) {
		_solo.waitForText(user);
		_solo.clickOnButton(user);
		_solo.sendKey(Solo.DOWN);
		_solo.sendKey(Solo.DOWN);
		_solo.sendKey(Solo.DOWN);
		_solo.sendKey(Solo.DOWN);
		_solo.sendKey(Solo.ENTER);
	}
}