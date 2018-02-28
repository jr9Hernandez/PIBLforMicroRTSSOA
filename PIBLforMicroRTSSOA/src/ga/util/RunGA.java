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
	private ArrayList<double[][]> probabilityMatrices; 
	
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
		populations=new ArrayList<Population>();
		for(int i=ConfigurationsGA.SIZE_CHROMOSOME;i>0;i--)
		{
			population = Population.getInitialPopulation(i, probabilityMat);
			System.out.println("new");
			population.printWithValue();
			populations.add(population);
		}
		
		//Fase 2 = avalia a população
		HashMap<Chromosome, BigDecimal> ChromosomesCompletePopulation =  new HashMap<>();
		Population completePopulation=new Population(ChromosomesCompletePopulation);
		
		for(int i=0;i<ConfigurationsGA.SIZE_CHROMOSOME;i++)
		{			
			ChromosomesCompletePopulation.putAll(populations.get(i).getChromosomes());
		}
		completePopulation.setChromosomes(ChromosomesCompletePopulation);
		completePopulation = evalFunction.evalPopulation(completePopulation,this.generations);
		for(int i=0;i<ConfigurationsGA.SIZE_CHROMOSOME;i++)
		{			
			updateFitnessPopulation(populations.get(i),completePopulation);
			System.out.println("newUpdated");
			populations.get(i).printWithValue();
		}
		
		
		
		resetControls();
		//Fase 3 = critério de parada
		while(continueProcess()){
			
			//Fase 4 = Seleção (Aplicar Cruzamento e Mutação)
			Selection selecao = new Selection();
			population = selecao.applySelection(population);
			
			//Repete-se Fase 2 = Avaliação da população
			population = evalFunction.evalPopulation(population,this.generations);
			
			//atualiza a geração
			updateGeneration();
		}
		
		return population;
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
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
}
