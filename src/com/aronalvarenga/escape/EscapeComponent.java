package com.aronalvarenga.escape;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.aronalvarenga.escape.entities.Player;
import com.aronalvarenga.escape.gui.Screen;

public class EscapeComponent extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 160;
	private static final int HEIGHT = 120;
	private static final int SCALE = 6;

	private boolean running;
	private Thread thread;

	private Game game;
	private Screen screen;
	private BufferedImage img;
	private int[] pixels;
	private InputHandler inputHandler;
	private Cursor emptyCursor, defaultCursor;
	private boolean hadFocus = false;

	public EscapeComponent() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		game = new Game();
		screen = new Screen(WIDTH, HEIGHT);

		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		inputHandler = new InputHandler();

		addKeyListener(inputHandler);
		addFocusListener(inputHandler);
		addMouseListener(inputHandler);
		addMouseMotionListener(inputHandler);
		emptyCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty");
		defaultCursor = getCursor();
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int fps;

	public void run() {
		requestFocus();

		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		double unprocessedSeconds = 0;

		int frames = 0;
		long lastTimer = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;

			if (passedTime < 0)
				passedTime = 0;
			if (passedTime > 100000000)
				passedTime = 100000000; // Cap at 0.1s to avoid massive catching up

			unprocessedSeconds += passedTime / 1000000000.0;

			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
			}

			render();
			frames++;

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				fps = frames;
				frames = 0;
			}

			// Don't burn CPU if we are running too fast
			if (!ticked) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void tick() {
		if (hasFocus()) {
			game.tick(inputHandler.keys);
		}
	}

	private void render() {
		if (hadFocus != hasFocus()) {
			hadFocus = !hadFocus;
			setCursor(hadFocus ? emptyCursor : defaultCursor);
		}
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game, hasFocus());

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.green);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(img, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		// Menus desenhados com Graphics (fonte do sistema garantida)
		if (game.menu != null) {
			game.menu.renderWithGraphics(g, getWidth(), getHeight());
		}

		// UI Elements
		if (game.menu == null && game.player != null) {
			// Life bar shadow
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(12, 12, 250, 20);

			// Life bar
			g.setColor(Color.red);
			g.fillRect(10, 10, 250, 20);
			g.setColor(new Color(0, 255, 0, 200));
			g.fillRect(10, 10, (int) ((Player.health / 20.0) * 250), 20);

			// Life text
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 16));
			g.drawString("Vida: " + (int) (Player.health), 20, 26);
		}

		// FPS Display
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		g.setColor(Color.yellow);
		g.drawString(fps + " FPS", getWidth() - 50, 20);

		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		EscapeComponent game = new EscapeComponent();

		JFrame frame = new JFrame("Engine 3D - Aron Alvarenga");

		frame.add(game);
		frame.setResizable(false);
		frame.pack();

		frame.setLocationRelativeTo(null);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.requestFocusInWindow();

		game.start();
	}
}
