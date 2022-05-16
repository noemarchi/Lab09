package it.polito.tdp.borders.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;



public class RegistraAlberoDiVisita implements TraversalListener<Country, DefaultEdge> 
{
	private Map<Country, Country> alberoInverso;
	private Graph<Country, DefaultEdge> grafo;
	
	public RegistraAlberoDiVisita(Map<Country, Country> alberoInverso, Graph<Country, DefaultEdge> grafo) 
	{
		super();
		this.alberoInverso = alberoInverso;
		this.grafo = grafo;
	}

	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) 
	{
		Country source = this.grafo.getEdgeSource(e.getEdge());
		Country target = this.grafo.getEdgeTarget(e.getEdge());
		
		// se target non c'è gia nella mappa, il target è il vertice che ho scoperto
		// a partire da source
		if(!alberoInverso.containsKey(target))
		{
			alberoInverso.put(target, source);
		}
		
		// se source non c'è gia nella mappa, il source è il vertice che ho scoperto
		// a partire da target
		else if(!alberoInverso.containsKey(source))
		{
			alberoInverso.put(source, target);
		}
		
		// non può verificarsi che mancano sia target che source
		
		// se sia target che source sono già presenti, è giusto non aggiungere niente
		// perchè quell'arco è gia presente
		
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Country> e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Country> e) {
		// TODO Auto-generated method stub
		
	}

}
