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

package me.nuymakstone.hrainac.wrap.entity.human;

import me.nuymakstone.hrainac.HrainMoveAddition;
import me.nuymakstone.hrainac.wrap.block.WrappedBlock;
import me.nuymakstone.hrainac.wrap.entity.WrappedEntity7;
import net.minecraft.server.v1_7_R4.EntityHuman;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class WrappedEntityHuman7 extends WrappedEntity7 implements WrappedEntityHuman {

    public WrappedEntityHuman7(Entity entity) {
        super(entity);
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        Object obj = WrappedBlock.getWrappedBlock(block, HrainMoveAddition.getServerVersion()).getNMS();
        net.minecraft.server.v1_7_R4.Block b = (net.minecraft.server.v1_7_R4.Block) obj;
        return ((EntityHuman) nmsEntity).a(b);
    }

    @Override
    public float getCurrentPlayerStrVsBlock(Block block, boolean flag) {
        Object obj = WrappedBlock.getWrappedBlock(block, HrainMoveAddition.getServerVersion()).getNMS();
        net.minecraft.server.v1_7_R4.Block b = (net.minecraft.server.v1_7_R4.Block) obj;
        return ((EntityHuman) nmsEntity).a(b, flag);
    }

    @Override
    public void releaseItem() {
        ((EntityHuman) nmsEntity).bA();
    }

    @Override
    public boolean usingItem() {
        return ((EntityHuman) nmsEntity).by();
    }
}
