package com.example.sdfilesample;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHelper {

	@SuppressLint("NewApi")
	public static boolean Append(String content, String path) {
		boolean result = false;

		if (content.isEmpty())
			return result;
		File targetFile = new File(path);
		 
		try {
			RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
			file.seek(targetFile.length());			
			file.write(content.getBytes());
			file.close();
			result = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static boolean AppendLine(String content, String path) {
		if (content==null) return false;
		content = content + "\r\n";
		return Append(content,path);
	}
	
	@SuppressLint("NewApi")
	public static boolean AppendInExternalStorageFile(String content, String relativePath) {
		if (content==null) return false;
		if (!relativePath.startsWith("/"))
			relativePath = "/" + relativePath;
		
		String path = null;
		
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory()
						.getCanonicalPath() + relativePath;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (path == null) return false;
		
		return Append(content,path);
	}
	
	@SuppressLint("NewApi")
	public static boolean AppendLineInExternalStorageFile(String content, String relativePath) {
		if (content==null) return false;
		content = content + "\r\n";
		return AppendInExternalStorageFile(content,relativePath);
	}
	
}
