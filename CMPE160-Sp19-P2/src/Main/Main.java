package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;

import CargoTrain.Train;
import Util.*;

public class Main {
	public static void main(String args[]) throws FileNotFoundException {
		Scanner input = new Scanner(new File(args[0]));
		File output = new File(args[1]);
		Station.initializeOutputFile(output);
		
		execute(readAndInitialize(input));
		Station.ClosePrintStream();
		input.close();
	}

	public static Train readAndInitialize(Scanner input) throws FileNotFoundException {
		int initialCarriages = input.nextInt();
		int sizeOfCarriages = input.nextInt();
		int numberOfStations = input.nextInt();
		Train train = new Train(initialCarriages, sizeOfCarriages);
		Station.setStations(numberOfStations);

		try {
			while (input.hasNextLine()) {
				int idOfCargo = input.nextInt();
				int idOfLoadingStation = input.nextInt();
				int idOfTargetStation = input.nextInt();
				int sizeOfCargo = input.nextInt();
				Cargo cargo = new Cargo(sizeOfCargo, (Station.getStationList())[idOfLoadingStation],
						(Station.getStationList())[idOfTargetStation], idOfCargo);
				Station.getStationList()[idOfLoadingStation].addToCargoQueue(cargo);
			}
			
		} catch (Exception e) {
			
		}
		
		return train;

	}

	public static void execute(Train train) throws FileNotFoundException {

		Station.initializePrintStream();
		for (int i = 0; i < Station.getStationNumber(); i++)
			Station.process(train);

	}
}