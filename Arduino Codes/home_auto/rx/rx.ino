#include <SoftwareSerial.h>
SoftwareSerial mySerial(2,3);  //rx, tx

void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
}

void loop() 
{
  //if(mySerial.available())
  {
    Serial.println((char)01011);
  }
}
