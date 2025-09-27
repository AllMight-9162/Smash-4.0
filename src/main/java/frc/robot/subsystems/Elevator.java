package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class Elevator {

    SparkFlex motor;

    RelativeEncoder encoder;
    SparkClosedLoopController PIDcontroller;
    SparkFlexConfig motorConfig;

    private final int IdMotor = 9;

    public double kP, kI, kD ,kFF ,kIz , outMax, outMin;

    public Elevator(){

        motor = new SparkFlex(IdMotor, MotorType.kBrushless);

        encoder = motor.getEncoder();
        encoder.setPosition(0.0);

        PIDcontroller = motor.getClosedLoopController();

        motorConfig = new SparkFlexConfig();
        motorConfig.encoder.positionConversionFactor(1);
        motorConfig.encoder.velocityConversionFactor(1);
        motorConfig.idleMode(IdleMode.kBrake);

        config();

    }

    public void config(){
        kP = 0.1;
        kI = 0.0000006;
        kD = 0.01;
        kFF = 0.0;
        kIz = 0.0;

        outMax = 1.0;
        outMin = -1.0;

        motorConfig.closedLoop.pidf(kP, kI, kD, kFF);
        motorConfig.closedLoop.iZone(kIz);
        motorConfig.closedLoop.outputRange(outMin, outMax);

        motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    }

    public void move(double position){
        PIDcontroller.setReference(position * 20, ControlType.kPosition);

    }

    public void stop(){
        motor.set(0.0);

    }

    public void reset(){
        move(0.0);

    }

    public double getPosition(){
        return encoder.getPosition() / 20;
        
    }
}