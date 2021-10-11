package model;

import cs3500.animator.util.AnimationBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import view.IAnimationViewModel;

/**
 * Class representing a simple animation model: this model maintains the state and enforces
 * the rules of specifically a simple animation.
 */
public class SimpleAnimationModel implements AnimationModel, IAnimationViewModel {
  // represents the map from shape names to shape objects
  private final Map<String, Shape> shapes;
  // represents an ordered list of shapes, based on the order they were inputted
  private final List<Shape> orderedShapes;
  // represents the map from shapes to their respective queues (represented as Priority queues)
  private final Map<Shape, Queue<IMotion>> motionMap;
  private boolean isStarted;
  private int canvasWidth;
  private int canvasHeight;
  private int originX;
  private int originY;
  private final Queue<ITempo> tempos;

  /**
   * Represents the builder class for this model, which can add shapes and motions to the model
   * it has.
   */
  public static final class Builder implements AnimationBuilder<AnimationModel> {
    AnimationModel model = new SimpleAnimationModel();

    /**
     * Constructs a final document.
     *
     * @return the newly constructed document
     */
    @Override
    public AnimationModel build() {
      return model;
    }

    /**
     * Specify the bounding box to be used for the animation.
     *
     * @param x      The leftmost x value
     * @param y      The topmost y value
     * @param width  The width of the bounding box
     * @param height The height of the bounding box
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<AnimationModel> setBounds(int x, int y, int width, int height) {
      model.setCanvas(x, y, width, height);
      return this;
    }

    /**
     * Adds a new shape to the growing document.
     *
     * @param name The unique name of the shape to be added. No shape with this name should already
     *             exist.
     * @param type The type of shape (e.g. "ellipse", "rectangle") to be added. The set of supported
     *             shapes is unspecified, but should include "ellipse" and "rectangle" as a
     *             minimum.
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<AnimationModel> declareShape(String name, String type) {
      ShapeType shapeType;
      switch (type) {
        case "rectangle":
          shapeType = ShapeType.RECTANGLE;
          break;
        case "ellipse":
          shapeType = ShapeType.CIRCLE;
          break;
        case "plus":
          shapeType = ShapeType.PLUS;
          break;
        default:
          shapeType = null;
      }
      model.addShape(name, shapeType);
      return this;
    }

    /**
     * Adds a transformation to the growing document.
     *
     * @param name The name of the shape (added with {@link AnimationBuilder#declareShape})
     * @param t1   The start time of this transformation
     * @param x1   The initial x-position of the shape
     * @param y1   The initial y-position of the shape
     * @param w1   The initial width of the shape
     * @param h1   The initial height of the shape
     * @param r1   The initial red color-value of the shape
     * @param g1   The initial green color-value of the shape
     * @param b1   The initial blue color-value of the shape
     * @param t2   The end time of this transformation
     * @param x2   The final x-position of the shape
     * @param y2   The final y-position of the shape
     * @param w2   The final width of the shape
     * @param h2   The final height of the shape
     * @param r2   The final red color-value of the shape
     * @param g2   The final green color-value of the shape
     * @param b2   The final blue color-value of the shape
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<AnimationModel> addMotion(String name, int t1, int x1, int y1, int w1,
        int h1, int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2,
        int b2) {
      int[] rgb1 = {r1, g1, b1};
      int[] rgb2 = {r2, g2, b2};

      model.addMotion(t1, t2, x1, x2, y1, y2, w1, w2, h1, h2, rgb1, rgb2, name);
      return this;
    }

    /**
     * Adds a tempo over a given time interval to the growing document.
     *
     * @param start The start tick of the time interval
     * @param end   The end tick of the time interval
     * @param speed The tempo specified for the time interval
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder<AnimationModel> addTempo(int start, int end, int speed) {
      model.addTempo(start, end, speed);
      return this;
    }
  }

  /**
   * Constructs a SimpleAnimationModel in which each the list of shapes, map of motions to shapes,
   * and original map of motions to shapes are initialized to empty, gameStarted is initialized to
   * false, and the currentTick begins at 0.
   */
  public SimpleAnimationModel() {
    // INVARIANT: Tick value is >= 0
    // INVARIANT: All motions in an animation must be associated with a created shape
    // INVARIANT: A game in the started state has all consecutive motions' for a given shape
    // respective start and end positions, dimensions, and color the same
    // INVARIANT: An animation model cannot have more than one motion executing at any given
    // tick value, with the exception of teleportation, represented by a motion with the same
    // start and end tick value
    this.shapes = new HashMap<>();
    this.motionMap = new HashMap<>();
    this.isStarted = false;
    this.orderedShapes = new ArrayList<>();
    this.tempos = new PriorityQueue<>(Tempo.TIME_INTERVAL_COMP);
  }

  /**
   * Enum representing a the type of a Shape. In this model, we support Rectangle and
   * Circle ShapesTypes.
   */
  public enum ShapeType { RECTANGLE, CIRCLE, PLUS }


  /**
   * Starts an animation after all motions commands have been added and checked for validity,
   * and setting the isStarted flag to true to allow the animation to run.
   *
   * @throws IllegalStateException if the in-order motions for any shape have jumps between
   *          the end shape state of one motion and the start shape state of the next motion
   */
  @Override
  public void startAnimation() throws IllegalStateException {
    // enforce class invariant where for a given shape,
    // all consecutive motions' must have the same respective
    // end and start data
    for (Queue<IMotion> q : motionMap.values()) {
      Queue<IMotion> motionQueueCopy = new PriorityQueue<>(q);
      while (motionQueueCopy.size() >= 2) {
        IMotion motion1 = motionQueueCopy.remove();
        IMotion motion2 = motionQueueCopy.element();
        if (!motion2.validConsecutiveMotion(motion1)) {
          throw new IllegalStateException("Game must be started without shape state"
              + " jumps between consecutive motions.");
        }
      }
    }

    this.isStarted = true;
  }

  /**
   * Adds a shape with the given name and ShapeType to this animation's map of shapes.
   * @param name represents the name of the shape to be added (for example a circle can be
   *             named "C"), which is also used as the key for this animation's map of shapes.
   * @param shapeType represents the ShapeType of the shape to be added, for example a Circle
   *                  or Rectangle.
   * @throws IllegalArgumentException throws an exception if the given ShapeType is not either
   *                                  a Circle or a Rectangle.
   * @throws IllegalStateException if animation has already been started
   */
  @Override
  public void addShape(String name, ShapeType shapeType)
      throws IllegalArgumentException, IllegalStateException {
    if (this.isStarted) {
      throw new IllegalStateException("Modifications to animation cannot be made once animation "
          + "has started");
    }
    if (name == null) {
      throw new IllegalArgumentException("Cannot add shape with null parameters.");
    }
    Shape newShape;
    switch (shapeType) {
      case RECTANGLE:
        newShape = new Rectangle(name);
        break;
      case CIRCLE:
        newShape = new Ellipse(name);
        break;
      case PLUS:
        newShape = new Plus(name);
        break;
      default:
        throw new IllegalArgumentException("Invalid shape type.");
    }
    shapes.put(name, newShape);
    orderedShapes.add(newShape);
    motionMap.put(newShape, new PriorityQueue<>(Motion.TIME_INTERVAL_COMP));
  }


  /**
   * Adds a shape with the given attributes to this animation's map of shapes.
   * @param r represents how red (from 0 - 255) to make the shape to add to this animation.
   * @param g represents how green (from 0 - 255) to make the shape to add to this animation.
   * @param b represents how blue (from 0 - 255) to make the shape to add to this animation.
   * @param name represents the identifying name of the shape to add to this animation (for example
   *             a circle's name can be "C")
   * @param hidden represents whether the shape is hidden or visible in this animation.
   * @param x represents the x position of the shape to be added, which must be within the size of
   *          the animation screen (0 - 600).
   * @param y represents the y position of the shape to be added, which must be within the size of
   *          the animation screen (0 - 600).
   * @param width represents the width of the shape to be added, which must be within the size of
   *              the animation screen (0 - 600).
   * @param height represents the height of the shape to be added, which must be within the size of
   *               the animation screen (0-600).
   * @param shapeType represents the type of shape to be added (either Circle or Rectangle) to this
   *                  animation
   * @throws IllegalArgumentException throws an exception if the values representing colors
   *                                  (r, g, b) are not between 0 and 255, or if name is null
   * @throws IllegalStateException if animation has already been started
   *
   */
  @Override
  public void addShape(int r, int g, int b, String name, boolean hidden, double x, double y,
      double width, double height, ShapeType shapeType)
      throws IllegalArgumentException, IllegalStateException {
    if (this.isStarted) {
      throw new IllegalStateException("Modifications to animation cannot be made once animation "
          + "has started");
    }
    if (name == null) {
      throw new IllegalArgumentException("Cannot add shape with null parameters.");
    }
    if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
      throw new IllegalArgumentException("Values representing colors must be between"
          + "0 and 255");
    }
    if (height < 0 || width < 0) {
      throw new IllegalArgumentException("Values representing height or width cannot be negative");
    }
    Shape newShape;
    switch (shapeType) {
      case RECTANGLE:
        newShape = new Rectangle(r, g, b, name, hidden, x, y, width, height);
        break;
      case CIRCLE:
        newShape = new Ellipse(r, g, b, name, hidden, x, y, width, height);
        break;
      case PLUS:
        newShape = new Plus(r, g, b, name, hidden, x, y, width, height);
        break;
      default:
        throw new IllegalArgumentException("Invalid ShapeType");
    }
    shapes.put(name, newShape);
    orderedShapes.add(newShape);
    motionMap.put(newShape, new PriorityQueue<>(Motion.TIME_INTERVAL_COMP));
  }

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
   * @param name represents the name of the shape the motion corresponds with
   * @throws IllegalStateException if animation is already started
   * @throws IllegalArgumentException if name or startRBG or endRGB is null or out of bounds
   *
   */
  @Override
  public void addMotion(int startTick, int endTick, double startX, double endX, double startY,
      double endY, double startW, double endW, double startH, double endH, int[] startRGB,
      int[] endRGB, String name)
      throws IllegalArgumentException, IllegalStateException {
    if (this.isStarted) {
      throw new IllegalStateException("Modifications to animation cannot be made once animation "
          + "has started");
    }
    if (name == null || startRGB == null || endRGB == null) {
      throw new IllegalArgumentException("Cannot add motion with null parameters.");
    }
    // enforcing invariant
    if (!shapes.containsKey(name)) {
      throw new IllegalArgumentException("Cannot add motion with shape " + name + " that doesn't "
          + "exist in animation.");
    }
    // enforcing invariant
    if (startTick < 0 || endTick < 0) {
      throw new IllegalStateException("Tick value cannot be negative");
    }
    Queue<IMotion> motionQueue = motionMap.get(shapes.get(name));
    IMotion newMotion = new Motion(startTick, endTick, startX, endX, startY, endY, startW, endW,
        startH, endH, startRGB, endRGB);
    // addToQueue enforces our invariant that only one motion can exist for any given tick value
    motionMap.put(shapes.get(name), newMotion.addToQueue(motionQueue));
  }

  /**
   * Removes a shape with the given name and all motions related to that shape from this animation.
   *
   * @param name represents the name of the shape to be removed.
   * @throws IllegalArgumentException if name is null or this animation does not have a shape of the
   *                                  given name
   * @throws IllegalStateException if animation has already been started
   */
  @Override
  public void removeShape(String name) throws IllegalArgumentException, IllegalStateException {
    if (this.isStarted) {
      throw new IllegalStateException("Modifications to animation cannot be made once animation "
          + "has started");
    }
    if (name == null || shapes.get(name) == null) {
      throw new IllegalArgumentException("Given shape: " + name + " does not exist in"
          + "list of shapes");
    }
    removeMotion(name);
    shapes.remove(name);
  }


  /**
   * Gets the list of shapes that are in this animation and returns a copy of them.
   * @return the list of shapes that are in this animation
   */
  @Override
  public Map<String, Shape> getShapes() {
    return new HashMap<>(this.shapes);
  }

  /**
   * Gets a list of shapes in this model, in the order they were inputted.
   * @return the list of shapes of this animation model, in the order they were inputted.
   */
  @Override
  public List<Shape> getOrderedShapes() {
    return new ArrayList<>(this.orderedShapes);
  }

  /**
   * Gets the list of motions associated with a given shape and returns a copy of them.
   * @param s the shape to get the motions of.
   * @return the motions associated with a given shape.
   */
  @Override
  public Queue<IMotion> getShapeMotions(Shape s) {
    return new PriorityQueue<>(this.motionMap.get(s));
  }

  /**
   * Gets the list of motions associated with a given shape.
   *
   * @param s the name of the shape to get the motions of.
   * @return the motions associated with a given shape.
   */
  @Override
  public Queue<IMotion> getShapeMotions(String s) {
    return getShapeMotions(this.shapes.get(s));
  }

  /**
   * Gets the x coordinate of the origin of this model's canvas.
   * @return the x coordinate of the origin of this model's canvas.
   */
  @Override
  public int getOriginX() {
    return this.originX;
  }

  /**
   * Gets the y coordinate of the origin of this model's canvas.
   * @return the y coordinate of the origin of this model's canvas.
   */
  @Override
  public int getOriginY() {
    return this.originY;
  }

  /**
   * Gets the width of this model's canvas.
   * @return the width of the origin of this model's canvas.
   */
  @Override
  public int getWidth() {
    return this.canvasWidth;
  }

  /**
   * Gets the height of this model's canvas.
   * @return the width of the origin of this model's canvas.
   */
  @Override
  public int getHeight() {
    return this.canvasHeight;
  }

  /**
   * Determines if there are any motions occuring during or after the given tick, i.e. if the
   * animation is over.
   *
   * @param tick represents the tick value to check against
   * @throws IllegalArgumentException if tick value is negative
   * @return true if there are no motions after the given tick; false if otherwise.
   */
  @Override
  public boolean isAnimationOver(int tick) throws IllegalArgumentException {
    if (tick < 0) {
      throw new IllegalArgumentException("Negative tick value is not allowed.");
    }
    for (Shape shape : orderedShapes) {
      for (IMotion motion : motionMap.get(shape)) {
        if (motion.compareToTick(tick) != -1) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int findNextTick(int tick) {
    if (tick < 0) {
      throw new IllegalArgumentException("Negative tick value is not allowed.");
    }
    int nextTick = -1;

    for (Shape shape : orderedShapes) {
      Queue<IMotion> motions = this.getShapeMotions(shape);
      IMotion motion = motions.poll();
      while (motion != null) {
        if (motion.getStartTick() >= tick && (nextTick == -1 || motion.getStartTick() < nextTick)) {
          nextTick = motion.getStartTick();
          break;
        } else if (motion.getEndTick() >= tick
            && (nextTick == -1 || motion.getEndTick() < nextTick)) {
          nextTick = motion.getEndTick();
          break;
        }
        motion = motions.poll();
      }
    }

    if (nextTick == -1) {
      return tick;
    } else {
      return nextTick;
    }
  }

  /**
   * Removes all motions related to the given shape name from this animation's motion list, or does
   * nothing if no motions exist relating to the given shape name.
   *
   * @param name represents the name of the shape to remove motions of
   * @throws IllegalArgumentException if no shape exists in this animation with given name, or name
   *          is null
   * @throws IllegalStateException if animation has already been started
   */
  @Override
  public void removeMotion(String name) throws IllegalArgumentException, IllegalStateException {
    if (this.isStarted) {
      throw new IllegalStateException("Modifications to animation cannot be made once animation "
          + "has started");
    }
    if (name == null) {
      throw new IllegalArgumentException("Cannot add shape with null parameters.");
    }
    if (!shapes.containsKey(name)) {
      throw new IllegalArgumentException("Cannot remove motion for shape " + name + " that doesn't "
          + "exist in animation.");
    }
    motionMap.replace(shapes.get(name), new PriorityQueue<>(Motion.TIME_INTERVAL_COMP));
  }

  /**
   * Generates the string representation of this Model, including the shapes in it and their
   * related motions. Bases time on seconds instead of ticks.
   * @return the string rendering of this Model.
   */
  @Override
  public String toString() {
    String result = "";
    for (String shapeName : shapes.keySet()) {
      Shape shape = shapes.get(shapeName);
      result += shape.toString() + "\n";
      for (IMotion motion : motionMap.get(shape)) {
        result += "motion " + shapeName + " " + motion.toString() + "\n";
      }
    }
    return result;
  }

  /**
   * Returns whether this animation has started or not.
   */
  public boolean isAnimationStarted() {
    return this.isStarted;
  }

  /**
   * Sets the canvas size of this model.
   * @param x the x coordinate of the origin of this model.
   * @param y the y coordiante of the origin of this model.
   * @param w the width of the canvas of this model.
   * @param h the height of the canvas of this model.
   * @throws IllegalArgumentException if the height or width values are negative.
   */
  @Override
  public void setCanvas(int x, int y, int w, int h) throws IllegalArgumentException {
    if (w < 0 || h < 0) {
      throw new IllegalArgumentException("Height or width cannot be negative.");
    }
    this.originX = x;
    this.originY = y;
    this.canvasWidth = w;
    this.canvasHeight = h;
  }

  @Override
  public int getTempo(int tick) throws IllegalArgumentException {
    if (tick < 0) {
      throw new IllegalArgumentException("Tick value cannot be negative.");
    }
    Queue<ITempo> tempQueue = new PriorityQueue<>(this.tempos);
    ITempo tempo = tempQueue.poll();
    while (tempo != null) {
      if (tempo.compareToTick(tick) == 0) {
        return tempo.getTempo();
      } else if (tempo.compareToTick(tick) > 0) {
        break;
      }
      tempo = tempQueue.poll();
    }
    return 0;
  }

  @Override
  public void addTempo(int start, int end, int tempo) {
    if (tempo <= 0) {
      throw new IllegalArgumentException("Cannot add tempo with a speed less than 1.");
    }
    if (start < 0 || end < 0) {
      throw new IllegalArgumentException("Tick values cannot be negative.");
    }

    ITempo newTempo = new Tempo(start, end, tempo);

    Queue<ITempo> tempQueue = new PriorityQueue<>(this.tempos);
    ITempo currTempo = tempQueue.poll();
    // check to make sure there are no tempo overlaps
    while (currTempo != null) {
      if (currTempo.compareToTick(start) == 0 || currTempo.compareToTick(end) == 0) {
        throw new IllegalArgumentException("Cannot add tempo with overlapping time interval.");
      } else if (currTempo.compareToTick(end) > 0) {
        break;
      }
      currTempo = tempQueue.poll();
    }
    tempos.add(newTempo);
  }

  @Override
  public Queue<ITempo> getTempos() {
    return new PriorityQueue<>(this.tempos);
  }
}
