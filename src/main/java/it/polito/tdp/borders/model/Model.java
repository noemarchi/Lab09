package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

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
	
	public List<Country> getAllCountries()
	{
		this.dao.loadAllCountries(idMapAll);
		
		List<Country> stati = new ArrayList<Country>(this.idMapAll.values());
		Collections.sort(stati);
		
		return stati;
	}

	private Map<Country, Country> visitaGrafo(Country partenza)
	{
		// creo iteratore
		GraphIterator<Country, DefaultEdge> visita = new BreadthFirstIterator<>(grafo, partenza);
		
		// creo l'albero inverso
		//	<stato, predecessore>
		Map<Country, Country> alberoInverso = new HashMap<Country, Country>();
		alberoInverso.put(partenza, null);
		
		// aggancio il listener all'iteratore
		visita.addTraversalListener(new RegistraAlberoDiVisita(alberoInverso, this.grafo));
		
		while(visita.hasNext())
		{
			Country c = visita.next();
		}
		
		return alberoInverso;
	}
	
	public List<Country> getStatiRaggiungibili(Country partenza)
	{
		Map<Country, Country> alberoInverso = this.visitaGrafo(partenza);
		
		List<Country> lista = new ArrayList<Country>(alberoInverso.keySet());
		
		Collections.sort(lista);
		
		return lista;
	}
	
	private Map<Integer, Country> visitaIterativa(Country partenza)
	{
		Map<Integer, Country> visitati = new HashMap<Integer, Country>();
		Map<Integer, Country> daVisitare = new HashMap<Integer, Country>();
		
		daVisitare.put(partenza.getId(), partenza);
		
		while(!daVisitare.isEmpty())
		{
			List<Country> stati = new LinkedList<Country>(daVisitare.values());
			Country stato = stati.get(0);
			
			visitati.put(stato.getId(), stato);
			
			List<Country> vicini = new ArrayList<Country>();
			vicini = Graphs.neighborListOf(grafo, stato);
			
			for(Country vicino: vicini)
			{
				if(!visitati.containsKey(vicino.getId()))
				{
					daVisitare.put(vicino.getId(), vicino);
				}
			}
			
			for(Country v: visitati.values())
			{
				daVisitare.remove(v.getId());
			}
		}
		
		return visitati;
	}

	public List<Country> getRaggiungibiliIterativo(Country partenza)
	{
		Map<Integer, Country> visitati = this.visitaIterativa(partenza);
		List<Country> lista = new ArrayList<Country>(visitati.values());
		
		Collections.sort(lista);
		
		return lista;
	}
}
