package dev.colbster937.eaglercraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.Mouse;
import net.minecraft.src.GuiScreen;
import net.peyton.eagler.minecraft.TextureLocation;

public class GuiScreenYap extends GuiScreen {
  private static final int visibleLines = 21;
  private static final int wrapWidth = 312;
  private static final TextureLocation ackbk = new TextureLocation("/eagler/demo_bg.png");
  private static final TextureLocation beaconx = new TextureLocation("/eagler/beacon.png");
  private GuiScreen parentScreen;
  private Boolean renderParent;
  private String txtFile;
  private ArrayList<String> ackLines;
  private int scrollPosition = 0;
  private int dragstart = -1;
  private int dragstartI = -1;
  private int mousey = 0;

  public GuiScreenYap(GuiScreen parentScreen, Boolean renderParent, String txtFile) {
    this.parentScreen = parentScreen;
    this.renderParent = renderParent;
    this.txtFile = txtFile;
  }

  public void initGui() {
    List<String> lines = EagRuntime.getRequiredResourceLines(this.txtFile);

    this.ackLines = new ArrayList<>();

    for (String line : lines) {
      if (line == null || line.isEmpty()) {
        this.ackLines.add("");
        continue;
      }
      String current = "   ";
      for (String word : line.split(" ")) {
        String cand = current.isEmpty() ? word : current + " " + word;
        if (this.fontRenderer.getStringWidth(cand) <= wrapWidth) {
          current = cand;
        } else {
          if (!current.isEmpty()) {
            this.ackLines.add(current);
          }
          current = word;
          if (this.fontRenderer.getStringWidth(current) > wrapWidth) {
            int start = 0;
            while (start < current.length()) {
              int lo = start + 1, hi = current.length(), best = lo;
              while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                int w = this.fontRenderer.getStringWidth(current.substring(start, mid));
                if (w <= wrapWidth) {
                  best = mid;
                  lo = mid + 1;
                } else {
                  hi = mid - 1;
                }
              }
              this.ackLines.add(current.substring(start, best));
              start = best;
            }
            current = "";
          }
        }
      }
      if (!current.isEmpty())
        this.ackLines.add(current);
    }
  }

  public void drawScreen(int var1, int var2, float var3) {
    mousey = var2;
    if (this.renderParent) {
      this.parentScreen.drawScreen(0, 0, var3);
      this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
    } else {
      this.drawDefaultBackground();
    }
    int x = (this.width - 345) / 2;
    int y = (this.height - 230) / 2;
    ackbk.bindTexture();
    GL11.glPushMatrix();
    GL11.glTranslatef(x, y, 0.0f);
    GL11.glScalef(1.39f, 1.39f, 1.39f);
    this.drawTexturedModalRect(0, 0, 0, 0, 248, 166);
    GL11.glPopMatrix();
    beaconx.bindTexture();
    this.drawTexturedModalRect(x + 323, y + 7, 114, 223, 13, 13);
    int lines = this.ackLines.size();
    if (scrollPosition < 0)
      scrollPosition = 0;
    if (scrollPosition + visibleLines > lines)
      scrollPosition = lines - visibleLines;
    for (int i = 0; i < visibleLines; ++i) {
      this.fontRenderer.drawString(this.ackLines.get(scrollPosition + i), x + 10, y + 10 + (i * 10), 0x404060);
    }
    int trackHeight = 193;
    int offset = trackHeight * scrollPosition / lines;
    drawRect(x + 326, y + 27, x + 334, y + 220, 0x33000020);
    drawRect(x + 326, y + 27 + offset, x + 334, y + 27 + (visibleLines * trackHeight / lines) + offset + 1, 0x66000000);
  }

  public void updateScreen() {
    if (this.renderParent) {
      this.parentScreen.updateScreen();
    }
    if (Mouse.isButtonDown(0) && dragstart > 0) {
      int trackHeight = 193;
      scrollPosition = (mousey - dragstart) * this.ackLines.size() / trackHeight + dragstartI;
      if (scrollPosition < 0)
        scrollPosition = 0;
      if (scrollPosition + visibleLines > this.ackLines.size())
        scrollPosition = this.ackLines.size() - visibleLines;
    } else {
      dragstart = -1;
    }
  }

  protected void mouseClicked(int par1, int par2, int par3) {
    int x = (this.width - 345) / 2;
    int y = (this.height - 230) / 2;
    if (par1 >= (x + 323) && par1 <= (x + 323 + 13) && par2 >= (y + 7) && par2 <= (y + 7 + 13)) {
      this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
      this.mc.displayGuiScreen(this.parentScreen);
    }
    int trackHeight = 193;
    int offset = trackHeight * scrollPosition / this.ackLines.size();
    if (par1 >= (x + 326) && par1 <= (x + 334) && par2 >= (y + 27 + offset)
        && par2 <= (y + 27 + offset + (visibleLines * trackHeight / this.ackLines.size()) + 1)) {
      dragstart = par2;
      dragstartI = scrollPosition;
    }
  }

  public void handleMouseInput() {
    super.handleMouseInput();
    int var1 = Mouse.getEventDWheel();
    if (var1 < 0) {
      scrollPosition += 3;
    }
    if (var1 > 0) {
      scrollPosition -= 3;
    }
  }
}
