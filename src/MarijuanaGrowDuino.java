import processing.core.*;
import processing.serial.*;
import cc.arduino.*;

@SuppressWarnings({ "serial", "unused" }) // REMOVE
public class MarijuanaGrowDuino extends PApplet{
	
	PFont title;
	PFont buttons;

	//Create Lamps
	Lamp fluorescentLight = new Lamp(13);
	
	//Create Fans
	Fan smallFan = new Fan(12);
	
	//Create WaterPumps
	WaterPump pump1 = new WaterPump(9);
	
	

	static Arduino arduino;

	 public void setup() {
	  size(500, 400);
	  background(0);
	  title = loadFont("Amienne-Bold-34.vlw");         // Title Font
	  buttons = loadFont("Amienne-Bold-29.vlw");       // Button Font

	  rect(325,60,100,50);                           //USED FOR DEBUG

	  

	  println(Arduino.list());                                 //Prints Open Serial Ports Can be removed Later
	  arduino = new Arduino(this, Arduino.list()[1], 115200);  // Sets connection and speed

	  // Setting relay pins to output
	  fluorescentLight.isOutput();
	  smallFan.isOutput();
	  pump1.isOutput();

	 
	  //TITLE
	  textFont(title, 34);
	  fill(0, 255, 0);
	  text("Marijuana Grow-Duino", 155, 20);
	  text("Control Board", 181, 45);
	  //text(hour() + ":" + minute() + ":" + second(), 100, 55);

	  //Labels
	  textFont(buttons, 29);
	  fill(255);
	  textAlign(RIGHT);
	  text("Fan :", 100, 80);
	  text("Lights :", 100, 105);
	  text("Water Pump :", 100, 130);
	  textAlign(CENTER);
	  text("Temperature F", 85, 165);

	  //noLoop();
	}

	public void draw() {
	  textMode(SCREEN);
	  varStatus();          // Updates the status of the fan, lights, and water pump in the UI

	}

	// Updates the status of the fan, lights, and water pump in the UI
	void varStatus(){
			
	  textAlign(LEFT);
	  //Clear to make letters clearer after Redraw
	  fill(0,0,0);
	  for(int i = 0; i != 10; i++){
	    text("ON", 105, 80);
	    text("OFF", 105,80);
	    text("ON", 105, 105);
	    text("OFF", 105,105);
	    text("ON", 105, 130);
	    text("OFF", 105, 130);
	    rect(45, 167, 80, 20);
	  }
	  //Update UI
	  if(smallFan.isOn() == true){
	    fill(255,255,0);
	    text("ON", 105, 80);
	  }
	  else if(smallFan.isOn() == false){
	    fill(255,0,0);
	    text("OFF", 105, 80);
	  }

	  if(fluorescentLight.isOn() == true){
	    fill(255,255,0);
	    text("ON", 105, 105);
	  }
	  else if(fluorescentLight.isOn() == false){
	    fill(255,0,0);
	    text("OFF", 105, 105);
	  }

	  if(pump1.isOn() == true){
	    fill(255,255,0);
	    text("ON", 105, 130);
	  }
	  else if(pump1.isOn() == false){
	    fill(255,0,0);
	    text("OFF", 105, 130);
	  }
	  textAlign(CENTER);
	  text("test", 85, 185);
	
	}


	public void mousePressed() {

	  //Create Button
	  for(int x = 100; x != 0; x-- ){
	    for(int y = 50; y != 0; y--){
	      if(mouseX == 325 + x && mouseY == 60 + y)
	    	  fluorescentLight.setOn(true);
	      }
	  }
	  redraw();
	}

	public void keyPressed() {
	  if(key == 'R'){
		  fluorescentLight.setOn(false);
	    redraw();
	  }
	  else if(key == 'Q'){
	   
	    redraw();
	  }
	  else if(key == 'W'){
	   
	    redraw();
	  }
	}

/*
 * // These are commands we need to send to HSHT15 to control it
String gTempCmd = 0b00000011;	//int gTempCmd  = 0b00000011; original
String gHumidCmd = 0b00000101;	//int gHumidCmd = 0b00000101; original
  
int shiftIn(int dataPin, int clockPin, int numBits)
{
   int ret = 0;
   int i;

   for (i=0; i<numBits; ++i)
   {
      digitalWrite(clockPin, HIGH);
      delay(10);  // I don't know why I need this, but without it I don't get my 8 lsb of temp
      ret = ret*2 + digitalRead(dataPin);
      digitalWrite(clockPin, LOW);
   }

   return(ret);
}

void sendCommandSHT(String command, int dataPin, int clockPin)
{
  int ack;

  // Transmission Start
  pinMode(dataPin, OUTPUT);
  pinMode(clockPin, OUTPUT);
  digitalWrite(dataPin, HIGH);
  digitalWrite(clockPin, HIGH);
  digitalWrite(dataPin, LOW);
  digitalWrite(clockPin, LOW);
  digitalWrite(clockPin, HIGH);
  digitalWrite(dataPin, HIGH);
  digitalWrite(clockPin, LOW);
           
  // The command (3 msb are address and must be 000, and last 5 bits are command)
  shiftOut(dataPin, clockPin, MSBFIRST, command);

  // Verify we get the coorect ack
  digitalWrite(clockPin, HIGH);
  pinMode(dataPin, INPUT);
  ack = digitalRead(dataPin);
  if (ack != LOW)
    Serial.println("Ack Error 0");
  digitalWrite(clockPin, LOW);
  ack = digitalRead(dataPin);
  if (ack != HIGH)
     Serial.println("Ack Error 1");
}

void waitForResultSHT(int dataPin)
{
  int i;
  int ack;
  
  pinMode(dataPin, INPUT);
  
  for(i= 0; i < 100; ++i)
  {
    delay(10);
    ack = digitalRead(dataPin);

    if (ack == LOW)
      break;
  }
  
  if (ack == HIGH)
    Serial.println("Ack Error 2");
}

int getData16SHT(int dataPin, int clockPin)
{
  int val;
  
  // Get the most significant bits
  pinMode(dataPin, INPUT);
  pinMode(clockPin, OUTPUT);
  val = shiftIn(dataPin, clockPin, 8);
  val *= 256;

  // Send the required ack
  pinMode(dataPin, OUTPUT);
  digitalWrite(dataPin, HIGH);
  digitalWrite(dataPin, LOW);
  digitalWrite(clockPin, HIGH);
  digitalWrite(clockPin, LOW);
           
  // Get the lest significant bits
  pinMode(dataPin, INPUT);
  val |= shiftIn(dataPin, clockPin, 8);

  return val;
}

void skipCrcSHT(int dataPin, int clockPin)
{
  // Skip acknowledge to end trans (no CRC)
  pinMode(dataPin, OUTPUT);
  pinMode(clockPin, OUTPUT);

  digitalWrite(dataPin, HIGH);
  digitalWrite(clockPin, HIGH);
  digitalWrite(clockPin, LOW);
}



  int theDataPin  = 10;
  int theClockPin = 11;
  char cmd = 0;
  int ack;
  
public class temp(){

           int val;
           int temp;
           
           sendCommandSHT(gTempCmd, theDataPin, theClockPin);
           waitForResultSHT(theDataPin);
           val = getData16SHT(theDataPin, theClockPin);
           skipCrcSHT(theDataPin, theClockPin);
           temp = -40.0 + 0.018 * (float)val;
           return temp;
           
         }
         
public class humid(){
           int val;
           int humid;
           
           sendCommandSHT(gHumidCmd, theDataPin, theClockPin);
           waitForResultSHT(theDataPin);
           val = getData16SHT(theDataPin, theClockPin);
           skipCrcSHT(theDataPin, theClockPin);
           humid = -4.0 + 0.0405 * val + -0.0000028 * val * val;
           return humid;
         }      
         
         
   


 * */



}
