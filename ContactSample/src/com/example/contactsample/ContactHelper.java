package com.example.contactsample;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;

public class ContactHelper {

	Context _context;
	
	public ContactHelper(Context context) {
		_context = context;
	}
	
	
	public List<Integer> GetContactids(){	
		ContentResolver cr = _context.getContentResolver();
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
	
}
