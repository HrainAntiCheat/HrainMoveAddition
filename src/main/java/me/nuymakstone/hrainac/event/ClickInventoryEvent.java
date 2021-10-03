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

public class ClickInventoryEvent extends Event {

    private int slot;
    private int windowID;
    private int mode;
    private int button;

    public ClickInventoryEvent(Player p, HrainACPlayer pp, int slot, int windowID, int mode, int button, WrappedPacket wPacket) {
        super(p, pp, wPacket);
        this.slot = slot;
        this.windowID = windowID;
        this.mode = mode;
        this.button = button;
    }

    @Override
    public void resync() {
        if(Event.allowedToResync(pp)) {
            p.updateInventory();
        }
    }

    public int getSlot() {
        return slot;
    }

    public int getWindowID() {
        return windowID;
    }

    public int getMode() {
        return mode;
    }

    public int getButton() {
        return button;
    }
}
