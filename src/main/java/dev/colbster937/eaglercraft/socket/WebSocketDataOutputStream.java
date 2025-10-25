package dev.colbster937.eaglercraft.socket;

import java.io.DataOutputStream;

public class WebSocketDataOutputStream extends DataOutputStream {
  public WebSocketDataOutputStream(WebSocketOutputStream out) {
    super(out);
  }
}
