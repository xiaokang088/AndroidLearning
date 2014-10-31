package com.example.servicesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
			// ������Ҫ������IntentService��Intent
			Intent intent = new Intent(this, MyIntentService.class);
			startService(intent);
		}

		if (v.getId() == R.id.btnService) {
			// ��������Ҫ������Service��Intent
			Intent intent = new Intent(this, MyService.class);
			startService(intent);
		}
	}
}
