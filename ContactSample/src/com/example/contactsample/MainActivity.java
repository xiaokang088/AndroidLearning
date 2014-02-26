package com.example.contactsample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.RawContacts;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOnClickListener(R.id.btnRead);
		setOnClickListener(R.id.btnWrite);
		setOnClickListener(R.id.btnReadAllContact);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void setOnClickListener(int id) {
		Button btn = (Button) this.findViewById(id);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int vID = v.getId();

		if (vID == R.id.btnRead) {
			readContacts();
		}

		if (vID == R.id.btnWrite) {
			write();
		}
		
		if (vID == R.id.btnReadAllContact) {
			getContacts();
		}
	}

	void read() {
		 
	}

	void write() {

		 
	}
	

	
	List<Integer> getContactids(){	
		ContentResolver cr = this.getContentResolver();
		// content://com.android.contacts/raw_contacts
		Uri rawUri = RawContacts.CONTENT_URI;
		String[] projections = new String[] {
				ContactsContract.RawContacts.CONTACT_ID,
				ContactsContract.Data.DISPLAY_NAME };
		Cursor rawContactCursor = cr.query(rawUri, projections, null, null,
				null);

		rawContactCursor.moveToFirst();
		List<Integer> contactids = new ArrayList<Integer>();
		if (rawContactCursor.getCount() > 0) {
			do {
				int id = rawContactCursor.getInt(0);
				String name = rawContactCursor.getString(1);
				contactids.add(id);
			} while (rawContactCursor.moveToNext());
		}
		rawContactCursor.close();
		return contactids;
	}
	
	
	void getContacts() {
		ContentResolver cr = this.getContentResolver();
		List<Integer> contactids = getContactids();
		List<VCardStruct> VCardStructs = new ArrayList<VCardStruct>();
		VCardHelper helper = new VCardHelper();
		for (int contactID : contactids) {
			VCardStruct contactStruct = helper.getContactSturt(contactID, cr);
			VCardStructs.add(contactStruct);
		}
		VCardHelper.WriteToVCard(VCardStructs);
	}
	
	static String relativePath = "allContact.vcf";
	
	void readContacts(){
		ContentResolver cr = this.getContentResolver();
	 
	/*	String content = FileHelper
				.ReadStringFromExternalStorageFile(relativePath);*/
		String path = Environment.getDataDirectory() +"/" +relativePath;
		String content =FileHelper.ReadStringFromFile(path );
		
		if (content != null) {
			
			String[] arr = content.split("\r\n");
			
			List<VCardStruct> vcardStructList = new ArrayList<VCardStruct>();
			List<String> vcardContent = null;  
			
			for (String str : arr) {
				if (str.startsWith(VCardStruct.startTag)) {
					vcardContent = new ArrayList<String>();
					vcardContent.add(str);
					continue;
				}

				if (str.startsWith(VCardStruct.endTag)) {
					if (vcardContent != null) {
						vcardContent.add(str);						
						VCardStruct struct = new VCardStruct(vcardContent);
						vcardStructList.add(struct);
					}
					continue;
				}

				if (vcardContent != null) {
					vcardContent.add(str);
				}
			}
			
			VCardHelper helper = new VCardHelper();
			List<Integer> contactids = getContactids();
			for (VCardStruct vCardStruct : vcardStructList) {
				if (vCardStruct != null)
					helper.insertContact(vCardStruct, cr);
			}
		 
		} 
	   
	}
}