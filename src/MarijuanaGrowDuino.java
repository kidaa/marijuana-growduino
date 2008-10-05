import processing.core.*;
import processing.serial.*;
import cc.arduino.*;

@SuppressWarnings({ "serial", "unused" }) // REMOVE
public class MarijuanaGrowDuino extends PApplet{


	PFont title;
	PFont buttons;

	int lightTimer = 0;         // The amount of hours the lights are to remain on
	int waterPumpTimer = 0;     // The amount of time the water pump must remain on
	int lightRelayPin = 13;     // Light relay pin       (NOT PERMANENT WILL CHANGE AFTER RELAY IS INSTALLED)
	int fanRelayPin = 12;       // Fan relay pin         (NOT PERMANENT WILL CHANGE AFTER RELAY IS INSTALLED)
	int waterPumpRelayPin = 9;  // Water pump relay pin  (NOT PERMANENT WILL CHANGE AFTER RELAY IS INSTALLED)


	boolean lights = false;                           // ON or OFF read to display status of lights
	boolean waterPumps = false;                       // ON or OFF read to display status of water pumps
	boolean fan = false;                              // ON or OFF read to display status of fan
	boolean floweringState = false;

	Arduino arduino;

	 public void setup() {
	  size(500, 400);
	  background(0);
	  title = loadFont("Amienne-Bold-34.vlw");         // Title Font
	  buttons = loadFont("Amienne-Bold-29.vlw");       // Button Font

	    rect(325,60,100,50);                           //USED FOR DEBUG

	  

	  println(Arduino.list());                                 //Prints Open Serial Ports Can be removed Later
	  arduino = new Arduino(this, Arduino.list()[1], 115200);  // Sets connection and speed

	  // Setting relay pins to output
	  arduino.pinMode(lightRelayPin, Arduino.OUTPUT);
	  arduino.pinMode(fanRelayPin, Arduino.OUTPUT);
	  arduino.pinMode(waterPumpRelayPin, Arduino.OUTPUT);

	 
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
	  clear();              // Used to make text clearer after redraw
	  setFan();             // Fan must be set so that the fan will work (find different way to do this)
	  varStatus();          // Updates the status of the fan, lights, and water pump in the UI

	}

	//Used to make text clearer after redraw
	void clear(){
	  textAlign(LEFT);
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
	}

	// Updates the status of the fan, lights, and water pump in the UI
	void varStatus(){

	  //Status
	  textAlign(LEFT);
	  if(getFan() == true){
	    fill(255,255,0);
	    text("ON", 105, 80);
	  }
	  else if(getFan() == false){
	    fill(255,0,0);
	    text("OFF", 105, 80);
	  }

	  if(getLights() == true){
	    fill(255,255,0);
	    text("ON", 105, 105);
	  }
	  else if(getLights() == false){
	    fill(255,0,0);
	    text("OFF", 105, 105);
	  }

	  if(getWaterPumps() == true){
	    fill(255,255,0);
	    text("ON", 105, 130);
	  }
	  else if(getWaterPumps() == false){
	    fill(255,0,0);
	    text("OFF", 105, 130);
	  }
	  textAlign(CENTER);
	  text("test", 85, 185);
	}


	void setLights(boolean b) {
	  if (b == true){
	    arduino.digitalWrite(lightRelayPin, Arduino.HIGH);
	  }
	  else if (b == false) {
	    arduino.digitalWrite(lightRelayPin, Arduino.LOW);
	  }
	  lights = b;
	}

	void setWaterPumps(boolean b) {
	  waterPumps = b;
	}

	// setFan() Checks the temperature and sets the fan On or Off
	void setFan() {
	/*  if (getTemperatureF() >= 78.0) {
	    fan = true;
	  } 
	  else if (getFan() == true && getTemperatureF() <= 68.0) {
	    fan = false;
	  } */
	}

	/*float getTemperatureF() {
	  temperatureF = (temperatureC * 9.0)/ 5.0 + 32.0;
	  return temperatureF;
	}*/

	void setFloweringState(boolean t) {
	  floweringState = t;
	  if(floweringState == true){
	    setLightTimer(12);
	  }
	  else if (floweringState == false){
	    setLightTimer(18);
	  }

	}

	boolean getLights() {
	  return lights;
	}

	boolean getWaterPumps() {
	  return waterPumps;
	}

	boolean getFan() {
	  return fan;
	}

	// getTemperature() will be used to get the value from the LM35 sensor
	/*float getTemperatureC() {
	  for(int i = 0;i <= 7;i++){ // gets 8 samples of temperature
	    //samples[i] = ( 5.0 * rawTemp * 100.0) / 1024.0;
	   // temperatureC = temperatureC + samples[i];
	    delay(1000);
	  }
	 // temperatureC = temperatureC/8.0;

	  //return temperatureC;
	}*/

	boolean getFloweringState() {
	  return floweringState;
	}

	void setLightTimer(int i) {
	  lightTimer = i;



	}

	int getLightTimer() {
	  return lightTimer; // remaining time???
	}

	public void mousePressed() {

	  
	  for(int x = 100; x != 0; x-- ){
	    for(int y = 50; y != 0; y--){
	      if(mouseX == 325 + x && mouseY == 60 + y)
	        setLights(true);
	    }
	  }
	  redraw();
	}

	public void keyPressed() {
	  if(key == 'R'){
	    setLights(false);
	    redraw();
	  }
	  else if(key == 'Q'){
	   // Arduino.lightit();
	    redraw();
	  }
	  else if(key == 'W'){
	   // Arduino.lightoff();
	    redraw();
	  }
	  
	}



}
