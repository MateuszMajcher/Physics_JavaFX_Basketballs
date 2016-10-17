package application;

import java.util.Vector;

import javax.xml.bind.ValidationEvent;

import com.sun.javafx.geom.Vec2d;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sprite{
	private Image image;
	private Color color;
	private Vec2d position;
	private Vec2d velocity;
	private double mass = 0.1;
	private int radius = 15;
	private double restitution = -0.7;
	
	public Sprite(Vec2d pos, double mass, int radius) {
		this.position = pos;
		this.velocity = new Vec2d(0, 0);
		this.mass = 0.1;
		this.radius = radius;
	}
	
	public void update(double time) {
		position.x += velocity.x * time * 100;
		position.y += velocity.y * time * 100;
	}
	
	public void render(GraphicsContext gc) {
		 gc.setFill(color);
		 gc.fillOval(position.x, position.y, radius, radius);
	     gc.strokeOval(position.x, position.y, radius, radius);
	}
	
	public Vec2d getPosition() {
		return position;
	}

	public void setPosition(Vec2d position) {
		this.position = position;
	}

	public Vec2d getVelocity() {
		return velocity;
	}

	public void setVelocity(Vec2d velocity) {
		this.velocity = velocity;
	}

	public double getRestitution() {
		return restitution;
	}

	public void setRestitution(double restitution) {
		this.restitution = restitution;
	}
	
	public Double getMass() {
		return mass;
	}

	
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void clear(GraphicsContext gc) {
		gc.clearRect(0, 0, 600, 400);
	}
	
	
	
}
