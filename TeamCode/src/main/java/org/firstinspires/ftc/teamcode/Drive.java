package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Java Drive Test")
public class Drive extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backRight = hardwareMap.get(DcMotor.class, "back_right");
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        final Input input = readInput();
        final DriveSpeeds driveSpeeds = inputToDriveSpeeds(input);
        writeDirectionTelemetry(driveSpeeds);
        final WheelSpeeds rawWheelSpeeds = directionToRawWheelSpeeds(driveSpeeds);
        final WheelSpeeds wheelSpeeds = normalizeWheelSpeeds(rawWheelSpeeds);
        writeWheelSpeeds(wheelSpeeds);
    }

    private Input readInput() {
        return new Input(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                gamepad1.right_stick_x
        );
    }

    private static DriveSpeeds inputToDriveSpeeds(Input input) {
        return new DriveSpeeds(
                -input.leftStickY,
                input.leftStickX,
                input.rightStickX
        );
    }

    private static WheelSpeeds directionToRawWheelSpeeds(DriveSpeeds driveSpeeds) {
        return new WheelSpeeds(
                driveSpeeds.axial + driveSpeeds.lateral + driveSpeeds.yaw,
                driveSpeeds.axial - driveSpeeds.lateral - driveSpeeds.yaw,
                driveSpeeds.axial - driveSpeeds.lateral + driveSpeeds.yaw,
                driveSpeeds.axial + driveSpeeds.lateral - driveSpeeds.yaw
        );
    }

    private static WheelSpeeds normalizeWheelSpeeds(WheelSpeeds wheelSpeeds) {
        return new WheelSpeeds(
                wheelSpeeds.frontLeft / 3.0,
                wheelSpeeds.frontRight / 3.0,
                wheelSpeeds.backLeft / 3.0,
                wheelSpeeds.backRight / 3.0
        );
    }

    private void writeDirectionTelemetry(DriveSpeeds driveSpeeds) {
        telemetry.addData("axial", driveSpeeds.axial);
        telemetry.addData("lateral", driveSpeeds.lateral);
        telemetry.addData("yaw", driveSpeeds.yaw);
    }

    private void writeWheelSpeeds(WheelSpeeds wheelSpeeds) {
        frontLeft.setPower(wheelSpeeds.frontLeft);
        frontRight.setPower(wheelSpeeds.frontRight);
        backLeft.setPower(wheelSpeeds.backLeft);
        backRight.setPower(wheelSpeeds.backRight);
    }

    private static class Input {
        public final double leftStickX, leftStickY, rightStickX;
        public Input(double leftStickX, double leftStickY, double rightStickX) {
            this.leftStickX = leftStickX;
            this.leftStickY = leftStickY;
            this.rightStickX = rightStickX;
        }
    }

    private static class DriveSpeeds {
        public final double axial, lateral, yaw;
        public DriveSpeeds(double axial, double lateral, double yaw) {
            this.axial = axial;
            this.lateral = lateral;
            this.yaw = yaw;
        }
    }

    private static class WheelSpeeds {
        public final double frontLeft, frontRight, backLeft, backRight;

        public WheelSpeeds(double frontLeft, double frontRight, double backLeft, double backRight) {
            this.frontLeft = frontLeft;
            this.frontRight = frontRight;
            this.backLeft = backLeft;
            this.backRight = backRight;
        }
    }
}
