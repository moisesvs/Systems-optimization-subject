package Algorithm;

import java.util.Random;

import Application.Constants;
import Application.CutWidthInstance;
import Application.SolutionCutWidth;
import Constructive.RandomSolution;
import LocalSearch.LocalSearch;

public class Algorithm {
	
	/**
	 * The best solution find algorithm
	 */
	protected SolutionCutWidth bestSolution;
	
	/**
	 * The random solution generator
	 */
	protected RandomSolution randomGenerator;
	
	/**
	 * The local search algorithm
	 */
	protected LocalSearch localSearch;
	
	/**
	 * CutWidth instance
	 */
	protected CutWidthInstance instance;
	
	/**
	 * Number connections
	 */
	protected int connections;
	
	/**
	 * Number nodes 1
	 */
	protected int numNodes1;
	
	/**
	 * Number nodes 2
	 */
	protected int numNodes2;
	
	/**
	 * Random number to create neighbors
	 */
	protected Random randomNumber;
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 */
	public Algorithm (RandomSolution randomGenerator, CutWidthInstance instance){
		long timeFirst = System.currentTimeMillis();
		
		bestSolution = executeAlgorithmTime(randomGenerator, instance, Constants.TIME_ALGORITHM_MILISECONDS);
		
		long totalTime = System.currentTimeMillis() - timeFirst;
		// set time in solution
		bestSolution.setTimeFindSolution(totalTime);
	}
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 * @param numSolution
	 */
	public Algorithm (RandomSolution randomGenerator, CutWidthInstance instance, int numSolutionEvalute){
		long timeFirst = System.currentTimeMillis();

		bestSolution = executeAlgorithmNumSolution (randomGenerator, instance, numSolutionEvalute);

		long totalTime = System.currentTimeMillis() - timeFirst;
		// set time in solution
		bestSolution.setTimeFindSolution(totalTime);
	}
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 */
	public Algorithm (RandomSolution randomGenerator, CutWidthInstance instance, LocalSearch localSearch){
		long timeFirst = System.currentTimeMillis();
		
		bestSolution = executeAlgorithmLocalSearchTime(randomGenerator, instance, localSearch, Constants.TIME_ALGORITHM_MILISECONDS);
		
		long totalTime = System.currentTimeMillis() - timeFirst;
		// set time in solution
		bestSolution.setTimeFindSolution(totalTime);
	}
	
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 */
	public Algorithm (CutWidthInstance instance, LocalSearch localSearch){
		this.instance = instance;
		this.localSearch = localSearch;
		this.connections = instance.getConnections();
		this.numNodes1 = instance.getNodes1();
		this.numNodes2 = instance.getNodes2();
		this.randomNumber = new Random();
//		
//		long timeFirst = System.currentTimeMillis();
//		
//		bestSolution = executeAlgorithmLocalSearchTime(randomGenerator, instance, localSearch, Constants.TIME_ALGORITHM_MILISECONDS);
//		
//		long totalTime = System.currentTimeMillis() - timeFirst;
//		// set time in solution
//		bestSolution.setTimeFindSolution(totalTime);
	}
	
	/**
	 * Execute algorithm with set solutions
	 * @param randomGenerator generator random
	 * @param instance of problem
	 * @param numSolutionsEvalute number solutions to assess 
	 * @return
	 */
	private SolutionCutWidth executeAlgorithmNumSolution (RandomSolution randomGenerator, CutWidthInstance instance, int numSolutionsAssess){
		
		// Max solution
		float bestSolutionValue = Float.MAX_VALUE;
		SolutionCutWidth analizingSolution = randomGenerator.createSolution();
		SolutionCutWidth bestSolution = analizingSolution;

		for (int i = 0; i < numSolutionsAssess; i ++){
			
			float valueFuntionObjetive = instance.getValueObjetiveFunction(analizingSolution);
			
			if (instance.bestValueObjetiveFunction(bestSolutionValue, valueFuntionObjetive)){
				bestSolutionValue = valueFuntionObjetive;
				bestSolution = analizingSolution;
				bestSolution.setValueFunctionObjetive(valueFuntionObjetive);
			}
			
			analizingSolution = randomGenerator.createSolution();

		}
		
		return bestSolution;
	}
	
	/**
	 * Execute algorithm the time parameter
	 * @param randomGenerator generator random
	 * @param instance of problem
	 * @param timeAlgorithm time will be running the algorithm
	 * @return SolutionMMDP a solution feasible to the problem instance
	 */
	private SolutionCutWidth executeAlgorithmTime (RandomSolution randomGenerator, CutWidthInstance instance, float timeAlgorithm){
		
		// Max solution
		float bestSolutionValue = Float.MAX_VALUE;
		SolutionCutWidth analizingSolution = randomGenerator.createSolution();
		SolutionCutWidth bestSolution = analizingSolution;

		long timeIni = System.currentTimeMillis();
		for (;;){
			
			float valueFuntionObjetive = instance.getValueObjetiveFunction(analizingSolution);
			
			if (instance.bestValueObjetiveFunction(bestSolutionValue, valueFuntionObjetive)){
				bestSolutionValue = valueFuntionObjetive;
				bestSolution = analizingSolution;
				bestSolution.setValueFunctionObjetive(valueFuntionObjetive);
			}
			
			analizingSolution = randomGenerator.createSolution();
			
			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_ALGORITHM_MILISECONDS)
				break;
		}
		
		return bestSolution;
	}

	/**
	 * Execute algorithm the time parameter
	 * @param instance instance CutWidth
	 * @param timeAlgorithm time will be running the algorithm
	 * @return SolutionCutWidth a solution feasible to the problem instance
	 */
	private SolutionCutWidth executeAlgorithmLocalSearchTime (RandomSolution randomGenerator, CutWidthInstance instance, LocalSearch localSearch, float timeAlgoritm){

		// Max solution
		float bestSolutionValue = Float.MAX_VALUE;
		SolutionCutWidth analizingSolution = randomGenerator.createSolution();
		SolutionCutWidth bestSolution = analizingSolution;

		long timeIni = System.currentTimeMillis();

		for (;;){
			// method improvement return solution improvement
			SolutionCutWidth solutionImprovement = localSearch.executeLocalSearchAlgorithm(analizingSolution, timeIni);
			
			float valueFuntionObjetive = instance.getValueObjetiveFunction(solutionImprovement);
			
			if (instance.bestValueObjetiveFunction(bestSolutionValue, valueFuntionObjetive)){
				bestSolutionValue = valueFuntionObjetive;
				bestSolution = analizingSolution;
				bestSolution.setValueFunctionObjetive(valueFuntionObjetive);
			}
			
			analizingSolution = randomGenerator.createSolution();

			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_ALGORITHM_MILISECONDS)
				break;
		}
		
		return bestSolution;
	}
	
	// get and sets
	public SolutionCutWidth getBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(SolutionCutWidth bestSolution) {
		this.bestSolution = bestSolution;
	}
}