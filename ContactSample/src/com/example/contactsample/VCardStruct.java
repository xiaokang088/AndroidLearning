package com.example.contactsample;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.provider.ContactsContract.CommonDataKinds.Phone;

public class VCardStruct {

	public static String startTag = "BEGIN:VCARD";
	public static String endTag = "END:VCARD";
	String versionTag = "VERSION:";
	String version = "3.0";

	// nickname
	String FNTag = "FN:";
	String NTag = "N:";

	// ORG
	String ORGTag = "ORG:";

	// Title
	String TitleTag = "TITLE:";

	// UID
	String UIDTag = "UID:";

	// photo
	String photoUrlTag = "PHOTO;TYPE=JPEG:%s";
	// %s is base64 data
	String photoEncodingTag = "PHOTO;TYPE=JPEG;ENCODING=B:[%s]";

	// Tel
	String telTag = "TEL;TYPE=";

	// Adress
	String AdrTag = "ADR;TYPE=";

	// URL
	String urlTag = "URL:";

	// REV
	String revTag = "REV:%s";

	// email
	String emailTag = "EMAIL;TYPE=";

	String NOTETag = "NOTE:";

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

	public VCardStruct(List<String> strArr) {

		telTypeMap.clear();
		emailTypeMap.clear();
		addressTypeMap.clear();

		for (String str : strArr) {

			if (str.startsWith(this.NTag)) {
				this.name = splitValue(str);
				continue;
			}

			if (str.startsWith(this.NTag)) {
				this.name = splitValue(str);
				continue;
			}

			if (str.startsWith(this.FNTag)) {
				this.nickName = splitValue(str);
				continue;
			}

			if (str.startsWith(this.ORGTag)) {
				this.org = splitValue(str);
				continue;
			}

			if (str.startsWith(this.TitleTag)) {
				this.title = splitValue(str);
				continue;
			}

			if (str.startsWith(this.UIDTag)) {
				this.uid = splitValue(str);
				continue;
			}

			if (str.startsWith(this.urlTag)) {
				this.url = splitValue(str);
				continue;
			}

			if (str.startsWith(this.NOTETag)) {
				this.note = splitValue(str);
				continue;
			}

			if (str.startsWith(this.revTag)) {
				this.rev = splitValue(str);
				continue;
			}

			if (str.startsWith(this.telTag)) {
				TypeValuePair typeValuePair = splitTypeAndValue(str,
						this.telTag);
				if (typeValuePair != null) {
					typeValuePair.type = findIndex(typeValuePair.typeStr,
							telTypes) + 1;
					telTypeMap.put(typeValuePair.type, typeValuePair.value);
				}
				continue;
			}

			if (str.startsWith(this.AdrTag)) {
				TypeValuePair typeValuePair = splitTypeAndValue(str,
						this.AdrTag);
				if (typeValuePair != null) {
					typeValuePair.type = findIndex(typeValuePair.typeStr,
							addressTypes) + 1;
					addressTypeMap.put(typeValuePair.type, typeValuePair.value);
				}
				continue;
			}

			if (str.startsWith(this.emailTag)) {
				TypeValuePair typeValuePair = splitTypeAndValue(str,
						this.emailTag);
				if (typeValuePair != null) {
					typeValuePair.type = findIndex(typeValuePair.typeStr,
							emailTypes) + 1;
					emailTypeMap.put(typeValuePair.type, typeValuePair.value);
				}
				continue;
			}
		}

	}

	String splitValue(String content) {
		String[] arr = content.split(":");
		if (arr.length >= 2) {
			return arr[1];
		} else {
			return null;
		}
	}

	int findIndex(String typeStr, String[] strArr) {
		for (int i = 0; i < strArr.length; i++) {
			if (typeStr.equalsIgnoreCase(strArr[i])) {
				return i;
			}
		}
		return -2;
	}

	TypeValuePair splitTypeAndValue(String content, String TAG) {
		TypeValuePair typeValuePair = null;
		String contentValue = content.replace(TAG, "");
		if (contentValue != null) {
			String[] typeValueArr = contentValue.split(":");
			if (typeValueArr.length >= 2) {
				typeValuePair = new TypeValuePair();
				typeValuePair.value = typeValueArr[1];
				typeValuePair.typeStr = typeValueArr[0];
			}
		}

		return typeValuePair;
	}

	public void SetName(String name, String nickName) {
		this.name = name;
		this.nickName = nickName;
	}

	public String GetName() {
		return this.name;
	}

	public String GetNickName() {
		return this.nickName;
	}

	public void SetTitle(String title) {
		this.title = title;
	}

	public String GetTitle() {
		return this.title;
	}

	public void setUID(String uid) {
		this.uid = uid;
	}

	public String GetUID() {
		return this.uid;
	}

	public void SetUrl(String url) {
		this.url = url;
	}

	public String GetUrl() {
		return this.url;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String GetOrg() {
		return this.org;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String GetNote() {
		return this.note;
	}

	public void SetTel(int telType, String number) {
		if (telType == 0)
			telType = 2;
		telTypeMap.put(telType, number);
	}

	public HashMap<Integer, String> GetTel() {
		return telTypeMap;
	}

	public void SetEmail(int emailType, String number) {
		if (emailType == 0)
			emailType = 5;
		emailTypeMap.put(emailType, number);
	}

	public HashMap<Integer, String> GetEmail() {
		return emailTypeMap;
	}

	public void SetAddress(int addressType, String address) {
		if (addressType == 0)
			addressType = 4;
		addressTypeMap.put(addressType, address);
	}

	public HashMap<Integer, String> GetAddress() {
		return addressTypeMap;
	}

	public String ToVCardContent() {

		if (this.name == null)
			return null;

		String ret = null;

		StringBuilder builder = new StringBuilder();
		builder.append(startTag);
		builder.append("\r\n");
		builder.append(versionTag);
		builder.append(version);
		builder.append("\r\n");

		if (this.uid != null) {
			builder.append(String.format(UIDTag + "%s", this.uid));
			builder.append("\r\n");
		}

		if (this.nickName != null) {
			builder.append(String.format(FNTag + "%s", this.nickName));
			builder.append("\r\n");
		}

		builder.append(String.format(NTag + "%s", this.name));
		builder.append("\r\n");

		if (this.title != null) {
			builder.append(String.format(TitleTag + "%s", this.title));
			builder.append("\r\n");
		}

		if (this.org != null) {
			builder.append(String.format(this.ORGTag + "%s", this.org));
			builder.append("\r\n");
		}

		if (this.url != null) {
			builder.append(String.format(this.urlTag + "%s", this.url));
			builder.append("\r\n");
		}

		if (this.note != null) {
			builder.append(String.format(this.NOTETag + "%s", this.note));
			builder.append("\r\n");
		}

		for (int emailtype : emailTypeMap.keySet()) {
			String email = emailTypeMap.get(emailtype);
			String emailContent = String.format(this.emailTag + "%s:%s",
					emailTypes[emailtype - 1], email);
			builder.append(emailContent);
			builder.append("\r\n");
		}

		for (int telType : telTypeMap.keySet()) {
			String tel = telTypeMap.get(telType);
			String telContent = String.format(this.telTag + "%s:%s",
					telTypes[telType - 1], tel);
			builder.append(telContent);
			builder.append("\r\n");
		}

		for (int adrType : addressTypeMap.keySet()) {
			String adr = addressTypeMap.get(adrType);
			String adrContent = String.format(this.AdrTag + "%s:%s",
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

	class TypeValuePair {

		public TypeValuePair() {

		}

		int type;

		public int GetType() {
			return type;
		}

		public void SetType(int value) {
			type = value;
		}

		String typeStr;

		public String GetTypeStr() {
			return typeStr;
		}

		public void SetTypeStr(String value) {
			typeStr = value;
		}

		String value;

		public String GetValue() {
			return value;
		}

		public void SetValue(String value) {
			this.value = value;
		}

	}
}
