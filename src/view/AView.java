package view;

import controller.Features;

/**
 * Represents a general animation's non-visual view with an associated ViewModel that information
 * about the model can be gotten from, and canvas information.
 */
public abstract class AView implements IAnimationView {
  protected IAnimationViewModel am;
  protected int tickRate;
  protected int topLeftCornerX;
  protected int topLeftCornerY;
  protected int width;
  protected int height;

  /**
   * Constructs a general animation view with ViewModel containing information about the
   * animation's shapes and motions, and the desired tickRate (ticks per unit of time).
   *
   * @param am represents ViewModel containing information about the
   *      animation's shapes and motions
   * @param tickRate represents the desired tickRate (ticks per unit of time)
   * @throws IllegalArgumentException if animation is null or tick rate is <= 0
   */
  protected AView(IAnimationViewModel am, int tickRate) {
    if (am == null) {
      throw new IllegalArgumentException("View cannot be constructed with null parameters.");
    }
    if (tickRate <= 0) {
      throw new IllegalArgumentException("View cannot be constructed with a tick rate of "
          + "0 or less.");
    }
    this.am = am;
    this.tickRate = tickRate;
    this.topLeftCornerX = am.getOriginX();
    this.topLeftCornerY = am.getOriginY();
    this.width = am.getWidth();
    this.height = am.getHeight();
  }

  /**
   * Used for visual views to add features (requests) to the animation. Unnecessary in
   * non-visual views, therefore suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-visual view
   */
  @Override
  public void addFeatures(Features features) {
    throw new UnsupportedOperationException("Cannot add features to non-interactive view.");
  }

  /**
   * Used for visual views to alter shape attributes with each tick. Unnecessary in this
   * class, therefore suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-visual view
   */
  @Override
  public void updateTick() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Non-visual animation views cannot update ticks.");
  }

  /**
   * Used for visual views to restart the animation. Unnecessary in
   * non-visual views, therefore suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-visual view
   */
  @Override
  public void restart() {
    throw new UnsupportedOperationException("Cannot restart non-visual view.");
  }

  /**
   * Used for visual views to pause the animation at the current state. Unnecessary in
   * non-visual views, therefore suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-visual view
   */
  @Override
  public void pause() {
    throw new UnsupportedOperationException("Cannot pause non-visual view.");
  }

  /**
   * Used for visual views to resume the animation from the last executed state. Unnecessary in
   * non-visual views, therefore suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-visual view
   */
  @Override
  public void resume() {
    throw new UnsupportedOperationException("Cannot resume non-visual view.");
  }

  /**
   * Used for visual views to toggle the animation's looping ability. Unnecessary in
   * non-visual views, therefore suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-visual view
   */
  @Override
  public void toggleLooping() {
    throw new UnsupportedOperationException("Cannot toggle looping for non-visual view.");
  }

  @Override
  public void changeSpeed(int newSpeed) {
    if (newSpeed <= 0) {
      throw new IllegalArgumentException("Cannot change speed to less than 1.");
    }
    this.tickRate = newSpeed;
  }

  /**
   * Used for visual views to get the current tick it is rendering. Unnecessary in
   * non-visual views, therefore suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-visual view
   */
  @Override
  public int getTick() {
    throw new UnsupportedOperationException("Cannot get tick of non-visual view.");
  }

  @Override
  public int getSpeed() {
    return this.tickRate;
  }

  @Override
  public void toggleDiscrete() {
    throw new UnsupportedOperationException("Cannot toggle discrete playing "
        + "on non-interactive visual view.");
  }

  @Override
  public void toggleFill() {
    throw new UnsupportedOperationException("cannot toggle fill in non visual views");
  }
}
