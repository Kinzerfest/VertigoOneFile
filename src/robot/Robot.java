package robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {
	Spark climber = new Spark(6), intakeLeft = new Spark(4), intakeRight = new Spark(5);
	TalonSRX elevator = new TalonSRX(62);
	Joystick primaryStick = new Joystick(0), secondaryStick = new Joystick(1), climberStick = new Joystick(2);
	DifferentialDrive driveTrain = new DifferentialDrive(new SpeedControllerGroup(new Spark(1), new Spark(0)), new SpeedControllerGroup(new Spark(2), new Spark(3)));;
	DigitalInput limitSwitchTop = new DigitalInput(5), limitSwitchBottom = new DigitalInput(4);
	AHRS navX  = new AHRS(SPI.Port.kMXP); 
	public void robotInit() {
	driveTrain.setDeadband(0.13);
	}
	public void autonomousInit() {
	}
	public void autonomousPeriodic() {
	}
	public void teleopPeriodic() {
		drive();
		angleAdjustment();
		Elevator();
		Intake();
	}
	public void testPeriodic() {
	}
	
	void drive() {
		double turnThrottle;
		if(primaryStick.getRawButton(4)) {turnThrottle = limit(angleDifference(navX.getAngle(), 270) * 0.02, 0.4);}
		if(primaryStick.getRawButton(5)) {turnThrottle = limit(angleDifference(navX.getAngle(), 90) * 0.02, 0.4);}
		if(primaryStick.getRawButton(3)) {turnThrottle = limit(angleDifference(navX.getAngle(), 0) * 0.02, 0.4);}
		if(primaryStick.getRawButton(2)) {turnThrottle = limit(angleDifference(navX.getAngle(), 180) * 0.02, 0.4);}
		else {turnThrottle = primaryStick.getX();}
		driveTrain.arcadeDrive(-primaryStick.getY(), turnThrottle);
	}
	
	void vectorDriveFC() {
		driveTrain.arcadeDrive(primaryStick.getMagnitude(), limit(angleDifference(navX.getAngle(), primaryStick.getDirectionDegrees()) * 0.04, 0.8));
	}
	
	void Elevator() {
    	if (!secondaryStick.getRawButton(1)){
	    	if (limitSwitchTop.get()&&-secondaryStick.getY()>=0) {
	    		elevator.set(ControlMode.PercentOutput, 0.0);
	        	}
	    	else if (limitSwitchBottom.get()&&-secondaryStick.getY()<=0) {
	    		elevator.set(ControlMode.PercentOutput, 0.0);
	        }
	    	else{
	    		elevator.set(ControlMode.PercentOutput, -secondaryStick.getY());
	    	}
    	} else {
    		elevator.set(ControlMode.PercentOutput, -secondaryStick.getY());
    	} 
	}
	
	void Intake() {
		if (secondaryStick.getRawButton(4)) {
		intakeLeft.set(0.6);
		}
		if(secondaryStick.getRawButton(5)) {
		intakeLeft.set(-0.85);	
		}
		else {
		intakeLeft.set(secondaryStick.getX());
		}
		intakeRight.set(-intakeLeft.getSpeed());
	}
	
	 void angleAdjustment() {
    	if(primaryStick.getRawButton(11)) {
    	navX.setAngleAdjustment(navX.getAngleAdjustment()+1.125);
    	}
    	if(primaryStick.getRawButton(12)) {
        navX.setAngleAdjustment(navX.getAngleAdjustment()-1.125);
        }
    }
	
    public static double limit(double value, double limit) {
    	if (value > limit) {return limit;}
    	else if (value < -limit) {return -limit;}
    	else {return value;}
    }
	public static double angleDifference(double startingAngle, double desiredAngle){
		double difference;
		difference = startingAngle - desiredAngle;
		if (difference > -180 && difference <= 180)
			return -difference;
		else if (difference <= -180)
			return -(difference + 360);
		else if (difference > 180)
			return -(difference - 360);
		else 
		return 0;
	}
}

/*
 * intakeLeft = new Spark(4);
	intakeRight = new Spark(5);
	elevator = new TalonSRX(62);
	climber = new Spark(6);
	limitSwitchTop = new DigitalInput(5);
	limitSwitchBottom = new DigitalInput(4);
	navX = new AHRS(SPI.Port.kMXP);
	
	primaryStick = new Joystick(0);
	secondaryStick = new Joystick(1);
	climberStick = new Joystick(3);
	*/
