import static org.junit.Assert.assertEquals;

import controller.Features;
import java.io.IOException;
import model.AnimationModel;
import model.SimpleAnimationModel;
import model.SimpleAnimationModel.ShapeType;
import org.junit.Test;
import view.IAnimationView;
import view.IAnimationViewModel;
import view.InteractiveVisualAnimationView;

/**
 * Tests the functionality and behavior of the interactive view.
 */
public class InteractiveViewTest {

  static private class MockView extends InteractiveVisualAnimationView {
    private final Appendable s;

    /**
     * Constructs a visual animation view frame with the given ViewModel as the source of
     * information about the shapes and motions in this animation, and the given tick rate.
     * Also constructs the various components of the frame/view that allow for user interactivity.
     *
     * @param av       represents the ViewModel for this animation
     * @param tickRate represents the given initial tick rate in ticks per unit of time
     */
    private MockView(IAnimationViewModel av, int tickRate, Appendable s) {
      super(av, tickRate);
      this.s = s;
    }

    private void runLoopingButton() {
      this.loopButton.doClick();
    }

    @Override
    public void resume() {
      super.resume();
      try {
        s.append("resume\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void pause() {
      super.pause();
      try {
        s.append("pause\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void restart() {
      super.restart();
      try {
        s.append("restart\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void toggleLooping() {
      super.toggleLooping();
      try {
        s.append("loop\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void changeSpeed(int newSpeed) {
      super.changeSpeed(newSpeed);
      try {
        s.append("speed\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void toggleFill() {
      super.toggleFill();
      try {
        s.append("fill\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void toggleDiscrete() {
      super.toggleDiscrete();
      try {
        s.append("discrete\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // restartButton, increaseButton, decreaseButton, pauseButton,
    // resumeButton, loopButton

    private void runRestartButton() {
      this.restartButton.doClick();
    }

    private void runIncreaseButton() {
      this.increaseButton.doClick();
    }

    private void runDecreaseButton() {
      this.decreaseButton.doClick();
    }

    private void runPauseButton() {
      this.pauseButton.doClick();
    }

    private void runResumeButton() {
      this.resumeButton.doClick();
    }

    private void runFillButton() {
      this.toggleFill.doClick();
    }

    private void runDiscreteButton() {
      this.discreteButton.doClick();
    }
  }

  // test the various methods triggered by features or timer in controller
  @Test
  public void testUpdateTick() throws IOException {
    AnimationModel am = new SimpleAnimationModel();

    IAnimationView view = new InteractiveVisualAnimationView(am, 10);
    am.startAnimation();
    view.updateTick();
    assertEquals(1, view.getTick());
    view.updateTick();
    assertEquals(2, view.getTick());
  }

  @Test
  public void testPauseResume() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder s = new StringBuilder();
    IAnimationView view = new MockView(am, 10, s);
    am.startAnimation();

    view.resume();
    view.pause();
    view.pause();
    view.resume();
    assertEquals("resume\npause\npause\nresume\n", s.toString());
  }

  @Test
  public void testRestart() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    IAnimationView view = new InteractiveVisualAnimationView(am, 10);
    am.startAnimation();

    assertEquals(0, view.getTick());
    view.updateTick();
    assertEquals(1, view.getTick());
    view.restart();
    assertEquals(0, view.getTick());
  }

  @Test
  public void testToggleLooping() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(1, 2, 3, 4, 5, 5, 7,
        8,
        9, 10, a, a, "R");
    IAnimationView view = new InteractiveVisualAnimationView(am, 10);
    am.startAnimation();

    assertEquals(0, view.getTick());
    view.updateTick();
    assertEquals(1, view.getTick());
    view.updateTick();
    view.updateTick();
    view.updateTick();
    assertEquals(4, view.getTick());

    view.restart();
    view.toggleLooping();
    assertEquals(0, view.getTick());
    view.updateTick();
    assertEquals(1, view.getTick());
    view.updateTick();
    assertEquals(0, view.getTick());

    view.toggleLooping();
    view.updateTick();
    view.updateTick();
    view.updateTick();
    assertEquals(3, view.getTick());
  }

  @Test
  public void testChangeSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    IAnimationView view = new InteractiveVisualAnimationView(am, 10);
    am.startAnimation();

    view.changeSpeed(12);
    assertEquals(12, view.getSpeed());
    view.changeSpeed(8);
    assertEquals(8, view.getSpeed());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidChangeSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    IAnimationView view = new InteractiveVisualAnimationView(am, 10);
    am.startAnimation();

    view.changeSpeed(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidAddFeatures() {
    AnimationModel am = new SimpleAnimationModel();
    IAnimationView view = new InteractiveVisualAnimationView(am, 10);
    am.startAnimation();

    view.addFeatures(null);
  }

  // test add features
  @Test
  public void testAddFeatures() {
    // create mock features object
    StringBuilder sb = new StringBuilder();
    AnimationModel am = new SimpleAnimationModel();

    Features featuresMock = new Features() {
      @Override
      public void changeSpeed(int newSpeed) {
        sb.append(newSpeed);
      }

      @Override
      public void restartAnimation() {
        sb.append("restart");
      }

      @Override
      public void enableDisableLooping() {
        sb.append("loop");
      }

      @Override
      public void resumeAnimation() {
        sb.append("resume");
      }

      @Override
      public void pauseAnimation() {
        sb.append("pause");
      }

      /**
       * Triggers the animation to toggle it's discrete feature to the opposite state.
       * Discrete enabled means that the animation will only display frames at the start and
       * end of its motions. Discrete disabled means that the animation will display
       * continuously a frame for every tick.
       */
      @Override
      public void enableDisableDiscrete() {
        sb.append("discrete");
      }

      /**
       * Triggers the animation to toggle whether all the shapes in the animation are filled
       * or just outlined.
       */
      @Override
      public void toggleFill() {
        sb.append("fill");
      }
    };

    IAnimationView view = new MockView(am, 10, sb);
    view.addFeatures(featuresMock);

    ((MockView)view).runRestartButton();
    ((MockView)view).runIncreaseButton();
    ((MockView)view).runDecreaseButton();
    ((MockView)view).runLoopingButton();
    ((MockView)view).runPauseButton();
    ((MockView)view).runResumeButton();
    ((MockView)view).runDiscreteButton();
    ((MockView)view).runFillButton();

    assertEquals("restart119looppauseresumediscretefill", sb.toString());
  }
}
