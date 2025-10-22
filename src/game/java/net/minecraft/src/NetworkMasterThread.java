package net.minecraft.src;

import net.lax1dude.eaglercraft.EagUtils;

class NetworkMasterThread extends Thread {
	final NetworkManager netManager;

	NetworkMasterThread(NetworkManager var1) {
		this.netManager = var1;
	}

	public void run() {
		EagUtils.sleep(5000L);
		if(NetworkManager.getReadThread(this.netManager).isAlive()) {
			try {
				NetworkManager.getReadThread(this.netManager).stop();
			} catch (Throwable var3) {
			}
		}

		if(NetworkManager.getWriteThread(this.netManager).isAlive()) {
			try {
				NetworkManager.getWriteThread(this.netManager).stop();
			} catch (Throwable var2) {
			}
		}

	}
}
