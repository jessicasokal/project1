package model;

import java.awt.Color;

/**
 * An abstract class containing general methods and fields of a Shape, including
 * changing the color, height and width, position, and whether it is hidden attributes of a Shape.
 */
public abstract class AShape implements Shape {
  protected int r;
  protected int g;
  protected int b;
  protected String name;
  protected boolean hidden;
  protected double x;
  protected double y;
  protected double width;
  protected double height;
  protected boolean fill;

  /**
   * Constructor to create an AShape with the given name, and the setting the rest of the fields
   * to their default values (0 for all numeric fields, true for where the shape is hidden or not).
   * @param name represents the identifying name given to the Shape being constructed, for example
   *             a circle may have the name "C".
   * @throws IllegalArgumentException if name is null
   */
  public AShape(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Shape cannot be constructed with null parameters.");
    }
    this.name = name;
    this.hidden = true;
  }

  /**
   * Constructor to create an AShape with the given name, setting each field to their given value.
   * @param r represents the amount of red, from 0 to 255, in the Shape being constructed.
   * @param g represents the amount of green, from 0 to 255, in the Shape being constructed.
   * @param b represents the amount of blue, from 0 to 255, in the Shape being constructed.
   * @param name represents the identifying name of the Shape being constructed, for example,
   *            a circle may have the name "C".
   * @param hidden represents whether the Shape being constructed is hidden or visible.
   * @param x represents the x position of the Shape being constructed.
   * @param y represents the y position of the Shape being constructed.
   * @param width represents the width of the bounding box of the Shape being constructed.
   * @param height represents the height of the bounding box of the Shape being constructed.
   * @throws IllegalArgumentException if any of the values representing colors (r, g, b) are not
   *                                  between 0 and 255, or the width and/or height value is
   *                                  negative
   */
  public AShape(int r, int g, int b, String name, boolean hidden, double x, double y, double width,
      double height) throws IllegalArgumentException {
    if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
      throw new IllegalArgumentException("Given color is invalid");
    }
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Width and/or height cannot be negative");
    }
    if (name == null) {
      throw new IllegalArgumentException("Shape cannot be constructed with null parameters");
    }
    this.r = r;
    this.g = g;
    this.b = b;
    this.name = name;
    this.hidden = hidden;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Changes the corresponding RGB values of this shape's color by the respective amounts.
   *
   * @param r represents the change in red
   * @param g represents the change in green
   * @param b represents the change in blue
   * @throws IllegalArgumentException if the change in color results in this shape's r, g, or b
   *                                  value being less than 0 or over 255.
   */
  @Override
  public void setColor(int r, int g, int b) throws IllegalArgumentException {
    if (r < 0 || r > 255 || g < 0 || g > 255
        || b < 0 || b > 255) {
      throw new IllegalArgumentException("Color change out of bounds.");
    }

    this.r = r;
    this.g = g;
    this.b = b;
  }

  /**
   * Changes the corresponding width and height values of this shape's bounding box by the
   * respective amounts.
   *
   * @param w represents the change in width.
   * @param h represents the change in height.
   * @throws IllegalArgumentException if the change in width or height results in this shape's
   *                                  dimensions being less than 0.
   */
  @Override
  public void setSize(double w, double h) throws IllegalArgumentException {
    if (w < 0 || h < 0) {
      throw new IllegalArgumentException("Dimension change out of bounds. Shape cannot have"
          + " negative dimensions.");
    }

    this.width = w;
    this.height = h;
  }

  /**
   * Changes the corresponding x position of this shape by the given amount.
   *
   * @param x represents the amount to change the x-coordinate by.
   */
  @Override
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Changes the corresponding y position of this shape by the given amount.
   *
   * @param y represents the amount to change the y-coordinate by.
   */
  @Override
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Gets the color value of this shape, as a Color object.
   * @return a Color object with the rgb values of this shape.
   */
  @Override
  public Color getColor() {
    return new Color(r, g, b);
  }

  /**
   * Gets the name of this shape.
   * @return the identifying name of this shape.
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Gets the x position of this shape.
   * @return the x position of this shape as a double.
   */
  @Override
  public double getX() {
    return this.x;
  }

  /**
   * Gets the y position of this shape.
   * @return the y position of this shape as a double.
   */
  @Override
  public double getY() {
    return this.y;
  }

  /**
   * Gets the width of this shape.
   * @return the width position of this shape as a double.
   */
  @Override
  public double getWidth() {
    return this.width;
  }

  /**
   * Gets the height position of this shape.
   * @return the height position of this shape as a double.
   */
  @Override
  public double getHeight() {
    return this.height;
  }

  /**
   * Makes this shape filled with the color that it is.
   */
  @Override
  public void makeFilled() {
    this.fill = true;
  }

  /**
   * Makes this shape outlined with the color that it is.
   */
  @Override
  public void makeOutlined() {
    this.fill = false;
  }

  /**
   * Generates the y points necessary to make a polygon shape.
   */
  @Override
  public int[] makeYs(int y, int height) {
    throw new UnsupportedOperationException("Only polygons have y arrays");
  }

  /**
   * Generates the x points necessary to make a polygon shape.
   */
  @Override
  public int[] makeXs(int x, int width) {
    throw new UnsupportedOperationException("Only polygons have x arrays");
  }

}
