package com.vartala.soulofw0lf.rpgparty;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Party {
	
	private List<Player> members = new ArrayList<>();

	public Party(Player leader) {
		this.members.add(leader);
		if(RpgParty.rolls == true){
			List<String> temp = new ArrayList<>();
			for(Player player : this.members)
				temp.add(player.getName());
			RollerAPI.addRoll(new Roll(temp));
		}
	}
	
	public void addPlayer(Player name){
		if(Util.getPlayerFromList(this.members, name.getName()) != null)
			return;
		Util.sendMessage(this.members, name.getName() + " Has Joined The Party.");
		this.members.add(name);
		if(RpgParty.rolls == true){
			List<String> temp = new ArrayList<>();
			for(Player player : this.members)
				temp.add(player.getName());
			RollerAPI.setPlayers(temp, RpgParty.pList.indexOf(this));
		}
	}
	
	public void addItem(ItemStack itm){
		RollerAPI.addItem(itm, RpgParty.pList.indexOf(this));
	}
	
	public void removePlayer(Player name){
		Player tmp = null;
		if((tmp = Util.getPlayerFromList(this.members, name.getName())) == null)
			return;
		this.members.remove(tmp);
		if(RpgParty.rolls == true){
			List<String> temp = new ArrayList<>();
			for(Player player : this.members)
				temp.add(player.getName());
			RollerAPI.setPlayers(temp, RpgParty.pList.indexOf(this));
		}
		Util.sendMessage(this.members, name.getName() + " Has Left The Party.");
		if(this.members.size() == 1){
			if(RpgParty.pcList.contains(this.members.get(0).getName()))
				RpgParty.pcList.remove(this.members.get(0).getName());
			Util.sendMessage(this.members.get(0), "Party Has Been Disbanded.");
			RollerAPI.remRoll(RpgParty.pList.indexOf(this));
			RpgParty.pList.remove(this);
		}
	}
	
	public void promotePlayer(Player name){
		Player tmp = null;
		if((tmp = Util.getPlayerFromList(this.members, name.getName())) == null)
			return;
		this.members.remove(tmp);
		this.members.add(0, name);
		Util.sendMessage(this.members, name.getName() + " Has Been Promoted To Leader.");
	}
	
	public String getLeader(){return this.members.get(0).getName();}
	
	public List<Player> getMembers(){return this.members;}
	
}
