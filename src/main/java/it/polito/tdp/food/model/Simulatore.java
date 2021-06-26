package it.polito.tdp.food.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulatore {
	//Modello del mondo
	private List<Stazione> stazioni;
	private List<Food> cibi;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Model model;
	//sono questi tre elementi che devo gestire durante la simulazione
	
	
	//Parametri di simulazione
	private int K=5; //n stazioni disponibili
	
	
	
	//risultati calcolati
	private Double tempoPreparazione;
	private int cibiPreparati;
	
	
	//coda degli eventi
	private PriorityQueue <Event> queue;
	
	
	public Simulatore(Graph<Food, DefaultWeightedEdge> grafo, Model model) {
		this.grafo=grafo;
		this.model=model;
		
		
	}
	public void init(Food partenza) {
		this.cibi=new ArrayList<>(this.grafo.vertexSet());
		this.stazioni= new ArrayList<>();
		this.cibiPreparati=0;
		for(Food cibo: cibi) {
			cibo.setPreparazione(StatoPreparazione.DA_PREPARARE);
		}
		for(int i=0; i<K; i++) {
			this.stazioni.add(new Stazione(true, null));
			//per ogni K creo una stazione LIBERA
			// che ancora non sta preparando nessun cibo.
		}
		this.tempoPreparazione=0.0;
		this.queue= new PriorityQueue<>();
		//dato il mio cibo di partenza, devo prendere i K vicini più calorici
		//lo prendo dal metodo che ho già
		List<Calorici> vicini= model.trovaCalorici(partenza);
		for(int i=0; i<this.K && i<vicini.size(); i++) {
			this.stazioni.get(i).setLibera(false);
			this.stazioni.get(i).setFood(vicini.get(i).getF());
			
			Event e= new Event(vicini.get(i).getPeso(),//perche il tempo dipende dalle calorie
					EventType.FINE_PREPARAZIONE, //schedulo già la fine
					this.stazioni.get(i),
					vicini.get(i).getF());
			queue.add(e);
			
		}
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e= queue.poll();
			processEvent(e);
		}
	}


	private void processEvent(Event e) {
		switch(e.getType()) {
		
		case INIZIO_PREPARAZIONE:
			//Guarda che è appena terminato il cibo, ora scegli il cibo da preparare
			List<Calorici> vicini= model.trovaCalorici(e.getCibo());
			Calorici prossimo=null; //prossimo cibo da cucinare
			for(Calorici vicino: vicini) {
				if(vicino.getF().getPreparazione()==StatoPreparazione.DA_PREPARARE) {
					prossimo=vicino;
					break;
					
				}
			}
			if(prossimo!=null) {
				prossimo.getF().setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(prossimo.getF());
				
				Event e2= new Event(e.getTime()+prossimo.getPeso(),
						EventType.FINE_PREPARAZIONE, 
						e.getStazione(),
						prossimo.getF());
				this.queue.add(e2);
			}
			
			break;
			//OCCHIO CHE APPENA FINISCE UNA PREPARAZIONE, ALLORA NE DEVE INIZIARE UNA NUOVA
		case FINE_PREPARAZIONE:
			this.cibiPreparati++;//perche ho finito un cibo
			e.getStazione().setLibera(true);
			this.tempoPreparazione=e.getTime();
			e.getCibo().setPreparazione(StatoPreparazione.PREPARATO);;
			
			Event e2= new Event(e.getTime(), EventType.INIZIO_PREPARAZIONE, e.getStazione(), e.getCibo());
			//cioe un evento con lo stesso momento di e, di inizio e stessa stazione e cibo
			this.queue.add(e2);
		}
		
	}
	public int getK() {
		return K;
	}


	public void setK(int k) {
		K = k;
	}


	public Double getTempoPreparazione() {
		return tempoPreparazione;
	}


	public void setTempoPreparazione(Double tempoPreparazione) {
		this.tempoPreparazione = tempoPreparazione;
	}
	public int getCibiPreparati() {
		return cibiPreparati;
	}
	
	
}
