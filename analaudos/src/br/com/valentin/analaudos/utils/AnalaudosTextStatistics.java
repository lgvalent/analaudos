package br.com.valentin.analaudos.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import br.com.orionsoft.monstrengo.core.exception.BusinessMessage;


public class AnalaudosTextStatistics {

	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();

		if(args.length == 0){
			System.out.println("Usage: java ... mainClass %1 [%2] [%3]\n Where:\n%1 is a filename with text reports, one per line.\n%2 is the number of lines to be processed. Default is all lines.\n%3 is the number of thread used in executor. Default is Runtime.availbleProcessors/2.");
			System.exit(1);
		}
		int limit = args.length>1?Integer.parseInt(args[1]):100000;
		int numberOfThreads = args.length>2?Integer.parseInt(args[2]):Runtime.getRuntime().availableProcessors()/2;

		final Map<String, DescriptiveStatistics> acc = CogrooUtil.createAccumulator();
	

		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String line = br.readLine();
		
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

		System.out.println(String.format("Runtime settings:\n %d lines\n %d thread(s)\n File: %s", limit, numberOfThreads, args[0]));
		while(line != null && limit-- > 0){
			
			final String localLine = line;
			
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					Map<String, Integer> unit = CogrooUtil.getPOSStatictics(localLine);
					
					CogrooUtil.joinStats(acc, unit);
					
				}
			});
			
			line = br.readLine();
		}
		
		br.close();
		
		executor.shutdown();
		if(!executor.awaitTermination(8000, TimeUnit.SECONDS))
			throw new RuntimeException("Time out for Executor...");

		CogrooUtil.showStatistcs("GERAL", acc);
		
		System.out.println("All documents processed in "+ ((System.nanoTime() - time) / 1000000) + "ms.");
	}

}
