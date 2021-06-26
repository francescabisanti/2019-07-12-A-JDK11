package it.polito.tdp.food.model;

public class Event implements Comparable <Event> {
	
	public enum EventType{
		INIZIO_PREPARAZIONE, //viene assegnato un cibo ad una stazione
		FINE_PREPARAZIONE, //la stazione ha completato la prep di un cibo
	}
	
	private Double time; //tempo in minuti;
	private EventType type;
	private Stazione stazione;
	private Food cibo;
	
	
	public Double getTime() {
		return time;
	}
	
	public Stazione getStazione() {
		return stazione;
	}
	
	public Food getCibo() {
		return cibo;
	}
	
	public Event(Double time, EventType type,Stazione stazione, Food cibo) {
		super();
		this.time = time;
		this.type=type;
		this.stazione = stazione;
		this.cibo = cibo;
	}
	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}

	public EventType getType() {
		return type;
	}
	
	
	
}
