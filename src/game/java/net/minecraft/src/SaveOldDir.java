package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class SaveOldDir extends SaveHandler {
	public SaveOldDir(VFile2 var1, String var2, boolean var3) {
		super(var1, var2, var3);
	}

	public IChunkLoader getChunkLoader(WorldProvider var1) {
		VFile2 var2 = this.getSaveDirectory();
		if(var1 instanceof WorldProviderHell) {
			VFile2 var3 = new VFile2(var2, "DIM-1");
			return new McRegionChunkLoader(var3);
		} else {
			return new McRegionChunkLoader(var2);
		}
	}

	public void saveWorldInfoAndPlayer(WorldInfo var1, List var2) {
		var1.setSaveVersion(19132);
		super.saveWorldInfoAndPlayer(var1, var2);
	}
}
