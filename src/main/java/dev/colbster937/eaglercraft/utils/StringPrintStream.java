package dev.colbster937.eaglercraft.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StringPrintStream extends PrintStream {
  private final ByteArrayOutputStream buffer;

  public StringPrintStream() {
    super(new ByteArrayOutputStream(), true);
    this.buffer = (ByteArrayOutputStream) this.out;
  }

  @Override
  public String toString() {
    try {
      return buffer.toString("UTF-8");
    } catch (Throwable e) {
      return null;
    }
  }

  public void reset() {
    buffer.reset();
  }
}
