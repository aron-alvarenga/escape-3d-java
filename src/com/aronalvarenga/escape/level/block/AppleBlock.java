package com.aronalvarenga.escape.level.block;

import com.aronalvarenga.escape.*;
import com.aronalvarenga.escape.entities.Item;
import com.aronalvarenga.escape.entities.Player;
import com.aronalvarenga.escape.gui.Sprite;
import com.aronalvarenga.escape.level.Level;

public class AppleBlock extends Block {
	private boolean open = false;
	private Sprite appleSprite;

	public AppleBlock() {
		tex = 1;
		blocksMotion = true;

		appleSprite = new Sprite(0, 0, 0, 8 * 5 + 0, Art.getCol(0xffff00));
		addSprite(appleSprite);
	}
	
	public void tick() {
		double distance = Math.sqrt((Player.xplayer - x) * (Player.xplayer - x) + (Player.zplayer - y) * (Player.zplayer - y));
		if(distance < 1) {
			System.out.println("Estou perto da ma??!");
			this.sprites.remove(appleSprite);
			blocksMotion = false;
		}
	}

	
	

}