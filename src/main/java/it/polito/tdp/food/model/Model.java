package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	SimpleWeightedGraph <Food, DefaultWeightedEdge> grafo;
	FoodDao dao;
	Map <Integer, Food> idMap;
	
	public Model() {
		dao= new FoodDao();
		idMap= new HashMap <Integer, Food>();
		dao.listAllFoods(idMap);
	}
	
	public void creaGrafo (int porzioni) {
		grafo= new SimpleWeightedGraph <Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(idMap, porzioni));
		for(Adiacenza a :dao.getAdiacenze(idMap)) {
			if(grafo.vertexSet().contains(a.getF1())&& grafo.vertexSet().contains(a.getF2()))
				Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
			
		}
	
	}
	
	public List <Calorici> trovaCalorici(Food f1){
		List <Food> vicini= Graphs.neighborListOf(this.grafo, f1);
		List<Calorici> result= new ArrayList<Calorici>();
		
		for(Food ff:vicini) {
			
			
			DefaultWeightedEdge e= this.grafo.getEdge(f1, ff);
			double peso= this.grafo.getEdgeWeight(e);
			Calorici c= new Calorici(ff,peso);
			result.add(c);
		}
		Collections.sort(result);
		return result;
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	public int getNArchi() {
		return grafo.edgeSet().size();
	}

	public SimpleWeightedGraph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public FoodDao getDao() {
		return dao;
	}

	public Map<Integer, Food> getIdMap() {
		return idMap;
	}
	
	public String simula(Food cibo, int K) {
		Simulatore sim= new Simulatore(this.grafo, this);
		sim.setK(K);
		sim.init(cibo);
		sim.run();
		String messaggio=String.format("Preparati %d cibi in %f minuti\n", sim.getCibiPreparati(), sim.getTempoPreparazione());
		return messaggio;
		
	}
	
}
