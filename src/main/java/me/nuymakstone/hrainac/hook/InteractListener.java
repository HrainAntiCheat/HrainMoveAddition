package me.nuymakstone.hrainac.hook;

import me.nuymakstone.hrainac.HrainMoveAddition;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import me.nuymakstone.hrainac.HrainMoveAddition.*;

public class InteractListener implements Listener {
    @EventHandler
    public void onShoot(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.BOW) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                //If item == Material.BOW
                HrainMoveAddition.shoot.put(e.getPlayer(),true);
            }else{
                HrainMoveAddition.shoot.put(e.getPlayer(),false);
            }
        }
    }
}
