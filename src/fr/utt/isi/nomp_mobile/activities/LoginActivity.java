package fr.utt.isi.nomp_mobile.activities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.tasks.PostRequestTask;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends ActionBarActivity {

	public static final String TAG = "LoginActivity";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private RequestTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(Config.PREF_KEY_USER_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPassword = getIntent().getStringExtra(Config.PREF_KEY_USER_PASSWORD);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		// assign action to login button
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						attemptLogin();
					}

				});

		// assign action to sign up button
		final Intent intentSignUp = new Intent(this, SignUpActivity.class);
		findViewById(R.id.sign_up_button).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(intentSignUp);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);

			// attempt to log the user in remotely
			showProgress(true);

			new PostRequestTask(this, "email", mEmail, "password", mPassword) {

				@Override
				protected void onPostExecute(String response) {
					showProgress(false);

					if (response == null) {
						Toast errorToast = Toast.makeText(getContext(),
								"Failed to login with remote server",
								Toast.LENGTH_LONG);
						errorToast.show();
						return;
					}

					// regex match "Invalid email or password" error
					Pattern pInvalid = Pattern
							.compile("<span>Invalid email or password.</span>");
					Matcher mInvalid = pInvalid.matcher(response);
					if (mInvalid.find()) {
						// notify user that there might be errors
						Toast errorToast = Toast.makeText(getContext(),
								"Invalid email or password", Toast.LENGTH_LONG);
						errorToast.show();
						mEmailView.setError(getContext().getString(
								R.string.error_incorrect_email));
						mPasswordView.setError(getContext().getString(
								R.string.error_incorrect_password));
						return;
					}

					// regex match "My account" link tag returned as HTML
					Pattern pNompId = Pattern
							.compile("<a href=\"/user/([^\"<>]+)\" title=\"My account\">");
					Matcher mNompId = pNompId.matcher(response);
					if (mNompId.find()) {
						String userNompId = mNompId.group(1);
						
						SharedPreferences userInfo = getContext()
								.getSharedPreferences(Config.PREF_NAME_USER,
										Context.MODE_PRIVATE);
						Editor editor = userInfo.edit();
						
						// store user nomp id in shared preferences
						editor.putString(Config.PREF_KEY_USER_NOMP_ID,
								userNompId);
						
						editor.commit();

						// get user profile
						new RequestTask(getContext(), "GET") {

							@Override
							public void onPostExecute(String result) {
								if (result != null
										&& !result
												.equals(RequestTask.MAL_FORMED_URL_EXCEPTION)
										&& !result
												.equals(RequestTask.IO_EXCEPTION)
										&& !result
												.equals(RequestTask.REQUEST_ERROR)) {
									Log.d(TAG, "profile ok\n" + result);

									try {
										// parse response as json object
										JSONObject profileObject = new JSONObject(
												result);

										SharedPreferences userInfo = getContext()
												.getSharedPreferences(
														Config.PREF_NAME_USER,
														Context.MODE_PRIVATE);
										Editor editor = userInfo.edit();

										// store user info in shared preferences
										editor.putString(
												Config.PREF_KEY_USER_ACTOR_TYPE,
												profileObject
														.getString("actor_type"));
										editor.putString(
												Config.PREF_KEY_USER_ACTOR_TYPE_NAME,
												profileObject
														.getString("actor_type_name"));
										editor.putString(
												Config.PREF_KEY_USER_NAME,
												profileObject.getString("name"));
										editor.putString(
												Config.PREF_KEY_USER_USERNAME,
												profileObject
														.getString("username"));
										editor.putString(
												Config.PREF_KEY_USER_EMAIL,
												profileObject
														.getString("email"));

										editor.commit();

										// redirect to account page
										Intent intent = new Intent(
												getContext(),
												AccountActivity.class);
										intent.putExtra(
												Config.PREF_KEY_USER_NAME,
												userInfo.getString(
														Config.PREF_KEY_USER_NAME,
														""));
										getContext().startActivity(intent);
									} catch (JSONException e) {
										Toast errorToast = Toast
												.makeText(
														getContext(),
														"Failed to receive data from server. Please try again later",
														Toast.LENGTH_LONG);
										errorToast.show();
										return;
									}
								} else {
									Toast errorToast = Toast
											.makeText(
													getContext(),
													"Failed to login with remote server",
													Toast.LENGTH_LONG);
									errorToast.show();
									return;
								}
							}

						}.execute(Config.NOMP_API_ROOT + "user/" + userNompId
								+ "/profile");
					}
				}

			}.execute(Config.NOMP_API_ROOT + "user/session");
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}
