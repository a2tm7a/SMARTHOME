unsigned long long timer;
char ch;
int spd = 0;

void setup() 
{
  Serial.begin(9600);
  attachInterrupt( 0, dimmer, CHANGE);
  digitalWrite(12, LOW);
}

void loop() 
{
  if( Serial.available())
  {
    ch = Serial.read();

    switch (ch)
    {
      case '5':
      digitalWrite(12, HIGH);
      Serial.println("5");
      spd = 5;
      break;

      case '3':
      spd = 3;
      Serial.println("3");
      break;

      case '0':
      digitalWrite(12, LOW);
      spd = 0;
      Serial.println("0");
      break;
    }
  }

  if(spd == 3)
  {
    if(millis() > timer + 6) digitalWrite(12,HIGH);
  }
}

void dimmer()
{
  digitalWrite(12, LOW);
  timer = millis();
}


