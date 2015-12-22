package linkanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;


public class DataReader {
	private static String DATA_PATH = "D:/matrix.txt";
	private static String RESULT_PATH = "D:/result.txt";
	private static int web_num;
	HashMap<Integer, Integer> col_num = new HashMap<Integer,Integer>();
	
    public HashMap<Integer, HashMap<Integer, Double>> initializeMatrix(String data) {
		Matrix<Double> matrix = new Matrix<Double>();
		HashMap<Integer, HashMap<Integer, Double>> row_element = new HashMap<Integer, HashMap<Integer, Double>>();
    	try {
			BufferedReader inputStream = new BufferedReader(new FileReader(data));
			String line = inputStream.readLine();
			while(line != null) {
				line = line.replace(',', ' ');
				line = line.replace('(', ' ');
				line = line.replace(')', ' ');
				line = line.trim();
				String[] array = line.split(" ");
				int node1 = Integer.parseInt(array[0]);
				int node2 = Integer.parseInt(array[1]);
			
				if (col_num.containsKey(node1 - 1)) {
					int value = col_num.get(node1 - 1) + 1;
					col_num.put(node1 - 1, value);
				}
				else {
					col_num.put(node1 - 1, 1);
				}
				Triple triple = new Triple(node2 - 1, node1 - 1, 1);
				//System.out.println(triple);
				matrix.add(triple);
				line = inputStream.readLine();
			}
			inputStream.close();
			for (Object t : matrix) {
				@SuppressWarnings("unchecked")
				Triple<Double> tr = (Triple<Double>) t;
				//System.out.println("tr: " + t);
				int col = tr.getCol();
				int row = tr.getRow();
				//System.out.println("col: " + col);
				double length = col_num.get(col);
				//System.out.println("length: " + length);
				tr.setElement(1 / length);
				//System.out.println(tr.getElement());
				double element = tr.getElement();
				if(row_element.containsKey(row)) {
					row_element.get(row).put(col, element);
				}
				else {
					row_element.put(row, new HashMap<Integer, Double>());
					row_element.get(row).put(col, element);
					//System.out.println(row_element.get(row).get(col));
				}
				
				
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
    	web_num = col_num.size();
		System.out.println("initialize Matrix from file finished");
		return row_element;
	}
    
    public void saveMatrixResults(String writeAddr,HashMap<Integer, HashMap<Integer, Double>> matrix) {
		File file = new File(writeAddr);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			for (int row = 0; row < matrix.size(); row++) {
				if (matrix.containsKey(row)){
					HashMap rowElement = matrix.get(row);
					Set colSet = rowElement.keySet();
					for (Object col : colSet) {
						writer.println("row: " + row + "col:" +col + " " + (double) rowElement.get(col));

					}
					
				}
			}
			System.out.println("save ranked results finished...");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

    public static void main(String[] args) {
    	DataReader read = new DataReader();
    	HashMap<Integer, HashMap<Integer, Double>> matrix = read.initializeMatrix(DATA_PATH);
    	if (matrix == null) {
    		System.out.println("print matrix");
    	}
    	PageRank pr = new PageRank(web_num );
    	double[] random_vector = new double[matrix.size()];
		double[] initial_vector = new double[matrix.size()];
		for (int i = 0; i < initial_vector.length; i++) {
			initial_vector[i] = (1.0 / web_num);
		}
		for (int i = 0; i < random_vector.length; i++) {
			random_vector[i] = 0;
		}
		read.saveMatrixResults(RESULT_PATH, matrix);
		double[] vector = pr.computePageRank(matrix,random_vector,initial_vector);
    	PriorityQueue<Double> queue = new PriorityQueue<Double>(5);
    	for (int i = 0; i < 5; i++) {
    		queue.add(vector[i]);
    	}
    	for (int i = 5; i < vector.length; i++) {
    		if (vector[i] > queue.peek()) {
    			queue.remove();
    			queue.add(vector[i]);
    		}
    	}
    	while(!queue.isEmpty()) {
    		System.out.println("poll");
    		double value = queue.poll();
    		for (int i = 0; i < vector.length; i++) {
    			if(vector[i] == value) {
    				System.out.println("id : " + ( i + 1));
    			}
        	}
    	}
    	System.out.println("Done");
    }
}
