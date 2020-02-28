package Util;
public class Cargo{
	 int id;
	 int size;
	 Station loadingStation;
	 Station targetStation;
	 
	 public Cargo(int size, Station loadingStation,
			 Station targetStation,int cargoID) {
		 this.size = size;
		 this.id = cargoID;
		 this.loadingStation = loadingStation;
		 this.targetStation = targetStation;
	 }
	 public int getLoadingStationID() {
		 return (this.loadingStation).getStationID();
		 
	 }
	 public int getTargetStationID() {
		 return (this.targetStation).getStationID();
		 
	 }
	 public int getCargoSize() {
		 return this.size;
	 }
	 @Override
	 public String toString() {
		 return id+" "+ loadingStation.getStationID() + " " +
	 targetStation.getStationID() + " " + size ;
	 }
	 @Override
	 public boolean equals(Object cargo) {
		return ((Cargo)cargo).id == this.id;
	 }
}