package br.com.valentin.analaudos.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import opennlp.tools.cmdline.TerminateToolException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.analyzer.InitializationException;
import org.cogroo.checker.CheckDocument;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;

import br.com.valentin.analaudos.build.AnalaudosBuilderTest;

/**
 * Good for apply a Dictionary (ignored and replaced) to a content. 
 *  
 * @author Lucio 20130601
 *  
 */
public class CogrooUtil{
	
	
	/** 
	 * Convert locale string pt_BR in Locale instance
	 */
	public static Locale getLocale(String langCode){
		String[] codes = langCode.split("_"); 
		String lang = codes[0];
		String country = codes.length>1?codes[1]:codes[0];

		return new Locale(lang, country);
	}

	/**
	 * 
	 * @param langCode a pt_BR string
	 * @return
	 */
	public static Analyzer getInstance(String langCode){
		long time = System.nanoTime();
		/* Creating CoGROO instance */
		ComponentFactory factory;

		try {
			factory = ComponentFactory.create(getLocale(langCode));
		} catch(InitializationException e) {
			e.printStackTrace();
			throw new TerminateToolException(1, "Could not find configuration for "
					+ langCode + ". Only " + new Locale("pt", "BR") + " might be supported for now.");
		}
		
		System.out.println("Cogroo loaded in "+ ((System.nanoTime() - time) / 1000000) + "ms.");

		return factory.createPipe();
	}
	
	public static List<Sentence> analyseText(String text, Analyzer analyzer){
		CheckDocument document = new CheckDocument();
		document.setText(text);
		analyzer.analyze(document);
		
		return document.getSentences();
	}
	
	/**
	 * Analyze all terms in text and generates a list with theirs tokens.
	 * @param text
	 * @param analyzer
	 * @return
	 */
	public static List<Token> analyseTermsTokens(String text, Analyzer analyzer){
		List<Token> result = new ArrayList<Token>(2);

		for(Sentence sentence: CogrooUtil.analyseText(text, analyzer)){
			for(Token token: sentence.getTokens()){
				result.add(token);
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		test();
	}
	
	private static void test() {
		
		Map<String, Integer> stats1 = getPOSStatictics(AnalaudosBuilderTest.prepareDocumentGraphTest1().getDocumentContent().getContentPlain());
		showStatistcs("DOC 1", stats1);

		Map<String, Integer> stats2 = getPOSStatictics(AnalaudosBuilderTest.prepareDocumentGraphTest2().getDocumentContent().getContentPlain());
		showStatistcs("DOC 2", stats2);
		
		Map<String, DescriptiveStatistics> results = createAccumulator();
		joinStats(results, stats1);
		joinStats(results, stats2);
		
		showStatistcs("JOINED", results);
}
	public static void showStatistcs(String label, Map<?,?> map){
		System.out.println("::::: " + label + " :::::");
		for(Entry<?,?> entry: map.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println(":::::  :::::");
	}
	
	public static Map<String, Integer> getPOSStatictics(String text){
		long time = System.nanoTime();
		TreeMap<String, Integer> result = new TreeMap<String, Integer>();

		Analyzer analyzer = CogrooUtil.getInstance("pt_BR");
		
		CheckDocument document = new CheckDocument();
		document.setText(text);

		analyzer.analyze(document);
		
		for(Sentence sentence: document.getSentences()){
			for(Token token: sentence.getTokens()){
				Integer count = result.get(token.getPOSTag());
				if(count == null){
					result.put(token.getPOSTag(), 1);
				}else{
					result.put(token.getPOSTag(), count+1);
					
				}
			}
		}

		System.out.println("Document processed in "+ ((System.nanoTime() - time) / 1000000) + "ms.");
		
		return result;
	}
	
	
	public static Map<String, DescriptiveStatistics> createAccumulator(){
		return new TreeMap<String, DescriptiveStatistics>();
	}
	
	public static void joinStats(Map<String, DescriptiveStatistics> accumulator, Map<String, Integer> unit){
		for(Entry<String, Integer> entry: unit.entrySet()){
			DescriptiveStatistics count = accumulator.get(entry.getKey());

			if(count == null){
				count = new DescriptiveStatistics();
				accumulator.put(entry.getKey(), count);
			}
			
			count.addValue(entry.getValue());
		}
	}
}

