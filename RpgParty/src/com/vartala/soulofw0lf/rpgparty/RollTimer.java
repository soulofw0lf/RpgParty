package com.vartala.soulofw0lf.rpgparty;

import org.bukkit.scheduler.BukkitRunnable;


public class RollTimer extends BukkitRunnable {
	
	private final Roll roll;
	
	/*Constructor For Roll Timer
	 * 
	 * @param roll This Is The Roll Which Initializes THIS
	 */
	public RollTimer(Roll roll){
		this.roll = roll;
	}

	/*Calls RollDone After Timer
	 * 
	 */
	@Override
	public void run() {
		roll.RollDone();
	}

}
