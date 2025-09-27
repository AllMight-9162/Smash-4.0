// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.Controle;
import frc.robot.subsystems.SwerveSubsystem;

import java.io.File;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
  // Aqui iniciamos o swerve
  private SwerveSubsystem swerve = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));

  // É criado o escolhedor de autônomo
  private final SendableChooser<Command> autoChooser;

  // Controle de Xbox, troque para o qual sua equipe estará utilizando
  private CommandXboxController controleXbox = new CommandXboxController(Controle.xboxControle);

  public RobotContainer() {
    // Definimos o comando padrão como a tração
    swerve.setDefaultCommand(swerve.driveCommand(
      () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
      () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND),
      () -> controleXbox.getRightX()));

    NamedCommands.registerCommand("Intake", new PrintCommand("Intake"));

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    // Configure the trigger bindings
    configureBindings();
  }

  // Função onde os eventos (triggers) são configurados
  private void configureBindings() {
    controleXbox.a().onTrue(swerve.driveCommandAlinharComJoystick(
    () -> MathUtil.applyDeadband(controleXbox.getLeftY(), Constants.Controle.DEADBAND),
    () -> MathUtil.applyDeadband(controleXbox.getLeftX(), Constants.Controle.DEADBAND),
    () -> controleXbox.getRightX(),
    () -> controleXbox.getRightY()));


    if(!Robot.isReal()){
      controleXbox.start().onTrue(Commands.runOnce(() -> swerve.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
    }
  }

  // Função que retorna o autônomo
  public Command getAutonomousCommand() {
    // Aqui retornamos o comando que está no selecionador
    return autoChooser.getSelected();
  }

  // Define os motores como coast ou brake
  public void setMotorBrake(boolean brake) {
    swerve.setMotorBrake(brake);
  }
}
