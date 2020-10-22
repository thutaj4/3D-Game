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


@SuppressWarnings("serial")
public class Display extends Canvas implements Runnable {
	// Episode 18 beginning of walls 13:47

	// variables for the window
	public static final int width = 800;
	public static final int height = 800;
	public static final String title = "3D Game";

	private Thread thread;
	private boolean running = false;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private int pixels[];
	private int newX = 0;
	private int oldX = 0;
	private int fps;

	private InputHandler input;

	public Display() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(width, height);
		game = new Game();
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	// checking if the game is running and if it isn't start the thread
	public synchronized void start() {
		if (running) {
			return;
		} else {
			running = true;
			thread = new Thread(this);
			thread.start();
		}

		System.out.println("Running");
	}

	// checking if the game is not running
	public synchronized void stop() {
		if (!running) {
			return;
		} else {
			running = false;
			try {
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previoustime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickcount = 0;
		boolean ticked = false;

		while (running) {
			long currenttime = System.nanoTime();
			long passedtime = currenttime - previoustime;
			previoustime = currenttime;
			unprocessedSeconds += passedtime / 1000000000.0;
			requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickcount++;

				if (tickcount % 60 == 0) {
					System.out.println(frames + " fps");
					fps = frames;
					previoustime += 1000;
					frames = 0;
				}
			}

			if (ticked) {
				render();
				frames++;
			}

			render();
			frames++;

			newX = InputHandler.MouseX;
			if (newX > oldX) {
				Controller.turnRight = true;
				System.out.println("wRight");
			}
			if (newX < oldX) {
				Controller.turnLeft = true;
				System.out.println("Left");
			}
			if (newX == oldX) {
				Controller.turnLeft = false;
				Controller.turnRight = false;
				System.out.println("Still");
			}
			oldX = newX;
		}
	}

	private void tick() {
		game.tick(input.key);
	}

	// rendering the pixels onto the screen in three dimensions
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		for (int i = 0; i < width * height; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 16));
		g.drawString("FPS: " + fps, 20, 40);
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blank");
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.getContentPane().setCursor(blank);
		frame.setSize(width, height);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();

		game.start();

	}

}
