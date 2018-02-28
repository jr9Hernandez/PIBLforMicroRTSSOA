package ga.model;

import java.util.ArrayList;

import ga.config.ConfigurationsGA;

public class ProbabilityMatrix {
	
	ArrayList<double[][]> ProbabilityMatrices;
	//int numMatrices;
	
	public ProbabilityMatrix()
	{
		
	}
	
	public ArrayList<double[][]> ProbabilitiesMatrixGeneration(int numMatrices)
	{
		ProbabilityMatrices=new ArrayList<double[][]>();
		for(int i=numMatrices;i>0;i--)
		{			
			ProbabilityMatrices.add(ProbabilitiesMatrixInitialization(i,ConfigurationsGA.QTD_SCRIPTS));
		}
		
		//printMatrix(ProbabilityMatrices.get(5));
		
		return ProbabilityMatrices;
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

}
