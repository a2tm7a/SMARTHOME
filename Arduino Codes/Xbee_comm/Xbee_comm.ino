#include<SoftwareSerial.h>

SoftwareSerial mySerial(2,3); //rx, tx

void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
}

void loop() {
  if(Serial.available())
  {
    mySerial.print((char)Serial.read());
  }
  
  if(mySerial.available())
  {
    int temp = mySerial.read();
    if(temp == 13) Serial.println();
    Serial.print((char)temp);
  }
}
