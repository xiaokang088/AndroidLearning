package com.example.rateappsample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.impl.conn.DefaultResponseParser;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	Button btnCheck;
	String Tag = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnRateGooglePlay = (Button) this
				.findViewById(R.id.btnRateGooglePlay);
		btnRateGooglePlay.setOnClickListener(this);

		Button btnRateAmazon = (Button) this.findViewById(R.id.btnRateAmazon);
		btnRateAmazon.setOnClickListener(this);

		Button btnRate = (Button) this.findViewById(R.id.btnRate);
		btnRate.setOnClickListener(this);

		btnCheck = (Button) this.findViewById(R.id.btnCheck);
		btnCheck.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnRateGooglePlay) {
			
			Bundle testBundle = new Bundle();
			testBundle.putString("status", "Success");
			testBundle.putString("current", "Videos");
			
			Bundle statusBundle = new Bundle();
			
			Bundle photoBundle = new Bundle();
			photoBundle.putInt("allCounters", 21);
			photoBundle.putInt("allSize", 0);
			
			Bundle videoBundle = new Bundle();
			photoBundle.putInt("allCounters", 1);
			photoBundle.putInt("allSize", 0);
			
			statusBundle.putBundle("Photos", photoBundle);
			statusBundle.putBundle("Videos", videoBundle);
			testBundle.putBundle("handler_status", statusBundle);
			
			String str= testBundle.toString();
			
			
			AppRater.ShowGooglePlayRateDialog(this);
		}

		if (v.getId() == R.id.btnRateAmazon) {
			AppRater.ShowAmazonRateDialog(this);
		}

		if (v.getId() == R.id.btnRate) {
			AppRater.ShowRateDialog(this);
		}

		if (v.getId() == R.id.btnCheck) {
			getVersion();
		}
	}

	private final static String package_name = "com.seagate.android.dashboard";
	/*
	 * <div class="content" itemprop="softwareVersion"> 1.0.0.23 </div>
	 */
	final static String versionDiv = "<div class=\"content\" itemprop=\"softwareVersion\">";
	final static String versionEndDiv = "</div>";

	/* <li><strong>Version:</strong> 1.0.0.23</li> */
	final static String amazonVersionDiv = "<li><strong>Version:</strong>";
	final static String amazonVersionEndDiv = "</li>";

	String googlePlayUrl = "https://play.google.com/store/apps/details?id="
			+ package_name + "&hl=en";

	String amazonUrl = "http://www.amazon.com/gp/mas/dl/android?p="
			+ package_name;

	void getVersion() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btnCheck.setText("start....");
					}
				});

				boolean isAmazon = AppRater.GetIsAmazon();

				// String str = isAmazon ? getPageContent(amazonUrl) :
				// getPageContent(googlePlayUrl);
				String str = getPageContent(amazonUrl);

				/*
				 * final String version = isAmazon ? getVersionOnAmazon(str) :
				 * getVersionOnGooglePlay(str);
				 */
				final String version = getVersionOnAmazon(str);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btnCheck.setText(version);
					}
				});
			}
		});
		thread.start();
	}

	String getPageContent(String url) {
		String str = "";
			 
		DefaultHttpClient client = new DefaultHttpClient();
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding"))
					request.addHeader("Accept-Encoding", "gzip");
				if (!request.containsHeader("Content-type"))
					request.addHeader("Content-type", "application/x-www-form-urlencoded");
				if (!request.containsHeader("Charsert"))
					request.addHeader("Charsert", "UTF-8");
			}
		});
		
		client.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					org.apache.http.Header ceheader = entity.getContentEncoding();
					if (ceheader != null) {
						HeaderElement[] codecs = ceheader.getElements();
						for (HeaderElement codec : codecs)
							if (codec.getName().equalsIgnoreCase("gzip")) {
								response.setEntity(new GzipDecompressingEntity(response.getEntity()));
								return;
							}
					}
				}
			}
		});
		
		HttpGet get = new HttpGet(url);
		HttpResponse response = null ;
		try {
			  response = client.execute(get);
			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			InputStream is = entity.getContent();

			if (is != null) {

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[120];
				int ch = -1;
				int count = 0;
				while ((ch = is.read(buf)) != -1) {
					baos.write(buf, 0, ch);
					count += ch;
				}
				str = new String(baos.toByteArray());

				return str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			get.abort();
			
		}
		return str;
	}

	String getVersionOnAmazon(String str) {
		String version = "";
		int divStartIndex = str.indexOf(amazonVersionDiv);
		if (divStartIndex != -1) {
			int startIndex = divStartIndex + amazonVersionDiv.length();
			String endString = str.substring(startIndex);
			int endIndex = endString.indexOf(amazonVersionEndDiv);
			version = endString.substring(0, endIndex);
			version = version.trim();
		}
		return version;
	}

	String getVersionOnGooglePlay(String str) {
		String version = "";
		int divStartIndex = str.indexOf(versionDiv);
		if (divStartIndex != -1) {
			int startIndex = divStartIndex + versionDiv.length();
			String endString = str.substring(startIndex);
			int endIndex = endString.indexOf(versionEndDiv);
			version = endString.substring(0, endIndex);
			version = version.trim();
		}
		return version;
	}

	class MyLineParser extends BasicLineParser {
		@Override
		public org.apache.http.Header parseHeader(final CharArrayBuffer buffer)
				throws ParseException {
			try {
				return super.parseHeader(buffer);
			} catch (ParseException ex) {
				// —π÷∆ParseException“Ï≥£
				return new BasicHeader("invalid", buffer.toString());
			}
		}
	}

	class MyClientConnection extends DefaultClientConnection {
		@Override
		protected HttpMessageParser createResponseParser(
				final SessionInputBuffer buffer,
				final HttpResponseFactory responseFactory,
				final HttpParams params) {
			return new DefaultResponseParser(buffer, new MyLineParser(),
					responseFactory, params);
		}
	}
	
	class MyClientConnectionOperator extends DefaultClientConnectionOperator {
		public MyClientConnectionOperator(final SchemeRegistry sr) {
			super(sr);
		}

		@Override
		public OperatedClientConnection createConnection() {
			return new MyClientConnection();
		}
	}
	
	class MyClientConnManager extends SingleClientConnManager {
		public MyClientConnManager(final HttpParams params,
				final SchemeRegistry sr) {
			super(params, sr);
		}

		@Override
		protected ClientConnectionOperator createConnectionOperator(
				final SchemeRegistry sr) {
			return new MyClientConnectionOperator(sr);
		}
	}
	
	private static class GzipDecompressingEntity extends HttpEntityWrapper {
		public GzipDecompressingEntity(HttpEntity entity) {
			super(entity);
		}

		public InputStream getContent() throws IOException, IllegalStateException {
			InputStream wrappedin = this.wrappedEntity.getContent();

			return new GZIPInputStream(wrappedin);
		}

		public long getContentLength() {
			return -1L;
		}
	}
}
