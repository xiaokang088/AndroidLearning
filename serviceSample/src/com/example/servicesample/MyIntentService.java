package com.example.servicesample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class MyIntentService extends IntentService {

	// http://blog.csdn.net/flowingflying/article/details/7616333

	String tag = "MyIntentService";
	private HttpClient client = null;

	public MyIntentService() {
		super("MyIntentService");
	}

	public MyIntentService(String name) {
		super(name);
		Log.i(tag, "name:" + name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		client = new DefaultHttpClient();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	Messenger mesenger ;
	@Override
	protected void onHandleIntent(Intent intent) {

		int result = Activity.RESULT_CANCELED;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			mesenger = (Messenger) bundle.get(Keys.Param_Handle);
			Message msg = Message.obtain();
			msg.arg1 = result;
			try {
				mesenger.send(msg);
			} catch (Exception e) {
				Log.w(getClass().getName(),
						"Exception Message: " + e.toString());
			}
		}

		String url = bundle.getString(Keys.Param_URL);
		String[] arr = bundle.getStringArray(Keys.Param_Files);
		long endTime = System.currentTimeMillis() + 5 * 1000;
		Log.i(tag, "on Start");
		Log.i(tag, "URL:" + url);

		for (String str : arr) {
			Log.i(tag, "Param_File:" + str);
		}

		 
		HttpGet get = new HttpGet(intent.getData().toString());
		try {
			ResponseHandler<byte[]> responseHandler = new ByteArrayResponseHandler();
			byte[] responseBody = client.execute(get, responseHandler);
			File output = new File(Environment.getExternalStorageDirectory(),
					intent.getData().getLastPathSegment());
			if (output.exists()) {
				output.delete();
			}
			FileOutputStream fos = new FileOutputStream(output.getPath());
			fos.write(responseBody);
			fos.close();
		} catch (Exception e) {
			Log.e(getClass().getName(), "Exception : " + e.toString());
			
		} finally {
			client.getConnectionManager().shutdown();
		}
		
		Message msg = Message.obtain();
		msg.arg1 = Activity.RESULT_OK;
		try {
			mesenger.send(msg);
		} catch (Exception e) {
			Log.w(getClass().getName(),
					"Exception Message: " + e.toString());
		}
		Log.i(tag, "on stop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// 检查返回HTTP Response的返回值，如果是3xx-6xx，不是2xx，则说明出错，例如404，Not Found。
	private class ByteArrayResponseHandler implements ResponseHandler<byte[]> {
		public byte[] handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(),
						statusLine.getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();
			if (entity == null)
				return null;
			return EntityUtils.toByteArray(entity);
		}
	}
}
