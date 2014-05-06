package com.example.dialogsample;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button1 = (Button)this.findViewById(R.id.button1);
		button1.setOnClickListener(this);
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
		int id= v.getId();
		
		switch (id){
		case R.id.button1:
			showDialog();
			break;
		}
	}
	
	void showDialog(){
		
		Dialog dialog = new Dialog(this,R.style.CustomDialogTheme);
		//View v = LayoutInflater.from(this).inflate(R.layout.dialog_test,null);
		//dialog.setContentView(v);
		dialog.setContentView(R.layout.dialog_test);
		dialog.show();
	}
	

}
