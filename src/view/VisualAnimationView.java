package view;

/**
 * Represents a visual animation view, where all shapes and motions are rendered through
 * JSwing and displayed visually.
 */
public class VisualAnimationView extends AVisualAnimationViewFrame {
  /**
   * Constructs a visual animation view with the given ViewModel as the source of information
   * about the shapes and motions in this animation, and the given tick rate.
   * @param am represents the ViewModel for this animation
   * @param tickRate represents the tick rate for this view, given in ticks per unit of time
   * @throws IllegalArgumentException if provided model is null or tick rate is negative
   */
  public VisualAnimationView(IAnimationViewModel am, int tickRate) {
    super(am, tickRate);
  }
}
