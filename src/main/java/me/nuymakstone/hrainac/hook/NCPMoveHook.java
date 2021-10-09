package me.nuymakstone.hrainac.hook;

import fr.neatmonster.nocheatplus.checks.*;
import fr.neatmonster.nocheatplus.checks.access.*;
import me.nuymakstone.hrainac.HrainMoveAddition;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.*;
import java.util.*;
import fr.neatmonster.nocheatplus.hooks.*;

public class NCPMoveHook implements NCPHook {
    public NCPMoveHook() {
        this.hook();
    }

    public String getHookName() {
        return "NCSHook";
    }

    public String getHookVersion() {
        return "demo-1";
    }

    public boolean onCheckFailure(CheckType checkType, Player p, IViolationInfo vlInfo) {
        if (checkType != CheckType.MOVING_SURVIVALFLY) {
            return false;
        } else if (!vlInfo.willCancel()) {
            return false;
        } else {
            if (HrainMoveAddition.shoot.containsKey(p) && HrainMoveAddition.shoot.containsValue(true)) {
                return true;
            } else {

                player pl = (player) HrainMoveAddition.map.get(p.getUniqueId());
                if (System.currentTimeMillis() - pl.stairbuffer < 1000L) {
                    return pl.buffer;
                } else {
                    pl.stairbuffer = System.currentTimeMillis();
                    boolean trap = false;
                    boolean ice = false;

                    int i;
                    for (int x = -2; x <= 2; ++x) {
                        for (i = -2; i <= 2; ++i) {
                            for (float y = -1.6F; y < 1.0F; y += 0.5F) {
                                String inv = p.getLocation().clone().add((double) x, (double) y, (double) i).getBlock().getType().toString();
                                if (inv.contains("STAIR") || inv.contains("STEP")) {
                                    pl.buffer = true;
                                    HrainMoveAddition.map.put(p.getUniqueId(), pl);
                                    return true;
                                }

                                if (inv.contains("TRAP_DOOR") || inv.contains("TRAPDOOR")) {
                                    trap = true;
                                }

                                if (inv.contains("ICE")) {
                                    ice = true;
                                }
                            }
                        }
                    }

                    if (trap && ice) {
                        pl.buffer = true;
                        HrainMoveAddition.map.put(p.getUniqueId(), pl);
                        return true;
                    } else {
                        List<Entity> entities = p.getNearbyEntities(2.0D, 2.0D, 2.0D);

                        for (i = 0; i < entities.size(); ++i) {
                            Entity ent = (Entity) entities.get(i);
                            if (ent.getType() == EntityType.BOAT) {
                                pl.buffer = true;
                                HrainMoveAddition.map.put(p.getUniqueId(), pl);
                                return true;
                            }
                        }

                        pl.buffer = false;
                        HrainMoveAddition.map.put(p.getUniqueId(), pl);
                        return false;
                    }
                }
            }
        }
    }

    public void hook() {
        NCPHookManager.addHook(CheckType.MOVING_SURVIVALFLY, this);
    }
}

