import static org.junit.Assert.assertEquals;

import controller.IController;
import controller.VisualController;
import java.io.IOException;
import model.AnimationModel;
import model.SimpleAnimationModel;
import org.junit.Test;
import view.IAnimationView;
import view.IAnimationViewModel;
import view.TextualAnimationView;
import view.VisualAnimationView;
import view.InteractiveVisualAnimationView;

/**
 * Tests for visual controller and expected behavior, along with the Features object it contains.
 */
public class VisualControllerTest {

  /**
   * Represents a mock view for testing that outputs a log of its actions, as well as perform
   * all other behavior of an interactive visual view.
   */
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
    @Override
    public void toggleFill() {
      super.toggleFill();
      try {
        s.append("fill\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

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

    private int getTickRate() {
      return this.tickRate;
    }
  }

  /**
   * Represents a mock controller for testing that outputs a log of its functions.
   */
  static private class MockController extends VisualController {

    private final Appendable s;

    /**
     * Constructs a new controller with the given model, view, and tick rate. Also sets up the timer
     * to be used for any visual views.
     *
     * @param model represents the animation model that holds the information about this animation
     * @param view  represents the animation view that renders this animation
     * @param speed represents the initial tick rate of this animation, given as ticks per unit
     * @throws IllegalArgumentException if model or view are null, or if speed is <= 0
     */
    public MockController(AnimationModel model, IAnimationView view, int speed,
        Appendable s) {
      super(model, view, speed);
      this.s = s;
    }

    @Override
    public void animationGo() {
      super.animationGo();
      try {
        if (view instanceof VisualAnimationView) {
          s.append("visual view go");
        } else if (view instanceof InteractiveVisualAnimationView) {
          s.append("interactive view go");
        }
        this.model.startAnimation();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    protected void checkSlowMo() {
      super.checkSlowMo();
      try {
        s.append("slowmo");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // test constructor errors
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    IController c = new VisualController(null,
        new TextualAnimationView(new SimpleAnimationModel(), 3), 3);
  }

  @Test(expected =  IllegalArgumentException.class)
  public void testNullView() {
    IController c = new VisualController(new SimpleAnimationModel(),
        null, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalSpeed() {
    IController c = new VisualController(new SimpleAnimationModel(),
        new TextualAnimationView(new SimpleAnimationModel(), 3), 0);
  }

  // test go method with visual view
  @Test
  public void testVisualViewGo() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new VisualAnimationView(am, 10);

    IController c = new MockController(am, av, 10, sb);
    c.animationGo();

    assertEquals(sb.toString(), "visual view go");
  }

  // test go method with interactive view
  @Test
  public void testInteractiveViewGo() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new InteractiveVisualAnimationView(am, 10);

    IController c = new MockController(am, av, 10, sb);
    c.animationGo();

    assertEquals(sb.toString(), "interactive view go");
  }

  // test the various feature functions and their effect on the controller
  @Test
  public void testFeatureChangeSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new MockView(am, 10, sb);

    IController c = new VisualController(am, av, 10);
    c.animationGo();

    ((MockView)av).runIncreaseButton();
    assertEquals("speed\n", sb.toString());
    assertEquals(11, av.getSpeed());
    ((MockView)av).runDecreaseButton();
    assertEquals("speed\nspeed\n", sb.toString());
    assertEquals(10, av.getSpeed());
  }

  @Test
  public void testFeatureRestart() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new MockView(am, 10, sb);

    IController c = new VisualController(am, av, 10);
    c.animationGo();

    ((MockView)av).runRestartButton();
    assertEquals("restart\n", sb.toString());
  }

  @Test
  public void testFeatureLooping() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new MockView(am, 10, sb);

    IController c = new VisualController(am, av, 10);
    c.animationGo();

    ((MockView)av).runLoopingButton();
    assertEquals("loop\n", sb.toString());
  }

  @Test
  public void testFeatureResume() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new MockView(am, 10, sb);

    IController c = new VisualController(am, av, 10);
    c.animationGo();

    ((MockView)av).runResumeButton();
    assertEquals("resume\n", sb.toString());
  }

  @Test
  public void testFeaturePause() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new MockView(am, 10, sb);

    IController c = new VisualController(am, av, 10);
    c.animationGo();

    ((MockView)av).runPauseButton();
    assertEquals("pause\n", sb.toString());
  }

  @Test
  public void testFeatureFill() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new MockView(am, 10, sb);

    IController c = new VisualController(am, av, 10);
    c.animationGo();

    ((MockView)av).runFillButton();
    assertEquals("fill\n", sb.toString());
  }

  // test feature discrete vs. continuous
  @Test
  public void testFeatureDiscrete() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new MockView(am, 10, sb);

    IController c = new VisualController(am, av, 10);
    c.animationGo();

    ((MockView)av).runDiscreteButton();
    assertEquals("discrete\n", sb.toString());
  }

  // test the slowmo functionality
  @Test
  public void testSlowMo() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    am.addTempo(1, 2, 5);
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new MockView(am, 30, sb);

    IController c = new MockController(am, av, 30, sb);
    c.animationGo();
    ((MockController)c).checkSlowMo();
    assertEquals(30, ((MockView)av).getTickRate());
    av.updateTick();
    ((MockController)c).checkSlowMo();
    assertEquals(5, ((MockView)av).getTickRate());
    av.updateTick();
    ((MockController)c).checkSlowMo();
    assertEquals(30, ((MockView)av).getTickRate());
    av.updateTick();
    ((MockController)c).checkSlowMo();
    assertEquals(30, ((MockView)av).getTickRate());
  }
}
