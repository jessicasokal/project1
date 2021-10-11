package model;

import java.util.Queue;
import model.SimpleAnimationModel.ShapeType;
import view.IAnimationViewModel;

/**
 * The model for creating an animation. Contains the methods to alter the shapes and their
 * corresponding motions, get shapes and motions, and start an animation.
 */
public interface AnimationModel extends IAnimationViewModel {

  /**
   * Starts an animation after all motions commands have been added, storing a copy of the motionMap
   * to keep the original motions, and setting the isStarted flag to true to allow the animation
   * to run.
   */
  void startAnimation();

  /**
   * Adds a shape with the given name and ShapeType to this animation's map of shapes.
   * @param name represents the name of the shape to be added (for example a circle can be
   *             named "C"), which is also used as the key for this animation's map of shapes.
   * @param shapeType represents the ShapeType of the shape to be added, for example a Circle
   *                  or Rectangle.
   * @throws IllegalArgumentException throws an exception if the given ShapeType is not either
   *                                  a Circle or a Rectangle.
   */
  void addShape(String name, ShapeType shapeType) throws IllegalArgumentException;

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
   *                                  (r, g, b) are not between 0 and 255, or the x, y, width, or
   *                                  height values are not within the size of the animation screen
   *                                  (0 - 600).
   *
   */
  void addShape(int r, int g, int b, String name, boolean hidden, double x, double y,
      double width, double height, ShapeType shapeType) throws IllegalArgumentException;


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
  void addMotion(int startTick, int endTick, double startX, double endX, double startY, double endY,
      double startW, double endW, double startH, double endH, int[] startRGB, int[] endRGB,
      String name) throws IllegalArgumentException, IllegalStateException;

  /**
   * Removes a shape with the given name and all motions related to the shape from this
   * animation.
   *
   * @param name represents the name of the shape to be removed.
   * @throws IllegalArgumentException if name is null or this animation does not have a shape of
   *                       the given name
   */
  void removeShape(String name) throws IllegalArgumentException;

  /**
   * Removes all motions related to the given shape name from this animation's motion list, or
   * does nothing if no motions exist relating to the given shape name.
   *
   * @param name represents the name of the shape to remove motions of
   * @throws IllegalArgumentException if no shape exists in this animation with given name
   */
  void removeMotion(String name) throws IllegalArgumentException;

  /**
   * Returns whether this animation has started or not.
   */
  boolean isAnimationStarted();

  /**
   * Sets the canvas size of this model.
   * @param x the x coordinate of the origin of this model.
   * @param y the y coordiante of the origin of this model.
   * @param w the width of the canvas of this model.
   * @param h the height of the canvas of this model.
   * @throws IllegalArgumentException if the height or width values are negative.
   */
  void setCanvas(int x, int y, int w, int h) throws IllegalArgumentException;

  /**
   * Returns the tick rate of the tempo specified at the given tick. If there is no tempo
   * occuring during the given tick, 0 is returned.
   *
   * @param tick represents the current tick that the tempo should be at
   *  @return the tick rate of the tempo occuring at the given tick, or 0 if there is no tempo
   *          occurring
   * @throws IllegalArgumentException if tick is negative
   */
  int getTempo(int tick) throws IllegalArgumentException;

  /**
   * Adds a tempo over the given time interval with the specified tick rate to this animation's
   * list of tempo commands.
   * @param start represents the start tick of the time interval for this tempo command
   * @param end represents the end tick of the time interval for this tempo command
   * @param tempo represents the tick rate specified for this tempo command
   * @throws IllegalArgumentException if the time interval overlaps with a time interval of another
   *          tempo from this animation
   */
  void addTempo(int start, int end, int tempo) throws IllegalArgumentException;

  /**
   * Returns a copy of this animation's list of tempos.
   * @return a copy of this animation's tempos
   */
  Queue<ITempo> getTempos();
}
