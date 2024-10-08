package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.UseTelemetry;
import org.firstinspires.ftc.teamcode.util.BulkReading;
import org.firstinspires.ftc.teamcode.settings.RobotSettings;

import java.util.Arrays;
import java.util.List;

/**
 * JVBoysSoccerRobot is the robot base superclass.
 * All hardware and subsystems are initialized here.
 * GO JV BOYS SOCCER TEAM!
 */
public class JVBoysSoccerRobot {

    private HardwareMap hwMap;
    private Telemetry telemetry;
    public BulkReading BR;
    private List<LynxModule> allHubs;
    private List<Subsystem> subsystems;
    public IMU imu;

    // Subsystems
    public Drivetrain drivetrainSubsystem;

    // Hardware
    public DcMotorEx SwerveMotorLeft, SwerveMotorRight;
    public CRServo SwerveServoLeft, SwerveServoRight;

    public DcMotorEx motorFL, motorFR, motorBL, motorBR; // mecanum motors when swerve doesn't work

    public JVBoysSoccerRobot(HardwareMap hwMap, Telemetry telemetry) {
        this.hwMap = hwMap;
        this.telemetry = telemetry;

        // Configuring Hubs to auto mode for bulk reads
        allHubs = hwMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
//            hub.setBulkCachingMode(LynxModule.BulkCachingMode.OFF);
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        initIMU();
        initHardware();
        drivetrainSubsystem = new Drivetrain(hwMap, telemetry, this);
//        clawSubsystem = new Claw(hwMap, telemetry, this);
//        armSubsystem = new Arm(hwMap, telemetry, this);
//        riggingSubsystem = new Rigging(hwMap, telemetry, this);
//        launcherSubsystem = new AirplaneLauncher(hwMap, telemetry, this);

        subsystems = Arrays.asList(drivetrainSubsystem
//                , clawSubsystem, armSubsystem, riggingSubsystem, launcherSubsystem
        );
        BR = new BulkReading(this);
    }

    public void initIMU() {
        imu = hwMap.get(IMU.class, "imu");
        IMU.Parameters parameters1 = new IMU.Parameters(new RevHubOrientationOnRobot(
                RobotSettings.LOGO_FACING_DIR, RobotSettings.USB_FACING_DIR));
        imu.initialize(parameters1);
    }

    public void initHardware() {
        SwerveServoRight = hwMap.get(CRServo.class, "");
        SwerveServoLeft = hwMap.get(CRServo.class, "");

        initDrivetrainHardware();
    }

    public void initDrivetrainHardware() {
        motorFL = hwMap.get(DcMotorEx.class, RobotSettings.FL_NAME);
        motorBL = hwMap.get(DcMotorEx.class, RobotSettings.BL_NAME);
        motorFR = hwMap.get(DcMotorEx.class, RobotSettings.FR_NAME);
        motorBR = hwMap.get(DcMotorEx.class, RobotSettings.BR_NAME);

        motorFL.setDirection(RobotSettings.FL_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        motorFR.setDirection(RobotSettings.FR_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        motorBR.setDirection(RobotSettings.BR_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        motorBL.setDirection(RobotSettings.BL_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void addTelemetry() {
        if (UseTelemetry.ROBOT_TELEMETRY) {
            for (Subsystem s : subsystems) {
                s.addTelemetry();
            }
        }
    }
    public void update() {
        for (Subsystem s : subsystems) {
            s.update();
        }
    }
}
