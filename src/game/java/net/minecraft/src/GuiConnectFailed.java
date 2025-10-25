package net.minecraft.src;

public class GuiConnectFailed extends GuiScreen {
	private String errorMessage;
	private String errorDetail;
	
	private GuiScreen parent;

	public GuiConnectFailed(GuiScreen var1, String var2, String var3, Object... var4) {
		this.parent = var1;
		StringTranslate var5 = StringTranslate.getInstance();
		this.errorMessage = var5.translateKey(var2);
		if(var4 != null) {
			this.errorDetail = var5.translateKeyFormat(var3, var4);
		} else {
			this.errorDetail = var5.translateKey(var3);
		}

	}

	public void updateScreen() {
	}

	protected void keyTyped(char var1, int var2) {
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.done")));
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(parent);
		}

	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 16777215);
		this.drawCenteredString(this.fontRenderer, this.errorDetail, this.width / 2, this.height / 2 - 10, 16777215);
		super.drawScreen(var1, var2, var3);
	}
}
