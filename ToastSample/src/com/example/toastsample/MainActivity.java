package com.example.toastsample;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnToast = (Button) this.findViewById(R.id.btnToast);
		btnToast.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {

		int id = view.getId();

		switch (id) {
		case R.id.btnToast:
			Toast toa = Toast.makeText(this, "test toast", Toast.LENGTH_LONG);
			toa.setGravity(Gravity.BOTTOM, 0, 0);
			toa.show();
			break;

		}
	}
}
