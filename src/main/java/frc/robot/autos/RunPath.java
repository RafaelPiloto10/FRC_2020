/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.autos;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.DriveTrainSubsystem;

/**
 * Get a Ramsete Command that drives a path
 */
public class RunPath {
    public static RamseteCommand getCommand(Trajectory path, DriveTrainSubsystem drive, boolean isReverse) {
        if (isReverse)
            drive.reverseEncoders();

        drive.resetOdometry(path.getInitialPose());
        drive.setNeutralMode(NeutralMode.Coast);
        return new RamseteCommand(path, drive::getPose,
                new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
                new SimpleMotorFeedforward(DriveConstants.ksVolts, DriveConstants.kvVoltSecondsPerMeter,
                        DriveConstants.kaVoltSecondsSquaredPerMeter),
                DriveConstants.kDriveKinematics, drive::getWheelSpeeds, AutoConstants.L_CONTROLLER,
                AutoConstants.R_CONTROLLER,
                // RamseteCommand passes volts to the callback
                (!isReverse ? drive::tankDriveVolts : drive::tankDriveVoltsReverse), drive);
    }
}
