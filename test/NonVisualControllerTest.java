import static org.junit.Assert.assertEquals;

import controller.IController;
import controller.NonVisualController;
import model.AnimationModel;
import model.SimpleAnimationModel;
import model.SimpleAnimationModel.ShapeType;
import org.junit.Test;
import view.IAnimationView;
import view.SVGAnimationView;
import view.TextualAnimationView;

/**
 * Tests the functionality and behavior of the NonVisualController class.
 */
public class NonVisualControllerTest {
  // test constructor errors
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    IController c = new NonVisualController(null,
        new TextualAnimationView(new SimpleAnimationModel(), 3), 3);
  }

  @Test(expected =  IllegalArgumentException.class)
  public void testNullView() {
    IController c = new NonVisualController(new SimpleAnimationModel(),
        null, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalSpeed() {
    IController c = new NonVisualController(new SimpleAnimationModel(),
        new TextualAnimationView(new SimpleAnimationModel(), 3), 0);
  }

  // test go method with textual view
  @Test
  public void testTextualViewGo() {
    AnimationModel am = new SimpleAnimationModel();

    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Reccy", false, 100, 150,
        20, 15, ShapeType.RECTANGLE);
    am.addMotion(5, 10, 100, 150, 150,
        200, 20, 15, 15, 10, i,
        i, "Reccy");
    am.setCanvas(30, 30, 360, 400);

    IAnimationView v = new TextualAnimationView(am, 2, sb);
    IController c = new NonVisualController(am, v, 2);

    c.animationGo();

    assertEquals(sb.toString(), "canvas 30 30 360 400\n"
        + "shape Reccy rectangle\n"
        + "motion Reccy 2.5 100.00 150.00 15.00 20.00 40 40 40 5.0 150.00 "
        + "200.00 10.00 15.00 40 40 40\n");
  }

  // test go method with SVG view
  @Test
  public void testSVGViewGo() {
    AnimationModel am = new SimpleAnimationModel();
    StringBuilder sb = new StringBuilder();

    IAnimationView av = new SVGAnimationView(am, 10, sb);

    IController c = new NonVisualController(am, av, 10);
    c.animationGo();

    assertEquals(sb.toString(), "<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "</svg>");
  }

}
