package com.aronalvarenga.escape.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.aronalvarenga.escape.Game;
import com.aronalvarenga.escape.gui.Bitmap;

public class MainMenu extends Menu {
	private static final String[] OPTIONS = { "Iniciar", "Sair" };
	private int selected = 0;

	@Override
	public void renderWithGraphics(Graphics g, int width, int height) {
		// Desenha um fundo gradiente
		for (int i = 0; i < height; i++) {
			float ratio = (float) i / height;
			int r = (int) (0x11 * (1 - ratio) + 0x05 * ratio);
			int ge = (int) (0x11 * (1 - ratio) + 0x05 * ratio);
			int b = (int) (0x18 * (1 - ratio) + 0x10 * ratio);
			g.setColor(new Color(r, ge, b));
			g.drawLine(0, i, width, i);
		}

		// Efeito de partículas/ruído sutil ao fundo (opcional, mas vamos focar no
		// essencial)

		// Título animado (pulso e flutuação)
		double floatOffset = Math.sin(time * 0.05) * 5;
		g.setFont(new Font("Verdana", Font.BOLD, 48));

		String title = "ESCAPE 3D";
		int titleWidth = g.getFontMetrics().stringWidth(title);

		// Sombra projetada
		g.setColor(new Color(0, 0, 0, 150));
		g.drawString(title, (width - titleWidth) / 2 + 4, (int) (100 + floatOffset) + 4);

		// Cor do título com brilho
		int alpha = 200 + (int) (Math.sin(time * 0.1) * 55);
		g.setColor(new Color(200, 200, 255, alpha));
		g.drawString(title, (width - titleWidth) / 2, (int) (100 + floatOffset));

		// Opções do menu
		g.setFont(new Font("Arial", Font.BOLD, 22));
		int startY = 300;
		for (int i = 0; i < OPTIONS.length; i++) {
			boolean isSelected = (selected == i);
			String text = OPTIONS[i].toUpperCase();

			if (isSelected) {
				double pulse = Math.sin(time * 0.2) * 5;
				g.setColor(new Color(255, 255, 150));

				// Indicadores laterais animados
				g.drawString(">", (width - g.getFontMetrics().stringWidth(text)) / 2 - 40 + (int) pulse,
						startY + i * 50);
				g.drawString("<", (width - g.getFontMetrics().stringWidth(text)) / 2
						+ g.getFontMetrics().stringWidth(text) + 20 - (int) pulse, startY + i * 50);

				g.setFont(new Font("Arial", Font.BOLD, 24)); // Aumenta levemente quando selecionado
			} else {
				g.setColor(new Color(150, 150, 150));
				g.setFont(new Font("Arial", Font.BOLD, 22));
			}

			int optWidth = g.getFontMetrics().stringWidth(text);
			g.drawString(text, (width - optWidth) / 2, startY + i * 50);
		}

		// Créditos no rodapé
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		g.setColor(new Color(100, 100, 120));
		g.drawString("DESENVOLVIDO POR ARON ALVARENGA", 20, height - 20);
	}

	@Override
	public void render(Bitmap target) {
		// Mantendo suporte ao renderizador de bitmap legado (simples)
		target.fill(0, 0, target.width, target.height, 0x050510);
		String title = "ESCAPE 3D";
		target.draw(title, (target.width - title.length() * 6) / 2, 20, 0xaaaaaa);
	}

	@Override
	public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
		super.tick(game, up, down, left, right, use);
		if (up) {
			selected = (selected - 1 + OPTIONS.length) % OPTIONS.length;
		}
		if (down) {
			selected = (selected + 1) % OPTIONS.length;
		}
		if (use) {
			if (selected == 0) {
				game.newGame();
				game.setMenu(null);
			} else if (selected == 1) {
				System.exit(0);
			}
		}
	}
}
