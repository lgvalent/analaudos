package br.com.valentin.analaudos.build;

import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.jgraph.graph.DefaultEdge;

import br.com.valentin.analaudos.build.AnalaudosDocument.DocEdge;
import br.com.valentin.analaudos.build.AnalaudosDocument.DocNode;
import br.com.valentin.analaudos.build.AnalaudosGraph.AnalaudosNode.Type;

public class AnalaudosGraph extends DirectedGraphBase<AnalaudosGraph.AnalaudosNode, AnalaudosGraph.AnalaudosEdge>{
	private final Logger log = Logger.getLogger(getClass()); 
	
	private static final long serialVersionUID = 1L;

	private Map<String, AnalaudosNode> nodesMap = new TreeMap<String, AnalaudosNode>();
	private int docsCount = 0;

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
		public static enum Type{WORD, NUMBER, MEASURE};
		public String id;
		public String fontColor;
		public String penColor;
		public String word;
		public Type type = Type.WORD;
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

		public static Type checkDataType(String word){
			if(word.equals("cm") || word.equals("cm²") || word.equals("cm³") || 
			   word.equals("mm") || word.equals("mm²") || word.equals("mm³") || 
			   word.equals("m") || word.equals("m²") || word.equals("m³"))
				return Type.MEASURE;
			
			NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
			Number n = nf.parse(word, new ParsePosition(0));
			if(n!=null)
				return Type.NUMBER;
			
			return Type.WORD;
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

		public String fontColor;
		public String penColor;

		public DescriptiveStatistics wordDistance = new DescriptiveStatistics(); 
		public DescriptiveStatistics phraseDistance = new DescriptiveStatistics(); 
		public DescriptiveStatistics linkLatency = new DescriptiveStatistics();
		public DescriptiveStatistics interceptedPonctuations = new DescriptiveStatistics();

		@Override
		public String toString() {
			return wordDistance.getN() + " link(s)[pd:" + String.format("%.2f", phraseDistance.getMean()) + ", wd:" + String.format("%.2f", wordDistance.getMean()) + ", ll:"+ String.format("%.2f", linkLatency.getMean()) +"ms, ip:"+ String.format("%.2f", interceptedPonctuations.getMean()) + "]";
		}
		
	}

	public AnalaudosDocument createDocGraph(String content, AnalaudosGraphStrategy strategy){
		StringWriter sw = new StringWriter();
		WriterAppender wa = new WriterAppender(new SimpleLayout(), sw);
		AnalaudosGraphStrategy.log.addAppender(wa);
		
		AnalaudosDocument docGraph = new AnalaudosDocument();
		
		docGraph.addContent(content);
		
		strategy.createDocGraph(docGraph, this);
		
		docGraph.setLog(sw.toString());
		AnalaudosGraphStrategy.log.removeAppender(wa);
		return docGraph;
	}

	
	public void addDocGraph(AnalaudosDocument docMap){
		prepareForAddDocGraph();

		for(DocEdge docEdge: docMap.edgeSet()){
			addDocEdge(docEdge);
		}
		
	}
	
	private void prepareForAddDocGraph(){
		docsCount++;
	}

	/**
	 * @param docEdge
	 */
	public AnalaudosEdge addDocEdge(DocEdge docEdge) {
		// INVERSE MODE: FALSE
		AnalaudosNode analNodeSource = addDocNode((DocNode) docEdge.getSource());
		AnalaudosNode analNodeTarget = addDocNode((DocNode) docEdge.getTarget());

		AnalaudosEdge analEdge = null;
		if(analNodeSource.type != Type.NUMBER){
			analEdge = this.getEdge(analNodeSource, analNodeTarget);
		} else{
			/* NUMBER vertex allows multiple edges */
			for(AnalaudosEdge analEdge_: this.getAllEdges(analNodeSource, analNodeTarget)){
				/* Analyze recurrent link */
				System.out.println("========>" + analNodeSource.word +"->"+ analNodeTarget.word +" Mean:" + analEdge_.wordDistance.getMean() + ", SD:"+ analEdge_.wordDistance.getStandardDeviation() + ", NodeWD:" + docEdge.wordDistance + ", RAW:" + Arrays.toString(analEdge_.wordDistance.getValues()));
				if(analEdge_.wordDistance.getMean() == docEdge.wordDistance){
					analEdge = analEdge_;
					System.out.println("========> Usou o MESMO");
				}
			}
		}
		
		if(analEdge == null){
			analEdge = this.addEdge(analNodeSource, analNodeTarget);
		}

		analEdge.linkLatency.addValue(docEdge.linkLatency);
		analEdge.wordDistance.addValue(docEdge.wordDistance);
		analEdge.phraseDistance.addValue(docEdge.phraseDistance);
		analEdge.interceptedPonctuations.addValue(docEdge.interceptedPonctuations.length());
		
		return analEdge;
	}
	
	private AnalaudosNode addDocNode(DocNode docNode){
		AnalaudosNode analNode = retrieveOrCreateNode(docNode);

		/* Update AnalNode statistics */
		analNode.labels.add(docNode.label);

		if(docNode.before!=null){
			AnalaudosNode beforeNode = retrieveOrCreateNode(docNode.before);
			if(!analNode.before.containsKey(beforeNode))
				analNode.before.put(beforeNode, 0);
			analNode.before.put(beforeNode, analNode.before.get(beforeNode)+1);
		}

		if(docNode.after!=null){
			AnalaudosNode afterNode = retrieveOrCreateNode(docNode.after);
			if(!analNode.after.containsKey(afterNode))
				analNode.after.put(afterNode, 0);
			analNode.after.put(afterNode, analNode.after.get(afterNode)+1);
		}
		
		return analNode;
	}

	private int nodeCount = 0;
	private AnalaudosNode retrieveOrCreateNode(DocNode docNode){
		
		AnalaudosNode analNode = retrieveNode(docNode);
		if(analNode == null){
			analNode = new AnalaudosNode();
			analNode.id = "w" + nodeCount++;
			analNode.fontColor = docNode.fontColor;
			analNode.penColor = docNode.penColor;
			
			analNode.type = AnalaudosNode.checkDataType(docNode.word);
			analNode.word = getNodeWord(docNode, analNode.type);
			
			this.addVertex(analNode);
		}

		return analNode;
	}
	
	public AnalaudosNode retrieveNode(DocNode docNode){
		AnalaudosNode.Type type = AnalaudosNode.checkDataType(docNode.word);

		String queryWord = getNodeWord(docNode, type);

		return this.nodesMap.get(queryWord);
	}
	
	private String getNodeWord(DocNode docNode, AnalaudosNode.Type type){
		switch (type) {
		case WORD: return docNode.word;
		default:
			return type.name();
		}
	}
	
	public String toDot(boolean showEdgeLabels){
		StringBuilder sb = new StringBuilder("digraph G {");
		
		for(AnalaudosNode node: this.vertexSet()){
			sb.append(node.id).append("[").append("fontcolor=\"").append(node.fontColor).append("\", ").append("color=\"").append(node.penColor).append("\", ").append("label=\"").append(node.toString()).append("\"]; ");
		}

		for(AnalaudosEdge edge: this.edgeSet()){
			AnalaudosNode source = (AnalaudosNode) edge.getSource();
			AnalaudosNode target = (AnalaudosNode) edge.getTarget();
			sb.append(source.id).append("->").append(target.id);
			if(showEdgeLabels)
				sb.append("[label=\"").append(edge.toString()).append("\", ").append("fontcolor=\"").append(edge.fontColor).append("\", ").append("color=\"").append(edge.penColor).append("\"]");
			sb.append("; ");
		}

		sb.append("}");

		
		return sb.toString();
	}

}