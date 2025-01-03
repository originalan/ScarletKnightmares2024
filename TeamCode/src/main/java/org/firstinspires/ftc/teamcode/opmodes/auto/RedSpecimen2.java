package org.firstinspires.ftc.teamcode.opmodes.auto;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.Arrays;

@Config
@Autonomous (name="Red Specimen (Strafe)", group="Testing")
public class RedSpecimen2 extends AutoBase {

    private boolean choicePicked = false;
    private int pathNumber = 0;
    private double timeDelay = 0;
    private MecanumDrive drive;

    private Action moveToBar11, moveToBar21, moveToObservationZone1;
    private Action depositFirstSpecimen1, depositFirstSpecimen2, pickUpSecondSpecimen1, pickUpSecondSpecimen2, depositSecondSpecimen1, depositSecondSpecimen2, moveBackToObservationZone2;
    private Action moveToFirstSample, moveToSecondSample;
    private Action pickUpThirdSpecimen1, pickUpThirdSpecimen2;
    private Action depositThirdSpecimen1, depositThirdSpecimen2;
    private Action pickUpFourthSpecimen1, pickUpFourthSpecimen2;
    private Action depositFourthSpecimen1, depositFourthSpecimen2;

    private VelConstraint baseVelConstraint = new MinVelConstraint(Arrays.asList(
            new TranslationalVelConstraint(50.0),
            new AngularVelConstraint(Math.PI)
    ));
    private AccelConstraint baseAccelConstraint = new ProfileAccelConstraint(-30.0, 50.0);

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        PoseStorage.AUTO_SHIFT_YAW = 0;
        drive = new MecanumDrive(hardwareMap, specimenStart);

        while (!choicePicked) {
            previousGamepad.copy(currentGamepad);
            currentGamepad.copy(gamepad1);

            telemetry.addLine("PICK NUMBER OF SPECIMEN");
            telemetry.addLine("Press A to increase path counter");
            telemetry.addLine("Press B to decrease path counter");
            telemetry.addLine("Press DPAD UP and DPAD DOWN to add starting delay");
            telemetry.addLine("Press X when ready");
            telemetry.update();
            if (currentGamepad.a && !previousGamepad.a) {
                pathNumber++;
            }
            if (currentGamepad.b && !previousGamepad.b) {
                pathNumber--;
            }
            if (currentGamepad.x && !previousGamepad.x) {
                choicePicked = true;
            }
            if (currentGamepad.dpad_up && !previousGamepad.dpad_up) {
                timeDelay += 0.5;
            }
            if (currentGamepad.dpad_down && !previousGamepad.dpad_down) {
                timeDelay -= 0.5;
                if (timeDelay < 0) {
                    timeDelay = 0;
                }
            }
            telemetry.addData("NUMBER OF SPECIMEN CHOSEN: ", pathNumber);
            telemetry.addData("Time Delay: ", timeDelay);
            if (isStopRequested()) return;
        }

        switch (pathNumber) {
            case 1:
                oneSpecimenPaths();
                break;
            case 2:
                twoSpecimenPaths();
                break;
            case 3:
                break;
            case 4:
                break;
        }

        telemetry.addData("NUMBER OF SPECIMEN CHOSEN: ", pathNumber);
        telemetry.update();

        // actions that need to happen on init
        Actions.runBlocking(clawSystem.closeClaw());

        waitForStart();

        if (isStopRequested()) return;

        switch (pathNumber) {
            case 1:
                Actions.runBlocking(
                        new SequentialAction(
                                new SleepAction(timeDelay),
                                new ParallelAction(
                                        armLift.updateArmSubsystem(),
                                        new SequentialAction(
                                                moveToBar11,
                                                armLift.depositSpecimen(),
                                                new SleepAction(0.5),
                                                moveToBar21,
                                                armLift.depositSpecimenDown(),
                                                new SleepAction(0.3),
                                                clawSystem.openClaw(),
                                                new SleepAction(0.3),
                                                armLift.restArm(),
                                                clawSystem.closeClaw(),
                                                moveToObservationZone1,
                                                armLift.stopUpdate()
                                        )
                                )
                        )
                );
                break;
            case 2:
                Actions.runBlocking(
                        new SequentialAction(
                                new SleepAction(timeDelay),
                                new ParallelAction(
                                        armLift.updateArmSubsystem(),
                                        new SequentialAction(
                                                depositFirstSpecimen1,
                                                armLift.depositSpecimen(),
                                                new SleepAction(0.5),
                                                depositFirstSpecimen2,
                                                armLift.depositSpecimenDown(),
                                                new SleepAction(0.3),
                                                clawSystem.openClaw(),
                                                new SleepAction(0.3),
                                                pickUpSecondSpecimen1,
                                                armLift.intakeSpecimen(),
                                                new SleepAction(0.5),
                                                armLift.intakeSpecimenDown(),
                                                new SleepAction(0.25),
                                                clawSystem.closeClaw(),
                                                new SleepAction(0.25),
                                                armLift.depositSpecimen(),
                                                pickUpSecondSpecimen2,

                                                depositSecondSpecimen1,
                                                new SleepAction(0.5),
                                                depositSecondSpecimen2,
                                                armLift.depositSpecimenDown(),
                                                new SleepAction(0.3),
                                                clawSystem.openClaw(),
                                                new SleepAction(0.3),
                                                armLift.restArm(),
//                                                clawSystem.closeClaw(),
//                                                armLift.deExtendSlide(),
//                                                armLift.restArm(),
//                                                moveToFirstSample,
//                                                moveToSecondSample,
//
//                                                pickUpThirdSpecimen1,
////                                                armLift.intakeSpecimen(),
////                                                clawSystem.openClaw(),
//                                                new SleepAction(1),
//                                                pickUpThirdSpecimen2,
//                                                new SleepAction(0.5),
////                                                clawSystem.closeClaw(),
//                                                new SleepAction(0.5),
////                                                armLift.depositSpecimen(),
//                                                depositThirdSpecimen1,
////                                                armLift.extendSlide(),
//        //                                        armLift.pivotDown(),
//                                                new SleepAction(0.5),
////                                                clawSystem.openClaw(),
//                                                new SleepAction(0.5),
//                                                depositThirdSpecimen2,
////                                                clawSystem.closeClaw(),
////                                                armLift.deExtendSlide(),
//
//                                                pickUpFourthSpecimen1,
////                                                armLift.intakeSpecimen(),
////                                                clawSystem.openClaw(),
//                                                new SleepAction(1),
//                                                pickUpFourthSpecimen2,
//                                                new SleepAction(0.5),
////                                                clawSystem.closeClaw(),
//                                                new SleepAction(0.5),
////                                                armLift.depositSpecimen(),
//                                                depositFourthSpecimen1,
////                                                armLift.extendSlide(),
//        //                                        armLift.pivotDown(),
//                                                new SleepAction(0.5),
////                                                clawSystem.openClaw(),
//                                                new SleepAction(0.5),
//                                                depositFourthSpecimen2,
////                                                clawSystem.closeClaw(),
////                                                armLift.deExtendSlide(),
//
////                                                armLift.restArm(),
                                                moveBackToObservationZone2,
                                                armLift.stopUpdate()
                                        )
                                )
                        )
                );
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    public void oneSpecimenPaths() {
        TrajectoryActionBuilder moveToBar1B = drive.actionBuilder(specimenStart)
                .lineToY(-58);
        TrajectoryActionBuilder moveToBar2B = moveToBar1B.endTrajectory().fresh()
                .lineToY(-51);
        TrajectoryActionBuilder moveToObservationZoneB = moveToBar2B.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(46, -62),
                    baseVelConstraint,
                    baseAccelConstraint);

        moveToBar11 = moveToBar1B.build();
        moveToBar21 = moveToBar2B.build();
        moveToObservationZone1 = moveToObservationZoneB.build();
    }

    public void twoSpecimenPaths() {
        TrajectoryActionBuilder depositFirstSpecimen1B = drive.actionBuilder(specimenStart)
                .lineToY(-58);
        TrajectoryActionBuilder depositFirstSpecimen2B = depositFirstSpecimen1B.endTrajectory().fresh()
                .lineToY(-51);
        TrajectoryActionBuilder pickUpSecondSpecimen1B = depositFirstSpecimen2B.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(28, -65), Math.toRadians(180));
        TrajectoryActionBuilder pickUpSecondSpecimen2B = pickUpSecondSpecimen1B.endTrajectory().fresh()
                .strafeTo(new Vector2d(28, -65));
        TrajectoryActionBuilder depositSecondSpecimen1B = pickUpSecondSpecimen2B.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(2, -58), Math.toRadians(270));
        TrajectoryActionBuilder depositSecondSpecimen2B = depositSecondSpecimen1B.endTrajectory().fresh()
                .strafeTo(new Vector2d(2, -49));
        TrajectoryActionBuilder moveToFirstSampleB = depositSecondSpecimen2B.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(36, -48), Math.toRadians(0))
                .lineToY(-12)
                .strafeToConstantHeading(new Vector2d(45, -48));
        TrajectoryActionBuilder moveToSecondSampleB = moveToFirstSampleB.endTrajectory().fresh()
                .lineToY( -12)
                .strafeToConstantHeading(new Vector2d(54, -48));
        TrajectoryActionBuilder pickUpThirdSpecimen1B = moveToSecondSampleB.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(36, -52), Math.toRadians(270));
        TrajectoryActionBuilder pickUpThirdSpecimen2B = pickUpThirdSpecimen1B.endTrajectory().fresh()
                .strafeTo(new Vector2d(36, -55));
        TrajectoryActionBuilder depositThirdSpecimen1B = pickUpThirdSpecimen2B.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(10, -39), Math.toRadians(90));
        TrajectoryActionBuilder depositThirdSpecimen2B = depositThirdSpecimen1B.endTrajectory().fresh()
                .strafeTo(new Vector2d(10, -41));
        TrajectoryActionBuilder pickUpFourthSpecimen1B = depositThirdSpecimen2B.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(36, -52), Math.toRadians(270));
        TrajectoryActionBuilder pickUpFourthSpecimen2B = pickUpFourthSpecimen1B.endTrajectory().fresh()
                .strafeTo(new Vector2d(36, -55));
        TrajectoryActionBuilder depositFourthSpecimen1B = pickUpFourthSpecimen2B.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(4, -39), Math.toRadians(90));
        TrajectoryActionBuilder depositFourthSpecimen2B = depositFourthSpecimen1B.endTrajectory().fresh()
                .strafeTo(new Vector2d(4, -41));

//        TrajectoryActionBuilder moveBackToObservationZoneB = depositFourthSpecimen2B.endTrajectory().fresh()
//                .strafeTo(new Vector2d(60, -60),
//                        baseVelConstraint,
//                        baseAccelConstraint);
        TrajectoryActionBuilder moveBackToObservationZoneB = depositSecondSpecimen2B.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(60, -60), Math.toRadians(270),
                        baseVelConstraint,
                        baseAccelConstraint);

        depositFirstSpecimen1 = depositFirstSpecimen1B.build();
        depositFirstSpecimen2 = depositFirstSpecimen2B.build();
        pickUpSecondSpecimen1 = pickUpSecondSpecimen1B.build();
        pickUpSecondSpecimen2 = pickUpSecondSpecimen2B.build();
        depositSecondSpecimen1 = depositSecondSpecimen1B.build();
        depositSecondSpecimen2 = depositSecondSpecimen2B.build();
        moveToFirstSample = moveToFirstSampleB.build();
        moveToSecondSample = moveToSecondSampleB.build();
        pickUpThirdSpecimen1 = pickUpThirdSpecimen1B.build();
        pickUpThirdSpecimen2 = pickUpThirdSpecimen2B.build();
        depositThirdSpecimen1 = depositThirdSpecimen1B.build();
        depositThirdSpecimen2 = depositThirdSpecimen2B.build();
        pickUpFourthSpecimen1 = pickUpFourthSpecimen1B.build();
        pickUpFourthSpecimen2 = pickUpFourthSpecimen2B.build();
        depositFourthSpecimen1 = depositFourthSpecimen1B.build();
        depositFourthSpecimen2 = depositFourthSpecimen2B.build();

        moveBackToObservationZone2 = moveBackToObservationZoneB.build();
    }
}
