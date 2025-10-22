package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class ChunkLoader implements IChunkLoader {
	private VFile2 saveDir;
	private boolean createIfNecessary;

	public ChunkLoader(VFile2 var1, boolean var2) {
		this.saveDir = var1;
		this.createIfNecessary = var2;
	}

	private VFile2 chunkFileForXZ(int var1, int var2) {
		String var3 = "c." + Integer.toString(var1, 36) + "." + Integer.toString(var2, 36) + ".dat";
		String var4 = Integer.toString(var1 & 63, 36);
		String var5 = Integer.toString(var2 & 63, 36);
		VFile2 var6 = new VFile2(this.saveDir, var4);
		if(!var6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}
		}

		var6 = new VFile2(var6, var5);
		if(!var6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}
		}

		var6 = new VFile2(var6, var3);
		return !var6.exists() && !this.createIfNecessary ? null : var6;
	}

	public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
		VFile2 var4 = this.chunkFileForXZ(var2, var3);
		if(var4 != null && var4.exists()) {
			try {
				InputStream var5 = var4.getInputStream();
				NBTTagCompound var6 = CompressedStreamTools.loadGzippedCompoundFromOutputStream(var5);
				if(!var6.hasKey("Level")) {
					System.out.println("Chunk file at " + var2 + "," + var3 + " is missing level data, skipping");
					return null;
				}

				if(!var6.getCompoundTag("Level").hasKey("Blocks")) {
					System.out.println("Chunk file at " + var2 + "," + var3 + " is missing block data, skipping");
					return null;
				}

				Chunk var7 = loadChunkIntoWorldFromCompound(var1, var6.getCompoundTag("Level"));
				if(!var7.isAtLocation(var2, var3)) {
					System.out.println("Chunk file at " + var2 + "," + var3 + " is in the wrong location; relocating. (Expected " + var2 + ", " + var3 + ", got " + var7.xPosition + ", " + var7.zPosition + ")");
					var6.setInteger("xPos", var2);
					var6.setInteger("zPos", var3);
					var7 = loadChunkIntoWorldFromCompound(var1, var6.getCompoundTag("Level"));
				}

				var7.func_25124_i();
				return var7;
			} catch (Exception var8) {
				var8.printStackTrace();
			}
		}

		return null;
	}

	public void saveChunk(World var1, Chunk var2) throws IOException {
		var1.checkSessionLock();
		VFile2 var3 = this.chunkFileForXZ(var2.xPosition, var2.zPosition);
		if(var3.exists()) {
			WorldInfo var4 = var1.getWorldInfo();
			var4.setSizeOnDisk(var4.getSizeOnDisk() - var3.length());
		}

		try {
			VFile2 var10 = new VFile2(this.saveDir, "tmp_chunk.dat");
			OutputStream var5 = var10.getOutputStream();
			NBTTagCompound var6 = new NBTTagCompound();
			NBTTagCompound var7 = new NBTTagCompound();
			var6.setTag("Level", var7);
			storeChunkInCompound(var2, var1, var7);
			CompressedStreamTools.writeGzippedCompoundToOutputStream(var6, var5);
			var5.close();
			if(var3.exists()) {
				var3.delete();
			}

			var10.renameTo(var3);
			WorldInfo var8 = var1.getWorldInfo();
			var8.setSizeOnDisk(var8.getSizeOnDisk() + var3.length());
		} catch (Exception var9) {
			var9.printStackTrace();
		}

	}

	public static void storeChunkInCompound(Chunk var0, World var1, NBTTagCompound var2) {
		var1.checkSessionLock();
		var2.setInteger("xPos", var0.xPosition);
		var2.setInteger("zPos", var0.zPosition);
		var2.setLong("LastUpdate", var1.getWorldTime());
		var2.setByteArray("Blocks", var0.blocks);
		var2.setByteArray("Data", var0.data.data);
		var2.setByteArray("SkyLight", var0.skylightMap.data);
		var2.setByteArray("BlockLight", var0.blocklightMap.data);
		var2.setByteArray("HeightMap", var0.heightMap);
		var2.setBoolean("TerrainPopulated", var0.isTerrainPopulated);
		var0.hasEntities = false;
		NBTTagList var3 = new NBTTagList();

		Iterator var5;
		NBTTagCompound var7;
		for(int var4 = 0; var4 < var0.entities.length; ++var4) {
			var5 = var0.entities[var4].iterator();

			while(var5.hasNext()) {
				Entity var6 = (Entity)var5.next();
				var0.hasEntities = true;
				var7 = new NBTTagCompound();
				if(var6.addEntityID(var7)) {
					var3.setTag(var7);
				}
			}
		}

		var2.setTag("Entities", var3);
		NBTTagList var8 = new NBTTagList();
		var5 = var0.chunkTileEntityMap.values().iterator();

		while(var5.hasNext()) {
			TileEntity var9 = (TileEntity)var5.next();
			var7 = new NBTTagCompound();
			var9.writeToNBT(var7);
			var8.setTag(var7);
		}

		var2.setTag("TileEntities", var8);
	}

	public static Chunk loadChunkIntoWorldFromCompound(World var0, NBTTagCompound var1) {
		int var2 = var1.getInteger("xPos");
		int var3 = var1.getInteger("zPos");
		Chunk var4 = new Chunk(var0, var2, var3);
		var4.blocks = var1.getByteArray("Blocks");
		byte[] var10003 = var1.getByteArray("Data");
		var0.getClass();
		var4.data = new NibbleArray(var10003, 7);
		var10003 = var1.getByteArray("SkyLight");
		var0.getClass();
		var4.skylightMap = new NibbleArray(var10003, 7);
		var10003 = var1.getByteArray("BlockLight");
		var0.getClass();
		var4.blocklightMap = new NibbleArray(var10003, 7);
		var4.heightMap = var1.getByteArray("HeightMap");
		var4.isTerrainPopulated = var1.getBoolean("TerrainPopulated");
		int var13;
		if(!var4.data.isValid()) {
			var13 = var4.blocks.length;
			var0.getClass();
			var4.data = new NibbleArray(var13, 7);
		}

		if(var4.heightMap == null || !var4.skylightMap.isValid()) {
			var4.heightMap = new byte[256];
			var13 = var4.blocks.length;
			var0.getClass();
			var4.skylightMap = new NibbleArray(var13, 7);
			var4.generateSkylightMap();
		}

		if(!var4.blocklightMap.isValid()) {
			var13 = var4.blocks.length;
			var0.getClass();
			var4.blocklightMap = new NibbleArray(var13, 7);
			var4.func_1014_a();
		}

		NBTTagList var5 = var1.getTagList("Entities");
		if(var5 != null) {
			for(int var6 = 0; var6 < var5.tagCount(); ++var6) {
				NBTTagCompound var7 = (NBTTagCompound)var5.tagAt(var6);
				Entity var8 = EntityList.createEntityFromNBT(var7, var0);
				var4.hasEntities = true;
				if(var8 != null) {
					var4.addEntity(var8);
				}
			}
		}

		NBTTagList var10 = var1.getTagList("TileEntities");
		if(var10 != null) {
			for(int var11 = 0; var11 < var10.tagCount(); ++var11) {
				NBTTagCompound var12 = (NBTTagCompound)var10.tagAt(var11);
				TileEntity var9 = TileEntity.createAndLoadEntity(var12);
				if(var9 != null) {
					var4.addTileEntity(var9);
				}
			}
		}

		return var4;
	}

	public void func_814_a() {
	}

	public void saveExtraData() {
	}

	public void saveExtraChunkData(World var1, Chunk var2) throws IOException {
	}
}
