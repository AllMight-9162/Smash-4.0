package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;

public class EarsUp {
    Servo leftServo;
    Servo rightServo;

    public EarsUp(){
        leftServo = new Servo(0);
        rightServo = new Servo(1);
        
    }

    public void up(){
        leftServo.set(0.5);
        rightServo.set(0.5);

    }
    
}
