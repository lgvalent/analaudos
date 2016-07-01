package br.com.valentin.analaudos.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class AnalaudosTextStatistics {

	public static void main(String[] args) throws Exception {
		if(args.length == 0){
			System.out.println("Usage: java ... mainClass %1\n Where %1 is a filename with text reports, one per line, to be processed.");
			System.exit(1);
		}
		
		Map<String, DescriptiveStatistics> acc = CogrooUtil.createAccumulator();
		
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		
		int limit = 10;
		String line = br.readLine();
		while(line != null && limit-- > 0){
			System.out.println(line);
			
			Map<String, Integer> unit = CogrooUtil.getPOSStatictics(line);
			
			CogrooUtil.joinStats(acc, unit);
			
			line = br.readLine();
		}
		
		br.close();
		
		CogrooUtil.showStatistcs("GERAL", acc);
	}

}
