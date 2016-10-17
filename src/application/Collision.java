package application;

import com.sun.javafx.geom.Vec2d;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Collision {
	

	@SuppressWarnings("restriction")
	static boolean collisionCirlceRect(Sprite c, Rectangle r) {
		@SuppressWarnings("restriction")
		Vec2d half = new Vec2d(r.getWidth()/2, r.getHeight());
		Vec2d center = new Vec2d(c.getPosition().x - (r.getX()+half.x)
							    ,c.getPosition().y - (r.getY()+half.y));
		
		Vec2d side = new Vec2d(Math.abs (center.x) - half.x
			    , Math.abs (center.y) - half.y);
		
		if (side.x >  c.getRadius() || side.y >  c.getRadius()) // outside
		    return false; 
        
		if (side.x < 0 || side.y < 0) // intersects side or corner
            return true;
		
		
		return side.x*side.x + side.y*side.y  < c.getRadius()*c.getRadius();
		
		
	}
	@SuppressWarnings("restriction")
	public static Bounce bounce(Sprite c, Rectangle r) {
		@SuppressWarnings("restriction")
		Vec2d half = new Vec2d(r.getWidth()/2, r.getHeight()/2);
		
		Vec2d center = new Vec2d(c.getPosition().x - (r.getX()+half.x)
							    ,c.getPosition().y - (r.getY()+half.y));
		
		Vec2d side = new Vec2d(Math.abs (center.x) - half.x
			    , Math.abs (center.y) - half.y);
		
		 if (side.x >  c.getRadius() || side.y >  c.getRadius()) // outside
			    return new Bounce(false); 
	        if (side.x < -c.getRadius() && side.y < -c.getRadius()) // inside
			    return new Bounce(false); 
			if (side.x < 0 || side.y < 0) // intersects side or corner
			{
				double dx = 0, dy = 0;
			    if (Math.abs (side.x) < c.getRadius() && side.y < 0)
				{
				    dx = center.x*side.x < 0 ? -1 : 1;
				}
			    else if (Math.abs (side.y) < c.getRadius() && side.x < 0)
				{
				    dy = center.y*side.y < 0 ? -1 : 1;
				}
				
	            return new Bounce(true, dx, dy);
			}
	        // circle is near the cornerb
	        boolean bounce = side.x*side.x + side.y*side.y  < c.getRadius()*c.getRadius();
			if (!bounce) return new Bounce(false);
			double norm = Math.sqrt (side.x*side.x+side.y*side.y);
			double dx = center.x < 0 ? -1 : 1;
			double dy = center.y < 0 ? -1 : 1;
			return new Bounce(true, dx*side.x/norm, dy*side.y/norm);
	}

}
