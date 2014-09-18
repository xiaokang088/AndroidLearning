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

	String encodingTag = ";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE";
	
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

			if (str.startsWith(this.FNTag)) {
				this.nickName = splitValue(str);
				continue;
			}

			if (this.nickName == null) {
				this.nickName = this.name;
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
		// FN:nick
		String[] arr = content.split(":");
		if (arr.length == 2) {
			String value = arr[1].trim();
			if (value.length() == 0)
				return null;
			return VCardHelper.Decoding(value);
		}
		return null;
	}

	int findIndex(String typeStr, String[] strArr) {
		for (int i = 0; i < strArr.length; i++) {
			if (typeStr.equalsIgnoreCase(strArr[i])) {
				return i;
			}
		}
		return -2;
	}

	//TAG = "ADR;TYPE="
	//encodingTag = ";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE"
	TypeValuePair splitTypeAndValue(String content, String TAG) {
		// ADR;TYPE=HOME;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:;;;;=E6=B1=9F=E8=A5=BF=EF=BC=8C;;=E4=B8=AD=E5=9B=BD
		TypeValuePair typeValuePair = null;
		String contentValue = content.replace(TAG, "");
		  contentValue = contentValue.replace(this.encodingTag, "");
		if (contentValue != null) {
			String[] typeValueArr = contentValue.split(":");
			if (typeValueArr.length == 2) {
				typeValuePair = new TypeValuePair();
				String value = typeValueArr[1].trim();
				typeValuePair.value = VCardHelper.Decoding(value);
				typeValuePair.typeStr = typeValueArr[0];
			}
		}

		return typeValuePair;
	}

	public void SetName(String name) {
		this.name = name;
	}

	public String GetName() {
		return this.name;
	}

	public void SetNickName(String nickName) {
		this.nickName = nickName;
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
		builder.append("\n");
		builder.append(versionTag);
		builder.append(version);
		builder.append("\n");

		if (this.uid != null) {
			builder.append(String.format(UIDTag + "%s", this.uid));
			builder.append("\n");
		}

		builder.append(getEncodeWords(NTag,this.name));
		builder.append("\n");

		String nickName = (this.nickName == null ? this.name : this.nickName);
		builder.append(getEncodeWords(FNTag,nickName));		
		builder.append("\n");

		if (this.title != null) {
			builder.append(getEncodeWords(TitleTag,this.title));
			builder.append("\n");
		}

		if (this.org != null) {
			builder.append(getEncodeWords(ORGTag,this.org));
			builder.append("\n");
		}

		if (this.url != null) {
			builder.append(getEncodeWords(urlTag,this.url));
			builder.append("\n");
		}

		if (this.note != null) {
			builder.append(getEncodeWords(NOTETag,this.note));
			builder.append("\n");
		}

		for (int emailtype : emailTypeMap.keySet()) {
			String email = emailTypeMap.get(emailtype);
			String emailContent = getEncodeWords(this.emailTag,
					emailTypes[emailtype - 1], email);
			
			builder.append(emailContent);
			builder.append("\n");
		}

		for (int telType : telTypeMap.keySet()) {
			String tel = telTypeMap.get(telType);
			String telContent = getEncodeWords(this.telTag,
					telTypes[telType - 1], tel);
			builder.append(telContent);
			builder.append("\n");
		}

		for (int adrType : addressTypeMap.keySet()) {
			String adr = addressTypeMap.get(adrType);
			String adrContent = getEncodeWords(this.AdrTag,
					addressTypes[adrType - 1], adr);
			
			builder.append(adrContent);
			builder.append("\n");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hhmmss");
		String dateStr = dateFormat.format(System.currentTimeMillis());
		builder.append(String.format(this.revTag, dateStr));
		builder.append("\n");
		builder.append(endTag);
		ret = builder.toString();
		return ret;
	}

	String getEncodeWords(String tag, String value) {
		String ret = "";

		String encodeValue = VCardHelper.Encoding(value);
		if (encodeValue.equalsIgnoreCase(value)) {
			// FN:nick
		} else {
			// FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=E4=B8=89=E5=A4=A7=E8=A1=97=3B=6E=75=6C=6C
			tag = tag.replace(":", encodingTag+":");
		}

		ret = String.format(tag + "%s", encodeValue);
		return ret;
	}
	
	String getEncodeWords(String tag, String type, String value) {
		String ret = "";

		String encodeValue = VCardHelper.Encoding(value);
		if (encodeValue.equalsIgnoreCase(value)) {
			// ADR;TYPE=HOME:Hangzhou
			tag = tag + type + ":";
		} else {
			// ADR;TYPE=HOME;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:;;;;=E6=B1=9F=E8=A5=BF=EF=BC=8C;;=E4=B8=AD=E5=9B=BD
			tag = tag + type +encodingTag + ":";
		}

		ret = String.format(tag + "%s", encodeValue);
		return ret;
	}
	
	public boolean IsEqual(VCardStruct otherVCardStruct) {
		if (otherVCardStruct == null)
			return false;

		/*
		 * String name, nickName; String org; String url; String title; String
		 * uid; String photo; String note; String rev;
		 */
		if (!compareString(otherVCardStruct.name, this.name))
			return false;

		if (!compareString(otherVCardStruct.nickName, this.nickName))
			return false;
		if (!compareString(otherVCardStruct.org, this.org))
			return false;
		if (!compareString(otherVCardStruct.url, this.url))
			return false;
		if (!compareString(otherVCardStruct.title, this.title))
			return false;
		/*
		 * if (!compareString(otherVCardStruct.uid, this.uid)) return false;
		 * if (!compareString(otherVCardStruct.photo, this.photo)) return false;
		 * if (!compareString(otherVCardStruct.rev, this.rev)) return false;
		 */
		if (!compareString(otherVCardStruct.note, this.note))
			return false;
		HashMap<Integer, String> otherAddressTypeMap = otherVCardStruct.addressTypeMap;

		for (int key : otherAddressTypeMap.keySet()) {
			if (this.addressTypeMap.containsKey(key)
					&& compareString(this.addressTypeMap.get(key),
							otherAddressTypeMap.get(key))) {
			} else {
				return false;
			}
		}

		HashMap<Integer, String> otherEmailTypeMap = otherVCardStruct.emailTypeMap;
		for (int key : otherEmailTypeMap.keySet()) {
			if (this.emailTypeMap.containsKey(key)
					&& compareString(this.emailTypeMap.get(key),
							otherEmailTypeMap.get(key))) {
			} else {
				return false;
			}
		}

		HashMap<Integer, String> otherTelTypeMap = otherVCardStruct.telTypeMap;
		for (int key : otherTelTypeMap.keySet()) {
			if (this.telTypeMap.containsKey(key)
					&& compareString(this.telTypeMap.get(key),
							otherTelTypeMap.get(key))) {
			} else {
				return false;
			}
		}

		return true;
	}

	boolean compareString(String leftOne, String rightOne) {
		if (leftOne != null)
			leftOne = leftOne.trim();
		if (rightOne != null)
			rightOne = rightOne.trim();

		int leftLength = leftOne == null ? 0 : leftOne.length();
		int rightLength = rightOne == null ? 0 : rightOne.length();

		if (leftLength == 0 && rightLength == 0)
			return true;

		if (leftOne == null || rightOne == null)
			return false;
		
		if (leftOne.equalsIgnoreCase(rightOne))
			return true;
		return false;
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
