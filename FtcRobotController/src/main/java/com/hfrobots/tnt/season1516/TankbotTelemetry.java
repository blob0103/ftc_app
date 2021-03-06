package com.hfrobots.tnt.season1516;

public abstract class TankbotTelemetry extends TankbotHardware {

    protected void updatePriorityTelemetry(String message) {
        telemetry.addData("00", message);
    }

    /**
     * Update the telemetry with current values from the hardware.
     */
    public void updateTelemetry() {
        if (wasWarningGenerated()) {
            setFirstMessage(getWarningMessage());
        }

        //
        // Send telemetry data to the driver station.
        //
        telemetry.addData
                ( "02"
                        , "Left drive: (P) "
                                + getLeftDrivePower()
                                + ", (E) "
                                + ""
                );
        telemetry.addData
                ( "03"
                        , "Right Drive: (P) "
                                + getRightDrivePower()
                                + ", (E)"
                                + ""
                );
    }

    public void updateGamepadTelemetry() {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData ("06", "GP1 Left x: " + -gamepad1.left_stick_x);
        telemetry.addData ("07", "GP1 Left y: " + -gamepad1.left_stick_y);
        telemetry.addData ("08", "GP1 Right x: " + -gamepad1.right_stick_x);
        telemetry.addData ("09", "GP1 Right y: " + -gamepad1.right_stick_y);
    }

    public void setFirstMessage(String message) {
        telemetry.addData ( "00", message);
    }


    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void setErrorMessage(String message) {
        setFirstMessage("ERROR: " + message);
    }
}