package dev.colbster937.eaglercraft.vfs;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public interface VFilenameFilter {
  boolean accept(VFile2 dir, String name);
}