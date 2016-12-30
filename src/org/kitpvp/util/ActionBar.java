package org.kitpvp.util;

//import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
//import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
//import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ActionBar {

//	private PacketPlayOutChat packet;
	private String text;

	public ActionBar(String text) {
		this.text = text;
//		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
//		this.packet = packet;
	}

	public void sendToPlayer(Player player) {
		player.sendMessage(text);
//		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public void sendToPlayers(ArrayList<Player> players) {
		for (Player player : players) {
			player.sendMessage(text);
//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void sendToServer() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.sendMessage(text);
//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

}