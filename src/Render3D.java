import java.util.Random;

public class Render3D extends Render {
	
	public double[] zBuffer;
	public double renderDistance = 5000.0;
	Random random = new Random();

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width*height];
	}

	public void floor(Game game) {

		double floorposition = 8;
		double ceilingposition = 8;
		double forward = game.controls.z;
		double right = game.controls.x;
		double rotation = game.controls.rotation;
		double cosine = Math.cos(rotation);
		double sine = Math.sin(rotation);
		double up = game.controls.y;
		double walking = Math.sin(game.time / 6.0) * 0.5;
		
		if(Controller.crouchWalk) {
			walking = Math.sin(game.time / 6.0) * 0.2;
		}
		if(Controller.runWalk) {
			walking = Math.sin(game.time / 6.0) * 0.7;
		}

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = (floorposition + up) / ceiling;
			
			if (Controller.walk) {
				z = (floorposition + up + walking) / ceiling;
			}

			if (ceiling < 0) {
				z = (ceilingposition - up) / -ceiling;
			}

			for (int x = 0; x < width; x++) {
				double xDepth = (x - width / 2.0) / height;
				xDepth *= z;
				double xx = xDepth * cosine + z * sine;
				double yy = z * cosine - xDepth * sine;
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);
				zBuffer[x + y * width] = z;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 8];
				
				if (z > 500) {
					pixels[x + y * width] = 0;
				}
			}
		}
		
		double xx = random.nextDouble();
		double yy = random.nextDouble();
		double zz = 1;
		
		int xPixel = (int)(xx / zz * height + width);
		int yPixel = (int)(yy / zz * height + height);
		if(xPixel >= 0 && yPixel <= 0) {
			
		}
	}
	
	public void RenderDistanceLimeter() {
		for (int i = 0; i < width * height; i++) {
			int color = pixels[i];
			int brigthness = (int) (renderDistance / zBuffer[i]);
			
			if (brigthness < 0) {
				brigthness = 0;
			}
			if (brigthness > 255) {
				brigthness = 255;
			}
			
			int r = (color >> 16) & 0xff, g = (color >> 8) & 0xff, b = (color) & 0xff;
			
			r = r*brigthness / 255;
			g = g*brigthness / 255;
			b = b*brigthness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}

}
