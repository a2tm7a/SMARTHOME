#define trigPin 12
#define echoPin 13

#include<EEPROM.h> 
int count = 0;
long d = 0;
int addr = 0;

void setup() {
  Serial.begin (9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
}

long dist_read() {
  long duration, dist;
  digitalWrite(trigPin, LOW);  // Added this line
  delayMicroseconds(2); // Added this line
  digitalWrite(trigPin, HIGH);
//  delayMicroseconds(1000); - Removed this line
  delayMicroseconds(10); // Added this line
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  dist = (duration/2) / 29.1;
//    Serial.print(dist);
//    Serial.print(" cm");
  delay(30);
  return dist;
}

long get_dist()
{
  long sum = 0;
  for(int i = 0; i < 5; i++)
  {
    sum += dist_read();
  }
  return sum/5;
}

void loop()
{
  d = dist_read();
  Serial.print("Distance = ");
  Serial.print(d);
 if(dist_read() < 40)
 { while(dist_read() < 40);
  
  if(dist_read() > 40)count++;
 }
   Serial.print("  ");
   Serial.println(count);
}
