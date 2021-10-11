package model;

import java.util.Comparator;

/**
 * Represents an abstract class for a time interval, holding
 * methods that all time intervals utilize.
 */
public abstract class ATimeInterval implements ITimeInterval {
  protected final int startTick;
  protected final int endTick;
  public final static Comparator<ITimeInterval> TIME_INTERVAL_COMP = new TimeIntervalComp();

  /**
   * Constructs a time interval with the given start and end ticks.
   * @param s the start tick of this interval.
   * @param e the end tick of this interval.
   */
  ATimeInterval(int s, int e) {
    if (s < 0 || e < 0 || e < s) {
      throw new IllegalArgumentException("Invalid tick values, cannot construct Time Interval");
    }
    this.startTick = s;
    this.endTick = e;
  }

  /**
   * Represents a comparator that compares two Motions based on their start tick values.
   */
  protected static class TimeIntervalComp implements Comparator<ITimeInterval> {
    @Override
    public int compare(ITimeInterval o1, ITimeInterval o2) {
      return o1.getStartTick() - o2.getStartTick();
    }
  }

  /**
   * Compares this time interval's tick range to the given tick.
   * -1 means entire interval comes before given tick.
   * 0 means this interval occurs during tick range.
   * 1 means this interval occurs after the tick range
   * at end tick already done.
   * @param tick represents the tick to compare this motion to
   * @return an int representing if the interval occurs during, before, or after the tick
   */
  @Override
  public int compareToTick(int tick) {
    if ((startTick == endTick && startTick == tick) || (tick >= startTick && tick < endTick)) {
      return 0;
    }
    else if (endTick <= tick) {
      return -1;
    }
    else {
      return 1;
    }
  }

  /**
   * Gets the first tick value of this motion.
   * @return the first tick value at the last tick of the motion.
   */
  @Override
  public int getStartTick() {
    return this.startTick;
  }

  /**
   * Gets the final tick value of this motion.
   * @return the end tick value at the last tick of the motion.
   */
  @Override
  public int getEndTick() {
    return this.endTick;
  }

}
