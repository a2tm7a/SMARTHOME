unsigned long long timer=0;
bool flag = false;
void setup() {
  Serial.begin(9600);
  pinMode(4,OUTPUT);
  pinMode(2, OUTPUT);
  pinMode(3,OUTPUT);
}

void loop() {
  if(Serial.available())
  {
    if(Serial.read() == 'o') 
    {
      digitalWrite(2, HIGH);
      digitalWrite(4, LOW);
      analogWrite(3, 150);
      timer = millis();
    }
    if(Serial.read() == 'p') 
    {
      digitalWrite(2,HIGH);
      digitalWrite(4,HIGH);
      timer = millis() - timer;
      Serial.println((double)timer);
    }
  }
  
}
