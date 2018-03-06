package ga.util;


import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ga.config.ConfigurationsGA;
import ga.model.Chromosome;
import ga.model.Population;
import ga.model.ProbabilityMatrix;
import ga.util.Evaluation.RatePopulation;

public class RunGA {
	
	private Population population;
	private ArrayList<Population> populations;
	private ProbabilityMatrix probabilityMat;
	Population completePopulation;
	
	private Instant timeInicial;
	private int generations=0;
	
	/**
	 * Este metodo aplicará todas as fases do processo PIBL
	 * @param evalFunction Será a função de avaliação que desejamos utilizar
	 */
	public Population run(RatePopulation evalFunction){
		
		//Fase 0 = gerar os vetores de probabilidades	
		probabilityMat=new ProbabilityMatrix();
				
		//Fase 1 = gerar a população inicial 
		newPopulation();
		
		//Fase 2 = avalia a população
		evaluatePopulation(evalFunction);
		
		
		resetControls();
		//Fase 3 = critério de parada
		while(continueProcess()){
			
			
			for(int i=0;i<ConfigurationsGA.SIZE_CHROMOSOME;i++)
			{
				//Fase 4 = Atualiza probabilities
				probabilityMat.updateMatrix(populations,i);
			
				//Fase 5 - Mutacao
				//Selection selecao = new Selection();
				//selecao.Mutation(populations.get(i));			
			}
			
			//Fase 6 - Geracao nova populacao
			newPopulation();
			
			//Repete-se Fase 2 = Avaliação da população
			evaluatePopulation(evalFunction);
			
			//atualiza a geração
			updateGeneration();
		}
		
		for(int i=0;i<ConfigurationsGA.SIZE_CHROMOSOME;i++)
		{
			probabilityMat.printMatrix(probabilityMat.getProbabilityMatrices().get(i));
		}
		
		return completePopulation;
	}
	
	private void updateGeneration(){
		this.generations++;
	}
	
	private boolean continueProcess() {
		switch (ConfigurationsGA.TYPE_CONTROL) {
		case 0:
			return hasTime();
			
		case 1:
			return hasGeneration();
				
		default:
			return false;
		}
		
	}


	private boolean hasGeneration() {
		if(this.generations < ConfigurationsGA.QTD_GENERATIONS){
			return true;
		}
		return false;
	}


	/**
	 * Função que inicia o contador de tempo para o critério de parada
	 */
	protected void resetControls(){
		this.timeInicial = Instant.now();
		this.generations = 0;
	}
	
	protected boolean hasTime(){
		Instant now = Instant.now();
		
		
		Duration duracao = Duration.between(timeInicial, now);
				
		//System.out.println( "Horas " + duracao.toMinutes());
		
		if(duracao.toHours() < ConfigurationsGA.TIME_GA_EXEC){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void updateFitnessPopulation(Population p,  Population completep)
	{
	    Iterator it = completep.getChromosomes().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
	        if(p.getChromosomes().containsKey(pair.getKey()))
	        {
	        	p.getChromosomes().put((Chromosome)pair.getKey(), (BigDecimal)pair.getValue());
	        }
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public void newPopulation()
	{
		//System.out.println("New Big Population");
		populations=new ArrayList<Population>();
		for(int i=1;i<=ConfigurationsGA.SIZE_CHROMOSOME;i++)
		{
			//System.out.println("pop "+i);
			population = Population.getInitialPopulation(i, probabilityMat);
			//System.out.println("end pop");
			//population.printWithValue();
			populations.add(population);
		}
	}
	public void evaluatePopulation(RatePopulation evalFunction)
	{
		HashMap<Chromosome, BigDecimal> ChromosomesCompletePopulation =  new HashMap<>();
		completePopulation=new Population(ChromosomesCompletePopulation);
		
		for(int i=0;i<ConfigurationsGA.SIZE_CHROMOSOME;i++)
		{			
			ChromosomesCompletePopulation.putAll(populations.get(i).getChromosomes());
		}
		completePopulation.setChromosomes(ChromosomesCompletePopulation);
		completePopulation = evalFunction.evalPopulation(completePopulation,this.generations);
		for(int i=0;i<ConfigurationsGA.SIZE_CHROMOSOME;i++)
		{			
			updateFitnessPopulation(populations.get(i),completePopulation);
			//populations.get(i).printWithValue();
		}
		
	}
}
