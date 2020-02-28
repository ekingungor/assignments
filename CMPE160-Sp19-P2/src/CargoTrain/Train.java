package CargoTrain;

import java.util.LinkedList;
import java.util.Queue;

import Util.*;

public class Train {
	int length;
	int carCapacity;
	Carriage head;
	Carriage tail;
	int currentStationID;

	public Train(int length, int carCapacity) {
		this.currentStationID = 0;
		this.carCapacity = carCapacity;
		this.length = length;
		if (this.length == 0) {
			this.head = null;
			this.tail = null;
		} else {
			Carriage first = new Carriage(carCapacity, null, null);
			this.head = first;
			this.tail = first;
			Carriage currentCarriage;
			Carriage previousCarriage = head;
			for (int i = 0; i < length - 1; i++) {
				currentCarriage = new Carriage(carCapacity, null, null);
				previousCarriage.setNext(currentCarriage);
				currentCarriage.setPrev(previousCarriage);
				previousCarriage = currentCarriage;

			}
		}
	}

	public Queue<Cargo> load(Queue<Cargo> cargos) {

		Queue<Cargo> newCargoQueue = new LinkedList<Cargo>();

		for (Cargo cargo : cargos) {
			if (cargo.getCargoSize() > carCapacity) {
				newCargoQueue.add(cargo);
				continue;
			}
			Carriage currentCarriage = head;
			if ((cargo.getTargetStationID()) == currentStationID) {
				newCargoQueue.add(cargo);
				continue;
			}
			boolean isPlacedToStack = false;
			while (!isPlacedToStack) {
				int sizeOfCurrentCargo = cargo.getCargoSize();

				if (head == null) {
					Carriage newCarriage = new Carriage(carCapacity, null, null);
					head = newCarriage;
					head.addToCarriageStack(cargo);
					isPlacedToStack = true;
					this.length++;
					continue;
				}
				if (sizeOfCurrentCargo > currentCarriage.emptySlot) {
					if (currentCarriage.getNext() != null) {
						currentCarriage = currentCarriage.getNext();
					} else {
						Carriage newCarriage = new Carriage(carCapacity, null, currentCarriage);
						currentCarriage.setNext(newCarriage);
						newCarriage.addToCarriageStack(cargo);

						isPlacedToStack = true;
						this.length++;
					}

				} else {
					currentCarriage.addToCarriageStack(cargo);
					isPlacedToStack = true;

				}
			}

		}
		removeUnnecessaryCarriages();
		((Station.getStationList())[currentStationID]).setCargoQueueOfStation(newCargoQueue);
		return newCargoQueue;

	}

	public void unload(Queue<Cargo> cargos) {
		Carriage currentCarriage = head;
		if (head == null) {
			return;
		}
		while (true) {
			while (!currentCarriage.isEmpty()) {
				cargos.add(currentCarriage.drawLastCargo());
			}

			if (currentCarriage.getNext() != null) {
				currentCarriage = currentCarriage.getNext();
			} else {
				return;
			}
		}

	}

	public void incrementTheCurrentStation() {
		this.currentStationID++;
	}

	public int getCurrentStationID() {
		return this.currentStationID;
	}

	public int getLength() {
		return length;
	}

	public void removeUnnecessaryCarriages() {

		main :
		while (true) {

			if (this.length == 0) {
				return;
			} else if (this.length == 1) {
				if (head.isEmpty()) {
					head = null;
					this.length--;
					return;
				} else {
					return;
				}
			} else {
				Carriage currentCarriage = head.getNext();

				while (currentCarriage != null) {
					if (currentCarriage.getNext() == null) {
						if (currentCarriage.isEmpty()) {
							Carriage prevCarriage = currentCarriage.getPrev();
							prevCarriage.setNext(null);
							currentCarriage.setPrev(null);
							this.length--;
							break;
						} else {
							break main;
						}

					} else {

						if (currentCarriage.isEmpty()) {
							Carriage prevCarriage = currentCarriage.getPrev();
							Carriage nextCarriage = currentCarriage.getNext();
							prevCarriage.setNext(nextCarriage);
							nextCarriage.setPrev(prevCarriage);
							currentCarriage.setNext(null);
							currentCarriage.setPrev(null);
							currentCarriage = nextCarriage;
							this.length--;
						} else {
							currentCarriage = currentCarriage.getNext();
						}
					}
				}

			}

		}
	}
}
