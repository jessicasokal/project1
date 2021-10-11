import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import model.Ellipse;
import model.Motion;
import model.Shape;

/**
 * Generates an animation representing bubble sort through sorting various sized ellipses.
 */
class BubbleSort {
  ArrayList<Shape> toSort;
  ArrayList<Motion> initialMotions;
  FileWriter writer;

  public BubbleSort() throws IOException {
    toSort = new ArrayList<Shape>(Arrays.asList(
        new Ellipse(127, 185, 246, "1", false, 40, 168, 18,
            12),
        new Ellipse(127, 185, 246, "2", false, 100, 168, 60,
            41),
        new Ellipse(127, 185, 246, "3", false, 160, 168, 70,
            45),
        new Ellipse(127, 185, 246, "4", false, 220, 168, 24,
            18),
        new Ellipse(127, 185, 246, "5", false, 280, 168, 35,
            29),
        new Ellipse(127, 185, 246, "6", false, 340, 168, 7,
            4),
        new Ellipse(127, 185, 246, "7", false, 400, 168, 50,
            35),
        new Ellipse(127, 185, 246, "8", false, 460, 168, 29,
            24),
        new Ellipse(127, 185, 246, "9", false, 520, 168, 43,
            38),
        new Ellipse(127, 185, 246, "10", false, 580, 168, 12,
            7)));

    writer = new FileWriter("bubblesort.txt");
  }

  public static void main(String[] args) throws IOException {
    BubbleSort bs = new BubbleSort();
    bs.bubbleSort();
  }

  void bubbleSort() throws IOException {
    writer.write("canvas 0 0 700 400\n");
    for (int ii = 0; ii < toSort.size(); ii++) {
      Shape currShape = toSort.get(ii);
      writer.write("shape " + currShape.toString() + '\n');

      Motion toWrite = new Motion(1, 1, currShape.getX(), currShape.getX(),
          currShape.getY(), currShape.getY(), currShape.getWidth(), currShape.getWidth(),
          currShape.getHeight(), currShape.getHeight(),
          new int[]{currShape.getColor().getRed(),
              currShape.getColor().getGreen(), currShape.getColor().getBlue()},
          new int[]{currShape.getColor().getRed(),
              currShape.getColor().getGreen(), currShape.getColor().getBlue()});
      writer.write("motion " + currShape.getName() +  " " +  toWrite.toStringAsInt() + '\n');
    }

    int startTick = 1;
    int endTick = 10;
    for (int ii = 0; ii < this.toSort.size(); ii++) {
      for (int jj = 0; jj < this.toSort.size() - ii - 1; jj++) {

        Shape first = toSort.get(jj);
        Shape second = toSort.get(jj + 1);

        Motion newFirstColorChange = new Motion(startTick, startTick + 1, first.getX(),
            first.getX(),
            first.getY(), first.getY(),
            first.getWidth(), first.getWidth(),
            first.getHeight(), first.getHeight(),
            new int[]{first.getColor().getRed(), first.getColor().getGreen(),
                first.getColor().getBlue()},
            new int[]{255, 0, 0});
        Motion newSecondColorChange = new Motion(startTick, startTick + 1, second.getX(),
            second.getX(),
            second.getY(), second.getY(),
            second.getWidth(), second.getWidth(),
            second.getHeight(), second.getHeight(),
            new int[]{second.getColor().getRed(), second.getColor().getGreen(),
                second.getColor().getBlue()},
            new int[]{255, 0, 0});

        writer.write("motion " + first.getName() + " " + newFirstColorChange.toStringAsInt()
            + '\n');
        writer.write("motion " + second.getName() + " " + newSecondColorChange.toStringAsInt()
            + '\n');


        second.setColor(255, 0,0 );
        first.setColor(255, 0,0 );
        toSort.set(jj, first);
        toSort.set(jj + 1, second);

        if (first.getWidth() > second.getWidth()) {
          Motion newFirst = new Motion(startTick + 1, endTick, first.getX(), second.getX(),
              first.getY(), first.getY(),
              first.getWidth(), first.getWidth(),
              first.getHeight(), first.getHeight(),
              new int[]{first.getColor().getRed(), first.getColor().getGreen(),
                  first.getColor().getBlue()},
              new int[]{first.getColor().getRed(), first.getColor().getGreen(),
                  first.getColor().getBlue()});

          Motion newSecond = new Motion(startTick + 1, endTick, second.getX(), first.getX(),
              second.getY(), second.getY(),
              second.getWidth(), second.getWidth(),
              second.getHeight(), second.getHeight(),
              new int[]{second.getColor().getRed(), second.getColor().getGreen(),
                  second.getColor().getBlue()},
              new int[]{second.getColor().getRed(), second.getColor().getGreen(),
                  second.getColor().getBlue()});

          writer.write("motion " + first.getName() + " " + newFirst.toStringAsInt() + '\n');
          writer.write("motion " + second.getName() + " " + newSecond.toStringAsInt() + '\n');

          double secondX = second.getX();
          double firstX = first.getX();
          second.setX(firstX);
          first.setX(secondX);
          toSort.set(jj, second);
          toSort.set(jj + 1, first);
        }

        Motion newFirstColorChange2 = new Motion(endTick, endTick + 1,
            first.getX(), first.getX(),
            first.getY(), first.getY(),
            first.getWidth(), first.getWidth(),
            first.getHeight(), first.getHeight(),
            new int[]{first.getColor().getRed(), first.getColor().getGreen(),
                first.getColor().getBlue()},
            new int[]{127, 185, 246});
        Motion newSecondColorChange2 = new Motion(endTick, endTick + 1, second.getX(),
            second.getX(),
            second.getY(), second.getY(),
            second.getWidth(), second.getWidth(),
            second.getHeight(), second.getHeight(),
            new int[]{second.getColor().getRed(), second.getColor().getGreen(),
                second.getColor().getBlue()},
            new int[]{127, 185, 246});

        writer.write("motion " + first.getName() + " " + newFirstColorChange2.toStringAsInt()
            + '\n');
        writer.write("motion " + second.getName() + " " + newSecondColorChange2.toStringAsInt()
            + '\n');

        toSort.get(jj).setColor(127, 185,246);
        toSort.get(jj + 1).setColor(127, 185,246);

        startTick = endTick + 1;
        endTick = endTick + 10;
      }

      // change the last sorted shapes color (guaranteed to be in correct place) to dark blue
      toSort.get(this.toSort.size() - ii - 1).setColor(0, 0, 255);
      Shape s = toSort.get(this.toSort.size() - ii - 1);
      Motion sMotion = new Motion(endTick, endTick + 1, s.getX(), s.getX(),
          s.getY(), s.getY(),
          s.getWidth(), s.getWidth(),
          s.getHeight(), s.getHeight(),
          new int[]{127, 185,246},
          new int[]{0, 0, 255});
      writer.write("motion " + s.getName() + " " + sMotion.toStringAsInt() + '\n');

      startTick = endTick + 1;
      endTick = endTick + 10;
    }

    writer.close();
  }
}
