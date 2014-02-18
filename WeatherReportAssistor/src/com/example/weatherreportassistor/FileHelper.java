package com.example.weatherreportassistor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.os.Environment;

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
			raf = new RandomAccessFile(path, "r");
			StringBuilder buider = new StringBuilder();
			String line = raf.readLine();
			while (line != null) {
				buider.append(line + "\n");
				line = raf.readLine();
			}
			result = buider.toString();
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
