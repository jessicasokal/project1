package view;

import controller.Features;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import model.IMotion;
import model.Shape;

/**
 * Represents an abstract visual animation view frame, where all shapes and motions are rendered
 * through JSwing and displayed visually.
 */
public abstract class AVisualAnimationViewFrame extends JFrame implements IAnimationView {

  protected VisualAnimationViewPanel vavp;
  protected final IAnimationViewModel am;
  protected int tick;
  protected int tickRate;

  /**
   * Constructs a visual animation view with the given ViewModel as the source of information
   * about the shapes and motions in this animation. Initializes JSwing animation elements,
   * including the JPanel.
   * @param am represents the ViewModel for this animation
   * @throws IllegalArgumentException if provided model is null or tick rate is less than or equal
   *          to 0
   */
  public AVisualAnimationViewFrame(IAnimationViewModel am, int tickRate) {
    super();
    if (am == null) {
      throw new IllegalArgumentException("View cannot be constructed with null parameters.");
    }
    if (tickRate <= 0) {
      throw new IllegalArgumentException("View cannot be constructed with a tick rate of "
          + "0 or less.");
    }
    this.setTitle("Animation");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // if can just do with othe then dont need these fields
    int topLeftCornerX = am.getOriginX();
    int topLeftCornerY = am.getOriginY();
    int width = am.getWidth();
    int height = am.getHeight();
    this.tick = 0;
    this.tickRate = tickRate;

    //this.setLayout(new BorderLayout());
    vavp = new VisualAnimationViewPanel();
    vavp.setLayout(new BoxLayout(vavp, BoxLayout.PAGE_AXIS));
    vavp.setPreferredSize(new Dimension(width, height));

    final JScrollPane scroll = new JScrollPane(vavp);
    this.add(scroll, BorderLayout.CENTER);

    // set window (frame) size to either the width and height of canvas + 40 to account for
    // scroll bars, or 800 x 800 if the canvas size is larger than that
    this.setPreferredSize(new Dimension(Math.min(width + 40, 800),
        Math.min(height + 40, 800)));

    this.am = am;

    vavp.setOrigin(topLeftCornerX, topLeftCornerY);
    vavp.setCanvas(width, height);

    this.pack();

    this.setVisible(true);
  }

  /**
   * Returns the list of shapes that are visible in the animation in the range
   * [startTickValue, endTickValue).
   *
   * @param startTickValue represents the start of the tick range (inclusive)
   * @param endTickValue represents the end of the tick range (exclusive)
   * @return the list of shapes that are visible in the animation during the given range
   */
  protected List<Shape> getShapesAt(int startTickValue, int endTickValue) {
    List<Shape> shapes = am.getOrderedShapes();
    List<Shape> movedShapes = new ArrayList<Shape>();
    for (Shape shape : shapes) {
      // get motions for that shape
      Queue<IMotion> motions = this.am.getShapeMotions(shape);
      IMotion prevMotion = null;

      // find the motion closest to the given tick range (either before or during)
      for (IMotion m : motions) {
        if (m.compareToTick(startTickValue) > 0) {
          break;
        }
        prevMotion = m;
      }

      // if there are motions that exist for this shape during or before the given tick range...
      if (prevMotion != null) {
        IMotion splicedMotion = prevMotion.splice(startTickValue, endTickValue);
        if (splicedMotion == null) {
          // the last executed motion is before the given tick range
          prevMotion.executeMotion(shape);
        } else {
          // the shape has a motion that is currently being executed in the given tick range
          splicedMotion.executeMotion(shape);
        }
        movedShapes.add(shape);
      }
    }
    return movedShapes;
  }

  /**
   * Renders this animation view at the current tick
   * as a JSwing frame with a panel representing the canvas where
   * the animation frame is drawn.
   */
  @Override
  public void render() {
    vavp.setShapes(getShapesAt(this.tick, this.tick + 1));
  }

  /**
   * Used for visual views to add features (requests) to the animation if interactive.
   * By default, suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-interactive
   *          visual view
   */
  @Override
  public void addFeatures(Features features) {
    throw new UnsupportedOperationException("Cannot add features to non-interactive view.");
  }

  /**
   * Used for visual views to restart the animation at tick 0 and reset all shapes to original
   * positions in the animation.
   */
  @Override
  public void restart() {
    this.tick = 0;
  }

  /**
   * Used for visual views to pause the animation at the current state if interactive.
   * By default, suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-interactive
   *          visual view
   */
  @Override
  public void pause() {
    throw new UnsupportedOperationException("Cannot pause non-interactive view.");
  }

  /**
   * Used for visual views to resume the animation if interactive from the last executed state.
   * By default, suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-interactive
   *          visual view
   */
  @Override
  public void resume() {
    throw new UnsupportedOperationException("Cannot resume non-interactive view.");
  }

  /**
   * Used for visual views to toggle the animation's looping ability if interactive.
   * By default suppressed here.
   *
   * @throws UnsupportedOperationException to suppress this method if called on non-interactive
   *          visual view
   */
  @Override
  public void toggleLooping() {
    throw new UnsupportedOperationException("Cannot toggle looping "
        + "on non-interactive visual view.");
  }

  @Override
  public void changeSpeed(int newSpeed) {
    if (newSpeed <= 0) {
      throw new IllegalArgumentException("Cannot change speed to less than 1.");
    }
    this.tickRate = newSpeed;
  }

  /**
   * Increases this view's tick by one, and render this frame's components accordingly.
   */
  @Override
  public void updateTick() {
    this.tick += 1;
    this.render();
    this.repaint();
  }

  @Override
  public int getTick() {
    return this.tick;
  }

  @Override
  public int getSpeed() {
    return this.tickRate;
  }

  @Override
  public void toggleDiscrete() {
    throw new UnsupportedOperationException("Cannot toggle discrete playing "
        + "on non-interactive visual view.");
  }

  @Override
  public void toggleFill() {
    throw new UnsupportedOperationException("Cannot toggle fill "
        + "on non-interactive visual view.");
  }
}
