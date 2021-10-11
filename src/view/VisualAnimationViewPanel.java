package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import model.Shape;

/**
 * Represents the drawable area as a panel for a visual animation view, aka the canvas
 * where the animation can be rendered.
 */
public class VisualAnimationViewPanel extends JPanel {
  List<Shape> shapesToDraw;
  int originX;
  int originY;
  int canvasWidth;
  int canvasHeight;
  boolean filled;

  /**
   * Constructs an animation view panel with default canvas settings of the top left corner (0, 0)
   * and the width and heigh as 0.
   */
  public VisualAnimationViewPanel() {
    super();
    shapesToDraw = new ArrayList<>();
    originY = 0;
    originX = 0;
    canvasHeight = 0;
    canvasWidth = 0;
    filled = true;
  }

  /**
   * Set this panel's list of shapes to render to the given list.
   *
   * @param los represents the list of shapes to render
   * @throws IllegalArgumentException if constructed with a null list
   */
  protected void setShapes(List<Shape> los) {
    if (los == null) {
      throw new IllegalArgumentException("View panel cannot have null list of shapes.");
    }
    if (filled) {
      for (Shape s : los) {
        s.makeFilled();
      }
    }
    else {
      for (Shape s : los) {
        s.makeOutlined();
      }
    }
    this.shapesToDraw = los;
  }

  /**
   * Set this panel's canvas top left corner to be at the given coordinates on the coordinate
   * grid.
   *
   * @param x represents the leftmost x-position of the canvas
   * @param y represents the topmost y-position of the canvas
   */
  protected void setOrigin(int x, int y) {
    this.originX = x;
    this.originY = y;
  }

  /**
   * Set this panel's canvas width and height to the given values.
   *
   * @param w represents the canvas width
   * @param h represents the canvas height
   * @throws IllegalArgumentException if width or height are negative
   */
  protected void setCanvas(int w, int h) {
    if (w < 0 || h < 0) {
      throw new IllegalArgumentException("Cannot have negative canvas dimensions.");
    }
    canvasWidth = w;
    canvasHeight = h;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.BLACK);

    AffineTransform originalTransform = g2d.getTransform();

    // translate to actual origin
    g2d.translate(-this.originX, -this.originY);

    g2d.clip(new Rectangle(originX, originY, this.canvasWidth, this.canvasHeight));

    // loop through the shapes and draw them;
    if (filled) {
      for (int ii = 0; ii < this.shapesToDraw.size(); ii++) {
        Shape s = shapesToDraw.get(ii);
        g2d.setColor(s.getColor());
        g2d.fill(s.visualizeShape());
      }
    }
    else {
      for (int ii = 0; ii < this.shapesToDraw.size(); ii++) {
        Shape s = shapesToDraw.get(ii);
        g2d.setColor(s.getColor());
        g2d.draw(s.visualizeShape());
      }
    }

    //reset the transform to what it was!
    g2d.setTransform(originalTransform);
  }

  void changeFill() {
    this.filled = !this.filled;
  }

}
