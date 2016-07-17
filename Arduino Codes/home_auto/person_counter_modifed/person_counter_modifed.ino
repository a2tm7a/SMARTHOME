#define trigPin1 13
#define echoPin1 12
#define trigPin2 9
#define echoPin2 8

#include <EEPROM.h>

int count = 0;
long d = 0;
int addr = 0;

bool pass1()
{
  if(dist_read1() < 40)
 { while(dist_read1() < 40);
  
  if(dist_read1() > 40)return 1;
 }
 else return 0;
}

bool pass2()
{
  if(dist_read2() < 40)
 { while(dist_read2() < 40);
  
  if(dist_read2() > 40)return 1;
 }
 else return 0;
}

void setup() {
  Serial.begin (9600);
  pinMode(trigPin1, OUTPUT);
  pinMode(echoPin1, INPUT);
  pinMode(trigPin2, OUTPUT);
  pinMode(echoPin2, INPUT);
}

long dist_read1() {
  long duration, dist;
  digitalWrite(trigPin1, LOW);  // Added this line
  delayMicroseconds(2); // Added this line
  digitalWrite(trigPin1, HIGH);
//  delayMicroseconds(1000); - Removed this line
  delayMicroseconds(10); // Added this line
  digitalWrite(trigPin1, LOW);
  duration = pulseIn(echoPin1, HIGH);
  dist = (duration/2) / 29.1;
//    Serial.print(dist);
//    Serial.print(" cm");
  delay(100);
  return dist;
}

long dist_read2() {
  long duration, dist;
  digitalWrite(trigPin2, LOW);  // Added this line
  delayMicroseconds(2); // Added this line
  digitalWrite(trigPin2, HIGH);
//  delayMicroseconds(1000); - Removed this line
  delayMicroseconds(10); // Added this line
  digitalWrite(trigPin2, LOW);
  duration = pulseIn(echoPin2, HIGH);
  dist = (duration/2) / 29.1;
//    Serial.print(dist);
//    Serial.print(" cm");
  delay(100);
  return dist;
}

//long get_dist()
//{
//  long sum = 0;
//  for(int i = 0; i < 5; i++)
//  {
//    sum += dist_read();
//  }
//  return sum/5;
//}

void loop()
{
//  d = dist_read();
//  Serial.print("Distance = ");
//  Serial.print(d);
 /*if(dist_read() < 40)
 { while(dist_read() < 40);
  
  if(dist_read() > 40)count++;
 }*/
   if(pass1()==1)
   {
     while(pass2()!=1)
     {if(pass1()==1) break;}
     if(pass2()==1)count++;
   }
   
   if(pass2()==1)
   {
     while(pass1()!=1)
     {if(pass2()==1) break;}
     if(pass1()==1) count--;
   }
   
   //Serial.println(count);
   if(addr<513)
   {
     EEPROM.write(addr, count);
     addr++;
   }
}
