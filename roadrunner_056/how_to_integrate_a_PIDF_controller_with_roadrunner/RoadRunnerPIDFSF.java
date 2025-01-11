public class RoadRunnerPIDFSF extends LinearOpMode {

    public enum STATES {
        INIT,
        DRIVE_FORWARD,
        STRAFE_LEFT_AND_LIFT_SLIDES,
        DRIVE_BACKWARD,
        STOP;
    }

    SampleMecanumDrive drive;
    DcMotorEx linearSlides;
    PIDFController PIDF;
    int targetPosition = 0;

    public void runOpMode() {

        // all the same initialization and trajectory building as above

        StateMachine machine = new StateMachine()
            .state(STATES.INIT) // creates a new state
            .transition(() -> isStarted()) // condition to transition from this state to the next one

            .state(STATES.DRIVE_FORWARD) // register a new state
            .onEnter(() -> drive.followTrajectorySequenceAsync(forward)) // code to happen one time when entering this state
            .transition(() -> !drive.isBusy())

            .state(STATES.STRAFE_LEFT_AND_LIFT_SLIDES)
            .onEnter(() -> drive.followTrajectorySequenceAsync(strafeLeft))
            .transition(() -> (!drive.isBusy() && linearSlides.atTarget()))

            .state(STATES.DRIVE_BACKWARD)
            .onEnter(() -> drive.followTrajectorySequenceAsync(backward))
            .transition(() -> !drive.isBusy())

            .state(STATES.STOP)

            .build();
        // building this StateMachine doesn't actually do anything. We still need to run it

        waitForStart();

        machine.start(); // this starts the state machine, putting us into the first state

        while(opModeIsActive()) {
            machine.update();
            drive.update();
            double power = PIDF(linearSlides.getCurrentPosition(), targetPosition);
            linearSlides.setPower(power);
        }
    }
}