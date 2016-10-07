package br.com.valentin.analaudos.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.com.valentin.analaudos.utils.CogrooUtil.TextStatistics;

/**
 * Analyze and generate texts statistics using coogro POStags and the word itself.
 * To use this class in command line,
 * Enter in WEB-INF directory and type this command:
 * java -classpath classes/:lib/* br.com.valentin.analaudos.utils.AnalaudosTextStatistics
 * 
 *  If all work, a manual usage will be shown.
 *  
 *  Use the tee command to save output to file and console at the same time:
 *  first_command 2>&1 | tee outfile
 *  
 * @author lucio
 *
 */
public class AnalaudosTextStatistics {

	public static void main(String[] args) throws Exception {
		
		long time = System.nanoTime();

		if(args.length == 0){
			System.out.println("Usage: java ... AnalaudosTextStatistics %1 [%2] [%3]\n Where:\n%1 is a filename (in UTF-8) with text reports, one per line.\n%2 is the number of lines to be processed. Default is all lines.\n%3 is the number of thread used in executor. Default is Runtime.availbleProcessors/2.");
			System.exit(1);
		}
		int limit = args.length>1?Integer.parseInt(args[1]):100000;
		int numberOfThreads = args.length>2?Integer.parseInt(args[2]):Runtime.getRuntime().availableProcessors()/2;
		int lineCount = 0;

		final TextStatistics textStatistics = new TextStatistics();
	
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0])), Charset.forName("UTF-8")));
		String line = br.readLine();
		
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

		System.out.println(String.format("Runtime settings:\n %d lines\n %d thread(s)\n File: %s", limit, numberOfThreads, args[0]));
		while(line != null && limit-- > 0){
			lineCount++;
			
			final String localLine = line;
			
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					textStatistics.addDoc(localLine);
				}
			});
			
			line = br.readLine();
		}
		
		br.close();
		
		executor.shutdown();
		if(!executor.awaitTermination(8000, TimeUnit.SECONDS))
			throw new RuntimeException("Time out for Executor...");

		CogrooUtil.showStatistcs("GERAL POS Tags:" + lineCount, textStatistics.getPosStatistics());
		CogrooUtil.showStatistcs("GERAL WORDS_POS:" + lineCount, textStatistics.getWordPosStatistics());
		CogrooUtil.showStatistcs("GERAL WORDS:" + lineCount, textStatistics.getWordStatistics());
		
		System.out.println("All documents processed in "+ ((System.nanoTime() - time) / 1000000) + "ms.");
	}

}
