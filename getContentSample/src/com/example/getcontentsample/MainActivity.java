package com.example.getcontentsample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	String TAG = "SMSTest";
	ImageView imgView;
	TextView txtResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imgView = (ImageView) this.findViewById(R.id.imgShow);

		Button btnGetPictures = (Button) this.findViewById(R.id.btnGetPictures);
		btnGetPictures.setOnClickListener(this);

		Button btnMessage = (Button) this.findViewById(R.id.btnGetMessage);
		btnMessage.setOnClickListener(this);

		Button btnCallLogs = (Button) this.findViewById(R.id.btnCallLogs);
		btnCallLogs.setOnClickListener(this);

		Button btnContacts = (Button) this.findViewById(R.id.btnContacts);
		btnContacts.setOnClickListener(this);

		Button btnInsertMessage = (Button) this
				.findViewById(R.id.btnInsertMessage);
		btnInsertMessage.setOnClickListener(this);

		Button btnInsertContacts = (Button) this
				.findViewById(R.id.btnInsertContacts);
		btnInsertContacts.setOnClickListener(this);

		Button btnInsertCallLogs = (Button) this
				.findViewById(R.id.btnInsertCallLogs);
		btnInsertCallLogs.setOnClickListener(this);

		Button btnUpdateContact = (Button) this
				.findViewById(R.id.btnUpdateContact);
		btnUpdateContact.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view) {
		Context mContext = getApplicationContext();
		ContentResolver cr = mContext.getContentResolver();
		int id = view.getId();

		switch (id) {
		case R.id.btnGetPictures: {
			getImages(cr);
			break;
		}
		case R.id.btnGetMessage: {
			getSms(cr);
			break;
		}
		case R.id.btnContacts: {
			getContacts(cr);
			break;
		}
		case R.id.btnCallLogs: {
			getCallLog(cr);
			break;
		}
		case R.id.btnInsertMessage: {
			insertSms(cr);
			break;
		}
		case R.id.btnInsertContacts: {
			insertContacts(cr);
			break;
		}
		case R.id.btnInsertCallLogs: {
			this.insertCallLog(cr);
			break;
		}
		case R.id.btnUpdateContact: {
			this.updateContacts(cr);
			break;
		}
		}
	}

	void getSms(ContentResolver cr) {
		Uri smsUri = Uri.parse("content://sms");
		String[] showColumns = new String[] { "thread_id", "address", "body",
				"date", "read", "seen" };
		Cursor smsCursor = cr.query(smsUri, null, null, null, null);

		int smsSize = smsCursor.getCount();
		Log.i(TAG, "count:" + smsSize);
		int smsColumns = smsCursor.getColumnCount();
		String[] columnNames = smsCursor.getColumnNames();

		int i = 0;
		for (String columnName : columnNames) {
			Log.i(TAG, "Column Name " + Integer.toString(i) + ":	 "
					+ columnName);
			i++;
		}

		i = 0;
		if (smsCursor.moveToFirst()) {
			do {
				String body = smsCursor.getString(smsCursor
						.getColumnIndex("body"));
				String address = smsCursor.getString(smsCursor
						.getColumnIndex("address"));
				String date = smsCursor.getString(smsCursor
						.getColumnIndex("date"));

				String content = String.format(
						" date:%s  address:%s  body:%s ", date, address, body);
				Log.i(TAG, content);
				i++;
			} while (smsCursor.moveToNext());
		}
	}

	void insertSms(ContentResolver cr) {
		try {
			Uri smsUri = Uri.parse("content://sms");
			ContentValues values = new ContentValues();
			values.put("body", "testSms");
			values.put("address", "15906819533");
			cr.insert(smsUri, values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void getImages(ContentResolver cr) {
		String columns[] = new String[] { MediaStore.Images.ImageColumns.DATA };
		// Uri[] mUri= new
		// Uri[]{MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// MediaStore.Images.Media.INTERNAL_CONTENT_URI }
		Uri mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		// String[] selection= new String[] { MediaInfoNames.DCIM_DIR,
		// MediaInfoNames.PICTURES_DIR, MediaInfoNames.DEFAULT_FS_DIR, }
		Cursor mResult = cr.query(videoUri, columns, null, null, null);

		boolean isNull = mResult == null;

		int size = mResult.getCount();
		Toast.makeText(this, "count:" + Integer.toString(size),
				Toast.LENGTH_SHORT).show();
		String[] columnNames = mResult.getColumnNames();
		String path = null;
		if (mResult.moveToFirst()) {
			do {
				path = mResult.getString(mResult
						.getColumnIndex(MediaStore.Images.ImageColumns.DATA));

				File  file = new File(path);
				long fileLength = file.length();
				double mbs= fileLength / 1024d / 1024d;
				Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
			} while (mResult.moveToNext());
		}

		File file = new File(path);
		Uri imgUri = Uri.fromFile(file);
		// imgView.setImageURI(imgUri);

		Bitmap bmp;
		try {
			bmp = MediaStore.Images.Media.getBitmap(cr, Uri.fromFile(file));
			imgView.setImageBitmap(bmp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void showImage() {

	}

	void downloadImage() {

	}

	void getCallLog(ContentResolver cr) {
		String testTag = "CallLog";
		Uri smsUri = CallLog.Calls.CONTENT_URI;
		Cursor callLogCursor = cr.query(smsUri, null, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);

		int callLogSize = callLogCursor.getCount();
		Log.i(testTag, "count:" + callLogSize);

		int callLogColumns = callLogCursor.getColumnCount();
		String[] columnNames = callLogCursor.getColumnNames();

		int i = 0;
		for (String columnName : columnNames) {
			Log.i(testTag, "Column Name " + Integer.toString(i) + ":	 "
					+ columnName);
			i++;
		}

		i = 0;
		if (callLogCursor.moveToFirst()) {
			do {
				String realnumber = callLogCursor.getString(callLogCursor
						.getColumnIndex("number"));
				String type = callLogCursor.getString(callLogCursor
						.getColumnIndex("type"));
				String date = callLogCursor.getString(callLogCursor
						.getColumnIndex("date"));

				String content = String.format(
						" realnumber:%s  type:%s  date:%s ", realnumber, type,
						date);
				Log.i(testTag, content);
				i++;
			} while (callLogCursor.moveToNext());
		}
	}

	void insertCallLog(ContentResolver cr) {
		try {
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.NUMBER, "15906819565");

			values.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
			values.put(CallLog.Calls.DATE, System.currentTimeMillis());
			values.put(CallLog.Calls.DURATION, "20");
			values.put(CallLog.Calls.NEW, "1");
			cr.insert(CallLog.Calls.CONTENT_URI, values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void getContacts(ContentResolver cr) {

		String testTag = "Contacts";

		// content://com.android.contacts/data
		Uri dataUri = ContactsContract.Data.CONTENT_URI;

		Uri authorityUri = ContactsContract.AUTHORITY_URI;

		// content://com.android.contacts/raw_contacts
		Uri rawUri = RawContacts.CONTENT_URI;

		// String selection = RawContacts.DELETED +" != 1";
		// Cursor contactCursor = cr.query(contactUri, null, selection, null,
		// null);

		Cursor contactCursor = cr.query(rawUri, null, null, null, null);	
		
		int contactSize = contactCursor.getCount();
		Log.i(testTag, "count:" + contactSize);

		int contactColumns = contactCursor.getColumnCount();
		String[] columnNames = contactCursor.getColumnNames();

		int i = 0;
		/*for (String columnName : columnNames) {
			Log.i(testTag, "Column Name " + Integer.toString(i) + ":	 "
					+ columnName);
			i++;
		}*/

		i = 0;
		if (contactCursor.moveToFirst()) {
			do {
				String displayName = contactCursor
						.getString(contactCursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				 
				/*  if (!displayName.startsWith("C")) continue;*/
				 
			 	Log.i(testTag, "displayName:" + displayName); 

				
			/*	for (int j = 0; j < contactColumns; j++) {
					String content = columnNames[j] + " : "
							+ contactCursor.getString(j);
					Log.i(testTag, content);
				}*/

				 int id =contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

				 if (id!=7)
					 continue;
				 
				 String selection = RawContacts.CONTACT_ID +" == " + id;
				 
				Cursor dataCursor = cr.query(dataUri, null, selection, null,
						null);

				String[] dataCollumns = dataCursor.getColumnNames();
				int dataCount = dataCursor.getCount();

				if (dataCount > 0) {
					if (dataCursor.moveToFirst()) {
						Log.i(testTag, "<<<<<<<<<<<<<<<<<<<<<<<<<<");
						do {
						
							for (String dataColumn : dataCollumns) {
								try{
								Log.i(testTag,
										dataColumn
												+ " : "
												+ dataCursor.getString(dataCursor
														.getColumnIndex(dataColumn)));
								} catch (Exception ex){
									ex.printStackTrace();
								}
								
							}
							
						} while (dataCursor.moveToNext());
						Log.i(testTag, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					}
				}
				/*int phoneCount = contactCursor
						.getInt(contactCursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (phoneCount > 0) {
					Cursor phones = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
							null, null);

					if (phones.moveToFirst()) {
						do {
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							Log.i(testTag, "phoneNumber:" + phoneNumber);

						} while (phones.moveToNext());
					}
				}*/

				i++;

				Log.i(testTag, "---------------------------------");

			} while (contactCursor.moveToNext());
		}
	}

	void insertContacts(ContentResolver cr) {
		try {
			ContentValues values = new ContentValues();

			// 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
			Uri rawContactUri = cr.insert(RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			// 设置内容类型
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
			// 设置联系人名字
			values.put(StructuredName.GIVEN_NAME, "testName");
			// 向联系人URI添加联系人名字
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);

			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
			// 设置联系人的电话号码
			values.put(Phone.NUMBER, "15906819533");
			// 设置电话类型
			values.put(Phone.TYPE, Phone.TYPE_MOBILE);
			// 向联系人电话号码URI添加电话号码
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);

			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
			// 设置联系人的Email地址
			values.put(Email.DATA, "xx@x.com");
			// 设置该电子邮件的类型
			values.put(Email.TYPE, Email.TYPE_WORK);
			// 向联系人Email URI添加Email数据
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);

			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
			// 设置联系人的公司地址
			values.put(Organization.DATA, "三大街");
			// 设置该公司地址的类型
			values.put(Organization.TYPE, Organization.TYPE_WORK);
			// 向联系人公司地址 URI添加公司地址数据
			cr.insert(android.provider.ContactsContract.Data.CONTENT_URI,
					values);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void updateContacts(ContentResolver cr) {

		String testTag = "Contacts";
		int ret = -1;
		try {
			Uri contactUri = ContactsContract.Data.CONTENT_URI;
			String selection = Data.DISPLAY_NAME + " like 'C%' ";

			ContentValues values = new ContentValues();
			values.put(Data.IN_VISIBLE_GROUP, "1");

			ret = cr.update(contactUri, values, selection, null);

			String[] arr = new String[] { Data.IN_VISIBLE_GROUP,
					Data.DISPLAY_NAME };

			Cursor retCursor = cr.query(contactUri, arr, selection, null, null);

			int contactSize = retCursor.getCount();
			Log.i(testTag, "count:" + contactSize);

			int contactColumns = retCursor.getColumnCount();
			String[] columnNames = retCursor.getColumnNames();

			retCursor.moveToFirst();

			do {

				int visible = retCursor.getInt(0);
				String displayName = retCursor.getString(1);
				Log.i(testTag, "visible:" + Integer.toString(visible)
						+ " displayName " + displayName);
			} while (retCursor.moveToNext());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.i(testTag, "update result:" + Integer.toString(ret));

	}

	interface Uris {
		static final Uri SMS_ALL = Uri.parse("content://sms");
		static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
		static final Uri SMS_SENT = Uri.parse("content://sms/sent");
		static final Uri SMS_DRAFT = Uri.parse("content://sms/draft");
		static final Uri SMS_OUTBOX = Uri.parse("content://sms/outbox");
		static final Uri SMS_UNDELIVERED = Uri
				.parse("content://sms/undelivered");
		static final Uri SMS_FAILED = Uri.parse("content://sms/failed");
		static final Uri SMS_QUEUED = Uri.parse("content://sms/queued");
		static final Uri SMS_CONVERSATIONS = Uri
				.parse("content://sms/conversations");
		static final Uri SMS_RAW_MESSAGE = Uri.parse("content://sms/raw");
		static final Uri SMS_ATTACHMENT = Uri
				.parse("content://sms/attachments");
		static final Uri SMS_NEW_THREAD_ID = Uri
				.parse("content://sms/threadID");
		static final Uri SMS_STATUS_PENDING = Uri
				.parse("content://sms/sr_pending");
		static final Uri SMS_ALL_ICC = Uri.parse("content://sms/icc");
		static final Uri SMS_CELLBROADCAST = Uri
				.parse("content://cellbroadcasts");
	}

	public interface SmsColumns {

		/*
		 * 12-30 13:44:52.410: I/SMS Test(15737): Column Name 0: _id 12-30
		 * 13:44:54.335: I/SMS Test(15737): Column Name 1: thread_id 12-30
		 * 13:44:54.335: I/SMS Test(15737): Column Name 2: address 12-30
		 * 13:44:54.335: I/SMS Test(15737): Column Name 3: person 12-30
		 * 13:44:54.335: I/SMS Test(15737): Column Name 4: date 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 5: date_sent 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 6: protocol 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 7: read 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 8: status 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 9: type 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 10: reply_path_present
		 * 12-30 13:44:54.340: I/SMS Test(15737): Column Name 11: subject 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 12: body 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 13: service_center 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 14: locked 12-30
		 * 13:44:54.340: I/SMS Test(15737): Column Name 15: error_code 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 16: seen 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 17: deletable 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 18: hidden 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 19: group_id 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 20: group_type 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 21: delivery_date 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 22: app_id 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 23: msg_id 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 24: callback_number
		 * 12-30 13:44:54.345: I/SMS Test(15737): Column Name 25: reserved 12-30
		 * 13:44:54.345: I/SMS Test(15737): Column Name 26: pri 12-30
		 * 13:44:54.350: I/SMS Test(15737): Column Name 27: teleservice_id 12-30
		 * 13:44:54.350: I/SMS Test(15737): Column Name 28: link_url
		 */

		/**
		 * The thread ID of the message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String THREAD_ID = "thread_id";

		/**
		 * The address of the other party
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ADDRESS = "address";

		/**
		 * The person ID of the sender
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String PERSON_ID = "person";

		/**
		 * The date the message was received
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE = "date";

		/**
		 * The date the message was sent
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE_SENT = "date_sent";

		/**
		 * Has the message been read
		 * <P>
		 * Type: INTEGER (boolean)
		 * </P>
		 */
		public static final String READ = "read";

		/**
		 * Indicates whether this message has been seen by the user. The "seen"
		 * flag will be used to figure out whether we need to throw up a
		 * statusbar notification or not.
		 */
		public static final String SEEN = "seen";

		/**
		 * The TP-Status value for the message, or -1 if no status has been
		 * received
		 */
		public static final String STATUS = "status";

		/**
		 * The subject of the message, if present
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SUBJECT = "subject";

		/**
		 * The body of the message
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BODY = "body";

		/**
		 * The id of the sender of the conversation, if present
		 * <P>
		 * Type: INTEGER (reference to item in content://contacts/people)
		 * </P>
		 */
		public static final String PERSON = "person";

		/**
		 * The protocol identifier code
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String PROTOCOL = "protocol";

		/**
		 * Whether the <code>TP-Reply-Path</code> bit was set on this message
		 * <P>
		 * Type: BOOLEAN
		 * </P>
		 */
		public static final String REPLY_PATH_PRESENT = "reply_path_present";

		/**
		 * The service center (SC) through which to send the message, if present
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SERVICE_CENTER = "service_center";

		/**
		 * Has the message been locked?
		 * <P>
		 * Type: INTEGER (boolean)
		 * </P>
		 */
		public static final String LOCKED = "locked";

		/**
		 * Error code associated with sending or receiving this message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String ERROR_CODE = "error_code";

		/**
		 * Meta data used externally.
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String META_DATA = "meta_data";
	}

	public interface callLogColumns {
		/*
		 * 12-30 14:41:34.940: I/CallLog(13376): Column Name 0:
		 * sns_receiver_count 12-30 14:41:34.940: I/CallLog(13376): Column Name
		 * 1: numberlabel 12-30 14:41:34.940: I/CallLog(13376): Column Name 2:
		 * sim_id 12-30 14:41:34.940: I/CallLog(13376): Column Name 3:
		 * service_type 12-30 14:41:34.940: I/CallLog(13376): Column Name 4:
		 * matched_number 12-30 14:41:34.940: I/CallLog(13376): Column Name 5:
		 * type 12-30 14:41:34.940: I/CallLog(13376): Column Name 6: contactid
		 * 12-30 14:41:34.945: I/CallLog(13376): Column Name 7: lookup_uri 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 8: dormant_set 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 9: sdn_alpha_id 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 10: sp_type 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 11: messageid 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 12: fname 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 13: simnum 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 14: lname 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 15: sns_pkey 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 16: account_id 12-30
		 * 14:41:34.945: I/CallLog(13376): Column Name 17: formatted_number
		 * 12-30 14:41:34.945: I/CallLog(13376): Column Name 18:
		 * call_out_duration 12-30 14:41:34.945: I/CallLog(13376): Column Name
		 * 19: number 12-30 14:41:34.945: I/CallLog(13376): Column Name 20:
		 * geocoded_location 12-30 14:41:34.945: I/CallLog(13376): Column Name
		 * 21: account_name 12-30 14:41:34.945: I/CallLog(13376): Column Name
		 * 22: is_read 12-30 14:41:34.945: I/CallLog(13376): Column Name 23:
		 * raw_contact_id 12-30 14:41:34.945: I/CallLog(13376): Column Name 24:
		 * cdnip_number 12-30 14:41:34.945: I/CallLog(13376): Column Name 25:
		 * m_subject 12-30 14:41:34.945: I/CallLog(13376): Column Name 26: date
		 * 12-30 14:41:34.945: I/CallLog(13376): Column Name 27:
		 * real_phone_number 12-30 14:41:34.950: I/CallLog(13376): Column Name
		 * 28: _id 12-30 14:41:34.950: I/CallLog(13376): Column Name 29: sns_tid
		 * 12-30 14:41:34.950: I/CallLog(13376): Column Name 30: name 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 31: normalized_number
		 * 12-30 14:41:34.950: I/CallLog(13376): Column Name 32: photo_id 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 33: logtype 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 34: reject_flag 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 35: m_content 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 36: pinyin_name 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 37: country_code 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 38: frequent 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 39: cityid 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 40: bname 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 41: countryiso 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 42: numbertype 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 43: new 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 44: duration 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 45: cnap_name 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 46: address 12-30
		 * 14:41:34.950: I/CallLog(13376): Column Name 47: remind_me_later_set
		 * 12-30 14:41:34.950: I/CallLog(13376): Column Name 48: e164_number
		 * 12-30 14:41:34.950: I/CallLog(13376): Column Name 49: voicemail_uri
		 */
	}

}
