package com.example.receiversample;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.receiversample.R.id;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	final long MS_IN_DAY = 86400000;
	int interDays = 0;
	SeekBar seekBar1;
	TextView textView1;
	CheckBox chkCurrenttime;
	static String TAG = "ReminderReceiver";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		chkCurrenttime = (CheckBox) this.findViewById(R.id.chkCurrenttime);
		seekBar1 = (SeekBar) this.findViewById(R.id.seekBar1);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				interDays = progress;
				textView1.setText("Days:" + interDays);
			}
		});

		setClickListener(R.id.btnNotification);
		setClickListener(R.id.btnRegisterReceiver);
		setClickListener(R.id.btnUnRegister);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void setClickListener(int btnID) {
		Button btn = (Button) this.findViewById(btnID);
		btn.setOnClickListener(this);
	}

	public void onClick(View view) {
		int viewID = view.getId();

		switch (viewID) {
		case R.id.btnRegisterReceiver:

			Date settingTime = new Date();
			if (chkCurrenttime.isChecked()) {

			} else {
				settingTime.setHours(20);
				settingTime.setMinutes(30);
				settingTime.setSeconds(0);
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(settingTime);
			SimpleDateFormat format = new SimpleDateFormat();
			Log.i(TAG, "setTime:" + format.format(settingTime));
			Log.i(TAG, "interDays:" + interDays);
			ReminderReceiver.registerReminder(this.getApplicationContext(),
					cal, interDays * MS_IN_DAY);
			break;
		case R.id.btnUnRegister:
			ReminderReceiver.unRegisterReminder(this);
			break;
		case R.id.btnNotification:
			NotificationHelper.showNotification(this, "tickertext", "title",
					"content");
			break;

		}
	}

}
