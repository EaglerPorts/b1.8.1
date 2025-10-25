package dev.colbster937.eaglercraft.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.MathHelper;

public class SaveUtils {
  private static IProgressUpdate progress;

  public static ArrayList<VFile2> listDirs(VFile2 root) {
    ArrayList<VFile2> dirs = new ArrayList<VFile2>();
    List<VFile2> files = root.listFiles(true);
    int count = files.size();
    ArrayList<String> names = new ArrayList<String>();
    String base = root.getPath();
    for (int i = 0; i < count; ++i) {
      VFile2 f = files.get(i);
      String path = f.getPath();
      if (path != null && path.startsWith(base)) {
        String rel = path.substring(base.length());
        if (rel.startsWith("/")) rel = rel.substring(1);
        if (!MathHelper.stringNullOrLengthZero(rel)) {
          int slash = rel.indexOf('/');
          if (slash >= 0) {
            String name = rel.substring(0, slash);
            if (!MathHelper.stringNullOrLengthZero(name) && !names.contains(name)) {
              names.add(name);
              dirs.add(new VFile2(root, name));
            }
          }
        }
      }
    }
    return dirs;
  }

  public static ArrayList<VFile2> listDirs(String root) {
    return listDirs(new VFile2(root));
  }

  public static byte[] export(String title, VFile2 folder) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos;
    try {
      if (progress != null) {
        progress.func_594_b(title);
        progress.displayLoadingString("Preparing export");
        progress.setLoadingProgress(0);
      }
      zos = new ZipOutputStream(baos);
      List<VFile2> all = folder.listFiles(true);
      int total = all.size();
      String basePath = folder.getPath();

      for (int i = 0; i < total; ++i) {
        VFile2 f = all.get(i);
        String relPath = f.getPath().substring(basePath.length());
        if (relPath.startsWith("/")) relPath = relPath.substring(1);
        if (MathHelper.stringNullOrLengthZero(relPath)) continue;

        byte[] data;
        try {
          data = f.getAllBytes();
        } catch (Throwable t) {
          data = null;
        }
        if (data != null) {
          if (progress != null) progress.displayLoadingString("Exporting: " + relPath);
          zos.putNextEntry(new ZipEntry(relPath));
          zos.write(data);
          zos.closeEntry();
        }
        if (progress != null) {
          progress.setLoadingProgress((int)((i + 1L) * 100L / (total == 0 ? 1 : total)));
        }
      }

      zos.finish();
      zos.flush();
      zos.close();

      if (progress != null) {
        progress.displayLoadingString("Export complete");
        progress.setLoadingProgress(100);
      }

      return baos.toByteArray();
    } catch (IOException e) {
      if (progress != null) {
        progress.displayLoadingString("Export failed");
        progress.setLoadingProgress(0);
      }
      return new byte[0];
    }
  }

  public static byte[] exportWorld(String file) {
    return export("World Export", new VFile2(Minecraft.getMinecraftDir(), "saves", file, "/"));
  }

  public static void _import(String title, VFile2 targetDir, VFile2 zipFile) {
    ZipInputStream zis;
    InputStream is;
    try {
      if (progress != null) {
        progress.func_594_b(title);
        progress.displayLoadingString("Preparing import");
        progress.setLoadingProgress(0);
      }
      is = zipFile.getInputStream();
      zis = new ZipInputStream(is);
      byte[] buffer = new byte[8192];
      ZipEntry entry;
      int processed = 0;
      while ((entry = zis.getNextEntry()) != null) {
        String name = entry.getName();
        if (name == null || name.length() == 0) {
          zis.closeEntry();
          continue;
        }
        if (name.endsWith("/")) {
          zis.closeEntry();
          processed++;
          if (progress != null) progress.setLoadingProgress(processed % 100);
          continue;
        }
        if (progress != null) progress.displayLoadingString("Importing: " + name);
        VFile2 out = new VFile2(targetDir, name);
        OutputStream os;
        try {
          os = out.getOutputStream();
          int n;
          while ((n = zis.read(buffer, 0, buffer.length)) > 0) {
            os.write(buffer, 0, n);
          }
          os.close();
        } finally {
        }
        zis.closeEntry();
        processed++;
        if (progress != null) progress.setLoadingProgress(processed % 100);
      }
      zis.close();
      is.close();
      if (progress != null) {
        progress.displayLoadingString("Import complete");
        progress.setLoadingProgress(100);
      }
    } catch (IOException e) {
      if (progress != null) {
        progress.displayLoadingString("Import failed");
        progress.setLoadingProgress(0);
      }
    }
  }

  public static void setProgressUpdate(IProgressUpdate p) {
    SaveUtils.progress = p;
  }

  public static void removeProgressUpdate() {
    SaveUtils.progress = null;
  }
}
