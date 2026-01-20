/*
 * TODO: arun
 * TODO: 1/12
 * TODO: 4
 * MyProgram.java does the main logic for reading input, routing the train cars, handles the engines, and printing the status's of each.
 */
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;
import java.io.File;


public class MyProgram {

    
    private static int autoEngineCounter = 0;

    
    private interface Event {}

    private static class CarEvent implements Event {
        Train car;
        CarEvent(Train car) { this.car = car; }
    }

    private static class EngineEvent implements Event {
        String engineID;
        String city;
        EngineEvent(String engineID, String city) {
            this.engineID = engineID;
            this.city = city;
        }
    }

    public static void main(String[] args) {

        
        int limitTrackA = 100000; // trenton
        int limitTrackB = 100000; // charlotte
        int limitTrackC = 100000; // baltimore

        int overweightLimit = 40000; 
        int maintenanceMiles = 700;

        // Tracks
        Queue<Event> track0 = new LinkedList<>();       
        Queue<Train> maintenanceQ = new LinkedList<>(); 

        Stack<Train> trackA = new Stack<>(); // trenton
        Stack<Train> trackB = new Stack<>(); // charlotte
        Stack<Train> trackC = new Stack<>(); // baltimore

        Queue<Train> otherDest = new LinkedList<>();    
        Queue<Train> overweightQ = new LinkedList<>();  

        
        try (Scanner x = new Scanner(new File("HelloWorldProject/src/data.txt"))) {

            while (x.hasNextLine()) {
                String line = x.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.equals("END")) break;

                if (line.startsWith("CAR")) {
                    String carID = line;

                    if (!x.hasNextLine()) break;
                    String contents = x.nextLine().trim();

                    if (!x.hasNextLine()) break;
                    String originCity = x.nextLine().trim();

                    if (!x.hasNextLine()) break;
                    String destCity = x.nextLine().trim();

                    if (!x.hasNextLine()) break;
                    int weight = Integer.parseInt(x.nextLine().trim());

                    if (!x.hasNextLine()) break;
                    int miles = Integer.parseInt(x.nextLine().trim());

                    Train car = new Train(carID, contents, originCity, destCity, weight, miles);
                    track0.add(new CarEvent(car));
                }
                else if (line.startsWith("ENG")) {
                    String engineID = line;

                    if (!x.hasNextLine()) break;
                    String city = x.nextLine().trim();

                    track0.add(new EngineEvent(engineID, city));
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading file:");
            e.printStackTrace();
            return;
        }

        
        while (!track0.isEmpty()) {
            Event ev = track0.remove();

            if (ev instanceof CarEvent) {
                Train car = ((CarEvent) ev).car;

                
                if (car.getMilesTraveled() > maintenanceMiles) {
                    maintenanceQ.add(car);
                    continue;
                }

                // overweight checking
                if (car.getWeight() > overweightLimit) {
                    overweightQ.add(car);
                    continue;
                }

                routeCarToDestinationTracks(car, trackA, trackB, trackC, otherDest,
                        limitTrackA, limitTrackB, limitTrackC);

            } else if (ev instanceof EngineEvent) {
                EngineEvent eng = (EngineEvent) ev;
                departByEngineSignal(eng.engineID, eng.city, trackA, trackB, trackC);
            }
        }

        
        while (!maintenanceQ.isEmpty()) {
            Train car = maintenanceQ.remove();
            car.setMilesTraveled(100); 

            
            if (car.getWeight() > overweightLimit) {
                overweightQ.add(car);
                continue;
            }

            routeCarToDestinationTracks(car, trackA, trackB, trackC, otherDest,
                    limitTrackA, limitTrackB, limitTrackC);
        }

        
        printStationStatus(trackA, trackB, trackC, otherDest, overweightQ);

        
        if (!trackA.isEmpty()) sendEngine(nextAutoEngineId(), "Trenton", trackA);
        if (!trackB.isEmpty()) sendEngine(nextAutoEngineId(), "Charlotte", trackB);
        if (!trackC.isEmpty()) sendEngine(nextAutoEngineId(), "Baltimore", trackC);
    }

    

    private static void routeCarToDestinationTracks(
            Train car,
            Stack<Train> trackA, Stack<Train> trackB, Stack<Train> trackC,
            Queue<Train> otherDest,
            int limitA, int limitB, int limitC) {

        String dest = car.getDestCity();

        if (dest.equals("Trenton")) {
            addCarToTrackWithLimit(car, trackA, limitA, "Trenton");
        } else if (dest.equals("Charlotte")) {
            addCarToTrackWithLimit(car, trackB, limitB, "Charlotte");
        } else if (dest.equals("Baltimore")) {
            addCarToTrackWithLimit(car, trackC, limitC, "Baltimore");
        } else {
            otherDest.add(car);
        }
    }

    private static void departByEngineSignal(String engineID, String city,
                                            Stack<Train> trackA, Stack<Train> trackB, Stack<Train> trackC) {

        if (city.equals("Trenton")) {
            sendEngine(engineID, "Trenton", trackA);
        } else if (city.equals("Charlotte")) {
            sendEngine(engineID, "Charlotte", trackB);
        } else if (city.equals("Baltimore")) {
            sendEngine(engineID, "Baltimore", trackC);
        } else {
            System.out.println(engineID + " has unknown destination city: " + city);
        }
    }

    private static String nextAutoEngineId() {
        String id = "ENG0000" + autoEngineCounter;
        autoEngineCounter++;
        return id;
    }
    private static void addCarToTrackWithLimit(Train car, Stack<Train> track, int limit, String cityName) {

        int currentWeight = getTotalWeight(track);

        
        if (!track.isEmpty() && currentWeight + car.getWeight() > limit) {
            sendEngine(nextAutoEngineId(), cityName, track);
        }

        track.push(car);
    }

    private static int getTotalWeight(Stack<Train> track) {
        int sum = 0;
        for (Train t : track) sum += t.getWeight();
        return sum;
    }

    private static void sendEngine(String engineID, String cityName, Stack<Train> track) {
        System.out.println(engineID + " leaving for " + cityName + " with the following cars:");

        while (!track.isEmpty()) {
            Train car = track.pop();
            System.out.println(car);
        }
        System.out.println();
    }

    private static void printStationStatus(Stack<Train> trackA, Stack<Train> trackB, Stack<Train> trackC,
                                           Queue<Train> otherDest, Queue<Train> overweightQ) {

        System.out.println("Station (before the final departures)");

        System.out.println("trenton");
        
        printStack(trackA);

        System.out.println("Charlotte");
        
        printStack(trackB);

        System.out.println("Baltimore");
        
        printStack(trackC);

        System.out.println("Other destinations");
        
        printQueue(otherDest);

        System.out.println("Overweight");
        
        printQueue(overweightQ);

        
        System.out.println();
    }

    private static void printStack(Stack<Train> track) {
        if (track.isEmpty()) {
            System.out.println("none");
        } else {
            for (Train t : track) {
                System.out.println(t.getCarID() + " - " + t.getWeight());
            }
        }
        System.out.println();
    }

    private static void printQueue(Queue<Train> q) {
        if (q.isEmpty()) {
            System.out.println("none");
        } else {
            for (Train t : q) {
                System.out.println(t.getCarID() + " - " + t.getWeight());
            }
        }
        System.out.println();
    }
} // end class