package ggxuite.shortener;

import ggxuite.util.IShortener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * http://2.gy/
 * 
 * @author rick
 * 
 */
public class GyShortener implements IShortener {
	private static final Logger log = Logger.getLogger(GyShortener.class.getName());
	@Override
	public String getShortener(String longUrl) {
		try {
			URL url = new URL("http://2.gy/index.php?alias=&url="
					+ URLEncoder.encode(longUrl, "UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String result = "";
			String line;
			String prefix = "<p>新URL: <strong><a href=\"";
			while ((line = rd.readLine()) != null) {
				int index = line.indexOf(prefix);
				if (index > -1) {
					rd.close();
					connection.disconnect();
					return line.substring(index + prefix.length(), index
							+ prefix.length() + 16);
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
		GyShortener s = new GyShortener();
		System.out
				.println(s
						.getShortener("http://dogbitesme.blogspot.com/2009/01/jquery-ui.html"));
	}
}
