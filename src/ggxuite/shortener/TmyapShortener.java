package ggxuite.shortener;

import ggxuite.util.IShortener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * http://t.myap.tw
 * 
 * @author rick
 * 
 */
public class TmyapShortener implements IShortener {
	private static final Logger log = Logger.getLogger(TmyapShortener.class.getName());
	@Override
	public String getShortener(String longUrl) {
		try {
			URL url = new URL("http://t.myap.tw/create?url="
					+ URLEncoder.encode(longUrl, "UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String result = "";
			String line;
			String prefix = "id=\"tinyurl\"><ax>您的短網址為：";
			while ((line = rd.readLine()) != null) {
				int index = line.indexOf(prefix);
				if (index > -1) {
					rd.close();
					connection.disconnect();
					return line.substring(index + prefix.length(), index
							+ prefix.length() + 21);
				}
			}
			rd.close();
			connection.disconnect();
			return result;
		} catch (Exception e) {
			log.throwing(getClass().toString(), "getShortener", e.fillInStackTrace());
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		TmyapShortener s = new TmyapShortener();
		System.out
				.println(s
						.getShortener("http://dogbitesme.blogspot.com/2009/01/jquery-ui.html"));
	}
}
