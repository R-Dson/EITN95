import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	public double money = 0;
	public int savingrate = 5000;
	public Random slump = new Random(); 

	public void treatEvent(Event x){
		switch (x.eventType){
			case MONTH:
				money = money*1.0221+savingrate;

				insertEvent(MONTH, time+1);
				break;
			case SPECIAL:
				int tmp = slump.nextInt(100);
				if(tmp<10) {
					money = money*0.75;
				}else if(tmp<35) {
					money = money*0.5;
				}else if(tmp<60) {
					money = money*0.4;
				}else {
					money = money*0.9;
				}
				insertEvent(SPECIAL, time+slump.nextInt(96));
				break;
			
		}
	}
	
	
	
}