package net.minecraft.src;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.colbster937.eaglercraft.vfs.VFileFilter;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

class ChunkFolderPattern implements VFileFilter {
	public static final Pattern folderRegexPattern = Pattern.compile("[0-9a-z]|([0-9a-z][0-9a-z])");

	private ChunkFolderPattern() {
	}

	public boolean accept(VFile2 var1) {
		Matcher var2 = folderRegexPattern.matcher(var1.getName());
		return var2.matches();
	}

	ChunkFolderPattern(Empty2 var1) {
		this();
	}
}
