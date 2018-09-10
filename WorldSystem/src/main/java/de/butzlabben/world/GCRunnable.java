package de.butzlabben.world;

public class GCRunnable implements Runnable {

	@Override
	public void run() {
		new Thread(() -> System.gc()).start();
	}
}
