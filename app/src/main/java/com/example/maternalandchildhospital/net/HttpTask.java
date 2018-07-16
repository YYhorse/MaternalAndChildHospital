package com.example.maternalandchildhospital.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.example.maternalandchildhospital.publics.util.Utils;

public class HttpTask extends Task<Integer> {
	private String url = "";
	private String content = "";
	private String timestamp = "";

	private String netResult = "";
	private String sessionID = "";
	private String cookie = "";
	private int netTimeOut = 60000;

	private NetReturnListener netReturnListener;

	private int httpType = 0;

	public HttpTask(TaskListener listener, String url, String content, String timestamp, NetReturnListener nrl, int httpType) {
		super(listener);
		this.url = url;
		this.content = content;
//		Utils.Log("url = " + url);
//		Utils.Log("content = " + content);
		this.timestamp = timestamp;
		cancelConnect = false;
		netResult = null;
		netReturnListener = nrl;
		this.httpType = httpType;

	}

	@Override
	public Integer get() {
		int temp = -1;
		// Utils.Log("httpType = " + httpType);
		switch (httpType) {
		case 0:
			temp = getRequest();
			break;
		case 1:
			temp = getRequestHists();
			break;
		}
		return temp;
	}

	/*********** 联网1 ***********/
	public int getRequest() {
		//
		int responseCode = -2; // -1代表联网被用户取消掉
		InputStream inputStream = null;
		InputStreamReader inputReader = null;
		BufferedReader reader = null;
		 Utils.Log("url = " + url);
		 Utils.Log("content = " + content);
		URL url = null;
		try {
			url = new URL(this.url.trim());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection connectiong = null;
		try {
			connectiong = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		connectiong.setConnectTimeout(netTimeOut);
		connectiong.setRequestProperty("Authorization", "Basic");
		connectiong.setRequestProperty("User-Agent", "Mozilla/5.0");
		if (sessionID.equals("")) {
			connectiong.setRequestProperty("sessionID", sessionID);
		}
		connectiong.setRequestProperty("terminalVersion", "1.0.0");
		connectiong.setRequestProperty("terminalType", "02");
		// connectiong.setRequestProperty("Content-Type",
		// "application/x-www-form-urlencoded");

		connectiong.setRequestProperty("Content-type", "text/xml; charset=UTF-8");

		connectiong.setDoInput(true);
		connectiong.setDoOutput(true);
		try {
			connectiong.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		try {
			connectiong.getOutputStream().write(this.content.trim().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			responseCode = connectiong.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utils.Log("responseCode = " + responseCode);
		if (cancelConnect) {
			responseCode = -1;
		} else {

			if (responseCode >= 200 && responseCode <= 299 && netResult == null) {
				try {
					inputStream = (InputStream) connectiong.getInputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inputReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputReader);
				String inputLine = null;
				StringBuffer strBuffer = new StringBuffer();
				try {
					while ((inputLine = reader.readLine()) != null) {
						strBuffer.append(inputLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Utils.Log("h|"+strBuffer+"|h");
				// if (!this.timestamp.equals(GlobalInfo.netTimestamp)) {
				// GlobalInfo.netResult = null;
				// } else {
				netResult = strBuffer.toString();
				// Utils.Log(netResult.substring(netResult.length() - 100,
				// netResult.length()));
				// Utils.Log("netResult = " + netResult);
				// }
				netReturnListener.netReturn(netResult);
				strBuffer = null;
			} else {
				netReturnListener.netReturn("1");
				return -2;
			}
		}
		if (inputReader != null) {
			try {
				inputReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputReader = null;
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			reader = null;
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputStream = null;
		}
		if (connectiong != null) {
			connectiong.disconnect();
			connectiong = null;
		}

		return responseCode;
	}

	/*********** 联网hists ***********/
	public int getRequestHists() {
		//
		int responseCode = -2; // -1代表联网被用户取消掉
		InputStream inputStream = null;
		InputStreamReader inputReader = null;
		BufferedReader reader = null;
		// Utils.Log("url = " + url);
		// Utils.Log("content = " + content);
		URL url = null;
		try {
			url = new URL(this.url.trim());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection connectiong = null;
		try {
			connectiong = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		connectiong.setConnectTimeout(netTimeOut);
		connectiong.setRequestProperty("Authorization", "Basic");
		connectiong.setRequestProperty("User-Agent", "Mozilla/5.0");
		if (sessionID.equals("")) {
			connectiong.setRequestProperty("sessionID", sessionID);
		}
		connectiong.setRequestProperty("terminalVersion", "1.0.0");
		connectiong.setRequestProperty("terminalType", "02");
		connectiong.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		// [theRequest setValue:@"application/x-www-form-urlencoded"
		// forHTTPHeaderField:@"Content-Type"];
		// connectiong.setRequestProperty("Content-type", "text/xml;
		// charset=UTF-8");

		connectiong.setDoInput(true);
		connectiong.setDoOutput(true);
		try {
			connectiong.setRequestMethod("POST");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}

		try {
			connectiong.getOutputStream().write(this.content.trim().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			responseCode = connectiong.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utils.Log("responseCode = " + responseCode);
		if (cancelConnect) {
			responseCode = -1;
		} else {

			if (responseCode >= 200 && responseCode <= 299 && netResult == null) {
				try {
					inputStream = (InputStream) connectiong.getInputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inputReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputReader);
				String inputLine = null;
				StringBuffer strBuffer = new StringBuffer();
				try {
					while ((inputLine = reader.readLine()) != null) {
						strBuffer.append(inputLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Utils.Log("h|"+strBuffer+"|h");
				// if (!this.timestamp.equals(GlobalInfo.netTimestamp)) {
				// GlobalInfo.netResult = null;
				// } else {
				netResult = strBuffer.toString();
				// Utils.Log(netResult.substring(netResult.length() - 100,
				// netResult.length()));
				// Utils.Log("netResult = " + netResult);
				// }
				netReturnListener.netReturn(netResult);
				strBuffer = null;
			} else {
				netReturnListener.netReturn("1");
				return -2;
			}
		}
		if (inputReader != null) {
			try {
				inputReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputReader = null;
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			reader = null;
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputStream = null;
		}
		if (connectiong != null) {
			connectiong.disconnect();
			connectiong = null;
		}

		return responseCode;
	}

	/*********** 联网2 ***********/
	public int getRequest1() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 60000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 120000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.SOCKET_BUFFER_SIZE, 1024 * 4);
		HttpPost httpPost = new HttpPost(this.url.trim());
		// httpPost.setHeader("sessionId", GlobalInfo.sessionID);
		httpPost.setHeader("cookie", cookie);
		StreamEntity se = new StreamEntity();
		se.data = content;
		se.encode = "utf-8";
		HttpEntity entity = new EntityTemplate(se);
		httpPost.setEntity(entity);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String respContent = null;
		try {
			respContent = httpclient.execute(httpPost, responseHandler);
			if (respContent != null && !respContent.equals("")) {
				netResult = respContent;
			} else {
				netResult = null;
			}
			netReturnListener.netReturn(netResult);
			// Utils.Log("netResult = " + netResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CookieStore mCookieStore = httpclient.getCookieStore();
		List<Cookie> cookies = mCookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			if ("JSESSIONID".equals(cookies.get(i).getName())) {
				cookie = "JSESSIONID=" + cookies.get(i).getValue();
				break;
			}

		}
		// Utils.Log("GlobalInfo.cookie = " + cookie);
		return 0;
	}

	class StreamEntity implements ContentProducer {
		public void writeTo(OutputStream outstream) throws IOException {
			Writer writer = new OutputStreamWriter(outstream, this.encode);
			writer.write(this.data);
			writer.flush();
		}

		public String encode;
		public String data;
	}
}
