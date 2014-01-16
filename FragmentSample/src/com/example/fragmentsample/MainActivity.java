package com.example.fragmentsample;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

public class MainActivity extends Activity implements HeaderFragment.ItemAddedListener{
	
	ItemAdapter adapter ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter= new ItemAdapter(this,0);
		 android.app.FragmentManager manager = this.getFragmentManager();
		
		 ItemFragment fragment = (ItemFragment)manager.findFragmentById(R.id.listItemFragment);
	 
		 adapter.add(new ItemInfo("abc"));
		 adapter.add(new ItemInfo("abdc"));
		 adapter.add(new ItemInfo("abddc"));
		 
		 fragment.setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void AddItem(ItemInfo info){
		adapter.add(info);
		adapter.notifyDataSetChanged();
	}
}
