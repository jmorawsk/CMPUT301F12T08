package tasktracker.view;

import tasktracker.controller.DatabaseAdapter;
import tasktracker.model.Preferences;
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
		
		// TODO Delete before release
		_dbHelper.open();
		_dbHelper.createUser("Debugger", "cmput301f12t08@gmail.com", "");
		_dbHelper.createUser("jbonot", "jbonot@ualberta.ca", "tasktracker");
		_dbHelper.createUser("jmorawsk", "cmput301f12t08@gmail.com", "tasktracker");
		_dbHelper.createUser("jasiewsk", "cmput301f12t08@gmail.com", "tasktracker");
		_dbHelper.createUser("dardis", "cmput301f12t08@gmail.com", "tasktracker");
		_dbHelper.close();
		// End to-do
		
		setupLogin();
		setupCreateAccount();
		setupDebugStuff();
	}

	/**
	 * Skip database checks and use app with username "Debugger"
	 */
	void setupDebugStuff() {
		Button debug = (Button) findViewById(R.id.button_debug);
		debug.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setPreferences("Debugger", "cmput301f12t08@gmail.com", "", true);
				proceedToHomePage("Debugger");
			}

		});
	}

	private void setupLogin() {
		Button login = (Button) findViewById(R.id.button_login);
		login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				_dbHelper.open();
				String username = _loginUsername.getText().toString();
				String password = _loginPassword.getText().toString();

				_cursor = _dbHelper.fetchUser(_loginUsername.getText()
						.toString(), _loginPassword.getText().toString());

				if (_cursor.moveToFirst()) {

					String email = _cursor.getString(_cursor
							.getColumnIndex(DatabaseAdapter.EMAIL));
					_cursor.close();
					_dbHelper.close();
					setPreferences(username, email, password, true);
					proceedToHomePage(username);

				} else {
					// User not found in database.
					ToastCreator.showShortToast(Login.this,
							"Invalid username/password combination.");
				}
			}

		});

	}

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

				_dbHelper.open();
				_dbHelper.createUser(username, email, password);
				_dbHelper.close();

				ToastCreator.showLongToast(Login.this, "Creation successful!");
				setPreferences(username, email, password, true);
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
		_cursor = _dbHelper.fetchUser(username);
		boolean nameTaken = _cursor.moveToFirst();
		_cursor.close();
		_dbHelper.close();

		return nameTaken;

	}

	private void proceedToHomePage(String user) {
		ToastCreator.showLongToast(this, "Welcome, " + user + "!");

		Intent intent = new Intent(getApplicationContext(), TaskListView.class);
		startActivity(intent);
	}

	private void setPreferences(String user, String email, String password,
			boolean save) {
		Preferences.setUsername(this, user, save);
		Preferences.setPassword(this, email, save);
		Preferences.setEmail(this, password, save);
	}

	/**
	 * from
	 * http://stackoverflow.com/questions/1819142/how-should-i-validate-an-e
	 * -mail-address-on-android
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}
}
