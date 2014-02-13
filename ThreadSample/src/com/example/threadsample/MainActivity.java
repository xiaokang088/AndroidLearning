package com.example.threadsample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

 
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	TextView txtMessage;
	String Tag = "TheadSample";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtMessage = (TextView) this.findViewById(R.id.textProgress);
		Button btnAsyncTask = (Button) this.findViewById(R.id.btnAsyncTask);
		btnAsyncTask.setOnClickListener(this);

		Button btnThread = (Button) this.findViewById(R.id.btnThread);
		btnThread.setOnClickListener(this);
		
		Button btnDelayThread = (Button) this.findViewById(R.id.btnDelayThread);
		btnDelayThread.setOnClickListener(this);
		
		Button btnSynchronized = (Button) this.findViewById(R.id.btnSynchronized);
		btnSynchronized.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				txtMessage.setText("1 is coming");
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void onClick(View arg0) {

		if (arg0.getId() == R.id.btnAsyncTask) {
			Pagetask task = new Pagetask(this);
			String parm = "http://www.baidu.com";
			task.execute(parm);
		}

		if (arg0.getId() == R.id.btnThread) {
			 commonThread();
		}
		
		if (arg0.getId() == R.id.btnDelayThread) {
			Log.i(Tag, "1 seconds before");
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.i(Tag, "1 seconds later");
				} 
				
				
			}, 1000);
		}
		
		if (arg0.getId() == R.id.btnSynchronized) {
			testSynchronized();
		}
	}
	
	
	void commonThread(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i(Tag, "start");
				Message message = new Message();
				message.what = 1;
				mHandler.sendMessage(message);
				
			/*	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				runOnUiThread(new Runnable(){
						
					@Override
					public void run(){
						Toast toast = Toast.makeText(MainActivity.this,"show toast",Toast.LENGTH_LONG);
						toast.setGravity(Gravity.BOTTOM, 0,0);
						toast.show();
					 }
					
				} );
			}
		});
		thread.start();
	}

	void testSynchronized() {
		/*
		 * new Thread(new TestSync(),"first").start(); new Thread(new
		 * TestSync(),"second").start();
		 */

		new Thread(new Runnable() {
			ResourceHelper helper = new ResourceHelper();

			@Override
			public void run() {
				helper.getorder();
				helper.order();
			}	
		},"third").start();
		
		new Thread(new Runnable() {
			ResourceHelper helper = new ResourceHelper();

			@Override
			public void run() {
				helper.getorder();
				// TODO Auto-generated method stub
				helper.order();
			}	
		},"forth").start();
	}

	class Pagetask extends AsyncTask<String, Integer, String> {
		ProgressDialog pdialog;

		public Pagetask(Context context) {

			pdialog = new ProgressDialog(context, 0);

			pdialog.setButton("cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					pdialog.cancel();
				}
			});

			pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});

			pdialog.setCancelable(true);
			pdialog.setMax(100);
			pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pdialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {

			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(arg0[0]);

				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				String s = null;

				if (is != null) {

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buf = new byte[120];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						baos.write(buf, 0, ch);
						count += ch;

						if (length > 0) {
							int progressValue = (int) (count / (float) length) * 100;
							this.publishProgress(progressValue);
						}
						Thread.sleep(100);
					}
					s = new String(baos.toByteArray());
					return s;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			txtMessage.setText(result);
			pdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {

			txtMessage.setText("Start...");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			String msg = " " + values[0];
			Log.i(Tag, msg);
			txtMessage.setText(msg);
			pdialog.setProgress(values[0]);
		}
	}

	class TestSync implements Runnable {

		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();
			Log.i(Tag, "i'm comming: " + threadName );
			
			synchronized (this) {
				for (int i = 0; i < 10; i++) {
					Log.i(Tag, "synchronized loop "+ threadName + " " + Integer.toString(i));
				}
			}
		}
	}
	
	class ResourceHelper{
		
		
		
		public String getorder(){
			String threadName = Thread.currentThread().getName();
			Log.i(Tag, "get order "+ threadName);
			return "this resource";
		}
		
		public synchronized void order(){
			String threadName = Thread.currentThread().getName();
			for (int i = 0; i < 10; i++) {
				Log.i(Tag, "order "+ threadName + " " + Integer.toString(i));
			}
		}
	}

}
