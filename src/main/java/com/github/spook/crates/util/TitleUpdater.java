package com.github.spook.crates.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public abstract class TitleUpdater {

  private static Method getHandle, sendPacket;
  private static Field activeContainerField, windowIdField, playerConnectionField;
  private static Constructor<?> chatMessageConstructor, packetPlayOutOpenWindowConstructor;

  static {
    try {
      getHandle = getCraftPlayer().getMethod("getHandle");
      chatMessageConstructor = getNmsClass("ChatMessage").getConstructor(String.class,
          Object[].class);
      Class<?> nmsPlayer = getNmsClass("EntityPlayer");
      activeContainerField = nmsPlayer.getField("activeContainer");
      windowIdField = getNmsClass("Container").getField("windowId");
      playerConnectionField = nmsPlayer.getField("playerConnection");
      packetPlayOutOpenWindowConstructor = getNmsClass("PacketPlayOutOpenWindow").getConstructor(
          Integer.TYPE, String.class, getNmsClass("IChatBaseComponent"), Integer.TYPE);
      sendPacket = getNmsClass("PlayerConnection").getMethod("sendPacket", getNmsClass("Packet"));
      // SeverSpecs.getNmsClass(nmsClassName) can be replaced with Class.forName("net.minecraft.server." + VERSION + "." + nmsClassName)
    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException |
             NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  private static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
    return Class.forName("net.minecraft.server.v1_8_R3." + nmsClassName);
  }

  private static Class<?> getCraftPlayer() throws ClassNotFoundException {
    return Class.forName("org.bukkit.craftbukkit.v1_8_R3." + "entity.CraftPlayer");
  }

  public static void update(final Player p, String title) {
    if (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("container.crafting")) {
      return;
    }
    try {
      Object handle = getHandle.invoke(p);
      Object message = chatMessageConstructor.newInstance(title, new Object[0]);
      Object container = activeContainerField.get(handle);
      Object windowId = windowIdField.get(container);
      Object packet = packetPlayOutOpenWindowConstructor.newInstance(windowId, "minecraft:chest",
          message, p.getOpenInventory().getTopInventory().getSize());
      Object playerConnection = playerConnectionField.get(handle);
      sendPacket.invoke(playerConnection, packet);
    } catch (IllegalArgumentException | IllegalAccessException | InstantiationException |
             InvocationTargetException e) {
      e.printStackTrace();
    }
    p.updateInventory();
  }
}