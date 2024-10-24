package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

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
    public Arm armSubsystem;
    public Rigging riggingSubsystem;
    public Claw clawSubsystem;

    // Hardware
    public DcMotorEx SwerveMotorLeft, SwerveMotorRight;
    public CRServo SwerveServoLeft, SwerveServoRight;

    public DcMotorEx motorFL, motorFR, motorBL, motorBR; // mecanum motors when swerve doesn't work
    public DcMotorEx motorArmL, motorArmR;
    public Servo servoPivotL, servoPivotR;
    public Servo servoClaw;

    public Servo servoRigL, servoRigR;
    public DcMotorEx motorRigL, motorRigR;

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
        clawSubsystem = new Claw(hwMap, telemetry, this);
        armSubsystem = new Arm(hwMap, telemetry, this);
        riggingSubsystem = new Rigging(hwMap, telemetry, this);
//        launcherSubsystem = new AirplaneLauncher(hwMap, telemetry, this);

        subsystems = Arrays.asList(drivetrainSubsystem, armSubsystem, riggingSubsystem, clawSubsystem
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
//        SwerveServoRight = hwMap.get(CRServo.class, "");
//        SwerveServoLeft = hwMap.get(CRServo.class, "");

        initDrivetrainHardware();
        initArmHardware();
        initRiggingHardware();
        initClawHardware();
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

    public void initArmHardware() {
        motorArmL = hwMap.get(DcMotorEx.class, RobotSettings.ARM_LMOTOR_NAME);
        motorArmR = hwMap.get(DcMotorEx.class, RobotSettings.ARM_RMOTOR_NAME);

        motorArmL.setDirection(RobotSettings.ARM_LMOTOR_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        motorArmR.setDirection(RobotSettings.ARM_RMOTOR_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        motorArmL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArmR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void initRiggingHardware() {
        motorRigL = hwMap.get(DcMotorEx.class, RobotSettings.RIGGING_LMOTOR_NAME);
        motorRigR = hwMap.get(DcMotorEx.class, RobotSettings.RIGGING_RMOTOR_NAME);
        motorRigL.setDirection(RobotSettings.RIGGING_LMOTOR_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        motorRigR.setDirection(RobotSettings.RIGGING_RMOTOR_REVERSED ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        servoRigL = hwMap.servo.get(RobotSettings.RIGGING_LSERVO_NAME);
        servoRigR = hwMap.servo.get(RobotSettings.RIGGING_RSERVO_NAME);
        servoRigL.setDirection(RobotSettings.RIGGING_LSERVO_REVERSED ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        servoRigR.setDirection(RobotSettings.RIGGING_RSERVO_REVERSED ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
    }

    public void initClawHardware() {
        servoPivotL = hwMap.servo.get(RobotSettings.ARM_LPIVOT_NAME);
        servoPivotR = hwMap.servo.get(RobotSettings.ARM_RPIVOT_NAME);
        servoPivotL.setDirection(RobotSettings.ARM_LPIVOT_REVERSED ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        servoPivotR.setDirection(RobotSettings.ARM_RPIVOT_REVERSED ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);

        servoClaw = hwMap.servo.get(RobotSettings.CLAW_SERVO_NAME);
        servoClaw.setDirection(RobotSettings.CLAW_SERVO_REVERSED ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
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