package com.example.adapterexample;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

	PlanAdapter adpater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		adpater = new PlanAdapter(this, 0);
		
		Date testDate = new Date(System.currentTimeMillis());
		adpater.add(new PlanItem("phone","das",testDate));
		
		ListView view = (ListView)this.findViewById(R.id.listView1);
		view.setAdapter(adpater);
		
		Button btnAdd = (Button)this.findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(View arg0){
		
		Date testDate = new Date(System.currentTimeMillis());
		adpater.add(new PlanItem("phone","das",testDate));
		adpater.notifyDataSetChanged();
	}

}
