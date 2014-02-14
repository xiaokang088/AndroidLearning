package com.example.googledrivesample;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Locale;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class GDriveOutputStream extends OutputStream{

	private final static int MaxTryCount = 5;
	private int mTryCount = 0;

	private final static String TAG = "GDriveOutputStream";
 
	File mFile;
	String mLocation;
	private int mChunkSize;
	private byte[] mBuffer;
	private int mBufferLen;
	private boolean mOk;
	private long mSize;
	private String mMimeType;
	private long mFileLength;
	Drive mBackupFile;
	
	public GDriveOutputStream(Drive backupFile, File file,
			long fileLength, String mimeType) throws IOException {
		mBackupFile = backupFile;
		mFile = file;
		mFileLength = fileLength;

		mChunkSize = 4 * 1024 * 1024;// MediaHttpUploader.MINIMUM_CHUNK_SIZE;//
										// 8 * 1024;
		mBufferLen = 0;
		mSize = 0;
		mBuffer = new byte[mChunkSize];
		mOk = true;
		
	 	mMimeType = mimeType;  

		try {
			buildPostRequest();
			mTryCount = 0;
		} catch (SocketTimeoutException e) {
			if (mTryCount < MaxTryCount) {
				mTryCount++;
				buildPostRequest();
			} else {
				e.printStackTrace();
				throw e;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}	

	void buildPostRequest() throws IOException{		
		JsonHttpContent content = new JsonHttpContent(mBackupFile
				 .getJsonFactory(), mFile);
		String url = UriTemplate.expand(mBackupFile
				.getRootUrl()
				+ "upload/"
				+ mBackupFile.getServicePath(),
				"files?uploadType=resumable", null, false);
		HttpRequest request = mBackupFile.getRequestFactory()
				.buildPostRequest(new GenericUrl(url), content);
		request.getHeaders().put("X-Upload-Content-Type", mMimeType);
		request.getHeaders().put("X-Upload-Content-Length", mFileLength);
		request.setThrowExceptionOnExecuteError(false);
		HttpResponse resp = request.execute();
		if (resp.isSuccessStatusCode()) {
			mLocation = resp.getHeaders().getLocation();
		}
		resp.disconnect();
	}

	@Override
	public void write(int oneByte) throws IOException {
		mBuffer[mBufferLen] = (byte) oneByte;
		mBufferLen++;
		if (mBufferLen >= mChunkSize)
			flush();
	}

	@Override
	public void close() throws IOException {
		flush();
		super.close();
	}

	@Override
	public void flush() throws IOException {
		if (!mOk)
			return;
		if (mBufferLen == 0)
			return;

		HttpRequest req = mBackupFile.getRequestFactory()
				.buildPutRequest(new GenericUrl(mLocation), null);
		req.setContent(new ByteArrayContent(mMimeType, mBuffer, 0, mBufferLen));
		long lastOffset = mSize + mBufferLen;
		req.getHeaders().setContentRange(
				String.format("bytes %d-%d/%d", mSize, mSize + mBufferLen - 1,
						mFileLength));
		req.setThrowExceptionOnExecuteError(false);
		try {
			HttpResponse resp = req.execute();
			if (!resp.isSuccessStatusCode() && resp.getStatusCode() != 308) {
				mOk = false;
			}
			resp.disconnect();
			if (mOk == false)
				throw new IOException(resp.getStatusMessage());
			mTryCount = 0;
		} catch (SocketTimeoutException e) {
			if (mTryCount < MaxTryCount) {
				mTryCount++;
				flush();
			} else {
				e.printStackTrace();
				throw e;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		mSize += mBufferLen;
		mBufferLen = 0;
		Log.d(TAG,
				String.format("uploading file: %s(%,d/%,d)",
						mBackupFile, mSize, mFileLength));
	}

	@Override
	public void write(byte[] buffer, int offset, int count) throws IOException {
		while (count > 0) {
			int cnt = mChunkSize - mBufferLen > count ? count : mChunkSize
					- mBufferLen;
			System.arraycopy(buffer, offset, mBuffer, mBufferLen, cnt);
			mBufferLen += cnt;
			offset += cnt;
			count -= cnt;
			if (mBufferLen >= mChunkSize)
				flush();
		}
	}
 
}
