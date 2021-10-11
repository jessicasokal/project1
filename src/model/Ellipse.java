package model;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Represents an Ellipse Shape.
 */
public class Ellipse extends AShape {

  /**
   * Constructs s ellipse with the given name.
   * @param name the name to identify this ellipse.
   */
  public Ellipse(String name) {
    super(name);
  }

  /**
   * Constructs s ellipse with the given attributes.
   * @param r the amount of red in this ellipse.
   * @param g the amount of green in this ellipse.
   * @param b the amount of blue in this ellipse
   * @param name the identifying name of this ellipse.
   * @param hidden whether this ellipse is hidden or not.
   * @param x the x position of this ellipse.
   * @param y the y position of this ellipse.
   * @param width the width of the bounding box of this ellipse.
   * @param height the height of the bounding box of this ellipse.
   */
  public Ellipse(int r, int g, int b, String name, boolean hidden, double x,
      double y, double width, double height) {
    super(r, g, b, name, hidden, x, y, width, height);
  }

  /**
   * Generates the string representation of this rectangle, following the format Circle +
   * [name of ellipse].
   * @return the string rendering of this ellipse.
   */
  @Override
  public String toString() {
    return this.name + " ellipse";
  }

  /**
   * Creates the visualization of this ellipse.
   */
  @Override
  public Shape visualizeShape() {
    return new Ellipse2D.Double(x, y, width, height);
  }

  /**
   * Gets the type of this shape (ellipse).
   *
   * @return the type of this shape
   */
  @Override
  public String getType() {
    return "ellipse";
  }
}