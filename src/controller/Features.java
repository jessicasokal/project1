package controller;

/**
 * Encapsulates the various "action events" or requests that a user can make for an interactive
 * animation program, and the behavior they should trigger.
 */
public interface Features {

  /**
   * Triggers a change in the tick rate for the animation.
   * @param newSpeed represents the new tick rate, given in ticks per unit of time.
   */
  void changeSpeed(int newSpeed);

  /**
   * Triggers the animation to start from tick 0.
   */
  void restartAnimation();

  /**
   * Triggers the animation to toggle it's looping feature to the opposite state. Looping enabled
   * means that the animation will restart at tick 0 once the last motion has been fully executed.
   * Looping disabled means that the animation will run continuously until stopped.
   */
  void enableDisableLooping();

  /**
   * Triggers the animation to start playing from whatever its last rendered/executed state
   * was.
   */
  void resumeAnimation();

  /**
   * Triggers the animation to stop at whatever state it is at until the animation is
   * started back up again.
   */
  void pauseAnimation();

  /**
   * Triggers the animation to toggle it's discrete feature to the opposite state. Discrete
   * enabled means that the animation will only display frames at the start and end of its
   * motions. Discrete disabled means that the animation will display continuously a frame for every
   * tick.
   */
  void enableDisableDiscrete();

  /**
   * Triggers the animation to toggle whether all the shapes in the animation are filled
   * or just outlined.
   */
  void toggleFill();
}
