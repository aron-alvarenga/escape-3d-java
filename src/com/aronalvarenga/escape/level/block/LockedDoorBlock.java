package com.aronalvarenga.escape.level.block;

import com.aronalvarenga.escape.entities.*;
import com.aronalvarenga.escape.level.Level;

public class LockedDoorBlock extends DoorBlock {
	public LockedDoorBlock() {
		tex = 5;
	}

	public boolean use(Level level, Item item) {
		return false;
	}

	public void trigger(boolean pressed) {
		open = pressed;
	}

}
