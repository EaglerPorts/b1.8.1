package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class RegionFileCache {
	private static final Map regionsByFilename = new HashMap();

	public static synchronized RegionFile createOrLoadRegionFile(VFile2 var0, int var1, int var2) {
		VFile2 var3 = new VFile2(var0, "region");
		VFile2 var4 = new VFile2(var3, "r." + (var1 >> 5) + "." + (var2 >> 5) + ".mcr");
		Reference var5 = (Reference)regionsByFilename.get(var4);
		RegionFile var6;
		if(var5 != null) {
			var6 = (RegionFile)var5.get();
			if(var6 != null) {
				return var6;
			}
		}

		if(regionsByFilename.size() >= 256) {
			clearRegionFileReferences();
		}

		var6 = new RegionFile(var4);
		regionsByFilename.put(var4, new SoftReference(var6));
		return var6;
	}

	public static synchronized void clearRegionFileReferences() {
		saveRegionFiles();

		regionsByFilename.clear();
	}

	public static synchronized void saveRegionFiles() {
		Iterator var0 = regionsByFilename.values().iterator();

		while(var0.hasNext()) {
			Reference var1 = (Reference)var0.next();

			try {
				RegionFile var2 = (RegionFile)var1.get();
				if(var2 != null) {
					var2.close();
				}
			} catch (IOException var3) {
				var3.printStackTrace();
			}
		}
	}

	public static int getSizeDelta(VFile2 var0, int var1, int var2) {
		RegionFile var3 = createOrLoadRegionFile(var0, var1, var2);
		return var3.getSizeDelta();
	}

	public static DataInputStream getChunkInputStream(VFile2 var0, int var1, int var2) {
		RegionFile var3 = createOrLoadRegionFile(var0, var1, var2);
		return var3.getChunkDataInputStream(var1 & 31, var2 & 31);
	}

	public static DataOutputStream getChunkOutputStream(VFile2 var0, int var1, int var2) {
		RegionFile var3 = createOrLoadRegionFile(var0, var1, var2);
		return var3.getChunkDataOutputStream(var1 & 31, var2 & 31);
	}
}
