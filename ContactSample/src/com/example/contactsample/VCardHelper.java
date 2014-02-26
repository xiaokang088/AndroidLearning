package com.example.contactsample;

import java.util.List;

public class VCardHelper {

	
	
	public static void WriteToVCard(VCardStruct contactStruct){	
		String relativePath = contactStruct.name  + ".vcf";
		String content = contactStruct.ToVCardContent();
		FileHelper.WriteInExternalStorageFile(content, relativePath);
	}
	
	public static void WriteToVCard(List<VCardStruct> contactStructs) {
		StringBuilder builder = new StringBuilder();
		String relativePath = "allContact.vcf";
		for (VCardStruct contactStruct : contactStructs) {
			
			String content = contactStruct.ToVCardContent();
			builder.append(content+"\r\n\r\n");
		}
		FileHelper.WriteInExternalStorageFile(builder.toString(), relativePath);
	}
	
}
