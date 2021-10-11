package model;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Represents a Rectangle Shape.
 */
public class Rectangle extends AShape {

  /**
   * Constructs a rectangle with the given name.
   * @param name the name to identify this rectangle.
   */
  public Rectangle(String name) {
    super(name);
  }

  /**
   * Constructs a rectangle with the given attributes.
   * @param r the amount of red in this rectangle.
   * @param g the amount of green in this rectangle.
   * @param b the amount of blue in this rectangle.
   * @param name the identifying name of this rectangle.
   * @param hidden whether this rectangle is visible or not.
   * @param x the x position of this rectangle.
   * @param y the y position of this rectangle.
   * @param width the width of this rectangle.
   * @param height the height of this rectangle.
   */
  public Rectangle(int r, int g, int b, String name, boolean hidden, double x,
      double y, double width, double height) {
    super(r, g, b, name, hidden, x, y, width, height);
  }

  /**
   * Generates the string representation of this rectangle, following the format Rectangle +
   * [name of rectangle].
   * @return the string rendering of this rectangle.
   */
  @Override
  public String toString() {
    return this.name + " rectangle";
  }

  /**
   * Creates the visualization of this Rectangle.
   */
  @Override
  public Shape visualizeShape() {
    return new Rectangle2D.Double(x, y, width, height);
  }

  /**
   * Gets the type of this shape (rectangle).
   *
   * @return the type of this shape
   */
  @Override
  public String getType() {
    return "rectangle";
  }
}
