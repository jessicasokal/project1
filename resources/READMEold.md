Assignment 5
Our high level approach to solving this problem was to create two main interfaces and three main classes to represent the things that we would need for an animation. Specifically, we have one interface for a general model of an animation, which includes methods to start an animation, add shapes, add motions, remove motions, remove shapes, etc. We then implemented this interface to a specific implementtion of a model called SimpleAnimationModel which allows for all the functions of the interface, however, does not implement anything else. In this class, we have fields for keeping track of whether an animation has been started yet or not, the map of shapes and their associated motions to be completed in the animations, the general list of shapes for an animation and the current tick value that this animation is on. With this information, the model is able to execute the functions necessary to animate something in general. 

In order to get the functionality that we wanted for our model, we also had to implement an interface for Shapes and a class for a motion. Our shape class simply holds the fields and methods required to show a Shape in an animation, specifically by keeping track of its x and y coordinates, its height and width, color, and whether it is hidden or not. We added a field for whether it is hidden or not because we were not sure if that is an option that a client will have as input, so we added it for now unless otherwise specified in the assignment that this is not a valid change in shape. The methods in this class also act to change these attributes to actually animate the shape. 

Finally, we have a class called Motion that contains the starting attributes and ending attributes of a specific shape, and then uses them to execute the changes as a motion. This class also handles the combing of different motions together, so if two motions come in spearately that are in the same tick range, and do not change the same attribute, then we combine them into one.

A defining characteristic of our program was the fact that we allow users to input overlapping motions (i.e. motions occuring during the same time period), so long as
the overlapping motions are not trying to change the same shape fields. Therefore, we utilize splicing of motions and combining motions whenever we add a motion to
the shape's queue to hold true our class invariants (that there can only be at most one motion for any given tick during an animation). We also allow users to
input motions out of order by only checking that the start and end states of shapes match up for consectutive motions when the animation actually
starts - clients can also remove shapes and motions.

Some other more specific design choices we made was to represents the list of motions as a priority queue instead of a regular list so that it will automatically sort the list for us by a given comparator (in which we defined as the start tick for motions). This does mean that traversal of the queue can be difficult, as it is only sorted
at the head of the queue, so we handle this by not modifying things in place and instead storing out changes in other queues or copies while we pop the head off of 
the original priority queue to traverse it. 
-------------------------------------------------------------

Assignment 6 
Design changes
After the first assignment, we did have to make some changes to our model
so that we could optimize and decouple the model from the view. Here are the changes:
 - Updated the executeMotion function to set shapes to the end state of the motion,
 rather than the change (and updated methods of Shape as well) to allow for views to use it
 as needed
 - Removed all tick control from the model, as well as some unnecessary fields,
  and implemented tick control in both the Excellence main method and views that cared about ticks
 - made certain methods like splice and execute motion public in the Motion class to allow the
 views to utilize them
 - added a way for a user to get a shape and its motions with just the shape's name
 - added getter methods to shape and svg classes so that our view could use them appropriately

Functionality/Design choices of note
We allow overlapping motions (we are able to combine motions through various methods in our Motion
class) and motions with gaps, so long as once the animation is started, all consecutive motions
have the same start and end state. The warning is conveyed to the user once the animation starts,
since our model allows users to input motions out of order, as well as remove motions/shapes before
it starts. The exception orginates in the Motion class.

We allow for teleportations in the form of a motion that has the same start and end tick, and applies
any state changes during that singular tick. For example: 

- m1 : t = 0 x = 0, t = 1 x = 5
- m2 : t = 1 x = 5, t = 1 x = 20
- m3 : t = 1 x = 20, t = 3 x = 25

would represent valid motions of an animation, with m2 being the teleportation.

The way our animations are designed, specifically in regards to the visual and SVG view, our
first tick of the animation is tick 0 = 0s. 

In our animations, a shape is not visible until its first motion, and appears using the specificatons
of that motion's start state. Additionally, during any gaps in between motions, a shape appears visible
and with all the attributes of its last executed motion's end state. After a shape's last motion,
it remains visible for the remainder of the animation in the state of it's last executed motion's
end.

To prevent the client/view from being able to directly access a model and all it's methods that
allow for mutation (like addShape and addMotion), we implemented a ViewModel interface, which our
animation model implements. This IViewModel interface allows us to provide getter methods for the
views, which each have their own IViewModel that they are constructed with, while not revealing
the methods that allow for mutation. This allows for maximum functionality with minimal room
for unwanted modification/mutation of the model by the view (decoupling).

Each view implements the view interface that we implemented, with the methods that are unnecessary
(i.e. updateTick in textual and svg views) suppressed appropriately. Each method implements the 
render method accordingly with its protocol. Because the SVG and textual function similarly with
their appendable field, those also extend an AView abstract class to provide some consolidation of code
in the constructors. However, since the visual view differs so much in both its fields and its
constructor, we felt it unnecessary to have that view extend the abstract class.

Because of the range of functionality we had provided in the previous assignment in the model,
Shape, and Motion classes, we ultimately did not have to change substantial amounts of code in these
except for the changes documented above.
-------------------------------------------------------------

Assignment 7

Changes made from Assignment 6:
    - Abstracted out the visual view into a new AVisualAnimationViewFrame class that both the
    original visual view and interative visual view extend. We did this because for our interactive
    view, we essentially used the panel that was constructed in the visual view, along with
    some of the JFrame settings that were modified in the abstract class's constructor. There
    were also some functions (like render, updateTick, and getShapesAt) that would have been 
    essentially duplicated code if we hadn't extracted them out from the two visual views.
    - Added a modified toString method to the Motion class (toStringAsInt) that printed out all
    of the motion information formatted as integers, so that we could use it when generating
    the .txt file for our algorithmic animation
    - Added a method isAnimationOver(int tick) to the IViewModel interface, as that was necessary
    to implement the looping functionality in the interactive view.
    - While we still kept our main method in Excellence and it was still responsible for the
    command line parsing, we moved the Timer and remaining control of actually starting the 
    animation into the appropriate controller classes, since the controller should be handling
    the majority of that work. In the main method, we now create the controller with the specified
    model, view and tick rate, and then simply call controller.go()
    
Design Choices 

As stated above, we decided to abstract out some of the similar visual view code into a new
AVisualAnimationViewFrame class to avoid code duplication between our two visual views. This 
builds in the idea of the composite interactive view that is constructed with the same JPanel class
we created for the original visual view.

We chose to implement JButtons as our sole form of user interaction, as they were user friendly
and could minimize illegal inputs. We display the various attributes of the view next to the buttons
(speed, looping, and current state being playing or paused) that are updated instantaneously 
according to the user interaction with the buttons. 

In order to implement these buttons and the events they trigger in a well-design way, we chose
to have a Features interface that represented the various requests a user could have for an
interactive view. This both makes the events application specific (rather than dealing strictly
with ActionEvents and ActionListeners, which are more general) and also easy to expand if there
are more requests/functionalities that can be added in the future, as they simply would be added
to the Features interface. Our Controller then had its own Features object that it used, and our
interactive view had an addFeatures method that allowed for the controller to assign its
Features object's methods to be the result of any action events for each of the view's buttons.
This allowed for a smooth flow between the view's buttons, the Controller's Features object, and
then any necessary delegation back to the view (i.e. methods in the view that reflected the changes
to be made in the view for each type of request). It also allowed for the controller to 
maintain control over the view, rather than changes happening solely within the view when 
buttons were pressed. 

Any methods that were not utilized by other views (but were included in the view interface) were
suppressed with UnsupportedOperationExceptions.

For the design of the controller itself, we decided to split up our controllers into visual, timer-based
controllers (i.e. controller that would handle the visual and interactive view) and non-visual, non-timer-based
controllers (i.e. controller that would handle the textual view and SVG view). We abstracted similar
code out into an abstract class - which would also make it easy to implement another controller should
there be a third type of controller that doesn't fit well enough into the two existing concrete classes -
and had an overarching IController interface that contained the main "go" method.

While the visual view and interactive view had differences (namely that the interactive view required
the addition of the Features object in the controller), because they both rely heavily on the same
type of Timer, we felt it best to have one controller able to control animations with both types
of views.


 