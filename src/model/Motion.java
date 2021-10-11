package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Class representing a motion, which can be any combination of changing or maintaining an x
 * position, y position, width, height, color or hidden status.
 */
public class Motion extends ATimeInterval implements IMotion {
  private final double startX;
  private final double endX;
  private final double startY;
  private final double endY;
  private final double startW;
  private final double endW;
  private final double startH;
  private final double endH;
  private final int[] startRGB;
  private final int[] endRGB;

  /**
   * Constructs a motion based on the given attributes.
   * @param startTick the starting tick of this motion.
   * @param endTick the ending tick of this motion.
   * @param startX the starting x position of this motion at the start tick value.
   * @param endX the ending x position of this motion at the end tick value.
   * @param startY the starting y position of this motion at the start tick value.
   * @param endY the ending y position of this motion at the end tick value.
   * @param startW the starting width of the shape in this motion at the start tick value.
   * @param endW the ending width of the shape in this motion at the end tick value.
   * @param startH the starting height of the shape in this motion at the start tick value.
   * @param endH the ending height of the shape in this motion at the end tick value.
   * @param startRGB the starting color representation of the shape in this motion at the start tick
   *                 value.
   * @param endRGB the ending color representation of the shape in this motion at the end tick
   *               value.
   * @throws IllegalArgumentException if tick is out of bounds or startRGB or endRGB are null
   */
  public Motion(int startTick, int endTick, double startX, double endX, double startY, double endY,
      double startW, double endW, double startH, double endH, int[] startRGB, int[] endRGB) {
    super(startTick, endTick);
    if (startRGB == null || endRGB == null) {
      throw new IllegalArgumentException("Cannot create motion with null parameters.");
    }
    this.startX = startX;
    this.endX = endX;
    this.startY = startY;
    this.endY = endY;
    this.startW = startW;
    this.endW = endW;
    this.startH = startH;
    this.endH = endH;
    this.startRGB = startRGB;
    this.endRGB = endRGB;
  }

  /**
   * Adds this motion to the given queue of motions, combining it if necessary with any
   * overlapping motions that already exist in the queue. Combining motions involves
   * both splicing motions into overlapped vs. non-overlapped time periods, as well as
   * combining the fields they change (x, y, w, h, and rgb) into one motion reflective of all
   * changes.
   * @param motionQueue represents the queue of motions for this motion to be added to
   * @return the updated queue with this motion added
   */
  public Queue<IMotion> addToQueue(Queue<IMotion> motionQueue) {
    Queue<IMotion> newMotionQueue = new PriorityQueue<>(Motion.TIME_INTERVAL_COMP);
    List<IMotion> overlappingMotions = new ArrayList<>();

    // represents the first motion in the queue (first motion to be executed, since priority queue)
    IMotion currentMotion = motionQueue.peek();

    // while the motionQueue isn't empty and the current motion doesn't start after this motion
    while (currentMotion != null && currentMotion.getStartTick() <= this.endTick) {

      if (this.startTick >= currentMotion.getEndTick()) {
        // if the current motion ends before this motion starts, it won't overlap so just add it
        // to the new motion queue
        newMotionQueue.add(motionQueue.poll());
      } else if (this.startTick >= currentMotion.getStartTick()
          || this.endTick <= currentMotion.getEndTick()) {
        // this motion overlaps with the current motion,
        // add current motion to list of overlapping motions and remove from the original queue
        overlappingMotions.add(currentMotion);
        motionQueue.remove();
      }
      // advance to the next motion in the motion queue
      currentMotion = motionQueue.peek();
    }

    // combine this motion with all the overlapping motions.
    IMotion currentCombineMotion = this;
    // while there are still motions to go through and combine with this motion (aka overlapping
    // motions)
    while (overlappingMotions.size() > 0) {
      // list will return anywhere from 1 - 3 elements representing the merged mot
      // ions
      List<IMotion> combinedMotions =
          currentCombineMotion.combineMotion(overlappingMotions.remove(0));

      // if there is at least one overlapping motion left, the last element of the combinedMotions
      // list will be the remainder of this original motion that still needs to be merged with the
      // rest of the overlapping motions
      currentCombineMotion = combinedMotions.get(combinedMotions.size() - 1);

      // add all the other resulting motions from the merge
      for (int index = 0; index < combinedMotions.size() - 1; index += 1) {
        newMotionQueue.add(combinedMotions.get(index));
      }
    }
    // add the last remainder motion from the result of merging all overlapping elements
    newMotionQueue.add(currentCombineMotion);

    // add all remaining motions from the original motionQueue to the new motion queue
    newMotionQueue.addAll(motionQueue);
    return newMotionQueue;
  }

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
  public List<IMotion> combineMotion(IMotion other) throws IllegalArgumentException {
    // confirm that overlap is valid
    if (!noConflictingChanges(other)) {
      throw new IllegalArgumentException("Cannot merge overlapping motions as there are"
          + " conflicting changes." + other.toString());
    }
    List<IMotion> results = new ArrayList<>();

    // determine overlap points
    int leftStartTick = Math.min(this.startTick, other.getStartTick());
    int rightEndTick = Math.max(this.endTick, other.getEndTick());
    int middleStartTick = Math.max(this.startTick, other.getStartTick());
    int middleEndTick = Math.min(this.endTick, other.getEndTick());

    // Motion splices at the overlap points
    IMotion motion1left = this.splice(leftStartTick, middleStartTick);
    IMotion motion1middle = this.splice(middleStartTick, middleEndTick);
    IMotion motion1right = this.splice(middleEndTick, rightEndTick);
    IMotion motion2left = other.splice(leftStartTick, middleStartTick);
    IMotion motion2middle = other.splice(middleStartTick, middleEndTick);
    IMotion motion2right = other.splice(middleEndTick, rightEndTick);

    // add splices together that aren't null, and add the spliced, combined motions to return list
    if (motion1left != null) {
      if (motion2left != null) {
        results.add(motion1left.addMotions(motion2left));
      } else {
        results.add(motion1left);
      }
    } else if (motion2left != null) {
      results.add(motion2left);
    }

    if (motion1middle != null) {
      if (motion2middle != null) {
        results.add(motion1middle.addMotions(motion2middle));
      } else {
        results.add(motion1middle);
      }
    } else if (motion2middle != null) {
      results.add(motion2middle);
    }

    if (motion1right != null) {
      if (motion2right != null) {
        results.add(motion1right.addMotions(motion2right));
      } else {
        results.add(motion1right);
      }
    } else if (motion2right != null) {
      results.add(motion2right);
    }

    return results;
  }

  /**
   * Get a splice/portion of this motion from the given start tick to the end tick.
   * If motion is completely out of the splice range, return null.
   * If motion does not fill total splice range, return
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
  public IMotion splice(int splitTickStart, int splitTickEnd) {
    if (this.startTick >= splitTickEnd || this.endTick <= splitTickStart
        || splitTickStart == splitTickEnd) {
      return null;
    }
    double rateOfChangeX = -(this.endX - this.startX) / (this.endTick - this.startTick);
    double rateOfChangeY = -(this.endY - this.startY) / (this.endTick - this.startTick);
    double rateOfChangeW = -(this.endW - this.startW) / (this.endTick - this.startTick);
    double rateOfChangeH = -(this.endH - this.startH) / (this.endTick - this.startTick);
    int rateOfChangeR = (this.endRGB[0] - this.startRGB[0]) / (this.endTick - this.startTick);
    int rateOfChangeG = (this.endRGB[1] - this.startRGB[1]) / (this.endTick - this.startTick);
    int rateOfChangeB = (this.endRGB[2] - this.startRGB[2]) / (this.endTick - this.startTick);

    int newStartTick = Math.max(splitTickStart, this.startTick);
    int newEndTick = Math.min(splitTickEnd, this.endTick);
    int[] newStartRGB = {this.startRGB[0] + rateOfChangeR * (newStartTick - this.startTick),
        this.startRGB[1] + rateOfChangeG * (newStartTick - this.startTick),
        this.startRGB[2] + rateOfChangeB * (newStartTick - this.startTick)};
    int[] newEndRGB = {this.endRGB[0] + rateOfChangeR * (newEndTick - this.endTick),
        this.endRGB[1] + rateOfChangeG * (newEndTick - this.endTick),
        this.endRGB[2] + rateOfChangeB * (newEndTick - this.endTick)};

    return new Motion(newStartTick, newEndTick,
        this.startX + (newStartTick - this.startTick) * rateOfChangeX,
        this.endX - (newEndTick - this.endTick) * rateOfChangeX,
        this.startY + (newStartTick - this.startTick) * rateOfChangeY,
        this.endY - (newEndTick - this.endTick) * rateOfChangeY,
        this.startW + (newStartTick - this.startTick) * rateOfChangeW,
        this.endW - (newEndTick - this.endTick) * rateOfChangeW,
        this.startH + (newStartTick - this.startTick) * rateOfChangeH,
        this.endH - (newEndTick - this.endTick) * rateOfChangeH,
        newStartRGB, newEndRGB);
  }

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
  public IMotion addMotions(IMotion m1) throws IllegalArgumentException {
    if (this.startTick != m1.getStartTick() || this.endTick != m1.getEndTick()
        || !this.noConflictingChanges(m1)) {
      throw new IllegalArgumentException("Illegal add motion changes.");
    }
    boolean[] change1 = this.bitwiseChangeList();
    boolean[] change2 = m1.bitwiseChangeList();
    String[] orderOfFields = {"x", "y", "w", "h"};

    // x
    double newStartX = 0;
    double newEndX = 0;
    if (change1[0] == change2[0]) {
      if (m1.getStartX() != this.startX) {
        throw new IllegalArgumentException("Cannot add motions that have conflicted unchanged "
            + "fields.");
      }
      newStartX = this.startX;
      newEndX = this.endX;
    } else if (change1[0]) {
      newStartX = this.startX;
      newEndX = this.endX;
    } else {
      newStartX = m1.getStartX();
      newEndX = m1.getEndX();
    }

    // y
    double newStartY = 0;
    double newEndY = 0;
    if (change1[1] == change2[1]) {
      if (m1.getStartY() != this.startY) {
        throw new IllegalArgumentException("Cannot add motions that have conflicted unchanged "
            + "fields.");
      }
      newStartY = this.startY;
      newEndY = this.endY;
    } else if (change1[1]) {
      newStartY = this.startY;
      newEndY = this.endY;
    } else {
      newStartY = m1.getStartY();
      newEndY = m1.getEndY();
    }

    // W
    double newStartW = 0;
    double newEndW = 0;
    if (change1[2] == change2[2]) {
      if (m1.getStartW() != this.startW) {
        throw new IllegalArgumentException("Cannot add motions that have conflicted unchanged "
            + "fields.");
      }
      newStartW = this.startW;
      newEndW = this.endW;
    } else if (change1[2]) {
      newStartW = this.startW;
      newEndW = this.endW;
    } else {
      newStartY = m1.getStartW();
      newEndW = m1.getEndW();
    }

    // H
    double newStartH = 0;
    double newEndH = 0;
    if (change1[3] == change2[3]) {
      if (m1.getStartH() != this.startH) {
        throw new IllegalArgumentException("Cannot add motions that have conflicted unchanged "
            + "fields.");
      }
      newStartH = this.startH;
      newEndH = this.endH;
    } else if (change1[2]) {
      newStartH = this.startH;
      newEndH = this.endH;
    } else {
      newStartH = m1.getStartH();
      newEndH = m1.getEndH();
    }

    // RGB
    int[] newRGBStart;
    int[] newRGBEnd;
    if (change1[4] == change2[4]) {
      if (!Arrays.equals(m1.getStartRGB(), this.startRGB) || !Arrays.equals(m1.getEndRGB(),
          this.endRGB)) {
        throw new IllegalArgumentException("Cannot add motions that have conflicted unchanged "
            + "fields.");
      }
      newRGBStart = this.startRGB;
      newRGBEnd = this.endRGB;
    } else if (change1[2]) {
      newRGBStart = this.startRGB;
      newRGBEnd = this.endRGB;
    } else {
      newRGBStart = m1.getStartRGB();
      newRGBEnd = m1.getEndRGB();
    }
    return new Motion(this.startTick, this.endTick, newStartX, newEndX, newStartY, newEndY,
        newStartW, newEndW, newStartH, newEndH, newRGBStart, newRGBEnd);
  }

  /**
   * Given two motions that are consecutive (i.e this motion is the next motion
   * directly after the given motion), check that all the end values of the given motion
   * match up with all the start values of the this motion.
   * @param m1 represents the consecutive motion that comes right before this motion
   * @return true if this motion's start values match the given motion's end values,
   *          false otherwise
   */
  public boolean validConsecutiveMotion(IMotion m1) {
    return (this.startX == m1.getEndX() && this.startY == m1.getEndY() &&
        this.startW == m1.getEndW()
        && this.startH == m1.getEndH() && Arrays.equals(this.startRGB, m1.getEndRGB()));
  }

  /**
   * Checks if this motion and the given motion have any overlapping changes/transformations that
   * change the same element of a shape during the same time period. For example, an invalid
   * overlap would be a motion that changes the x position of a shape from tick 3 to tick 5,
   * and another motion that also changes the x position of a shape from tick 4 to tick 6.
   *
   * @param m1 represents the given motions to check for any invalid conflicting changes
   * @return true if there are no invalid/conflicting changes made by the two motions, otherwise
   *
   */
  private boolean noConflictingChanges(IMotion m1) {
    // check if there is overlap between motions
    if (this.startTick >= m1.getStartTick() || this.endTick <= m1.getEndTick()) {
      // if the overlapping motions change the same element of the shape, invalid overlap
      boolean[] change1 = this.bitwiseChangeList();
      boolean[] change2 = m1.bitwiseChangeList();
      for (int ii = 0; ii < 5; ii += 1) {
        if (change1[ii] && change2[ii]) {
          return false;
        }
      }
    }
    // if no or valid overlap, return true
    return true;
  }

  /**
   * Represents the elements of a shape this motion changes as a list of booleans.
   * Each boolean in the list represents a change in x, y, width, height, or color respectively,
   * where a change in the element is represented by true and a lack of change by this motion
   * is represented by false.
   * E.g. a motion that changed the x position and color of a shape would be represented as:
   * [true, false, false, false, true].
   */
  public boolean[] bitwiseChangeList() {
    boolean[] bitwiseChanges = new boolean[5];
    bitwiseChanges[0] = startX - endX != 0;
    bitwiseChanges[1] = startY - endY != 0;
    bitwiseChanges[2] = startW - endW != 0;
    bitwiseChanges[3] = startH - endH != 0;
    bitwiseChanges[4] = !Arrays.equals(startRGB, endRGB);
    return bitwiseChanges;
  }

  /**
   * Executes this motion for a given Shape, changing its attributes to reflect
   * change in position, size, color, or hidden status to the end state of this motion.
   * @param shape the shape to execute motion for.
   */
  public void executeMotion(Shape shape) {
    shape.setColor(endRGB[0], endRGB[1] , endRGB[2]);
    shape.setSize(endW, endH);
    shape.setX(endX);
    shape.setY(endY);
  }

  /**
   * Generates the string representation of this Motion, including each of its attributes (ticks,
   * x position, y position, width, height, color).
   * @return the string rendering of this Motion.
   */
  public String toString() {
    // returns "startx starty starth startw startr startg startb endx endy endh endw endr endg endb"
    return this.startTick + " " + toStringStartValues() + " "
        + this.endTick + " " + toStringEndValues();
  }

  /**
   * Generates a formatted string of the start values of a motion.
   * @return the formatted string of a motion's start values.
   */
  public String toStringStartValues() {
    return String.format("%.2f %.2f %.2f %.2f %d %d %d",
        this.startX, this.startY, this.startH, this.startW, this.startRGB[0],
        this.startRGB[1], this.startRGB[2]);
  }

  /**
   * Generates a formatted string of the end values of a motion.
   * @return the formatted string of a motion's end values.
   */
  public String toStringEndValues() {
    return String.format("%.2f %.2f %.2f %.2f %d %d %d", this.endX, this.endY, this.endH, this.endW,
        this.endRGB[0], this.endRGB[1], this.endRGB[2]);
  }

  /**
   * Generates the string representation of this Motion, including each of its attributes (ticks,
   * x position, y position, width, height, color) as integers.
   * @return the string rendering of this Motion.
   */
  public String toStringAsInt() {
    // returns "startx starty starth startw startr startg startb endx endy endh endw endr endg endb"
    return this.startTick + " " + toStringStartValuesAsInt() + " "
        + this.endTick + " " + toStringEndValuesAsInt();
  }

  /**
   * Generates a formatted string of the start values of a motion as integers.
   * @return the formatted string of a motion's start values.
   */
  private String toStringStartValuesAsInt() {
    return String.format("%d %d %d %d %d %d %d",
        (int)this.startX, (int)this.startY, (int)this.startH, (int)this.startW, this.startRGB[0],
        this.startRGB[1], this.startRGB[2]);
  }

  /**
   * Generates a formatted string of the end values of a motion as integers.
   * @return the formatted string of a motion's end values.
   */
  private String toStringEndValuesAsInt() {
    return String.format("%d %d %d %d %d %d %d", (int)this.endX, (int) this.endY,
        (int)this.endH, (int)this.endW,
        this.endRGB[0], this.endRGB[1], this.endRGB[2]);
  }

  /**
   * Gets the first x value of this motion.
   * @return the first x value at the last tick of the motion.
   */
  public double getStartX() {
    return startX;
  }

  /**
   * Gets the end x value of this motion.
   * @return the end x value at the last tick of the motion.
   */
  public double getEndX() {
    return endX;
  }

  /**
   * Gets the first y value of this motion.
   * @return the first y value at the last tick of the motion.
   */
  public double getStartY() {
    return startY;
  }

  /**
   * Gets the final y value of this motion.
   * @return the end y value at the last tick of the motion.
   */
  public double getEndY() {
    return endY;
  }

  /**
   * Gets the first width value of this motion.
   * @return the first width value at the last tick of the motion.
   */
  public double getStartW() {
    return startW;
  }

  /**
   * Gets the final width value of this motion.
   * @return the end width value at the last tick of the motion.
   */
  public double getEndW() {
    return endW;
  }

  /**
   * Gets the final RGB value of this motion.
   * @return the end RGB value at the last tick of the motion.
   */
  public double getStartH() {
    return startH;
  }

  /**
   * Gets the final height value of this motion.
   * @return the end height value at the last tick of the motion.
   */
  public double getEndH() {
    return endH;
  }

  /**
   * Gets the first RGB value of this motion.
   * @return the first RGB value at the first tick of the motion.
   */
  public int[] getStartRGB() {
    return startRGB;
  }

  /**
   * Gets the final RGB value of this motion.
   * @return the end RGB value at the last tick of the motion.
   */
  public int[] getEndRGB() {
    return endRGB;
  }

}