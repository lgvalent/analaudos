package br.com.valentin.analaudos.build;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AnalaudosValidation{
	private final Logger log = Logger.getLogger(getClass());

	private AnalaudosGraph analaudosGraph; 

	private static final long serialVersionUID = 1L;

	private List<AnalaudosDocument.DocEdge> foundedSet;
	private List<AnalaudosDocument.DocEdge> foundedErrorSet;
	private List<AnalaudosDocument.DocEdge> notFoundedSet;
	private AnalaudosDocument analaudosDocument;
	
	private double precision;
	private double recall;
	private double fMeasure;

	public AnalaudosValidation(AnalaudosGraph analaudosGraph) {
		this.analaudosGraph = analaudosGraph;
	}

	/**
	 * Analyze text using current AnalaudosGraph and compare created edges with json validation data
	 * @param text Content to be analyzed
	 * @param json  JSON representing content
	 * @return F-measure from validation
	 */
	public double validate(String text, String json){

		AnalaudosDocument docGraph = new AnalaudosDocument(json);

		this.analaudosDocument = this.analaudosGraph.createDocGraph(text);

		log.debug("----------Starting validation --------------------");
		this.foundedSet = new ArrayList<AnalaudosDocument.DocEdge>();
		this.foundedErrorSet = new ArrayList<AnalaudosDocument.DocEdge>();
		this.notFoundedSet = new ArrayList<AnalaudosDocument.DocEdge>();
		this.notFoundedSet.addAll(docGraph.edgeSet());

		for(AnalaudosDocument.DocEdge cEdge: this.analaudosDocument.edgeSet()){
			boolean founded = false;
			for(AnalaudosDocument.DocEdge gEdge: docGraph.edgeSet()){
				founded =  cEdge.getSource().equals(gEdge.getSource()) && cEdge.getTarget().equals(gEdge.getTarget());
				if(founded){
					notFoundedSet.remove(gEdge);
					break;
				}
			}

			if(founded){
				cEdge.penColor="green";
				foundedSet.add(cEdge);
				log.debug("Founded:" + cEdge.toString2());
			}else{
				cEdge.penColor="red";
				foundedErrorSet.add(cEdge);
				log.debug("FoundedError:" + cEdge.toString2());
			}
		}

		log.debug(String.format("\n\tFounded:%d\n\tFounded Error:%d\n\tNot Founded:%d\n", foundedSet.size(), foundedErrorSet.size(),  notFoundedSet.size()));

		/** Precision Recall
		 * https://en.wikipedia.org/wiki/Precision_and_recall
		 */
		this.precision = (double)foundedSet.size() / (foundedErrorSet.size() + foundedSet.size());
		this.recall = foundedSet.size() / (notFoundedSet.size()+foundedSet.size());

		this.fMeasure = 2 * precision * recall / (precision+recall); 
		log.debug(String.format("\n\tPrecision:%f\n\tRecall:%f\n\tF-measure:%f", precision, recall,  fMeasure));
		
		return this.fMeasure;
	}

	public AnalaudosGraph getAnalaudosGraph() {return analaudosGraph;}

	public List<AnalaudosDocument.DocEdge> getFoundedSet() {return foundedSet;}

	public List<AnalaudosDocument.DocEdge> getFoundedErrorSet() {return foundedErrorSet;}

	public List<AnalaudosDocument.DocEdge> getNotFoundedSet() {return notFoundedSet;}

	public AnalaudosDocument getAnalaudosDocument() {return analaudosDocument;}


}

