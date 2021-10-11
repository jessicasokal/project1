package view;

import controller.Features;
import java.io.IOException;

/**
 * Represents the view component of an animation containing shapes and their corresponding
 * motions. The specific representation of the animation elements is variable.
 */
public interface IAnimationView {

  /**
   * Produces the rendering of this animation view, representing the motions and shapes
   * as per this view's specific rendering protocol.
   *
   * @throws IOException if there is an error in rendering the view to a given output
   */
  void render() throws IOException;

  /**
   * Increases the current tick of this view to render by one.
   */
  void updateTick() throws IOException;

  /**
   * Adds the various features (requests) that this view supports as a listener to any relevant
   * components of this view.
   * @param features represents the Features object that will act as a listener
   * @throws IllegalArgumentException if features is null
   */
  void addFeatures(Features features) throws IllegalArgumentException;

  /**
   * Pause this animation, halting any further rendering until unpaused.
   */
  void pause();

  /**
   * Start playing the animation after a pause or from its last executed state.
   */
  void resume();

  /**
   * Restart this animation's view from tick 0.
   */
  void restart();

  /**
   * Toggle the ability to loop (i.e. when the last motion in the animation has finished,
   * automatically start back at tick 0) based on the view's current ability to loop.
   */
  void toggleLooping();

  /**
   * Toggle the ability to show all the shapes as filled or just outlined.
   */
  void toggleFill();

  /**
   * Change the tick rate of this view to the given speed.
   * @param newSpeed represents the new tick rate, given in ticks per unit of time
   * @throws IllegalArgumentException if newSpeed is less than or equal to 0
   */
  void changeSpeed(int newSpeed) throws IllegalArgumentException;

  /**
   * Returns the current tick that this view is on.
   * @return this view's current tick
   */
  int getTick();

  /**
   * Returns the current tick rate of this view.
   * @return this view's tick rate
   */
  int getSpeed();

  /**
   * Toggle the ability to render the animation discretely or continuously. Discrete
   * enabled means that the animation will only display frames at the start and end of its
   * motions. Discrete disabled means that the animation will display continuously a frame for every
   * tick.
   */
  void toggleDiscrete();
}