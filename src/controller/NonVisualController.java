package controller;

import java.io.IOException;
import model.AnimationModel;
import view.IAnimationView;

/**
 * Represents a controller for an animation view and model that represent a non-visual,
 * non-timer-based rendering of the animation.
 */
public class NonVisualController extends MVCController {

  /**
   * Constructs a new controller with the given model, non-visual view, and tick rate.
   *
   * @param model represents the animation model that holds the information about this animation
   * @param view  represents the animation view that renders this animation
   * @param speed represents the initial tick rate of this animation, given as ticks per unit
   * @throws IllegalArgumentException if model or view are null, or if speed is <= 0
   */
  public NonVisualController(AnimationModel model, IAnimationView view, int speed) {
    super(model, view, speed);
  }

  @Override
  public void animationGo() {
    super.animationGo();
    try {
      view.render();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
