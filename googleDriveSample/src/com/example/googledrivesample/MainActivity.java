package com.example.googledrivesample;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import com.example.googledrivesample.R.id;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Children.List;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.DriveScopes;

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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String TAG = "GoogleSample";

	int REQUEST_CODE_AUTH = 4;
	GoogleAccountCredential credential;
	String appName;
	Drive myDrive;
	private int REQUEST_AUTHORIZATION = 11;
	final String PREF_ACCOUNT_NAME = "accountName";
	TextView tvShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setClick(R.id.btnCheckGoogleService);
		setClick(R.id.btnInitOrSignIn);
		setClick(R.id.btnGetInfo);
		setClick(R.id.btnGetAllFiles);
		setClick(R.id.btnCreateFolders);
		setClick(R.id.btnUploadFile);
		setClick(R.id.btnCreateEmptyFile);
		setClick(R.id.btnDeleteFile);
		setClick(R.id.btnDownLoadFile);
		tvShow = (TextView) this.findViewById(R.id.tvShow);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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

		switch (viewID) {
		case R.id.btnCheckGoogleService:
			this.checkGoogleService();
			break;
		case R.id.btnGetInfo:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						getinfo();
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.btnInitOrSignIn:
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						initOrSignIn();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.btnGetAllFiles: {
			new Thread(new Runnable() {
				@Override
				public void run() {
					getAllFiles();
				}
			}).start();
			break;
		}
		case R.id.btnLogout: {
			new Thread(new Runnable() {
				@Override
				public void run() {
					logout();
				}
			}).start();
			break;
		}
		case R.id.btnCreateFolders: {
			new Thread(new Runnable() {
				@Override
				public void run() {
					CreateFolder();
				}
			}).start();
			break;
		}
		case R.id.btnUploadFile: {
			new Thread(new Runnable() {
				@Override
				public void run() {
					UploadFile();
				}
			}).start();
			break;
		}
		case R.id.btnCreateEmptyFile:{
			new Thread(new Runnable() {
				@Override
				public void run() {
					CreateFile();
				}
			}).start();
			
			break;
		}
		case R.id.btnDeleteFile:{
			new Thread(new Runnable() {
				@Override
				public void run() {
					DeleteFile();
				}
			}).start();
			
			break;
		}
		case R.id.btnDownLoadFile:{
			new Thread(new Runnable() {
				@Override
				public void run() {
					DownloadFile();
				}
			}).start();
			break;
		}
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
				tvShow.setText("GoogleService is available  ");
				Log.i(TAG, "GoogleService is available");
			} else {
				tvShow.setText("GoogleService is unavailable  ");
				Log.i(TAG, "GoogleService is unavailable");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void initOrSignIn() throws IOException {
		Set<String> rest = Collections.singleton(DriveScopes.DRIVE_FILE);
		credential = GoogleAccountCredential.usingOAuth2(this, rest);

		SharedPreferences prefs = this.getSharedPreferences("gdrive",
				Context.MODE_PRIVATE);
		String accountName = prefs.getString(PREF_ACCOUNT_NAME, null);

		credential.setSelectedAccountName(accountName);

		appName = this.getString(R.string.app_name);
		myDrive = new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).setApplicationName(appName)
				.build();

		Account account = credential.getSelectedAccount();

		if (account == null) {
			showInfoOnUIThread("start login");
			this.startActivityForResult(credential.newChooseAccountIntent(),
					REQUEST_CODE_AUTH);
		} else {
			showInfoOnUIThread("Has been login:" + accountName);
		}
	}

	void getinfo() {
		showInfoOnUIThread("Try to get info");

		Account account = credential.getSelectedAccount();
		if (account != null) {
			try {
				About about = myDrive.about().get().execute();

				final long totalSpace = about.getQuotaBytesTotal();
				final long usedSpace = about.getQuotaBytesUsed();

				showInfoOnUIThread("Got info :"
						+ String.format("totalSpace:%s usedSpace:%s",
								totalSpace, usedSpace));

			} catch (UserRecoverableAuthIOException e) {
				e.printStackTrace();
				Log.d(TAG, "Fail" + e.getCause());
				startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
			} catch (Exception ex) {
				Log.d(TAG, "Fail" + ex.getCause());
				ex.printStackTrace();
			}
		} else {
			showInfoOnUIThread("Not login, please login firstly");
		}
	}

	public static final String MIME_FOLDER = "application/vnd.google-apps.folder";
	public static final String MIME_STREAM = "application/octet-stream";

	void getAllFiles() {
		try {
			String rootFolderID = myDrive.about().get().execute()
					.getRootFolderId();

			Log.i(TAG, "rootFolderID:" + rootFolderID);
			File file = new File();
			file.setId(rootFolderID).setMimeType(MIME_FOLDER);
			FindChild(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void FindChild(File file) throws IOException {
		Log.i(TAG, "findChild for :" + file.getTitle());
		try {
			String fileID = file.getId();
			List childrenList = myDrive.children().list(fileID);
			String sql = "trashed=false ";
			childrenList.setQ(sql);
			ChildList childs = childrenList.execute();

			if (childs != null) {
				java.util.List<ChildReference> childItems = childs.getItems();
				if (childItems.size() > 0) {
					for (ChildReference child : childItems) {
						String fileId = child.getId();
						File tempFile = myDrive.files().get(fileId).execute();
						String fileName = tempFile.getTitle();
						Log.i(TAG, "child   id :" + fileId);
						if (tempFile.getMimeType()
								.equalsIgnoreCase(MIME_FOLDER)) {
							Log.i(TAG, "child folder name :" + fileName);
							FindChild(tempFile);
						} else {
							Log.i(TAG, "child file name :" + fileName);
						}
					}
				} else {
					Log.i(TAG, "no child for :" + file.getTitle());
				}
			} else {
				Log.i(TAG, "no child for :" + file.getTitle());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

	}

	void CreateFolder() {
		String rootFolderID;
		try {
			rootFolderID = myDrive.about().get().execute().getRootFolderId();

			File newFolder = new File();
			newFolder.setTitle("ABC");
			newFolder.setMimeType(MIME_FOLDER);
			newFolder.setParents(Arrays.asList(new ParentReference()
					.setId(rootFolderID)));

			File file = myDrive.files().insert(newFolder).execute();

			String id = file.getId();
			String title = file.getTitle();
			String downloadURL = file.getDownloadUrl();

			Log.i(TAG, String.format("id:%s title:%s downloadURL:%s", id,
					title, downloadURL));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void CreateFile(){
		try {
			String parentID = "0B-QETmR0ODN3YmZMTmhmbG9vX3M";
			File newFile = new File();
			newFile.setTitle("empty.txt");
			newFile.setParents(Arrays.asList(new ParentReference().setId(parentID)));
			File resultFile = myDrive.files().insert(newFile).execute();
			String id= resultFile.getId();
			String name = resultFile.getTitle();
			Log.i(TAG, String.format("new file id:%s name:%s", id,name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void DeleteFile(){
		
		String folderID = "0B-QETmR0ODN3YmZMTmhmbG9vX3M";
		String fileID = "0B-QETmR0ODN3bXdUelVHS2V5Y1U";
		
		try {
			myDrive.files().delete(folderID).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void UploadFile() {

		try {
			String rootFolderID = myDrive.about().get().execute()
					.getRootFolderId();
			File newFile = new File();
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = format.format(date) + "test.jpg";
			newFile.setTitle(fileName);
			newFile.setParents(Arrays.asList(new ParentReference()
					.setId(rootFolderID)));

			String path = "/storage/sdcard0/DCIM/Camera/IMG_20100101_080327.jpg";
			java.io.File sourceFile = new java.io.File(path);
			boolean isExist = sourceFile.exists();
			if (!isExist)
				return;
			long mDefaultLength = sourceFile.length();
			String mimeType = getMimeType(path);
			GDriveOutputStream targetStream = new GDriveOutputStream(myDrive,
					newFile, mDefaultLength, mimeType);

			InputStream sourceStream = new FileInputStream(sourceFile);
			int BUFFER_SIZE = 40960;
			byte[] b = new byte[BUFFER_SIZE];
			int s = 0;
			while (0 <= (s = sourceStream.read(b))) {
				targetStream.write(b, 0, s);
			}
			sourceStream.close();
			targetStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void DownloadFile() {
		String downloadID = "0B-QETmR0ODN3Vmt6cm1hV2JBZ28";
		try {
			 
			File downloadFile =  myDrive.files().get(downloadID).execute();
			String downloadURL = downloadFile.getDownloadUrl();

			HttpResponse resp = myDrive.getRequestFactory()
					.buildGetRequest(new GenericUrl(downloadURL)).execute();
			
			ManagedInputStream sourceStream =new  ManagedInputStream(resp.getContent());
			
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = format.format(date) + "test.jpg";
			
			String path = "/storage/sdcard0/DCIM/Camera/"+fileName;
			java.io.File targetFile = new java.io.File(path);
			if (targetFile.exists()) return ;
			
			 
			FileOutputStream targetStream = new FileOutputStream(targetFile);
			int BUFFER_SIZE = 40960;
			byte[] b = new byte[BUFFER_SIZE];
			int s = 0;
			while (0 <= (s = sourceStream.read(b))) {
				targetStream.write(b, 0, s);
			}
			sourceStream.close();
			targetStream.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	String getMimeType(String url) {
		if (url == null || url.lastIndexOf(".") < 0 || url.length() == 0)
			return null;

		String type = null;
		String extension = url.substring(url.lastIndexOf(".") + 1); // MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			extension = extension.toLowerCase(Locale.getDefault());
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

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
							tvShow.setText("login in success accountName:"
									+ accountName);
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

	void showInfoOnUIThread(final String text) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tvShow.setText(text);
			}
		});
	}

	void logout() {
		try {
			credential.setSelectedAccountName(null);
			tvShow.setText("log out");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			SharedPreferences prefs = this.getSharedPreferences("gdrive",
					Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(PREF_ACCOUNT_NAME, null);
			editor.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
