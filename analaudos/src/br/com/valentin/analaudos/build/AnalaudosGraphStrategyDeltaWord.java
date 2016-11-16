package br.com.valentin.analaudos.build;

import br.com.valentin.analaudos.build.AnalaudosDocument.DocEdge;
import br.com.valentin.analaudos.build.AnalaudosDocument.DocNode;
import br.com.valentin.analaudos.build.AnalaudosGraph.AnalaudosEdge;
import br.com.valentin.analaudos.build.AnalaudosGraph.AnalaudosNode;

public class AnalaudosGraphStrategyDeltaWord implements AnalaudosGraphStrategy{
	
	public void createDocGraph(AnalaudosDocument docGraph, AnalaudosGraph graph) {
		
		log.debug("1st phase: positive linkScore...");
		for(DocNode docNodeSource: docGraph.vertexSet()){
			for(DocNode docNodeTarget: docGraph.vertexSet()){
				double linkScore = linkResolve(docNodeSource, docNodeTarget, graph); 
				if(linkScore > 0){
					DocEdge docEdge = docGraph.addEdge(docNodeSource, docNodeTarget);
					docEdge.wordDistance = docNodeTarget.index - docNodeSource.index;
					docEdge.interceptedPonctuations = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, docNodeTarget);
					
					docEdge.phraseDistance= AnalaudosDocument.checkPhraseDistance(docEdge.interceptedPonctuations);

					docEdge.linkScore = linkScore;
				}
			}
		}
		
		log.debug("2nd phase: leaf with possible link, lookup max negative score");
		for(DocNode docNodeSource: docGraph.vertexSet()){
			if(docGraph.outDegreeOf(docNodeSource)==0){
				double linkScore = 0;
				DocNode leftDocNode = docNodeSource.before;
				DocNode rightDocNode = docNodeSource.after;
				DocNode foundDocNode = null;
				
				while(leftDocNode != null || rightDocNode != null){
					if(leftDocNode != null){
						linkScore = linkResolve(docNodeSource, leftDocNode, graph);
						if(0 > linkScore && linkScore > Double.NEGATIVE_INFINITY){
							foundDocNode = leftDocNode;
							break;
						}else{
							leftDocNode = leftDocNode.before;
						}
					}

					if(rightDocNode != null){
						linkScore = linkResolve(docNodeSource, rightDocNode, graph);
						if(0 > linkScore && linkScore > Double.NEGATIVE_INFINITY){
							/* Break the right flow on end of sentence */
							String ponctuations = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, rightDocNode);
							if(ponctuations.contains(".")){
								rightDocNode = null;
							}else{
								foundDocNode = rightDocNode;
								break;
							}
						}else{
							rightDocNode = rightDocNode.after;
						}
					}
				}
				
				if(foundDocNode !=null){
					log.debug("Linking: " + docNodeSource + "->" + foundDocNode + String.format("%.2f", linkScore));
					DocEdge docEdge = docGraph.addEdge(docNodeSource, foundDocNode);
					docEdge.wordDistance = foundDocNode.index - docNodeSource.index;
					docEdge.interceptedPonctuations = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, foundDocNode);
					
					docEdge.linkScore = linkScore;
				}
			}
		}
	}

	/**
	 * @param docNodeSource
	 * @param docNodeTarget
	 * @return Calculate the reliability of a link between two nodes
	 * 
	 * @version 20160520: Uses wordDistance and standard deviation to resolve link
	 */
	private double linkResolve(DocNode docNodeSource, DocNode docNodeTarget, AnalaudosGraph graph) {
		double result = 0.0f;
		StringBuilder sb = new StringBuilder(); 
		sb.append('[').append(docNodeSource.index).append(':').append(docNodeTarget.index).append(']').append('\t').append(docNodeSource.word).append('\t').append("->").append(docNodeTarget.word);
		AnalaudosNode analNodeSource = graph.retrieveNode(docNodeSource);
		AnalaudosNode analNodeTarget= graph.retrieveNode(docNodeTarget);
		if(analNodeSource != null && analNodeTarget != null){
			for(AnalaudosEdge analEdge: graph.getAllEdges(analNodeSource, analNodeTarget)){
				// Analyze WordDistance feature
				double wordDistance = analEdge.wordDistance.getMean() - (docNodeTarget.index - docNodeSource.index);
				double wordStandardDeviation = analEdge.wordDistance.getStandardDeviation();
				double wordDistanceWeight = 1 - Math.abs(wordDistance) / ((wordStandardDeviation>0?wordStandardDeviation:1)*2);
				
				sb.append('\t').append("wd[delta=").append(wordDistance).append(":mean=").append(analEdge.wordDistance.getMean()).append(":var=").append(analEdge.wordDistance.getVariance()).append(":sd=").append(analEdge.wordDistance.getStandardDeviation()).append(":weight=").append(wordDistanceWeight).append(']');
//				sb.append(" wdValues").append(Arrays.toString(analEdge.wordDistance.getValues()));

				result = wordDistanceWeight;
				
				// Analyze Ponctuation
				double interceptPonctuationWeight = 0;
				if(wordDistanceWeight > 0){
					String ponctuation = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, docNodeTarget);
					double interceptPonctuation = analEdge.interceptedPonctuations.getMean() - ponctuation.length();
					interceptPonctuationWeight = 1 - Math.abs(interceptPonctuation) / (analEdge.interceptedPonctuations.getStandardDeviation()*2);
					sb.append(" ip[len=").append(ponctuation.length()).append(":mean=").append(analEdge.interceptedPonctuations.getMean()).append(":weight=").append(interceptPonctuationWeight).append("]");
				}
				// interceptPonctuationWight may be NAN -> ?/0
//				if(!Double.isNaN(interceptPonctuationWeight)) 
//					result += interceptPonctuationWeight;;
				
				sb.append(" result=").append(result);
				
				if(result > 0){
					docNodeSource.fontColor = analNodeSource.fontColor;
					docNodeSource.penColor = analNodeSource.penColor;
					docNodeTarget.fontColor = analNodeTarget.fontColor;
					docNodeTarget.penColor = analNodeTarget.penColor;
					sb.append("[LINKED]");
					
				}
				log.debug(sb.toString());

				if(result > 0) break;
			}
		}
		
		return result;
	}

}