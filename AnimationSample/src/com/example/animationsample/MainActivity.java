package com.example.animationsample;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	Button btnTest;
	TextView txtTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnTest = (Button) findViewById(R.id.btnTest);
		txtTarget = (TextView) findViewById(R.id.txtTarget);
		btnTest.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.btnTest: {
			Animation pushAnimation = AnimationUtils.loadAnimation(this,
					R.anim.push_left);
			txtTarget.startAnimation(pushAnimation);
			break;
		}

		}

	}
}
