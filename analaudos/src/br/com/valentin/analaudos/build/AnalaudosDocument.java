package br.com.valentin.analaudos.build;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jgraph.graph.DefaultEdge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AnalaudosDocument extends DirectedGraphBase<AnalaudosDocument.DocNode, AnalaudosDocument.DocEdge>{
	private static final long serialVersionUID = 1L;
	
	private String log;
	
	public AnalaudosDocument(String jsonGraph) {
		super(AnalaudosDocument.DocEdge.class);
		addJsonGraph(jsonGraph);
	}

	public AnalaudosDocument() {
		super(AnalaudosDocument.DocEdge.class);
	}

	public static class DocNode{

		public DocNode(){}
		public DocNode(AnalaudoJson.Node node){
			this.id =  node.id;
			this.index =  Integer.parseInt(node.id.replaceAll("[A-Za-z]", ""));
			this.fontColor = node.fontColor;
			this.penColor = node.penColor;
			this.label = node.label;
			this.word = node.word.trim(); // 20160526: Some JSONs are keeping leading and trailing whitespaces 
		}

		public String id;
		public int index; // Use id to extract index: w10 -> 10
		public String fontColor;
		public String penColor;
		public String label;
		public String word;

		/** Like a linked list */
		public AnalaudosDocument.DocNode before;
		public AnalaudosDocument.DocNode after;

		@Override
		public String toString() {
			return label;
		}
		
		@Override
		public boolean equals(Object obj) {
			return index == ((DocNode)obj).index;
		}
	}

	public static class DocEdge extends DefaultEdge{
		private static final long serialVersionUID = 1L;

		public String id;
		public int wordDistance;
		public int phraseDistance;
		public int linkLatency;
		public String interceptedPonctuations = "";
		public String fontColor;
		public String penColor;

		public double linkScore = 0;

		public boolean interceptPonctuation(){return !this.interceptedPonctuations.isEmpty();}

		@Override
		public String toString() {
			return "[pd:" + phraseDistance + ", wd:" + wordDistance + ", ll:"+ linkLatency +"ms, ip:'"+ interceptedPonctuations  +"', s:" + String.format("%.2f", linkScore) + "]";
		}
		
		public String toString2(){
			StringBuilder sb = new StringBuilder();
			DocNode source = (DocNode) getSource();
			DocNode target = (DocNode) getTarget();
			
			sb.append('[').append(source.index).append(':').append(target.index).append(']').append('\t').append(source.word).append('\t').append("->").append(target.word);
			sb.append('\t').append(toString());

			return sb.toString();
		}
	}
	
	public String getLog() {return log;}
	public void setLog(String log) {this.log = log;}

	public void addActionLogData(String actionLog){
	
		for(DocEdge e: this.edgeSet()){
			DocNode source = (DocNode) e.getSource();
			DocNode target = (DocNode) e.getTarget();
	
			/* Time between clicks */
			e.linkLatency = retrieveCreateEdgeLatency(source.id, target.id, actionLog);
	
		}
	}
	
	public static String checkIntercepPonctuation(DocNode source, DocNode target){
		StringBuilder result = new StringBuilder();
		DocNode temp = source;
		while(temp != target){
			if(source.index < target.index)
				temp = temp.after;
			else
				temp = temp.before;

			if(temp == null) break;

			// Doesn't match number like 2,3 or 2.3 as ponctuation
			if(temp.label.matches(".*\\.([ a-zA-Z].*|$)"))
				result.append('.');
			if(temp.label.matches(".*,([ a-zA-Z].*|$)"))
				result.append(',');
			if(temp.label.contains(";"))
				result.append(';');
			if(temp.label.contains(":"))
				result.append(':');
			if(temp.label.contains("!"))
				result.append('!');
			if(temp.label.contains("?"))
				result.append('?');
		}
//		System.out.println(source + "->" + target + "=" + result);
		
		return result.toString();

	}

	public static int checkPhraseDistance(DocNode source, DocNode target){
		String interceptedPonctuations = checkIntercepPonctuation(source, target);
		
		return checkPhraseDistance(interceptedPonctuations); 
	}

	public static int checkPhraseDistance(String interceptedPonctuations){
		int result = 0;
		
		result += StringUtils.countMatches(interceptedPonctuations, ".");
		result += StringUtils.countMatches(interceptedPonctuations, "!");
		result += StringUtils.countMatches(interceptedPonctuations, "?");

		return result;
	}

	private void addJsonGraph(String analaudosJson){
		Gson gson = new GsonBuilder().create();
		AnalaudoJson json = gson.fromJson(analaudosJson, AnalaudoJson.class);
	
		if(json.version != 0.2) throw new RuntimeException("Only AnalaudosJson(v0.2) is supported, with orphan nodes included!");
	
		/* Node metadata */
		DocNode previous = null;
	
		Map<String, DocNode> nodes = new TreeMap<String, DocNode>();
		for(AnalaudoJson.Node n: json.nodes){
			DocNode node = new DocNode(n);
	
			if(previous != null){ 
				previous.after = node;
				node.before = previous;
			}
			previous = node;
	
			nodes.put(node.id, node);
	
			this.addVertex(node);
		}
	
		for(List<String> edges: json.edges){
			DocNode source = nodes.get(edges.get(0));
			DocNode target = nodes.get(edges.get(1));
	
			DocEdge edge = this.addEdge(source, target);
	
			edge.wordDistance = target.index - source.index;
			/* Check ponctuation between nodes */ 
			edge.interceptedPonctuations = checkIntercepPonctuation(source, target);
			
			edge.phraseDistance = checkPhraseDistance(edge.interceptedPonctuations);
		}
	}

	/** Retrieve edge latency: time between source click and target click */
	private Integer retrieveCreateEdgeLatency(String sourceId, String targetId, String actionLog){
		actionLog = actionLog.replaceAll(";w", ",w"); //Lucio 20160516: new version of analaudos.js create a log using , in parameters and ; in actions.
	
		String createEdgePattern = "([0-9]*):selectSource\\(%a1\\); ([0-9]*):createEdge\\(%a1,%a2\\)";
		Pattern p = Pattern.compile(createEdgePattern.replaceAll("%a1", sourceId).replaceAll("%a2",	targetId));
		Matcher m = p.matcher(actionLog);
	
		if(m.find()){
			int selectSource = Integer.parseInt(m.group(1));
			int createEdge = Integer.parseInt(m.group(2));
			return createEdge - selectSource;
		}
	
		return -1;
	}

	/**
	 * Create a graph from document words, without edges, only simple metadata
	 * @param doc
	 * @return
	 */
	public void addContent(String content){
		String[] words = content.split(" ");
		int wordsCount = 0;
		DocNode previous = null;
	
		for(String word: words){
			DocNode node = new DocNode();
			node.id = "w"+wordsCount;
			node.index = wordsCount++;
			node.label = word;
	
			/* analaudos.js: a.addText() */
			word = word.replaceAll("[:;()\\[\\]?!]", "");
			word = word.replaceAll("[\n\r\\.,](\\ |$)", " "); // Replace dot and comma by a space, or word separator
			word = Normalizer.normalize(word, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			word = word.trim();
			node.word = word.toLowerCase();
	
			if(previous != null){
				previous.after = node;
				node.before = previous;
			}
			previous = node;
	
			this.addVertex(node);
		}
	}

	public String toDot(boolean showEdgeLabels){
		StringBuilder sb = new StringBuilder("digraph G {");
		
		for(DocNode node: this.vertexSet()){
			sb.append(node.id).append("[").append("fontcolor=\"").append(node.fontColor).append("\", ").append("pencolor=\"").append(node.penColor).append("\", ").append("label=\"").append(node.word).append("\"];");
		}

		for(DocEdge edge: this.edgeSet()){
			DocNode source = (DocNode) edge.getSource();
			DocNode target = (DocNode) edge.getTarget();
			sb.append(source.id).append("->").append(target.id);
			if(showEdgeLabels)
				sb.append("[label=\"").append(edge.toString()).append("\", ").append("fontcolor=\"").append(edge.fontColor).append("\", ").append("color=\"").append(edge.penColor).append("\"]");
			sb.append(";");

		}

		sb.append("}");

		return sb.toString();
	}

}