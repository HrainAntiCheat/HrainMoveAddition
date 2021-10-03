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

package me.nuymakstone.hrainac.check.interaction.terrain;

import me.nuymakstone.hrainac.check.BlockInteractionCheck;
import me.nuymakstone.hrainac.event.InteractWorldEvent;
import org.bukkit.util.Vector;

public class FabricatedBlockInteract extends BlockInteractionCheck {

    public FabricatedBlockInteract() {
        super("fabricatedblockinteract", true, 0, 2, 0.999, 5000, "%player% 没能绕过 fabricated block interact, VL: %vl%", null);
    }

    @Override
    protected void check(InteractWorldEvent e) {

        if(e.getTargetedBlockFace() == InteractWorldEvent.BlockFace.INVALID) {
            fail(e);
            return;
        }

        Vector cursorPos = e.getCursorPositionOnTargetedBlock();
        //Disable these for now; currently there is no efficient way to get block hitboxes.
        //boolean onFace = false;
        for(double value : new double[] {cursorPos.getX(), cursorPos.getY(), cursorPos.getZ()}) {
            //Unnecessary to do "value % 0.0625 != 0" because the position is actually
            //stored as 3 bytes, not floats. Each represents a multiple of 1/16 of an axis.
            if(value > 1 || value < 0) {
                fail(e);
                return;
            }
            /*
            if(value == 0 || value == 1)
                onFace = true;
            */
        }

        /*
        if(!onFace) {
            fail(e);
            return;
        }
        */

        /*
        Vector cursorPosOffset = cursorPos.clone().subtract(new Vector(0.5, 0.5, 0.5)).multiply(2);
        Vector normal = e.getTargetedBlockFaceNormal();
        double[] cursorPosOffsetArray = new double[] {cursorPosOffset.getX(), cursorPosOffset.getY(), cursorPosOffset.getZ()};
        double[] normalArray = new double[] {normal.getX(), normal.getY(), normal.getZ()};
        boolean onCorrectFace = false;
        for(int i = 0; i < 3; i ++) {
            double normalComponent = normalArray[i];
            double cursorPosComponent = cursorPosOffsetArray[i];
            if(normalComponent == cursorPosComponent) {
                onCorrectFace = true;
                break;
            }
        }

        if(!onCorrectFace) {
            fail(e);
            return;
        }
        */

        reward(e.getHrainACPlayer());

    }

    private void fail(InteractWorldEvent e) {
        punishAndTryCancelAndBlockRespawn(e.getHrainACPlayer(), e);
    }
}
