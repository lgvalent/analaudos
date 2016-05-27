package br.com.valentin.analaudos.build;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgraph.graph.DefaultEdge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AnalaudosDocument extends DirectedGraph<AnalaudosDocument.DocNode, AnalaudosDocument.DocEdge>{
	private static final long serialVersionUID = 1L;

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
			this.label = node.label;
			this.word = node.word.trim(); // 20160526: Some JSONs are keeping leading and trailing whitespaces 
		}

		public String id;
		public int index; // Use id to extract index: w10 -> 10
		public String fontColor;
		public String label;
		public String word;

		/** Like a linked list */
		public AnalaudosDocument.DocNode before;
		public AnalaudosDocument.DocNode after;

		@Override
		public String toString() {
			return label;
		}
	}

	public static class DocEdge extends DefaultEdge{
		private static final long serialVersionUID = 1L;

		public String id;
		public int wordsDistance;
		public int linkLatency;
		public String interceptedPonctuations = "";
		public double reliability = 0;

		public boolean interceptPonctuation(){return !this.interceptedPonctuations.isEmpty();}

		@Override
		public String toString() {
			return super.toString() + "[wd:" + wordsDistance + ", ll:"+ linkLatency +"ms, ip:"+ interceptedPonctuations  +", r:" + String.format("%.2f", reliability) + "%]";
		}
	}

	public void addActionLogData(String actionLog){
	
		for(DocEdge e: this.edgeSet()){
			DocNode source = (DocNode) e.getSource();
			DocNode target = (DocNode) e.getTarget();
	
			/* Time between clicks */
			e.linkLatency = retrieveCreateEdgeLatency(source.id, target.id, actionLog);
	
			/* Check ponctuation between nodes */ 
			e.interceptedPonctuations = "";
			DocNode temp = source;
			while(temp != target){
				if(e.wordsDistance > 0)
					temp = temp.after;
				else
					temp = temp.before;
	
				if(temp == null) break;
	
				if(temp.label.contains("."))
					e.interceptedPonctuations += '.';
				if(temp.label.contains(","))
					e.interceptedPonctuations += ',';
				if(temp.label.contains(";"))
					e.interceptedPonctuations += ';';
				if(temp.label.contains(":"))
					e.interceptedPonctuations += ':';
				if(temp.label.contains("!"))
					e.interceptedPonctuations += '!';
				if(temp.label.contains("?"))
					e.interceptedPonctuations += '?';
			}
		}
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
	
			edge.wordsDistance = target.index - source.index;
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
		DocNode before = null;
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
	
			if(before != null)node.before = before;
			if(previous != null)previous.after = node;
			previous = node;
	
			this.addVertex(node);
		}
	}

	public String toDot(boolean showEdgeLabels){
		StringBuilder sb = new StringBuilder("digraph G {");
		
		for(DocNode node: this.vertexSet()){
			sb.append(node.id).append("[").append("fontcolor=\"").append(node.fontColor).append("\", ").append("label=\"").append(node.word).append("\"];");
		}

		for(DocEdge edge: this.edgeSet()){
			DocNode source = (DocNode) edge.getSource();
			DocNode target = (DocNode) edge.getTarget();
			sb.append(source.id).append("->").append(target.id);
			if(showEdgeLabels)
				sb.append("[label=\"").append(edge.toString()).append("\"]");
			sb.append(";");

		}

		sb.append("}");

		
		return sb.toString();
	}

}