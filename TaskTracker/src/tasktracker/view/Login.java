package tasktracker.view;

import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.Preferences;
//import tasktracker.model.WebDBManager;
import tasktracker.model.elements.RequestCreateUser;
import tasktracker.model.elements.RequestGetAUser;
import tasktracker.model.elements.RequestGetAllUsers;
import tasktracker.model.elements.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;

public class Login extends Activity {

	private DatabaseAdapter _dbHelper;
	private Cursor _cursor;

	EditText _loginUsername;
	EditText _loginPassword;

	EditText _newUsername;
	EditText _newEmail;
	EditText _newPassword;
	EditText _newPasswordConfirm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		_loginUsername = (EditText) findViewById(R.id.username);
		_loginPassword = (EditText) findViewById(R.id.password);

		_newUsername = (EditText) findViewById(R.id.new_username);
		_newEmail = (EditText) findViewById(R.id.new_email);
		_newPassword = (EditText) findViewById(R.id.new_password);
		_newPasswordConfirm = (EditText) findViewById(R.id.new_password_confirm);

		_dbHelper = new DatabaseAdapter(this);
		
		// Import users from the web server to the local database.
		RequestGetAllUsers request = new RequestGetAllUsers(getBaseContext());
		
		setupLogin();
		setupCreateAccount();
	}

	/**
	 * Validate a user's login and continue to the homepage.
	 */
	private void setupLogin() {
		Button login = (Button) findViewById(R.id.button_login);
		login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String username = _loginUsername.getText().toString();
				String password = _loginPassword.getText().toString();

				RequestGetAUser downloadUserAndSignIn = new RequestGetAUser(getBaseContext(), true, username);
				Preferences.setUsername(Login.this, username, true);
				proceedToHomePage(username);
			}

		});

	}

	/**
	 * Validate the creation of a new account and sign in.
	 */
	private void setupCreateAccount() {

		Button create = (Button) findViewById(R.id.button_create_account);
		create.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String username = _newUsername.getText().toString();
				String email = _newEmail.getText().toString();
				String password = _newPassword.getText().toString();
				String passwordConfirm = _newPasswordConfirm.getText()
						.toString();

				if (usernameTaken(username)) {
					ToastCreator.showShortToast(Login.this,
							"This username is unavailable.");
					return;
				}

				if (!isValidEmail(email)) {
					ToastCreator.showShortToast(Login.this,
							"Please supply a valid email address.");
					return;
				}

				if (!password.equals(passwordConfirm)) {
					ToastCreator.showShortToast(Login.this,
							"Your passwords do not match.");
					return;
				}
				
				User user = new User();
				user.setEmail(email);
				user.setName(username);
				user.setPassword(password);
				
				//Add user to crowdsourcer, and then local SQL db using crowdsourcer's returned ID
				RequestCreateUser creator = new RequestCreateUser(getBaseContext(), user, true);
				
				proceedToHomePage(username);
			}

		});
	}

	/**
	 * Checks if a username is available in the database.
	 * 
	 * @return True if the username is available in the database; otherwise
	 *         false.
	 */
	private boolean usernameTaken(String username) {

		_dbHelper.open();
		_cursor = _dbHelper.fetchUserViaName(username);
		boolean nameTaken = _cursor.moveToFirst();
		_cursor.close();
		_dbHelper.close();

		return nameTaken;

	}

	/**
	 * Navigate to the home page, TaskListView
	 * @param user the username of the actor signing in.
	 */
	private void proceedToHomePage(String user) {

		ToastCreator.showShortToast(getApplicationContext(), "Welcome, " + user + "!");
		Intent intent = new Intent(getApplicationContext(), TaskListView.class);
		finish();
		startActivity(intent);
	}

	private final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}
}
