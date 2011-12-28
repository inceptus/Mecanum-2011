/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//Standard for inceptus code
package org.inceptus;

//Imports
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

//Mecanum class. We are using IterativeRobot as SimpleRobot was not working.
public class Mecanum extends IterativeRobot {
    //Get the joysticks (keep joy* naming system so it can be adjusted later)
    Joystick joy1 = new Joystick(1);
    Joystick joy2 = new Joystick(2);
    Joystick joy3 = new Joystick(3);
    
    //Get the jaguars
    Jaguar front_right = new Jaguar(2);
    Jaguar front_left = new Jaguar(1);
    Jaguar rear_right = new Jaguar(3);
    Jaguar rear_left = new Jaguar(4);
    
    //Setup RobotDrive
    RobotDrive drive = new RobotDrive(front_left, rear_left, front_right, rear_right);
    
    //If using iOS controls
    boolean iOS = false;
    
    //When robot starts
    public void robotInit() {
        //Invert only the right side motors. Keep the left side normal.
        drive.setInvertedMotor(MotorType.kFrontRight, true);
        drive.setInvertedMotor(MotorType.kRearRight, true);
        drive.setInvertedMotor(MotorType.kFrontLeft, false);
        drive.setInvertedMotor(MotorType.kRearLeft, false);
        //Disable the Watchdog as it can cause issues with this version of java.
        Watchdog.getInstance().setEnabled(false);
    }
    
    //When robot is disabled
    public void disabledInit() {
        //Stop the motors for safety
        front_left.set(0);
        rear_left.set(0);
        front_right.set(0);
        rear_right.set(0);
        //Log disabled status
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Disabled");
    }

    //Periodically called during autonomous
    public void autonomousPeriodic() {
        
    }
    
    //When robot is started in autonomous
    public void autonomousInit() {
        //Set the iOS boolean to the opposite of digital input 1.
        iOS = !(DriverStation.getInstance().getDigitalIn(1));
        //Log initiation success
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Autonomous Initiated");
    }
    
    //Called at the start of teleop
    public void teleopInit() {
        //Log initiation success
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Teleop Initiated");
    }
    
    //Periodically called during teleop
    public void teleopPeriodic() {
        //Initiate the X,Y,Z vars to be set later
        double X, Y, Z;
        //Check if using iOS interface or not
        if(iOS){
            //Axis id's from http://comets.firstobjective.org/DSHelp.html
            X = joy1.getRawAxis(1);
            Y = joy1.getRawAxis(2);
            Z = joy1.getRawAxis(3);
        }else{
            //Standard controls
            X = joy1.getX();
            Y = joy1.getY();
            Z = joy2.getX();
        }
        //Threshold
        if( Math.abs(X) < .2 ){
            X = 0;
        }
        if( Math.abs(Y) < .2 ){
            Y = 0;
        }
        if( Math.abs(Z) < .2 ){
            Z = 0;
        }
        
    }
    
    //Routine to simplify driving the 4 motors
    public void inceptusDrive(double FL, double RL, double FR, double RR, double sensitivity)
    {
        //Drive
        front_left.set(FL * sensitivity);
        rear_left.set(RL * sensitivity);
        front_right.set(FR * sensitivity);
        rear_right.set(RR * sensitivity);
    }
}
