package com.example.googledrivesample;

import java.io.IOException;
import java.io.InputStream;

public class ManagedInputStream extends InputStream {

	protected IOException handleIOException(IOException e) {
		return e;
	}

	protected InputStream	is;

	public ManagedInputStream(InputStream is) {
		this.is = is;
	 
	}

	@Override
	public int available() throws IOException {
		try {
			return is.available();
		} catch (IOException e) {
			throw handleIOException(e);
		}
	}

	@Override
	public void close() throws IOException {
		try {
			is.close();
		} catch (IOException e) {
			throw handleIOException(e);
		} finally {
		 
		}
	}

	@Override
	public void mark(int readlimit) {
		is.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return is.markSupported();
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		try {
			return is.read(buffer, offset, length);
		} catch (IOException e) {
			throw handleIOException(e);
		}
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		try {
			return is.read(buffer);
		} catch (IOException e) {
			throw handleIOException(e);
		}
	}

	@Override
	public synchronized void reset() throws IOException {
		try {
			is.reset();
		} catch (IOException e) {
			throw handleIOException(e);
		}
	}

	@Override
	public long skip(long byteCount) throws IOException {
		try {
			return is.skip(byteCount);
		} catch (IOException e) {
			throw handleIOException(e);
		}
	}

	@Override
	public int read() throws IOException {
		try {
			return is.read();
		} catch (IOException e) {
			throw handleIOException(e);
		}
	}

}
