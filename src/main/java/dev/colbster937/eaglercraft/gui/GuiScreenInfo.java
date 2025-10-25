package dev.colbster937.eaglercraft.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

public class GuiScreenInfo extends GuiScreen {
  private final StringTranslate translate;
  private final GuiScreen parent;
  private final TextLine[] lines;
  private final boolean mm;

  public GuiScreenInfo(GuiScreen parent, TextLine... lines) {
    this.translate = StringTranslate.getInstance();
    this.parent = parent;
    this.lines = lines;
    this.mm = parent == null || parent instanceof GuiMainMenu;
  }

  public GuiScreenInfo(TextLine... lines) {
    this(null, lines);
  }

  @Override
  public void initGui() {
    controlList.add(new GuiButton(0, (width - 200) / 2, height / 4 + 132,
        translate.translateKey(this.mm ? "gui.toMenu" : "gui.done")));
  }

  @Override
  public void drawScreen(int mx, int my, float f) {
    drawDefaultBackground();
    for (int i = 0; i < lines.length; i++) {
      TextLine line = lines[i];
      drawCenteredString(fontRenderer, translate.translateKey(line.text), width / 2, height / 2 - 50 + (i * 10),
          line.color);
    }
    super.drawScreen(mx, my, f);
  }

  @Override
  protected void actionPerformed(GuiButton var1) {
    if (var1.id == 0) {
      if (this.parent == null)
        this.mc.displayGuiScreen(new GuiMainMenu());
      else
        this.mc.displayGuiScreen(parent);
    }

  }

  public static class TextLine {
    private final String text;
    private final int color;

    public TextLine(String text, int color) {
      this.text = text;
      this.color = color;
    }

    public TextLine(String text) {
      this(text, 16777215);
    }
  }
}
