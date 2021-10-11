package model;

import java.util.List;
import java.util.Queue;

/**
 * Interface representing a motion, which can be any combination of changing or maintaining an x
 * position, y position, width, height, color or hidden status.
 */
public interface IMotion extends ITimeInterval {

  /**
   * Adds this motion to the given queue of motions, combining it if necessary with any
   * overlapping motions that already exist in the queue. Combining motions involves
   * both splicing motions into overlapped vs. non-overlapped time periods, as well as
   * combining the fields they change (x, y, w, h, and rgb) into one motion reflective of all
   * changes.
   * @param motionQueue represents the queue of motions for this motion to be added to
   * @return the updated queue with this motion added
   */
  Queue<IMotion> addToQueue(Queue<IMotion> motionQueue);

  /**
   * Combines the changes/transformations two motions, this motion and the given motion, make
   * into one motion. Assumes that motions have the same start and end tick.
   *
   * @param m1 the given motion whose changes are added with this motion
   * @return a motion that holds the changes both this motion and the given motion m1 made
   * @throws IllegalArgumentException if motions do not have the same start and end tick as each
   *        other, or they illegally overlap (i.e. both try to change the same element of a shape,
   *        or their unchanged fields are different)
   */
  IMotion addMotions(IMotion m1) throws IllegalArgumentException;

  /**
   * Get a splice/portion of this motion from the given start tick to the end tick.
   * If motion is completely out of the splice range, return null.
   * If motion does not fill total splice range, return;
   * the part of the motion that does fit it the range.
   * Examples:
   * A motion from t=3 to t=8, spliced from t=4 to t=6, would return the portion of the motion from
   * t=4 to t=6
   * A motion from t=4 to t=6, spliced from t=6 to
   * @param splitTickStart represents the start of the splice range in ticks
   * @param splitTickEnd represents the end of the splice range in ticks
   * @return the splice of this motion from the given tick range, or null if motion is outside of
   *          tick range or tick range is 0.
   */
  IMotion splice(int splitTickStart, int splitTickEnd);

  /**
   * Given two motions that are consecutive (i.e this motion is the next motion
   * directly after the given motion), check that all the end values of the given motion
   * match up with all the start values of the this motion.
   * @param m1 represents the consecutive motion that comes right before this motion
   * @return true if this motion's start values match the given motion's end values,
   *          false otherwise
   */
  boolean validConsecutiveMotion(IMotion m1);

  /**
   * Represents the elements of a shape this motion changes as a list of booleans.
   * Each boolean in the list represents a change in x, y, width, height, or color respectively,
   * where a change in the element is represented by true and a lack of change by this motion
   * is represented by false.
   * E.g. a motion that changed the x position and color of a shape would be represented as:
   * [true, false, false, false, true].
   */
  boolean[] bitwiseChangeList();

  /**
   * Executes this motion for a given Shape, changing its attributes to reflect
   * change in position, size, color, or hidden status to the end state of this motion.
   * @param shape the shape to execute motion for.
   */
  void executeMotion(Shape shape);

  /**
   * Generates the string representation of this Motion, including each of its attributes (ticks,
   * x position, y position, width, height, color).
   * @return the string rendering of this Motion.
   */
  String toString();

  /**
   * Generates a formatted string of the start values of a motion.
   * @return the formatted string of a motion's start values.
   */
  String toStringStartValues();

  /**
   * Generates a formatted string of the end values of a motion.
   * @return the formatted string of a motion's end values.
   */
  String toStringEndValues();

  /**
   * Generates the string representation of this Motion, including each of its attributes (ticks,
   * x position, y position, width, height, color) as integers.
   * @return the string rendering of this Motion.
   */
  String toStringAsInt();

  /**
   * Gets the first x value of this motion.
   * @return the first x value at the last tick of the motion.
   */
  double getStartX();

  /**
   * Gets the end x value of this motion.
   * @return the end x value at the last tick of the motion.
   */
  double getEndX();

  /**
   * Gets the first y value of this motion.
   * @return the first y value at the last tick of the motion.
   */
  double getStartY();

  /**
   * Gets the final y value of this motion.
   * @return the end y value at the last tick of the motion.
   */
  double getEndY();

  /**
   * Gets the first width value of this motion.
   * @return the first width value at the last tick of the motion.
   */
  double getStartW();

  /**
   * Gets the final width value of this motion.
   * @return the end width value at the last tick of the motion.
   */
  double getEndW();

  /**
   * Gets the final RGB value of this motion.
   * @return the end RGB value at the last tick of the motion.
   */
  double getStartH();

  /**
   * Gets the final height value of this motion.
   * @return the end height value at the last tick of the motion.
   */
  double getEndH();

  /**
   * Gets the first RGB value of this motion.
   * @return the first RGB value at the first tick of the motion.
   */
  int[] getStartRGB();

  /**
   * Gets the final RGB value of this motion.
   * @return the end RGB value at the last tick of the motion.
   */
  int[] getEndRGB();

  /**
   * Returns this motion and the other motion given as a list of spliced, combined motions.
   * Combining motions involves splitting up the motions into non-overlapping and overlapping
   * periods of time, and consolidating the motions' changes into one motion
   * for overlapping periods of time.
   * examples:
   *     2  3  4  5  6
   *     |-----|         motion1
   *        |--------|   motion2
   *     combined:
   *     |--|            newmotion1
   *        |--|         newmotion2
   *           |-----|   newmotion3
   *     2  3  4  5  6
   *     |--------|      m1
   *     |--------|      m2
   *     combined:
   *     |--------|      newmotion1
   *     2  3  4  5  6
   *           |-----|   m1
   *        |--------|   m2
   *     combined:
   *        |--|         newmotion1
   *           |-----|   newmotion2
   *     2  3  4  5  6
   *     |--------|      m1
   *        |--|         m2
   *     combined:
   *     |--|            newmotion1
   *        |--|         newmotion2
   *           |--|      newmotion3
   *
   * @param other the given motion to combine with this motion
   * @return the list of combined motion(s)
   * @throws IllegalArgumentException if there are illegal/invalid overlaps between the two motions
   */
  List<IMotion> combineMotion(IMotion other);
}