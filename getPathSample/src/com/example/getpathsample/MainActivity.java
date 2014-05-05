package com.example.getpathsample;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	String TAG = "getPathSample";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TestPath();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	void TestPath() {
		
		File cacheDir =  this.getCacheDir();
		
		writeLog(cacheDir,"getCacheDir");
		
		File externalDir =  this.getExternalCacheDir();
		writeLog(externalDir,"getExternalCacheDir");
		
		SimpleDateFormat logfileNameFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		Date currentDate = new Date(System.currentTimeMillis());
		String dateFileName = logfileNameFormat.format(currentDate);

		Date date = null;
		try {
			date = logfileNameFormat.parse(dateFileName);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int day = date.getDay();

		File dataFile = Environment.getDataDirectory();
		//writeLog(dataFile, "getDataDirectory");

		File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
		//writeLog(downloadCacheDirectory, "getDownloadCacheDirectory");
		
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
		writeLog(externalStorageDirectory, "getExternalStorageDirectory");
		
		File getExternalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		//writeLog(getExternalStoragePublicDirectory, "getExternalStoragePublicDirectory");
		
		File getRootDirectory = Environment.getRootDirectory();
		//writeLog(getRootDirectory, "getRootDirectory");
		
		String getExternalStorageState = Environment.getExternalStorageState();
		Log.i(TAG,"getExternalStorageState:"+ getExternalStorageState);
	 
		
		
	}
	
	void writeLog(File file,String type){		 
		Log.i(TAG,
				type + " getAbsolutePath:"
						+ file.getAbsolutePath());
		try {
			Log.i(TAG,
					type + " getCanonicalPath:"
							+ file.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, 	type + " getPath:" + file.getPath());
		Log.i(TAG, 	type + " exists:" + file.exists());
		
		String logFolderPath = file.getAbsolutePath() + File.separator + "TestPath";
		File logFolder = new File(logFolderPath);
		boolean isFolderExists = logFolder.exists();
		if (!isFolderExists) {
			logFolder.mkdir();
		}
		
		String logPath = logFolderPath + File.separator + "a.log";
		File logFile = new File(logPath);
		boolean isExists = logFile.exists();
		if (!isExists) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
		
	}
}
