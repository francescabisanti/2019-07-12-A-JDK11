package it.polito.tdp.food.model;

public class Stazione {
	//ci serve un'info che ci dice se è libera o meno
	private boolean libera;
	private Food food; //per sapere il cibo che la stazione sta preparando
	public Stazione(boolean libera, Food food) {
		super();
		this.libera = libera;
		this.food = food;
	}
	public boolean isLibera() {
		return libera;
	}
	public void setLibera(boolean libera) {
		this.libera = libera;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	
	
	

}
