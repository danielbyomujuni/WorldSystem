package de.butzlabben.autoupdater.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.butzlabben.autoupdater.UpdateInformations;

/**
 * @author Butzlabben
 * @since 03.09.2018
 */
public class UpdateInformationsTest {
	
	private final String url = "https://seagiants.eu/worldsystem/info.php?version=1.0";

	@Test
	public void testCallURL() {
		assertNotNull("Failed to call URL to AutoUpdate Server", UpdateInformations.callURL(url));
	}
}
