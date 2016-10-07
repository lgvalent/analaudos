package br.com.valentin.analaudos.utils;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.sun.swing.internal.plaf.synth.resources.synth;

import br.com.valentin.analaudos.build.AnalaudosBuilderTest;

/**
 * Good for apply a Dictionary (ignored and replaced) to a content. 
 *  
 * @author Lucio 20130601
 *  
 */
public class CogrooUtil{
	
	public static class TextStatistics{
		private static Analyzer analyzer = CogrooUtil.getInstance("pt_BR");

		private Map<String, DescriptiveStatistics> posStatistics = new TreeMap<String, DescriptiveStatistics>();
		private Map<String, DescriptiveStatistics> wordPosStatistics = new TreeMap<String, DescriptiveStatistics>();
		private Map<String, DescriptiveStatistics> wordStatistics = new TreeMap<String, DescriptiveStatistics>();
		/** TODO Impacto da contagem de stemmer do corpus para afirma que há poucas palavras, já que 
		 * os atuais dados endicam palavras com POS diferentes ou ainda singular/plural masculino/feminino */
		private Map<String, DescriptiveStatistics> stemmStatistics = new TreeMap<String, DescriptiveStatistics>();

		public void addDoc(String text){
			long time = System.nanoTime();

			CheckDocument document = new CheckDocument();
			document.setText(text);

			analyzer.analyze(document);

			Map<String, Integer> posValues = new HashMap<String, Integer>();
			Map<String, Integer> wordPosValues = new HashMap<String, Integer>();
			Map<String, Integer> wordValues = new HashMap<String, Integer>();
			
			for(Sentence sentence: document.getSentences()){
				for(Token token: sentence.getTokens()){
//					System.out.println(token.getPOSTag() + ":" + sentence.getText().substring(token.getStart(), token.getEnd()));
					addStatistics(token.getPOSTag(), token, posValues);
					addStatistics(sentence.getText().substring(token.getStart(), token.getEnd()).toLowerCase()+":"+token.getPOSTag(), token, wordPosValues);
					addStatistics(sentence.getText().substring(token.getStart(), token.getEnd()).toLowerCase(), token, wordValues);
				}
			}
			
			// Join parcial results
			addStatistics(posValues, posStatistics);
			addStatistics(wordPosValues, wordPosStatistics);
			addStatistics(wordValues, wordStatistics);

			System.out.println("Document processed in "+ ((System.nanoTime() - time) / 1000000) + "ms.");
		}
		
		private void addStatistics(String key, Token token, Map<String, Integer> values){
			if(token.getPOSTag().equals("num"))
				key = token.getPOSTag();
			
			Integer value = values.get(key);
			if(value == null){
				values.put(key, 1);
			}else{
				values.put(key, value+1);
			}
		}

		private synchronized void addStatistics(Map<String, Integer> values, Map<String, DescriptiveStatistics> totals){
			for(Entry<String, Integer> entry: values.entrySet()){
				DescriptiveStatistics count = totals.get(entry.getKey());

				if(count == null){
					count = new DescriptiveStatistics();
					totals.put(entry.getKey(), count);
				}
				
				count.addValue(entry.getValue());
			}
		}

		public Map<String, DescriptiveStatistics> getPosStatistics(){
			return this.posStatistics;
		}

		public Map<String, DescriptiveStatistics> getWordStatistics(){
			return this.wordStatistics;
		}

		public Map<String, DescriptiveStatistics> getWordPosStatistics(){
			return this.wordPosStatistics;
		}
}
	
	
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
		long time = System.nanoTime();
		
		TextStatistics textStatistics = new TextStatistics();
		textStatistics.addDoc(AnalaudosBuilderTest.prepareDocumentGraphTest1().getDocumentContent().getContentPlain());
		showStatistcs("DOC 1: POS stats", textStatistics.getPosStatistics());
		showStatistcs("DOC 1: WORD stats", textStatistics.getWordStatistics());

		textStatistics.addDoc(AnalaudosBuilderTest.prepareDocumentGraphTest2().getDocumentContent().getContentPlain());
		showStatistcs("DOC 2: POS stats", textStatistics.getPosStatistics());
		showStatistcs("DOC 2: WORD stats", textStatistics.getWordStatistics());
		
		System.out.println("TEST: Using class, finished in "+ ((System.nanoTime() - time) / 1000000) + "ms.");
	}

	public static void showStatistcs(String label, Map<?,?> map){
		PrintStream out;
		try {
			out = new PrintStream(System.out, true, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out = new PrintStream(System.out, true);
		}
		
		out.println("::::: " + label + " :::::");
		out.println("POS\t n\t sum\t mean\t min\t max\t sd\t var");
		for(Entry<?,?> entry: map.entrySet()){
			out.print(entry.getKey() + "\t");
			Object value = entry.getValue();
			if(value instanceof DescriptiveStatistics){
				DescriptiveStatistics values = (DescriptiveStatistics) value;
				out.print(values.getN());out.print("\t");
				out.print(values.getSum());out.print("\t");
				out.print(values.getMean());out.print("\t");
				out.print(values.getMin());out.print("\t");
				out.print(values.getMax());out.print("\t");
				out.print(values.getStandardDeviation());out.print("\t");
				out.print(values.getVariance());out.print("\t");
				out.println();
			}else{
				out.println(value);
			}
		}
		out.println(":::::  :::::");
	}
	
}

