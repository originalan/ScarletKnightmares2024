package org.firstinspires.ftc.teamcode.roadrunner.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.TankDrive;

@Config
public final class SplineTest extends LinearOpMode {

    public static int counter = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

            waitForStart();

            switch (counter) {
                case 1:
                    Actions.runBlocking(
                            drive.actionBuilder(beginPose)
//                                    .splineTo(new Vector2d(24, 24), Math.PI / 2)
                                    .strafeTo(new Vector2d(24, 48))
                                    .strafeTo(new Vector2d(0, 48))
                                    .build()
                    );
                    break;
                case 2:
                    Actions.runBlocking(
                            drive.actionBuilder(beginPose)
                                    .strafeTo(new Vector2d(24, 0))
                                    .build()
                    );
                    break;
                case 3:
                    Actions.runBlocking(
                            drive.actionBuilder(beginPose)
                                    .strafeTo(new Vector2d(0, 24))
                                    .build()
                    );
                    break;
            }
        } else if (TuningOpModes.DRIVE_CLASS.equals(TankDrive.class)) {
            TankDrive drive = new TankDrive(hardwareMap, beginPose);

            waitForStart();

            Actions.runBlocking(
                    drive.actionBuilder(beginPose)
                            .splineTo(new Vector2d(30, 30), Math.PI / 2)
                            .splineTo(new Vector2d(0, 60), Math.PI)
                            .build());
        } else {
            throw new RuntimeException();
        }
    }
}