package ga.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ga.config.ConfigurationsGA;

public class ProbabilityMatrix {
	
	private ArrayList<double[][]> probabilityMatrices;
	int numMatrices;
	static Random rand = new Random();
	
	public ProbabilityMatrix()
	{
		ProbabilitiesMatrixGeneration(ConfigurationsGA.SIZE_CHROMOSOME);
	}
	
	public void ProbabilitiesMatrixGeneration(int numMatrices)
	{
		//System.out.println("Creating Matrix");
		this.numMatrices=numMatrices;
		probabilityMatrices=new ArrayList<double[][]>();
		for(int i=1;i<=numMatrices;i++)
		{			
			probabilityMatrices.add(ProbabilitiesMatrixInitialization(i,ConfigurationsGA.QTD_SCRIPTS));
			//printMatrix(probabilityMatrices.get(i-1));
		}
		
		

	}
	public double[][] ProbabilitiesMatrixInitialization(int tamVector, int tamScripts)
	{
		double[][] matrix= new double[tamScripts][tamVector];
		for(int i=0;i<tamScripts;i++)
		{
			for(int j=0; j<tamVector; j++)
			{
				matrix[i][j]=1/(double)tamScripts;
			}
		}
		return matrix;
	}
	public void updateMatrix(ArrayList<Population> populations, int indexProbMatrix )
	{
		//System.out.println("updating matrix");
		Population populationToUpdate=populations.get(indexProbMatrix);
		sortByValue(populationToUpdate.getChromosomes());
		
		for(int i=0;i<ConfigurationsGA.NUMBER_OF_VECTORS_TO_UPDATE_FROM;i++)
		{
			for(int j=0;j<indexProbMatrix+1;j++)
			{
				//System.out.println("updating matrix "+j);
				Chromosome ch=(Chromosome)populationToUpdate.getChromosomes().keySet().toArray()[i];
				//ch.print();
				int bestElement=ch.getGenes().get(j);												
				probabilityMatrices.get(indexProbMatrix)[bestElement][j]=probabilityMatrices.get(indexProbMatrix)[bestElement][j]+ConfigurationsGA.LEARNING_RATE;
				

				for(int k=0;k<ConfigurationsGA.QTD_SCRIPTS;k++)
				{
					if(k!=bestElement)
					{
						probabilityMatrices.get(indexProbMatrix)[k][j]=probabilityMatrices.get(indexProbMatrix)[k][j]-(ConfigurationsGA.LEARNING_RATE/((double)ConfigurationsGA.QTD_SCRIPTS-1));
					}
				}
				
			}
		}
		//printMatrix(probabilityMatrices.get(indexProbMatrix));
	}
	public void printMatrix(double[][] matrix)
	{
		for(int i=0;i<matrix.length;i++)
		{
			for(int j=0; j<matrix[i].length; j++)
			{
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println("");
		}
	}
	public void printVector(double[] vector)
	{
		for(int i=0;i<vector.length;i++)
		{
			System.out.println(vector[i]+" ");
		}
	}
	public int selectionFromProbabilityMatrix(double[] vector)
	{
		double sum=0;
		int j=0;
		int selected=-1;		
		double fraction=rand.nextInt(100);
		fraction=fraction/100;
		while(selected<0 && j<vector.length)
		{
			sum=sum+(vector[j]);
			if(sum>fraction)
			{
				selected=j;
			}
			else
			{
				j=j+1;
			}
		}
		return j;
	}

	/**
	 * @return the probabilityMatrices
	 */
	public ArrayList<double[][]> getProbabilityMatrices() {
		return probabilityMatrices;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List list = new LinkedList(map.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
			if(sortedHashMap.size()==ConfigurationsGA.SIZE_POPULATION)
			{
				break;
			}
		} 
		return sortedHashMap;

	}

}
