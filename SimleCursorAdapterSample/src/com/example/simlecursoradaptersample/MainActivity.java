package com.example.simlecursoradaptersample;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.RawContacts;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity implements android.view.View.OnClickListener{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnHello  = (Button)this.findViewById(R.id.btnHello);
		btnHello.setOnClickListener(this);
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
		ListView listView1 = (ListView) this.findViewById(R.id.listView1);

		try {
			ContentResolver contentResolver = this.getContentResolver();
			Uri rawUri = RawContacts.CONTENT_URI;
			String[] projections = new String[] { ContactsContract.Data.DISPLAY_NAME };
			Cursor myCursor = contentResolver.query(rawUri, null, null, null,
					null);

			int count = myCursor.getCount();
			myCursor.moveToFirst();
			SimpleCursorAdapter myAdapter;
			myAdapter = new SimpleCursorAdapter(this,
					android.R.layout.simple_expandable_list_item_1, myCursor,
					projections, new int[] { android.R.id.text1 });
			listView1.setAdapter(myAdapter);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
