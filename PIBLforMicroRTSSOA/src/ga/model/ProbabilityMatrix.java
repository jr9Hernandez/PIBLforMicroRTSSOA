package ga.model;

import java.util.ArrayList;
import java.util.Random;

import ga.config.ConfigurationsGA;

public class ProbabilityMatrix {
	
	private ArrayList<double[][]> probabilityMatrices;
	//int numMatrices;
	static Random rand = new Random();
	
	public ProbabilityMatrix()
	{
		probabilityMatrices=ProbabilitiesMatrixGeneration(ConfigurationsGA.SIZE_CHROMOSOME);
	}
	
	public ArrayList<double[][]> ProbabilitiesMatrixGeneration(int numMatrices)
	{
		probabilityMatrices=new ArrayList<double[][]>();
		for(int i=numMatrices;i>0;i--)
		{			
			probabilityMatrices.add(ProbabilitiesMatrixInitialization(i,ConfigurationsGA.QTD_SCRIPTS));
		}
		
		//printMatrix(ProbabilityMatrices.get(5));
		
		return probabilityMatrices;
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
	public void updateMatrix()
	{
		
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

}
