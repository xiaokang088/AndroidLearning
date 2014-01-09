package com.example.sdfilesample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String TAG = "IOTest";
	String FILE_NAME = "/testLog.txt";
	String content = "test my";
	TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnWrite = (Button) this.findViewById(R.id.btnWrite);
		btnWrite.setOnClickListener(this);

		Button btnRead = (Button) this.findViewById(R.id.btnRead);
		btnRead.setOnClickListener(this);

		textView1 = (TextView) this.findViewById(R.id.textView1);

		Button btnWriteRoot = (Button) this.findViewById(R.id.btnWriteRoot);
		btnWriteRoot.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {

		int id = arg0.getId();

		switch (id) {
		case R.id.btnRead:
			String result = readLog();
			textView1.setText(result);
			break;
		case R.id.btnWrite:
			writeToLog();
			break;
		case R.id.btnWriteRoot: 
			FileHelper.AppendLineInExternalStorageFile("ABC","abc.txt");
			break;
		}
	}

	void writeToLog() {

		String state = Environment.getExternalStorageState();
		Log.i(TAG, "getExternalStorageState:" + state);
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			Log.i(TAG, "sdCardDir:" + sdCardDir.getPath());

			try {
				File targetFile = new File(sdCardDir.getCanonicalPath()
						+ FILE_NAME);
				Log.i(TAG, "targetFile:" + targetFile.getPath());
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				raf.seek(targetFile.length());

				StringBuilder builder = new StringBuilder();
				builder.append(content + "\n");
				String content = builder.toString();
				raf.writeUTF(content);

				// raf.write(content.getBytes());

				raf.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	String readLog() {

		String result = "";

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			try {
				String path = sdCardDir.getCanonicalPath() + FILE_NAME;
				RandomAccessFile raf = new RandomAccessFile(path, "r");
				StringBuilder buider = new StringBuilder();
				String line = raf.readLine();
				while (line != null) {
					buider.append(line + "\n");
					line = raf.readLine();
				}

				result = buider.toString();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	String readFromLog() {
		String result = "";

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			Log.i(TAG, "sdCardDir" + sdCardDir.getPath());

			try {
				FileInputStream targetFile = new FileInputStream(
						sdCardDir.getCanonicalPath() + FILE_NAME);

				BufferedReader br = new BufferedReader(new InputStreamReader(
						targetFile));

				StringBuilder builder = new StringBuilder("");
				String line = null;

				while ((line = br.readLine()) != null) {
					builder.append(line);
				}
				return builder.toString();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}

}
