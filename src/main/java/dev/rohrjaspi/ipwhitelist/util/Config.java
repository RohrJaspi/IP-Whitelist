package dev.rohrjaspi.ipwhitelist.util;

import dev.rohrjaspi.ipwhitelist.Main;

public class Config {

	public static String getConfigString(String path) {
		return Main.getInstance().getConfig().getString(path);
	}

	public static int getConfigInt(String path) {
		return Main.getInstance().getConfig().getInt(path);
	}

}
