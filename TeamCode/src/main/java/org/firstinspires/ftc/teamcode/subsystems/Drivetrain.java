package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.opmodes.auto.PoseStorage;
import org.firstinspires.ftc.teamcode.settings.RobotSettings;
import org.firstinspires.ftc.teamcode.settings.UseTelemetry;

public class Drivetrain extends Subsystem {

    private HardwareMap hwMap;
    private Telemetry telemetry;
    private JVBoysSoccerRobot robot;

    private  double  power, theta, sin, cos, max;
    public Orientation lastAngle;
    public double initYaw;
    private double powerFL, powerFR, powerBL, powerBR;

    private double prevPowerFL, prevPowerFR, prevPowerBL, prevPowerBR;

    public boolean isFieldCentric = true;
    public boolean orthogonalMode = false;

    public static double MAX_SPEED = 1.0;

    public Drivetrain(HardwareMap hwMap, Telemetry telemetry, JVBoysSoccerRobot robot) {
        this.hwMap = hwMap;
        this.telemetry = telemetry;
        this.robot = robot;

        prevPowerFL = -5;
        prevPowerBL = -5;
        prevPowerBR = -5;
        prevPowerFR = -5;

//        if (!PoseStorage.AUTO_SHIFTED) {
//        }else {
//            initYaw = PoseStorage.ORIGINAL_INIT_YAW + PoseStorage.AUTO_SHIFT_YAW;
//        }
//        lastAngle = robot.imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//        initYaw = PoseStorage.originalInitYaw + PoseStorage.AUTO_SHIFT_DEGREES; // b/c auto started with back facing front
    }

    /**
     * As a last measure, if switching between auto and teleop the robot is not perfectly straight
     * You can rotate the robot in teleop, and then set the initial yaw as its current angle
     */
    public void resetInitYaw() {
        lastAngle = robot.imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        initYaw = lastAngle.firstAngle;
    }

    /**
     * Moves robot based on joystick inputs and if it is field-centric or robot-centric drive
     * @param x is the strafing (left-right) motion of the robot ; left joystick left-right
     * @param y is the vertical (forward-backward) motion of the robot ; left joystick up-down
     * @param r is the rotation of the robot CW or CCW ; right joystick left-right
     */
    public void moveXYR(double x, double y, double r) {
        lastAngle = robot.imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        power = 0;
        theta = 0;
        sin = 0;
        cos = 0;
        max = 0;

        if (isFieldCentric) {
            lastAngle = robot.imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double zeroedYaw = (-1 * initYaw) + lastAngle.firstAngle;
            double thetaGamepad = Math.atan2(y, x) * 180 / Math.PI; // Angle of gamepad in degrees, -180 to 180 degrees
            theta = (360 - zeroedYaw) + thetaGamepad; // Real theta that robot must travel in degrees
            theta = theta * Math.PI / 180; // convert to radians
            power = Math.hypot(x, y);
        }else {
            power = Math.hypot(x, y);
            theta = Math.atan2(y, x); // -pi to pi
        }

        if (orthogonalMode) {
            if (isFieldCentric) {
                double newTheta = Math.toDegrees(theta);
                newTheta %= 360;
                double ref = newTheta % 90;
                if (ref > 45) {
                    theta = Math.toRadians( newTheta + (90 - ref) );
                }else {
                    theta = Math.toRadians( newTheta - ref );
                }
            }else {
                double newTheta = Math.toDegrees(theta);
                newTheta %= 360;
                double ref = newTheta % 90;
                double zeroYaw = (-1 * initYaw) + lastAngle.firstAngle;
                double thetaGamePad = Math.atan2(y, x) * 180 / Math.PI; // in degrees
                newTheta = (360 - zeroYaw) + thetaGamePad;
                if (ref > 45) {
                    theta = Math.toRadians( newTheta + (90 - ref) );
                }else {
                    theta = Math.toRadians( newTheta - ref );
                }
            }
        }

        sin = Math.sin(theta - (Math.PI / 4));
        cos = Math.cos(theta - (Math.PI / 4));
        max = Math.max(Math.abs(sin), Math.abs(cos));

        powerFL= power * cos / max + r;
        powerFR = power * sin / max - r;
        powerBL = power * sin / max + r;
        powerBR = power * cos / max - r;

        if (Math.abs(power) + Math.abs(r) > 1) {
            powerFL /= power + Math.abs(r);
            powerFR /= power + Math.abs(r);
            powerBL /= power + Math.abs(r);
            powerBR /= power + Math.abs(r);
        }

        powerFL *= MAX_SPEED;
        powerBR *= MAX_SPEED;
        powerFR *= MAX_SPEED;
        powerBL *= MAX_SPEED;

        if (prevPowerBR != powerBR) {
            robot.motorBR.setPower(powerBR);
        }
        if (prevPowerBL != powerBL) {
            robot.motorBL.setPower(powerBL);
        }
        if (prevPowerFL != powerFL) {
            robot.motorFL.setPower(powerFL);
        }
        if (prevPowerFR != powerFR) {
            robot.motorFR.setPower(powerFR);
        }
        prevPowerBR = powerBR;
        prevPowerBL = powerBL;
        prevPowerFL = powerFL;
        prevPowerFR = powerFR;
    }

    @Override
    public void addTelemetry() {
        if (UseTelemetry.DRIVETRAIN_TELEMETRY) {
            telemetry.addLine("Drivetrain Telemetry: ON");

            telemetry.addData("   Front Left/Right Calculated Powers", "%4.2f, %4.2f", powerFL, powerFR);
            telemetry.addData("   Back Left/Right Calculated Powers", "%4.2f, %4.2f", powerBL, powerBR);
        }else {
            telemetry.addLine("Drivetrain Telemetry: OFF");
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void stop() {

    }
}
