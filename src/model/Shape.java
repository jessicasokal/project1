package model;

import java.awt.Color;

/**
 * Represents a 2D shape with a color, size, position, and hidden/visible state.
 */
public interface Shape {

  /**
   * Changes the corresponding RGB values of this shape's color by the respective amounts.
   *
   * @param r represents the change in red
   * @param g represents the change in green
   * @param b represents the change in blue
   * @throws IllegalArgumentException if the change in color results in this shape's r, g, or b
   *                                  value being less than 0 or over 255.
   */
  void setColor(int r, int g, int b) throws IllegalArgumentException;

  /**
   * Changes the corresponding width and height values of this shape's bounding box by the
   * respective amounts.
   *
   * @param w represents the change in width.
   * @param h represents the change in height.
   * @throws IllegalArgumentException if the change in width or height results in this shape's
   *                                  dimensions being less than 0.
   */
  void setSize(double w, double h) throws IllegalArgumentException;

  /**
   * Changes the corresponding x position of this shape by the given amount.
   *
   * @param deltaX represents the amount to change the x-coordinate by.
   */
  void setX(double deltaX);

  /**
   * Changes the corresponding y position of this shape by the given amount.
   *
   * @param deltaY represents the amount to change the y-coordinate by.
   */
  void setY(double deltaY);

  /**
   * Creates the visualization of a single shape.
   */
  java.awt.Shape visualizeShape();

  /**
   * Gets the color value of this shape, as a Color object.
   * @return a Color object with the rgb values of this shape.
   */
  Color getColor();

  /**
   * Gets the name of this shape.
   * @return the identifying name of this shape.
   */
  String getName();

  /**
   * Gets the x position of this shape.
   * @return the x position of this shape as a double.
   */
  double getX();

  /**
   * Gets the y position of this shape.
   * @return the y position of this shape as a double.
   */
  double getY();

  /**
   * Gets the width of this shape.
   * @return the width position of this shape as a double.
   */
  double getWidth();

  /**
   * Gets the height position of this shape.
   * @return the height position of this shape as a double.
   */
  double getHeight();

  /**
   * Gets the type of this shape (e.g. rectangle, circle, etc.)
   *
   * @return the type of this shape
   */
  String getType();

  /**
   * Makes this shape filled with the color that it is.
   */
  void makeFilled();

  /**
   * Makes this shape outlined with the color that it is.
   */
  void makeOutlined();

  /**
   * Generates the y points necessary to make a polygon shape.
   */
  int[] makeYs(int y, int height);

  /**
   * Generates the x points necessary to make a polygon shape.
   */
  int[] makeXs(int x, int width);

}
