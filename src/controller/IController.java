package controller;

/**
 * Represents a general controller from the MVC design for a given animation that can
 * control the state of the associated view and model.
 */
public interface IController {

  /**
   * Start the animation's model and view rendering in the appropriate way,
   * depending on the type of view and model the Controller handles.
   */
  void animationGo();
}
