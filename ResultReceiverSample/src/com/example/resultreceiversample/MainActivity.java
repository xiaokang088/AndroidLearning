package com.example.resultreceiversample;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	private DownloadResultReceiver receiver = new DownloadResultReceiver(null);
	int DownloadStartCode = 0;
	TextView textShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Button btnTest = (Button) this.findViewById(R.id.btnTest);
		btnTest.setOnClickListener(this);

		textShow = (TextView) this.findViewById(R.id.textView1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View arg0) {
		Bundle data = new Bundle();
		data.putString("name", "ABC");
		receiver.send(DownloadStartCode, data);
	}

	private class DownloadResultReceiver extends android.os.ResultReceiver {

		public DownloadResultReceiver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);

			String result = resultData.getString("name");
			textShow.setText(result);
			Log.i("ResultReceiver", result);

		}

	}
}
