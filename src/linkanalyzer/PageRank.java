package linkanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

public class PageRank {
	double beta = 0.8 ;
	int web_num;
	double precision = 0.0001;
	double random_factor = (1.0 / web_num) * (1 - beta);
    public PageRank(int web_num) {
		// TODO Auto-generated constructor stub
    	this.beta = 0.8;
		this.web_num = web_num;
		this.precision = 0.0001;
		this.random_factor = (1.0 / web_num) * (1 - beta);
		
	}
    
	public boolean check(double vec1[], double vec2[]) {
		double deviation = 0;
	    for(int i = 0; i < web_num; i++) {
	    	deviation += Math.abs(vec1[i] - vec2[i]);
	    }
	    System.out.println("The deviation is " + deviation);   
	    if(deviation <= precision) {
	    	return true;	
	    }    
	    return false;
	}
	public void multiVectorandMatrix(HashMap<Integer, HashMap<Integer, Double>> matrix, double[] vector1, double[] vector2) {
		System.out.println("matrix length" + matrix.size());
		System.out.println("v1 length" + vector1.length);
		System.out.println("vector2 length" + vector2.length);
		for (int row = 0; row < vector1.length; row++) {
			vector2[row] = 0;
			if (matrix.containsKey(row)){
				HashMap rowElement = matrix.get(row);
				Set colSet = rowElement.keySet();
				for (Object col : colSet) {
					vector2[row] += vector1[(int) col] * ((double) rowElement.get(col)) * beta;				}
				
			}
			//System.out.println("random_factor" + random_factor);
			vector2[row] += random_factor;
		}
		//return vector2;
	}
	
	public double[] computePageRank(HashMap<Integer, HashMap<Integer, Double>> matrix,double[] vector1, double[] vector2) {
		//double[] result = new double[vector1.length];
		while (!check(vector1, vector2)) {
			//System.out.println("checked finished!");
			System.arraycopy(vector2, 0, vector1, 0, web_num);
			multiVectorandMatrix(matrix,vector1,vector2);
			saveRankResults("D:/123.txt", vector2);
			
		}
		System.out.println("computing has finished!");
		
		return vector2;
	}

	public void saveRankResults(String writeAddr,double[] vector) {
		File file = new File(writeAddr);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			for(double id : vector) {
				writer.println(id);
			}
			System.out.println("save ranked results finished...");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
