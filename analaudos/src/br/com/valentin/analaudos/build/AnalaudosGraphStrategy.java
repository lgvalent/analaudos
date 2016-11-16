package br.com.valentin.analaudos.build;

import org.apache.log4j.Logger;


public interface AnalaudosGraphStrategy {
	public static final Logger log = Logger.getLogger("AnalaudosGraphStrategy");
	
	public void createDocGraph(AnalaudosDocument document, AnalaudosGraph graph);
	
}