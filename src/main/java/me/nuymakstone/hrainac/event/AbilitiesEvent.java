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

package me.nuymakstone.hrainac.event;

import me.nuymakstone.hrainac.HrainACPlayer;
import me.nuymakstone.hrainac.wrap.packet.WrappedPacket;
import org.bukkit.entity.Player;

/**
 * The vanilla client sends this packet when the player starts/stops
 * flying with the Flags parameter changed accordingly. All other parameters
 * are ignored by the vanilla server. (Wiki.vg)
 **/
public class AbilitiesEvent extends Event {

    private final boolean flying;

    public AbilitiesEvent(Player p, HrainACPlayer pp, boolean flying, WrappedPacket packet) {
        super(p, pp, packet);
        this.flying = flying;
    }

    @Override
    public void postProcess() {
        if (!isCancelled() && isFlying()) {
            pp.setFlyPendingTime(System.currentTimeMillis());
        }
        pp.setFlying(flying);
    }

    public boolean isFlying() {
        return flying;
    }
}
