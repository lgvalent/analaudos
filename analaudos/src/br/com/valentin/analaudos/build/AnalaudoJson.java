package br.com.valentin.analaudos.build;

import java.util.List;

public class AnalaudoJson{
	private static final long serialVersionUID = 1L;

	public static class Node{
		private static final long serialVersionUID = 1L;

		public String id;
		public String fontColor;
		public String label;
		public String word;
		
		@Override
		public String toString() {
			return label;
		}
	}
	
	public List<Node> nodes;
	public List<List<String>> edges;
	public double version;
}