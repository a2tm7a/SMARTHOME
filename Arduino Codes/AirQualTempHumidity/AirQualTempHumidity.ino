#include <dht11.h>

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
  
}

float sum=0, readingLM35=0, tempLM35=0;

void loop()
{
  Serial.println("\n");

  int chk = DHT11.read(DHT11PIN);
  readingLM35 = analogRead(LM35Pin);
  sum=0;
  for(int i = 0; i < 1000; i++)
  {
    sum += readingLM35;
  }
  tempLM35 = (sum/1000)*500/1024;
  sensorValue = analogRead(airQPin);

  //Serial.print("Read sensor: ");
  switch (chk)
  {
    case DHTLIB_OK: 
    //Serial.println("OK"); 
    break;
    case DHTLIB_ERROR_CHECKSUM: 
    Serial.println("Checksum error"); 
    break;
    case DHTLIB_ERROR_TIMEOUT: 
    Serial.println("Time out error"); 
    break;
    default: 
    Serial.println("Unknown error"); 
    break;
  }

  Serial.print("Humidity (%): ");
  Serial.println((float)DHT11.humidity, 2);

  Serial.print("DHT11 temperature (°C): ");
  Serial.println((float)DHT11.temperature, 2);
//
//  if((float)DHT11.temperature >= 60){
//    Serial.println("Garmi ka pro level -> infinity");
//  }
//  if((float)DHT11.temperature <= 20){
//    Serial.println("Thand ka pro level -> infinity");
//  }
//
//  Serial.print("Temperature (°F): ");
//  Serial.println(Fahrenheit(DHT11.temperature), 2);
//
//  Serial.print("Temperature (°K): ");
//  Serial.println(Kelvin(DHT11.temperature), 2);
//
//  Serial.print("Dew Point (°C): ");
//  Serial.println(dewPoint(DHT11.temperature, DHT11.humidity));
//
//  Serial.print("Dew PointFast (°C): ");
//  Serial.println(dewPointFast(DHT11.temperature, DHT11.humidity));

  Serial.print("LM35 Temperature (°C): ");
  Serial.println(tempLM35);

  Serial.print("LM35 reading: ");
  Serial.println(readingLM35);
  
  Serial.print(sensorValue, DEC);  // prints the value read
  Serial.println(" ppm CO2\n\n");
  if (sensorValue > 500) {
    // Activate digital output pin 8 - the LED will light up
    digitalWrite(led, HIGH);
  }
  else {
    // Deactivate digital output pin 8 - the LED will not light up
    digitalWrite(led, LOW);
  }

  if(tempLM35>50)   digitalWrite(alarm, HIGH);
  if (tempLM35<=30) digitalWrite(alarm, LOW);
  delay(2000);
}
//
// END OF FILE 1
//
