#include <PID_v1.h>
#include <L298NX2.h>
#include <HCSR04.h>

#define MotFwd 8  // Motor Forward pin  //left 13 right 8
#define MotRev 7  // Motor Reverse pin  // left 12 right 7



//Motor right
#define enA 9
#define in1 7
#define in2 8
#define Rc1 3
#define Rc2 4

//Motor left
#define enB 11
#define in3 13
#define in4 12
#define Lc1 5
//#define Lc2 6


#define ultraLeftTrig A1
#define ultraLeftEcho A0


#define ultraRightTrig A3
#define ultraRightEcho A4


//HCSR04 frontHC(ultraMiddleTrig, ultraMiddleEcho); //initialisation class HCSR04 (trig pin , echo pin)
HCSR04 rightHC(ultraRightTrig, ultraRightEcho);    //initialisation class HCSR04 (trig pin , echo pin)
HCSR04 leftHC(ultraLeftTrig, ultraLeftEcho);       //initialisation class HCSR04 (trig pin , echo pin)

boolean LEFTWALL = 0, RIGHTWALL = 0, FRONTWALL = 0;

const int rightIr = 5;
const int leftIr = A5;
const int frontIr = 10;

unsigned short rightMotor = 93, leftMotor = 93;

// Motor 1 encoder pins
const int encoder1BPin = 3;
const int encoder1APin = 2;

volatile int rightEncoder = 0;
volatile int leftEncoder = 0;

L298N myMotor1(enA, in1, in2);
L298N myMotor2(enB, in3, in4);

void setup() {
  Serial.begin(9600);

  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);

  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  pinMode(ultraLeftTrig, OUTPUT);  // Sets the trigPin as an Output
  pinMode(ultraLeftEcho, INPUT);   // Sets the echoPin as an Input

  pinMode(ultraRightTrig, OUTPUT);  // Sets the trigPin as an Output
  pinMode(ultraRightEcho, INPUT);   // Sets the echoPin as an Input



  pinMode(leftIr, INPUT);
  pinMode(rightIr, INPUT);
  pinMode(frontIr, INPUT);


  attachInterrupt(digitalPinToInterrupt(encoder1APin), changeRight, CHANGE);
  attachInterrupt(digitalPinToInterrupt(encoder1BPin), changeLeft, CHANGE);

  myMotor1.setSpeed(rightMotor);
  myMotor2.setSpeed(leftMotor);
  myMotor1.forward();
  myMotor2.forward();

  // uturn();

  // pinMode(encoder1APin, INPUT_PULLUP);
  // pinMode(encoder1BPin, INPUT_PULLUP);
  // pinMode(encoder2APin, INPUT_PULLUP);
  // pinMode(encoder2BPin, INPUT_PULLUP);
}

void changeRight(){
  rightEncoder++;
}

void changeLeft(){
  leftEncoder++;
}

void moveForward(int x = 130){
    myMotor1.setSpeed(rightMotor);
    myMotor2.setSpeed(leftMotor);
    myMotor1.forward();
    myMotor2.forward();

    leftEncoder = 0;
    while(leftEncoder <= x){ // here
      calibrate();
    }
}

void loop() {
  // myMotor1.stop();
  // myMotor2.stop();
  
    myMotor1.setSpeed(rightMotor);
    myMotor2.setSpeed(leftMotor);
    myMotor1.forward();
    myMotor2.forward();

  calibrate();
  walls();
  
  if (LEFTWALL && RIGHTWALL && FRONTWALL) {
    turnLeft();
    turnLeft();
    moveForward(130);
  }
  else if (LEFTWALL && !RIGHTWALL && FRONTWALL) {
    turnRight();
    moveForward();
   
  }
  else if(!LEFTWALL && RIGHTWALL && FRONTWALL){
    delay(200);
    turnLeft();

    moveForward();
  }
  else if(!LEFTWALL && RIGHTWALL && !FRONTWALL){
    delay(600);
    turnLeft();

    moveForward();
  }
  else if(!LEFTWALL && !RIGHTWALL && FRONTWALL){
    delay(500);
    turnLeft();

    moveForward();
  }
  else if (!LEFTWALL && !RIGHTWALL && !FRONTWALL) {
    delay(200);
    turnLeft();

    moveForward();
  }
}

void uturn() {
  
  myMotor1.stop();
  myMotor2.stop();
  delay(1500);
  myMotor1.setSpeed(140);
  myMotor2.setSpeed(140);
  myMotor1.forward();
  myMotor2.backward();
  rightEncoder = 0;
  while(rightEncoder <= 115);

}

void turnLeft() {
  
  myMotor1.stop();
  myMotor2.stop();
  delay(1500);
  myMotor1.setSpeed(130);
  myMotor2.setSpeed(130);
  myMotor1.forward();
  myMotor2.backward();
  rightEncoder = 0;
  while(rightEncoder <= 60);
  myMotor1.stop();
  myMotor2.stop();
  delay(1000);
}

void turnRight() {
  myMotor1.stop();
  myMotor2.stop();
  delay(1500);
  myMotor1.setSpeed(130);
  myMotor2.setSpeed(130);
  myMotor2.forward();
  myMotor1.backward();
  leftEncoder = 0;
  while(leftEncoder <= 52);
  myMotor1.stop();
  myMotor2.stop();
  delay(1000);
}

void walls() {
  LEFTWALL = digitalRead(leftIr) == 0;
  RIGHTWALL = digitalRead(rightIr) == 0;
  FRONTWALL = digitalRead(frontIr) == 0;
}

float value = 8;

void calibrate() {
  float left = leftHC.dist();
  float right = rightHC.dist();

  if (left <= 3.5) {
    myMotor1.setSpeed(rightMotor);
    myMotor2.setSpeed(leftMotor);
    myMotor1.backward();
    myMotor2.forward();

    while (left <= 3.5) {
      left = leftHC.dist();
    }
  } else if (right <= 3.5) {
    myMotor1.setSpeed(rightMotor);
    myMotor2.setSpeed(leftMotor);
    myMotor1.forward();
    myMotor2.backward();

    while (right <= 3.5) {
      right = rightHC.dist();
    }
  }
  
  else{
    
  myMotor1.setSpeed(rightMotor);
  myMotor2.setSpeed(leftMotor);
  myMotor1.forward();
  myMotor2.forward();
  }
}

