package cs3500.animator;


import controller.IController;
import controller.NonVisualController;
import controller.VisualController;
import cs3500.animator.util.AnimationReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import model.AnimationModel;
import model.SimpleAnimationModel.Builder;
import view.IAnimationView;
import view.SVGAnimationView;
import view.TextualAnimationView;
import view.VisualAnimationView;
import view.InteractiveVisualAnimationView;

/**
 * Class that acts somewhat as a bare-bones controller for an animation.
 * Allows the client to run either textul, visual, or svg animation.
 */
public final class Excellence {
  private static Readable in;
  private static Appendable out = System.out;
  private static IAnimationView view;
  private static int speed = 1;
  private static AnimationModel model;


  /**
   * Main method that accepts various command line arguments specifying the input,
   * ouput, speed, and view type for the animation, and runs the specified
   * animation.
   *
   * @param args represents the various command line arguments inputted for the animation
   * @throws IOException if the input file cannot be found, or if there is an error
   *      while writing to the specified file
   */
  public static void main(String[] args) throws IOException {
    String viewString = "";
    for (int ii = 0; ii < args.length - 1; ii += 2) {
      String cmd = args[ii];
      String field = args[ii + 1];
      switch (cmd) {
        case "-in":
          in = new FileReader(field);
          model = AnimationReader.parseFile(in, new Builder());
          break;
        case "-out":
          out = new FileWriter(field);
          break;
        case "-speed":
          speed = Integer.parseInt(field);
          break;
        case "-view":
          viewString = field;
          break;
        default:
          throw new IllegalArgumentException("unrecognized command.");
      }
    }

    IController controller;
    switch (viewString) {
      case "text":
        view = new TextualAnimationView(model, speed, out);
        controller = new NonVisualController(model, view, speed);
        break;
      case "visual":
        view = new VisualAnimationView(model, speed);
        controller = new VisualController(model, view, speed);
        break;
      case "interactive":
        view = new InteractiveVisualAnimationView(model, speed);
        controller = new VisualController(model, view, speed);
        break;
      case "svg":
        view = new SVGAnimationView(model, speed, out);
        controller = new NonVisualController(model, view, speed);
        break;
      default:
        throw new IllegalArgumentException("Invalid animation type");
    }

    controller.animationGo();

    if (out instanceof FileWriter) {
      ((FileWriter) out).close();
    }
  }
}
