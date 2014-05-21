package com.example.inter_operation_b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity implements android.view.View.OnClickListener{

	TextView tvShow;
	Button btnOK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		tvShow = (TextView) this.findViewById(R.id.tvShow);
		btnOK = (Button)this.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(this);
		
		try {
			Intent intent = this.getIntent();
			boolean isColor = intent.getBooleanExtra("IsColor", false);
			String color = intent.getStringExtra("Color");
			tvShow.setText("IsColor:" + isColor + " color: " + color);
		} catch (Exception ex) {
			tvShow.setText(ex.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		try {
			Intent intent = this.getIntent();
			boolean isColor = intent.getBooleanExtra("IsColor", false);
			String color = intent.getStringExtra("Color");
			tvShow.setText("IsColor:" + isColor + " color: " + color);
		} catch (Exception ex) {
			tvShow.setText(ex.toString());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
 
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra("IsGetColor", true);
		resultIntent.putExtra("ReturnColor", "black");
		this.setResult(100,resultIntent);
		this.finish();
	}
}
