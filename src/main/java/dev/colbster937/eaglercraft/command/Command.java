package dev.colbster937.eaglercraft.command;

import dev.colbster937.eaglercraft.SingleplayerCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.src.StringTranslate;

public class Command {
  protected final Minecraft mc;
  
  private final String cmd;
  private final String[] aliases;
  private final String usage;

  public Command(String cmd, String[] aliases, String usage) {
    this.mc = Minecraft.getMinecraft();
    this.cmd = cmd;
    this.aliases = aliases;
    this.usage = usage;
  }

  public boolean isCommand(String commandString) {
    String command = commandString.split(" ")[0];
    if (command.equals(this.cmd))
      return true;
    for (String alias : this.aliases)
      if (command.equals(alias))
        return true;
    return false;
  }

  public void run(String[] args) {
  }

  public void showUsage(String cmd) {
    SingleplayerCommands.showChat(StringTranslate.getInstance().translateKeyFormat("eaglercraft.command.usage", cmd, this.usage));
  }

  public String getCommand() {
    return this.cmd;
  }

  public String[] getAliases() {
    return this.aliases;
  }
}