package de.butzlabben.autoupdater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Butzlabben
 * @since 02.05.2018
 */
public class UpdateInformations {

	private final String version, url;

	public static synchronized UpdateInformations getInformations() {
		String json = callURL("https://seagiants.eu/worldsystem/info.php");
		Gson gson = new GsonBuilder().create();
		UpdateInformations ui = gson.fromJson(json, UpdateInformations.class);
		return ui;
	}

	private static String callURL(String URL) {
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(URL);
			urlConn = url.openConnection();

			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);

			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);

				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public String getVersion() {
		return version;
	}

	public String getURL() {
		return url;
	}

	public UpdateInformations(String version, String url) {
		this.version = version;
		this.url = url;
	}
}
