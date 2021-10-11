import static org.junit.Assert.assertEquals;

import controller.Features;
import cs3500.animator.util.AnimationReader;
import java.io.FileReader;
import java.io.IOException;
import model.AnimationModel;
import model.Motion;
import model.SimpleAnimationModel;
import model.SimpleAnimationModel.Builder;
import model.SimpleAnimationModel.ShapeType;
import org.junit.Test;
import view.IAnimationView;
import view.SVGAnimationView;
import view.TextualAnimationView;

/**
 * Tests the expected behavior of the textual animation view class.
 */
public class TextualAnimationViewTest {


  // test for unsupported ops
  @Test(expected = UnsupportedOperationException.class)
  public void testAddFeatures() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.addFeatures(new Features() {
      @Override
      public void changeSpeed(int newSpeed) {
        // empty bc mock
      }

      @Override
      public void restartAnimation() {
        // empty bc mock
      }

      @Override
      public void enableDisableLooping() {
        // empty bc mock
      }

      @Override
      public void resumeAnimation() {
        // empty bc mock
      }

      @Override
      public void pauseAnimation() {
        // empty bc mock
      }

      /**
       * Triggers the animation to toggle it's discrete feature to the opposite state.
       * Discrete enabled means that the animation will only display frames at the start
       * and end of its motions. Discrete disabled means that the animation will display
       * continuously a frame for every tick.
       */
      @Override
      public void enableDisableDiscrete() {
        // empty bc mock
      }

      /**
       * Triggers the animation to toggle whether all the shapes in the animation are
       * filled or just outlined.
       */
      @Override
      public void toggleFill() {
        // empty bc mock
      }
    });
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUpdateTick() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    try {
      av.updateTick();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRestart() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.restart();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testPause() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.pause();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testResume() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.resume();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testToggleLooping() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.toggleLooping();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTick() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.getTick();
  }

  @Test
  public void testGetSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    assertEquals(2, av.getSpeed());

    av = new SVGAnimationView(am, 4, sb);
    assertEquals(4, av.getSpeed());
  }

  @Test
  public void testChangeSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    assertEquals(2, av.getSpeed());
    av.changeSpeed(4);
    assertEquals(4, av.getSpeed());
    av.changeSpeed(1);
    assertEquals(1, av.getSpeed());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidChangeSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    IAnimationView view = new TextualAnimationView(am, 10);
    am.startAnimation();

    view.changeSpeed(0);
  }

  // test constructor
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new TextualAnimationView(null, 2, sb);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new TextualAnimationView(null, 2, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTickRateNegative() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new TextualAnimationView(am, -1, sb);
  }

  // test normal textual render usage, one shape, one motion
  @Test
  public void testRenderTextual() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Reccy", false, 100, 150,
        20, 15, ShapeType.RECTANGLE);
    am.addMotion(5, 10, 100, 150, 150,
        200, 20, 15, 15, 10, i,
        i, "Reccy");
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "canvas 0 0 0 0\n"
        + "shape Reccy rectangle\n"
        + "motion Reccy 2.5 100.00 150.00 15.00 20.00 40 40 40 5.0 150.00 "
        + "200.00 10.00 15.00 40 40 40\n");
  }

  // test textual render, multiple shapes and motions
  @Test
  public void testRenderTextualMultipleShapes() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Reccy", false, 100, 150,
        20, 15, ShapeType.RECTANGLE);
    am.addShape(20, 25, 4, "C", false, 150, 150,
        20, 10, ShapeType.CIRCLE);
    am.addMotion(5, 10, 150, 150, 150,
        200, 20, 15, 10, 10, i,
        i, "C");
    am.addMotion(10, 11, 150, 200, 200,
        300, 15, 16, 10, 10, i,
        i, "C");
    am.addMotion(10, 11, 150, 200, 200,
        300, 15, 16, 10, 10, i,
        i, "Reccy");
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "canvas 0 0 0 0\n"
        + "shape Reccy rectangle\n"
        + "motion Reccy 5.0 150.00 200.00 10.00 15.00 40 40 40 5.5 200.00 300.00 10.00 16.00 40 "
        + "40 40\n" + "shape C ellipse\n"
        + "motion C 2.5 150.00 150.00 10.00 20.00 40 40 40 5.0 150.00 200.00 10.00 15.00 40 40 40\n"
        + "motion C 5.0 150.00 200.00 10.00 15.00 40 40 40 5.5 200.00 300.00 10.00 16.00 40 40 "
        + "40\n");

  }

  // test textual render, no shapes or motions
  @Test
  public void testRenderTextualNoShapesOrMotions() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "canvas 0 0 0 0\n");
  }

  // test textual render, smalldemo.txt
  @Test
  public void testRenderTextualBuildings() throws IOException {
    Readable in = new FileReader("smalldemo.txt");
    AnimationModel am = AnimationReader.parseFile(in, new Builder());
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "canvas 200 70 360 360\n"
        + "shape R rectangle\n"
        + "motion R 0.5 200.00 200.00 100.00 50.00 255 0 0 "
        + "5.0 200.00 200.00 100.00 50.00 255 0 0\n"
        + "motion R 5.0 200.00 200.00 100.00 50.00 255 0 0 "
        + "25.0 300.00 300.00 100.00 50.00 255 0 0\n"
        + "motion R 25.0 300.00 300.00 100.00 50.00 255 0 0 "
        + "25.5 300.00 300.00 100.00 50.00 255 0 0\n"
        + "motion R 25.5 300.00 300.00 100.00 50.00 255 0 0 "
        + "35.0 300.00 300.00 100.00 25.00 255 0 0\n"
        + "motion R 35.0 300.00 300.00 100.00 25.00 255 0 0 "
        + "50.0 200.00 200.00 100.00 25.00 255 0 0\n"
        + "shape C ellipse\n"
        + "motion C 3.0 440.00 70.00 60.00 120.00 0 0 255 10.0 "
        + "440.00 70.00 60.00 120.00 0 0 255\n"
        + "motion C 10.0 440.00 70.00 60.00 120.00 0 0 255 25.0 "
        + "440.00 250.00 60.00 120.00 0 0 255\n"
        + "motion C 25.0 440.00 250.00 60.00 120.00 0 0 255 35.0 "
        + "440.00 370.00 60.00 120.00 0 170 85\n"
        + "motion C 35.0 440.00 370.00 60.00 120.00 0 170 85 40.0 "
        + "440.00 370.00 60.00 120.00 0 255 0\n"
        + "motion C 40.0 440.00 370.00 60.00 120.00 0 255 0 50.0 "
        + "440.00 370.00 60.00 120.00 0 255 0\n");
  }

  @Test
  public void testTextualViewWithChangedCanvas() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Reccy", false, 100, 150,
        20, 15, ShapeType.RECTANGLE);
    am.addMotion(5, 10, 100, 150, 150,
        200, 20, 15, 15, 10, i,
        i, "Reccy");
    am.setCanvas(30, 30, 360, 400);
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "canvas 30 30 360 400\n"
        + "shape Reccy rectangle\n"
        + "motion Reccy 2.5 100.00 150.00 15.00 20.00 40 40 40 5.0 150.00 "
        + "200.00 10.00 15.00 40 40 40\n");
  }


  // test adding shapes with no motions
  @Test
  public void testShapesButNoMotions() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.addShape("Reccy", ShapeType.RECTANGLE);
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals("canvas 0 0 0 0\n"
        + "shape Reccy rectangle\n", sb.toString());
  }


  // test adding shape with a motion, but with no changes in the motion
  @Test
  public void testShapesNoMovement() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.addShape("Reccy", ShapeType.RECTANGLE);
    int[] rgb = {50, 40, 20};
    am.addMotion(10, 15, 15, 15, 9,
        9, 30, 30, 4, 4, rgb,
        rgb, "Reccy");
    am.startAnimation();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals("canvas 0 0 0 0\n"
        + "shape Reccy rectangle\n"
        + "motion Reccy 5.0 15.00 9.00 4.00 30.00 50 40 20 7.5 15.00 9.00 4.00 30.00 50 40 20\n",
        sb.toString());
  }


  // test adding shapes with gaps in motions (allowed in our model)
  @Test
  public void testGaps() throws IOException {
    int[] a = {3, 4, 5};
    Motion motion1 = new Motion(2, 3, 3, 4, 5, 5, 7,
        8,
        9, 10, a, a);
    Motion motion2 = new Motion(4, 5, 4, 3, 5, 6, 8,
        8,
        10, 10, a, a);
    AnimationModel am = new SimpleAnimationModel();
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 3, 3, 4, 5, 5, 7,
        8,
        9, 10, a, a, "R");
    am.addMotion(4, 5, 4, 3, 5, 6, 8,
        8,
        10, 10, a, a, "R");
    am.startAnimation();
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new TextualAnimationView(am, 2, sb);
    av.render();
    assertEquals("canvas 0 0 0 0\n"
        + "shape R rectangle\n"
        + "motion R 1.0 3.00 5.00 9.00 7.00 3 4 5 1.5 4.00 5.00 10.00 8.00 3 4 5\n"
        + "motion R 2.0 4.00 5.00 10.00 8.00 3 4 5 2.5 3.00 6.00 10.00 8.00 3 4 5\n",
        sb.toString());
  }

}