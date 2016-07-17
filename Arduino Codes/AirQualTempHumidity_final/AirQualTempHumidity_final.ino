#include <dht11.h>
#include <SoftwareSerial.h>

SoftwareSerial mySerial(2,3); //rx | tx
dht11 DHT11;

#define DHT11PIN 5
#define LM35Pin A0
#define airQPin A3
#define alarm 7

int sensorValue;
int led = 13;

void setup()
{
  pinMode(LM35Pin, INPUT);
  pinMode(led, OUTPUT);
  Serial.begin(9600);
  mySerial.begin(9600);
  pinMode(8,OUTPUT);
  digitalWrite(8,HIGH);
}

float readingLM35=0, tempLM35=0;

void sensor_read()
{
//  Serial.println("\n");

  int chk = DHT11.read(DHT11PIN);
  readingLM35 = analogRead(LM35Pin);
  tempLM35 = readingLM35*500/1024;
  sensorValue = analogRead(airQPin);

  //Serial.print("Read sensor: ");
  switch (chk)
  {
    case DHTLIB_OK: 
    mySerial.println("Y"); //ok 
    break;
    case DHTLIB_ERROR_CHECKSUM: 
    mySerial.println("X"); //Checksum error
    break;
    case DHTLIB_ERROR_TIMEOUT: 
    mySerial.println("W"); //Time out error 
    break;
    default: 
    mySerial.println("V"); //Unknown error 
    break;
  }

  mySerial.println((float)DHT11.humidity, 2);
  mySerial.println("U");
  mySerial.println((float)DHT11.temperature, 2);
  mySerial.println("U");
//  Serial.println(readingLM35);
  
  mySerial.println(sensorValue, DEC);  // prints the value read
//  Serial.println(" ppm CO2\n\n");
//  if (sensorValue > 500) {
//    // Activate digital output pin 8 - the LED will light up
//    digitalWrite(led, HIGH);
//  }
//  else {
//    // Deactivate digital output pin 8 - the LED will not light up
//    digitalWrite(led, LOW);
//  }

}

void loop()
{
  readingLM35 = analogRead(LM35Pin);
  tempLM35 = readingLM35*500/1024;
  
  if(tempLM35>50)   {
    digitalWrite(alarm, HIGH);
    mySerial.println("8");
  }
  if (tempLM35<=30) digitalWrite(alarm, LOW);

  

  if(mySerial.available())
  {
    char ch = mySerial.read();
    Serial.print((char)ch);
    if(ch == 'z')
    {
      sensor_read();
      mySerial.println("Z");
    }
  }
  if(Serial.available())
  {
    mySerial.print((char)Serial.read()); 
  }

  
}






//
