package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;

public class CachingOptimizedOpMode extends OpMode {

	private CachingDcMotorEx exampleMotor;
	private List<LynxModule> allHubs;
	private ElapsedTime elapsedtime;

	@Override
	public void init() {

		elapsedtime = new ElapsedTime();

		// this just sets the bulk reading mode for each hub
		allHubs = hardwareMap.getAll(LynxModule.class);
		for (LynxModule hub : allHubs) {
			hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
		}

		exampleMotor = new CachingDcMotorEx(hardwareMap.get(DcMotorEx.class, "example motor"));
		elapsedtime.reset();
	}

	@Override
	public void loop() {
		// clears the cache on each hub
		for (LynxModule hub : allHubs) {
			hub.clearBulkCache();
		}

		// after the first time, it won't actually send new commands
		exampleMotor.setPower(1);

		telemetry.addData("Motor Position", exampleMotor.getCurrentPosition());
		telemetry.addData("Loop Times", elapsedtime.milliseconds());
		elapsedtime.reset();
	}
}

