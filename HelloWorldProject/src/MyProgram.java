/*
 * TODO: Name
 * TODO: Date
 * TODO: Class Period
 * TODO: Program Description
 */
import java.util.Scanner;
import java.io.File;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

public class MyProgram {
	public static int val = 0;

	public static void main(String[] args) {

		int limitTrackA = 100000, limitTrackB = 100000, limitTrackC = 100000;
		int overweightLimit = 40000;

		Stack<Train> trackA = new Stack<Train>();
		Stack<Train> trackB = new Stack<Train>();
		Stack<Train> trackC = new Stack<Train>();

		Queue<Train> otherDest = new LinkedList<Train>();
		Queue<Train> overweightQ = new LinkedList<Train>();

		Scanner x = new Scanner(System.in);
		try{
			File f = new File("HelloWorldProject/src/data.txt");
			x = new Scanner (f);

			String name = x.nextLine();
			System.out.println(name);

			while (x.hasNextLine()) {

				String line = x.nextLine().trim();

				if (line.equals("END")) {
					break;
				}

				if (line.startsWith("CAR")) {

					String carID = line;
					String contents = x.nextLine().trim();
					String originCity = x.nextLine().trim();
					String destCity = x.nextLine().trim();
					int weight = Integer.parseInt(x.nextLine().trim());
					int miles = Integer.parseInt(x.nextLine().trim());

					Train car = new Train(carID, contents, originCity, destCity, weight, miles);

					if (car.getWeight() > overweightLimit) {
						overweightQ.add(car);
					}
					else if (destCity.equals("Baltimore")) {
						addCarToTrackWithLimit(car, trackA, limitTrackA, "Baltimore");
					}
					else if (destCity.equals("Charlotte")) {
						addCarToTrackWithLimit(car, trackB, limitTrackB, "Charlotte");
					}
					else if (destCity.equals("Trenton")) {
						addCarToTrackWithLimit(car, trackC, limitTrackC, "Trenton");
					}
					else {
						otherDest.add(car);
					}
				}

				else if (line.startsWith("ENG")) {

					String engineID = line;
					String city = x.nextLine().trim();

					if (city.equals("Baltimore")) {
						sendEngine(engineID, "Baltimore", trackA);
					}
					else if (city.equals("Charlotte")) {
						sendEngine(engineID, "Charlotte", trackB);
					}
					else if (city.equals("Trenton")) {
						sendEngine(engineID, "Trenton", trackC);
					}
				}
			}

			printStationStatus(trackA, trackB, trackC, otherDest, overweightQ);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		x.close();
	}

	private static void addCarToTrackWithLimit(Train car, Stack<Train> track, int limit, String cityName) {

		int currentWeight = getTotalWeight(track);

		if (currentWeight + car.getWeight() > limit) {
			String autoEngineID = "ENG" + String.format("%05d", val);
			val++;
			sendEngine(autoEngineID, cityName, track);
		}

		track.push(car);
	}
}
