package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import com.sun.xml.internal.fastinfoset.sax.SystemIdResolver;

import CargoTrain.*;
import javafx.scene.shape.ClosePath;

public class Station {
	static int nextID = 0;
	int id;
	Queue<Cargo> cargoQueue;
	static PrintStream printStream;
	static Station[] stations;
	static int totalNumberOfStations;
	static File f;
	

	public Station() {
		this.id = Station.nextID;
		nextID++;
		this.cargoQueue = new LinkedList<Cargo>();

	}

	public static void setStations(int numberOfStations) {
		totalNumberOfStations = numberOfStations;
		Station.stations = new Station[numberOfStations];
		for (int i = 0; i < numberOfStations; i++) {
			Station newStation = new Station();
			Station.stations[i] = newStation;
		}
	}

	public static void process(Train train){
		int currentStationID = train.getCurrentStationID();
		if (currentStationID == 0) {
			Queue<Cargo> loadedCargos = train.load((stations[currentStationID]).cargoQueue);
			
//				System.out.println(cargo + " 1 " + train.getLength());
				printStream.println(train.getCurrentStationID() +" "+ train.getLength());
			
		} else {	
			
			train.unload((stations[currentStationID]).cargoQueue);

			Queue<Cargo> newCargoQueue= train.load((stations[currentStationID]).cargoQueue);
			for (Cargo cargo : newCargoQueue) {
//				System.out.println(cargo + " 1 " + train.getLength());
				printStream.println(cargo);
			}
			printStream.println(train.getCurrentStationID() +" "+ train.getLength());

		}
		if (Station.stations.length > currentStationID + 1) {
			train.incrementTheCurrentStation();
		}


	}

	public void addToCargoQueue(Cargo cargo) {
		cargoQueue.add(cargo);
	}

	public static Station[] getStationList() {
		return stations;
	}

	public int getStationID() {
		return this.id;
	}

	public static int getStationNumber() {
		return totalNumberOfStations;
	}

	public void setCargoQueueOfStation(Queue<Cargo> cargoQueueOfStation) {
		this.cargoQueue = cargoQueueOfStation;
	}
	public static void initializePrintStream() throws FileNotFoundException {
		Station.printStream = new PrintStream(Station.f);
	}
	public static void initializeOutputFile(File file) {
		Station.f = file;
	}
	public static void ClosePrintStream() {
		printStream.close();
		
	}
}