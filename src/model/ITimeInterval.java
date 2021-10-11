package model;


/**
 * Class representing a time interval, aka a range of tick values.
 */
public interface ITimeInterval {

  /**
   * Compares this time interval's tick range to the given tick.
   * -1 means entire interval comes before given tick.
   * 0 means this interval occurs during tick range.
   * 1 means this interval occurs after the tick range
   * at end tick already done.
   * @param tick represents the tick to compare this motion to
   * @return an int representing if the interval occurs during, before, or after the tick
   */
  int compareToTick(int tick);

  /**
   * Gets the first tick value of this time interval.
   * @return the first tick value at the last tick of the time interval.
   */
  int getStartTick();

  /**
   * Gets the final tick value of this motion.
   * @return the end tick value at the last tick of the time interval.
   */
  int getEndTick();
}
