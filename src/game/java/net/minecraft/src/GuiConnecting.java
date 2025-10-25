package net.minecraft.src;

import net.lax1dude.eaglercraft.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.socket.AddressResolver;

import net.minecraft.client.Minecraft;

public class GuiConnecting extends GuiScreen {
	private NetClientHandler clientHandler;
	private boolean cancelled = false;

	private GuiScreen parent;
	private boolean connected = false;
	private String currentAddress;
	private int timer = 0;
	private boolean successful = false;
	private IWebSocketClient webSocket;

	public GuiConnecting(Minecraft var1, GuiScreen var2, String var3) {
		System.out.println("Connecting to " + var3);
		this.parent = var2;
		var1.changeWorld1((World) null);
		this.currentAddress = AddressResolver.resolveURI(var3);
		this.clientHandler = new NetClientHandler(var1, var2);
	}

	public void updateScreen() {
		++timer;
		if (timer > 1) {
			if (this.webSocket == null) {
				this.webSocket = PlatformNetworking.openWebSocket(this.currentAddress);
				if (this.webSocket.getState() == EnumEaglerConnectionState.FAILED
						|| this.webSocket.getState() == EnumEaglerConnectionState.CLOSED) {
					this.mc.displayGuiScreen(new GuiConnectFailed(this.parent, "connect.failed", "disconnect.genericReason",
							new Object[] { "Could not open websocket to\"" + this.currentAddress + "\"!" }));
				}
			} else {
				if (this.webSocket.getState() == EnumEaglerConnectionState.CONNECTED) {
					if (!this.successful) {
						this.clientHandler.getNetManager().setWebSocket(this.webSocket);
						this.clientHandler.addToSendQueue(new Packet2Handshake(this.mc.session.username));
						this.successful = true;
						this.connected = true;
					} else {
						this.clientHandler.processReadPackets();
					}
				} else if (this.webSocket.getState() == EnumEaglerConnectionState.FAILED) {
					if (this.webSocket != null) {
						this.webSocket.close();
						this.webSocket = null;
					}
					this.mc.displayGuiScreen(new GuiConnectFailed(this.parent, "connect.failed", "disconnect.genericReason",
							new Object[] { "Connection Refused!" }));
				}
			}
			if (timer > 200 && !this.clientHandler.getNetManager().isOpen()) {
				if (this.webSocket != null) {
					this.webSocket.close();
					this.webSocket = null;
				}
				this.mc.displayGuiScreen(new GuiConnectFailed(this.parent, "connect.failed", "disconnect.genericReason",
						new Object[] { "Connection timed out" }));
			}
		}

	}

	protected void keyTyped(char var1, int var2) {
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.clear();
		this.controlList
				.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
	}

	protected void actionPerformed(GuiButton var1) {
		if (var1.id == 0) {
			this.cancelled = true;
			if (this.clientHandler != null) {
				this.clientHandler.disconnect();
			}

			this.mc.displayGuiScreen(parent);
		}

	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		StringTranslate var4 = StringTranslate.getInstance();
		if (this.clientHandler == null) {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.connecting"), this.width / 2,
					this.height / 2 - 50, 16777215);
			this.drawCenteredString(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 16777215);
		} else {
			this.drawCenteredString(this.fontRenderer, var4.translateKey("connect.authorizing"), this.width / 2,
					this.height / 2 - 50, 16777215);
			this.drawCenteredString(this.fontRenderer, this.clientHandler.field_1209_a, this.width / 2, this.height / 2 - 10,
					16777215);
		}

		super.drawScreen(var1, var2, var3);
	}

	static NetClientHandler setNetClientHandler(GuiConnecting var0, NetClientHandler var1) {
		return var0.clientHandler = var1;
	}

	static boolean isCancelled(GuiConnecting var0) {
		return var0.cancelled;
	}

	static NetClientHandler getNetClientHandler(GuiConnecting var0) {
		return var0.clientHandler;
	}
}
