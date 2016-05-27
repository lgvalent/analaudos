package br.com.valentin.analaudos.build;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.DefaultDirectedGraph;

/**
 * Specializes DirectGraph with defaultEdge to set Source and Target.
 * The DefaultDirectGraph has a bug that keep theses att null.
 * @author lucio
 *
 * @param <V>
 * @param <E>
 */
public class DirectedGraph <V,E extends DefaultEdge > extends DefaultDirectedGraph<V, E>{

	public DirectedGraph(Class<? extends E> edgeClass) {
		super(edgeClass);
	}

	private static final long serialVersionUID = 1L;
	
	@Override
	public E addEdge(V arg0, V arg1) {
		E edge = super.addEdge(arg0, arg1);
		
		if(edge != null){
			edge.setSource(arg0);
			edge.setTarget(arg1);
		}
			
		return edge;
	}
	

}
