#include<SoftwareSerial.h>

SoftwareSerial mySerial(2,3);  // rx | tx

char ch = 'a';
int flag = 0;

void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
  
  pinMode(0, INPUT);
  pinMode(1, OUTPUT);
  pinMode(8, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode( 2, INPUT);
  
}

void loop() {
  if(mySerial.available())
  {
    ch= mySerial.read();
    Serial.print((char)ch);
    switch (ch){
      case 'a':  //manual mode
      digitalWrite(8, HIGH);
      Serial.println("A");
      mySerial.println("A");
      digitalWrite(9, LOW);
      digitalWrite(10, LOW);
      digitalWrite(11, LOW);
      digitalWrite(12, LOW);
      flag = 0;
      break;
      
     case 'b': //auto mode
     digitalWrite(8, LOW);
     Serial.println("B");
     mySerial.println("B");
     flag = 1;
     break;
      
      case 'c':
      if(flag == 1){  
      digitalWrite(9, HIGH); mySerial.println("C"); }
      break;
      
     case 'd':
     if(flag ==1 ){
     digitalWrite(9, LOW); mySerial.println("D");   }
     break;
     
     case 'e':
      if(flag == 1){  
      digitalWrite(10, HIGH); mySerial.println("E"); }
      break;
      
     case 'f':
     if(flag ==1 ){
     digitalWrite(10, LOW); mySerial.println("F"); }
     break;
     
     case 'g':
      if(flag == 1){  
      digitalWrite(11, HIGH); mySerial.println("G"); }
      break;
      
     case 'h':
     if(flag ==1 ){
     digitalWrite(11, LOW); mySerial.println("H");}
     break;
   
      case 'i':
      if(flag == 1){  
      digitalWrite(12, HIGH); mySerial.println("I"); }
      break;
    
     case 'j':
     if(flag ==1 ){
     digitalWrite(12, LOW); mySerial.println("J"); }
     break;
    }
  }
  if(Serial.available())
  {
    mySerial.print((char)Serial.read());
  }
}
