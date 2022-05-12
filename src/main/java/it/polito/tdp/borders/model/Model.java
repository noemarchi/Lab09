package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import it.polito.tdp.borders.db.BordersDAO;

public class Model 
{
	private Graph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	private Map<Integer, Country> idMapAll;
	List<Border> confini;
	private Map<Integer, Country> vertici;

	public Model() 
	{
		dao = new BordersDAO();
		idMapAll = new HashMap<Integer, Country>();
		
	}
	
	public void creaGrafo(int anno)
	{
		// creo il grafo vuoto
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		// aggiungo i vertici al grafo. quali sono?
		
		// riempio la idMap con tutti gli stati
		this.dao.loadAllCountries(idMapAll);
		// ottengo i confini, passando l'anno come parametro
		this.confini = this.dao.getCountryPairs(anno, idMapAll);
		// estraggo i vertici-stati dalla lista dei confini
		vertici = new HashMap<Integer, Country>();
		for(Border b: confini)
		{
			vertici.put(b.getC1().getId(), b.getC1());
			vertici.put(b.getC2().getId(), b.getC2());
		}
		// aggiungo i vertici al grafo
		Graphs.addAllVertices(this.grafo, this.vertici.values());
		
		// aggiungo gli archi al grafo
		for(Border b: confini)
		{
			Graphs.addEdgeWithVertices(this.grafo, b.getC1(), b.getC2());
		}
	}
	
	public int nVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
	public List<StatoGrado> getStatiGrado()
	{
		List<StatoGrado> ret = new ArrayList<StatoGrado>();
		
		for(Country c: this.grafo.vertexSet())
		{
			int grado = Graphs.neighborListOf(this.grafo, c).size();
			StatoGrado x = new StatoGrado(c, grado);
			ret.add(x);
		}
		
		return ret;
	}
	
	public int getComponentiConnesse() 
	{
		ConnectivityInspector ispettore = new ConnectivityInspector(this.grafo);
		List<Set<Country>> connessi = ispettore.connectedSets();
		
		return connessi.size();
	}

}
