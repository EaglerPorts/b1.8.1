package dev.colbster937.eaglercraft.socket;

import java.io.DataInputStream;

public class WebSocketDataInputStream extends DataInputStream {
  public WebSocketDataInputStream(WebSocketInputStream in) {
    super(in);
  }
}
