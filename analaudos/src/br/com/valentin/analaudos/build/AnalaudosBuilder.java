package br.com.valentin.analaudos.build;

import java.util.Arrays;

import org.apache.log4j.Logger;

import br.com.valentin.analaudos.build.AnalaudosDocument.DocEdge;
import br.com.valentin.analaudos.build.AnalaudosDocument.DocNode;
import br.com.valentin.analaudos.build.AnalaudosGraph.AnalaudosEdge;
import br.com.valentin.analaudos.build.AnalaudosGraph.AnalaudosNode;

public class AnalaudosBuilder{
	private final Logger log = Logger.getLogger(getClass());

	private AnalaudosGraph analaudosGraph; 
	
	private static final long serialVersionUID = 1L;
	
	public AnalaudosBuilder(AnalaudosGraph analaudosGraph) {
		this.analaudosGraph = analaudosGraph;
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
					docEdge.wordsDistance = docNodeTarget.index - docNodeSource.index;
					docEdge.interceptedPonctuations = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, docNodeTarget);
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
		AnalaudosNode analNodeSource = this.analaudosGraph.retrieveNode(docNodeSource);
		AnalaudosNode analNodeTarget= this.analaudosGraph.retrieveNode(docNodeTarget);
		if(analNodeSource != null && analNodeTarget != null){
			AnalaudosEdge analEdge = this.analaudosGraph.getEdge(analNodeSource, analNodeTarget);
			if(analEdge != null){
				// Analyze WordDistance feature
				double wordDistanceWeight = 0;
				double wordDistance = analEdge.wordDistance.getMean() - (docNodeTarget.index - docNodeSource.index);
				sb.append('\t').append('[').append(wordDistance).append(':').append(analEdge.wordDistance.getMean()).append(':').append(analEdge.wordDistance.getVariance()).append(':').append(analEdge.wordDistance.getStandardDeviation()).append(']');
				sb.append(Arrays.toString(analEdge.wordDistance.getValues()));
				
				if(wordDistance == 0)
					wordDistanceWeight = 1;
				else if(Math.abs(wordDistance) > (analEdge.wordDistance.getStandardDeviation()*2))
					wordDistanceWeight = -1;
				else
					wordDistanceWeight = (Math.abs(wordDistance) / (analEdge.wordDistance.getStandardDeviation()*2));
				
				// Analyze Ponctuation
				double interceptPonctuationWeight = 0;
				if(wordDistanceWeight > 0){
					String ponctuation = AnalaudosDocument.checkIntercepPonctuation(docNodeSource, docNodeTarget);
					interceptPonctuationWeight = analEdge.interceptedPonctuations.getMean() - ponctuation.length();
					if(interceptPonctuationWeight == 0)
						interceptPonctuationWeight = 1;
					else if(Math.abs(interceptPonctuationWeight) > (analEdge.interceptedPonctuations.getStandardDeviation()*2))
						interceptPonctuationWeight = -1;
					else
						interceptPonctuationWeight = (Math.abs(interceptPonctuationWeight) / (analEdge.interceptedPonctuations.getStandardDeviation()*2));
					sb.append("[ip:").append(analEdge.interceptedPonctuations.getMean()).append('-').append(ponctuation.length()).append('=').append(interceptPonctuationWeight).append("]");
				}
				
				result = wordDistanceWeight + interceptPonctuationWeight;
				

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
	
	
	public void joinAnalaudoGraph(AnalaudosGraph analaudosGraph){

	}
	
}