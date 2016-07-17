#include<SoftwareSerial.h>

SoftwareSerial mySerial(2,3); // rx | tx

int m1=4; //Close
int m2=2; //Open
int ea=3;
char dir2='x';
char dir='x';// 'c' to close, 'o' to open

//float distance=150; // distance in cm
//float motorRPM=50; // Speed in rpm
//float radius=0.5; //cm
//float curtainTime = (distance*60)/(motorRPM*6.28*radius);//time in seconds
float curtainTime=5; //Time (in seconds) to open or close the curtains

void setup() {
  // put your setup code here, to run once:
  pinMode(m1,OUTPUT);
  pinMode(m2,OUTPUT);
  digitalWrite(m1,LOW);
  digitalWrite(m2,LOW);
  Serial.begin(9600);
  mySerial.begin(9600);
  dir2 = dir;
}

void loop() {
  // put your main code here, to run repeatedly:
 // if(Serial.available() > 0)
 // Jai Shree Ram 
 dir2 = dir;
 if(mySerial.available())
  {
    dir = mySerial.read();
    Serial.println(dir);
  }
  if (dir=='p') //Close
  {
    mySerial.print("P");
    digitalWrite(m1,HIGH);
    digitalWrite(m2,LOW);
    analogWrite(ea,200);
//    Serial.println("Closing...");
    delay(curtainTime*1000);
    digitalWrite(m1,LOW);
    digitalWrite(m2,LOW);
    analogWrite(ea,0);
//    Serial.println("Closed\n\n");
  }
  else if(dir=='o') //Open
  {
    mySerial.print("O");
    digitalWrite(m1,LOW);
    digitalWrite(m2,HIGH);
    analogWrite(ea,200);
//    Serial.println("Opening...");
    delay(curtainTime*1000);
    digitalWrite(m1,LOW);
    digitalWrite(m2,LOW);
    analogWrite(ea,0);
//    Serial.println("Opened\n\n");
  }
}
