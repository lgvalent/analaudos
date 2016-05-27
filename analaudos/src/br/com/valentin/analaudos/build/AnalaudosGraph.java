package br.com.valentin.analaudos.build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultEdge;

import br.com.valentin.analaudos.build.AnalaudosDocument.DocEdge;
import br.com.valentin.analaudos.build.AnalaudosDocument.DocNode;

public class AnalaudosGraph extends DirectedGraph<AnalaudosGraph.AnalaudosNode, AnalaudosGraph.AnalaudosEdge>{
	private final Logger log = Logger.getLogger(getClass()); 
	
	private static final long serialVersionUID = 1L;

	private Map<String, AnalaudosNode> nodesMap = new TreeMap<String, AnalaudosNode>();

	@Override
	public boolean addVertex(AnalaudosNode v) {
		boolean result = super.addVertex(v);

		if(result)
			nodesMap.put(v.word, v);

		return result;
	}
	
	public AnalaudosGraph() {
		super(AnalaudosGraph.AnalaudosEdge.class);
	}

	public static class AnalaudosNode implements Comparable<AnalaudosNode>{
		public String id;
		public String fontColor;
		public String word;
		public List<String> labels = new ArrayList<String>(); 
		public Map<AnalaudosNode, Integer> before = new TreeMap<AnalaudosNode, Integer>(); 
		public Map<AnalaudosNode, Integer> after = new TreeMap<AnalaudosNode, Integer>();

		@Override
		public String toString() {
			return word;
		}

		@Override
		public int compareTo(AnalaudosNode o) {
			if(o == null) return -1;
			if(word != null && o.word == null) return 1;
			return o.word.compareTo(o.word);
		}

		//		@Override
		//		public boolean equals(Object obj) {
		//			if(this.word == null || obj == null || ((AnalaudosNode)obj).word == null) return false;
		//			
		//			return this.word.equals(((AnalaudosNode)obj).word);
		//		}
	}

	public static class AnalaudosEdge extends DefaultEdge{
		private static final long serialVersionUID = 1L;

		public DescriptiveStatistics wordDistance = new DescriptiveStatistics(); 
		public DescriptiveStatistics linkLatency = new DescriptiveStatistics();
		public DescriptiveStatistics interceptedPonctuations = new DescriptiveStatistics();

		@Override
		public String toString() {
			return super.toString() + "[wd:" + String.format("%.2f", wordDistance.getMean()) + ", ll:"+ String.format("%.2f", linkLatency.getMean()) +"ms, ip:"+ String.format("%.2f", interceptedPonctuations.getMean()) + "]";
		}
		
	}

	public AnalaudosDocument createDocGraph(String content){
		AnalaudosDocument docGraph = new AnalaudosDocument();
		
		docGraph.addContent(content);
		
		for(DocNode docNodeSource: docGraph.vertexSet()){
			for(DocNode docNodeTarget: docGraph.vertexSet()){
				double linkReliability = linkResolve(docNodeSource, docNodeTarget); 
				if(linkReliability > 0){
					DocEdge docEdge = docGraph.addEdge(docNodeSource, docNodeTarget);
					docEdge.reliability = linkReliability;
				}
			}
		}
		
		return docGraph;
	}

	/**
	 * @param docNodeSource
	 * @param docNodeTarget
	 * @return Calculate the reliability of a link between two nodes
	 * 
	 * @version 20160520: Uses wordDistance and standard deviation to resolve link
	 */
	private double linkResolve(DocNode docNodeSource, DocNode docNodeTarget) {
		double result = 0.0f;
		StringBuilder sb = new StringBuilder(); 
		sb.append('[').append(docNodeSource.index).append(':').append(docNodeTarget.index).append(']').append('\t').append(docNodeSource.word).append('\t').append("->").append(docNodeTarget.word);
		
		AnalaudosNode analNodeSource = nodesMap.get(docNodeSource.word);
		AnalaudosNode analNodeTarget= nodesMap.get(docNodeTarget.word);
		if(analNodeSource != null && analNodeTarget != null){
			AnalaudosEdge analEdge = this.getEdge(analNodeSource, analNodeTarget);
			if(analEdge != null){
				double wordDistance = analEdge.wordDistance.getMean() - (docNodeTarget.index - docNodeSource.index);
				sb.append('\t').append('[').append(wordDistance).append(':').append(analEdge.wordDistance.getMean()).append(':').append(analEdge.wordDistance.getVariance()).append(':').append(analEdge.wordDistance.getStandardDeviation()).append(']');
				sb.append(Arrays.toString(analEdge.wordDistance.getValues()));
				
				if(Math.abs(wordDistance) > (analEdge.wordDistance.getStandardDeviation()*2))
					result = -1;
				else
					result = (Math.abs(wordDistance) / (analEdge.wordDistance.getStandardDeviation()*2)); 

				if(Double.isNaN(result)) result = 1;
				sb.append(result);

				if(result > 0){
					sb.append("[LINKED<->]");
				}
				log.debug(sb.toString());
			}
		}
		
		return result;
	}
	
	
	public void addDocGraph(AnalaudosDocument docGraph){
		for(DocEdge docEdge: docGraph.edgeSet()){
			addDocEdge(docEdge);
		}
	}

	/**
	 * @param docEdge
	 */
	public AnalaudosEdge addDocEdge(DocEdge docEdge) {
		AnalaudosNode analNodeSource = addDocNode((DocNode) docEdge.getSource());
		AnalaudosNode analNodeTarget = addDocNode((DocNode) docEdge.getTarget());

		AnalaudosEdge analEdge = this.getEdge(analNodeSource, analNodeTarget);
		if(analEdge == null){
			analEdge = this.addEdge(analNodeSource, analNodeTarget);
		}

		analEdge.linkLatency.addValue(docEdge.linkLatency);
		analEdge.wordDistance.addValue(docEdge.wordsDistance);
		analEdge.interceptedPonctuations.addValue(docEdge.interceptedPonctuations.length());
		
		return analEdge;
	}

	
	private AnalaudosNode addDocNode(DocNode docNode){
		AnalaudosNode analNode = retrieveNode(docNode);

		/* Update AnalNode statistics */
		analNode.labels.add(docNode.label);

		if(docNode.before!=null){
			AnalaudosNode beforeNode = retrieveNode(docNode.before);
			if(!analNode.before.containsKey(beforeNode))
				analNode.before.put(beforeNode, 0);
			analNode.before.put(beforeNode, analNode.before.get(beforeNode)+1);
		}

		if(docNode.after!=null){
			AnalaudosNode afterNode = retrieveNode(docNode.after);
			if(!analNode.after.containsKey(afterNode))
				analNode.after.put(afterNode, 0);
			analNode.after.put(afterNode, analNode.after.get(afterNode)+1);
		}
		
		return analNode;
	}

	private int nodeCount = 0;
	private AnalaudosNode retrieveNode(DocNode docNode){
		AnalaudosNode analNode = this.nodesMap.get(docNode.word);
		if(analNode == null){
			analNode = new AnalaudosNode();
			analNode.id = "w" + nodeCount++;
			analNode.word = docNode.word;
			analNode.fontColor = docNode.fontColor;

			this.addVertex(analNode);
		}

		return analNode;
	}
	
	
	public String toDot(boolean showEdgeLabels){
		StringBuilder sb = new StringBuilder("digraph G {");
		
		for(AnalaudosNode node: this.vertexSet()){
			sb.append(node.id).append("[").append("fontcolor=\"").append(node.fontColor).append("\", ").append("label=\"").append(node.word).append("\"];");
		}

		for(AnalaudosEdge edge: this.edgeSet()){
			AnalaudosNode source = (AnalaudosNode) edge.getSource();
			AnalaudosNode target = (AnalaudosNode) edge.getTarget();
			sb.append(source.id).append("->").append(target.id);
			if(showEdgeLabels)
				sb.append("[label=\"").append(edge.toString()).append("\"]");
			sb.append(";");
		}

		sb.append("}");

		
		return sb.toString();
	}

}