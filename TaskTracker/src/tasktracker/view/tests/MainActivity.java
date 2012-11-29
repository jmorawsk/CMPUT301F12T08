package tasktracker.view.tests;

import tasktracker.view.Login;
import junit.framework.Assert;
import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MainActivity extends ActivityInstrumentationTestCase2
{

    // Button Indices / Names
    private static final String NAME_TOOLBAR_CREATE = "Create a Task";                                     // "Create a Task"
    private static final int    TASK_CREATE         = 6;                                                   // "Create Task"
    private static final int    TASK_NAME           = 0;
    private static final int    TASK_DESCRIPTION    = 1;

    private static final String STRING_255          = "fdsasdhfjia;jiwojeijkl;fdjskfsdfsafds fsdafsdfsdfhjsdlfhjsdlafsadlfjk;sad7"
                                                            + "fjp2u348ifdsjaknc jkfjk;fjk jjksd;fjasdlfhjsdlafsadlfjkjjfdsaf9d0f89"
                                                            + "0sadfi9sdfjksdlfjksad;lfjksad;lfj9090dsfjksda;fjkd;slfajksa;dlfjasf09d"
                                                            + "989-859-05fkjsfk;ljksad;fj23482390-48239-j";

    private Solo                _solo;
    private Button              _createButton;

    public MainActivity() throws ClassNotFoundException
    {

        super(Login.class);
    }

    @Override
    protected void setUp() throws Exception
    {

        _solo = new Solo(getInstrumentation(), getActivity());
        _solo.clickOnButton("Enter Debug Mode");
        _solo.clickOnButton(NAME_TOOLBAR_CREATE);
        // clearDatabase();
    }

    private void clearDatabase()
    {

        _solo.clickOnButton("Enter Debug Mode");
        Button clear = _solo.getButton("Clear SQL Database");
        _solo.clickOnButton("Clear SQL Database");
    }

    public void testCreateButtonEnable()
    {

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

        //

        // Assert.assertTrue(_solo.searchText("Test Folder"));
        // _solo.clickOnButton("Add Folder");
        // _solo.enterText(TASK_NAME, "NEWLINE\n\n");
        // _solo.clickOnButton(TASK_NAME);
        // Assert.assertFalse(_solo.searchText("NEWLINE"));
        // _solo.clickOnButton("Add Folder");
        // _solo.enterText(TASK_NAME, "SINGLEQUOTE '' '' '''' '' '");
        // _solo.clickOnButton(TASK_NAME);
        // Assert.assertFalse(_solo.searchText("SINGLEQUOTE"));
        // _solo.clickOnButton("Add Folder");
        // _solo.enterText(
        // 0,
        // "TOOLONG asjdhflakjsdhflkajshdlfkjahskjdhflkashjdflakjshdflkjahsdlfkjahsldkjfhalskdj");
        // _solo.clickOnButton(TASK_NAME);
        // Assert.assertFalse(_solo.searchText("TOOLONG"));
        // _solo.clickLongInList(2, 0);
        // _solo.clickOnButton(TASK_NAME);
    }

    // public void testTaskFulfillment(){
    //
    // }

    public void testTypicalCase()
    {

        // Typical Case
        _solo.enterText(TASK_NAME, "Typical Case");
        _solo.enterText(TASK_DESCRIPTION, "Test task description.");
        _solo.clickOnButton(TASK_CREATE);
        Assert.assertTrue(_solo.searchText("Test Task Name"));

        _solo.clickOnButton(NAME_TOOLBAR_CREATE);
        _createButton = _solo.getButton(TASK_CREATE);
    }

    public void testTooLongInput()
    {

        _solo.enterText(TASK_NAME, "NAME TOO LONG " + STRING_255);
        _solo.enterText(TASK_DESCRIPTION, "NAME TOO LONG " + STRING_255);
        _solo.clickOnButton(NAME_TOOLBAR_CREATE);
    }

    public void testForPotentialSQLinjection()
    {

        _solo.enterText(TASK_NAME, "'SELECT * FROM members' '' ' '''''");
        _solo.enterText(TASK_DESCRIPTION, "'SELECT * FROM members' '' ' '''''");
        _solo.clickOnButton(NAME_TOOLBAR_CREATE);

    }

    public void testForRegex()
    {

        _solo.enterText(TASK_NAME, "\n\\s");
        _solo.enterText(TASK_DESCRIPTION, "fdsfds");
        _solo.clickOnButton(NAME_TOOLBAR_CREATE);

    }

    @Override
    public void tearDown() throws Exception
    {

        _solo.finishOpenedActivities();
    }
}