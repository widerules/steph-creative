
package com.droid.gamedev.object.collision;

/**
 * <code>CollisionShape</code> interface represents a form of geometric shape
 * that behave as sprite collision area. <code>CollisionShape</code> is able
 * to determine whether its area is intersected with another collision shape
 * area.
 * <p>
 * 
 * This interface that play the role to determine whether two sprites are
 * collided to each other or not using <code>CollisionShape</code>
 * {@linkplain #intersects(CollisionShape)} method.
 */
public interface CollisionShape {
	
	/**
	 * Returns whether this collision shape intersects with other collision
	 * shape area.
	 */
	public boolean intersects(CollisionShape shape);
	
	/**
	 * Moves this collision shape to specified location.
	 */
	public void setLocation(double x, double y);
	
	/**
	 * Moves this collision shape by specified delta.
	 */
	public void move(double dx, double dy);
	
	/**
	 * Sets the boundary of this collision shape to specified boundary.
	 */
	public void setBounds(double x1, double y1, int w1, int h1);
	
	/**
	 * Returns the <code>x</code>-position of this collision shape.
	 */
	public double getX();
	
	/**
	 * Returns the <code>y</code>-position of this collision shape.
	 */
	public double getY();
	
	/**
	 * Returns the width of this collision shape.
	 */
	public int getWidth();
	
	/**
	 * Returns the height of this collision shape.
	 */
	public int getHeight();
	
}
