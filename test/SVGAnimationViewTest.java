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

/**
 * Tests for the SVGAnimationView.
 */
public class SVGAnimationViewTest {

  // test for unsupported ops
  @Test(expected = UnsupportedOperationException.class)
  public void testAddFeatures() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
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
    IAnimationView av = new SVGAnimationView(am, 2, sb);
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
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.restart();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testPause() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.pause();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testResume() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.resume();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testToggleLooping() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.toggleLooping();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTick() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.getTick();
  }

  @Test
  public void testGetSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    assertEquals(2, av.getSpeed());

    av = new SVGAnimationView(am, 4, sb);
    assertEquals(4, av.getSpeed());
  }

  @Test
  public void testChangeSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    assertEquals(2, av.getSpeed());
    av.changeSpeed(4);
    assertEquals(4, av.getSpeed());
    av.changeSpeed(1);
    assertEquals(1, av.getSpeed());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidChangeSpeed() {
    AnimationModel am = new SimpleAnimationModel();
    IAnimationView view = new SVGAnimationView(am, 10);
    am.startAnimation();

    view.changeSpeed(0);
  }

  // test constructor
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new SVGAnimationView(null, 2, sb);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new SVGAnimationView(null, 2, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTickRateNegative() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    IAnimationView av = new SVGAnimationView(am, -1, sb);
  }


  @Test
  public void testSVGAnimationView() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Reccy", false, 100, 150,
        20, 15, ShapeType.RECTANGLE);
    am.addMotion(5, 10, 100, 150, 150,
        200, 20, 15, 15, 10, i,
        i, "Reccy");
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<rect id=\"Reccy\" x=\"100.00\" y=\"150.00\" width=\"20.00\" height=\"15.00\" "
        + "fill=\"rgb(40,40,40)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"2500.0ms\" attributeName=\""
        + "visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /><animate attributeType="
        + "\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" attributeName=\"x\" from=\"100\" to=\"150"
        + "\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" attributeName=\"y\" "
        + "from=\"150\" to=\"200\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" attributeName=\"width"
        + "\" from=\"20\" to=\"15\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" attributeName=\""
        + "height\" from=\"15\" to=\"10\" fill=\"freeze\" />\n"
        + "</rect>\n"
        + "</svg>");
  }

  @Test
  public void testSVGAnimationViewMultipleShapes() throws IOException {
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
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<rect id=\"Reccy\" x=\"150.00\" y=\"200.00\" width=\"15.00\" height=\"10.00\" "
        + "fill=\"rgb(40,40,40)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"5000.0ms\" attributeName=\""
        + "visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /><animate attributeType"
        + "=\"xml\" begin=\"5000.0ms\" dur=\"500.0ms\" attributeName=\"x\" from=\"150\" to=\"200\" "
        + "fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"5000.0ms\" dur=\"500.0ms\" attributeName=\"y\" "
        + "from=\"200\" to=\"300\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"5000.0ms\" dur=\"500.0ms\" attributeName=\""
        + "width\" from=\"15\" to=\"16\" fill=\"freeze\" />\n"
        + "</rect>\n"
        + "<ellipse id=\"C\" cx=\"160.00\" cy=\"155.00\" rx=\"10.00\" ry=\"5.00\" fill=\"rgb"
        + "(40,40,40)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"2500.0ms\" attributeName=\""
        + "visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /><animate attributeType"
        + "=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" attributeName=\"cy\" from=\"155\" to=\""
        + "205\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" attributeName=\"rx\" "
        + "from=\"10\" to=\"7\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"5000.0ms\" dur=\"500.0ms\" attributeName=\"cx\" "
        + "from=\"157\" to=\"208\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"5000.0ms\" dur=\"500.0ms\" attributeName=\"cy\" "
        + "from=\"205\" to=\"305\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"5000.0ms\" dur=\"500.0ms\" attributeName=\"rx\" "
        + "from=\"7\" to=\"8\" fill=\"freeze\" />\n"
        + "</ellipse>\n"
        + "</svg>", sb.toString());
  }

  // a view with no shapes or motions
  @Test
  public void testNoShapesOrMotions() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "</svg>", sb.toString());
  }

  // test adding shapes with no motions
  @Test
  public void testShapesButNoMotions() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();
    am.addShape("Reccy", ShapeType.RECTANGLE);
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "\n"
        + "</svg>", sb.toString());
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
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<rect id=\"Reccy\" x=\"15.00\" y=\"9.00\" width=\"30.00\" "
        + "height=\"4.00\" fill=\"rgb(50,40,20)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"5000.0ms\" "
        + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /></rect>\n"
        + "</svg>", sb.toString());
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
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<rect id=\"R\" x=\"3.00\" y=\"5.00\" width=\"7.00\" height=\"9.00\" "
        + "fill=\"rgb(3,4,5)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"1000.0ms\" "
        + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" />"
        + "<animate attributeType=\"xml\" begin=\"1000.0ms\" dur=\"500.0ms\" "
        + "attributeName=\"x\" from=\"3\" to=\"4\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"1000.0ms\" dur=\"500.0ms\" "
        + "attributeName=\"width\" from=\"7\" to=\"8\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"1000.0ms\" dur=\"500.0ms\" "
        + "attributeName=\"height\" from=\"9\" to=\"10\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2000.0ms\" dur=\"500.0ms\" "
        + "attributeName=\"x\" from=\"4\" to=\"3\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2000.0ms\" dur=\"500.0ms\" "
        + "attributeName=\"y\" from=\"5\" to=\"6\" fill=\"freeze\" />\n"
        + "</rect>\n"
        + "</svg>", sb.toString());
  }


  // change the canvas size
  @Test
  public void testChangeCanvas() throws IOException {
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
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"30 30 360 400\" "
        + "version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<rect id=\"Reccy\" x=\"100.00\" y=\"150.00\" width=\"20.00\" "
        + "height=\"15.00\" fill=\"rgb(40,40,40)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" />"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"x\" from=\"100\" to=\"150\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"y\" from=\"150\" to=\"200\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"width\" from=\"20\" to=\"15\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"height\" from=\"15\" to=\"10\" fill=\"freeze\" />\n"
        + "</rect>\n"
        + "</svg>", sb.toString());
  }

  // test inputting from a file
  @Test
  public void testFromFile() throws IOException {
    Readable in = new FileReader("smalldemo.txt");
    AnimationModel am = AnimationReader.parseFile(in, new Builder());
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"200 70 360 360\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<rect id=\"R\" x=\"200.00\" y=\"200.00\" width=\"50.00\" height=\"100.00\" fill=\""
        + "rgb(255,0,0)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"500.0ms\" attributeName=\""
        + "visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /><animate "
        + "attributeType=\"xml\" begin=\"5000.0ms\" dur=\"20000.0ms\" attributeName=\"x\" "
        + "from=\"200\" to=\"300\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"5000.0ms\" dur=\"20000.0ms\" "
        + "attributeName=\"y\" from=\"200\" to=\"300\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"25500.0ms\" dur=\"9500.0ms\" "
        + "attributeName=\"width\" from=\"50\" to=\"25\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"35000.0ms\" dur=\"15000.0ms\" "
        + "attributeName=\"x\" from=\"300\" to=\"200\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"35000.0ms\" dur=\"15000.0ms\" "
        + "attributeName=\"y\" from=\"300\" to=\"200\" fill=\"freeze\" />\n"
        + "</rect>\n"
        + "<ellipse id=\"C\" cx=\"500.00\" cy=\"100.00\" rx=\"60.00\" ry=\"30.00\" "
        + "fill=\"rgb(0,0,255)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"3000.0ms\" attributeName=\""
        + "visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /><animate "
        + "attributeType=\"xml\" begin=\"10000.0ms\" dur=\"15000.0ms\" attributeName=\"cy\" "
        + "from=\"100\" to=\"280\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"25000.0ms\" dur=\"10000.0ms\" "
        + "attributeName=\"cy\" from=\"280\" to=\"400\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"25000.0ms\" dur=\"10000.0ms\" "
        + "attributeName=\"fill\" from=\"rgb(0, 0, 255)\" to=\"rgb(0, 170, 85)\" "
        + "fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"35000.0ms\" dur=\"5000.0ms\" "
        + "attributeName=\"fill\" from=\"rgb(0, 170, 85)\" to=\"rgb(0, 255, 0)\" "
        + "fill=\"freeze\" />\n"
        + "</ellipse>\n"
        + "</svg>", sb.toString());
  }

}