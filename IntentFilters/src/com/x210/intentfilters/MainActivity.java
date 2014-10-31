package com.x210.intentfilters;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Action;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button actionbutton;
	private Button categorybutton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		actionbutton = (Button) findViewById(R.id.ActionButton);
		categorybutton = (Button) findViewById(R.id.CategoryButton);
		actionbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("myapp.action.test1");
				startActivity(intent);
			}
		});
		categorybutton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("myapp.action.test1");
				intent.addCategory("cate1");
				intent.addCategory("cate2");
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
