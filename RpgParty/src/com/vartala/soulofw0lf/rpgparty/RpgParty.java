package com.vartala.soulofw0lf.rpgparty;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import rc2k7.plugins.partycreator.teleport.TeleInvite;

public class RpgParty extends JavaPlugin {
	
	public static List<Party> pList = new ArrayList<>();
	public static List<Invite> iList = new ArrayList<>();
	public static List<String> pcList = new ArrayList<>();
	public static List<String> loreList = new ArrayList<>();
	
	public static List<TeleInvite> tiList = new ArrayList<>();
	
	public static int maxPlayers = 4;
	public static int rollTimer = 60;
	public static String lores = null;
	public static Boolean rolls = true;
	public static Boolean ench = true;
	public static Boolean lore = true;
	public static Logger log = null;
	public static JavaPlugin plugin;
	public static Boolean autoroll = false;
	
	public void onEnable() {
		pList = new ArrayList<>();
		iList = new ArrayList<>();
		pcList = new ArrayList<>();
		loreList = new ArrayList<>();
		plugin = this;
		log = Bukkit.getLogger();
		Bukkit.getPluginManager().registerEvents(new Listener(), this);
		saveDefaultConfig();
		loadConfig();
	}
	
	public void onDisable() {
		pList = new ArrayList<>();
		iList = new ArrayList<>();
		pcList = new ArrayList<>();
	}
	
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player && (label.equalsIgnoreCase("party") || label.equalsIgnoreCase("p") || label.equalsIgnoreCase("rparty")) && args.length >= 1){
			Player player = (Player)sender;
			//TELEPORT COMMAND-----------------------------------------------------------------
			if (args[0].equalsIgnoreCase("test")){
				if (player.hasPermission("party.test")){
					String test = getConfig().getString("test");
					test = test.replaceAll("%p", player.getName());
					player.performCommand(test);
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("tp")){
				if(args.length != 2){
					Util.sendMessage(player,  "/party tp <NAME>");
					return true;
				}
				Party party = null;
				if((party = Util.getParty(player.getName())) != null){
					if(!party.getLeader().equals(player.getName())){
						Util.sendMessage(player, "You Are Not The Party Leader.");
						return true;
					}
					if(!player.hasPermission("party.summon")){
						Util.sendMessage(player, ChatColor.RED + "You Do Not Have Permission To Use This Command.");
						return true;
					}
				}else{
					Util.sendMessage(player, "You Are Not In A Party.");
					return true;
				}
				Player b = Bukkit.getPlayer(args[1]);
				if( b == null || player.getName().equals(b.getName())){
					Util.sendMessage(player, "Could Not Find Player.");
					return true;
				}
				Party pB = Util.getParty(b.getName());
				if(pB == null || (party != pB)){
					Util.sendMessage(player, "You Are Not In The Same Party.");
					return true;
				}
				if(Util.getTeleInvite(b.getName()) != null){
					Util.sendMessage(player, "Player Already Has A Teleport Request.");
					return true;
				}
				tiList.add(new TeleInvite(player, b));
				return true;
			}
			//TELEPORTALL COMMAND-----------------------------------------------------------------
			if(args[0].equalsIgnoreCase("tpall")){
				if(args.length != 1){
					Util.sendMessage(player,  "/party tpall");
					return true;
				}
				Party party = null;
				if((party = Util.getParty(player.getName())) != null){
					if(!party.getLeader().equals(player.getName())){
						Util.sendMessage(player, "You Are Not The Party Leader.");
						return true;
					}
					if(!player.hasPermission("party.summonall")){
						Util.sendMessage(player, ChatColor.RED + "You Do Not Have Permission To Use This Command.");
						return true;
					}
				}else{
					Util.sendMessage(player, "You Are Not In A Party.");
					return true;
				}
				for(Player play : party.getMembers()){
					if(Util.getTeleInvite(play.getName()) != null)
						continue;
					if(player.getName().equals(play.getName()))
						continue;
					tiList.add(new TeleInvite(player, play));
				}
				return true;
			}
			//INVITE COMMAND-------------------------------------------------------------------
			if(args[0].equalsIgnoreCase("invite")){
				if(!player.hasPermission("party.invite")){
					Util.sendMessage(player, ChatColor.RED + "You Do Not Have Permission To Use This Command.");
					return true;
				}
				if(args.length != 2){
					Util.sendMessage(player, "/party invite <NAME>");
					return true;
				}
				Party party = null;
				//Checks If Player Is Leader
				if((party = Util.getParty(player.getName())) != null){
					if(!party.getLeader().equals(player.getName())){
						Util.sendMessage(player, "You Are Not The Party Leader.");
						return true;
					}
				}else{
					pList.add(party = new Party(player));
				}
				//Checks If Party Is Max Size
				if(party.getMembers().size() == maxPlayers){
					Util.sendMessage(player, "Your Party Is Full.");
					return true;
				}
				Player b = Bukkit.getPlayer(args[1]);
				//Checks If Target Player Was Not Found Or Is The Current Player
				if(b == null || player == b){
					Util.sendMessage(player, "Player Not Found.");
					if(party.getMembers().size() == 1 && !Util.hasPlayerInvited(player.getName()))
						party.removePlayer(player);
					return true;
				}
				//Checks If Target Player Already Has An Invitation
				if(Util.getInvite(b.getName()) != null){
					Util.sendMessage(player, b.getName() + " Already Has A Pending Invite.");
					return true;
				}
				//Check If Target Player Is Already In A Party
				if(Util.getParty(b.getName()) != null){
					Util.sendMessage(player, b.getName() + " Is Already In A Party.");
					return true;
				}
				iList.add(new Invite(player, b));
				Util.sendMessage(party.getMembers(), b.getName() + " Invited To Party.");
				return true;
				//ACCEPT COMMAND-----------------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("accept")){
				Invite invite = null;
				TeleInvite tinvite = null;
				if((invite = Util.getInvite(player.getName())) == null && ((tinvite = Util.getTeleInvite(player.getName()))) == null){
					Util.sendMessage(player, "You Do Not Have Any Pending Invites.");
					return true;
				}
				if(invite != null)
					invite.acceptInvite();
				if(tinvite != null)
					tinvite.accept();
				return true;
				//DENY COMMAND-------------------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("deny")){
				Invite invite = null;
				TeleInvite tinvite = null;
				if((invite = Util.getInvite(player.getName())) == null && ((tinvite = Util.getTeleInvite(player.getName()))) == null){
					Util.sendMessage(player, "You Do Not Have Any Pending Invites.");
					return true;
				}
				if(invite != null)
					invite.denyInvite();
				if(tinvite != null)
					tinvite.deny();
				return true;
				//KICK COMMAND-------------------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("kick")){
				if(args.length != 2){
					Util.sendMessage(player, "/party kick <NAME>");
					return true;
				}
				Party p = null;
				if((p = Util.getParty(player.getName())) != null){
					p = Util.getParty(player.getName());
					if(!p.getLeader().equals(player.getName())){
						Util.sendMessage(player, "You Are Not The Party Leader.");
						return true;
					}
					Player b = Bukkit.getPlayer(args[1]);
					if(Util.getPlayerFromList(p.getMembers(), b.getName()) == null){
						Util.sendMessage(player, b.getName() + " Is Not In The Party.");
						return true;
					}
					p.removePlayer(b);
					Util.sendMessage(b, "You Were Kicked From The Party.");
					if(pcList.contains(b.getName()))
						pcList.remove(b.getName());
					return true;
				}else{
					Util.sendMessage(player, "You Are Not In A Party.");
				}
				//PROMOTE COMMAND----------------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("promote")){
				if(args.length != 2){
					Util.sendMessage(player, "/party promote <NAME>");
					return true;
				}
				Party party = null;
				if((party = Util.getParty(player.getName())) != null){
					if(!party.getLeader().equals(player.getName())){
						Util.sendMessage(player, "You Are Not The Party Leader.");
						return true;
					}
					Player b = Bukkit.getPlayer(args[1]);
					if(Util.getPlayerFromList(party.getMembers(), b.getName()) == null){
						Util.sendMessage(player, b.getName() + " Is Not In The Party.");
						return true;
					}
					party.promotePlayer(b);
					return true;
				}else{
					Util.sendMessage(player, "You Are Not In A Party.");
				}
				//CHAT COMMAND-------------------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("chat")){
				if(Util.getParty(player.getName()) == null){
					Util.sendMessage(player, "You Are Not In A Party.");
					return true;
				}
				if(pcList.contains(player.getName())){
					pcList.remove(player.getName());
					Util.sendMessage(player, "Party Chat Disabled.");
					return true;
				}else{
					pcList.add(player.getName());
					Util.sendMessage(player, "Party Chat Enabled.");
					return true;
				}
				//LEAVE COMMAND-----------------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("leave")){
				Party party = null;
				if((party = Util.getParty(player.getName())) == null){
					Util.sendMessage(player, "You Are Not In A Party.");
					return true;
				}
				party.removePlayer(player);
				Util.sendMessage(player, "You Left The Party.");
				return true;
				//RELOAD COMMAND-----------------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("reload")){
				if(!player.isOp())
					return true;
				loadConfig();
				Util.sendMessage(player, "Configs Reloaded.");
				return true;
			}else if(args[0].equalsIgnoreCase("roll")){
				RollerAPI.doRoll(player);
				return true;
			}else if(args[0].equalsIgnoreCase("pass")){
				RollerAPI.doPass(player);
				return true;
			}else if(args[0].equalsIgnoreCase("info")){
				RollerAPI.doInfo(player);
				return true;
			}else {
				return true;
			}
		}
		
		if(label.equalsIgnoreCase("pcc") && sender instanceof Player){
			if(args.length == 0)
				return false;
			Player send = (Player)sender;
			Party party = null;
			if((party = Util.getParty(send.getName())) != null){
				for(Player player : Bukkit.getOnlinePlayers())
					if(Util.getPlayerFromList(party.getMembers(), player.getName()) != null)
						player.sendMessage(send.getName() + " > " + ChatColor.AQUA + Util.combineArray(args));
			}else{
				Util.sendMessage(send, "You Are Not In A Party.");
			}
			return true;
		}
		return false;
	}

	
	public void loadConfig(){
		maxPlayers = getConfig().getInt("MaxPlayers");
		rollTimer = getConfig().getInt("RollDuration");
		rolls = getConfig().getBoolean("Rolls");
		ench = getConfig().getBoolean("Enchant Roll");
		lore = getConfig().getBoolean("Lore Roll");
		autoroll = getConfig().getBoolean("Auto Roll");
		for (String key : getConfig().getConfigurationSection("Lores").getKeys(false)){
			loreList.add(getConfig().getString("Lores." + key));
		}
	}
}
