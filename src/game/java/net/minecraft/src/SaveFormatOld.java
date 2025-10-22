package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class SaveFormatOld implements ISaveFormat {
	protected final VFile2 field_22180_a;

	private static VFile2 worldsList;

	public SaveFormatOld(VFile2 var1) {
		this.worldsList = new VFile2(var1.getPath(), "worlds_list.txt");
		this.field_22180_a = var1;
	}

	public String func_22178_a() {
		return "Old Format";
	}

	public List func_22176_b() {
		ArrayList var1 = new ArrayList();

		if(worldsList.exists()) {
			for (int i = 0; i < worldsList.getAllLines().length; ++i) {
				String var3 = "World" + (i + 1);
				WorldInfo var4 = this.getWorldInfo(var3);
				if(var4 != null) {
					var1.add(new SaveFormatComparator(var3, "", var4.getLastTimePlayed(), var4.getSizeOnDisk(), var4.func_35918_q(), false));
				}
			}
		}

		return var1;
	}

	public void flushCache() {
	}

	public WorldInfo getWorldInfo(String var1) {
		VFile2 var2 = new VFile2(this.field_22180_a, var1);
		VFile2 var3 = new VFile2(var2, "level.dat");
		if(!var3.exists()) {
			return null;
		} else {
			NBTTagCompound var4;
			NBTTagCompound var5;
			if(var3.exists()) {
				try {
					var4 = CompressedStreamTools.loadGzippedCompoundFromOutputStream(var3.getInputStream());
					var5 = var4.getCompoundTag("Data");
					return new WorldInfo(var5);
				} catch (Exception var7) {
					var7.printStackTrace();
				}
			}

			var3 = new VFile2(var2, "level.dat_old");
			if(var3.exists()) {
				try {
					var4 = CompressedStreamTools.loadGzippedCompoundFromOutputStream(var3.getInputStream());
					var5 = var4.getCompoundTag("Data");
					return new WorldInfo(var5);
				} catch (Exception var6) {
					var6.printStackTrace();
				}
			}

			return null;
		}
	}

	public void func_22170_a(String var1, String var2) {
		VFile2 var3 = new VFile2(this.field_22180_a, var1);
		VFile2 var4 = new VFile2(var3, "level.dat");
		if(var4.exists()) {
			try {
				NBTTagCompound var5 = CompressedStreamTools.loadGzippedCompoundFromOutputStream(var4.getInputStream());
				NBTTagCompound var6 = var5.getCompoundTag("Data");
				var6.setString("LevelName", var2);
				CompressedStreamTools.writeGzippedCompoundToOutputStream(var5, var4.getOutputStream());
			} catch (Exception var7) {
				var7.printStackTrace();
			}

		}
	}

	public void func_22172_c(String var1) {
		VFile2 var2 = new VFile2(this.field_22180_a, var1);
		VFile2 var3 = new VFile2(var2, "level.dat");
		if(var3.exists()) {
			func_22179_a(var2.listFiles(true).toArray(new VFile2[0]));
			var2.delete();
		}
	}

	protected static void func_22179_a(VFile2[] var0) {
		for(int var1 = 0; var1 < var0.length; ++var1) {
			var0[var1].delete();
		}

	}

	public ISaveHandler getSaveLoader(String var1, boolean var2) {
		return new SaveHandler(this.field_22180_a, var1, var2);
	}

	public boolean isOldMapFormat(String var1) {
		return false;
	}

	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		return false;
	}
}
