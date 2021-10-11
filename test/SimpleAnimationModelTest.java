import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import model.AnimationModel;
import model.Ellipse;
import model.Motion;
import model.Rectangle;
import model.Shape;
import model.SimpleAnimationModel;
import model.SimpleAnimationModel.ShapeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the expected behavior and functionality of the Simple Animation Model class.
 */
public class SimpleAnimationModelTest {

  // testing findNextTick

  // test invalid tick
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidFindNextTick() {
    AnimationModel am = new SimpleAnimationModel();
    am.findNextTick(-1);
  }

  // test findNextTick functionality
  @Test
  public void testFindNextTick() {
    AnimationModel am = new SimpleAnimationModel();
    assertEquals(1, am.findNextTick(1));

    int[] i = {40, 40, 40};
    StringBuilder sb = new StringBuilder();
    am.addShape(40, 40, 45, "Reccy", false, 100, 150,
        20, 15, ShapeType.RECTANGLE);
    am.addShape(20, 25, 4, "C", false, 150, 150,
        20, 10, ShapeType.CIRCLE);
    am.addMotion(5, 10, 150, 150, 150,
        200, 20, 15, 10, 10, i,
        i, "C");

    assertEquals(5, am.findNextTick(1));
    assertEquals(5, am.findNextTick(5));
    assertEquals(10, am.findNextTick(6));

    am.addMotion(10, 12, 150, 200, 200,
        300, 15, 16, 10, 10, i,
        i, "C");
    am.addMotion(7, 11, 150, 200, 200,
        300, 15, 16, 10, 10, i,
        i, "Reccy");
    assertEquals(7, am.findNextTick(6));
    assertEquals(7, am.findNextTick(7));
    assertEquals(10, am.findNextTick(10));
    assertEquals(12, am.findNextTick(12));
    assertEquals(14, am.findNextTick(14));
  }

  // testing isAnimationOver with no motions
  @Test
  public void testIsAnimationOverNoMotions() {
    AnimationModel am = new SimpleAnimationModel();
    assertTrue(am.isAnimationOver(1));
  }

  // testing isAnimationOver with motions
  @Test
  public void testIsAnimationOverMotions() {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};

    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 4, 3, 3, 5, 4, 2, 2, 5,
        5, a, a, "R");
    assertTrue(am.isAnimationOver(4));
    assertTrue(am.isAnimationOver(5));
    assertFalse(am.isAnimationOver(1));
    assertFalse(am.isAnimationOver(3));
  }

  // testing isAnimationOver with invalid tick
  @Test(expected = IllegalArgumentException.class)
  public void testIsAnimationOverBadInput() {
    AnimationModel am = new SimpleAnimationModel();
    boolean b = am.isAnimationOver(-1);
  }

  // testing getShapes if the list of shapes is empty
  @Test
  public void testGetShapesEmpty() {
    AnimationModel am = new SimpleAnimationModel();
    assertEquals(am.getShapes(), new HashMap<String, Shape>());
  }

  // testing getShapes if the list of shapes is not empty
  @Test
  public void testGetShapesNonEmpty() {
    AnimationModel am = new SimpleAnimationModel();

    // checking that before any shapes are added, "R" does not exist, "C" does not exist,
    // and "F" does not, and the total length is 0.
    assertEquals(am.getShapes().size(), 0);
    assertEquals(am.getShapes().get("R"), null);
    assertEquals(am.getShapes().get("C"), null);
    assertEquals(am.getShapes().get("F"), null);

    // adding two shapes to the list
    am.addShape("R", ShapeType.RECTANGLE);
    am.addShape("C", ShapeType.CIRCLE);

    // checking that after shapes are added, "R" exists, "C" exists,
    // and "F" does not, and the total length is 2.
    assertEquals(am.getShapes().size(), 2);

    // check that specifically the two names added are the ones in the list
    ArrayList<String> keys = new ArrayList<String>(Arrays.asList("R", "C"));
    boolean allInGetShapes = true;
    for (int ii = 0; ii < am.getShapes().size(); ii++) {
      if (am.getShapes().get(keys.get(ii)) == null) {
        allInGetShapes = false;
      }
    }
    assertEquals(allInGetShapes, true);

    assertNotEquals(am.getShapes().get("R"), null);
    assertNotEquals(am.getShapes().get("C"), null);
    assertEquals(am.getShapes().get("F"), null);
  }

  // Testing removeShape exception, removing from empty list
  @Test (expected = IllegalArgumentException.class)
  public void removeShapeTestExceptionEmpty() {
    AnimationModel am = new SimpleAnimationModel();

    // testing removing shape before any shape is in list
    am.removeShape("F");
  }

  // Testing removeShape exception, removing item that is not in list
  @Test (expected = IllegalArgumentException.class)
  public void removeShapeTestExceptionNonEmpty() {
    AnimationModel am = new SimpleAnimationModel();

    // making sure there are no items in the shape list before hand
    assertEquals(am.getShapes().size(), 0);
    assertEquals(am.getShapes().get("R"), null);
    assertEquals(am.getShapes().get("C"), null);
    assertEquals(am.getShapes().get("F"), null);

    // adding two shapes to the list
    am.addShape("R", ShapeType.RECTANGLE);
    am.addShape("C", ShapeType.CIRCLE);

    // checking that R and C exist in the list of shapes, but F doesn't
    assertEquals(am.getShapes().size(), 2);

    // check that specifically the two names added are the ones in the list
    ArrayList<String> keys = new ArrayList<String>(Arrays.asList("R", "C"));
    boolean allInGetShapes = true;
    for (int ii = 0; ii < am.getShapes().size(); ii++) {
      if (am.getShapes().get(keys.get(ii)) == null) {
        allInGetShapes = false;
      }
    }
    assertEquals(allInGetShapes, true);

    assertNotEquals(am.getShapes().get("R"), null);
    assertNotEquals(am.getShapes().get("C"), null);
    assertEquals(am.getShapes().get("F"), null);

    // testing removing shape with non empty list, but given shape not in it
    am.removeShape("F");
  }

  // Testing removeShape
  @Test
  public void removeShapeTestFromShapes() {
    AnimationModel am = new SimpleAnimationModel();

    // making sure there are no items in the shape list before hand
    assertEquals(am.getShapes().size(), 0); // checking that shapes list is empty
    assertEquals(am.getShapes().get("R"), null); // checking that motions list for R does
    // not exist
    assertEquals(am.getShapes().get("C"), null); // checking that motions list for C does not
    // exist

    // adding two shapes to the list
    am.addShape("R", ShapeType.RECTANGLE);
    am.addShape("C", ShapeType.CIRCLE);

    // checking that R and C exist in the list of shape
    assertEquals(am.getShapes().size(), 2); // checking that added to shape list

    // check that specifically the two names added are the ones in the list
    ArrayList<String> keys = new ArrayList<String>(Arrays.asList("R", "C"));
    boolean allInGetShapes = true;
    for (int ii = 0; ii < am.getShapes().size(); ii++) {
      if (am.getShapes().get(keys.get(ii)) == null) {
        allInGetShapes = false;
      }
    }
    assertEquals(allInGetShapes, true);

    assertNotEquals(am.getShapes().get("R"), null); // checking that added to motions list
    assertNotEquals(am.getShapes().get("C"), null); // checking that added to motions list


    // testing removing shape before any shape is in list
    am.removeShape("C");
    am.removeShape("R");

    // checking that R and C have been removed from the list of shapes
    assertEquals(am.getShapes().size(), 0); // checking that removed from shape list
    assertEquals(am.getShapes().get("R"), null); // checking that removed from motion list
    assertEquals(am.getShapes().get("C"), null); // checking that removed from motion list
  }

  // Testing addShape method, two argument version
  @Test
  public void testAddShape2Arg() {
    AnimationModel am = new SimpleAnimationModel();

    // making sure there are no items in the shape list before hand
    assertEquals(am.getShapes().size(), 0); // checking that shapes list is empty
    assertEquals(am.getShapes().get("R"), null); // checking that motions list for R does
    // not exist
    assertEquals(am.getShapes().get("C"), null); // checking that motions list for C does not
    // exist
    assertEquals(am.getShapes().get("F"), null); // checking that motions list for C does not
    // exist

    // adding two shapes to the list
    am.addShape("R", ShapeType.RECTANGLE);
    am.addShape("C", ShapeType.CIRCLE);

    // checking that R and C exist in the list of shapes
    assertEquals(am.getShapes().size(), 2); // checking that added to shape list

    // check that specifically the two names added are the ones in the list
    ArrayList<String> keys = new ArrayList<String>(Arrays.asList("R", "C"));
    boolean allInGetShapes = true;
    for (int ii = 0; ii < am.getShapes().size(); ii++) {
      if (am.getShapes().get(keys.get(ii)) == null) {
        allInGetShapes = false;
      }
    }
    assertEquals(allInGetShapes, true);

    assertNotEquals(am.getShapes().get("R"), null); // checking that added to motions list
    assertNotEquals(am.getShapes().get("C"), null); // checking that added to motions list
    assertEquals(am.getShapes().get("F"), null); // checking that F was not added
  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10Arg() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with one invalid color value
    am.addShape(-2, 30, 45, "h", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);
  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgTwoAddedShapes() {
    AnimationModel am = new SimpleAnimationModel();

    // adding two shapes to the list, one with one invalid color value
    am.addShape(30, 30, 45, "c1", true, 4.5, 3.5,
        5, 7, ShapeType.CIRCLE);
    assertEquals(am.getShapes().size(), 1);
    am.addShape(-2, 30, 45, "h", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);
  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgInvalidGreen() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with one invalid color value
    am.addShape(30, -5, 45, "h", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);

  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgInvalidBlue() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with one invalid color value
    am.addShape(30, 35, -10, "h", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);

  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgOutside255() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with one invalid color value
    am.addShape(30, 270, 3, "h", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);

  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgMultipleInvalidColors() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with one invalid color value
    am.addShape(256, 270, -9, "h", true, 4.5, 3.5,
        5, 7, ShapeType.CIRCLE);
  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgInvalidHeightWidth() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with invalid height and width
    am.addShape(2, 27, 240, "h", true, 4.5, 3.5,
        -5, -7, ShapeType.CIRCLE);
  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgInvalidHeight() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with one invalid height
    am.addShape(2, 27, 240, "h", true, 4.5, 3.5,
        5, -7, ShapeType.CIRCLE);
  }

  // Testing addShape 10 argument version
  @Test (expected = IllegalArgumentException.class)
  public void testAddShape10ArgInvalidWidth() {
    AnimationModel am = new SimpleAnimationModel();

    // adding one shape to the list, with one invalid width
    am.addShape(2, 27, 240, "h", true, 4.5, 3.5,
        -5, 7, ShapeType.CIRCLE);
  }

  // Testing addShape 10 argument version
  @Test
  public void testAddShape10ArgValid() {
    AnimationModel am = new SimpleAnimationModel();

    // making sure there are no items in the shape list before hand
    assertEquals(am.getShapes().size(), 0); // checking that shapes list is empty
    assertEquals(am.getShapes().get("R"), null); // checking that motions list for R does
    // not exist
    assertEquals(am.getShapes().get("F"), null); // checking that motions list for C does not
    // exist

    // adding one shape to the list, all valid
    am.addShape(255, 255, 255, "R", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);

    // checking that there is now one item in the shapes list
    assertEquals(am.getShapes().size(), 1);
    // checking that specifically R exists in the list
    assertEquals(am.getShapes().get("R") != null,  true);

    assertNotEquals(am.getShapes().get("R"), null);
    assertEquals(am.getShapes().get("F"), null);
  }

  // Testing addShape 10 argument version
  @Test
  public void testAddShape10ArgValidMultiple() {
    AnimationModel am = new SimpleAnimationModel();

    // making sure there are no items in the shape list before hand
    assertEquals(am.getShapes().size(), 0); // checking that shapes list is empty
    assertEquals(am.getShapes().get("R"), null); // checking that motions list for R does
    // not exist
    assertEquals(am.getShapes().get("E"), null); // checking that motions list for C does not
    // exist
    assertEquals(am.getShapes().get("S"), null); // checking that motions list for R does
    // not exist
    assertEquals(am.getShapes().get("T"), null); // checking that motions list for C does not
    // exist
    assertEquals(am.getShapes().get("O"), null); // checking that motions list for C does not
    // exist

    // adding multiple valid shapes to the list
    am.addShape(255, 255, 255, "R", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);
    am.addShape(0, 0, 0, "E", false, -5, 3.5,
        5, 7, ShapeType.RECTANGLE);
    am.addShape(29, 50, 25, "S", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);
    am.addShape(255, 255, 255, "T", false, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);
    am.addShape(255, 255, 255, "O", true, 4.5, 3.5,
        5, 7, ShapeType.RECTANGLE);

    // confirm there are 5 elements in the array
    assertEquals(am.getShapes().size(), 5); // checking that shapes list is empty
    // check that specifically the five names added are the ones in the list
    ArrayList<String> keys = new ArrayList<String>(Arrays.asList("R", "E", "S", "T", "O"));
    boolean allInGetShapes = true;
    for (int ii = 0; ii < am.getShapes().size(); ii++) {
      if (am.getShapes().get(keys.get(ii)) == null) {
        allInGetShapes = false;
      }
    }
    assertEquals(allInGetShapes, true);

    assertNotEquals(am.getShapes().get("R"), null);
    assertNotEquals(am.getShapes().get("E"), null);
    assertNotEquals(am.getShapes().get("S"), null);
    assertNotEquals(am.getShapes().get("T"), null);
    assertNotEquals(am.getShapes().get("O"), null);

  }

  @Test (expected = IllegalStateException.class)
  public void testStartAnimationException() {
    AnimationModel am = new SimpleAnimationModel();

    am.addShape("R", ShapeType.RECTANGLE);
    am.addShape("F", ShapeType.CIRCLE);
    am.addShape("G", ShapeType.RECTANGLE);
    am.addShape("H", ShapeType.RECTANGLE);

    int[] rgb = new int[3];
    rgb[0] = 90;
    rgb[1] = 30;
    rgb[2] = 45;
    // incorrect consecutive ticks, end of X is not the same as next start of X, directly
    // consecutive
    am.addMotion(1, 4, 30, 30, 40, 45, 20,
        59, 30, 30, rgb, rgb, "R");
    am.addMotion(4, 11, 45, 30, 40, 45, 20,
        59, 30, 30, rgb, rgb, "R");

    am.startAnimation();
  }

  @Test (expected = IllegalStateException.class)
  public void testStartAnimationExceptionIndirectlyConsecutive() {
    AnimationModel am = new SimpleAnimationModel();

    am.addShape("R", ShapeType.RECTANGLE);
    am.addShape("F", ShapeType.CIRCLE);
    am.addShape("G", ShapeType.RECTANGLE);
    am.addShape("H", ShapeType.RECTANGLE);

    int[] rgb = new int[3];
    rgb[0] = 90;
    rgb[1] = 30;
    rgb[2] = 45;
    // incorrect consecutive ticks, end of X is not the same as next start of X, indirectly
    // consecutive
    am.addMotion(1, 4, 30, 30, 40, 45, 20,
        59, 30, 30, rgb, rgb, "R");
    am.addMotion(7, 11, 45, 30, 40, 45, 20,
        59, 30, 30, rgb, rgb, "R");

    am.startAnimation();
  }

  @Test
  public void testStartAnimation() {
    AnimationModel am = new SimpleAnimationModel();

    am.addShape("R", ShapeType.RECTANGLE);
    am.addShape("F", ShapeType.CIRCLE);
    am.addShape("G", ShapeType.RECTANGLE);
    am.addShape("H", ShapeType.RECTANGLE);

    int[] rgb = new int[3];
    rgb[0] = 90;
    rgb[1] = 30;
    rgb[2] = 45;
    // correctly consecutive ticks
    am.addMotion(1, 4, 30, 30, 40, 45, 20,
        59, 30, 30, rgb, rgb, "R");
    am.addMotion(4, 6, 30, 35, 45, 40, 59,
        20, 30, 30, rgb, rgb, "R");

    assertEquals(am.isAnimationStarted(), false);
    am.startAnimation();
    assertEquals(am.isAnimationStarted(), true);
  }

  @Test
  public void testIsAnimationStarted() {
    AnimationModel m = new SimpleAnimationModel();
    assertEquals(m.isAnimationStarted(), false);
    m.startAnimation();
    assertEquals(m.isAnimationStarted(), true);
  }

  // TODO tests for removeMotion

  // Tests for the toString method
  @Test
  public void testToStringModel() {
    AnimationModel am = new SimpleAnimationModel();
    am.addShape("R", ShapeType.RECTANGLE);

    int[] rgb = {90, 30, 50};
    am.addMotion(1, 4, 30, 30, 40, 45, 20,
        59, 30, 30, rgb, rgb,
        "R");
    assertEquals("R rectangle\n"
            + "motion R 1 30.00 40.00 30.00 20.00 90 30 50 4 30.00 45.00 30.00 59.00 90 30 50\n",
        am.toString());
  }

  // Testing TODO list

  // addMotion

  // TODO Test the following examples of overlapping motions for addMotion
  /* examples:
    2  3  4  5  6
    |-----|         motion1
       |--------|   motion2
    combined:
    |--|            newmotion1
       |--|         newmotion2
          |-----|   newmotion3

    2  3  4  5  6
    |--------|      m1
    |--------|      m2
    combined:
    |--------|      newmotion1

    2  3  4  5  6
          |-----|   m1
       |--------|   m2
    combined:
       |--|         newmotion1
          |-----|   newmotion2

    2  3  4  5  6
    |--------|      m1
       |--|         m2
    combined:
    |--|            newmotion1
       |--|         newmotion2
          |--|      newmotion3
     */

  @Test
  public void testAddMotionOverlappingMotionCombos() {
    int[] a = {3, 4, 5};
    Motion motion1 = new Motion(2, 3, 3, 4, 5, 5, 7,
        8,
        9, 10, a, a);
    Motion motion2 = new Motion(1, 3, 3, 3, 5, 6, 8,
        8,
        10, 10, a, a);
    AnimationModel am = new SimpleAnimationModel();
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(1, 3, 3, 3, 5, 6, 8,
        8,
        10, 10, a, a, "R");
    am.addMotion(2, 3, 3, 4, 5, 5, 7,
        8,
        9, 10, a, a, "R");

    assertEquals("Rectangle R\n"
        + "motion R 1 3.00 5.00 10.00 8.00 3 4 5\t2 3.00 5.50 10.00 8.00 3 4 5\n"
        + "motion R 2 3.00 5.50 9.00 7.00 3 4 5\t3 4.00 6.00 10.00 8.00 3 4 5\n", am.toString());
    am.removeMotion("R");
    am.addMotion(2, 4, 3, 6, 4, 4, 2, 2, 5,
        5, a, a, "R");
    am.addMotion(3, 5, 6, 6, 4, 4, 5, 8,
        5, 5, a, a, "R");
    assertEquals("Rectangle R\n"
        + "motion R 2 3.00 4.00 5.00 2.00 3 4 5\t3 4.50 4.00 5.00 2.00 3 4 5\n"
        + "motion R 3 4.50 4.00 5.00 5.00 3 4 5\t4 6.00 4.00 5.00 6.50 3 4 5\n"
        + "motion R 4 6.00 4.00 5.00 6.50 3 4 5\t5 6.00 4.00 5.00 8.00 3 4 5\n", am.toString());
  }

  // Tests that two overlapping motions can't be added if they have conflicting unchanged fields
  @Test(expected = IllegalArgumentException.class)
  public void testBadUnchangedFields() {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 4, 3, 6, 4, 4, 2, 2, 5,
        5, a, a, "R");
    am.addMotion(3, 5, 4, 4, 1, 1, 5, 8,
        5, 5, a, a, "R");
  }

  // TODO test adding a motion with no overlaps to existing motions in order
  // TODO test adding a motion that does nothing
  // TODO Test adding a motion that overlaps two existing motions
  // TODO Test adding motions out of order (i.e. t=1 to t=2, t=4 to t=5, then t=2 to 2=3)

  /**
   * Test error with overlapping change of x position for two motions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOverlapChangeX() {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 4, 3, 6, 4, 4, 2, 2, 5,
        5, a, a, "R");
    am.addMotion(3, 4, 4, 9, 4, 4, 2, 2, 5,
        5, a, a, "R");
  }

  /**
   * Test error with overlapping change of y pos for two motions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOverlapChangeY() {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};

    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 4, 3, 3, 5, 4, 2, 2, 5,
        5, a, a, "R");
    am.addMotion(3, 4, 4, 9, 7, 4, 2, 2, 5,
        5, a, a, "R");
  }

  /**
   * Test error with overlapping change of w for two motions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOverlapChangeW() {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 4, 3, 3, 4, 4, 2, 3, 5,
        5, a, a, "R");
    am.addMotion(3, 4, 3, 3, 4, 4, 6, 2, 5,
        5, a, a, "R");
  }

  /**
   * Test error with overlapping change of h for two motions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOverlapChangeH() {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 4, 3, 3, 4, 4, 2, 2, 5,
        6, a, a, "R");
    am.addMotion(3, 4, 3, 3, 4, 4, 2, 2, 5,
        9, a, a, "R");
  }

  /**
   * Test error with overlapping change of color for two motions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOverlapChangeColor() {
    AnimationModel am = new SimpleAnimationModel();
    int[] a = {3, 4, 5};
    int[] b = {3, 4, 5};
    am.addShape("R", ShapeType.RECTANGLE);
    am.addMotion(2, 4, 3, 3, 4, 4, 2, 2, 5,
        5, a, b, "R");
    am.addMotion(3, 4, 3, 3, 4, 4, 2, 2, 5,
        5, b, a, "R");
  }
  // TODO Test error with two back-to-back motions that break class invariant for shape state
  // continuity (i.e. m1 t=1 to t=2, x = 200, m2 t=2 to t=4 x = 150 when startAnimation is called
  // TODO Test error with two consecutive motions that break class invariant for shape state
  // continuity (i.e. m1 t=1 to t=2, x = 200, m2 t=3 to t=4 x = 150 when startAnimation is called

  // test for gaps (allowed in our model)
  @Test
  public void testGapMotion() {
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
    assertEquals(2, am.getShapeMotions("R").size());
    assertEquals(motion1, am.getShapeMotions("R").peek());
  }


  // TODO Test adding a motion as a teleport (i.e. m1 t=1 to t=1, x = 200 -> x = 300 (valid move)

  // test illegal argument exception for addMotion, addShape, removeShape,
  //  and removeMotion null input
  @Test (expected = IllegalArgumentException.class)
  public void testAddShapeNullInput() {
    AnimationModel m = new SimpleAnimationModel();
    m.addShape(null, ShapeType.RECTANGLE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testRemoveShapeNullInput() {
    AnimationModel m = new SimpleAnimationModel();
    m.removeShape(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testRemoveMotionNullInput() {
    AnimationModel m = new SimpleAnimationModel();
    m.removeMotion(null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddMotion() {
    AnimationModel m = new SimpleAnimationModel();
    int[] rgb = new int[3];
    rgb[0] = 90;
    rgb[1] = 30;
    rgb[2] = 45;
    m.addMotion(4, 6, 30, 35, 45, 40, 59,
        20, 30, 30, rgb, rgb, null);
  }

  @Test (expected = IllegalStateException.class)
  public void testRemoveStateException() {
    AnimationModel m = new SimpleAnimationModel();
    // error for animation that has not been started
    m.addShape("S", ShapeType.RECTANGLE);
    m.startAnimation();
    m.removeShape("S");
  }

  @Test (expected = IllegalStateException.class)
  public void testRemoveMotionStateException() {
    AnimationModel m = new SimpleAnimationModel();
    // error for animation that has not been started
    m.addShape("S", ShapeType.RECTANGLE);
    m.startAnimation();
    m.removeMotion("S");
  }

  @Test (expected = IllegalStateException.class)
  public void testAddMotionStateException() {
    AnimationModel m = new SimpleAnimationModel();
    // error for animation that has not been started
    m.addShape("S", ShapeType.RECTANGLE);
    m.startAnimation();
    int[] rgb = new int[3];
    rgb[0] = 90;
    rgb[1] = 30;
    rgb[2] = 45;
    m.addMotion(4, 6, 30, 35, 45, 40, 59,
        20, 30, 30, rgb, rgb, "S");
  }

  @Test (expected = IllegalStateException.class)
  public void testAddShapeStateException() {
    AnimationModel m = new SimpleAnimationModel();
    // error for animation that has not been started
    m.addShape("S", ShapeType.RECTANGLE);
    m.startAnimation();
    int[] rgb = new int[3];
    rgb[0] = 90;
    rgb[1] = 30;
    rgb[2] = 45;
    m.addShape("R", ShapeType.CIRCLE);
  }

  // test for set canvas
  @Test
  public void testSetCanvas() {
    AnimationModel am = new SimpleAnimationModel();

    assertEquals(am.getOriginX(), 0);
    assertEquals(am.getOriginY(), 0);
    assertEquals(am.getWidth(), 0);
    assertEquals(am.getHeight(), 0);

    am.setCanvas(40, 20, 400, 390);

    assertEquals(am.getOriginX(), 40);
    assertEquals(am.getOriginY(), 20);
    assertEquals(am.getWidth(), 400);
    assertEquals(am.getHeight(), 390);
  }

  // test for set canvas
  @Test
  public void testSetCanvasNegativeX() {
    AnimationModel am = new SimpleAnimationModel();

    assertEquals(am.getOriginX(), 0);
    assertEquals(am.getOriginY(), 0);
    assertEquals(am.getWidth(), 0);
    assertEquals(am.getHeight(), 0);

    am.setCanvas(-40, 20, 400, 390);

    assertEquals(am.getOriginX(), -40);
    assertEquals(am.getOriginY(), 20);
    assertEquals(am.getWidth(), 400);
    assertEquals(am.getHeight(), 390);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetCanvasNegativeHeight() {
    AnimationModel am = new SimpleAnimationModel();

    assertEquals(am.getOriginX(), 0);
    assertEquals(am.getOriginY(), 0);
    assertEquals(am.getWidth(), 0);
    assertEquals(am.getHeight(), 0);

    am.setCanvas(20, 20, -20, 390);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetCanvasNegativeWidth() {
    AnimationModel am = new SimpleAnimationModel();

    assertEquals(am.getOriginX(), 0);
    assertEquals(am.getOriginY(), 0);
    assertEquals(am.getWidth(), 0);
    assertEquals(am.getHeight(), 0);

    am.setCanvas(20, 20, 20, -390);
  }

  // tests for get height
  @Test
  public void testGetHeight() {
    AnimationModel am = new SimpleAnimationModel();
    am.setCanvas(-40, 20, 400, 390);
    assertEquals(am.getHeight(), 390);

    AnimationModel am2 = new SimpleAnimationModel();
    am2.setCanvas(40, 20, 400, 0);
    assertEquals(am2.getHeight(), 0);
  }

  // tests for get width
  @Test
  public void testGetWidth() {
    AnimationModel am = new SimpleAnimationModel();
    am.setCanvas(-40, 20, 400, 390);
    assertEquals(am.getWidth(), 400);

    AnimationModel am2 = new SimpleAnimationModel();
    am2.setCanvas(40, 20, 0, 0);
    assertEquals(am2.getWidth(), 0);
  }

  // tests for get origin y
  @Test
  public void testGetOriginY() {
    AnimationModel am = new SimpleAnimationModel();
    am.setCanvas(-40, 20, 400, 390);
    assertEquals(am.getOriginY(), 20);

    AnimationModel am2 = new SimpleAnimationModel();
    am2.setCanvas(40, -20, 0, 0);
    assertEquals(am2.getOriginY(), -20);

    AnimationModel am3 = new SimpleAnimationModel();
    am3.setCanvas(40, 0, 0, 0);
    assertEquals(am3.getOriginY(), 0);
  }

  //tests for get origin x
  @Test
  public void testGetOriginX() {
    AnimationModel am = new SimpleAnimationModel();
    am.setCanvas(-40, 20, 400, 390);
    assertEquals(am.getOriginX(), -40);

    AnimationModel am2 = new SimpleAnimationModel();
    am2.setCanvas(46, -20, 0, 0);
    assertEquals(am2.getOriginX(), 46);

    AnimationModel am3 = new SimpleAnimationModel();
    am3.setCanvas(0, 0, 0, 0);
    assertEquals(am3.getOriginX(), 0);
  }

  // test for get ordered shapes
  @Test
  public void testGetOrderedShapes() {
    AnimationModel am = new SimpleAnimationModel();
    am.addShape("C", ShapeType.RECTANGLE);
    am.addShape("R", ShapeType.CIRCLE);
    am.addShape("T", ShapeType.RECTANGLE);
    am.addShape("F", ShapeType.CIRCLE);
    am.addShape("P", ShapeType.CIRCLE);
    am.addShape("A", ShapeType.RECTANGLE);

    assertEquals(am.getShapes(), new ArrayList<Shape>(Arrays.asList(new Rectangle("C"),
        new Ellipse("R"), new Rectangle("T"), new Ellipse("F"),
        new Ellipse("P"), new Rectangle("A"))));
  }

}
