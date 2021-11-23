#include <SPI.h>
#include <MFRC522.h>
#define RST_PIN 5
#define SS_PIN 53

MFRC522 mfrc(SS_PIN, RST_PIN);

int relay =4;
int trigPin = 9;
int echoPin =8;
int count=0;

String cmd = "";
String mode ="f";

int check[10][4]={
  {163,3,125,26},
  {76,76,4,73},
  {211,44,134,26}
};

void setup() {
  Serial.begin(9600);
  SPI.begin();
  pinMode(relay,OUTPUT);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(2,OUTPUT);
  Serial1.begin(9600);
  mfrc.PCD_Init();
}
//최종
void loop() {
  float duration, distance;
  digitalWrite(trigPin, HIGH);
  delay(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  distance=((float)(340*duration)/10000)/2;
  
  if(distance>5){
    Serial.println("오픈된 상태");
    count++;
    delay(100);
    if(count>=5&&count<=10){
      tone(3,532,500);
      delay(800);
      }
   }
  if(distance<=5){ //닫힘, 오픈됐으나 문을 열지 않으면 자동으로 닫힘
    Serial.println("잠김");
 //   delay(5000);
    if(!count==0){
      delay(2000);
      tone(3,784,300);
      delay(300);
      tone(3,784,300);
      delay(300);
      digitalWrite(relay,HIGH);
      
      count=count*0;
    }
    delay(5);
  }
  if (Serial1.available()) {
    Serial.println("스마트폰에서 키트로");
    cmd=(char)Serial1.read();
    Serial.println(cmd[0]);
    mode = cmd[0];
    cmd = "";
    if(mode=="a"){
      Serial.println("오픈");
        tone(3,784,300);
        delay(300);
        tone(3,784,300);
        delay(300);
        digitalWrite(relay,LOW);
        delay(8000); //솔레노이드 잠금 해제 이후 5초 이내에 열지 않으면 잠김
        count=-1;
    }
    if(mode=="b"){
      Serial.println("잠김");
        tone(3,784,300);
        delay(300);
        tone(3,784,300);
        delay(300);
        digitalWrite(relay,HIGH);
        delay(1000);
        count=count*0;
    }
  }
  
  if(!mfrc.PICC_IsNewCardPresent()||!mfrc.PICC_ReadCardSerial()){
    Serial.println("카드를 대시오");
    delay(50); 
    return;
  }
  
  
  for(int i=0;i<=9;i++){//카드번호 일치하면 오픈
    if(mfrc.uid.uidByte[0] ==check[i][0] && mfrc.uid.uidByte[1]== check[i][1]
    && mfrc.uid.uidByte[2] ==check[i][2] && mfrc.uid.uidByte[3] ==check[i][3]){
      Serial.println("오픈");
      tone(3,784,300);
      delay(300);
      tone(3,784,300);
      delay(300);
      digitalWrite(relay,LOW);
      delay(8000); //솔레노이드 잠금 해제 이후 5초 이내에 열지 않으면 잠김
      count=-1;
      }
      } 
}
