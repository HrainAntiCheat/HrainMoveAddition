/*
 * This file is part of HrainMoveAddition Anticheat.
 * Copyright (C) 2018 HrainMoveAddition Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.nuymakstone.hrainac.module;

import me.nuymakstone.hrainac.HrainMoveAddition;
import me.nuymakstone.hrainac.check.*;
import me.nuymakstone.hrainac.check.combat.FightAccuracy;
import me.nuymakstone.hrainac.check.combat.FightSynchronized;
import me.nuymakstone.hrainac.check.interaction.entity.EntityInteractReach;
import me.nuymakstone.hrainac.check.interaction.inventory.InventoryActions;
import me.nuymakstone.hrainac.check.movement.FabricatedMove;
import me.nuymakstone.hrainac.check.movement.SprintDirection;
import me.nuymakstone.hrainac.check.movement.look.InvalidPitch;
import me.nuymakstone.hrainac.check.movement.position.*;
import me.nuymakstone.hrainac.event.*;
import org.bukkit.entity.Player;

import java.util.*;

public class CheckManager {

    private final Set<UUID> exemptedPlayers;
    private final Set<UUID> forcedPlayers;

    //make these HashSets?
    private final List<Check> checks;
    private final List<BlockDigCheck> blockDigChecks;
    private final List<BlockInteractionCheck> blockInteractionChecks;
    private final List<CustomCheck> customChecks;
    private final List<EntityInteractionCheck> entityInteractionChecks;
    private final List<MovementCheck> movementChecks;

    public CheckManager(HrainMoveAddition HrainAC) {
        Check.setHrainACReference(HrainAC);
        exemptedPlayers = new HashSet<>();
        forcedPlayers = new HashSet<>();
        checks = new ArrayList<>();
        blockDigChecks = new ArrayList<>();
        blockInteractionChecks = new ArrayList<>();
        customChecks = new ArrayList<>();
        entityInteractionChecks = new ArrayList<>();
        movementChecks = new ArrayList<>();
    }

    //initialize checks
    public void loadChecks() {
        new Timer();
        //new FightHitbox();
        //new Phase();
        //new FabricatedMove();
        //new FlyOld();
        new Fly();
        //new Step();
        //new BlockBreakSpeedSurvival();
        //new Inertia();
        //new BlockBreakDirection();
        //new WrongBlock();
        //new FightSynchronized();
        //new FightSynchronized();
        //new FightAccuracy();
         //new AimbotHeuristic();
        //new AntiVelocityBasic();
        //new AntiVelocityJump();
        //new InvalidPitch();
        new EntityInteractReach();
       // new EntityInteractDirection();
        //new BlockInteractDirection();
        //new WrongBlockFace();
        //new ActionToggleSpeed();
        new SmallHop();
        //new FastFall();
        //new SwimVertical();
        //new ClickDuration();
        //new AimbotPrecision();
        //new FightSynchronized();
        //new FightMulti();
        //new AimbotConvergence();
        new Strafe();
       // new NoClip();
        //new FabricatedBlockInteract();
        //new InventoryMove();
        new InventoryActions();
        //new SprintDirection();
    }

    public void unloadChecks() {
        checks.clear();
        blockDigChecks.clear();
        blockInteractionChecks.clear();
        customChecks.clear();
        entityInteractionChecks.clear();
        movementChecks.clear();
    }

    //iterate through appropriate checks
    void dispatchEvent(Event e) {
        for (CustomCheck check : customChecks) {
            check.checkEvent(e);
        }
        if (e instanceof MoveEvent) {
            for (MovementCheck check : movementChecks)
                check.checkEvent((MoveEvent) e);
        } else if (e instanceof InteractEntityEvent) {
            for (EntityInteractionCheck check : entityInteractionChecks)
                check.checkEvent((InteractEntityEvent) e);
        } else if (e instanceof BlockDigEvent) {
            for (BlockDigCheck check : blockDigChecks)
                check.checkEvent((BlockDigEvent) e);
        } else if (e instanceof InteractWorldEvent) {
            for (BlockInteractionCheck check : blockInteractionChecks)
                check.checkEvent((InteractWorldEvent) e);
        }
    }

    public void removeData(Player p) {
        for (Check check : checks)
            check.removeData(p);
    }

    public List<Check> getChecks() {
        return checks;
    }

    public List<BlockDigCheck> getBlockDigChecks() {
        return blockDigChecks;
    }

    public List<BlockInteractionCheck> getBlockInteractionChecks() {
        return blockInteractionChecks;
    }

    public List<CustomCheck> getCustomChecks() {
        return customChecks;
    }

    public List<EntityInteractionCheck> getEntityInteractionChecks() {
        return entityInteractionChecks;
    }

    public List<MovementCheck> getMovementChecks() {
        return movementChecks;
    }

    public Set<UUID> getExemptedPlayers() {
        return exemptedPlayers;
    }

    public Set<UUID> getForcedPlayers() {
        return forcedPlayers;
    }

    public void addExemption(UUID uuid) {
        forcedPlayers.remove(uuid);
        exemptedPlayers.add(uuid);
    }

    public void addForced(UUID uuid) {
        exemptedPlayers.remove(uuid);
        forcedPlayers.add(uuid);
    }

    public void removeExemption(UUID uuid) {
        exemptedPlayers.remove(uuid);
    }

    public void removeForced(UUID uuid) {
        forcedPlayers.remove(uuid);
    }
}
