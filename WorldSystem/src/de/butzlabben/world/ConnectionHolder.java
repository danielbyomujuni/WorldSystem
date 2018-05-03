package de.butzlabben.world;

import java.net.URL;
import java.util.Scanner;

public class ConnectionHolder {

	@SuppressWarnings("resource")
	public static int getMaxPlayers() throws Exception{

		String inputLine = "";
		try {
			inputLine = new Scanner(new URL("https://seagiants.eu/worldsystem/limit.php").openStream(), "UTF-8").nextLine();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (inputLine.equals(""))
			return 0;
		return Integer.parseInt(inputLine);
	}
	
	@SuppressWarnings("resource")
	public static int getMaxPlayersWithLicense(String license) throws Exception{
		String inputLine = "";
		String url = "https://seagiants.eu/worldsystem/limit.php?license=" + license;
		try {
			inputLine = new Scanner(new URL(url).openStream(), "UTF-8").nextLine();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (inputLine.equals(""))
			return 0;
		return Integer.parseInt(inputLine);
	}
}
