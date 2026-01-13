/*
 * TODO: arun
 * TODO: 1/12
 * TODO: 4
 * TODO: Implement the train station program to read from a file, manage train cars using stacks and queues
 */

import java.util.Scanner;
import java.io.File;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

public class MyProgram {

    // Used to generate unique auto engine IDs
    public static int val = 0;

    public static void main(String[] args) {

        int limitTrackA = 100000, limitTrackB = 100000, limitTrackC = 100000;
        int overweightLimit = 40000;

        Stack<Train> trackA = new Stack<>();
        Stack<Train> trackB = new Stack<>();
        Stack<Train> trackC = new Stack<>();

        Queue<Train> otherDest = new LinkedList<>();
        Queue<Train> overweightQ = new LinkedList<>();

        // Use try-with-resources so the scanner always closes correctly
        try (Scanner x = new Scanner(new File("HelloWorldProject/src/data.txt"))) {

            while (x.hasNextLine()) {

                String line = x.nextLine();

                if (line.length() == 0) {
                    continue;
                }

                if (line.equals("END")) {
                    break;
                }

                if (line.startsWith("CAR")) {

                    String carID = line;

                    if (!x.hasNextLine());
                    String contents = x.nextLine();

                    if (!x.hasNextLine()) ;
                    String originCity = x.nextLine();

                    if (!x.hasNextLine());
                    String destCity = x.nextLine();

                    if (!x.hasNextLine());
                    int weight = Integer.parseInt(x.nextLine());

                    if (!x.hasNextLine());
                    int miles = Integer.parseInt(x.nextLine());

                    Train car = new Train(carID, contents, originCity, destCity, weight, miles);

                    if (car.getWeight() > overweightLimit) {
                        overweightQ.add(car);
                    } else if (destCity.equals("Baltimore")) {
                        addCarToTrackWithLimit(car, trackA, limitTrackA, "Baltimore");
                    } else if (destCity.equals("Charlotte")) {
                        addCarToTrackWithLimit(car, trackB, limitTrackB, "Charlotte");
                    } else if (destCity.equals("Trenton")) {
                        addCarToTrackWithLimit(car, trackC, limitTrackC, "Trenton");
                    } else {
                        otherDest.add(car);
                    }

                } else if (line.startsWith("ENG")) {

                    String engineID = line;

                    if (!x.hasNextLine());
                    String city = x.nextLine();

                    if (city.equals("Baltimore")) {
                        sendEngine(engineID, "Baltimore", trackA);
                    } else if (city.equals("Charlotte")) {
                        sendEngine(engineID, "Charlotte", trackB);
                    } else if (city.equals("Trenton")) {
                        sendEngine(engineID, "Trenton", trackC);
                    } else {
                        // If engine city is unknown, you can ignore or print a message
                        System.out.println(engineID + " has unknown destination city: " + city);
                    }
                }
            }

            // Print remaining cars at the station
            printStationStatus(trackA, trackB, trackC, otherDest, overweightQ);

            // Auto-send remaining stacks
            if (!trackA.isEmpty()) {
                sendEngine(nextAutoEngineId(), "Baltimore", trackA);
            }
            if (!trackB.isEmpty()) {
                sendEngine(nextAutoEngineId(), "Charlotte", trackB);
            }
            if (!trackC.isEmpty()) {
                sendEngine(nextAutoEngineId(), "Trenton", trackC);
            }

        } catch (Exception e) {
            System.out.println("Error reading file or processing data:");
            e.printStackTrace();
        }
    }

    private static int nextAutoEngineId() {
        
        return "ENG00000";
    }

    private static void addCarToTrackWithLimit(Train car, Stack<Train> track, int limit, String cityName) {

        int currentWeight = getTotalWeight(track);

        // If adding this car would exceed the limit, dispatch current cars first
        if (currentWeight + car.getWeight() > limit && !track.isEmpty()) {
            sendEngine(nextAutoEngineId(), cityName, track);
        }

        track.push(car);
    }

    private static int getTotalWeight(Stack<Train> track) {
        int sum = 0;
        for (Train t : track) {
            sum += t.getWeight();
        }
        return sum;
    }

    private static void sendEngine(String engineID, String cityName, Stack<Train> track) {

        System.out.println(engineID + " leaving for " + cityName + " with the following cars:");

        while (!track.isEmpty()) {
            Train car = track.pop();
            System.out.println(car.toString());
        }
    }

    private static void printStationStatus(Stack<Train> trackA, Stack<Train> trackB, Stack<Train> trackC,
                                           Queue<Train> otherDest, Queue<Train> overweightQ) {

        System.out.println("Baltimore");
        printStack(trackA);

        System.out.println("Charlotte");
        printStack(trackB);

        System.out.println("Trenton");
        printStack(trackC);

        System.out.println("Other Destinations");
        printQueue(otherDest);

        System.out.println("Overweight");
        printQueue(overweightQ);
    }

    private static void printStack(Stack<Train> track) {
        if (track.isEmpty()) {
            System.out.println("[empty]");
        } else {
            for (Train t : track) {
                System.out.println(t.getCarID() + " - " + t.getWeight());
            }
        }
    }

    private static void printQueue(Queue<Train> q) {
        if (q.isEmpty()) {
            System.out.println("[empty]");
        } else {
            for (Train t : q) {
                System.out.println(t.getCarID() + " - " + t.getWeight());
            }
        }
    } // end of MyProgram class
} // end of MyProgram class
