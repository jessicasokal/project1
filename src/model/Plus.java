package model;

import java.awt.Polygon;
import java.awt.Shape;

/**
 * Represents a Plus sign Shape.
 */
public class Plus extends AShape {

  /**
   * Constructs a plus sign with the given name.
   * @param name the name to identify this plus sign.
   */
  public Plus(String name) {
    super(name);
  }

  /**
   * Constructs a plus sign with the given attributes.
   * @param r the amount of red in this plus sign.
   * @param g the amount of green in this plus sign.
   * @param b the amount of blue in this plus sign
   * @param name the identifying name of this plus sign.
   * @param hidden whether this plus sign is hidden or not.
   * @param x the x position of this plus sign.
   * @param y the y position of this plus sign.
   * @param width the width of the bounding box of this plus sign.
   * @param height the height of the bounding box of this plus sign.
   */
  public Plus(int r, int g, int b, String name, boolean hidden, double x,
      double y, double width, double height) {
    super(r, g, b, name, hidden, x, y, width, height);

  }

  @Override
  public Shape visualizeShape() {
    return new Polygon(makeXs((int)this.x, (int)this.width),
        makeYs((int)this.y, (int)this.height), 12);
  }

  @Override
  public String getType() {
    return "plus";
  }

  @Override
  public int[] makeXs(int x, int width) {
    double oneFourth = width / 4;
    int[] xs = {(int)(x + oneFourth),
        (int)(x + 3 * (oneFourth)),
        (int)(x + 3 * (oneFourth)),
        (x + width),
        (x + width),
        (int)(x + 3 * (oneFourth)),
        (int)(x + 3 * (oneFourth)),
        (int)(x + oneFourth),
        (int)(x + oneFourth),
        x,
        x,
        (int)(x + oneFourth)
    };
    return xs;
  }

  @Override
  public int[] makeYs(int y, int height) {
    double oneFourthY = height / 4;
    int[] ys = {y,
        y,
        (int)(y + oneFourthY),
        (int)(y + oneFourthY),
        (int)(y + 3 * (oneFourthY)),
        (int)(y + 3 * (oneFourthY)),
        (y + height),
        (y + height),
        (int)(y + 3 * (oneFourthY)),
        (int)(y + 3 * (oneFourthY)),
        (int)(y + oneFourthY),
        (int)(y + oneFourthY)
    };
    return ys;
  }

}
