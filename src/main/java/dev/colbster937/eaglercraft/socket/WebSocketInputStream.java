package dev.colbster937.eaglercraft.socket;

import java.io.InputStream;
import java.io.IOException;

import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.IWebSocketFrame;

public class WebSocketInputStream extends InputStream {
  private final IWebSocketClient webSocket;
  private byte[] buf;
  private int idx;

  public WebSocketInputStream(IWebSocketClient webSocket) {
    this.webSocket = webSocket;
  }

  @Override
  public int read() throws IOException {
    byte[] b = new byte[1];
    int n = read(b, 0, 1);
    return n == -1 ? -1 : (b[0] & 0xFF);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (off < 0 || len < 0 || off + len > b.length) throw new IndexOutOfBoundsException();
    if (len == 0) return 0;

    while (buf == null || idx >= buf.length) {
      IWebSocketFrame f = webSocket.getNextBinaryFrame();
      if (f != null) {
        byte[] x = f.getByteArray();
        if (x != null && x.length > 0) {
          buf = x;
          idx = 0;
          break;
        }
      }
    }

    int n = Math.min(len, buf.length - idx);
    System.arraycopy(buf, idx, b, off, n);
    idx += n;
    if (idx >= buf.length) buf = null;
    return n;
  }

  @Override
  public void close() throws IOException {

  }
}
