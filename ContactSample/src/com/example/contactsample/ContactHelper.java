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
		List<VCardStruct> phoneCards = GetAllContacts();
		return phoneCards == null ? 0 : phoneCards.size();
	}

	public void WriteToPhone(String path) {
		List<VCardStruct> cards = readFromVCard(path);
		List<VCardStruct> phoneCards = GetAllContacts();

		for (VCardStruct card : cards) {

			boolean isExist = false;
			for (VCardStruct phoneCard : phoneCards) {

				if (card.name != null && phoneCard.name != null
						&& phoneCard.name.equalsIgnoreCase(card.name)) {
					if (phoneCard.IsEqual(card)) {
						isExist = true;
						break;
					}
				} else {
					continue;
				}
			}

			if (isExist) {
				continue;
			} else {
				_vCardHelper.insertContact(card);
			}
		}
	}

	public List<VCardStruct> GetAllContacts() {
		List<Integer> contactids = getContactids();
		List<VCardStruct> VCardStructs = new ArrayList<VCardStruct>();
		for (int contactID : contactids) {
			VCardStruct contactStruct = _vCardHelper.getContactSturt(contactID,
					cr);
			if ((contactStruct.name == null || contactStruct.name.length() == 0)) {
				// invalid data
				continue;
			}
			VCardStructs.add(contactStruct);
		}
		return VCardStructs;
	}
	
	List<Integer> getContactids() {
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



	List<VCardStruct> readFromVCard(String path) {
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
