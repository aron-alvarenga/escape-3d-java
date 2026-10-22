package com.aronalvarenga.escape;

import java.awt.event.KeyEvent;

import com.aronalvarenga.escape.entities.*;
import com.aronalvarenga.escape.level.Level;
import com.aronalvarenga.escape.level.block.LadderBlock;
import com.aronalvarenga.escape.menu.*;

public class Game {
	public int time;
	public Level level;
	public Player player;
	public int pauseTime;
	public Menu menu;

	public Game() {
		setMenu(new MainMenu());
	}

	public void newGame() {
		Level.clear();
		level = Level.loadLevel(this, "start");

		player = new Player();
		player.level = level;
		level.player = player;
		player.x = level.xSpawn;
		player.z = level.ySpawn;
		player.health = 20;
		level.addEntity(player);
		player.rot = Math.PI + 0.4;
	}

	public void switchLevel(String name, int id) {
		pauseTime = 30;
		level.removeEntityImmediately(player);
		level = Level.loadLevel(this, name);
		level.findSpawn(id);
		player.x = level.xSpawn;
		player.z = level.ySpawn;
		((LadderBlock) level.getBlock(level.xSpawn, level.ySpawn)).wait = true;
		player.x += Math.sin(player.rot) * 0.2;
		player.z += Math.cos(player.rot) * 0.2;
		level.addEntity(player);
	}

	public void tick(boolean[] keys) {
		// Quando um menu está aberto, processar apenas o menu
		if (menu != null) {
			boolean up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_NUMPAD8];
			boolean down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_NUMPAD2];
			boolean left = keys[KeyEvent.VK_A];
			boolean right = keys[KeyEvent.VK_D];
			boolean use = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_ENTER];
			keys[KeyEvent.VK_W] = keys[KeyEvent.VK_UP] = keys[KeyEvent.VK_NUMPAD8] = false;
			keys[KeyEvent.VK_S] = keys[KeyEvent.VK_DOWN] = keys[KeyEvent.VK_NUMPAD2] = false;
			keys[KeyEvent.VK_A] = false;
			keys[KeyEvent.VK_D] = false;
			keys[KeyEvent.VK_SPACE] = false;
			keys[KeyEvent.VK_ENTER] = false;
			menu.tick(this, up, down, left, right, use);
			return;
		}

		if (pauseTime > 0) {
			pauseTime--;
			return;
		}

		time++;

		boolean strafe = keys[KeyEvent.VK_CONTROL] || keys[KeyEvent.VK_ALT] || keys[KeyEvent.VK_ALT_GRAPH] || keys[KeyEvent.VK_SHIFT];

		boolean lk = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_NUMPAD4];
		boolean rk = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_NUMPAD6];

		boolean up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_NUMPAD8];
		boolean down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_NUMPAD2];
		boolean left = keys[KeyEvent.VK_A] || (strafe && lk);
		boolean right = keys[KeyEvent.VK_D] || (strafe && rk);

		boolean turnLeft = keys[KeyEvent.VK_Q] || (!strafe && lk);
		boolean turnRight = keys[KeyEvent.VK_E] || (!strafe && rk);

		boolean use = keys[KeyEvent.VK_SPACE];

		for (int i = 0; i < 8; i++) {
			if (keys[KeyEvent.VK_1 + i]) {
				keys[KeyEvent.VK_1 + i] = false;
				player.selectedSlot = i;
				player.itemUseTime = 0;
			}
		}

		if (keys[KeyEvent.VK_ESCAPE]) {
			keys[KeyEvent.VK_ESCAPE] = false;
			setMenu(new PauseMenu());
		}

		if (keys[KeyEvent.VK_P]) {
			keys[KeyEvent.VK_P] = false;
			setMenu(new PauseMenu());
		}

		if (use) {
			keys[KeyEvent.VK_SPACE] = false;
		}

		player.tick(up, down, left, right, turnLeft, turnRight);
		if (use) {
			player.activate();
		}

		level.tick();

		if (keys[KeyEvent.VK_Z]) {
			player.attack = true;
		}

		if (player.health <= 0) {
			this.newGame();
		}
	}

	public void getLoot(Item item) {
		player.addLoot(item);
	}

	public void win(Player player) {
		
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public void lose(Player player) {

	}

	public void resetToMainMenu() {
		Level.clear();
		level = null;
		player = null;
		setMenu(new MainMenu());
	}
}
