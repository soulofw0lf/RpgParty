package com.vartala.soulofw0lf.rpgparty;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class PlayerManager {
	
	private static String stub = ChatColor.GREEN + "Party Roll: " + ChatColor.WHITE;

	public PlayerManager(){}
	
	/*Checks If Player Is In A Roll
	 * 
	 * 
	 */
	public static boolean inRoll(Player p){
		for(Roll r : RollManager.getRollList())
			for(String l : r.getPlayers())
				if(l.equals(p.getName()))
					return true;
		return false;
	}
	
	public static void setPlayers(List<String> l, int i){
		Roll r = RollManager.getRollFromList(i);
		if(r != null)
			r.setPlayers(l);
	}
	
	public static void sendMultiMessageP(List<Player> list, String message){
		for(Player player : list)
			player.sendMessage(stub + message);
	}
	
	public static void sendMultiMessageP(Player[] list, String message){
		for(Player player : list)
			player.sendMessage(stub + message);
	}
	
	public static void sendMultiMessageS(List<String> list, String message){
		for(Player player : Bukkit.getOnlinePlayers())
			if(list.contains(player.getName()))
				player.sendMessage(stub + message);
	}
	
	public static void sendMultiMessageS(String[] list, String message){
		for(String player : list)
			Bukkit.getServer().getPlayerExact(player).sendMessage(stub + message);
	}
	
}
