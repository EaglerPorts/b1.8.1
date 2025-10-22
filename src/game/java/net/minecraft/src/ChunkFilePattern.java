package net.minecraft.src;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.colbster937.eaglercraft.vfs.VFilenameFilter;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

class ChunkFilePattern implements VFilenameFilter {
	public static final Pattern dataFilenamePattern = Pattern.compile("c\\.(-?[0-9a-z]+)\\.(-?[0-9a-z]+)\\.dat");

	private ChunkFilePattern() {
	}

	public boolean accept(VFile2 var1, String var2) {
		Matcher var3 = dataFilenamePattern.matcher(var2);
		return var3.matches();
	}

	ChunkFilePattern(Empty2 var1) {
		this();
	}
}
