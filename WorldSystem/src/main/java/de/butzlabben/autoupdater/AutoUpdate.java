package de.butzlabben.autoupdater;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Butzlabben
 * @since 02.05.2018
 */
public class AutoUpdate implements Runnable {

	private UpdateInformations ui;
	private String jar;

	public AutoUpdate(UpdateInformations ui, String jar) {
		this.ui = ui;
		this.jar = jar;
	}

	@Override
	public void run() {
		FileChannel out = null;
		FileOutputStream outStream = null;
		try {
			
			ReadableByteChannel in = Channels
					.newChannel(new URL(ui.getURL()).openStream());
			outStream = new FileOutputStream(jar);
			out = outStream.getChannel();
			out.transferFrom(in, 0, Long.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
