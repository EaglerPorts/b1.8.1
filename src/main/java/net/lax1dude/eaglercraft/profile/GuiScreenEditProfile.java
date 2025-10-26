package net.lax1dude.eaglercraft.profile;

import org.lwjgl.opengl.GL11;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.Keyboard;
import net.lax1dude.eaglercraft.Mouse;
import net.lax1dude.eaglercraft.internal.FileChooserResult;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.lax1dude.eaglercraft.profile.EaglerProfile.EaglerProfileSkin;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.StringTranslate;
import net.peyton.eagler.minecraft.TextureLocation;

public class GuiScreenEditProfile extends GuiScreen {
	
	private GuiScreen parent;
	private GuiTextField username;
	
	private boolean dropDownOpen = false;
	private String[] dropDownOptions;
	private int slotsVisible = 0;
	private int selectedSlot = 0;
	private int scrollPos = -1;
	private int skinsHeight = 0;
	private boolean dragging = false;
	private int mousex = 0;
	private int mousey = 0;

  private int trans = 15;
	
	private static final TextureLocation gui = new TextureLocation("/eagler/gui/eagler_gui.png");
	
	public static final String[] defaultOptions = new String[] {
		"Default Steve",
		"Tennis Steve",
		"Tuxedo Steve",
		"Athlete Steve",
		"Cyclist Steve",
		"Boxer Steve",
		"Prisoner Steve",
		"Scottish Steve",
		"Developer Steve",
		"Herobrine",
		"Slime",
		"Trump",
		"Notch",
		"Creeper",
		"Zombie",
		"Pig",
		"Squid",
		"Mooshroom"
	};
	
	public static final TextureLocation[] defaultOptionsTextures = new TextureLocation[] {
			new TextureLocation("/eagler/skins/01.default_steve.png"),
			new TextureLocation("/eagler/skins/02.tennis_steve.png"),
			new TextureLocation("/eagler/skins/03.tuxedo_steve.png"),
			new TextureLocation("/eagler/skins/04.athlete_steve.png"),
			new TextureLocation("/eagler/skins/05.cyclist_steve.png"),
			new TextureLocation("/eagler/skins/06.boxer_steve.png"),
			new TextureLocation("/eagler/skins/07.prisoner_steve.png"),
			new TextureLocation("/eagler/skins/08.scottish_steve.png"),
			new TextureLocation("/eagler/skins/09.dev_steve.png"),
			new TextureLocation("/eagler/skins/10.herobrine.png"),
			new TextureLocation("/eagler/skins/11.slime.png"),
			new TextureLocation("/eagler/skins/12.trump.png"),
			new TextureLocation("/eagler/skins/13.notch.png"),
			new TextureLocation("/eagler/skins/14.creeper.png"),
			new TextureLocation("/eagler/skins/15.zombie.png"),
			new TextureLocation("/eagler/skins/16.pig.png"),
			new TextureLocation("/eagler/skins/17.squid.png"),
			new TextureLocation("/eagler/skins/18.mooshroom.png")
	};
	
	protected String screenTitle = "Edit Profile";
	
	public GuiScreenEditProfile(GuiScreen parent) {
		this.parent = parent;
		reconcatDD();
	}
	
	private void reconcatDD() {
		String[] n = new String[EaglerProfile.skins.size()];
		for(int i = 0; i < n.length; ++i) {
			n[i] = EaglerProfile.skins.get(i).name;
		}
		
		this.dropDownOptions = EaglerProfile.concatArrays(n, defaultOptions);
	}
	
	private GuiButton button0, button1, button2, button10, button11, button12;

	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		StringTranslate var1 = StringTranslate.getInstance();
		this.screenTitle = var1.translateKey("eaglercraft.profile.title");
		this.username = new GuiTextField(this, this.fontRenderer, this.width / 2 - 20 + 1, this.height / 6 + 24 + 1 + trans, 138, 20, EaglerProfile.getName());
		this.username.isEnabled = true;
		selectedSlot = EaglerProfile.presetSkinId == -1 ? EaglerProfile.customSkinId : (EaglerProfile.presetSkinId + EaglerProfile.skins.size());
		//this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, "eeeee"));
		this.controlList.add(button0 = new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, var1.translateKey("gui.done")));
		//this.controlList.add(button1 = new GuiButton(2, this.width / 2 - 21, this.height / 6 + 110, 71, 20, var1.translateKey("eaglercraft.profile.addSkin")));
		//this.controlList.add(button2 = new GuiButton(3, this.width / 2 - 21 + 71, this.height / 6 + 110, 72, 20, var1.translateKey("eaglercraft.profile.clearSkin")));
		//this.buttonList.add(new GuiButton(200, this.width / 2, this.height / 6 + 72, 150, 20, var1.translateKey("gui.done")));
	}
	
	private static ModelBiped playerModel = null;
	
	public void drawScreen(int mx, int my, float par3) {
		StringTranslate var1 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
		this.drawString(this.fontRenderer, var1.translateKey("eaglercraft.profile.screenname"), this.width / 2 - 20, this.height / 6 + 8 + trans, 10526880);
		this.drawString(this.fontRenderer, var1.translateKey("eaglercraft.profile.playerSkin"), this.width / 2 - 20, this.height / 6 + 66 + trans, 10526880);
		
		mousex = mx;
		mousey = my;
		
		int skinX = this.width / 2 - 120;
		int skinY = this.height / 6 + 8;
		int skinWidth = 80;
		int skinHeight = 130;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, 0xff000015);
		
		this.username.drawTextBox();
		if(dropDownOpen) {
			super.drawScreen(0, 0, par3);
		}else {
			super.drawScreen(mx, my, par3);
		}
		
		skinX = this.width / 2 - 20;
		skinY = this.height / 6 + 82 + trans;
		skinWidth = 140;
		skinHeight = 22;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 21, skinY + skinHeight - 1, -16777216);
		drawRect(skinX + skinWidth - 20, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		gui.bindTexture();
		drawTexturedModalRect(skinX + skinWidth - 18, skinY + 3, 0, 0, 16, 16);
		
		this.fontRenderer.drawStringWithShadow(dropDownOptions[selectedSlot], skinX + 5, skinY + 7, 14737632);

		skinX = this.width / 2 - 20;
		skinY = this.height / 6 + 103 + trans;
		skinWidth = 140;
		skinHeight = (this.height - skinY - 10);
		slotsVisible = (skinHeight / 10);
		if(slotsVisible > dropDownOptions.length) slotsVisible = dropDownOptions.length;
		skinHeight = slotsVisible * 10 + 7;
		skinsHeight = skinHeight;
		if(scrollPos == -1) {
			scrollPos = selectedSlot - 2;
		}
		if(scrollPos > (dropDownOptions.length - slotsVisible)) {
			scrollPos = (dropDownOptions.length - slotsVisible);
		}
		if(scrollPos < 0) {
			scrollPos = 0;
		}
		if(dropDownOpen) {
			drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
			drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
			for(int i = 0; i < slotsVisible; i++) {
				if(i + scrollPos < dropDownOptions.length) {
					if(selectedSlot == i + scrollPos) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x77ffffff);
					}else if(mx >= skinX && mx < (skinX + skinWidth - 10) && my >= (skinY + i*10 + 5) && my < (skinY + i*10 + 15)) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x55ffffff);
					}
					this.fontRenderer.drawStringWithShadow(dropDownOptions[i + scrollPos], skinX + 5, skinY + 5 + i*10, 14737632);
				}
			}
			int scrollerSize = skinHeight * slotsVisible / dropDownOptions.length;
			int scrollerPos = skinHeight * scrollPos / dropDownOptions.length;
			drawRect(skinX + skinWidth - 4, skinY + scrollerPos + 1, skinX + skinWidth - 1, skinY + scrollerPos + scrollerSize, 0xff888888);
		}
		
		int xx = this.width / 2 - 80;
		int yy = this.height / 6 + 130;
		skinX = this.width / 2 - 120;
		skinY = this.height / 6 + 8;
		skinWidth = 80;
		skinHeight = 130;
		
		int id = selectedSlot - EaglerProfile.skins.size();
		
		if(id < 0) {
			Minecraft.getMinecraft().renderEngine.bindTexture(EaglerProfile.skins.get(selectedSlot).glTex);
		}else {
			defaultOptionsTextures[id].bindTexture();
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) xx, (float) (yy - 80), 100.0F);
		GL11.glScalef(50.0f, 50.0f, 50.0f);
		GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		RenderHelper.enableStandardItemLighting();
		GL11.glScalef(1.0F, -1.0F, 1.0F);
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(((yy - my) * -0.06f), 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(((xx - mx) * 0.06f), 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(0.0F, -1.0F, 0.0F);
		
		if(playerModel == null) {
			playerModel = new ModelBiped(0.0f);
		}
		
		playerModel.render(null, 0.0f, 0.0f, (float)(System.currentTimeMillis() % 100000) / 50f, ((xx - mx) * 0.06f), ((yy - my) * -0.1f), 0.0625F);

		RenderHelper.disableStandardItemLighting();
		
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glColor4f(1f, 1f, 1f, 1f); 
		
	}
	
	public void handleMouseInput() {
		super.handleMouseInput();
		if(dropDownOpen) {
			int var1 = Mouse.getEventDWheel();
			if(var1 < 0) {
				scrollPos += 3;
			}
			if(var1 > 0) {
				scrollPos -= 3;
			}
			if(scrollPos < 0) {
				scrollPos = 0;
			}
			if(scrollPos > defaultOptions.length + EaglerProfile.skins.size()) {
				scrollPos = defaultOptions.length + EaglerProfile.skins.size();
			}
		}
	}
	
	private void save() {
		EaglerProfile.setName(this.username.getText().length() <= 0 ? EaglerProfile.getName() : this.username.getText());
		EaglerProfile.presetSkinId = selectedSlot - EaglerProfile.skins.size();
		if (EaglerProfile.presetSkinId < 0) {
			EaglerProfile.presetSkinId = -1;
			EaglerProfile.customSkinId = selectedSlot;
		} else {
			EaglerProfile.customSkinId = -1;
		}
		EaglerProfile.save();
	}
	
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(!dropDownOpen) {
			if(par1GuiButton.id == 200) {
				save();
				this.mc.displayGuiScreen(parent);
			}else if(par1GuiButton.id == 2) {
				EagRuntime.displayFileChooser("image/png", "png");
			}else if(par1GuiButton.id == 3) {
				for(EaglerProfileSkin i : EaglerProfile.skins) {
					this.mc.renderEngine.deleteTexture(i.glTex);
				}
				EaglerProfile.skins.clear();
				this.dropDownOptions = defaultOptions;
				this.selectedSlot = 0;
				save();
			}
		}
	}
	
	public void updateScreen() {
		this.username.updateCursorCounter();
		
		if(dropDownOpen) {
			if(Mouse.isButtonDown(0)) {
				int skinX = this.width / 2 - 20;
				int skinY = this.height / 6 + 103;
				int skinWidth = 140;
				if(mousex >= (skinX + skinWidth - 10) && mousex < (skinX + skinWidth) && mousey >= skinY && mousey < (skinY + skinsHeight)) {
					dragging = true;
				}
				if(dragging) {
					int scrollerSize = skinsHeight * slotsVisible / dropDownOptions.length;
					scrollPos = (mousey - skinY - (scrollerSize / 2)) * dropDownOptions.length / skinsHeight;
				}
			}else {
				dragging = false;
			}
		}else {
			dragging = false;
		}
		
		if(EagRuntime.fileChooserHasResult()) {
			FileChooserResult result = EagRuntime.getFileChooserResult();
			if(result != null) {
				ImageData loadedSkin = ImageData.loadImageFile(result.fileData, ImageData.getMimeFromType(result.fileName));
				if(loadedSkin != null) {
					boolean isLegacy = loadedSkin.width == 64 && loadedSkin.height == 32;
					boolean isModern = loadedSkin.width == 64 && loadedSkin.height == 64;
					if(isLegacy) {
						ImageData newSkin = new ImageData(64, 64, true);
						SkinConverter.convert64x32to64x64(loadedSkin, newSkin);
						loadedSkin = newSkin;
						isModern = true;
					}
					if(isModern) {
						byte[] rawSkin = new byte[16384];
						for(int i = 0, j, k; i < 4096; ++i) {
							j = i << 2;
							k = loadedSkin.pixels[i];
							rawSkin[j] = (byte)(k >>> 24);
							rawSkin[j + 1] = (byte)(k >>> 16);
							rawSkin[j + 2] = (byte)(k >>> 8);
							rawSkin[j + 3] = (byte)(k & 0xFF);
						}
						for(int y = 20; y < 32; ++y) {
							for(int x = 16; x < 40; ++x) {
								rawSkin[(y << 8) | (x << 2)] = (byte)0xff;
							}
						}
						int k;
						if((k = EaglerProfile.addSkin(result.fileName.substring(0, 32), rawSkin)) != -1) {
							selectedSlot = k;
							reconcatDD();
							save();
						}
					}else {
						EagRuntime.showPopup("The selected image '" + result.fileName + "' is not the right size!\nEaglercraft only supports 64x32 or 64x64 skins");
					}
				}else {
					EagRuntime.showPopup("The selected file '" + result.fileName + "' is not a supported format!");
				}
			}
		}
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	protected void keyTyped(char par1, int par2) {
		this.username.textboxKeyTyped(par1, par2);
		
		String text = username.getText();
		if (text == null) text = "";
		if(text.length() > 16) text = text.substring(0, 16);
		text = text.replaceAll("[^A-Za-z0-9\\-_]", "_");
		this.username.setText(text);
		
		if(par2 == 200 && selectedSlot > 0) {
			--selectedSlot;
			scrollPos = selectedSlot - 2;
		}
		if(par2 == 208 && selectedSlot < (dropDownOptions.length - 1)) {
			++selectedSlot;
			scrollPos = selectedSlot - 2;
		}
	}
	
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.username.mouseClicked(par1, par2, par3);
		
		if (par3 == 0) {
			int skinX = this.width / 2 + 140 - 40;
			int skinY = this.height / 6 + 82 + trans;
		
			if(par1 >= skinX && par1 < (skinX + 20) && par2 >= skinY && par2 < (skinY + 22)) {
				dropDownOpen = !dropDownOpen;
			}
			
			skinX = this.width / 2 - 20;
			int skinWidth = 140;
			int skinHeight = skinsHeight;
			
			if(!(par1 >= skinX && par1 < (skinX + skinWidth) && par2 >= skinY && par2 < (skinY + skinHeight + 22))) {
				dropDownOpen = false;
				dragging = false;
			}
			
			skinY += 21;
			
			if(dropDownOpen && !dragging) {
				for(int i = 0; i < slotsVisible; i++) {
					if(i + scrollPos < dropDownOptions.length) {
						if(selectedSlot != i + scrollPos) {
							if(par1 >= skinX && par1 < (skinX + skinWidth - 10) && par2 >= (skinY + i*10 + 5) && par2 < (skinY + i*10 + 15) && selectedSlot != i + scrollPos) {
								selectedSlot = i + scrollPos;
								dropDownOpen = false;
								dragging = false;
							}
						}
					}
				}
			}
			
		}
	}
	
	
}