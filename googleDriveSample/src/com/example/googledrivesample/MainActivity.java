package com.example.googledrivesample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import android.os.Bundle;
import android.os.Environment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String TAG = "GoogleSample";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setClick(R.id.btnTest);
		setClick(R.id.btnGetInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void setClick(int btnID) {
		Button btnTest = (Button) this.findViewById(btnID);
		btnTest.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// checkGoogleService();

		int viewID = v.getId();

		if (viewID == R.id.btnTest) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						test();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}

		if (viewID == R.id.btnGetInfo) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						getinfo();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	void checkGoogleService() {

		try {
			int isGoogleServiceAvailable = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(this);

			boolean ret = isGoogleServiceAvailable == com.google.android.gms.common.ConnectionResult.SUCCESS;

			/*
			 * boolean ret = GooglePlayServicesUtil
			 * .isUserRecoverableError(isGoogleServiceAvailable);
			 */

			if (ret) {
				Log.i(TAG, "GoogleService is available");
			} else {
				Log.i(TAG, "GoogleService is unavailable");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String CLIENT_ID = "442264846445.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "qdKLyYNqFt5bNO91VOPBtGc7";
	private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

	int REQUEST_CODE_AUTH = 4;
	GoogleAccountCredential credential;
	String appName ;
	Drive myDrive;

	void test() throws IOException {
		Set<String> rest = Collections.singleton(DriveScopes.DRIVE_FILE);
		credential = GoogleAccountCredential.usingOAuth2(this,rest
				);

		/*SharedPreferences prefs = this.getSharedPreferences("gdrive",
				Context.MODE_PRIVATE);
		credential.setSelectedAccountName(prefs.getString(PREF_ACCOUNT_NAME,
				null));*/

		  appName = this.getString(R.string.app_name);
		myDrive = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).setApplicationName(appName).build();

		Account account = credential.getSelectedAccount();

		if (account == null) {
			this.startActivityForResult(credential.newChooseAccountIntent(),
					REQUEST_CODE_AUTH);
		} else {

			try {
				com.google.api.services.drive.model.About about = myDrive
						.about().get().execute();

				long totalSpace = about.getQuotaBytesTotal();
				long usedSpace = about.getQuotaBytesUsed();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	  private int REQUEST_AUTHORIZATION = 11;

	void getinfo() {
		myDrive = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).setApplicationName(appName)
				.build();
		Account account = credential.getSelectedAccount();
		if (account != null) {
			try {
				  About about = myDrive.about().get().execute();

				long totalSpace = about.getQuotaBytesTotal();
				long usedSpace = about.getQuotaBytesUsed();
			} catch (UserRecoverableAuthIOException e) {
				e.printStackTrace();
				Log.d("Token", "token:" + e.getCause());
				startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	final String PREF_ACCOUNT_NAME = "accountName";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_AUTH) {
			if (resultCode == RESULT_OK) {

				if (data != null && data.getExtras() != null) {
					String accountName = data.getExtras().getString(
							AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						try {
							credential.setSelectedAccountName(accountName);
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						try {
							SharedPreferences prefs = this
									.getSharedPreferences("gdrive",
											Context.MODE_PRIVATE);
							Editor editor = prefs.edit();
							editor.putString(PREF_ACCOUNT_NAME, accountName);
							editor.commit();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	String GetRelativePathInExternalStorage(String relativePath) {
		if (!relativePath.startsWith("/"))
			relativePath = "/" + relativePath;

		String path = null;

		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory()
						.getCanonicalPath() + relativePath;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
}
