import java.util.Queue;
import model.AnimationModel;
import model.ITempo;
import model.SimpleAnimationModel;
import model.Tempo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for behavior of the Tempo class, as well as Tempo-related methods in the
 * SimpleAnimationModel class.
 */
public class TempoTest {

  // test invalid start/end
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStart() {
    ITempo t = new Tempo(-1, 3, 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidEnd() {
    ITempo t = new Tempo(1, -3, 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRange() {
    ITempo t = new Tempo(1, 0, 8);
  }

  // test invalid tempo
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTempo() {
    ITempo t = new Tempo(1, 3, 0);
  }


  // test getter methods
  @Test
  public void testGetter() {
    ITempo t = new Tempo(2, 4, 10);
    assertEquals(2, t.getStartTick());
    assertEquals(4, t.getEndTick());
    assertEquals(10, t.getTempo());
  }

  // test compareTo method
  @Test
  public void testCompare() {
    ITempo t = new Tempo(2, 4, 10);
    assertEquals(0, t.compareToTick(2));
    assertEquals(0, t.compareToTick(3));
    assertEquals(1, t.compareToTick(1));
    assertEquals(-1, t.compareToTick(4));
    assertEquals(-1, t.compareToTick(5));
  }

  // test adding a tempo to model
  @Test
  public void testAddAndGetTempo() {
    AnimationModel am = new SimpleAnimationModel();
    am.addTempo(3, 5, 10);
    assertEquals(10, am.getTempo(3));
    am.addTempo(5, 10, 3);
    am.addTempo(1, 2, 50);
    assertEquals(50, am.getTempo(1));
    assertEquals(3, am.getTempo(5));
    Queue<ITempo> tmp = am.getTempos();
    assertEquals(50, tmp.remove().getTempo());
    assertEquals(10, tmp.remove().getTempo());
    assertEquals(3, tmp.remove().getTempo());
    assertEquals(10, am.getTempo(3));
  }

  // test adding invalid tempo to model
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTempoToModel() {
    AnimationModel am = new SimpleAnimationModel();
    am.addTempo(1, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOverlapTempoToModel() {
    AnimationModel am = new SimpleAnimationModel();
    am.addTempo(1, 3, 5);
    am.addTempo(2, 5, 10);
  }

  // test getting a tempo with no tempos at that tick
  @Test
  public void testNoTempos() {
    AnimationModel am = new SimpleAnimationModel();
    assertEquals(0, am.getTempo(1));
    am.addTempo(3, 5, 10);
    assertEquals(0, am.getTempo(2));
    assertEquals(0, am.getTempo(5));
  }

  // test getting a tempo invalid tick
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGet() {
    AnimationModel am = new SimpleAnimationModel();
    am.addTempo(1, 3, 4);
    am.getTempo(-1);
  }
}
