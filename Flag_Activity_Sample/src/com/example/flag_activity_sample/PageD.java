package com.example.flag_activity_sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PageD extends Activity implements
		android.view.View.OnClickListener {

	@Override
	public void onClick(View v) {
		try {
			// TODO Auto-generated method stub
			
			Intent newPageIntent = new Intent(this, MainActivity.class);
			//will clear A/B/C
			newPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			this.startActivity(newPageIntent);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void setClickListener(int btnID) {
		Button btn = (Button) this.findViewById(btnID);
		btn.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_d);
		this.setClickListener(R.id.button1);
	}

}
