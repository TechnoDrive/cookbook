package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MeasuringLoopTimes extends OpMode {
	private ElapsedTime elapsedtime;

	@Override
	public void init() {
		elapsedtime = new ElapsedTime();
		elapsedtime.reset();
	}

	@Override
	public void loop() {

		telemetry.addData("Loop Times", elapsedtime.milliseconds());
		elapsedtime.reset();
	}
}

