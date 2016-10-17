package application;

public class Bounce {
	public boolean bounce;
	public double x;
	public double y;
	
	public Bounce(boolean b) {
		this.bounce = b;
		this.x = 0;
		this.y = 0;
	}
	
	public Bounce(boolean b, double x, double y) {
		this.bounce = b;
		this.x = x;
		this.y = y;
	}
}
