package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.JVBoysSoccerRobot;

@TeleOp(name = "Mecanum Drivetrain Test", group = "Testing")
public class MecanumDrivetrainTest extends LinearOpMode {

    private HardwareMap hwMap;
    private Telemetry telemetry;
    private JVBoysSoccerRobot robot;
    private ElapsedTime runtime = new ElapsedTime();

    private double previousX = 5, previousY = 5, previousR = 5;
    private Gamepad currentGamepad1;
    private Gamepad previousGamepad1;
    private Gamepad currentGamepad2;
    private Gamepad previousGamepad2;

    @Override
    public void runOpMode() throws InterruptedException {
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
        currentGamepad2 = new Gamepad();
        previousGamepad2 = new Gamepad();

        hwMap = hardwareMap;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new JVBoysSoccerRobot(hwMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Elapsed time", runtime.toString());

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                previousGamepad1.copy(currentGamepad1);
                currentGamepad1.copy(gamepad1);
                previousGamepad2.copy(currentGamepad2);
                currentGamepad2.copy(gamepad2);

                telemetry.addLine("CONTROLS: ");
                telemetry.addLine("    DPAD UP: Reset Init Yaw ");
                telemetry.addLine("    B: Swap from field centric to robot centric ");
                telemetry.addLine("    A: Toggle Orthogonal Mode ");
                telemetry.addLine("    TRIGGERS: Slowdown the robot by factor of 3 ");

                // Failsafe field-oriented view
                if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                    robot.drivetrainSubsystem.resetInitYaw();
                }

                drivetrainControls();

                robot.addTelemetry();
                telemetry.update();
//                robot.BR.readAll();
            }
        }
    }

    // MECANUM DRIVE
    public void drivetrainControls() {
        double x = gamepad1.left_stick_x * 1.05;
        double y = gamepad1.left_stick_y * -1;
        double r = gamepad1.right_stick_x;

        if (currentGamepad1.b && !previousGamepad1.b) {
            robot.drivetrainSubsystem.isFieldCentric = !robot.drivetrainSubsystem.isFieldCentric;
        }

        if (currentGamepad1.a && !previousGamepad1.a) {
            robot.drivetrainSubsystem.orthogonalMode = !robot.drivetrainSubsystem.orthogonalMode;
        }

        if (currentGamepad1.right_trigger > 0.01 || currentGamepad1.left_trigger > 0.01) {
            x /= 3;
            y /= 3;
            r /= 3;
        }

        // attempting to save motor calls == faster frequency of command calls
        if ( !(previousX == x && previousY == y && previousR == r) ) {
            robot.drivetrainSubsystem.moveXYR(x, y, r);
        }

        previousX = x;
        previousY = y;
        previousR = r;
    }
}
