package com.vartala.soulofw0lf.rpgparty;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class Listener implements org.bukkit.event.Listener {

	@EventHandler
	public void onPlayerDmg(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player a = (Player)event.getEntity();
			Player b = null;
			if(event.getDamager() instanceof Player)
				b = (Player)event.getDamager();
			if(event.getDamager() instanceof Arrow)
				if (((Arrow)event.getDamager()).getShooter() instanceof Player)
					b = (Player)((Arrow)event.getDamager()).getShooter();
			Party p = null;
			if((p = Util.getParty(a.getName())) == null)
				return;
			if(p.getMembers().contains(b))
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void partyChat(AsyncPlayerChatEvent event){
		Player sender = event.getPlayer();
		Party party = null;
		if(RpgParty.pcList.contains(sender.getName())){
			event.setCancelled(true);
			for(Player player : event.getRecipients())
				if((party = Util.getParty(sender.getName())) != null)
					if(Util.getPlayerFromList(party.getMembers(), player.getName()) != null)
						player.sendMessage(sender.getName() + " > " + ChatColor.AQUA + event.getMessage());
		}
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Party party = null;
		if((party = Util.getParty(player.getName())) != null)
			party.removePlayer(player);
		if(RpgParty.pcList.contains(player.getName()))
			RpgParty.pcList.remove(player.getName());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event){
		Player player = event.getPlayer();
		Party party = null;
		if((party = Util.getParty(player.getName())) != null)
			party.removePlayer(player);
		if(RpgParty.pcList.contains(player.getName()))
			RpgParty.pcList.remove(player.getName());
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event){
		if(RpgParty.rolls == true){
			if (RpgParty.lore == true){
				Player player = event.getPlayer();
				ItemStack itm = event.getItem().getItemStack();
				if(itm == null){
				} else {
					if(!(itm.getItemMeta().hasLore())){
					} else {
						for(String strlore : itm.getItemMeta().getLore())
						{
							for(String lores : RpgParty.loreList){
								if(strlore.contains(lores.replaceAll("@", ":")))
								{
									Party party = null;
									if((party = Util.getParty(player.getName())) == null)
										return;
									party.addItem(itm);
									event.getItem().remove();
									event.setCancelled(true);
								}
							}
						}
					}
				}

				if (RpgParty.ench == true){
					if(itm.getEnchantments().isEmpty())
						return;
					Party party = null;
					if((party = Util.getParty(player.getName())) == null)
						return;
					party.addItem(itm);
					event.getItem().remove();
					event.setCancelled(true);

				}
			}
		}
	}

	@EventHandler
	public void onItemClick(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player))
			return;
	//	if(PartyCreator.hasHighRoller){
			Player player = (Player)event.getWhoClicked();
	//		if(itm.getEnchantments().isEmpty())
	//			return;
	//		Party party = null;
	//		if((party = Util.getParty(player.getName())) == null)
	//			return;
	//		if(event.getInventory().getType() != InventoryType.CHEST)
	//			return;
	//		if(event.getCurrentItem().isSimilar(player.getInventory().getItem(event.getSlot())))
	//			return;
			if(event.getInventory().getName().equals("Rolling For:")){
				ItemStack itm = event.getCurrentItem();{
				if(itm == null)
					return;}
				if (event.isLeftClick()){
					RollerAPI.doRoll(player);
					event.setResult(Result.DENY);
				}
				if (event.isRightClick()){
					RollerAPI.doPass(player);
					event.setResult(Result.DENY);
				}
	//			return;
	//	if(event.getInventory().getName().equals("Bank:"))
	//			return;
	//		party.addItem(itm);
	//		event.getInventory().remove(itm);
	//		
			event.setCancelled(true);}
		}
	//}

}
