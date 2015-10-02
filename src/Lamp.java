import cc.arduino.Arduino;

public class Lamp {
		private int lampPin;
		private boolean onOff; //True on; False off
		private boolean output; // output or input?

		
		public Lamp(int lampPin) {
			setPin(lampPin);
						
		}
		public void setPin(int lampPin) {
			this.lampPin = lampPin;
		}
		public int getPin() {
			return lampPin;
		}
		
		//Set pin HIGH or LOW
		public void setOn(boolean onOff) {
			this.onOff = onOff;
			
			if(this.onOff == true) {
				MarijuanaGrowDuino.arduino.digitalWrite(this.getPin(), Arduino.HIGH);				
				/*Should be sent from class as variable*/	
			}
			else if(this.onOff == false){
				MarijuanaGrowDuino.arduino.digitalWrite(this.getPin(), Arduino.LOW);
			}
		
		}
		public boolean isOn() {
			return onOff;
			
		}
		// Set pin OUTPUT or INPUT
		public void setOutput(boolean output) {
			this.output = output;
			
			if (this.output == true){
				MarijuanaGrowDuino.arduino.pinMode(this.getPin(), Arduino.OUTPUT);
			}
			else if(this.output == false){
				MarijuanaGrowDuino.arduino.pinMode(this.getPin(), Arduino.OUTPUT);
			}
		}
		public boolean isOutput() {
			return output;
		}
		

	}



