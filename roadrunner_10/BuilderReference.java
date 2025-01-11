// Robot waits for the specified time in seconds (NOT MILLISECONDS!)
// This is a simple wait segment that is useful for running actions in between trajectories.

.waitSeconds(5)



// Robot turns counterclockwise by the specified angle
// This turn is in radians, so you must convert your degrees to radians using `Math.toRadians()`.
// If you see `Math.PI`, it is already in radians, and does not need `Math.toRadians()`. Degrees from 0 to 360 need to be converted to radians.
// To turn clockwise, use a negative angle.

.turn(-Math.PI / 6) // Turns clockwise by `Math.PI / 6` degrees, ending at a heading of 0 degrees
.turn(Math.PI / 6) // Turns counterclockwise by `Math.PI / 6` degrees, ending at the original heading



// Robot turns counterclockwise to the specified angle
// This turn is in radians, so you must convert your degrees to radians using `Math.toRadians()`.
// By default, the robot will turn in the shortest direction to the specified heading.
// To turn in the opposite direction, you can add or subtract a very small number (1e-6) to the heading you want to turn to.
// If it still does not work, you can use the `turn()` method instead.

.turnTo(Math.toRadians(90)) // Turns to a heading of 90 degrees
.turnTo(Math.PI / 6) // Turns to a heading of `Math.PI / 6` degrees, ending at the original heading



// `setTangent()` allows you to set a heading tangent on a trajectory, allowing you to follow a trajectory at arbitrary heading tangents
// This is equivalent to specifying a custom tangent in the `TrajectoryBuilder()` constructor.

.setTangent(90) // Sets tangent to 90



// If you see these hooks on the start and/or end of spline trajectories, you can use `setReversed()` to fix them
// These hooks make your robot move backwards instead of forward or vice versa in splines, creating suboptimal paths.
// This can be fixed by reversing the path using `setReversed(true)`.

.setReversed(false)  // Unreversed trajectory has hooks on the start and end
.splineTo(Vector2d(-48.0, -24.0), -Math.PI / 2)
.setReversed(false)
.splineTo(Vector2d(-48.0, 0.0), Math.PI)



.setReversed(true)  // Reversed trajectory has no hooks on the start and end, and is smooth
.splineTo(Vector2d(-48.0, -24.0), -Math.PI / 2)
.setReversed(false)
.splineTo(Vector2d(-48.0, 0.0), Math.PI)



// By default, each trajectory is set to `setReversed(false)`, which does not reverse the paths.
// This means that:
.setReversed(false)
.splineTo(Vector2d(-48.0, -24.0), -Math.PI / 2)
.setReversed(false)
.splineTo(Vector2d(-48.0, 0.0), Math.PI)

// Is the same as:
.splineTo(Vector2d(-48.0, -24.0), -Math.PI / 2)
.splineTo(Vector2d(-48.0, 0.0), Math.PI)



// Robot moves to the specified coordinates while maintaining its heading.
// Both `strafeTo()` and `strafeToConstantHeading()` are equivalent.
// So, if you start at a 90 degree angle, it will keep that angle the entire path.

.strafeTo(new Vector2d(48, -48))
.strafeToConstantHeading(new Vector2d(48, -48))



// Robot moves to the specified coordinates while linearly interpolating between the start heading and a specified end heading
// In other words, it constantly turns to a certain heading (once more, in radians) while moving to the specified coordinates.

.strafeToLinearHeading(new Vector2d(36, 36), Math.toRadians(90))



// Robot moves to the specified coordinates while splinely interpolating between the start heading and a specified end heading
// In other words, it constantly turns to a certain heading (once more, in radians) while moving to the specified coordinates.

.strafeToSplineHeading(new Vector2d(36, 36), Math.toRadians(90))



// Robot moves to the specified x coordinate in the direction of the robot heading (straight line).
// Both `lineToX()` and `lineToXConstantHeading()` are equivalent.
// 🚨 Will cause an error if your heading is perpendicular to direction your robot is traveling! 🚨

.lineToX(48)
.lineToXConstantHeading(48)



// Robot moves to the specified y coordinate in the direction of the robot heading (straight line).
// Both `lineToY()` and `lineToYConstantHeading()` are equivalent.
// 🚨 Will cause an error if your heading is perpendicular to direction your robot is traveling! 🚨

.lineToY(36)
.lineToYConstantHeading(36)



// Robot moves to the specified coordinates in a spline path while following a tangent heading interpolator

.splineTo(new Vector2d(48, 48), Math.PI / 2)



// Robot moves to the specified coordinates in a spline path while following a tangent heading interpolator

.setTangent(0)
.splineTo(new Vector2d(48, 48), Math.PI / 2)



// Robot moves to the specified coordinates in a spline path while keeping the heading constant
// The robot maintains the heading it starts at throughout the trajectory.
// To change the shape of the spline, change `endTangent`.

.setTangent(0)
.splineToConstantHeading(new Vector2d(48, 48), Math.PI / 2)



// Robot moves to the specified coordinates in a spline path while separately linearly interpolating the heading
// To change the shape of the spline, change `endTangent`.

.setTangent(0)
.splineToLinearHeading(new Pose2d(48, 48, 0), Math.PI / 2)



// Robot moves to the specified coordinates in a spline path while separately spline interpolating the heading
// To change the shape of the spline, change `endTangent`.

.setTangent(0)
.splineToSplineHeading(new Pose2d(48, 48, 0), Math.PI / 2)


