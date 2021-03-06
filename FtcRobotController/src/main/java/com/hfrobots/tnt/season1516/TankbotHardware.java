package com.hfrobots.tnt.season1516;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

public abstract class TankbotHardware extends OpMode {

    // Required hardware map
    // Motor Controller 1 (AL00XQ80)
    //     Port 1 - leftDrive (encoder is in port 1)
    //     Port 2 - rightDrive (encoder is in port 2)
    // Servo Controller 1 (AL00XOIU)
    //     Port 6 - hookServo
    //

    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     * <p/>
     * The system calls this member once when the OpMode is enabled.
     */
    @Override
    public void init() {
        //
        // Use the hardwareMap to associate class members to hardware ports.
        //
        // Note that the names of the devices (i.e. arguments to the get method)
        // must match the names specified in the configuration file created by
        // the FTC Robot Controller (Settings-->Configure Robot).
        //
        // The variable below is used to provide telemetry data to a class user.
        //
        warningGenerated = false;
        warningMessage = "Can't map; ";

        try {
            leftDrive = hardwareMap.dcMotor.get("leftDrive");
            leftDrive.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception p_exeception) {
            appendWarningMessage("leftDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            leftDrive = null;
        }

        try {
            rightDrive = hardwareMap.dcMotor.get("rightDrive");
        } catch (Exception p_exeception) {
            appendWarningMessage("rightDrive");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            rightDrive = null;
        }

        try {
            hookServo = hardwareMap.servo.get("hookServo");
        } catch (Exception p_exeception) {
            appendWarningMessage("hookServo");
            DbgLog.msg(p_exeception.getLocalizedMessage());

            hookServo = null;
        }
    }

    /**
     * Access whether a warning has been generated.
     */
    boolean wasWarningGenerated() {
        return warningGenerated;
    }

    /**
     * Access the warning message.
     */
    String getWarningMessage()

    {
        return warningMessage;
    }

    /**
     * Mutate the warning message by ADDING the specified message to the current
     * message; set the warning indicator to true.
     * <p/>
     * A comma will be added before the specified message if the message isn't
     * empty.
     */
    void appendWarningMessage(String exceptionMessage) {
        if (warningGenerated) {
            warningMessage += ", ";
        }
        warningGenerated = true;
        warningMessage += exceptionMessage;
    }

    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    float scaleMotorPower(float unscaledPower) {

        //
        // Ensure the values are legal.
        //
        float clippedPower = Range.clip(unscaledPower, -1, 1);

        float[] scaleFactors =
                {0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the given unscaled power.
        //
        int scaleIndex = (int) (clippedPower * 16.0);

        if (scaleIndex < 0) {
            scaleIndex = -scaleIndex;
        } else if (scaleIndex > 16) {
            scaleIndex = 16;
        }

        final float scaledPower;

        if (clippedPower < 0) {
            scaledPower = -scaleFactors[scaleIndex];
        } else {
            scaledPower = scaleFactors[scaleIndex];
        }

        return scaledPower;
    }

    double getRightDrivePower() {

        if (rightDrive != null) {
            return rightDrive.getPower();
        }

        return 0.0;
    }

    double getLeftDrivePower() {

        if (leftDrive != null) {
            return leftDrive.getPower();
        }

        return 0.0;
    }

    void setDrivePower(double leftDrivePower, double rightDrivePower) {
        if (leftDrive != null) {
            leftDrive.setPower(leftDrivePower);
        }

        if (rightDrive != null) {
            rightDrive.setPower(rightDrivePower);
        }
    }

    void stopAllDriveMotors() {
        setDrivePower(0, 0);
    }

    public void runUsingEncoders() {
        DcMotor[] motorsWithEncoders = new DcMotor[] { leftDrive, rightDrive };

        for (DcMotor aMotor : motorsWithEncoders) {
            if (aMotor != null) {
                aMotor.setMode
                        (DcMotorController.RunMode.RUN_USING_ENCODERS
                        );
            }
        }
    }

    public void runWithoutDriveEncoders() {
        DcMotor[] allDriveMotors = new DcMotor[] { leftDrive, rightDrive};

        for (DcMotor aMotor : allDriveMotors) {
            if (aMotor != null) {
                aMotor.setMode
                        (DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }
    }

    public void resetRightDriveEncoder() {
        if (rightDrive != null) {
            rightDrive.setMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }

    public void resetLeftDriveEncoder() {
        if (leftDrive != null) {
            leftDrive.setMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    }

    /**
     * Reset both drive wheel encoders.
     */
    public void resetDriveEncoders() {
        //
        // Reset the motor encoders on the drive wheels.
        //
        resetLeftDriveEncoder();
        resetRightDriveEncoder();
    }

    public void setHookServoPosition(final double position) {
        if (hookServo != null) {
            hookServo.setPosition(position);
        }
    }

    public double getHookServoPosition() {
        if (hookServo != null) {
            return hookServo.getPosition();
        }

        return 0;
    }

    /**
     * Indicate whether a message is a available to the class user.
     */
    private boolean warningGenerated = false;

    /**
     * Store a message to the user if one has been generated.
     */
    private String warningMessage;

    private DcMotor leftDrive;

    private DcMotor rightDrive;

    private Servo hookServo;
}
