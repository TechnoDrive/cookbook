public class RoadRunnerPIDF extends LinearOpMode {
    // the capitalization and snake_case is just convention because the values of an enum are constants
    public enum STATES {
        INIT,
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
    private PIDFController PIDF;

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
            drive.update();
            double power = PIDF.calculate(linearSlides.getCurrentPosition(), targetPosition)
            linearSlides.setPower(power);
        }
    }
}