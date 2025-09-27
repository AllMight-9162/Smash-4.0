package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.CoralIntake;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.KelpIntake;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  Elevator elevator;
  KelpIntake kelpIntake;
  CoralIntake coralIntake;
  XboxController pilotoSub;
  Timer timer;
  boolean n1,n2,n3,n4,processor,station,stop;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    elevator = new Elevator();
    kelpIntake = new KelpIntake();
    coralIntake = new CoralIntake();
    pilotoSub = new XboxController(1);
    timer = new Timer();
    
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

  }
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  
  @Override
  public void autonomousInit() {
    m_robotContainer.setMotorBrake(true);
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    kelpIntake.angulate(45.0);
    timer.reset();
    timer.start();

    n1 = false;
    n2 = false;
    n3 = false;
    n4 = false;
    processor = false;
    station = false;
    stop = false;
  }

  
  @Override
  public void teleopPeriodic() {
    if(pilotoSub.getAButtonPressed()){
      //n4
      n1 = false;
      n2 = false;
      n3 = false;
      n4 = true;
      processor = false;
      station = false;
      stop = false;

    }else if(pilotoSub.getBButtonPressed()){
      //n3
      n1 = false;
      n2 = false;
      n3 = true;
      n4 = false;
      processor = false;
      station = false;
      stop = false;

    }else if(pilotoSub.getYButtonPressed()){
      //n2
      n1 = false;
      n2 = true;
      n3 = false;
      n4 = false;
      processor = false;
      station = false;
      stop = false;

    }else if(pilotoSub.getXButtonPressed()){
      //n1
      n1 = true;
      n2 = false;
      n3 = false;
      n4 = false;
      processor = false;
      station = false;
      stop = false;

    }else if(pilotoSub.getRightBumperButtonPressed()){
      //processor
      n1 = false;
      n2 = false;
      n3 = false;
      n4 = false;
      processor = true;
      station = false;
      stop = false;

    }else if(pilotoSub.getLeftBumperButtonPressed()){
      //station
      if(n1 == true || n2 == true || n3 == true || n4 == true){
        station = false;
      }
      else{
        n1 = false;
        n2 = false;
        n3 = false;
        n4 = false;
        processor = false;
        station = true;
        stop = false;

      }

    }else if(pilotoSub.getBackButtonPressed()){
      //stop
      n1 = false;
      n2 = false;
      n3 = false;
      n4 = false;
      processor = false;
      station = false;
      stop = true;
    }


    n1();
    n2();
    n3();
    n4();
    processor();
    station();
    stop();
    
  }

  public void n1(){
    if(n4 == true){
      elevator.reset();
      kelpIntake.reset();
      coralIntake.angulate(200.0);
      if(pilotoSub.getLeftBumperButtonPressed()){
        coralIntake.drop();
      }
    }
    
  }

  public void n2(){
    if(n4 == true){
      elevator.reset();
      kelpIntake.reset();
      coralIntake.angulate(180.0);
      if(pilotoSub.getLeftBumperButtonPressed()){
        coralIntake.drop();
      }
    }
    
  }

  public void n3(){
    if(n4 == true){
      elevator.move(2.5);
      kelpIntake.angulate(90.0);
      coralIntake.angulate(180.0);
      if(pilotoSub.getLeftBumperButtonPressed()){
        coralIntake.drop();
        kelpIntake.take();
      }
    }
    
  }

  public void n4(){
    if(n4 == true){
      elevator.move(3.5);
      kelpIntake.angulate(90.0);
      coralIntake.angulate(180.0);
      if(pilotoSub.getLeftBumperButtonPressed()){
        coralIntake.drop();
        kelpIntake.take();
      }
    }
    
  }

  public void processor(){
    if(n4 == true){
      elevator.move(4.0);
      kelpIntake.angulate(45.0);
      coralIntake.reset();
      if(pilotoSub.getLeftBumperButtonPressed()){
        kelpIntake.drop();
      }
    }
  }

  public void station(){
    if(station == true){
      elevator.reset();
      kelpIntake.angulate(45.0);
      coralIntake.angulate(45.0);
      coralIntake.take();
    }

  }

  public void stop(){
    if(stop == true){
      elevator.reset();
      kelpIntake.angulate(45.0);
      kelpIntake.stop();
      coralIntake.reset();
      coralIntake.stop();
    }
  }

}
