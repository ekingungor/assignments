package CargoTrain;

import java.util.Stack;
import Util.*;

public class Carriage{
	int capacity;
	int emptySlot;
	Stack<Cargo> cargos;
	Carriage next;
	Carriage prev;
	
	public Carriage(int capacity,Carriage next,Carriage prev) {
		this.capacity = capacity;
		this.emptySlot = capacity;
		
		Stack<Cargo> cargos = new Stack<>();
		this.cargos = cargos;
		this.next=next;
		this.prev=prev;
	}
	public boolean isFull() {
		return emptySlot==0;
	}
	
	public void push(Cargo cargo) {
		cargos.push(cargo);
	}
	
	public Cargo pop() {
		return cargos.pop();
	}
	
	public void setNext(Carriage carriage) {
		this.next = carriage;
	}
	public void setPrev(Carriage carriage) {
		this.prev = carriage;
	}
	public Carriage getNext() {
		return this.next;
	}
	public Carriage getPrev() {
		return this.prev;
	}
	public Cargo drawLastCargo() {
		Cargo poppedCargo = cargos.pop();
		emptySlot = emptySlot + poppedCargo.getCargoSize();
		return poppedCargo;
		
		
	}
	public void addToCarriageStack(Cargo cargo) {
		emptySlot= emptySlot - cargo.getCargoSize();
		cargos.push(cargo);
	}
	public int getEmptySlot() {
		return this.emptySlot;
	}
	public boolean isEmpty() {
		return this.emptySlot==capacity;
	}
	
	
		
		
	
}