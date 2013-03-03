/**
 * 
 */
package ggxuite.util;

import ggxuite.module.XuiteUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Rick
 * 
 */
public class XuiteUtil {
	private static final Logger log = Logger.getLogger(XuiteUtil.class
			.getName());
	private String METHOD_GET_LIST = "xuite.webhd.private.cloudbox.getList";
	private String METHOD_GET_FILE = "xuite.webhd.prepare.cloudbox.getFile";
	private HttpURLConnection conn;
	private InputStream is;
	private InputStreamReader isr;
	private OutputStreamWriter wr;
	private BufferedReader rd;
	private StringReader sr;
	private String apiKey;
	private String secretKey;
	private String auth;

	public XuiteUtil(String apiKey, String secretKey) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
	}

	public XuiteUtil(String apiKey, String secretKey, String auth) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.auth = auth;
	}

	public XuiteUtil(XuiteUser xuite) {
		this.apiKey = xuite.getApiKey();
		this.secretKey = xuite.getSecretKey();
		this.auth = xuite.getoAuth();
	}

	public JSONArray getList() throws ClientProtocolException,
			URISyntaxException, IOException, JSONException {
		String urlStr = "https://api.xuite.net/api.php?api_key=" + apiKey
				+ "&api_sig="
				+ getMD5(secretKey, apiKey, auth, "/", METHOD_GET_LIST)
				+ "&method=" + METHOD_GET_LIST + "&key=/&auth=" + auth;
		log.info("getList URL:" + urlStr);
		URL url = new URL(urlStr);

		String line;
		StringBuffer sb = new StringBuffer();
		urlConn(url, null);
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		JSONObject json = new JSONObject(sb.toString());
		return json.getBoolean("ok") ? json.getJSONObject("rsp").getJSONArray(
				"file") : json.getJSONArray("msg");
	}

	public String getDirectURL(String key, String parent)
			throws ClientProtocolException, URISyntaxException, IOException,
			JSONException {
		String urlStr = "https://api.xuite.net/api.php?api_key=" + apiKey
				+ "&api_sig="
				+ getMD5(secretKey, apiKey, auth, key, METHOD_GET_FILE, parent)
				+ "&method=" + METHOD_GET_FILE + "&key=" + key + "&parent="
				+ parent + "&auth=" + auth;
		URL url = new URL(urlStr);
		log.info(urlStr);
		String line;
		urlConn(url, null);
		StringBuffer sb = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		JSONObject json = new JSONObject(sb.toString());
		return json.getBoolean("ok") ? json.getJSONObject("rsp").getString(
				"url2") : "Fail!!";
	}

	private void urlConn(URL url, Map<String, String> params)
			throws IOException {
		conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		if (params != null) {
			List<NameValuePair> pa = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					pa.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
			}
			wr.write(getQuery(pa));
		}
		wr = new OutputStreamWriter(conn.getOutputStream());
		wr.flush();
		is = conn.getInputStream();
		isr = new InputStreamReader(is);
		rd = new BufferedReader(isr);
	}

	private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	public String getMD5(String... param) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			String p = "";
			for (String i : param)
				p += i;
			md.update(p.getBytes());
			result = toHexString(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String toHexString(byte[] in) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < in.length; i++) {
			String hex = Integer.toHexString(0xFF & in[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public void close() {
		try {
			if (sr != null)
				sr.close();
			if (wr != null)
				wr.close();
			if (rd != null)
				rd.close();
			if (is != null)
				is.close();
			if (conn != null)
				conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	@SuppressWarnings("rawtypes")
	List<Class> classes;
	int shortenerIndex = 0;

	@SuppressWarnings("rawtypes")
	public String getShortener(String url) {

		String shortUrl = null;
		try {
			int retry = 0;

			if (classes == null) {
				classes = new ArrayList<Class>(Arrays.asList(Util
						.getClasses("ggxuite.shortener")));
			}

			while (Util.isEmpty(shortUrl) && retry < 5) {
				shortUrl = ((IShortener) classes.get(
						shortenerIndex % classes.size()).newInstance())
						.getShortener(url);
				if (Util.isEmpty(shortUrl)) {
					classes.remove(shortenerIndex % classes.size());
					retry++;
				} else {
					shortenerIndex++;
				}
			}
			if (retry == 5) {
				shortUrl = url;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shortUrl;
	}
}
