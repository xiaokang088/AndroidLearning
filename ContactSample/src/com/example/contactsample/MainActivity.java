package com.example.contactsample;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

@SuppressWarnings("unused")
public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	ContactHelper _contactHelper;
	VCardHelper _vCardHelper;
	static String relativePath = "allContact.vcf";
	String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOnClickListener(R.id.btnBackup);
		setOnClickListener(R.id.btnTest);
		setOnClickListener(R.id.btnRestore);
		_contactHelper = new ContactHelper(this);
		_vCardHelper = new VCardHelper(this);
		path = Environment.getExternalStorageDirectory() + "/" + relativePath;
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

		if (vID == R.id.btnBackup) {
			backupContacts();
		}

		if (vID == R.id.btnTest) {
			test();
		}

		if (vID == R.id.btnRestore) {
			restoreContacts();
		}
	}

	void test() {
		// ORG;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=E4=B8=89=E5=A4=A7=E8=A1=97=3B=6E=75=6C=6C
		String readString = "Coolpad";
 
		QuotedPrintableCodec codec = new QuotedPrintableCodec();

		String encodeWords = codec.encode(readString);
		String decodeWords = codec.decode(encodeWords);

		int len = decodeWords.length();

	}
	
	void backupContacts() {
		List<VCardStruct> vCardStructs = _contactHelper.GetAllContacts();
		if (vCardStructs.size() == 0) {
			return;
		}
		StringBuilder builder = new StringBuilder();

		for (VCardStruct contactStruct : vCardStructs) {
			if (contactStruct == null)
				continue;
			String content = contactStruct.ToVCardContent();
			if (content != null)
				builder.append(content + "\n");
		}
		builder.deleteCharAt(builder.lastIndexOf("\n"));
		FileUtil.Write(builder.toString(), path);
	}

	void restoreContacts() {
		_contactHelper.WriteToPhone(path);
	}
}
