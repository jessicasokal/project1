package model;

/**
 * Class representing the tempo at which an animation is being played during
 * a certain period of time.
 */
public class Tempo extends ATimeInterval implements ITempo {
  private final int tempo;

  /**
   * Constructs a tempo with a given start tick, end tick, and desired tempo.
   * @param s the start tick for which this tempo would begin.
   * @param e the end tick for which this tempo would end.
   * @param t tempo the tempo of the animation at a certain time.
   */
  public Tempo(int s, int e, int t) {
    super(s, e);
    if (t <= 0) {
      throw new IllegalArgumentException("Cannot have a speed less than 1.");
    }
    this.tempo = t;
  }

  @Override
  public int getTempo() {
    return tempo;
  }
}
