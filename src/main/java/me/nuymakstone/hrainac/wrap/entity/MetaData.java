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

package me.nuymakstone.hrainac.wrap.entity;

public class MetaData {

    private Type type;
    private boolean value;

    public MetaData(Type type, boolean value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public boolean getValue() {
        return value;
    }

    public enum Type {
        USE_ITEM,
        SPRINT,
        SNEAK
    }
}
