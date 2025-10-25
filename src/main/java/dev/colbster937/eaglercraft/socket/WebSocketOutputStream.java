package dev.colbster937.eaglercraft.socket;

import java.io.OutputStream;

import dev.colbster937.eaglercraft.utils.ScuffedUtils;

import java.io.IOException;

import net.lax1dude.eaglercraft.internal.IWebSocketClient;

public class WebSocketOutputStream extends OutputStream {
  private final IWebSocketClient webSocket;

  public WebSocketOutputStream(IWebSocketClient webSocket) {
    this.webSocket = webSocket;
  }

  @Override
  public void write(int b) throws IOException {
    write(ScuffedUtils.getBytes(b), 0, 1);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    if (off < 0 || len < 0 || off + len > b.length)
      throw new IndexOutOfBoundsException();
    if (len == 0)
      return;
    byte[] slice = new byte[len];
    System.arraycopy(b, off, slice, 0, len);
    webSocket.send(slice);
  }

  @Override
  public void flush() throws IOException {

  }

  @Override
  public void close() throws IOException {

  }
}