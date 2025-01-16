# Why Run To Position Is Not A Good Idea
When using a PID(F) controller to move a motor to a certain position, it is often tempting
to use the `RUN_TO_POSITION` mode, especially since it appears in many examples.
This motor run mode is built into the FTC SDK and is designed to move a motor to a certain encoder position autonomously.

However, this mode is not ideal for a few reasons (ordered from most to least significant):
## Locked at 20 HZ

The `RUN_TO_POSITION` mode is locked at 20 HZ,
meaning that the motor will only update its position every 50 milliseconds.

**Why this is a problem:**

Imagine a scenario where you are trying to move a motor to a certain position.
If the motor is moving too fast, it will overshoot the target position and then have to correct itself.
However, if the motor is only updating its position 20 times per second, it will not be able to correct itself quickly enough,
leading to overshoots and even oscillations.

Here's a [demo](https://www.youtube.com/watch?v=fusr9eTceEo&t=133s) of this effect.

### Why going Custom PID(F) is better:
With a custom PID(F) controller, you can update the motor's position every loop. Since most FTC loops run at higher than 20hz,
a custom solution will update the motor's position more frequently solving the problems mentioned.

## Syntax Requirement and Crash Errors
The `RUN_TO_POSITION` mode requires that you set the target position before you start the motor.
```java
motor.setTargetPosition(1000);
motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
motor.setPower(1);
```

First, this syntax is required to be followed exactly. This means that if you want to switch between manual and automatic control,
you will have to change the motor mode every time you switch. Programming any FSM or even any complex auto becomes quite difficult.

While this seems like a minor inconvenience, if done in the wrong order, it **crashes the entire bot without outputting any useful error messages**.
This can be a significant issue not only when debugging but also in games if it's precisely not controlled.

## Customizability and Tuning
Although the 'RUN_TO_POSITION' mode allows for some basic PID tuning, most teams don't do it and just use the default values.
This can lead to scenarios where the motor improperly responds to disturbances or even fails to reach the target position.

Moreover, the RTP mode is a simple PID controller, meaning that it doesn't have the advanced features that a custom PID(F) controller has.

For example, for systems like arms and slides, you may want to add feedforward gains to counteract gravity or friction. RTP will not allow you to do that.

- https://www.ctrlaltftc.com/feedforward-control
- https://www.youtube.com/watch?v=E6H6Nqe6qJo
# Sooo, how do I solve it?
PID(F) controllers seem scary, but they really aren't. All they do is try to get a motor to a certain position and hold it there.
Here's a [guide](https://www.ctrlaltftc.com/pidf_controllers/integrating_a_custom_PIDF_controller) on not only how to implement one but the logic.

And of course, if you don't wanna do that or code your own, there are libraries like [FTCLib](https://ftclib.org/) and [Homeostasis](https://www.ctrlaltftc.com/homeostasis-by-thermal-equilibrium/what-is-homeostasis) that have PID(F) controllers that are just plug-n-play (with tuning of course).

If you're using Roadrunner, you can use this [recipe](https://cookbook.dairy.foundation/roadrunner_056/how_to_integrate_a_PIDF_controller_with_roadrunner/how_to_integrate_a_PIDF_controller_with_roadrunner.html#pidf-controller-and-gains)

Happy coding! 🎉

*Last Updated: 2024-11-02*