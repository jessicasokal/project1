import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import model.IMotion;
import model.Motion;
import model.Rectangle;
import model.Shape;
import org.junit.Test;

/**
 * Tests for the expected behaviors of the Motion class.
 */
public class MotionTest {

  // test to string for motion
  @Test
  public void testToString() {
    // test with no change
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 15, 15, 9,
        9, 30, 30, 4, 4, rgb,
        rgb);
    assertEquals("10 15.00 9.00 4.00 30.00 50 40 20 15 15.00 9.00 4.00 30.00 50 40 20",
        m.toString());

    // test with teleportation
    m = new Motion(10, 10, 10, 15, 9,
        20, 32, 30, 4, 8, rgb,
        rgb);
    assertEquals("10 10.00 9.00 4.00 32.00 50 40 20 10 15.00 20.00 8.00 30.00 50 40 20",
        m.toString());

    // test with change

    m = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 4, 8, rgb,
        rgb);
    assertEquals("10 10.00 9.00 4.00 32.00 50 40 20 15 15.00 20.00 8.00 30.00 50 40 20",
        m.toString());
  }

  // test execute motion
  @Test
  public void testExecuteMotion() {
    int[] rgb = {50, 40, 20};
    Shape r = new Rectangle(30, 35, 20, "Rect", false,
        10, 43, 59, 70);
    Motion m = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 4, 8, rgb,
        rgb);
    m.executeMotion(r);
    assertEquals(15, r.getX(), .01);
    r.setX(400);
    m.executeMotion(r);
    assertEquals(15, r.getX(), .01);
    m = new Motion(10, 15, 20, 20, 9,
        9, 30, 30, 4, 4, rgb,
        rgb);
    m.executeMotion(r);
    assertEquals(20, r.getX(), .01);
  }

  // test bitwiseChangeList
  @Test
  public void testBitwiseChangeList() {
    boolean[] noChange = {false, false, false, false, false};
    boolean[] x = {true, false, false, false, false};
    boolean[] multipleChange = {true, true, false, false, true};
    int[] rgb = {50, 40, 20};
    int[] rgb2 = {60, 40, 20};
    Motion m = new Motion(10, 15, 15, 15, 9,
        9, 30, 30, 4, 4, rgb,
        rgb);
    assertArrayEquals(noChange, m.bitwiseChangeList());
    m = new Motion(10, 15, 15, 20, 9,
        9, 30, 30, 4, 4, rgb,
        rgb);
    assertArrayEquals(x, m.bitwiseChangeList());
    m = new Motion(10, 15, 15, 20, 9,
        -4, 30, 30, 4, 4, rgb,
        rgb2);
    assertArrayEquals(multipleChange, m.bitwiseChangeList());
  }

  // test compareToTick
  @Test
  public void testCompareToTick() {
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 15, 20, 9,
        9, 30, 30, 4, 4, rgb,
        rgb);
    assertEquals(1, m.compareToTick(9));
    assertEquals(0, m.compareToTick(10));
    assertEquals(0, m.compareToTick(11));
    assertEquals(-1, m.compareToTick(15));
    assertEquals(-1, m.compareToTick(16));
  }

  // test splice
  @Test
  public void testSplice() {
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 15, 20, 9,
        9, 30, 30, 4, 4, rgb, rgb);
    IMotion m2 = m.splice(10, 11);
    assertEquals(m2.getStartTick(), 10);
    assertEquals(m2.getEndTick(), 11);
    assertEquals(m2.getEndX(), 16, 0.01);
    assertEquals(m2.getStartX(), 15, 0.01);
    assertEquals(m2.getStartY(), 9, 0.01);
    assertEquals(m2.getEndY(), 9, 0.01);
    m2 = m.splice(15, 16);
    assertNull(m2);
    m2 = m.splice(9, 10);
    assertNull(m2);
  }

  // tests for getter methods of motion
  @Test
  public void testGetEndRGB() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 4, 8, rgb, rgb);

    assertEquals(m.getEndRGB(), rgb);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 4, 8, rgb2, rgb3);

    assertEquals(m2.getEndRGB(), rgb3);
  }

  @Test
  public void testGetStartRGB() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 4, 8, rgb,
        rgb);

    assertEquals(m.getStartRGB(), rgb);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 4, 8, rgb2, rgb3);

    assertEquals(m2.getStartRGB(), rgb2);
  }

  @Test
  public void testGetEndH() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 8, 8, rgb,
        rgb);

    assertEquals(m.getEndH(), 8, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 8, 4, rgb2, rgb3);

    assertEquals(m2.getEndH(), 4, .01);
  }

  @Test
  public void testGetStartH() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 8, 8, rgb,
        rgb);

    assertEquals(m.getStartH(), 8, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 8, 4, rgb2, rgb3);

    assertEquals(m2.getStartH(), 8, .01);
  }

  @Test
  public void testGetEndW() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 9,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getEndW(), 32, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 9,
        20, 32, 30, 8, 4, rgb2, rgb3);

    assertEquals(m2.getEndW(), 30, .01);
  }

  @Test
  public void testGetStartW() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 9,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getStartW(), 32, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.getStartW(), 30, .01);
  }

  @Test
  public void testGetEndY() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getEndY(), 20, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 20,
        9, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.getEndY(), 9, .01);
  }

  @Test
  public void testGetStartY() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 10, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getStartY(), 20, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 10, 15, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.getStartY(), 9, .01);
  }

  @Test
  public void testGetEndX() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 15, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getEndX(), 15, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 15, 10, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.getEndX(), 10, .01);
  }

  @Test
  public void testGetStartX() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 15, 15, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getStartX(), 15, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(10, 15, 6, 10, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.getStartX(), 6, .01);
  }

  @Test
  public void testGetEndTick() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 10, 15, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getEndTick(), 10, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(2, 3, 6, 10, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.getEndTick(), 3, .01);
  }

  @Test
  public void testGetStartTick() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 10, 15, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.getStartTick(), 10, .01);

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(2, 3, 6, 10, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.getStartTick(), 2, .01);
  }

  // tests for toStringStartValues
  @Test
  public void testToStringStartValues() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 10, 15, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.toStringStartValues(), "15.00 20.00 8.00 32.00 50 40 20");

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(2, 3, 6, 10, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.toStringStartValues(), "6.00 9.00 8.00 30.00 0 0 0");
  }

  // tests for toStringStartValues
  @Test
  public void testToStringEndValues() {
    // testing where end rgb is same as before
    int[] rgb = {50, 40, 20};
    Motion m = new Motion(10, 10, 15, 15, 20,
        20, 32, 32, 8, 8, rgb,
        rgb);

    assertEquals(m.toStringEndValues(), "15.00 20.00 8.00 32.00 50 40 20");

    // testing where rgb does change
    int[] rgb2 = {0, 0, 0};
    int[] rgb3 = {4, 0, 0};
    Motion m2 = new Motion(2, 3, 6, 10, 9,
        20, 30, 32, 8, 4, rgb2, rgb3);

    assertEquals(m2.toStringEndValues(), "10.00 20.00 4.00 32.00 4 0 0");
  }

}