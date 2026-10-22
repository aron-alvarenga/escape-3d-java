package com.aronalvarenga.escape.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.aronalvarenga.escape.Game;
import com.aronalvarenga.escape.gui.Bitmap;

public class PauseMenu extends Menu {
	private static final String[] OPTIONS = {
		"Continuar",
		"Voltar para o menu principal",
		"Sair do jogo"
	};
	private int selected = 0;

	@Override
	public void renderWithGraphics(Graphics g, int width, int height) {
		g.setColor(new Color(0, 0, 0, 180));
		g.fillRect(0, 0, width, height);

		int panelW = 400;
		int panelH = 220;
		int panelX = (width - panelW) / 2;
		int panelY = (height - panelH) / 2 - 20;
		g.setColor(new Color(0x22, 0x22, 0x28));
		g.fillRect(panelX, panelY, panelW, panelH);
		g.setColor(new Color(0x11, 0x11, 0x18));
		g.fillRect(panelX + 4, panelY + 4, panelW - 8, panelH - 8);

		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.setColor(new Color(0xaa, 0xaa, 0xaa));
		String title = "PAUSA";
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (width - titleWidth) / 2, panelY + 45);

		g.setFont(new Font("Arial", Font.PLAIN, 18));
		int startY = panelY + 85;
		for (int i = 0; i < OPTIONS.length; i++) {
			String opt = (selected == i ? "> " : "  ") + OPTIONS[i];
			int optWidth = g.getFontMetrics().stringWidth(opt);
			g.setColor(selected == i ? new Color(0xff, 0xff, 0xaa) : new Color(0x88, 0x88, 0x88));
			g.drawString(opt, (width - optWidth) / 2, startY + i * 35);
		}
	}

	@Override
	public void render(Bitmap target) {
		// Overlay escuro semi-transparente (escurece a tela)
		for (int i = 0; i < target.pixels.length; i++) {
			int p = target.pixels[i];
			int r = (p >> 16) & 0xff;
			int g = (p >> 8) & 0xff;
			int b = p & 0xff;
			target.pixels[i] = ((r / 4) << 16) | ((g / 4) << 8) | (b / 4);
		}

		// Painel central
		int panelW = 140;
		int panelH = 70;
		int panelX = (target.width - panelW) / 2;
		int panelY = (target.height - panelH) / 2 - 5;
		target.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0x222228);
		target.fill(panelX + 2, panelY + 2, panelX + panelW - 2, panelY + panelH - 2, 0x111118);

		// Título PAUSA
		String title = "PAUSA";
		int titleX = (target.width - title.length() * 6) / 2;
		target.draw(title, titleX + 1, panelY + 12, 0x222222);
		target.draw(title, titleX, panelY + 11, 0xaaaaaa);

		// Opções
		int startY = panelY + 28;
		for (int i = 0; i < OPTIONS.length; i++) {
			String opt = (selected == i ? "> " : "  ") + OPTIONS[i];
			int x = (target.width - opt.length() * 6) / 2;
			int y = startY + i * 10;
			int color = selected == i ? 0xffffaa : 0x888888;
			target.draw(opt, x + 1, y + 1, 0x222222);
			target.draw(opt, x, y, color);
		}
	}

	@Override
	public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
		if (up) {
			selected = (selected - 1 + OPTIONS.length) % OPTIONS.length;
		}
		if (down) {
			selected = (selected + 1) % OPTIONS.length;
		}
		if (use) {
			if (selected == 0) {
				game.setMenu(null);
			} else if (selected == 1) {
				game.resetToMainMenu();
			} else if (selected == 2) {
				System.exit(0);
			}
		}
	}
}
