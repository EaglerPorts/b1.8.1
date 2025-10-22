package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public interface ISaveHandler {
	WorldInfo loadWorldInfo();

	void checkSessionLock();

	IChunkLoader getChunkLoader(WorldProvider var1);

	void saveWorldInfoAndPlayer(WorldInfo var1, List var2);

	void saveWorldInfo(WorldInfo var1);

	VFile2 func_28113_a(String var1);
}
