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

public class Climb {

    SparkFlex leftMotor ;
    SparkFlex rightMotor;

    RelativeEncoder leftEncoder;
    RelativeEncoder righEncoder;

    SparkClosedLoopController leftPID;
    SparkClosedLoopController rightPID;
    SparkFlexConfig motorConfig;

    private final int IdLeftMotor = 16;
    private final int IdRightMotor = 17;
    
    public double kP, kI, kD ,kFF ,kIz , outMax, outMin;

    public Climb(){

        leftMotor = new SparkFlex(IdLeftMotor, MotorType.kBrushless);
        rightMotor = new SparkFlex(IdRightMotor, MotorType.kBrushless);

        leftEncoder = leftMotor.getEncoder();
        leftEncoder.setPosition(0.0);
        righEncoder = rightMotor.getEncoder();
        righEncoder.setPosition(0.0);

        leftPID = leftMotor.getClosedLoopController();
        rightPID = rightMotor.getClosedLoopController();

        motorConfig = new SparkFlexConfig();
        motorConfig.encoder.positionConversionFactor(1);
        motorConfig.encoder.velocityConversionFactor(1);
        motorConfig.idleMode(IdleMode.kBrake);

        Config(leftMotor);
        Config(rightMotor);

    }

    public void Config(SparkFlex motor){
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
    
    public void Up(){
        leftPID.setReference( 0.125 * 60, ControlType.kPosition);
        rightPID.setReference(-0.125 * 60, ControlType.kPosition);

    }
}
