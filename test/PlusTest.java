import static org.junit.Assert.assertEquals;

import cs3500.animator.util.AnimationReader;
import java.awt.Polygon;
import java.io.FileReader;
import java.io.IOException;
import model.AnimationModel;
import model.Plus;
import model.Shape;
import model.SimpleAnimationModel;
import model.SimpleAnimationModel.Builder;
import model.SimpleAnimationModel.ShapeType;
import org.junit.Test;
import view.IAnimationView;
import view.SVGAnimationView;

/**
 * Tests for new plus sign shape, including methods of class,
 * and utilizing it in an SVG file.
 */
public class PlusTest {


  @Test
  public void testMakeXs() {
    Shape p = new Plus(255, 255, 255, "plus 1", false, 30,
        40, 100, 100);

    boolean allSame = true;
    int[] ii = p.makeXs((int)p.getX(), (int)p.getWidth());
    int[] actualXs = new int[]{55, 105, 105, 130, 130, 105, 105, 55, 55, 30, 30, 55};
    for (int jj = 0; jj < 12; jj++) {
      if (actualXs[jj] != ii[jj]) {
        allSame = false;
      }
    }
    assertEquals(allSame, true);


    Shape p2 = new Plus(255, 255, 255, "plus 1", false, 0,
        0, 30, 9);

    boolean allSame2 = true;
    int[] ii2 = p.makeXs((int)p2.getX(), (int)p2.getWidth());

    int[] actualXs2 = new int[]{7, 21, 21, 30, 30, 21, 21, 7, 7, 0, 0, 7};
    for (int jj2 = 0; jj2 < 12; jj2++) {
      if (actualXs2[jj2] != ii2[jj2]) {
        allSame2 = false;
      }
    }
    assertEquals(allSame2, true);
  }

  @Test
  public void testMakeYs() {
    Shape p = new Plus(255, 255, 255, "plus 1", false, 30,
        40, 100, 100);

    boolean allSame = true;
    int[] ii = p.makeYs((int)p.getY(), (int)p.getHeight());
    int[] actualYs = new int[]{40, 40, 65, 65, 115, 115, 140, 140, 115, 115, 65, 65};
    for (int jj = 0; jj < 12; jj++) {
      if (actualYs[jj] != ii[jj]) {
        allSame = false;
      }
    }
    assertEquals(allSame, true);


    Shape p2 = new Plus(255, 255, 255, "plus 1", false, 0,
        0, 30, 9);

    boolean allSame2 = true;
    int[] ii2 = p.makeYs((int)p2.getX(), (int)p2.getWidth());
    int[] actualXs2 = new int[]{0, 0, 7, 7, 21, 21, 30, 30, 21, 21, 7, 7};
    for (int jj2 = 0; jj2 < 12; jj2++) {
      if (actualXs2[jj2] != ii2[jj2]) {
        allSame2 = false;
      }
    }
    assertEquals(allSame2, true);
  }

  @Test
  public void testVisualizeShape() {
    Shape p = new Plus(255, 255, 255, "plus 1", false, 30,
        40, 100, 100);

    assertEquals(p.visualizeShape(), new Polygon(p.makeXs((int)p.getX(), (int)p.getWidth()),
        p.makeYs((int)p.getY(), (int)p.getHeight()), 12));
  }

  @Test
  public void testGetType() {
    Shape p = new Plus(255, 255, 255, "plus 1", false, 30,
        40, 100, 100);
    assertEquals(p.getType(), "plus");

    Shape p2 = new Plus(170, 0, 233, "plus 2", true, 10,
        20, 300, 500);
    assertEquals(p2.getType(), "plus");
  }


  // testing SVG output with no shape or motion
  @Test
  public void testSVGAnimationViewNoShape() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Plus", false, 100, 150,
        20, 15, ShapeType.PLUS);
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "\n"
        + "</svg>");
  }

  // testing SVG output with no change in motion
  @Test
  public void testSVGAnimationViewNoMotion() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Plus", false, 100, 150,
        20, 15, ShapeType.PLUS);
    am.addMotion(1, 1, 100, 100, 150,
        150, 20, 20, 15, 15, i,
        i, "Plus");
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<polygon id=\"Plus\" points=\"105,150 115,150 115,153 120,153 120,159 115,159 "
        + "115,165 105,165 105,159 100,159 100,153 105,153\" fill=\"rgb(40,40,40)\" "
        + "visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"500.0ms\" "
        + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" />"
        + "</polygon>\n</svg>");
  }

  // testing SVG output with change in width, height, x and y
  @Test
  public void testSVGAnimationView() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Plus", false, 100, 150,
        20, 15, ShapeType.PLUS);
    am.addMotion(5, 10, 100, 150, 150,
        200, 20, 15, 15, 10, i,
        i, "Plus");
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "<svg viewBox = \"0 0 0 0\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<polygon id=\"Plus\" "
        + "points=\"105,150 115,150 115,153 120,153 120,159 115,159 115,165 105,165 105,159 "
        + "100,159 100,153 105,153\" fill=\"rgb(40,40,40)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" />"
        + "<animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"points\" from=\"105,150 115,150 115,153 120,153 120,159 115,159 "
        + "115,165 105,165 105,159 100,159 100,153 105,153\" to=\"153,200 159,200 159,202 165,202 "
        + "165,206 159,206 159,210 153,210 153,206 150,206 150,202 153,202\" fill=\"freeze\" />\n"
        + "</polygon>\n"
        + "</svg>");
  }

  // testing SVG output with multiple kinds of shapes, and multiple motions for plus
  @Test
  public void testSVGAnimationViewMultipleShapes() throws IOException {
    AnimationModel am = new SimpleAnimationModel();
    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Plus", false, 100, 150,
        20, 15, ShapeType.PLUS);
    am.addShape(40, 255, 60, "Rect", false, 200, 250,
        15, 10, ShapeType.RECTANGLE);
    am.addMotion(5, 10, 100, 150, 150,
        200, 20, 15, 15, 10, i,
        i, "Plus");
    // change in color
    int[] i2 = {255, 240, 40};
    am.addMotion(15, 20, 150, 300, 200,
        300, 15, 20, 10, 40, i,
        i2, "Plus");
    am.addMotion(1, 20, 200, 300, 250,
        400, 20, 12, 15, 50, i,
        i, "Rect");
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals(sb.toString(), "<svg viewBox = \"0 0 0 0\" "
        + "version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<polygon id=\"Plus\" points=\"105,150 115,150 115,153 120,153"
        + " 120,159 115,159 115,165 105,165 105,159 100,159 100,153 105,153\" "
        + "fill=\"rgb(40,40,40)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\""
        + "freeze\" /><animate attributeType=\"xml\" begin=\"2500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"points\" from=\"105,150 115,150 115,153 120,153 120,159 115,159 "
        + "115,165 105,165 105,159 100,159 100,153 105,153\" to=\"153,200 159,200 159,202 "
        + "165,202 165,206 159,206 159,210 153,210 153,206 150,206 150,202 153,202\" "
        + "fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"7500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"points\" from=\"153,200 159,200 159,202 165,202 165,206 "
        + "159,206 159,210 153,210 153,206 150,206 150,202 153,202\" to=\"305,300 315,300 "
        + "315,310 320,310 320,330 315,330 315,340 305,340 305,330 300,330 300,310 305,310\" "
        + "fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"7500.0ms\" dur=\"2500.0ms\" "
        + "attributeName=\"fill\" from=\"rgb(40, 40, 40)\" to=\"rgb(255, 240, 40)\" "
        + "fill=\"freeze\" />\n"
        + "</polygon>\n"
        + "<rect id=\"Rect\" x=\"200.00\" y=\"250.00\" width=\"20.00\" height=\"15.00\" "
        + "fill=\"rgb(40,40,40)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"500.0ms\" "
        + "attributeName=\"visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" />"
        + "<animate attributeType=\"xml\" begin=\"500.0ms\" dur=\"9500.0ms\" attributeName=\"x\" "
        + "from=\"200\" to=\"300\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"500.0ms\" dur=\"9500.0ms\" attributeName=\"y\" "
        + "from=\"250\" to=\"400\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"500.0ms\" dur=\"9500.0ms\" "
        + "attributeName=\"width\" "
        + "from=\"20\" to=\"12\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"500.0ms\" dur=\"9500.0ms\""
        + " attributeName=\"height\" from=\"15\" to=\"50\" fill=\"freeze\" />\n"
        + "</rect>\n"
        + "</svg>");
  }

  // test inputting from a file with plus sign shape
  @Test
  public void testFromFile() throws IOException {
    Readable in = new FileReader("smalldemowithplus.txt");
    AnimationModel am = AnimationReader.parseFile(in, new Builder());
    StringBuilder sb = new StringBuilder();
    am.startAnimation();
    IAnimationView av = new SVGAnimationView(am, 2, sb);
    av.render();
    assertEquals("<svg viewBox = \"200 70 360 360\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<polygon id=\"R\" points=\"212,200 236,200 236,225 250,225 250,275 "
        + "236,275 236,300 212,300 212,275 200,275 200,225 212,225\" fill=\"rgb(255,0,0)\" "
        + "visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"500.0ms\" attributeName=\""
        + "visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /><animate "
        + "attributeType=\"xml\" begin=\"5000.0ms\" dur=\"20000.0ms\" attributeName=\"points\" "
        + "from=\"212,200 236,200 236,225 250,225 250,275 236,275 236,300 212,300 212,275 200,275 "
        + "200,225 212,225\" to=\"312,300 336,300 336,325 350,325 350,375 336,375 336,400 312,400"
        + " 312,375 300,375 300,325 312,325\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"25500.0ms\" dur=\"9500.0ms\" attributeName=\""
        + "points\" from=\"312,300 336,300 336,325 350,325 350,375 336,375 336,400 312,400 312,375 "
        + "300,375 300,325 312,325\" to=\"306,300 318,300 318,325 325,325 325,375 318,375 318,400 "
        + "306,400 306,375 300,375 300,325 306,325\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"35000.0ms\" dur=\"15000.0ms\" attributeName=\""
        + "points\" from=\"306,300 318,300 318,325 325,325 325,375 318,375 318,400 306,400 306,375 "
        + "300,375 300,325 306,325\" to=\"206,200 218,200 218,225 225,225 225,275 218,275 218,300 "
        + "206,300 206,275 200,275 200,225 206,225\" fill=\"freeze\" />\n"
        + "</polygon>\n"
        + "<ellipse id=\"C\" cx=\"500.00\" cy=\"100.00\" rx=\"60.00\" ry=\"30.00\" fill=\""
        + "rgb(0,0,255)\" visibility=\"hidden\" >\n"
        + "<animate attributeType=\"xml\" begin=\"0.0ms\" dur=\"3000.0ms\" attributeName=\""
        + "visibility\" from=\"hidden\" to=\"visible\" fill=\"freeze\" /><animate attributeType=\""
        + "xml\" begin=\"10000.0ms\" dur=\"15000.0ms\" attributeName=\"cy\" from=\"100\" to=\"280\""
        + " fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"25000.0ms\" dur=\"10000.0ms\" "
        + "attributeName=\"cy\""
        + " from=\"280\" to=\"400\" fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"25000.0ms\" dur=\"10000.0ms\" "
        + "attributeName=\"fill\" from=\"rgb(0, 0, 255)\" to=\"rgb(0, 170, 85)\" "
        + "fill=\"freeze\" />\n"
        + "<animate attributeType=\"xml\" begin=\"35000.0ms\" dur=\"5000.0ms\" "
        + "attributeName=\"fill\" from=\"rgb(0, 170, 85)\" to=\"rgb(0, 255, 0)\""
        + " fill=\"freeze\" />\n"
        + "</ellipse>\n"
        + "</svg>", sb.toString());
  }

  // Tested toggle feature in VisualContollerTest.java to keep it together with other features
  // Tested toggle button in InteractiveViewTest.java to keep together with other buttons

}