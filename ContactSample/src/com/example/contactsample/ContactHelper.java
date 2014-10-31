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
	// content://com.android.contacts/raw_contacts
	Uri rawUri = RawContacts.CONTENT_URI;
	String[] projections = new String[] {
			ContactsContract.RawContacts.CONTACT_ID,
			ContactsContract.Data.DISPLAY_NAME };
	ContentResolver cr;
	VCardHelper _vCardHelper;

	public ContactHelper(Context context) {
		_context = context;
		cr = _context.getContentResolver();
		_vCardHelper = new VCardHelper(context);
	}

	public int GetCount() {	
		List<Integer> contactids = GetContactids();
		return contactids == null ? 0 : contactids.size();
	}
	
	public void InsertContact(VCardStruct card){
		_vCardHelper.InsertContact(card);
	}

	public void MergeContact(VCardStruct oldCard, VCardStruct newCard) {
		if (oldCard == null || newCard == null)
			return;
		_vCardHelper.MergeContact(oldCard,newCard); 
	}
	
	public VCardStruct GetVCardStruct(int contactID){
		VCardStruct contactStruct = _vCardHelper.getContactSturt(contactID,
				cr);
		return  contactStruct;
	}
	
	public List<Integer> GetContactids() {
		Cursor rawContactCursor = cr.query(rawUri, projections, null, null,
				null);

		rawContactCursor.moveToFirst();
		List<Integer> contactids = new ArrayList<Integer>();
		if (rawContactCursor.getCount() > 0) {
			do {
				int id = rawContactCursor.getInt(0);
				contactids.add(id);
			} while (rawContactCursor.moveToNext());
		}
		rawContactCursor.close();
		return contactids;
	}

	public List<VCardStruct> ReadFromVCard(String path) {
		List<VCardStruct> vcardStructList = null;

		String content = FileUtil.ReadStringFromFile(path);

		if (content != null) {

			String[] arr = content.split("\n");

			vcardStructList = new ArrayList<VCardStruct>();
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
						if (struct != null && struct.name != null)
							vcardStructList.add(struct);
					}
					continue;
				}

				if (vcardContent != null) {
					vcardContent.add(str);
				}
			}
		}
		return vcardStructList;
	}

}
