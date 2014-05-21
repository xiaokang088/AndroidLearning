package com.example.inter_operation_a;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends  Activity implements
		android.view.View.OnClickListener {

	// http://hmkcode.com/android-start-another-activity-of-another-application/
	TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btn = (Button) this.findViewById(R.id.btnB);
		btn.setOnClickListener(this);

		Button btnDashboard = (Button) this.findViewById(R.id.btnDashboard);
		btnDashboard.setOnClickListener(this);

		Button btnDashboardlog = (Button) this
				.findViewById(R.id.btnDashboardlog);
		btnDashboardlog.setOnClickListener(this);

		Button btnBTest = (Button) this.findViewById(R.id.btnBTest);
		btnBTest.setOnClickListener(this);

		tvResult = (TextView) this.findViewById(R.id.tvResult);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.btnB) {
			// TODO Auto-generated method stub
			Intent LaunchIntent = getPackageManager()
					.getLaunchIntentForPackage("com.example.inter_operation_b");
			this.startActivity(LaunchIntent);
		}

		if (id == R.id.btnDashboard) {
			// TODO Auto-generated method stub
			Intent LaunchIntent = getPackageManager()
					.getLaunchIntentForPackage("com.seagate.android.dashboard");
			startActivity(LaunchIntent);
		}

		if (id == R.id.btnDashboardlog) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setComponent(new ComponentName(
					"com.seagate.android.dashboard",
					"com.seagate.android.dashboard.activity.MainActivity"));
			startActivity(intent);
		}

		if (id == R.id.btnBTest) {
			Intent pickContactIntent = new Intent(
					"com.example.inter_operation_b.TestActivity");
			pickContactIntent.putExtra("IsColor", true);
			pickContactIntent.putExtra("Color", "Blue");

			this.startActivityForResult(pickContactIntent, 200);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Bundle dataBundle = data.getExtras();
		boolean isGetColor = dataBundle.getBoolean("IsGetColor");
		String resultValue = dataBundle.getString("ReturnColor");

		String str = "RequestCode:" + requestCode + "	resultCode:" + resultCode;
		str += "isGetColor:" + isGetColor + "		ReturnColor:" + resultValue;
		tvResult.setText(str);
		super.onActivityResult(requestCode, resultCode, data);
	}

}
