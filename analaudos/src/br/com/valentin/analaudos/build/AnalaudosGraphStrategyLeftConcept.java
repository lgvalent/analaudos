package br.com.valentin.analaudos.build;

import br.com.valentin.analaudos.build.AnalaudosDocument.DocEdge;
import br.com.valentin.analaudos.build.AnalaudosDocument.DocNode;
import br.com.valentin.analaudos.build.AnalaudosGraph.AnalaudosEdge;
import br.com.valentin.analaudos.build.AnalaudosGraph.AnalaudosNode;

public class AnalaudosGraphStrategyLeftConcept implements AnalaudosGraphStrategy{
	
	public void createDocGraph(AnalaudosDocument docGraph, AnalaudosGraph graph) {
		log.debug("1st phase: inside sentence and left concept match");
		for(DocNode docNodeSource: docGraph.vertexSet()){
			
			DocNode leftDocNodeTarget = docNodeSource.before;
			DocNode rightDocNodeTarget = docNodeSource.after;
			
			while(leftDocNodeTarget!=null || rightDocNodeTarget!=null){
				if(leftDocNodeTarget != null){
					String interceptedPonctuations = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, leftDocNodeTarget);
					if(interceptedPonctuations.contains(".")){ // Begin sentence reached
						leftDocNodeTarget = null;
					}else{
						double linkScore = linkResolve(docNodeSource, leftDocNodeTarget, graph); 
						if(linkScore < Double.MAX_VALUE){
							createEdge(docGraph, docNodeSource, leftDocNodeTarget, linkScore, "green");
							
							// Break right check
//							rightDocNodeTarget = null;
//							leftDocNodeTarget = null;
							leftDocNodeTarget = leftDocNodeTarget.before;
						}else{
							leftDocNodeTarget = leftDocNodeTarget.before;
						}
					}

				}
				if(rightDocNodeTarget != null){
					String interceptedPonctuations = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, rightDocNodeTarget);
					if(interceptedPonctuations.contains(".")){ // Begin sentence reached
						rightDocNodeTarget = null;
					}else{
						double linkScore = linkResolve(docNodeSource, rightDocNodeTarget, graph); 
						if(linkScore < Double.MAX_VALUE){
							createEdge(docGraph, docNodeSource, rightDocNodeTarget, linkScore, "lime");
							
							// Break right check
							// Break right check
//							rightDocNodeTarget = null;
//							leftDocNodeTarget = null;
							rightDocNodeTarget = rightDocNodeTarget.after;
						}else{
							rightDocNodeTarget = rightDocNodeTarget.after;
						}
						
					}
				}
			}
		}
	}

	/**
	 * @param docGraph
	 * @param docNodeSource
	 * @param docNodeTarget
	 * @param linkScore
	 */
	private void createEdge(AnalaudosDocument docGraph, DocNode docNodeSource,	DocNode docNodeTarget, double linkScore, String color) {
		DocEdge docEdge = docGraph.addEdge(docNodeSource, docNodeTarget);
		docEdge.wordDistance = docNodeTarget.index - docNodeSource.index;
		docEdge.interceptedPonctuations = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, docNodeTarget);
		
		docEdge.phraseDistance= AnalaudosDocument.checkPhraseDistance(docEdge.interceptedPonctuations);

		docEdge.linkScore = linkScore;
		docEdge.penColor = color;
	}

	/**
	 * @param docNodeSource
	 * @param docNodeTarget
	 * @return Calculate the reliability of a link between two nodes
	 * 
	 * @version 20161115: Uses neighborhood relationship inside and left outside sentence
	 */
	private double linkResolve(DocNode docNodeSource, DocNode docNodeTarget, AnalaudosGraph graph) {
		double result = Double.MAX_VALUE;
		StringBuilder sb = new StringBuilder(); 
		sb.append('[').append(docNodeSource.index).append(':').append(docNodeTarget.index).append(']').append('\t').append(docNodeSource.word).append('\t').append("->").append(docNodeTarget.word);
		AnalaudosNode analNodeSource = graph.retrieveNode(docNodeSource);
		AnalaudosNode analNodeTarget= graph.retrieveNode(docNodeTarget);
		if(analNodeSource != null && analNodeTarget != null){
			for(AnalaudosEdge analEdge: graph.getAllEdges(analNodeSource, analNodeTarget)){
				// Analyze WordDistance feature
				double wordDistance = analEdge.wordDistance.getMean() - (docNodeTarget.index - docNodeSource.index);
				
				if(wordDistance < result)
					result = wordDistance;
				
				sb.append('\t').append("wd[delta=").append(wordDistance).append(":mean=").append(analEdge.wordDistance.getMean()).append(":var=").append(analEdge.wordDistance.getVariance()).append(":sd=").append(analEdge.wordDistance.getStandardDeviation()).append(']').append("[LINKED]");
			}
		}
		
		log.debug(sb.toString());
		return result;
	}

}