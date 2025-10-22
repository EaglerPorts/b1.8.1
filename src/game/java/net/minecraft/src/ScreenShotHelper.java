package net.minecraft.src;

import net.lax1dude.eaglercraft.internal.PlatformApplication;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class ScreenShotHelper {
	public static String saveScreenshot(VFile2 var0, int var1, int var2) {
		return func_35879_a(var0, (String)null, var1, var2);
	}

	public static String func_35879_a(VFile2 var0, String var1, int var2, int var3) {
		return "Saved screenshot as " + PlatformApplication.saveScreenshot();
	}
}
