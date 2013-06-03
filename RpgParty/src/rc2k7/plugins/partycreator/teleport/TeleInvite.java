package rc2k7.plugins.partycreator.teleport;

import org.bukkit.entity.Player;

import com.vartala.soulofw0lf.rpgparty.RpgParty;
import com.vartala.soulofw0lf.rpgparty.Util;


public class TeleInvite implements Runnable {
	
	private Player a;
	private Player b;
	
	private Thread thread = null;
	private boolean isRunning = false;
	
	public TeleInvite(Player a, Player b){
		isRunning = true;
		Util.sendMessage(a, "Teleport Request Sent To " + b.getName());
		Util.sendMessage(b, a.getName() + " Wants To Teleport You.");
		this.a = a;
		this.b = b;
		thread = new Thread(this);
		thread.start();
	}
	
	public void accept(){
		isRunning = false;
		Util.sendMessage(a, b.getName() + " Has Accepted Teleportation");
		Util.sendMessage(a, "Do Not Move For 5 Seconds");
		Util.sendMessage(b, "Do Not Move For 5 Seconds");
		TeleTimer tt = new TeleTimer(a, b);
		new Thread(tt).start();
		RpgParty.tiList.remove(this);
	}
	
	public void deny(){
		isRunning = false;
		Util.sendMessage(a, b.getName() + " Has Denied Teleportation.");
		RpgParty.tiList.remove(this);
	}
	
	public String getTargetPlayer(){ return b.getName(); }
	
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
			deny();
			Util.sendMessage(b, "Teleport Invitation Timed Out.");
		} catch (Exception e) {
		}
	}

}
