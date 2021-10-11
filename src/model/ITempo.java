package model;

/**
 * Represents the interface for a tempo, aka a time interval with a specific tick
 * rate.
 */
public interface ITempo extends ITimeInterval {

  /**
   * Returns this tempo's tick rate.
   * @return this tempo's tick rate
   */
  int getTempo();

}
