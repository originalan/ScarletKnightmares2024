package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.settings.UseTelemetry;
import org.firstinspires.ftc.teamcode.util.BulkReading;
import org.firstinspires.ftc.teamcode.util.LinkagePIDController;
import org.firstinspires.ftc.teamcode.util.MotionProfile;
import org.firstinspires.ftc.teamcode.util.MotionProfileParameters;

@Config
public class LinearSlide extends Subsystem {

    private HardwareMap hwMap;
    private Telemetry telemetry;
    private JVBoysSoccerRobot robot;
    private MotionProfile mp;
    private LinkagePIDController pid;

    public static double p = 0, i = 0, d = 0;

    private double previousPower = 0;
    public double referencePos = 0;
    public static double MAX_POWER = 0.5;

    private double previousCurrentPos = 10000;
    private double previousRefPos = 10000;

    public enum SlideState {
        MOTION_PROFILE,
        BASIC_PID,
        AT_REST
    }
    public SlideState slideState = SlideState.AT_REST;

    public LinearSlide(HardwareMap hwMap, Telemetry telemetry, JVBoysSoccerRobot robot) {
        this.hwMap = hwMap;
        this.telemetry = telemetry;
        this.robot = robot;
        this.mp = new MotionProfile(telemetry);
        pid = new LinkagePIDController();
    }

    @Override
    public void addTelemetry() {
        if (UseTelemetry.SLIDE_TELEMETRY) {
            telemetry.addLine("SLIDE TELEMETRY: ON");
//            telemetry.addData("    Arm Power", robot.motorArmL.getPower());
//            telemetry.addData("    Arm Encoder Position (R)", BulkReading.pMotorLinkage);
//            telemetry.addData("    Pivot Servo Position", robot.servoPivotR.getPosition());
        }else {
            telemetry.addLine("SLIDE TELEMETRY: OFF");
        }
    }

    @Override
    public void update() {
        switch (slideState) {
            case MOTION_PROFILE:
                mp.updateState();
                double refPos = mp.getInstantPosition();

                if ( !(previousCurrentPos == BulkReading.pMotorLinkage && previousRefPos == refPos) ) {
                    setSlidePower(pid.calculatePID(referencePos, BulkReading.pMotorLinkage));
                }
                previousCurrentPos = BulkReading.pMotorLinkage;
                previousRefPos = refPos;
                break;
            case BASIC_PID:
                double pow = pid.calculatePID(referencePos, BulkReading.pMotorLinkage);
                setSlidePower(pow);
                break;
            case AT_REST:
                setSlidePower(0);
                break;
        }
    }

    @Override
    public void stop() {

    }

    public void setSlidePower(double power) {
        if (power > MAX_POWER) {
            power = MAX_POWER;
        }
        if (power < -MAX_POWER) {
            power = -MAX_POWER;
        }
        if (power != previousPower) {
            robot.motorSlide.setPower(power);
        }
        previousPower = power;
    }

    public void setMotionProfile(int targetPosition, int acl, int vel, int dcl) {
        referencePos = targetPosition; // used for manual control later in teleop
        mp.setProfile(new MotionProfileParameters(BulkReading.pMotorLinkage, targetPosition, acl, vel, dcl));
        slideState = SlideState.MOTION_PROFILE;
    }
}
