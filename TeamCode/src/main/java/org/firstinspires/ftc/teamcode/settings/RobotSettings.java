package org.firstinspires.ftc.teamcode.settings;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class RobotSettings {

    public static RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING_DIR = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
    public static RevHubOrientationOnRobot.UsbFacingDirection USB_FACING_DIR = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

    public static ElapsedTime SUPER_TIME = new ElapsedTime();

    // DRIVETRAIN

    public static final String FL_NAME = "FL";
    public static final String FR_NAME = "FR";
    public static final String BL_NAME = "BL";
    public static final String BR_NAME = "BR";

    public static final boolean FL_REVERSED = true;
    public static final boolean FR_REVERSED = false;
    public static final boolean BL_REVERSED = true;
    public static final boolean BR_REVERSED = false;

    public static final String ARM_LMOTOR_NAME = "ArmLeft";
    public static final String ARM_RMOTOR_NAME = "ArmRight";
    public static final boolean ARM_LMOTOR_REVERSED = false;
    public static final boolean ARM_RMOTOR_REVERSED = true;

    public static final String ARM_LPIVOT_NAME = "ArmPivotLeft";
    public static final String ARM_RPIVOT_NAME = "ArmPivotRight";
    public static final boolean ARM_LPIVOT_REVERSED = true;
    public static final boolean ARM_RPIVOT_REVERSED = false;

    public static String CLAW_SERVO_NAME = "ClawServoL";
    public static boolean CLAW_SERVO_REVERSED = false;
    public static String CLAW_SERVO2_NAME = "ClawServoR";
    public static boolean CLAW_SERVO2_REVERSED = true;

    public static String CLAW_RWRIST_NAME = "ClawServoDiffyR";
    public static boolean CLAW_RWRIST_REVERSED = true;
    public static String CLAW_LWRIST_NAME = "ClawServoDiffyL";
    public static boolean CLAW_LWRIST_REVERSED = false;

    // ENCODER MAPPING
    public static final int TOTAL_ENCODER_TICKS = 8192;
    public static final double GEAR_RATIO = 1;
    public static double ENCODER_CONVERSION_CONSTANT = ((double)TOTAL_ENCODER_TICKS * GEAR_RATIO) / 360.0;

}
