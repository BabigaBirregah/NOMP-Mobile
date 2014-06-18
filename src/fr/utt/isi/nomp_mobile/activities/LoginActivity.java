package fr.utt.isi.nomp_mobile.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.R.id;
import fr.utt.isi.nomp_mobile.R.layout;
import fr.utt.isi.nomp_mobile.R.menu;
import fr.utt.isi.nomp_mobile.R.string;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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

			// attempt to log the user in locally
			SharedPreferences userInfo = this.getSharedPreferences(
					Config.PREF_NAME_USER, Context.MODE_PRIVATE);
			if (userInfo.getBoolean(Config.PREF_KEY_USER_IS_LOGGED, false)) {
				if (mEmail != userInfo.getString(Config.PREF_KEY_USER_EMAIL,
						null)) {
					mEmailView
							.setError(getString(R.string.error_incorrect_email));
					mEmailView.requestFocus();
					return;
				} else if (mPassword != userInfo.getString(
						Config.PREF_KEY_USER_PASSWORD, null)) {
					mPasswordView
							.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
					return;
				} else {
					// attempt to log the user in remotely
					showProgress(true);
					try {
						String queryString = "email="
								+ URLEncoder.encode(mEmail, "UTF-8") + "&"
								+ "password="
								+ URLEncoder.encode(mPassword, "UTF-8");

						new RequestTask(this, "POST", queryString) {

							@Override
							public void onPostExecute(String result) {
								showProgress(false);
								// TODO: login action
								// TODO: redirect to user account page
							}

						}.execute(Config.NOMP_API_ROOT + "user/session");
					} catch (UnsupportedEncodingException e) {
						Toast errorToast = Toast
								.makeText(
										this,
										"Your email/password could not be encoded. Please check your input.",
										Toast.LENGTH_LONG);
						errorToast.show();
					}
				}
			}
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
