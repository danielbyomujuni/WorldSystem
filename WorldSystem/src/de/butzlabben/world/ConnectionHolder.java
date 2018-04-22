package de.butzlabben.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class ConnectionHolder {

	@SuppressWarnings("resource")
	public static int getMaxPlayers() throws Exception{
		BufferedReader reader = null;
		String inputLine = "";
		try {
			inputLine = new Scanner(new URL("http://seagiants.eu/validation/limit.php").openStream(), "UTF-8").nextLine();

		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
		if (inputLine.equals(""))
			return 0;
		return Integer.parseInt(inputLine);
	}
	
	@SuppressWarnings("resource")
	public static int getMaxPlayersWithLicense(String license) throws Exception{
		BufferedReader reader = null;
		String inputLine = "";
		String url = "http://seagiants.eu/validation/limit.php?license=" + license;
		try {
			inputLine = new Scanner(new URL(url).openStream(), "UTF-8").nextLine();

		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
		if (inputLine.equals(""))
			return 0;
		return Integer.parseInt(inputLine);
	}
}
