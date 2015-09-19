package edu.pitt.sis.exp.colfusion.bll.similarityMeasures;

public interface SimilarityDistanceMeasure {
	
	//public double computeSimilarity(double value1, double value2);
	
	//public double computeSimilarity(int value1, int value2);
	
	public double computeSimilarity(String value1, String value2);
	
	//public double computeDistance(double value1, double value2);
	
	//public double computeDistance(int value1, int value2);
	
	public double computeDistance(String value1, String value2);
}
