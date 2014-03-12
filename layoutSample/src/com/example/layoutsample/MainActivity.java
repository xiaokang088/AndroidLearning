package com.example.layoutsample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn = (Button) this.findViewById(R.id.btnTestCreateUIByCode);
		btn.setOnClickListener(this);
		btn = (Button) this.findViewById(R.id.btnTestGravity);
		btn.setOnClickListener(this);
		btn = (Button) this.findViewById(R.id.btnRightToLeft);
		btn.setOnClickListener(this);
		btn = (Button) this.findViewById(R.id.btnListviewTest);
		btn.setOnClickListener(this);
		btn = (Button) this.findViewById(R.id.btnGridviewTest);
		btn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		 * Intent intent = new Intent(this, GravityTestActivity.class);
		 * this.startActivity(intent);
		 */

		if (v.getId() == R.id.btnTestCreateUIByCode) {
			Intent newIntent = new Intent(this, CreateUIByCode.class);
			this.startActivity(newIntent);
		}

		if (v.getId() == R.id.btnTestGravity) {
			Intent newIntent = new Intent(this, GravityTestActivity.class);
			this.startActivity(newIntent);
		}

		if (v.getId() == R.id.btnRightToLeft) {
			Intent newIntent = new Intent(this, layoutDirection.class);
			this.startActivity(newIntent);
		}
		
		if (v.getId() == R.id.btnLinearLayout) {
			Intent newIntent = new Intent(this, linearlayoutsample.class);
			this.startActivity(newIntent);
		}
		
		if (v.getId() == R.id.btnListviewTest) {
			Intent newIntent = new Intent(this, ListViewLoader.class);
			this.startActivity(newIntent);
		}
		
		if (v.getId() == R.id.btnGridviewTest) {
			Intent newIntent = new Intent(this, HelloGridView.class);
			this.startActivity(newIntent);
		}
	}

}
