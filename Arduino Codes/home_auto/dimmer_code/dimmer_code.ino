void setup() {
  Serial.begin(9600);
  pinMode(12, INPUT);
}

void loop() {
  Serial.println(analogRead(A0));
}
