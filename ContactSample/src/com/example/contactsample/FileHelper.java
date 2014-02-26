package com.example.contactsample;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class FileHelper {

	public static boolean Write(String content, String path) {
		boolean result = false;

		if (content == null)
			return result;
		
		File targetFile = new File(path);
		if (targetFile.exists()){
			targetFile.delete();
		}

		try {
			RandomAccessFile file = new RandomAccessFile(targetFile, "rw");	
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

	public static boolean Append(String content, String path) {
		boolean result = false;

		if (content == null)
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
	
	public static boolean WriteInExternalStorageFile(String content, String relativePath) {
		if (content==null) return false;
		String path = GetRelativePathInExternalStorage(relativePath);
		if (path == null) return false;
		return Write(content,path);
	}
	
	public static String GetRelativePathInExternalStorage(String relativePath){
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
		 return path;
	}
	
	public static boolean AppendLineInExternalStorageFile(String content, String relativePath) {
		if (content==null) return false;
		content = content + "\r\n";
		return AppendInExternalStorageFile(content,relativePath);
	}
	
	public static String ReadStringFromFile(String path) {
		String result = null;

		RandomAccessFile raf;
		try {
			InputStream stream = new FileInputStream(path);

			if (stream != null) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				byte[] buf = new byte[120];
				int ch = -1;
				while ((ch = stream.read(buf)) != -1) {
					outputStream.write(buf, 0, ch);
				}
				result = new String(outputStream.toByteArray(),"UTF-8");
				return result;
			}		 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	public static String ReadStringFromExternalStorageFile(String relativePath){	 
		if(relativePath == null) return null;
		
		String path = null;
		
		if (!relativePath.startsWith("/"))
			relativePath = "/" + relativePath;
		
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
		return ReadStringFromFile(path);
	}
	
}
