package controller;

import model.AnimationModel;
import view.IAnimationView;

/**
 * Represents the controller for any Animation view and animation model that can control the
 * behavior of the animation.
 */
public abstract class MVCController implements IController {
  // fields are protected because we create a mock controller class that
  // extends this one in our testing, and we need access to these fields in child classes that
  // extend this one

  protected final AnimationModel model;
  protected final IAnimationView view;
  protected int tickRate;

  /**
   * Constructs a new controller with the given model, view, and tick rate.
   *
   * @param model represents the animation model that holds the information about this animation
   * @param view represents the animation view that renders this animation
   * @param speed represents the initial tick rate of this animation, given as ticks per unit
   * @throws IllegalArgumentException if model or view are null, or if speed is <= 0
   */
  public MVCController(AnimationModel model, IAnimationView view, int speed) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Controller cannot be constructed with null params.");
    }
    if (speed <= 0) {
      throw new IllegalArgumentException("View cannot be constructed with a tick rate of "
          + "0 or less.");
    }
    this.model = model;
    this.view = view;
  }

  @Override
  public void animationGo() {
    model.startAnimation();
  }
}
