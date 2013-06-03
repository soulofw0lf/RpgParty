package com.vartala.soulofw0lf.rpgparty;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import rc2k7.plugins.partycreator.teleport.TeleInvite;

public class Util {
	
	private static String stub = ChatColor.GOLD + "Rpg Party: " + ChatColor.WHITE;
	
	public static Party getParty(String name){
		for(Party party : RpgParty.pList)
			if(getPlayerFromList(party.getMembers(), name) != null)
				return party;
		return null;
	}
	private static Random rand = new Random();
	
	public static int getRand(){
		return rand.nextInt(100)+1;
	}

	public static Invite getInvite(String name){
		for(Invite invite : RpgParty.iList)
			if(invite.getUser().equals(name))
			return invite;
		return null;
	}
	
	public static TeleInvite getTeleInvite(String name){
		for(TeleInvite invite : RpgParty.tiList)
			if(invite.getTargetPlayer().equals(name))
				return invite;
		return null;
	}
	
	public static void sendMessage(Player player, String message){
		if(player == null)
			return;
		player.sendMessage(stub + message);
	}
	
	public static void sendMessage(List<Player> list, String message){
		if(list.size() == 0)
			return;
		for(Player player : list)
			player.sendMessage(stub + message);
	}
	
	public static Player getPlayer(String name){
		for(Player player : Bukkit.getOnlinePlayers())
			if(player.getName().equals(name))
				return player;
		return null;
	}
	
	public static Player getPlayerFromList(List<Player> list, String name){
		for(Player player : list)
			if(player.getName().equals(name))
				return player;
		return null;
	}
	
	public static String combineArray(String[] args){
		String tmp = "";
		for(String str : args)
			tmp += str + " ";
		return tmp.substring(0, tmp.length() - 1);
	}
	
	public static boolean hasPlayerInvited(String name){
		for(Invite invite : RpgParty.iList)
			if(invite.getLeader().equals(name))
				return true;
		return false;
	}
	
	public static boolean hasPlayerTeleInvited(String name){
		for(TeleInvite invite : RpgParty.tiList)
			if(invite.getTargetPlayer().equals(name))
				return true;
		return false;
	}

}
