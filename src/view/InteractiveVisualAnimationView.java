package view;

import controller.Features;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents a visual animation view, where all shapes and motions are rendered through
 * JSwing and displayed visually, along with options for an interactive GUI.
 */
public class InteractiveVisualAnimationView extends AVisualAnimationViewFrame {

  private final JLabel speedLabel;
  private final JLabel loopingLabel;
  private final JLabel stateLabel;
  private final JLabel discreteLabel;
  private final JLabel tickLabel;
  protected JButton toggleFill;


  // protected for testing purposes when creating a mock view class that extends this class
  protected JButton restartButton;
  protected JButton increaseButton;
  protected JButton decreaseButton;
  protected JButton pauseButton;
  protected JButton resumeButton;
  protected JButton loopButton;
  protected JButton discreteButton;

  private boolean loopingEnabled;
  private boolean discreteEnabled;

  /**
   * Constructs a visual animation view frame with the given ViewModel as the source of information
   * about the shapes and motions in this animation, and the given tick rate.
   * Also constructs the various components of the frame/view that allow for user interactivity.
   * @param av represents the ViewModel for this animation
   * @param tickRate represents the given initial tick rate in ticks per unit of time
   */
  public InteractiveVisualAnimationView(IAnimationViewModel av, int tickRate) {
    super(av, tickRate);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    this.add(buttonPanel, BorderLayout.SOUTH);

    restartButton = new JButton("start/restart");
    buttonPanel.add(restartButton);

    increaseButton = new JButton("increase speed");
    buttonPanel.add(increaseButton);

    decreaseButton = new JButton("decrease speed");
    buttonPanel.add(decreaseButton);

    pauseButton = new JButton("pause");
    buttonPanel.add(pauseButton);

    resumeButton = new JButton("resume");
    buttonPanel.add(resumeButton);

    loopButton = new JButton("toggle looping");
    buttonPanel.add(loopButton);

    discreteButton = new JButton("toggle discrete");
    buttonPanel.add(discreteButton);

    toggleFill = new JButton("toggle fill");
    buttonPanel.add(toggleFill);

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
    buttonPanel.add(infoPanel);
    //this.add(infoPanel, BorderLayout.WEST);
    speedLabel = new JLabel("Speed");
    // need to get actual speed from controller
    speedLabel.setText("Speed: " + this.tickRate);
    infoPanel.add(speedLabel);

    this.loopingEnabled = false;
    this.discreteEnabled = false;

    loopingLabel = new JLabel("Looping");
    // need to get actual enabled boolean from controller
    loopingLabel.setText("Looping: disabled");

    stateLabel = new JLabel("State");
    stateLabel.setText("State: not started");

    discreteLabel = new JLabel("Discrete");
    discreteLabel.setText("Playing mode: continuous");

    tickLabel = new JLabel("Tick");
    tickLabel.setText("Current tick: 0");

    infoPanel.add(loopingLabel);
    infoPanel.add(stateLabel);
    infoPanel.add(discreteLabel);
    infoPanel.add(tickLabel);

    this.setPreferredSize(new Dimension((int)Math.max(this.getPreferredSize().getWidth(),
        buttonPanel.getLayout().minimumLayoutSize(this).getWidth()),
        (int)this.getPreferredSize().getHeight()));

    this.pack();
    this.setVisible(true);
  }

  /**
   * Adds action listeners to all the appropriate components of this view frame, resulting
   * in triggering the appropriate method of the given features object.
   * @param features represents the features object to be used as the result of the action
   *                 listener being triggered
   * @throws IllegalArgumentException if features is null
   */
  @Override
  public void addFeatures(Features features) throws IllegalArgumentException {
    if (features == null) {
      throw new IllegalArgumentException("Cannot add null feature.");
    }
    restartButton.addActionListener(evt -> features.restartAnimation());
    increaseButton.addActionListener(evt -> features.changeSpeed(this.tickRate + 1));
    decreaseButton.addActionListener(evt -> features.changeSpeed(this.tickRate - 1));
    pauseButton.addActionListener(evt -> features.pauseAnimation());
    resumeButton.addActionListener(evt -> features.resumeAnimation());
    loopButton.addActionListener(evt -> features.enableDisableLooping());
    discreteButton.addActionListener(evt -> features.enableDisableDiscrete());
    toggleFill.addActionListener(evt -> features.toggleFill());
  }

  @Override
  public void updateTick() {
    this.tick += 1;
    if (loopingEnabled && this.am.isAnimationOver(this.tick)) {
      this.restart();
    }
    if (this.discreteEnabled) {
      this.tick = this.am.findNextTick(this.tick);
    }
    this.tickLabel.setText("Current tick: " + this.tick);
    this.render();
    this.repaint();
  }

  @Override
  public void restart() {
    this.tick = 0;
    stateLabel.setText("Current state: playing");
  }

  @Override
  public void pause() {
    stateLabel.setText("Current state: paused");
  }

  @Override
  public void resume() {
    stateLabel.setText("Current state: playing");
  }

  @Override
  public void toggleLooping() {
    this.loopingEnabled = !this.loopingEnabled;
    String label = "disabled";
    if (this.loopingEnabled) {
      label = "enabled";
    }
    loopingLabel.setText("Looping: " + label);
  }

  @Override
  public void toggleDiscrete() {
    this.discreteEnabled = !this.discreteEnabled;
    String label = "continuous";
    if (this.discreteEnabled) {
      label = "discrete";
    }
    discreteLabel.setText("Playing mode: " + label);
  }

  @Override
  public void toggleFill() {
    this.vavp.changeFill();
  }

  @Override
  public void changeSpeed(int newSpeed) {
    super.changeSpeed(newSpeed);
    speedLabel.setText("Speed: " + tickRate);
  }
}
