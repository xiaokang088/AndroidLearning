package com.example.layoutsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GravityTestActivity extends Activity implements
		android.view.View.OnClickListener {

	TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gravitytest);
		Button btn = (Button) this.findViewById(R.id.btnTestGravity);
		btn.setOnClickListener(this);
		textView1 = (TextView) this.findViewById(R.id.textView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	int[] GravityArr = { Gravity.TOP, Gravity.BOTTOM, Gravity.LEFT,
			Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL };

	int i=0;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		i++;
		if(i>GravityArr.length-1)
			i=0;
		
		int gravity = GravityArr[i];
		textView1.setGravity(gravity);
		textView1.setText(Integer.toString(gravity));
	}

}
