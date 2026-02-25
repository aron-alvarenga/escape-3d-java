package com.aronalvarenga.escape.level.block;

import com.aronalvarenga.escape.entities.*;

public class WinBlock extends Block {
	public void addEntity(Entity entity) {
		super.addEntity(entity);
		if (entity instanceof Player) {
			((Player)entity).win();
		}
	}
}
