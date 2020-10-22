import java.util.Random;

public class Screen extends Render {

	private Render test;
	private Render3D render;

	public Screen(int width, int height) {
		super(width, height);
		Random rand = new Random();
		test = new Render(256, 256);
		render = new Render3D(width,height);

		for (int i = 0; i < 256 * 256; i++) {
			test.pixels[i] = rand.nextInt() * ((rand.nextInt(5))/4);
		}
	}

	public void render(Game game) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
		
		render.floor(game);
		render.RenderDistanceLimeter();
		draw(render,0,0);

	}
}
