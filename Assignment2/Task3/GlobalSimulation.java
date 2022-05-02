public class GlobalSimulation{

	public static final int MONTH = 1, SPECIAL = 2; 
	public static double time = 0; 
	public static EventListClass eventList = new EventListClass(); 
	public static void insertEvent(int type, double TimeOfEvent){  
		eventList.InsertEvent(type, TimeOfEvent);
	}
}