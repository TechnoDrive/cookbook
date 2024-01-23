# How to Integrate a PIDF Controller With Roadrunner

*This recipe will assume you have a functioning PIDF controller that has already been tuned. If you do not, refer to [integrating a custom PIDF controller](./how_to_integrate_a_PIDF_controller_with_roadrunner.md).*

## Ingredients

1. A PID or PIDF controller class
2. Tuned PID(F) gains
3. An OpMode or LinearOpMode
4. A Finite State Machine

## The Recipe

### PID(F) Controller and gains

This recipes assumes you have 1) a PID(F) class that works and 2) tuned PID(F) gains. This recipe will not go over how to implement these and you should reference [integrating a custom PIDF controller](./how_to_integrate_a_PIDF_controller_with_roadrunner.md).

### Finite State Machines

For a more indepth understanding of what finite state machines are, visit [gm0](https://gm0.org/en/latest/docs/software/concepts/finite-state-machines.html?highlight=finite). In short, a finite state machine is a code structure which allows code to run linearly while also having quasi-parallel actions running. The example we will be working with today is driving with Roadrunner while controlling linear slides.

You can work with Finite State Machines in either a LinearOpMode or an OpMode, either works. For the purpose of this recipe, we will be using a LinearOpMode. To use an OpMode you can move everything before the while loop can go in init and everything in the while loop into the `loop()` function.

We will first have a full example and then break it down piece by piece.

```
// the capitalization and snake_case is just convention because the values of an enum are constants
public enum STATES {
    INIT,
    START,
    DRIVE_FORWARD,
    STRAFE_LEFT_AND_LIFT_SLIDES,
    DRIVE_BACKWARD,
    STOP;
}

private STATES previousState = STATES.INIT;
private STATES currentState = STATES.INIT;
private int targetPosition = 0;

private TrajectorySequence forward;
private TrajectorySequence strafeLeft;
private TrajectorySequence backward;

private SampleMecanumDrive drive;
private DcMotorEx linearSlides;
privaet PIDFController PIDF;

public void runOpMode() {
    /* for the purpose of this recipe, I will be using linear slides with PIDF control to demonstrate.
    The linear slides will simply be called linearSlides.
    */

    // linear slide initialization code
    // pidf initialization code

    drive = new SampleMecanumDrive(hardwareMap);
    drive.setPoseEstimate(new Pose2d());

    forward = drive.TrajectorySequenceBuilder(new Pose2d())
        .forward(10)
        .build();

    strafeLeft = drive.TrajectorySequenceBuilder(forward.end())
        .addDisplacementMarker(() -> {
            targetPosition = 800;
        })
        .strafeLeft(10)
        .build();

    backward = drive.TrajectorySequenceBuilder(strafeLeft.end())
        .back(10)
        .build();

    waitForStart();

    currentState = STATES.DRIVE_FORWARD;

    while(opModeIsActive) {
        switch (currentState) {
            case (INIT):
                break;
            case (DRIVE_FORWARD):
                if (previousState != currentState) {
                    // everything in here will run once when the state switches

                    drive.followTrajectoryAsync(forward)
                    previousState = STATES.DRIVE_FORWARD;
                } else if (!drive.isBusy()) {
                    currentState = STATES.STRAFE_LEFT_AND_LIFT_SLIDES;
                }
                break;
            case (STRAFE_LEFT_AND_LIFT_SLIDES):
                if (previousState != currentState) {
                    // inside this trajectory sequence the targetPosition is set and the slides will start updating
                    drive.followTrajectorySequenceAsync(strafeLeft);
                } else if (!drive.isBusy() && linearSlides.atTarget()) {
                    currentState = STATES.DRIVE_BACKWARD;
                }
                break;
            case (DRIVE_BACKWARD):
                if (previousState != currentState) {
                    drive.followTrajectorySequenceAsync(backward);
                    previousState = STATES.DRIVE_BACKWARD;
                } else if (!drive.isBusy()) {
                    currentState = STATES.STOP;
                }
                break;
            case (STOP):
                break;
        }

        // outside of the switch we update our slides, that way they are always receiving new information
        double power = PIDF.calculate(linearSlides.getCurrentPosition(), targetPosition)
        linearSlides.setPower(power);
    }
}
```

Okay, let's break this down piece by piece. First, what is an "enum" and why do we use them? Enums are a way to define a set of named constant values. They provide a convenient and readable way to work with predefined, named values in your code. Here, we used an enum to describe the various states the robot could be in.

```
public enum STATES {
    INIT,
    START,
    DRIVE_FORWARD,
    STRAFE_LEFT_AND_LIFT_SLIDES,
    DRIVE_BACKWARD,
    STOP;
}
```

By using names with meaning like these, it is much clearer when writing and reading the code what each block does. It also means we don't have to remember that state 0 means START and state 1 means DRIVE_FORWARD, etc....

Next we initialize all our stuff and build our trajectories. The important one to note is creating `strafeLeft`, which includes slide movement.

```
strafeLeft = drive.TrajectorySequenceBuilder(forward.end())
        .addDisplacementMarker(() -> {
            targetPosition = 800;
        })
        .strafeLeft(10)
        .build();
```

We used a displacement marker, which says "right here do this thing." The `() -> {}` is just fancy notation for a one time use function. The empty paranthesis says "this doesn't require any arguments to be passed into the function" and the curly braces denote the start of the function. In this case, we're just setting a variable `targetPosition`, but this marker could include setting servo position, reading sensors, or anything else really.

```
case (DRIVE_FORWARD):
    if (previousState != currentState) {
        // everything in here will run once when the state switches

        drive.followTrajectoryAsync(forward)
        previousState = STATES.DRIVE_FORWARD;
    } else if (!drive.isBusy()) {
        currentState = STATES.STRAFE_LEFT_AND_LIFT_SLIDES;
    }
    break;
```

So now we're getting to the Finite State Machine (FSM) part. The first part of this case, which you'll see in each part, is checking whether previous and current states are equal. This allows us to run code the first time it enters this state, like starting a trajectory (in this example). Then inside that same block, we also need to set the previous state to the one we're in.

The else if just checks if we're done with this state so it knows when to move on. This is the transition trigger, in this case, finishing the roadrunner trajectory.

The next case is the more interesting one.

```
case (STRAFE_LEFT_AND_LIFT_SLIDES):
    if (previousState != currentState) {
        // inside this trajectory sequence the targetPosition is set and the slides will start updating
        drive.followTrajectorySequenceAsync(strafeLeft);
    } else if (!drive.isBusy() && linearSlides.atTarget()) {
        currentState = STATES.DRIVE_BACKWARD;
    }
    break;
```

Here we have the same structure. However, this time our transition trigger is finishing the roadrunner trajectory and the linear slides reaching their target. Because this runs in a loop, once the displacement marker in the trajectory sequence triggers and changes the targetPosition, the PID update that runs at the end of every loop will start setting the power of the linear slides to reach that target.

Whew! You should now be able to integrate a PID(F) controller with roadrunner trajectories.

This example was meant to be general and explain the structure and concepts needed to make PID(F) controllers work with roadrunner. It will almost certainly require changes to make it work exactly how you wish, so don't worry if your code doesn't look exactly like this example!

### State Factory


*Last Updated: 2024-01-23*