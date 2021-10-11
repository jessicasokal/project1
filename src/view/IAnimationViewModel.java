package view;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import model.IMotion;
import model.Shape;

/**
 * Represents the intermediary between an animation view and mode, and the ability to pass
 * model information to the animation view without direct client access.
 */
public interface IAnimationViewModel {

  /**
   * Gets the list of shapes that are in this animation.
   * @return the list of shapes that are in this animation
   */
  Map<String, Shape> getShapes();

  /**
   * Gets a list of shapes in this model, in the order they were inputted.
   * @return the list of shapes of this animation model, in the order they were inputted.
   */
  List<Shape> getOrderedShapes();

  /**
   * Gets the list of motions associated with a given shape.
   * @param s the shape to get the motions of.
   * @return the motions associated with a given shape.
   */
  Queue<IMotion> getShapeMotions(Shape s);

  /**
   * Gets the list of motions associated with a given shape.
   * @param s the name of the shape to get the motions of.
   * @return the motions associated with a given shape.
   */
  Queue<IMotion> getShapeMotions(String s);

  /**
   * Gets the x coordinate of the origin of this model's canvas.
   * @return the x coordinate of the origin of this model's canvas.
   */
  int getOriginX();

  /**
   * Gets the y coordinate of the origin of this model's canvas.
   * @return the y coordinate of the origin of this model's canvas.
   */
  int getOriginY();

  /**
   * Gets the width of this model's canvas.
   * @return the width of the origin of this model's canvas.
   */
  int getWidth();

  /**
   * Gets the height of this model's canvas.
   * @return the width of the origin of this model's canvas.
   */
  int getHeight();

  /**
   * Determines if there are any motions occurring during or after the given tick, i.e. if the
   * animation is over.
   *
   * @param tick represents the tick value to check against
   * @return true if there are no motions after the given tick; false if otherwise.
   * @throws IllegalArgumentException if tick value is negative
   */
  boolean isAnimationOver(int tick) throws IllegalArgumentException;

  /**
   * Finds the next occuring start or end tick of a motion in this animation from the given tick
   * (inclusive). E.g. if there is a motion occuring from t=3 to t=6 and a motion occuring
   * from t=5 to t=7, findNextTick(4) would return 5. Similarly, findNextTick(3) would return 3.
   * If there are no motions occuring during or after the given tick, the given tick value is
   * returned.
   *
   * @param tick represents the tick with which to find the next occuring start or end
   *             of a motion
   * @return the next occuring start or end tick of a motion, or the given tick value if there
   *        are no motions after the given tick
   * @throws IllegalArgumentException if tick is negative
   */
  int findNextTick(int tick) throws IllegalArgumentException;
}
