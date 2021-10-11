package view;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import model.IMotion;
import model.Shape;

/**
 * Represents an animation view, where each shape and motion in the animation is represented
 * textually.
 */
public class TextualAnimationView extends AView {

  private final Appendable appendable;

  /**
   * Constructs a textual animation view with the given ViewModel and tickrate, and a default
   * appendable of a StringBuilder.
   *
   * @param am       represents ViewModel containing information about the animation's shapes and
   *                 motions
   * @param tickRate represents the desired tickRate (ticks per unit of time)
   */
  public TextualAnimationView(IAnimationViewModel am, int tickRate) {
    this(am, tickRate, new StringBuilder());
  }

  /**
   * Constructs a textual animation view with the given ViewModel, tickrate, and appendable.
   *
   * @param am         represents ViewModel containing information about the animation's shapes and
   *                   motions
   * @param tickRate   represents the desired tickRate (ticks per unit of time)
   * @param appendable represents the location for the textual output representing the animation's
   *                   shapes and motions
   * @throws IllegalArgumentException if appendable parameter is null
   */
  public TextualAnimationView(IAnimationViewModel am, int tickRate, Appendable appendable) {
    super(am, tickRate);
    if (appendable == null) {
      throw new IllegalArgumentException("View cannot be constructed with null parameters.");
    }
    this.appendable = appendable;
  }

  /**
   * Renders the animation this view represents in a textual output. The formatting is the shape
   * name and type is rendered, and then a list of its corresponding motions in chronological tick
   * order.
   *
   * @throws IOException if there is an error with appending to the textual output
   */
  public void render() throws IOException {
    List<Shape> shapes = this.am.getOrderedShapes();
    appendable.append("canvas " + Integer.toString(topLeftCornerX) + " "
        + topLeftCornerY + " " + width + " "
        + height + "\n");
    for (Shape s : shapes) {
      appendable.append("shape " + s.getName() + " " + s.getType() + "\n");
      Queue<IMotion> motions = am.getShapeMotions(s);
      for (IMotion m : motions) {
        double startTime = (float) m.getStartTick() / this.tickRate;
        double endTime = (float) m.getEndTick() / this.tickRate;
        appendable.append("motion " + s.getName() + " " + startTime
            + " " + m.toStringStartValues() + " " + endTime + " "
            + m.toStringEndValues() + "\n");
      }
    }
  }
}


