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

public class KelpIntake {

    UltrasonicSensor ultrasonicSensor;

    SparkMax leftMotor;
    SparkMax rightMotor;
    SparkMax angulateMotor;

    RelativeEncoder encoder;
    SparkClosedLoopController PIDcontroller;
    SparkMaxConfig motorConfig;

    private final int TriggerPort = 0;
    private final int EchoPort = 1;

    private final int IdAngulateMotor = 13;
    private final int IdLeftMotor = 14;
    private final int IdRightMotor = 15;

    public double kP, kI, kD ,kFF ,kIz , outMax, outMin;

    public KelpIntake(){

        ultrasonicSensor = new UltrasonicSensor(TriggerPort, EchoPort);

        leftMotor = new SparkMax(IdLeftMotor, MotorType.kBrushless);
        rightMotor = new SparkMax(IdRightMotor, MotorType.kBrushless);
        angulateMotor = new SparkMax(IdAngulateMotor, MotorType.kBrushless);

        encoder = angulateMotor.getEncoder();
        encoder.setPosition(0.0);

        PIDcontroller = angulateMotor.getClosedLoopController();

        motorConfig = new SparkMaxConfig();
        motorConfig.encoder.positionConversionFactor(1);
        motorConfig.encoder.velocityConversionFactor(1);

        config(angulateMotor);
        config(leftMotor);
        config(rightMotor);
        
    }

    public void config(SparkMax motor){

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
        motorConfig.idleMode(IdleMode.kBrake);

        motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
       
    }

    public void angulate(double position){
        PIDcontroller.setReference((position * 20) * 360, ControlType.kPosition);
    }

    public void take(){
        leftMotor.set(-1.0);
        rightMotor.set(1.0);
    } 

    public void drop(){
        leftMotor.set(1.0);
        rightMotor.set(-1.0);
    }

    public void stop(){
        leftMotor.set(0.0);
        rightMotor.set(0.0);

    }

    public void reset(){
        angulate(0.0);
    }

    public double getPosition(){
        return encoder.getPosition() / 12;
        
    }

}
