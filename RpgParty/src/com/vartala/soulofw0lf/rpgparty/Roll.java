package com.vartala.soulofw0lf.rpgparty;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;



public class Roll {

	//Lists Of Players In Roll, Rolled Players, and Items
	private List<String> pList = new ArrayList<>();
	private List<String> rList = new ArrayList<>();
	private List<ItemStack> pendingList = new ArrayList<>();

	//Roll Checks
	private boolean isRunning = false;
	private BukkitTask task = null;

	//Variables For Rolling
	private Inventory inv = Bukkit.createInventory(null, 9, "Rolling For:");
	private Player hPlayer = null;
	private int hRoll = 0;

	/*Constructor For Roll
	 * 
	 */
	public Roll(ItemStack itm, List<String> list) {
		pList.clear();
		pList = list;
		addItem(itm);
	}

	/*Constructor For Roll
	 * 
	 */
	public Roll(List<String> list) {
		pList.clear();
		pList = list;

	}

	/*Adds ItemStack To PendingList
	 * 
	 */
	public void addItem(ItemStack item){
		if(item == null)
			return;
		this.pendingList.add(item);
		loadItems();
		checkRoll();

	}

	/*Sets The Player List For Roll
	 * -Player List Is The Players Able To Roll
	 * And Get Notifications On The Roll
	 */
	public void setPlayers(List<String> list){
		pList.clear();
		for(String name : list){
			pList.add(name);
			
		}
	}

	/*Rolls A Random Number For Player
	 * 
	 */
	public void doRoll(Player p){
		if(!isRunning)
			return;
		if(!pList.contains(p.getName()) && !rList.contains(p.getName()))
			return;
		if(rList.contains(p.getName())){
			p.sendMessage("You've already rolled! Don't be greedy!");
			return;
		}
		rList.add(p.getName());
		p.closeInventory();
		int temp = Util.getRand();
		if(temp > hRoll){
			hPlayer = p;
			hRoll = temp;
		}
		PlayerManager.sendMultiMessageS(pList, p.getName() + " Rolled: " + temp);
		if(pList.size() == rList.size())
			RollDone();
	}

	/*Player Passes On An Item
	 * 
	 */
	public void doPass(Player p){
		if(!pList.contains(p.getName()) || rList.contains(p.getName()))
			return;
		PlayerManager.sendMultiMessageS(pList, p.getName() + " Passed ");
		p.closeInventory();
		rList.add(p.getName());
		if(pList.size() == rList.size())
			RollDone();
	}

	/*Show The Roll inventory To A Player
	 * 
	 */
	public void doInfo(Player p){
		if(!pList.contains(p.getName()))
			return;
		p.openInventory(inv);
	}

	/*Called When A Roll Finishes
	 * Etc. Timer or All Uses Rolled
	 */
	public void RollDone(){
		this.task.cancel();
		PlayerManager.sendMultiMessageS(this.pList, "Rolling Has Ended!!");
		if(hPlayer != null){
			hPlayer.getInventory().addItem(this.pendingList.get(0));
			PlayerManager.sendMultiMessageS(this.pList, hPlayer.getName() + " Has Won!");
		}
		resetRoll();
	}

	public List<String> getPlayers(){return pList;}

	/*Check If There Is No Current Roll
	 * If So It Initializes A New Roll
	 */
	private void checkRoll(){
		if(pendingList.size() > 0 && !isRunning){
			isRunning = true;
			if(task != null)
				task.cancel();
			task = new RollTimer(this).runTaskLater(RpgParty.plugin, RpgParty.rollTimer * 20);
			PlayerManager.sendMultiMessageS(pList, "Info, Roll, or Pass for " + pendingList.get(0).getType().toString());
			for(String name : pList){
				if (RpgParty.autoroll == true){
					ActionManager.doInfo(Bukkit.getPlayer(name));
				}
			}
		}
	}

	/*Resets Roll Variables For Next Roll
	 * 
	 */
	private void resetRoll(){
		this.inv.remove(pendingList.get(0));
		pendingList.remove(0);
		loadItems();
		hPlayer = null;
		hRoll = 0;
		rList.clear();
		task.cancel();
		isRunning = false;
		new BukkitRunnable(){
			public void run(){
		checkRoll();
		cancel();
			}
		}.runTaskLater(RpgParty.plugin, 10);
	}

	/*Loads The PendingItems Into The Roll Inventory
	 * 
	 */
	private void loadItems(){
		inv.clear();
		if(this.pendingList.size() < 9)
			for(ItemStack itm : pendingList)
				inv.addItem(itm);
		else
			for(int i = 0; i<9; i++)
				inv.addItem(pendingList.get(i));
	}

}
