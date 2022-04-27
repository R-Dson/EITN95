public class GlobalSimulation {
    // EVENTS!
    public static final int MEASURE = 1;
    public static final int ARRIVAL = 2;
    public static final int SERVED = 3;

    public static double time = 0; // The global time variable
    public static EventListClass eventList = new EventListClass(); // The event list used in the program

    public static void insertEvent(int type, double TimeOfEvent) { // Just to be able to skip dot notation
        eventList.InsertEvent(type, TimeOfEvent);
    }

    public static void reset() {
        time = 0;
        eventList = new EventListClass();
    }
}