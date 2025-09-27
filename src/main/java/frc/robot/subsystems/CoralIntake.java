package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.RelativeEncoder;
import frc.robot.subsystems.Utilities.UltrasonicSensor;

public class CoralIntake {

    UltrasonicSensor leftUltrasonic;
    UltrasonicSensor rightUltrasonic;
    
    SparkMax AngulateMotor;
    SparkMax LeftMotor;
    SparkMax RightMotor;

    RelativeEncoder encoder;
    SparkClosedLoopController PIDcontroller;
    SparkMaxConfig motorConfig;

    private final int LeftTriggerPort = 2;
    private final int LeftEchoPort = 3;

    private final int RightTriggerPort = 4;
    private final int RightEchoPort = 5;

    private final int IdAngulateMotor = 13;
    private final int IdLeftMotor = 14;
    private final int IdRightMotor = 15;
    
    public double kP, kI, kD ,kFF ,kIz , outMax, outMin;

    public CoralIntake(){

        leftUltrasonic = new UltrasonicSensor(LeftTriggerPort, LeftEchoPort);
        rightUltrasonic = new UltrasonicSensor(RightTriggerPort, RightEchoPort);

        AngulateMotor = new SparkMax(IdAngulateMotor, MotorType.kBrushless);
        LeftMotor = new SparkMax(IdLeftMotor, MotorType.kBrushless);
        RightMotor = new SparkMax(IdRightMotor, MotorType.kBrushless);

        encoder = AngulateMotor.getEncoder();
        encoder.setPosition(0.0);

        PIDcontroller = AngulateMotor.getClosedLoopController();

        motorConfig = new SparkMaxConfig();
        motorConfig.encoder.positionConversionFactor(1);
        motorConfig.encoder.velocityConversionFactor(1);
        motorConfig.idleMode(IdleMode.kBrake);

        Config(AngulateMotor);
        Config(RightMotor);
        Config(LeftMotor);

    }

    public void Config(SparkMax motor){

        kP = 0.1;
        kI = 0.0000006;
        kD = 0.015;
        kFF = 0.0;
        kIz = 0.0;

        outMax = 0.5;
        outMin = -0.5;

        motorConfig.closedLoop.pidf(kP, kI, kD, kFF);
        motorConfig.closedLoop.iZone(kIz);
        motorConfig.closedLoop.outputRange(outMin, outMax);

        motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    }

    public void angulate(double position){
        PIDcontroller.setReference((position * 20) * 360, ControlType.kPosition);

    }

    public void take(){
        angulate(55);
        LeftMotor.set(1.0);
        RightMotor.set(1.0);

    }

    public void drop(){
        angulate(245);
        LeftMotor.set(-1.0);
        RightMotor.set(-1.0);

    }

    public void reset(){
        angulate(0.0);
    }

    public void stop(){
        LeftMotor.set(0.0);
        RightMotor.set(0.0);
        
    }

    public double getPosition(){
        return (encoder.getPosition() / 20) * 360;
    }

}