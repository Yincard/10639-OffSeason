package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Helpers.Helpers;
import org.firstinspires.ftc.teamcode.Helpers.Controller;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Scoring.Box;
import org.firstinspires.ftc.teamcode.Subsystems.Scoring.Deposit;
import org.firstinspires.ftc.teamcode.Subsystems.Scoring.Intake;

@TeleOp(name = "👑 OffSeason TeleOp ")
public class Main extends LinearOpMode {

    public MecanumDrive driveTrain;
    public Deposit depositSystem;
    public Intake intakeSystem;
    public Box pixelDetector;
    public Controller Driver;
    public Controller Operator;
    public ElapsedTime loopTime;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        driveTrain = new MecanumDrive(hardwareMap, Helpers.defaultTelePose);
        depositSystem = new Deposit(hardwareMap);
        intakeSystem = new Intake(hardwareMap);
        pixelDetector = new Box(hardwareMap);
        Driver = new Controller(gamepad1);
        Operator = new Controller(gamepad2);
        loopTime = new ElapsedTime();

        initializeSubsystems();
        waitForStart();

        if (isStopRequested()) return;
        while (opModeIsActive() && !isStopRequested()) {
            loopSubsystems();
        }
    }

    private void initializeSubsystems() {

        depositSystem.init();
        intakeSystem.init();
        pixelDetector.init();

        telemetry.addLine("--- Scoring Conditions Initialized ---");
        telemetry.addLine("--- Subsystems Initialized ---");
        telemetry.update();
    }

    private void loopSubsystems() {
        loopTime.reset();

        Operator.readButtons();
        Driver.readButtons();

        driveTrain.loop(Driver, telemetry);
        depositSystem.loop(Driver, Operator, telemetry);
        intakeSystem.loop(Operator, telemetry);
        pixelDetector.loop(telemetry);


        double loopTimeMs = loopTime.milliseconds();
        telemetry.addLine("--- Loop Times ---");
        telemetry.addData("loopTimeMs", loopTimeMs);
        telemetry.addData("loopTimeHz", 1000.0 / loopTimeMs);
        telemetry.update();
    }
}
