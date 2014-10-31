package com.example.contactsample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;

public class VCardHelper {

	// content://com.android.contacts/data
	Uri dataUri = ContactsContract.Data.CONTENT_URI;

	String[] dataProjections = new String[] {
			ContactsContract.RawContacts.CONTACT_ID,
			ContactsContract.RawContacts.Data.MIMETYPE,
			ContactsContract.RawContacts.Data.DATA1,
			ContactsContract.RawContacts.Data.DATA2,
			ContactsContract.RawContacts.Data.DATA3,
			ContactsContract.RawContacts.Data.DATA4,
			ContactsContract.RawContacts.Data.DATA5,
			ContactsContract.RawContacts.Data.DATA6,
			ContactsContract.RawContacts.Data.DATA7,
			ContactsContract.RawContacts.Data.DATA8,
			ContactsContract.RawContacts.Data.DATA9,
			ContactsContract.RawContacts.Data.DATA10,
			ContactsContract.RawContacts.Data.DATA11,
			ContactsContract.RawContacts.Data.DATA12,
			ContactsContract.RawContacts.Data.DATA13,
			ContactsContract.RawContacts.Data.DATA14,
			ContactsContract.RawContacts.Data.DATA15, };
	Context _context;

	public VCardHelper(Context context) {
		_context = context;
	}

	public VCardStruct getContactSturt(int contactID, ContentResolver cr) {
		VCardStruct contactStruct = new VCardStruct();
		contactStruct.setUID(Integer.toString(contactID));
		String selectoin = RawContacts.CONTACT_ID + " == " + contactID;
		Cursor dataCursor = cr.query(dataUri, dataProjections, selectoin, null,
				null);

		int dataCount = dataCursor.getCount();
		if (dataCount > 0) {
			dataCursor.moveToFirst();
			do {
				String mimeType = this.getString("mimetype", dataCursor);

				if (mimeType.equalsIgnoreCase(Email.CONTENT_ITEM_TYPE)) {
					parseEmail(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Phone.CONTENT_ITEM_TYPE)) {
					parsePhone(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(StructuredName.CONTENT_ITEM_TYPE)) {
					parseName(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Nickname.CONTENT_ITEM_TYPE)) {
					parsNickName(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Organization.CONTENT_ITEM_TYPE)) {
					parseOrganization(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Website.CONTENT_ITEM_TYPE)) {
					parseWebSite(contactStruct, dataCursor);
					continue;
				}

				if (mimeType
						.equalsIgnoreCase(StructuredPostal.CONTENT_ITEM_TYPE)) {
					parseAddress(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Im.CONTENT_ITEM_TYPE)) {
					parseIm(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Note.CONTENT_ITEM_TYPE)) {
					parseNote(contactStruct, dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Im.CONTENT_ITEM_TYPE)) {
					parseIm(contactStruct, dataCursor);
					continue;
				}

			} while (dataCursor.moveToNext());
		}
		dataCursor.close();
		if (contactStruct.nickName == null)
			contactStruct.nickName = contactStruct.name;
		return contactStruct;
	}

	public void InsertContact(VCardStruct vcard) {
		try {
			ContentResolver cr = _context.getContentResolver();
			ContentValues values = new ContentValues();
			Uri rawContactUri = cr.insert(RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);

			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			values.put(StructuredName.DISPLAY_NAME, vcard.GetName());
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);

			// phone
			insertPhone(vcard, rawContactId, cr);

			// Email
			insertEmail(vcard, rawContactId, cr);

			// Address
			insertAddress(vcard, rawContactId, cr);

			String note = vcard.GetNote();
			this.insertNote(note, rawContactId, cr);

			// org
			String org = vcard.GetOrg();
			this.insertOrg(org, rawContactId, cr);

			String title = vcard.GetTitle();
			this.insertTitle(title, rawContactId, cr);

			String url = vcard.GetUrl();
			this.insertUrl(url, rawContactId, cr);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void MergeContact(VCardStruct oldCard, VCardStruct newCard) {
		if (newCard == null)
			return;

		/*
		 * String name, nickName; String org; String url; String title; String
		 * uid; String photo; String note; String rev;
		 */
		if (!VCardStruct.CompareString(newCard.name, oldCard.name))
			return;

		if (!VCardStruct.CompareString(newCard.nickName, oldCard.nickName))
			return;
		
		ContentResolver cr = _context.getContentResolver();
		int rawContactId = oldCard.GetRawContactID();
		
		if (!VCardStruct.CompareString(newCard.org, oldCard.org)) {
			// insert org
			//this.insertOrg(newCard.org, rawContactId, cr);
		}

		if (!VCardStruct.CompareString(newCard.url, oldCard.url)) {
			// insert url
			//this.insertOrg(newCard.url, oldCard.GetRawContactID() , cr);
		}

		if (!VCardStruct.CompareString(newCard.title, oldCard.title)) {
			// insert title
			//this.insertOrg(newCard.title, oldCard.GetRawContactID() , cr);
		}

		if (!VCardStruct.CompareString(newCard.note, oldCard.note)) {
			// insert note;
			//this.insertOrg(newCard.note, oldCard.GetRawContactID() , cr);
		}

		HashMap<Integer, String> newAddressTypeMap = newCard.addressTypeMap;

		for (int key : newAddressTypeMap.keySet()) {
			if (oldCard.addressTypeMap.containsKey(key)
					&& VCardStruct.CompareString(
							oldCard.addressTypeMap.get(key),
							newAddressTypeMap.get(key))) {
			} else {
				this.insertAddress(key, newAddressTypeMap.get(key), rawContactId, cr);
			}
		}

		HashMap<Integer, String> newEmailTypeMap = newCard.emailTypeMap;
		for (int key : newEmailTypeMap.keySet()) {
			if (oldCard.emailTypeMap.containsKey(key)
					&& VCardStruct.CompareString(oldCard.emailTypeMap.get(key),
							newEmailTypeMap.get(key))) {
			} else {
				this.insertEmail(key, newEmailTypeMap.get(key), rawContactId, cr);
			}
		}

		HashMap<Integer, String> newTelTypeMap = newCard.telTypeMap;
		for (int key : newTelTypeMap.keySet()) {
			if (oldCard.telTypeMap.containsKey(key)
					&& VCardStruct.CompareString(oldCard.telTypeMap.get(key),
							newTelTypeMap.get(key))) {
			} else {
				this.insertPhone(key, newTelTypeMap.get(key), rawContactId, cr);
			}
		}

	}

	void insertEmail(VCardStruct vcard, long rawContactId, ContentResolver cr) {
		HashMap<Integer, String> emailTypeMap = vcard.GetEmail();
		if (emailTypeMap != null && emailTypeMap.size() > 0) {
			for (int emailtype : emailTypeMap.keySet()) {
				String email = emailTypeMap.get(emailtype);
				insertEmail(emailtype, email, rawContactId, cr);
			}
		}
	}

	void insertEmail(int emailtype, String email, long rawContactId,
			ContentResolver cr) {
		ContentValues values = new ContentValues();
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.DATA, email);
		values.put(Email.TYPE, emailtype);
		cr.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
	}

	void insertPhone(VCardStruct vcard, long rawContactId, ContentResolver cr) {
		HashMap<Integer, String> telTypeMap = vcard.GetTel();
		if (telTypeMap != null && telTypeMap.size() > 0) {
			for (int telType : telTypeMap.keySet()) {
				String tel = telTypeMap.get(telType);
				insertPhone(telType, tel, rawContactId, cr);
			}
		}
	}

	void insertPhone(int telType, String tel, long rawContactId,
			ContentResolver cr) {
		ContentValues values = new ContentValues();
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, tel);
		values.put(Phone.TYPE, telType);
		cr.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
	}

	void insertAddress(VCardStruct vcard, long rawContactId, ContentResolver cr) {
		HashMap<Integer, String> addressTypeMap = vcard.GetAddress();
		if (addressTypeMap != null && addressTypeMap.size() > 0) {
			for (int adrType : addressTypeMap.keySet()) {
				String adr = addressTypeMap.get(adrType);
				insertAddress(adrType, adr, rawContactId, cr);
			}

		}
	}

	void insertAddress(int adrType, String adr, long rawContactId,
			ContentResolver cr) {
		ContentValues values = new ContentValues();
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
		String[] adrArray = adr.split(";");
		if (adrArray != null && adrArray.length >= 7) {
			values.put(StructuredPostal.POBOX, adrArray[0]);
			values.put(StructuredPostal.DATA6, adrArray[1]);
			values.put(StructuredPostal.DATA4, adrArray[2]);
			values.put(StructuredPostal.DATA7, adrArray[3]);
			values.put(StructuredPostal.DATA8, adrArray[4]);
			values.put(StructuredPostal.DATA9, adrArray[5]);
			values.put(StructuredPostal.DATA10, adrArray[6]);
		} else {
			values.put(StructuredPostal.DATA, adr);
		}

		values.put(StructuredPostal.TYPE, adrType);
		cr.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
	}

	void insertNote(String note,long rawContactId, ContentResolver cr) {
		ContentValues values = new ContentValues();
		if (note != null) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE);
			values.put(Note.NOTE, note);
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	void insertTitle(String title,long rawContactId, ContentResolver cr) {
		ContentValues values = new ContentValues();
		if (title != null) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
			values.put(Organization.TITLE, title);
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	void insertUrl(String url,long rawContactId, ContentResolver cr) {
		ContentValues values = new ContentValues();
		if (url != null) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
			values.put(Website.URL, url);
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);
		}
	}

	void insertOrg(String org,long rawContactId, ContentResolver cr) {
		ContentValues values = new ContentValues();
		if (org != null) {
			String[] orgs = org.split(";");
			if (orgs.length >= 2) {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
				if (orgs[0] != "null")
					values.put(Organization.COMPANY, orgs[0]);
				if (orgs[1] != "null")
					values.put(Organization.DEPARTMENT, orgs[1]);
				cr.insert(
						android.provider.ContactsContract.Data.CONTENT_URI,
						values);
			}
		}
	}

	void parseEmail(VCardStruct contactStuct, Cursor dataCursor) {
		/*
		 * http://developer.android.com/reference/android/provider/ContactsContract
		 * .CommonDataKinds.Email.html data1 String address Email address itself
		 * data2 int Type TYPE_CUSTOM/TYPE_HOME/TYPE_WORK/TYPE_OTHER/TYPE_MOBILE
		 */

		String data1 = this.getString(ContactsContract.RawContacts.Data.DATA1,
				dataCursor);
		int data2 = this.getInt(ContactsContract.RawContacts.Data.DATA2,
				dataCursor);
		contactStuct.SetEmail(data2, data1);

	}

	void parsePhone(VCardStruct contactStuct, Cursor dataCursor) {

		/*
		 * http://developer.android.com/reference/android/provider/ContactsContract
		 * .CommonDataKinds.Phone.html data1 String number data2 int Type
		 * TYPE_CUSTOM. Put the actual type in LABEL. TYPE_HOME TYPE_MOBILE
		 * TYPE_WORK TYPE_FAX_WORK TYPE_FAX_HOME TYPE_PAGER TYPE_OTHER
		 * TYPE_CALLBACK TYPE_CAR TYPE_COMPANY_MAIN TYPE_ISDN TYPE_MAIN
		 * TYPE_OTHER_FAX TYPE_RADIO TYPE_TELEX TYPE_TTY_TDD TYPE_WORK_MOBILE
		 * TYPE_WORK_PAGER TYPE_ASSISTANT TYPE_MMS
		 */

		String data1 = this.getString(ContactsContract.RawContacts.Data.DATA1,
				dataCursor);
		int data2 = this.getInt(ContactsContract.RawContacts.Data.DATA2,
				dataCursor);
		contactStuct.SetTel(data2, data1);
	}

	void parseName(VCardStruct contactStuct, Cursor dataCursor) {

		/*
		 * http://developer.android.com/reference/android/provider/ContactsContract
		 * .CommonDataKinds.StructuredName.html
		 */
		String data1 = this.getString(ContactsContract.RawContacts.Data.DATA1,
				dataCursor);
		contactStuct.SetName(data1);
	}

	void parsNickName(VCardStruct contactStuct, Cursor dataCursor) {

		// http://developer.android.com/reference/android/provider/ContactsContract.CommonDataKinds.Nickname.html
		String data1 = this.getString(ContactsContract.RawContacts.Data.DATA1,
				dataCursor);
		contactStuct.SetNickName(data1);
	}

	void parseOrganization(VCardStruct contactStuct, Cursor dataCursor) {
		// String COMPANY DATA1
		// int TYPE DATA2 Allowed values are:
		// TYPE_CUSTOM. Put the actual type in LABEL.
		// TYPE_WORK
		// TYPE_OTHER
		// String LABEL DATA3
		// String TITLE DATA4
		// String DEPARTMENT DATA5
		// String JOB_DESCRIPTION DATA6
		// String SYMBOL DATA7
		// String PHONETIC_NAME DATA8
		// String OFFICE_LOCATION DATA9
		// String PHONETIC_NAME_STYLE DATA10
		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		int data2 = dataCursor.getInt(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA2));
		String data3 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA3));
		String data4 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA4));
		String data5 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA5));
		/*
		 * String data6 = dataCursor.getString(dataCursor
		 * .getColumnIndex(ContactsContract.RawContacts.Data.DATA6)); String
		 * data7 = dataCursor.getString(dataCursor
		 * .getColumnIndex(ContactsContract.RawContacts.Data.DATA7)); String
		 * data8 = dataCursor.getString(dataCursor
		 * .getColumnIndex(ContactsContract.RawContacts.Data.DATA8)); String
		 * data9 = dataCursor.getString(dataCursor
		 * .getColumnIndex(Organization.OFFICE_LOCATION));
		 * 
		 * 
		 * String data10 = this.getString(Organization.PHONETIC_NAME_STYLE,
		 * dataCursor);
		 */ 
		
		contactStuct.setOrg(data1);
		contactStuct.SetTitle(data4);
	}

	void parseWebSite(VCardStruct contactStuct, Cursor dataCursor) {
		// ContactsContract.CommonDataKinds.Website
		// String URL DATA1
		// int TYPE DATA2 Allowed values are:
		// TYPE_CUSTOM. Put the actual type in LABEL.
		// TYPE_HOMEPAGE
		// TYPE_BLOG
		// TYPE_PROFILE
		// TYPE_HOME
		// TYPE_WORK
		// TYPE_FTP
		// TYPE_OTHER
		// String LABEL DATA3

		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		int data2 = dataCursor.getInt(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA2));
		String data3 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA3));

		contactStuct.SetUrl(data1);
	}

	void parseAddress(VCardStruct contactStuct, Cursor dataCursor) {
		/*
		 * String FORMATTED_ADDRESS DATA1 int TYPE DATA2 Allowed values are:
		 * TYPE_CUSTOM. Put the actual type in LABEL. TYPE_HOME TYPE_WORK
		 * TYPE_OTHER String LABEL DATA3 String STREET DATA4 String POBOX DATA5
		 * Post Office Box number String NEIGHBORHOOD DATA6 String CITY DATA7
		 * String REGION DATA8 String POSTCODE DATA9 String COUNTRY DATA10
		 */

		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		int data2 = dataCursor.getInt(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA2));
		String data3 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA3));
		String data4 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA4));
		String data5 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA5));
		String data6 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA6));
		String data7 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA7));
		String data8 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA8));
		String data9 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA9));
		String data10 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA10));
		data4 = checkNull(data4);
		data5 = checkNull(data5);
		data6 = checkNull(data6);
		data7 = checkNull(data7);
		data8 = checkNull(data8);
		data9 = checkNull(data9);
		data10 = checkNull(data10);
		/*
		 * the post office box; the extended address; the street address; the
		 * locality (e.g., city); the region (e.g., state or province); the
		 * postal code; the country name 七个部分组成，如果，其他的一个部分没有，必须用分号分开
		 */

		String address = String.format("%s;%s;%s;%s;%s;%s;%s", data5, data6,
				data4, data7, data8, data9, data10);
		contactStuct.SetAddress(data2, address);
	}

	String checkNull(String data) {
		return data == null ? "" : data;
	}

	void parseIm(VCardStruct contactStuct, Cursor dataCursor) {
		// String DATA DATA1
		// int TYPE DATA2 Allowed values are:
		// TYPE_CUSTOM. Put the actual type in LABEL.
		// TYPE_HOME
		// TYPE_WORK
		// TYPE_OTHER
		// String LABEL DATA3
		// String PROTOCOL DATA5
		// Allowed values:
		//
		// PROTOCOL_CUSTOM. Also provide the actual protocol name as
		// CUSTOM_PROTOCOL.
		// PROTOCOL_AIM
		// PROTOCOL_MSN
		// PROTOCOL_YAHOO
		// PROTOCOL_SKYPE
		// PROTOCOL_QQ
		// PROTOCOL_GOOGLE_TALK
		// PROTOCOL_ICQ
		// PROTOCOL_JABBER
		// PROTOCOL_NETMEETING
		// String CUSTOM_PROTOCOL DATA6

		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		int data2 = dataCursor.getInt(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA2));

	}

	void parseNote(VCardStruct contactStuct, Cursor dataCursor) {
		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		contactStuct.setNote(data1);
	}

	String getString(String clumnName, Cursor dataCursor) {
		String ret = null;
		try {
			ret = dataCursor.getString(dataCursor.getColumnIndex(clumnName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	int getInt(String clumnName, Cursor dataCursor) {
		int ret = 0;
		try {
			ret = dataCursor.getInt(dataCursor.getColumnIndex(clumnName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	static QuotedPrintableCodec codec = new QuotedPrintableCodec();

	public static String Encoding(String str) {
		return codec.encode(str);
	}

	public static String Decoding(String str) {
		return codec.decode(str);
	}

}
