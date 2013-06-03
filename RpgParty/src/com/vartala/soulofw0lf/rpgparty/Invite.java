package com.vartala.soulofw0lf.rpgparty;

import org.bukkit.entity.Player;

public class Invite implements Runnable {
	
	private Player leader;
	private Player user;
	
	private Thread thread = null;
	private boolean isRunning = false;
	
	public Invite(Player leader, Player user) {
		this.leader = leader;
		this.user = user;
		Util.sendMessage(user, this.leader.getName() + " Has Invited You To A Party.");
		this.isRunning = true;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public void acceptInvite(){
		isRunning = false;
		Party party = Util.getParty(leader.getName());
		if(party == null ){
			Util.sendMessage(user, "Party No Longer Exists");
			RpgParty.iList.remove(this);
			return;
		}
		party.addPlayer(user);
		Util.sendMessage(user, "Accepted Invitation.");
		RpgParty.iList.remove(this);
	}
	
	public void denyInvite(){
		isRunning = false;
		Party party = Util.getParty(leader.getName());
		if(party != null){
			Util.sendMessage(party.getMembers(), user.getName() + " Denied Invitation.");
			if(party.getMembers().size() == 1 && !Util.hasPlayerInvited(this.leader.getName()))
				party.removePlayer(this.leader);
		}
		Util.sendMessage(user, "Denied Invivitation.");
		
		RpgParty.iList.remove(this);
	}
	
	public String getUser(){return this.user.getName();}
	
	public String getLeader(){return this.leader.getName();}

	@Override
	public void run() {
		try {
			int cd = 30;
			while(cd > 0){
				Thread.sleep(1000);
				cd--;
				if(!isRunning)
					return;
			}
			denyInvite();
			Util.sendMessage(user, "Invitation Timed Out.");
		} catch (Exception e) {
		}
	}

}
