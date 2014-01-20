package com.example.sdfilesample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String TAG = "IOTest";
	String FILE_NAME = "/testLog.txt";
	String content = "test my";
	TextView txtView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setButtonClick(R.id.btnDevelop);
		setButtonClick(R.id.btnLogin2);
		setButtonClick(R.id.btnMaster);
		setButtonClick(R.id.btnView);
		setButtonClick(R.id.btnInputStream);
		setButtonClick(R.id.btnOutputStream);
		
		txtView = (TextView) this.findViewById(R.id.txtView);
	}
	
	void setButtonClick(int id){
		Button btn = (Button) this.findViewById(id);
		btn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	String URL_Develop = "http://develop.seagate-v3-accounts.nero-stage.com/api/v1";
	String URL_Master = "http://master.seagate-v3-accounts.nero-stage.com/api/v1";
	String URL_Login = "https://login2.seagate.com/api/v1";

	String serverPath = "seagate.txt";

	public void onClick(View arg0) {
		int id = arg0.getId();
		boolean result = false;
		switch (id) {
		case R.id.btnDevelop:
			result = FileHelper.WriteInExternalStorageFile(URL_Develop,
					serverPath);
			txtView.setText(URL_Develop);
			break;
		case R.id.btnMaster:
			result = FileHelper.WriteInExternalStorageFile(URL_Master,
					serverPath);
			txtView.setText(URL_Master);
			break;
		case R.id.btnLogin2:
			result = FileHelper.WriteInExternalStorageFile(URL_Login,
					serverPath);
			txtView.setText(URL_Login);
			break;
		case R.id.btnView:
			String content = FileHelper
					.ReadStringFromExternalStorageFile(serverPath);
			result = (content != null);
			if(content != null) {
			txtView.setText(content);
			} else {
				txtView.setText("current is default server");
			}
			break;
		case R.id.btnInputStream:
			String inputContent = ReadFromFile();
			if (inputContent != null) {
				txtView.setText(inputContent);
			} else {
				txtView.setText("current is default server");
			}
			break;
		case R.id.btnOutputStream:
			result= WriteToFile();
			break;
		}
		
		
		String toastStr = null;
		if (result) {
			toastStr = "  success";
		} else {
			toastStr = "  fail";
		}
		Toast toast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}
	
	
	String ReadFromFile(){
		String result = null;
		
		//String path= "abc.txt";
		String path= FileHelper.GetRelativePathInExternalStorage("abc.txt");
				
		try {
			//FileInputStream stream = this.openFileInput(path);
			FileInputStream stream = new FileInputStream(path);
			
			byte[] buffer = new byte[stream.available()];
			stream.read(buffer);
			String content = EncodingUtils.getString(buffer, "UTF-8");
			stream.close();
			Toast toast = Toast.makeText(this, "FileInputStream:" + content, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.show();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally{
			
		}
		
		return result;
	}
	
	
	boolean WriteToFile(){
		
		//String path= "abc.txt";
		String path= FileHelper.GetRelativePathInExternalStorage("abc.txt");
		
		try {
			// append mode
			//FileOutputStream stream = this.openFileOutput(path, Context.MODE_APPEND);
			
			FileOutputStream stream = new FileOutputStream(path,false);
		
			stream.write(("URL:http://baidu.com").getBytes());
			stream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
