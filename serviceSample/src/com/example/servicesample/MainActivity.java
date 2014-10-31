package com.example.servicesample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnIntentService = (Button) this
				.findViewById(R.id.btnIntentService);
		btnIntentService.setOnClickListener(this);

		Button btnService = (Button) this.findViewById(R.id.btnService);
		btnService.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnIntentService) {
			// 创建需要启动的IntentService的Intent
			Intent intent = new Intent(this, MyIntentService.class);
			intent.setAction("com.example.servicesample.MyIntentService.download");
			// intent.setDataAndType(data, type);
			String[] files = new String[] { "1.png", "2.jpg" };
			intent.putExtra(Keys.Param_Files, files);
			intent.putExtra(Keys.Param_URL, "http://www.baidu.com");
			intent.setData(Uri
					.parse("http://commonsware.com/Android/excerpt.pdf"));
			intent.putExtra(Keys.Param_Handle, new Messenger(handler));
			startService(intent);
		}

		if (v.getId() == R.id.btnService) {
			// 创建所需要启动的Service的Intent
			Intent intent = new Intent(this, MyService.class);
			startService(intent);
		}
	}

	// Handler通过handlerMessage（）接受消息，运行在主线程，用于处理UI等内容。
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.arg1) {
			case Activity.RESULT_OK:
				Toast.makeText(MainActivity.this, "Result : OK ",
						Toast.LENGTH_LONG).show();
				break;
			case Activity.RESULT_CANCELED:
				Toast.makeText(MainActivity.this, "Result : Cancel ",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};
}
