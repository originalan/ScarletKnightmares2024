package org.firstinspires.ftc.teamcode.opmodes.auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.JVBoysSoccerRobot;

public abstract class AutoBase extends LinearOpMode {

    protected ElapsedTime runtime = new ElapsedTime();
    protected Gamepad currentGamepad = new Gamepad();
    protected Gamepad previousGamepad = new Gamepad();

    protected final Pose2d redCloseStart = new Pose2d(12, -54.3, Math.toRadians(90));
    protected final Pose2d redFarStart = new Pose2d(-36, -54.3, Math.toRadians(90));
    protected final Pose2d blueCloseStart = new Pose2d(12, 54.3, Math.toRadians(270));
    protected final Pose2d blueFarStart = new Pose2d(-36, 54.3, Math.toRadians(270));

    protected JVBoysSoccerRobot robot;
    protected ArmLift armLift;
    protected ClawSystem clawSystem;

    protected boolean isBlue = false;

    protected TrajectoryActionBuilder wait2;

    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        robot = new JVBoysSoccerRobot(hardwareMap, telemetry, true);

        armLift = new ArmLift();
        clawSystem = new ClawSystem();

        PoseStorage.ORIGINAL_INIT_YAW = robot.imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Elapsed time", runtime.toString());
    }

    public class ArmLift {
        private boolean stopUpdate = false;
        public class UpdateArmSubsystem implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    initialized = true;
                    stopUpdate = false;
                }

                if (stopUpdate) {
                    return false;
                }else {
                    robot.armSubsystem.update();
                    return true;
                }
            }
        }
        public Action updateArmSubsystem() {
            return new UpdateArmSubsystem();
        }

        public class StopUpdate implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                stopUpdate = true;
                return false;
            }
        }
        public Action stopUpdate() {
            return new StopUpdate();
        }

        public class DepositSpecimen implements Action {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    robot.armSubsystem.setDepositSpecimen();
                    robot.armSubsystem.armState = Arm.ArmState.MOTION_PROFILE;
                    initialized = true;
                }
                if (!robot.armSubsystem.getMP().isBusy()) {
                    return false;
                }
                return true;
            }
        }
        public Action depositSpecimen() {
            return new DepositSpecimen();
        }

        public class PivotDown implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                robot.armSubsystem.setPivot(Arm.pivotPresetDepositSpecimen - Arm.pivotDownIncrement);
                return false;
            }
        }
        public Action pivotDown() {
            return new PivotDown();
        }

        public class IntakeSpecimen implements Action {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    robot.armSubsystem.setIntakeSpecimen();
                    robot.armSubsystem.armState = Arm.ArmState.MOTION_PROFILE;
                    initialized = true;
                }
                if (!robot.armSubsystem.getMP().isBusy()) {
                    return false;
                }
                return true;
            }
        }
        public Action intakeSpecimen() {
            return new IntakeSpecimen();
        }

        public class DepositSample implements Action {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    robot.armSubsystem.setDepositSample();
                    robot.armSubsystem.armState = Arm.ArmState.MOTION_PROFILE;
                    initialized = true;
                }
                if (!robot.armSubsystem.getMP().isBusy()) {
                    return false;
                }
                return true;
            }
        }
        public Action depositSample() {
            return new DepositSample();
        }

        public class IntakeSample implements Action {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    robot.armSubsystem.setIntakeSample();
                    robot.armSubsystem.armState = Arm.ArmState.MOTION_PROFILE;
                    initialized = true;
                }
                if (!robot.armSubsystem.getMP().isBusy()) {
                    return false;
                }
                return true;
            }
        }
        public Action intakeSample() {
            return new IntakeSample();
        }

        public class RestArm implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    initialized = true;
                    robot.armSubsystem.setRest();
                    robot.armSubsystem.armState = Arm.ArmState.MOTION_PROFILE;
                }
                if (!robot.armSubsystem.getMP().isBusy()) {
                    robot.armSubsystem.armState = Arm.ArmState.AT_REST;
                    robot.armSubsystem.setArmPower(0);
                    robot.motorArmR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.motorArmR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    return false;
                }
                return true;
            }
        }
        public Action restArm() {
            return new RestArm();
        }
    }

    public class ClawSystem {
        public class CloseClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                robot.clawSubsystem.closeClaw();
                return false;
            }
        }
        public Action closeClaw() {
            return new CloseClaw();
        }

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                robot.clawSubsystem.openClaw();
                return false;
            }
        }
        public Action openClaw() {
            return new OpenClaw();
        }
    }

    @Override
    public abstract void runOpMode() throws InterruptedException;

}