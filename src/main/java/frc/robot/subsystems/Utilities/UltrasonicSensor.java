package frc.robot.subsystems.Utilities;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;

public class UltrasonicSensor {
    private final DigitalOutput trigger;
    private final DigitalInput echo;
    private final Timer timer;

    public UltrasonicSensor(int triggerPort, int echoPort) {
        trigger = new DigitalOutput(triggerPort);
        echo = new DigitalInput(echoPort);
        timer = new Timer();
    }

    public double getDistance() {
        // Dispara o Trigger
        trigger.set(true);
        Timer.delay(0.00001); // Pulso de 10µs
        trigger.set(false);

        // Espera um tempo limitado para o Echo ativar
        timer.reset();
        timer.start();
        while (!echo.get()) {
            if (timer.get() > 0.05) { // Timeout de 50ms
                return -1;
            }
        }

        timer.reset();
        while (echo.get()) {
            if (timer.get() > 0.05) { // Timeout de 50ms
                return -1;
            }
        }

        double pulseDuration = timer.get();
        return (pulseDuration * 34300.0) / 2.0; // Distância em cm
    }
}