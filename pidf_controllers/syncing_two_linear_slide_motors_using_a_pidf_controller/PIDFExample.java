public class PIDFExample extends LinearOpMode {

    // PID(F) declaration
    // kp = 0, ki = 0, kd = 0, kf = 0;

    private PIDFController examplePIDF = new PIDFController(0, 0, 0, 0);

    @Override
    public void runOpMode() {
        int targetPosition = 0;

        // motor setup
        DcMotorEx leftSlide = hardwareMap.get(DcMotorEx.class, "leftSlide");
        DcMotorEx rightSlide = hardwareMap.get(DcMotorEx.class, "rightSlide");

        waitForStart();

        while(opModeIsActive()) {
            /* 
            Calculates PID based only on one encoder.
            This can also be the average position of the two linear slides, but we haven't noticed much difference
            */
            double power = PIDFController.update(leftSlide.getCurrentPosition(), targetPosition);

            // see how both motors are getting the same power
            leftSlide.setPower(power);
            rightSlide.setPower(power);
        }
    }

}