package de.butzlabben.world.autoupdater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.butzlabben.world.WorldSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @author Butzlabben
 * @since 02.05.2018
 */
public class UpdateInformations {

    private final String version, url, plugin;
    private final boolean silent;

    public UpdateInformations(String version, String url, String plugin, boolean silent) {
        this.version = version;
        this.url = url;
        this.plugin = plugin;
        this.silent = silent;
    }

    protected static synchronized UpdateInformations getInformations() {
        String json = callURL("https://zendilu.net/butzlabben/worldsystem/info.php?version=" + WorldSystem.getInstance().getDescription().getVersion());
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, UpdateInformations.class);
    }

    public static String callURL(String URL) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn;
        InputStreamReader in = null;
        try {
            URL url = new URL(URL);
            urlConn = url.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

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

    public String getPlugin() {
        return plugin;
    }

    public boolean isSilent() {
        return silent;
    }
}
