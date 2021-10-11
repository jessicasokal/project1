1. Implementing Toggle Fill & Plus Sign Shape

- We implemented the features to add a plus sign shape and to toggle the fill of all the shapes in an animation.

In order to do this we had to:

- Create a new concrete class called “Plus.java” that extended our AShape abstract class. In this class, we overrode the methods visualizeShape, getType, and had to add two methods makeXs and makeYs. VisualizeShape now returned the plus sign as a java polygon, that the views could then use to create graphics 2d objects out of. In addition, we had to add public methods called makeXs and makeYs in order to create the x and y arrays for the points in the polygon. We had to add this to our interface (Shape.java) since they were public and in our AShape.java file simply returned an UnsupportedOperationException for the other shapes that don’t override these methods as they do not use x or y arrays for their points. If we need to create a new polygon, however, we can once again use these methods and override them instead with their points.

- We then also had to add “PLUS” to our ShapeType enum in SimpleanimationModel.java (line 148), and add it as an option for declaring and adding shapes in our builder class, specifically in the declareShape and addShape methods in SimpleAnimationModel.java. This made it so that Plus was recognized as a Shape that could be used.

- With these changes, the shape was able to be used and viewed in the interactive mode of our animations, however, to are it compatible with our SVG mode we also had to make changes there.

- Specifically, we had to add a condition in our “render” method for a Plus shape, in order to allow it to render a plus sign shape. In this condition, we had to change the tag to polygon and give the file all the pairs of points of the shape. To give these pairs of points, we had to create a private helper method called getPoints, which just looped through the x and y arrays of the polygon and returned a useful string of all the pairs.

- We also had to add a condition to our SVGMotionTag method to deal with motions dealing with the plus sign, creating an overloaded version of our animateTag method to do so that took in strings for the starting and ending conditions instead of ints as we had before.

- To implement toggling fill of all the shapes, we had to:

- In Shape.java, added makeFilled and makeOutlined methods that either set a shape as outlined or filled.

- In AShape.java, added a field for whether an individual shape is filled for not, and implemented the new methods (makeOutlined, makeFilled)

- In IAnimationView.java, add a public method to toggle the fill of shapes

- In Features.java add a feature for toggling fill

- VisualController, add a method toggleFill that just delegates to the toggleFill in the view.

- In InteractiveVisualAnimationView.java, implemented toggleFill method to delegate to VisualAnimationViewPanel to actually deal with toggling the fill. In addition, added button to button panel to allow user to interactively change the fill of shapes

- In our abstract class for visual view frames (AVisualAnimationViewFrame.java) implement toggleFill and throw an unsupported operation exception as only the interactive view needs it, and thus can just override it itself.

- In our VisualAnimationPanel.java file we did the actual toggling of the fill by adding a boolean field that keeps track of if the shapes should be filled or not. We then altered our setShapes method to either set all the shapes as filled (using the new methods we implemented in AShape) if the boolean field is true, or false otherwise. Then in our paint component method, we just checked the boolean again, and if it was filled, filled all the shapes, otherwise just “drew” them.

2. Implementing Discrete and Continuous

First, we started by adding a new method to our Features interface, "enableDisableDiscrete". This allowed us to add the implementation of it in our
VisualController's AnimationFeatures class. InteractiveView had it's own "toggleDiscrete" method called by the Features class
that changed the boolean we added to represent if it was discrete or continuous playing (our IAnimationView interface and view abstract classes also had this method then, but we suppressed
them for all other views with UnsupportedOperationException). This allowed the interactive view to check every time it updated its tick and rendered
itself if it should render the next continuous or discrete tick by checking the value of the boolean variable in the InteractiveView class.

The bulk of the determining the next discrete tick work was done in our SimpleAnimationModel class through the "findNextTick" method that we
added to the IAnimationViewModel interface (so that our view could have access to it) and the SimpleAnimationModel concrete class. This method
utilized the queue structure that we hold our motions in, and searched for the nearest tick (inclusive) that was a start or end tick of a motion.
If there was no next tick found, the method simply returns the provided tick value.

3. Implementing Slow Mo

We started by changing the AnimationReader class (added an additional "tempo" keyword and "readTempo" helper method) and the AnimationBuilder 
interface to have an addTempo method. This allows us to simply add a line formatted as seen below to any input file, and then read it in
as it would a shape or motion keyword:

tempo 1 3 40

values are start tick, end tick, and tick rate

We changed our structure for the model's supporting classes to lessen code duplication and expose fewer concrete classes, extending it to 
include an ITimeInterval, ITempo, and IMotion interface, as well as an ATimeInterval abstract classes housing the start and end tick methods.

We added a concrete Tempo class that could hold a start tick, end tick, and tick rate to represent the new tempo.

We added a queue of ITempo to our SimpleAnimationModel class to hold any tempos added. Additionally, we added the getTempos, getTempo(int tick), 
and addTempo(int start, int end, int tempo) methods to the SimpleAnimationModel class to allow for the addition and getting of the tempos now
held by the model. It is in the addTempo method that we check for any overlapping tempos that would be invalid because their time intervals 
conflict.

We added a helper method to our VisualController class called checkSlowMo that checks if there is currently a different tempo to be applied
rather than the default tick rate, and applies it if appropriate. When in these tempo time intervals, the user is not able to change the 
speed of the animation. Once the tempo aka slow mo period ends, whatever the last executed tickRate was is restored to the animation through the
checkSlowMo method.


