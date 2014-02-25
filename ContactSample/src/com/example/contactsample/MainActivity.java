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
	String[] dataProjections = new String[] { ContactsContract.RawContacts.CONTACT_ID,
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
			ContactsContract.RawContacts.Data.DATA15,
			};
	// content://com.android.contacts/data
	Uri dataUri = ContactsContract.Data.CONTENT_URI;
	
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
			read();
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

	void getContacts() {				
		ContentResolver cr = this.getContentResolver();
		// content://com.android.contacts/raw_contacts
		Uri rawUri = RawContacts.CONTENT_URI;
		String[] projections = new String[] { ContactsContract.RawContacts.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME };
		Cursor rawContactCursor = cr.query(rawUri, projections, null, null, null);
		
		rawContactCursor.moveToFirst();	
		List<Integer> contactids = new ArrayList<Integer>();
		do{
			int id = rawContactCursor.getInt(0);
			String name = rawContactCursor.getString(1);
			if (name.equalsIgnoreCase("gg111"))
				continue;
			contactids.add(id);
		} while(rawContactCursor.moveToNext());
		rawContactCursor.close();
		
		for(int contactID :contactids){			
			VCardStruct contactStruct =  getContactSturt(contactID,cr);
			WriteToVCard(contactStruct);
		}
		 
	}
	
	VCardStruct getContactSturt(int contactID, ContentResolver cr){		
		VCardStruct contactStuct = new VCardStruct();
		String selectoin = RawContacts.CONTACT_ID + " == " + contactID;
		Cursor dataCursor = cr.query(dataUri, dataProjections, selectoin, null,
				null);

		int dataCount = dataCursor.getCount();
		if (dataCount > 0) {
			dataCursor.moveToFirst();
			do {
				String mimeType  = this.getString("mimetype", dataCursor);
				 
				if (mimeType.equalsIgnoreCase(Email.CONTENT_ITEM_TYPE)) {
					parseEmail(contactStuct,dataCursor);
					continue;
				}

				if (mimeType.equalsIgnoreCase(Phone.CONTENT_ITEM_TYPE)) {
					parsePhone(contactStuct, dataCursor);
					continue;
				}
				
				if (mimeType.equalsIgnoreCase(StructuredName.CONTENT_ITEM_TYPE )) {
					parseName(contactStuct,dataCursor);
					continue;
				}
				
				if (mimeType.equalsIgnoreCase(Organization.CONTENT_ITEM_TYPE )) {
					parseOrganization(contactStuct,dataCursor);
					continue;
				}
				
				if (mimeType.equalsIgnoreCase(Website.CONTENT_ITEM_TYPE)){
					parseWebSite(contactStuct,dataCursor);
					continue;
				}
				
				if (mimeType.equalsIgnoreCase(StructuredPostal.CONTENT_ITEM_TYPE)){
					parseAddress(contactStuct,dataCursor);
					continue;
				}
				 
				if (mimeType.equalsIgnoreCase(Im.CONTENT_ITEM_TYPE)){
					parseIm(contactStuct,dataCursor);
					continue;
				}
				
			} while (dataCursor.moveToNext());
		}
		dataCursor.close();
		return contactStuct;
	}
	
	void WriteToVCard(VCardStruct contactStruct){	
		String relativePath = contactStruct.name   + ".vcf";
		String content = contactStruct.ToVCardContent();
		FileHelper.WriteInExternalStorageFile(content, relativePath);
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
		http://developer.android.com/reference/android/provider/ContactsContract.CommonDataKinds.Phone.html
		data1 	String	number		 
		data2	int		Type		TYPE_CUSTOM. Put the actual type in LABEL.
TYPE_HOME
TYPE_MOBILE
TYPE_WORK
TYPE_FAX_WORK
TYPE_FAX_HOME
TYPE_PAGER
TYPE_OTHER
TYPE_CALLBACK
TYPE_CAR
TYPE_COMPANY_MAIN
TYPE_ISDN
TYPE_MAIN
TYPE_OTHER_FAX
TYPE_RADIO
TYPE_TELEX
TYPE_TTY_TDD
TYPE_WORK_MOBILE
TYPE_WORK_PAGER
TYPE_ASSISTANT
TYPE_MMS
		*/
		 
		String data1 =  this.getString(ContactsContract.RawContacts.Data.DATA1, dataCursor);
		int data2 = this.getInt(ContactsContract.RawContacts.Data.DATA2, dataCursor) ;
		contactStuct.SetTel(data2, data1);
	}

	void parseName(VCardStruct contactStuct, Cursor dataCursor){
		
		/*String	DISPLAY_NAME	DATA1	
		String	GIVEN_NAME	DATA2	
		String	FAMILY_NAME	DATA3	
		String	PREFIX	DATA4	Common prefixes in English names are "Mr", "Ms", "Dr" etc.
		String	MIDDLE_NAME	DATA5	
		String	SUFFIX	DATA6	Common suffixes in English names are "Sr", "Jr", "III" etc.
		String	PHONETIC_GIVEN_NAME	DATA7	Used for phonetic spelling of the name, e.g. Pinyin, Katakana, Hiragana
		String	PHONETIC_MIDDLE_NAME	DATA8	
		String	PHONETIC_FAMILY_NAME	DATA9	
		*/
		String data1 = this.getString(ContactsContract.RawContacts.Data.DATA1, dataCursor);
		contactStuct.SetName(data1, null);
	}

	void parseOrganization(VCardStruct contactStuct, Cursor dataCursor){
//		String	COMPANY	DATA1	
//		int	TYPE	DATA2	Allowed values are:
//		TYPE_CUSTOM. Put the actual type in LABEL.
//		TYPE_WORK
//		TYPE_OTHER
//		String	LABEL	DATA3	
//		String	TITLE	DATA4	
//		String	DEPARTMENT	DATA5	
//		String	JOB_DESCRIPTION	DATA6	
//		String	SYMBOL	DATA7	
//		String	PHONETIC_NAME	DATA8	
//		String	OFFICE_LOCATION	DATA9	
//		String	PHONETIC_NAME_STYLE	DATA10	
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
				.getColumnIndex(Organization.OFFICE_LOCATION));
		
		String data10 = this.getString(Organization.PHONETIC_NAME_STYLE,
				dataCursor);
		
		contactStuct.setOrg(data1 + ";" + data5);
		contactStuct.SetTitle(data4);
	}

	void parseWebSite(VCardStruct contactStuct, Cursor dataCursor){
//		String	URL	DATA1	
//		int	TYPE	DATA2	Allowed values are:
//		TYPE_CUSTOM. Put the actual type in LABEL.
//		TYPE_HOMEPAGE
//		TYPE_BLOG
//		TYPE_PROFILE
//		TYPE_HOME
//		TYPE_WORK
//		TYPE_FTP
//		TYPE_OTHER
//		String	LABEL	DATA3
		
		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		int data2 = dataCursor.getInt(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA2));
		String data3 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA3));
		
		contactStuct.SetUrl(data1); 
	}

	void parseAddress(VCardStruct contactStuct, Cursor dataCursor){
		/*String	FORMATTED_ADDRESS	DATA1	
		int	TYPE	DATA2	Allowed values are:
		TYPE_CUSTOM. Put the actual type in LABEL.
		TYPE_HOME
		TYPE_WORK
		TYPE_OTHER
		String	LABEL	DATA3	
		String	STREET	DATA4	
		String	POBOX	DATA5	Post Office Box number
		String	NEIGHBORHOOD	DATA6	
		String	CITY	DATA7	
		String	REGION	DATA8	
		String	POSTCODE	DATA9	
		String	COUNTRY	DATA10*/
		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		int data2 = dataCursor.getInt(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA2));
		contactStuct.SetAddress(data2, data1);
	}
	
	void parseIm(VCardStruct contactStuct, Cursor dataCursor){
//		String	DATA	DATA1	
//		int	TYPE	DATA2	Allowed values are:
//		TYPE_CUSTOM. Put the actual type in LABEL.
//		TYPE_HOME
//		TYPE_WORK
//		TYPE_OTHER
//		String	LABEL	DATA3	
//		String	PROTOCOL	DATA5	
//		Allowed values:
//
//		PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
//		PROTOCOL_AIM
//		PROTOCOL_MSN
//		PROTOCOL_YAHOO
//		PROTOCOL_SKYPE
//		PROTOCOL_QQ
//		PROTOCOL_GOOGLE_TALK
//		PROTOCOL_ICQ
//		PROTOCOL_JABBER
//		PROTOCOL_NETMEETING
//		String	CUSTOM_PROTOCOL	DATA6	
		
		String data1 = dataCursor.getString(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA1));
		int data2 = dataCursor.getInt(dataCursor
				.getColumnIndex(ContactsContract.RawContacts.Data.DATA2));
	 
	}
	
	String getString(String clumnName, Cursor dataCursor) {
		String ret = null;
		try {
			ret = dataCursor.getString(dataCursor
					.getColumnIndex(clumnName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
	
	int getInt(String clumnName, Cursor dataCursor) {
		int ret = 0;
		try {
			ret = dataCursor.getInt(dataCursor
					.getColumnIndex(clumnName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}
}
