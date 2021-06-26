package it.polito.tdp.food.model;

public class Calorici implements Comparable <Calorici> {
	private Food f;
	private double peso;
	public Calorici(Food f, double peso) {
		super();
		this.f = f;
		this.peso = peso;
	}
	public Food getF() {
		return f;
	}
	public void setF(Food f) {
		this.f = f;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Calorici o) {
		if((o.peso-this.peso)<0)
			return +1;
		else 
			return -1;
		
	}
	@Override
	public String toString() {
		return f + " = " + peso + "\n";
	}
	
	
	
}
