/* Riceve messaggi che vengono inviati via Bluetooth HC-05 da una app. 
*/

#include <SoftwareSerial.h>

const int RXPin = 3;  // da collegare su TX di HC05
const int TXPin = 2;  // da collegare su RX di HC05
const int Red = 10;
const int Green = 9;
const int Blue = 8; 
const int ritardo = 10;
String red="";
String green="";
String blue="";
int redV;
int blueV;
int greenV;
 char* ptr;

String messaggio="";

//creo una nuova porta seriale via software
SoftwareSerial myBT = SoftwareSerial(RXPin, TXPin);
  
char msgChar;
  
void setup()
{
  pinMode(RXPin, INPUT);
  pinMode(TXPin, OUTPUT);
  pinMode(Red, OUTPUT);
  pinMode(Blue, OUTPUT);
  pinMode(Green, OUTPUT);
   
  myBT.begin(38400);
  Serial.begin(9600); 
  Serial.print("Void Setup");
}

void loop() 
{
  while(myBT.available()){
    msgChar = char(myBT.read());
    //Serial.print(msgChar);
    if(msgChar!= '|')
    {
       messaggio=messaggio+msgChar;
    }else{
     int i =0;
     while(messaggio.charAt(i)!='-')
     {
      red=red+messaggio.charAt(i);
      i++;
     }
      i++;
       while(messaggio.charAt(i)!='-')
     {
      green=green+messaggio.charAt(i);
      i++;
     }
     i++;
       while(i<messaggio.length())
     {
      blue=blue+messaggio.charAt(i);
      i++;
     }
    //   Serial.println("red: " + red);
 //  Serial.println("green: " + green);
  //  Serial.println("blue: " + blue);
     redV=red.toInt();
    greenV=green.toInt();
    blueV=blue.toInt();
    analogWrite(Red, redV);
    analogWrite(Green, greenV);
    analogWrite(Blue, blueV);
    red="";
    green="";
    blue="";
    messaggio="";

    }
  }

  delay(ritardo); //delay                
}
