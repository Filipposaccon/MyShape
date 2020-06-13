#include <SoftwareSerial.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);  //Inizializzo il display
const int RXPin = 3;  //imposto pin ricevitore
const int TXPin = 2; //imposto pin trasmettitore
const int Red = 10; //pin Red
const int Green = 9; //pin Green
const int Blue = 8; //pin Blue
const int ritardo = 10; //delay 
String red = ""; 
String green = "";
String blue = "";
int redV;
int blueV;
int greenV;

String messaggio = ""; //string per il messaggio

//creo una nuova porta seriale via software
SoftwareSerial myBT = SoftwareSerial(RXPin, TXPin);//inzializzo i pin del modulo bluethoot

char msgChar;

void setup()
{
  pinMode(RXPin, INPUT);
  pinMode(TXPin, OUTPUT);
  pinMode(Red, OUTPUT);
  pinMode(Blue, OUTPUT);
  pinMode(Green, OUTPUT);
  lcd.begin(20, 4);// inizializzo il display
  lcd.setCursor(5, 0);//imposto il cursore
  lcd.print("Colori RGB");//scrivo a display
  myBT.begin(38400);
  Serial.begin(9600);
  Serial.print("Void Setup");
}

void loop()
{
  while (myBT.available()) {//Entro se ci sono messaggi disponibili
    msgChar = char(myBT.read());//leggo il contenuto del buffer
    if (msgChar != '|')//inizio la procedura di splitting
    {
      messaggio = messaggio + msgChar;
    } else {
      int i = 0;
      while (messaggio.charAt(i) != '-')
      {
        red = red + messaggio.charAt(i);
        i++;
      }
      i++;
      while (messaggio.charAt(i) != '-')
      {
        green = green + messaggio.charAt(i);
        i++;
      }
      i++;
      while (i < messaggio.length())
      {
        blue = blue + messaggio.charAt(i);
        i++;
      }
      lcd.setCursor(0, 1);//imposto il cursore
      lcd.print("Red: "+ red);//scrivo a display
      lcd.setCursor(0, 2);//imposto il cursore
      lcd.print("Green: "+green);//scrivo a display
      lcd.setCursor(0, 3);//imposto il cursore
      lcd.print("Blue: "+blue);//scrivo a display
      redV = red.toInt();//converto in int i codici dei colori per settarli sul led
      greenV = green.toInt();
      blueV = blue.toInt();
      analogWrite(Red, redV);//imposto i colori sul led
      analogWrite(Green, greenV);
      analogWrite(Blue, blueV);
      red = "";//ripulisco le variabili dai vecchi valori
      green = "";
      blue = "";
      messaggio = "";

    }
  }

  delay(ritardo); //delay
}
