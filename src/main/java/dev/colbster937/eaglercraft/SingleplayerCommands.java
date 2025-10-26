package dev.colbster937.eaglercraft;

import java.util.ArrayList;

import dev.colbster937.eaglercraft.command.Command;
import dev.colbster937.eaglercraft.command.TeleportCommand;
import net.lax1dude.eaglercraft.profile.EaglerProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.src.StringTranslate;

public class SingleplayerCommands {
  private static final ArrayList<Command> commands;

  public static void processCommand(String commandString) {
    if (!commandString.startsWith("/")) commandString = "/" + commandString;
    String[] args = commandString.replaceAll(" +", " ").split(" ");
    for (Command command : commands) {
      if (command.isCommand(args[0])) {
        command.run(args);
        return;
      }
    }
    showChat(StringTranslate.getInstance().translateKey("eaglercraft.command.unknown"));
  }

  public static void showChat(String msg) {
    Minecraft.getMinecraft().ingameGUI.addChatMessage(msg);
  }

  public static void sendDummyChat(String msg) {
    showChat("<" + EaglerProfile.getName() + "> " + msg);
  }

  static {
    commands = new ArrayList<>();
    commands.add(new TeleportCommand());
  }
}
