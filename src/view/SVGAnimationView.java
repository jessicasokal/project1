package view;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import model.Ellipse;
import model.IMotion;
import model.Plus;
import model.Rectangle;
import model.Shape;

/**
 * The SVGAnimationView: This view renders an animation as a .svg file that can then be
 * played in a browser to show its visual representation.
 */
public class SVGAnimationView extends AView {
  private final Appendable appendable;

  /**
   * Constructs an SVGAnimationView based on the given model and tick rate, using a
   * default appendable for the text output.
   * @param am the model to base the animation on.
   * @param tickRate the tick rate of the animation.
   */
  public SVGAnimationView(IAnimationViewModel am, int tickRate) {
    this(am, tickRate, new StringBuilder());
  }

  /**
   * Constructs an SVGAnimationView based on the given model, tick rate and appendable (for the
   * text output).
   * @param am the model to base the animation off of.
   * @param tickRate the tick rate of the animation.
   * @param ap the appendable to append the text output onto.
   * @throws IllegalArgumentException if appendable parameter is null
   */
  public SVGAnimationView(IAnimationViewModel am, int tickRate, Appendable ap) {
    super(am, tickRate);
    if (ap == null) {
      throw new IllegalArgumentException("View cannot be constructed with null parameters.");
    }
    this.appendable = ap;
  }

  /**
   * Renders this SVGAnimation as an SVG file.
   * @throws IOException if appending text output fails.
   */
  @Override
  public void render() throws IOException {
    this.appendable.append(String.format("<svg viewBox = \"%d %d %d %d\""
        + " version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n", this.topLeftCornerX,
        this.topLeftCornerY, this.width, this.height));
    List<Shape> shapes = am.getOrderedShapes();
    for (Shape s : shapes) {
      String shapeString = "";
      String endTag = "";

      Queue<IMotion> motions = am.getShapeMotions(s);
      IMotion firstMotion = motions.peek();

      // use the first Motion to fill in details for the shape
      if (firstMotion != null) {
        if (s instanceof Rectangle) {
          shapeString = String.format("<rect id=\"%s\" x=\"%.02f\" y=\"%.02f\" width=\"%.02f\" "
                  + "height=\"%.02f\" fill=\"rgb(%d,%d,%d)\" visibility=\"hidden\" >",
              s.getName(),
              firstMotion.getStartX(), firstMotion.getStartY(),
              firstMotion.getStartW(), firstMotion.getStartH(),
              firstMotion.getStartRGB()[0], firstMotion.getStartRGB()[1],
              firstMotion.getStartRGB()[2]);
          endTag = "</rect>";
        } else if (s instanceof Ellipse) {
          shapeString = String.format("<ellipse id=\"%s\" cx=\"%.02f\" cy=\"%.02f\" rx=\"%.02f\" "
                  + "ry=\"%.02f\" fill=\"rgb(%d,%d,%d)\" visibility=\"hidden\" >", s.getName(),
              firstMotion.getStartX() + (firstMotion.getStartW() / 2),
              firstMotion.getStartY() + (firstMotion.getStartH() / 2),
              firstMotion.getStartW() / 2, firstMotion.getStartH() / 2,
              firstMotion.getStartRGB()[0], firstMotion.getStartRGB()[1],
              firstMotion.getStartRGB()[2]);
          endTag = "</ellipse>";
        }
        else if (s instanceof Plus) {
          shapeString = String.format("<polygon id=\"%s\" points=\"", s.getName());
          shapeString = shapeString + getPoints(s, (int)firstMotion.getStartX(),
              (int)firstMotion.getStartY(),
              firstMotion.getStartW(),
              firstMotion.getStartH()) + "\"";
          shapeString = shapeString + String.format(
              " fill=\"rgb(%d,%d,%d)\" visibility=\"hidden\" >",
              firstMotion.getStartRGB()[0], firstMotion.getStartRGB()[1],
              firstMotion.getStartRGB()[2])
          ;
          endTag = "</polygon>";
        }
        appendable.append(shapeString).append("\n");

        appendable.append(String.format("<animate attributeType=\"xml\" "
                + "begin=\"0.0ms\" dur=\"%.1fms\" "
                + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" />",
            (float)firstMotion.getStartTick() / this.tickRate * 1000));
      }

      for (IMotion m : motions) {
        motionSVGTag(s, m);
      }
      appendable.append(endTag).append("\n");
    }
    appendable.append("</svg>");
  }

  /**
   * Gets the string representation of points of a polygon given the x and y positions of it,
   * as well as its width and height.
   * @param s the shape to get the points for.
   * @param x the x position of the shape.
   * @param y the y position of the shape.
   * @param width the width of the shape.
   * @param height the height of the shape.
   * @return the string representation of a polygon's points
   */
  private String getPoints(Shape s, int x, int y, double width, double height) {
    int[] xs = s.makeXs(x, (int)width);
    int[] ys = s.makeYs(y, (int)height);

    String finalString = "";
    for (int ii = 0; ii < xs.length; ii++) {
      if (ii != 0) {
        finalString = finalString + " " + Integer.toString(xs[ii]) + "," + Integer.toString(ys[ii]);
      }
      else {
        finalString = Integer.toString(xs[ii]) + "," + Integer.toString(ys[ii]);
      }
    }
    return finalString;
  }

  /**
   * Adds a motions svg animate tag translation to the svg output file.
   * @param s the shape that the given motion is being applied to.
   * @param m the motion the given shape is executing.
   * @throws IOException if appending the string output fails.
   */
  private void motionSVGTag(Shape s, IMotion m) throws IOException {
    boolean[] changes = m.bitwiseChangeList();
    double timeStart = (float)m.getStartTick() / tickRate * 1000;
    double timeEnd = (float)m.getEndTick() / tickRate * 1000;
    double dur = timeEnd - timeStart;
    if (s instanceof Rectangle) {
      // change in x
      if (changes[0]) {
        appendable.append(animateTag(timeStart, dur, "x",
            (int)m.getStartX(), (int)m.getEndX()));
      }
      // change in y
      if (changes[1]) {
        appendable.append(animateTag(timeStart, dur, "y",
            (int)m.getStartY(), (int)m.getEndY()));
      }
      // change in width
      if (changes[2]) {
        appendable.append(animateTag(timeStart, dur, "width",
            (int)m.getStartW(), (int)m.getEndW()));
      }
      // change in height
      if (changes[3]) {
        appendable.append(animateTag(timeStart, dur, "height", (int) m.getStartH(),
            (int) m.getEndH()));
      }
    } else if (s instanceof Ellipse) {
      // change in x
      if (changes[0]) {
        appendable.append(animateTag(timeStart, dur, "cx",
            (int)m.getStartX() + ((int)m.getStartW() / 2),
            (int)m.getEndX() + ((int)m.getEndW() / 2)));
      }
      // change in y
      if (changes[1]) {
        appendable.append(animateTag(timeStart, dur, "cy",
            (int)m.getStartY() + ((int) m.getStartH() / 2),
            (int)m.getEndY() + (int) m.getEndH() / 2));
      }
      // change in width
      if (changes[2]) {
        appendable.append(animateTag(timeStart, dur, "rx",
            (int)m.getStartW() / 2, (int)m.getEndW() / 2));
      }
      // change in height
      if (changes[3]) {
        appendable.append(animateTag(timeStart, dur, "ry", (int) m.getStartH() / 2,
            (int) m.getEndH() / 2));
      }
    }
    else if (s instanceof Plus) {
      // change in x, y, width, or height
      if (changes[0] || changes[1] || changes[2]) {
        appendable.append(animateTag(timeStart, dur, "points",
            getPoints(s, (int)m.getStartX(), (int)m.getStartY(), m.getStartW(), m.getStartH()),
            getPoints(s, (int)m.getEndX(), (int)m.getEndY(), m.getEndW(), m.getEndH())));
      }

    }
    // change in color
    if (changes[4]) {
      appendable.append(String.format("<animate attributeType=\"xml\" begin=\"%.1fms\" "
              + "dur=\"%.1fms\" "
              + "attributeName=\"fill\" from=\"rgb(%d, %d, %d)\" "
              + "to=\"rgb(%d, %d, %d)\" fill=\"freeze\" />\n", timeStart, dur, m.getStartRGB()[0],
          m.getStartRGB()[1], m.getStartRGB()[2], m.getEndRGB()[0], m.getEndRGB()[1],
          m.getEndRGB()[2]));
    }
  }

  /**
   * Creates the string representation of a motion in SVG's "animate" tag format.
   * @param startTime the start time that a motion occurred.
   * @param duration how long the motion occurred for.
   * @param attributeName the name of the attribute of the shape that is being modified.
   * @param from the starting value of the attribute being changed.
   * @param to the final value of the attribute being changed.
   * @return
   */
  private String animateTag(double startTime, double duration, String attributeName,
      int from, int to) {
    return String.format("<animate attributeType=\"xml\" begin=\"%.1fms\" dur=\"%.1fms\" "
        + "attributeName=\"%s\" from=\"%d\" to=\"%d\" fill=\"freeze\" />\n",
        startTime, duration, attributeName, from, to);
  }

  /**
   * Creates the string representation of a motion in SVG's "animate" tag format, given
   * Strings as from and to motions.
   * @param startTime the start time that a motion occurred.
   * @param duration how long the motion occurred for.
   * @param attributeName the name of the attribute of the shape that is being modified.
   * @param from the starting value of the attribute being changed.
   * @param to the final value of the attribute being changed.
   * @return
   */
  private String animateTag(double startTime, double duration, String attributeName,
      String from, String to) {
    return String.format("<animate attributeType=\"xml\" begin=\"%.1fms\" dur=\"%.1fms\" "
            + "attributeName=\"%s\"", startTime, duration, attributeName)
        + " from=\"" + from +  "\" to=\"" + to + "\" fill=\"freeze\" />\n";
  }
}
