package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "wow what an amazing day")
public class mecanumdrivewheels extends LinearOpMode {
    //Initializing hardware
    private DcMotor leftFront, rightFront, leftBack, rightBack;
    private DcMotorEx linearslide1, gilfoil, linearslide2;
    private CRServo lagrangepoints, jackismadidk;
    private GoBildaPinpointDriver odocomputer;
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        //Initializing variables
        double y, x, z, theta, sin, cos, max, power;
        //Variable finds
        int post, post2, tickRotation, post3;
        final int tickRotation2 = 4200, tickRotation3 = 6000;
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        linearslide1 = hardwareMap.get(DcMotorEx.class, "linearslide1");
        lagrangepoints = hardwareMap.get(CRServo.class, "lagrangepoints");
        gilfoil = hardwareMap.get(DcMotorEx.class, "gilfoil");
        linearslide2 = hardwareMap.get(DcMotorEx.class,"linearslide2");
        jackismadidk = hardwareMap.get(CRServo.class, "jackismadidk");
        odocomputer = hardwareMap.get(GoBildaPinpointDriver.class, "odocomputer");
        odocomputer.resetPosAndIMU();
        linearslide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearslide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linearslide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearslide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        linearslide1.setDirection(DcMotorSimple.Direction.REVERSE);
        linearslide2.setDirection(DcMotorSimple.Direction.REVERSE);
        jackismadidk.setDirection(CRServo.Direction.REVERSE);
        waitForStart();
        if (isStopRequested()) return;
        if (opModeIsActive()) {
            post3 = gilfoil.getCurrentPosition();
            while(opModeIsActive()) {
                odocomputer.update();
                if (gamepad2.right_bumper) {
                    lagrangepoints.setPower(1);
                }
                else if (gamepad2.left_bumper) {
                    lagrangepoints.setPower(-1);
                }
                else {
                    lagrangepoints.setPower(0);
                }
                if (gamepad2.y) {
                    runtime.reset();
                    jackismadidk.setPower(1);
                }
                else if (gamepad2.a) {
                    runtime.reset();
                    jackismadidk.setPower(-1);
                }
                if (runtime.seconds() > 0.5) {
                    jackismadidk.setPower(0);
                }
                telemetry.addData("jackismadidk", jackismadidk.getPower());
                //Linear Slide
                post = linearslide1.getCurrentPosition();
                telemetry.addData("post", post);
                if (post3 > 3500) {
                    tickRotation = 3500;
                }
                else {
                    tickRotation = 2400;
                }
                if (-gamepad2.left_stick_y != 0 && ((post < tickRotation && post > 10) || (post < 10 && -gamepad2.left_stick_y > 0) || (post > tickRotation && -gamepad2.left_stick_y < 0))) {
                    linearslide1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    linearslide1.setPower(-gamepad2.left_stick_y);
                } else if (post > 10) {
                    linearslide1.setPower(0);
                    linearslide1.setTargetPosition(post);
                    linearslide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    linearslide1.setVelocity(700);
                }
                post2 = linearslide2.getCurrentPosition();
                telemetry.addData("post2", post2);
                if (-gamepad2.right_stick_y != 0 && ((post2 < tickRotation2 && post2 > 10) || (post2 < 10 && -gamepad2.right_stick_y > 0) || (post2 > tickRotation2 && -gamepad2.right_stick_y < 0))) {
                    linearslide2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    linearslide2.setPower(-gamepad2.right_stick_y);
                } else if (post2 > 10) {
                    linearslide2.setPower(0);
                    linearslide2.setTargetPosition(post2);
                    linearslide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    linearslide2.setVelocity(700);
                }
                float cool = 0;
                post3 = gilfoil.getCurrentPosition();
                if (gamepad2.left_trigger != 0) {
                    cool = -gamepad2.left_trigger;
                }
                else if (gamepad2.right_trigger != 0) {
                    cool = gamepad2.right_trigger;
                }
                else {
                    cool = 0;
                }
                telemetry.addData("post3", post3);
                if (Math.abs(cool) > 0.3 && ((post3 < tickRotation3 && post3 > -10000) || (post3 < 10 && cool > 0) || (post3 > tickRotation3 && cool < 0))) {
                    gilfoil.setVelocity(cool * 2800);
                }
                else {
                    gilfoil.setVelocity(0);
                }
                if (gamepad1.left_bumper) {
                    odocomputer.resetPosAndIMU();
                }
                double botHeading = odocomputer.getHeading();
                y = -gamepad1.left_stick_y;
                x = gamepad1.left_stick_x;
                z = gamepad1.right_stick_x;
                theta = Math.atan2(y,x);
                power = Math.hypot(x,y);
                sin = Math.sin((theta - botHeading) - Math.PI/4); //
                cos = Math.cos((theta - botHeading) - Math.PI/4);
                max = Math.max(Math.abs(sin), Math.abs(cos));

                leftFront.setPower(power * cos/max + z);
                leftBack.setPower(power * sin/max + z);
                rightFront.setPower(power * sin/max - z);
                rightBack.setPower(power * cos/max - z);

                double[] motor = {leftFront.getPower(), leftBack.getPower(), rightBack.getPower(), rightFront.getPower()};

                if ((power + Math.abs(z)) > 1) {
                    leftFront.setPower(motor[0]/(power + Math.abs(z)));
                    rightFront.setPower(motor[3]/(power + Math.abs(z)));
                    leftBack.setPower(motor[1]/(power + Math.abs(z)));
                    rightBack.setPower(motor[2]/(power + Math.abs(z)));
                }
                telemetry.update();
            }
        }
    }
}