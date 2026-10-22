package com.aronalvarenga.escape.menu;

import java.awt.Graphics;

import com.aronalvarenga.escape.*;
import com.aronalvarenga.escape.gui.Bitmap;

public class Menu {
	public int time = 0;

	public void render(Bitmap target) {
	}

	public void renderWithGraphics(Graphics g, int width, int height) {
	}

	public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
		time++;
	}
}