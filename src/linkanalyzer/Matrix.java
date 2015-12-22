package linkanalyzer;

import java.util.Arrays;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Sparse matrix
 * @author zhangge
 *
 * @param <E>
 */
public class Matrix<E> implements Iterable<E>{
	@Override
	public String toString() {
		return Arrays.toString(data);
	}
	Triple<E>[] data;
	int[] first;
	int m;
	int n;
	int size;//非空元素个数
	public Matrix() {
		// TODO Auto-generated constructor stub
		this(10);
	}
	@SuppressWarnings("unchecked")
	public Matrix(int size) {
		// TODO Auto-generated constructor stub
		this.data = new Triple[size];
	}
	public Matrix(Triple<E>[] data, int[] first, int m, int n, int len) {
		super();
		this.data = data;
		this.first = first;
		this.m = m;
		this.n = n;
		this.size = len;
	}
	public boolean add(Triple<E> triple){  
        ensureCapacity(size + 1);   
        data[size++] = triple;    //size++  
        return true;  
    }
	public void ensureCapacity(int minCapacity) {  
        int oldCapacity = data.length;  
        if (minCapacity > oldCapacity) { 
            int newCapacity = oldCapacity *  2 + 1;    
            if (newCapacity < minCapacity)   
                newCapacity = minCapacity;  
            data = Arrays.copyOf(data, newCapacity);  
        }  
    }
	/**
	 * Compress the two-dimensional matrix using Triples
	 * @param e:A two-dimensional matrix
	 * @return Compressed Matrix
	 */
	
	public Matrix<E> compressMatrix(E[][] e){
		Matrix<E> matrix = new Matrix<E>();
		int m = e.length;
		int n = e[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (e[i][j] != null ) {
					if (e[i][j] instanceof Integer) {
						if ((Integer) e[i][j] != 0) {
							Triple<E> triple = new Triple<E>(i, j, e[i][j]);
							matrix.add(triple);
						}
					}
					else {
						Triple<E> triple = new Triple<E>(i, j, e[i][j]);
						matrix.add(triple);	
					}
					
				}
			}
		}
		return matrix;
		
	}
	
	public Matrix<E> transMatrix(Matrix<E> matrix) {
		Matrix<E> transmatrix = new Matrix<E>();
		transmatrix.m = matrix.m;
		transmatrix.n = matrix.n;
		if(matrix.size == 0) return null;  
        for(int col=0; col < matrix.n; col++) {  //根据m的列的顺序排列转换矩阵的顺序  
            for(int p=0;p < matrix.size; p++) {  
                if(matrix.data[p].col == col){  
                    Triple<E> triple = new Triple<E>(matrix.data[p].col, matrix.data[p].row, (E)matrix.data[p].element);  
                    transmatrix.add(triple);  
                }  
            }  
        }  
        
		return transmatrix;
	}
	
	public void remove(int index) {
    	rangeCheck(index);
    	int numMoved = size - index - 1;
    	if (numMoved > 0) {
    		System.arraycopy(data, index + 1, data, index, numMoved);
    	}
    	data[--size] = null;
    }
    private void rangeCheck(int index) {
    	if (index >= size) {
    		try {
    			throw new Exception();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
	public Iterator<E> iterator() {
		return new MyIter();
	}	
    private class MyIter implements Iterator<E> {
		private int cursor = 0;
		private int lastRet = -1;
		public boolean hasNext() {
            return cursor != size;
        }
		
		@SuppressWarnings("unchecked")
		public E next() {
			if (cursor >= size)
	        	throw new NoSuchElementException();
	        Object[] elementData = Matrix.this.data;
	        if (cursor >= elementData.length)
	        	throw new ConcurrentModificationException();
			lastRet = cursor;
			return  (E) elementData[cursor++];
		}
		
		public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                Matrix.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

	}
}
/**
 * 
 *
 *
 * @param <E>
 */
class Triple<E> implements Comparable<Triple>{
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	int row;
	int col;
	E element;
	public Triple() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "[" + row + "," + col + "," + element + "]";
	}

	public Triple(int node1, int node2, E element) {
		super();
		this.row = node1;
		this.col = node2;
		this.element = element;
	}
	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}

	@Override
	public int compareTo(Triple o) {
		int row1 = this.getRow();
		int row2 = o.getRow();
		return Integer.compare(row1, row2);
	}

	
}
