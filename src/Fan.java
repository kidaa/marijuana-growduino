import cc.arduino.Arduino;

public class Fan {
		private int fanPin;
		private boolean onOff; //True on; False off
		private boolean output; // output or input?

		
		public Fan(int fanPin) {
			setPin(fanPin);
						
		}
		public void setPin(int fanPin) {
			this.fanPin = fanPin;
		}
		public int getPin() {
			return fanPin;
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



