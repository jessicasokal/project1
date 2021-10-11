package controller;

import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Timer;
import model.AnimationModel;
import view.IAnimationView;

/**
 * Represents a controller for an animation view and model that represent a visual, timer-based
 * rendering of the animation. Can support user interactivity.
 */
public class VisualController extends MVCController {
  protected final Features features;
  protected final Timer timer;
  private int slowMoTempo;

  /**
   * Constructs a new controller with the given model, visual view, and tick rate.
   * Also sets up the timer to be used for view.
   *
   * @param model represents the animation model that holds the information about this animation
   * @param view  represents the animation view that renders this animation
   * @param speed represents the initial tick rate of this animation, given as ticks per unit
   * @throws IllegalArgumentException if model or view are null, or if speed is <= 0
   */
  public VisualController(AnimationModel model, IAnimationView view, int speed) {
    super(model, view, speed);
    this.tickRate = speed;
    this.slowMoTempo = 0;
    int delay = 1000 / speed;
    this.features = new AnimationFeatures();
    ActionListener taskPerformer = e -> {
      try {
        view.updateTick();
        checkSlowMo();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    };
    this.timer = new Timer(delay, taskPerformer);
  }

  /**
   * Represents the various features (requests) that are included in an interactive view
   * and the expected behavior between the controller and view for each one.
   */
  protected class AnimationFeatures implements Features {

    @Override
    public void changeSpeed(int newSpeed) {
      if (slowMoTempo <= 0) {
        if (newSpeed < 1) {
          tickRate = 1;
          view.changeSpeed(tickRate);
        } else {
          tickRate = newSpeed;
          view.changeSpeed(newSpeed);
        }
        timer.setDelay(1000 / tickRate);
      }
    }

    @Override
    public void restartAnimation() {
      view.restart();
      timer.restart();
    }

    @Override
    public void enableDisableLooping() {
      view.toggleLooping();
    }

    @Override
    public void resumeAnimation() {
      timer.start();
      view.resume();
    }

    @Override
    public void pauseAnimation() {
      timer.stop();
      view.pause();
    }

    /**
     * Triggers the animation to toggle it's discrete feature to the opposite state. Discrete
     * enabled means that the animation will only display frames at the start and end of its
     * motions. Discrete disabled means that the animation will display continuously a frame for
     * every tick.
     */
    @Override
    public void enableDisableDiscrete() {
      view.toggleDiscrete();
    }

    @Override
    public void toggleFill() {
      view.toggleFill();
    }
  }

  @Override
  public void animationGo() {
    super.animationGo();
    try {
      view.addFeatures(features);
    } catch (UnsupportedOperationException e) {
      timer.start();
    }
  }

  /**
   * Checks if there should be slow motion activated for the current tick. If there should be,
   * changes the tick rate appropriately to the given tempo. If not, the most recent non-slowmo
   * tick rate is restored.
   */
  protected void checkSlowMo() {
    slowMoTempo = this.model.getTempo(this.view.getTick());
    if (slowMoTempo > 0) {
      view.changeSpeed(slowMoTempo);
      timer.setDelay(1000 / slowMoTempo);
    } else {
      view.changeSpeed(tickRate);
      timer.setDelay(1000 / tickRate);
    }
  }
}

