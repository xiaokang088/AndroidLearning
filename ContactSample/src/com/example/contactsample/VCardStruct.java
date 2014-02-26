package com.example.contactsample;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import android.provider.ContactsContract.CommonDataKinds.Phone;

public class VCardStruct {

	final String startTag = "BEGIN:VCARD";
	String endTag = "END:VCARD";
	String versionTag = "VERSION:3.0";

	// nickname
	String FNTag = "FN:%s";
	String NTag = "N:%s";

	// ORG
	String ORGTag = "ORG:%s";

	// Title
	String TitleTag = "TITLE:%s";

	// UID
	String UIDTag = "UID:%s";

	// photo
	String photoUrlTag = "PHOTO;TYPE=JPEG:%s";
	// %s is base64 data
	String photoEncodingTag = "PHOTO;TYPE=JPEG;ENCODING=B:[%s]";

	// Tel
	String telTag = "TEL;TYPE=%s:%s";

	// Adress
	String AdrTag = "ADR;TYPE=%s:%s";

	// URL
	String urlTag = "URL;%s";

	// REV
	String revTag = "REV:%s";

	// email
	String emailTag = "EMAIL;TYPE=%s:%s";

	String NOTETag = "NOTE:%s";

	final String[] emailTypes = { "HOME", "WORK", "OTHER", "MOBILE", "PREF" };

	final String[] telTypes = { "HOME", "CELL", "WORK", "FAX,WORK", "FAX,HOME",
			"PAGER", "OTHER", "CALLBACK", "CAR", "COMPANY,Main", "ISDN",
			"MAIN", "OTHER,FAX", "RADIO", "TELEX", "TTY,TDD", "WORK,MOBILE" };

	final String[] addressTypes = { "HOME", "WORK", "OTHER", "PREF" };

	HashMap<Integer, String> telTypeMap = new HashMap<Integer, String>();

	HashMap<Integer, String> emailTypeMap = new HashMap<Integer, String>();

	HashMap<Integer, String> addressTypeMap = new HashMap<Integer, String>();

	String name, nickName;
	String org;
	String url;
	String title;
	String uid;
	String photo;
	String note;
	String rev;

	public VCardStruct() {
		telTypeMap.clear();
		emailTypeMap.clear();
		addressTypeMap.clear();
	}

	public void SetName(String name, String nickName) {
		this.name = name;
		this.nickName = nickName;
	}

	public void SetTitle(String title) {
		this.title = title;
	}

	public void setUID(String uid) {
		this.uid = uid;
	}

	public void SetTel(int telType, String number) {
		if (telType == 0)
			telType = 2;
		telTypeMap.put(telType, number);
	}

	public void SetEmail(int emailType, String number) {
		if (emailType == 0)
			emailType = 5;
		emailTypeMap.put(emailType, number);
	}

	public void SetAddress(int addressType, String address) {
		if (addressType == 0)
			addressType = 4;
		addressTypeMap.put(addressType, address);
	}

	public void SetUrl(String url) {
		this.url = url;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String ToVCardContent() {

		if (this.name == null)
			return null;

		String ret = null;

		StringBuilder builder = new StringBuilder();
		builder.append(startTag);
		builder.append("\r\n");
		builder.append(versionTag);
		builder.append("\r\n");

		if (this.uid != null) {
			builder.append(String.format(UIDTag, this.uid));
			builder.append("\r\n");
		}

		if (this.nickName != null) {
			builder.append(String.format(FNTag, this.nickName));
			builder.append("\r\n");
		}

		builder.append(String.format(NTag, this.name));
		builder.append("\r\n");

		if (this.title != null) {
			builder.append(String.format(TitleTag, this.title));
			builder.append("\r\n");
		}

		if (this.org != null) {
			builder.append(String.format(this.ORGTag, this.org));
			builder.append("\r\n");
		}

		if (this.url != null) {
			builder.append(String.format(this.urlTag, this.url));
			builder.append("\r\n");
		}

		if (this.note != null) {
			builder.append(String.format(this.NOTETag, this.note));
			builder.append("\r\n");
		}

		for (int emailtype : emailTypeMap.keySet()) {
			String email = emailTypeMap.get(emailtype);
			String emailContent = String.format(this.emailTag,
					emailTypes[emailtype - 1], email);
			builder.append(emailContent);
			builder.append("\r\n");
		}

		for (int telType : telTypeMap.keySet()) {
			String tel = telTypeMap.get(telType);
			String telContent = String.format(this.telTag,
					telTypes[telType - 1], tel);
			builder.append(telContent);
			builder.append("\r\n");
		}

		for (int adrType : addressTypeMap.keySet()) {
			String adr = addressTypeMap.get(adrType);
			String adrContent = String.format(this.AdrTag,
					addressTypes[adrType - 1], adr);
			builder.append(adrContent);
			builder.append("\r\n");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hhmmss");
		String dateStr = dateFormat.format(System.currentTimeMillis());
		builder.append(String.format(this.revTag, dateStr));
		builder.append("\r\n");
		builder.append(endTag);
		ret = builder.toString();
		return ret;
	}
}
