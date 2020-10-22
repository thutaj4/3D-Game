
public class Controller {

	public double x, y, z, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk= false;
	public static boolean crouchWalk = false;
	public static boolean runWalk = false;

	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean run) {
		double rotationspeed = 0.01;
		double walkspeed = 0.5;
		double xMove = 0;
		double zMove = 0;
		double jumpHeight = 0.5;
		double crouchHeight = 0.3;

		if (forward) {
			zMove++;
			walk = true;
		}
		if (back) {
			zMove--;
			walk = true;
		}
		if (left) {
			xMove--;
			walk = true;
		}
		if (right) {
			xMove++;
			walk = true;
		}
		if (turnLeft) {
			rotationa -= rotationspeed;
			walk = true;
		}
		if (turnRight) {
			rotationa += rotationspeed;
			walk = true;
		}
		if (jump) {
			y += jumpHeight;
			run = false;
		}
		if (crouch) {
			y-= crouchHeight;
			run = false;
			crouchWalk = true;
			walkspeed = 0.25;
		}
		if(run) {
			walkspeed = 1;
			walk = true;
			runWalk = true;
		}
		if (!forward && !back && !left && !right) {
			walk = false;
		}
		if(!crouch) {
			crouchWalk = false;
		}
		if(!run) {
			runWalk = false;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkspeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkspeed;

		x += xa;
		z += za;
		y *= 0.9;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotationa *= 0.8;
	}

}
