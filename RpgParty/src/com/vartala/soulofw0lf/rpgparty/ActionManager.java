package com.vartala.soulofw0lf.rpgparty;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ActionManager {
	
	public ActionManager(){}
	
	/*This Does A Roll Action
	 * 
	 * @param p The Player That Is Rolling
	 */
	public static void doRoll(Player p){
		if(!PlayerManager.inRoll(p))
			return;
		Roll r = null;
		if((r = RollManager.getRollFromList(p)) != null)
			r.doRoll(p);
	}
	
	/*This Does A Pass Action
	 * 
	 * @param p The Player That Is Passing
	 */
	public static void doPass(Player p){
		if(!PlayerManager.inRoll(p))
			return;
		Roll r = null;
		if((r = RollManager.getRollFromList(p)) != null)
			r.doPass(p);
	}
	
	/*This Does A Roll Action
	 * 
	 * @param p The Player That Is Rolling
	 */
	public static void doInfo(Player p){
		if(!PlayerManager.inRoll(p))
			return;
		Roll r = null;
		if((r = RollManager.getRollFromList(p)) != null)
			r.doInfo(p);
	}
	
	public static void addItem(ItemStack itm, int i){
		Roll r = RollManager.getRollFromList(i);
		if(r != null)
			r.addItem(itm);
	}

}
