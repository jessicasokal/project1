import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import model.Ellipse;
import model.Rectangle;
import model.Shape;
import org.junit.Test;

/**
 * Tests for the expected behavior of the Shape class.
 */
public class ShapeTest {

  @Test
  public void testSetColor() {
    // testing with constructor where color is initialized
    Shape s = new Ellipse(30, 35, 20, "C", false, 400, 600, 59,
        70);

    s.setColor(0, 0, 0);

    assertEquals(s.getColor(), new Color(0,0 ,0));

    // testing with constructor where color is not initialized
    Shape s2 = new Ellipse("C");

    s2.setColor(40, 45, 0);

    assertEquals(s2.getColor(), new Color(40,45 ,0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetColorExceptionNegative() {
    // testing with constructor where color is initialized
    Shape s = new Ellipse(6, 35, 20, "C", false, 400, 600, 59,
        70);

    s.setColor(-7, 0, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetColorExceptionTooBig() {
    // testing with constructor where color is initialized
    Shape s = new Ellipse(255, 35, 20, "C", false, 400, 600, 59,
        70);

    s.setColor(256, 0, 0);
  }

  @Test
  public void testSetSize() {
    // testing with constructor where color is initialized
    Shape s = new Ellipse(30, 35, 20, "C", false, 400, 600, 59,
        70);

    s.setSize(0, 0);

    assertEquals(s.getHeight(), 0, .01);
    assertEquals(s.getWidth(), 0, .01);

    // testing with constructor where color is not initialized
    Shape s2 = new Ellipse("C");

    s2.setSize(100, 40);

    assertEquals(s2.getHeight(), 40, .01);
    assertEquals(s2.getWidth(), 100, .01);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSizeExceptionNegative() {
    // testing with constructor where color is initialized
    Shape s = new Rectangle(255, 35, 20, "C", false, 400, 600,
        59, 70);

    s.setSize(256, -9);
  }

  @Test
  public void testSetX() {
    // testing with constructor where color is initialized
    Shape s = new Rectangle(30, 35, 20, "C", false, 400, 600, 59,
        70);

    s.setX(90);

    assertEquals(s.getX(), 90, .01);

    // testing with constructor where color is not initialized
    Shape s2 = new Ellipse("C");

    s2.setX(1);

    assertEquals(s2.getX(), 1, .01);
  }

  @Test
  public void testSetY() {
    // testing with constructor where color is initialized
    Shape s = new Rectangle(30, 35, 20, "C", false, 400, 600, 59,
        70);

    s.setY(90);

    assertEquals(s.getY(), 90, .01);

    // testing with constructor where color is not initialized
    Shape s2 = new Ellipse("C");

    s2.setY(1);

    assertEquals(s2.getY(), 1, .01);
  }

  @Test
  public void testVisualizeShape() {
    Shape s = new Rectangle(30, 35, 20, "C", false, 400, 600, 59,
        70);
    assertEquals(s.visualizeShape(), new Rectangle2D.Double(400, 600, 59, 70));

    Shape s2 = new Ellipse(30, 35, 20, "C", false, 400, 600, 59,
        70);
    assertEquals(s2.visualizeShape(), new Ellipse2D.Double(400, 600, 59, 70));
  }

  @Test
  public void testGetColor() {
    Shape s = new Rectangle(30, 35, 20, "C", false, 400, 600, 59,
        70);
    assertEquals(s.getColor(), new Color(30, 35, 20));

    Shape s2 = new Ellipse(255, 255, 255, "C", false,
        400, 600, 59,
        70);
    assertEquals(s2.getColor(), new Color(255, 255, 255));
  }

  @Test
  public void testGetName() {
    Shape s = new Rectangle(30, 35, 20, "Rect", false,
        400, 600, 59, 70);
    assertEquals(s.getName(), "Rect");

    Shape s2 = new Ellipse(255, 255, 255, "C", false, 400,
        600, 59,
        70);
    assertEquals(s2.getName(), "C");
  }

  @Test
  public void testGetX() {
    Shape s = new Rectangle(30, 35, 20, "Rect", false,
        400, 600, 59, 70);
    assertEquals(s.getX(), 400, .01);

    Shape s2 = new Ellipse(255, 255, 255, "C", false, 160,
        600, 59,
        70);
    assertEquals(s2.getX(), 160, .01);
  }

  @Test
  public void testGetY() {
    Shape s = new Rectangle(30, 35, 20, "Rect", false,
        400, 43, 59, 70);
    assertEquals(s.getY(), 43, .01);

    Shape s2 = new Ellipse(255, 255, 255, "C", false, 160,
        600, 59,
        70);
    assertEquals(s2.getY(), 600, .01);
  }

  @Test
  public void testGetWidth() {
    Shape s = new Rectangle(30, 35, 20, "Rect", false,
        400, 43, 59, 70);
    assertEquals(s.getWidth(), 59, .01);

    Shape s2 = new Ellipse(255, 255, 255, "C", false, 160,
        600, 10, 70);
    assertEquals(s2.getWidth(), 10, .01);
  }

  @Test
  public void testGetHeight() {
    Shape s = new Rectangle(30, 35, 20, "Rect", false,
        400, 43, 59, 10);
    assertEquals(s.getHeight(), 10, .01);

    Shape s2 = new Ellipse(255, 255, 255, "C", false, 160,
        600, 59, 70);
    assertEquals(s2.getHeight(), 70, .01);
  }

}