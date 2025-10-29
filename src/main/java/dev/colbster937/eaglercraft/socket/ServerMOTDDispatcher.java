package dev.colbster937.eaglercraft.socket;

import java.util.List;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.IWebSocketFrame;
import net.minecraft.src.ServerNBTStorage;
import net.minecraft.src.StringTranslate;

public class ServerMOTDDispatcher {
  private final ServerNBTStorage server;
  private final IWebSocketClient webSocket;
  private final StringTranslate translate;

  private long timer = 0L;
  private boolean hasSentAccept = false;
  public boolean isFinished = false;

  public ServerMOTDDispatcher(ServerNBTStorage server, IWebSocketClient webSocket) {
    this.server = server;
    this.webSocket = webSocket;
    this.server.pingSentTime = System.currentTimeMillis();
    this.translate = StringTranslate.getInstance();
  }

  public void update() {
    if (this.timer >= 150 || this.webSocket.getState() == EnumEaglerConnectionState.FAILED) {
      this.server.field_35792_e = -1L;
      this.server.field_35791_d = "\u00a74" + translate.translateKey("eaglercraft.cantReachServer");
      finish();
      return;
    }
    if (!this.hasSentAccept && this.webSocket.getState() == EnumEaglerConnectionState.CONNECTED) {
      this.hasSentAccept = true;
      this.webSocket.send("Accept: MOTD");
    }
    List<IWebSocketFrame> frames = this.webSocket.getNextFrames();
    if (frames != null) {
      for (IWebSocketFrame frame : frames) {
        if (frame.isString()) {
          String str = frame.getString();
          try {
            JSONObject obj = new JSONObject(str);
            JSONObject infoObj = obj.getJSONObject("data");
            this.server.field_35792_e = System.currentTimeMillis() - this.server.pingSentTime;
            this.server.field_35791_d = "\u00a77" + infoObj.getJSONArray("motd").getString(0);
            this.server.field_35794_c = "\u00a77" + infoObj.getInt("online") + "\u00a78/\u00a77"
                + infoObj.getInt("max");
            finish();
          } catch (Throwable t) {
            t.printStackTrace();
          }
        }
      }
    }
    timer++;
  }

  private void finish() {
    this.isFinished = true;
    try {
      this.webSocket.close();
    } catch (Throwable t) {
    }
  }
}
