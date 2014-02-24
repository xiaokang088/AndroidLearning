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

import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.ContactStruct;
import a_vcard.android.syncml.pim.vcard.VCardComposer;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;
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
		// Read from phone to Vcard
		OutputStreamWriter writer = null;
		File SDCardFile = Environment.getExternalStorageDirectory();
		path = SDCardFile + "/a.vcard";
		
		try {
			writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		VCardComposer composer = new VCardComposer();

		// create a contact
		ContactStruct contact1 = new ContactStruct();
		contact1.name = "Neo";
		contact1.company = "The Company";
		contact1.addPhone(Contacts.Phones.TYPE_MOBILE, "+123456789", null, true);
	 
		
		// create vCard representation
		String vcardString;
		try {
			vcardString = composer.createVCard(contact1,
					VCardComposer.VERSION_VCARD30_INT);

			writer.write(vcardString);

			writer.write("\n"); // add empty lines between contacts

			// repeat for other contacts
			// ...

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void write() {

		try {
			VCardParser parser = new VCardParser();
			VDataBuilder builder = new VDataBuilder();
			
			// read whole file to string
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(path), "UTF-8"));

			String vcardString = "";
			String line;
			while ((line = reader.readLine()) != null) {
				vcardString += line + "\n";
			}
			reader.close();

			// parse the string
			boolean parsed = parser.parse(vcardString, "UTF-8", builder);
			if (!parsed) {
				throw new VCardException("Could not parse vCard file: " + path);
			}

			// get all parsed contacts
			List<VNode> pimContacts = builder.vNodeList;

			// do something for all the contacts
			for (VNode contact : pimContacts) {
				ArrayList<PropertyNode> props = contact.propList;

				// contact name - FN property
				String name = null;
				for (PropertyNode prop : props) {
					if ("FN".equals(prop.propName)) {
						name = prop.propValue;
						// we have the name now
						break;
					}
				}

				// similarly for other properties (N, ORG, TEL, etc)
				// ...

				System.out.println("Found contact: " + name);
			}
		} catch (Exception ex) {

		}
	}

	void getContacts() {				
		ContentResolver cr = this.getContentResolver();
		// content://com.android.contacts/raw_contacts
		Uri rawUri = RawContacts.CONTENT_URI;
		String[] projections = new String[] { ContactsContract.RawContacts.CONTACT_ID };
		Cursor rawContactCursor = cr.query(rawUri, projections, null, null, null);
		
		rawContactCursor.moveToFirst();	
		List<Integer> contactids = new ArrayList<Integer>();
		do{
			int id = rawContactCursor.getInt(0);
			contactids.add(id);
		} while(rawContactCursor.moveToNext());
		rawContactCursor.close();
		
		for(int contactID :contactids){			
			ContactStruct contactStruct =  getContactSturt(contactID,cr);
			WriteToVCard(contactStruct);
		}
		 
	}
	
	ContactStruct getContactSturt(int contactID, ContentResolver cr){		
		ContactStruct contactStuct = new ContactStruct();
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
	
	void WriteToVCard(ContactStruct contactStruct){
	 
		File SDCardFile = Environment.getExternalStorageDirectory();
		path = SDCardFile + "/" + contactStruct.name + ".vcf";

		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		VCardComposer composer = new VCardComposer();

		// create vCard representation
		String vcardString;
		try {
			vcardString = composer.createVCard(contactStruct,
					VCardComposer.VERSION_VCARD30_INT);

			writer.write(vcardString);

			writer.write("\n"); // add empty lines between contacts

			// repeat for other contacts
			// ...

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VCardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void parseEmail(ContactStruct contactStuct, Cursor dataCursor) {

		/*
		 * http://developer.android.com/reference/android/provider/ContactsContract
		 * .CommonDataKinds.Email.html data1 String address Email address itself
		 * data2 int Type TYPE_CUSTOM/TYPE_HOME/TYPE_WORK/TYPE_OTHER/TYPE_MOBILE
		 */

		String data1 = this.getString(ContactsContract.RawContacts.Data.DATA1,
				dataCursor);
		int data2 = this.getInt(ContactsContract.RawContacts.Data.DATA2,
				dataCursor);
		contactStuct.addContactmethod(Contacts.KIND_EMAIL, data2, data1, null,
				true);
	}

	void parsePhone(ContactStruct contactStuct, Cursor dataCursor) {
		
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
		contactStuct.addPhone(data2, data1, data1, true);
	}

	void parseName(ContactStruct contactStuct, Cursor dataCursor){
		
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
		contactStuct.name = data1;
		//contactStuct.addContactmethod(Contacts.KIND_EMAIL, type, data, label, isPrimary);
	}

	void parseOrganization(ContactStruct contactStuct, Cursor dataCursor){
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
		contactStuct.company = data1;
		contactStuct.title = data4;
		contactStuct.addOrganization(data2, data1, data4, true);
	}

	void parseWebSite(ContactStruct contactStuct, Cursor dataCursor){
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
		
		//contactStuct.addContactmethod(Contacts., type, data, label, isPrimary)
	}

	void parseAddress(ContactStruct contactStuct, Cursor dataCursor){
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
		contactStuct.addContactmethod(Contacts.KIND_POSTAL, data2, data1, null, true);
		
	}
	
	void parseIm(ContactStruct contactStuct, Cursor dataCursor){
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
		contactStuct.addContactmethod(Contacts.KIND_IM, data2, data1, null,
				true);
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
