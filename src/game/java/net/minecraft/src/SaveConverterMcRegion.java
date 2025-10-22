package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class SaveConverterMcRegion extends SaveFormatOld {
	public SaveConverterMcRegion(VFile2 var1) {
		super(var1);
	}

	public String func_22178_a() {
		return "Scaevolus\' McRegion";
	}

	public List func_22176_b() {
		ArrayList var1 = new ArrayList();
		List<VFile2> var2 = this.field_22180_a.listFiles(true);
		int var3 = var2.size();

		ArrayList var4 = new ArrayList();
		String var5 = this.field_22180_a.getPath();

		for (int var6 = 0; var6 < var3; ++var6) {
			VFile2 var7 = var2.get(var6);
			String var8 = var7.getPath();
			if (var8 != null && var8.startsWith(var5)) {
				String var9 = var8.substring(var5.length());
				if (var9.startsWith("/")) {
					var9 = var9.substring(1);
				}
				if (!MathHelper.stringNullOrLengthZero(var9)) {
					int var10 = var9.indexOf('/');
					String var11 = var10 >= 0 ? var9.substring(0, var10) : var9;
					if (!MathHelper.stringNullOrLengthZero(var11) && !var4.contains(var11)) {
						var4.add(var11);
					}
				}
			}
		}

		int var12 = var4.size();
		for (int var13 = 0; var13 < var12; ++var13) {
			String var14 = (String)var4.get(var13);
			WorldInfo var15 = this.getWorldInfo(var14);
			if (var15 != null) {
				boolean var16 = var15.getSaveVersion() != 19132;
				String var17 = var15.getWorldName();
				if (var17 == null || MathHelper.stringNullOrLengthZero(var17)) {
					var17 = var14;
				}

				var1.add(new SaveFormatComparator(
					var14,
					var17,
					var15.getLastTimePlayed(),
					var15.getSizeOnDisk(),
					var15.func_35918_q(),
					var16
				));
			}
		}

		return var1;
	}

	public void flushCache() {
		RegionFileCache.clearRegionFileReferences();
	}

	public ISaveHandler getSaveLoader(String var1, boolean var2) {
		return new SaveOldDir(this.field_22180_a, var1, var2);
	}

	public boolean isOldMapFormat(String var1) {
		WorldInfo var2 = this.getWorldInfo(var1);
		return var2 != null && var2.getSaveVersion() == 0;
	}

	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		var2.setLoadingProgress(0);
		ArrayList var3 = new ArrayList();
		ArrayList var4 = new ArrayList();
		ArrayList var5 = new ArrayList();
		ArrayList var6 = new ArrayList();
		VFile2 var7 = new VFile2(this.field_22180_a, var1);
		VFile2 var8 = new VFile2(var7, "DIM-1");
		System.out.println("Scanning folders...");
		this.func_22183_a(var7, var3, var4);
		if(var8.exists()) {
			this.func_22183_a(var8, var5, var6);
		}

		int var9 = var3.size() + var5.size() + var4.size() + var6.size();
		System.out.println("Total conversion count is " + var9);
		this.func_22181_a(var7, var3, 0, var9, var2);
		this.func_22181_a(var8, var5, var3.size(), var9, var2);
		WorldInfo var10 = this.getWorldInfo(var1);
		var10.setSaveVersion(19132);
		ISaveHandler var11 = this.getSaveLoader(var1, false);
		var11.saveWorldInfo(var10);
		this.func_22182_a(var4, var3.size() + var5.size(), var9, var2);
		if(var8.exists()) {
			this.func_22182_a(var6, var3.size() + var5.size() + var4.size(), var9, var2);
		}

		return true;
	}

	private void func_22183_a(VFile2 var1, ArrayList var2, ArrayList var3) {
		ChunkFolderPattern var4 = new ChunkFolderPattern((Empty2)null);
		ChunkFilePattern var5 = new ChunkFilePattern((Empty2)null);
		VFile2[] var6 = var1.listFiles(var4);
		VFile2[] var7 = var6;
		int var8 = var6.length;

		for(int var9 = 0; var9 < var8; ++var9) {
			VFile2 var10 = var7[var9];
			var3.add(var10);
			VFile2[] var11 = var10.listFiles(var4);
			VFile2[] var12 = var11;
			int var13 = var11.length;

			for(int var14 = 0; var14 < var13; ++var14) {
				VFile2 var15 = var12[var14];
				VFile2[] var16 = var15.listFiles(var5);
				VFile2[] var17 = var16;
				int var18 = var16.length;

				for(int var19 = 0; var19 < var18; ++var19) {
					VFile2 var20 = var17[var19];
					var2.add(new ChunkFile(var20));
				}
			}
		}

	}

	private void func_22181_a(VFile2 var1, ArrayList var2, int var3, int var4, IProgressUpdate var5) {
		Collections.sort(var2);
		byte[] var6 = new byte[4096];
		Iterator var7 = var2.iterator();

		while(var7.hasNext()) {
			ChunkFile var8 = (ChunkFile)var7.next();
			int var9 = var8.getXChunk();
			int var10 = var8.getYChunk();
			RegionFile var11 = RegionFileCache.createOrLoadRegionFile(var1, var9, var10);
			if(!var11.isChunkSaved(var9 & 31, var10 & 31)) {
				try {
					DataInputStream var12 = new DataInputStream(new GZIPInputStream(var8.getChunkFile().getInputStream()));
					DataOutputStream var13 = var11.getChunkDataOutputStream(var9 & 31, var10 & 31);
					boolean var14 = false;

					while(true) {
						int var17 = var12.read(var6);
						if(var17 == -1) {
							var13.close();
							var12.close();
							break;
						}

						var13.write(var6, 0, var17);
					}
				} catch (IOException var15) {
					var15.printStackTrace();
				}
			}

			++var3;
			int var16 = (int)Math.round(100.0D * (double)var3 / (double)var4);
			var5.setLoadingProgress(var16);
		}

		RegionFileCache.clearRegionFileReferences();
	}

	private void func_22182_a(ArrayList var1, int var2, int var3, IProgressUpdate var4) {
		Iterator var5 = var1.iterator();

		while(var5.hasNext()) {
			VFile2 var6 = (VFile2)var5.next();
			VFile2[] var7 = var6.listFiles(false).toArray(new VFile2[0]);
			func_22179_a(var7);
			var6.delete();
			++var2;
			int var8 = (int)Math.round(100.0D * (double)var2 / (double)var3);
			var4.setLoadingProgress(var8);
		}

	}
}
