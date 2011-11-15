package Application;

import Constructive.RandomSolution;
import LocalSearch.LocalSearch;

public class Algorithm {
	
	/**
	 * The best solution find algorithm
	 */
	private SolutionMMDP bestSolution;
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 */
	public Algorithm (RandomSolution randomGenerator, MMDPInstance instance){
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
	public Algorithm (RandomSolution randomGenerator, MMDPInstance instance, int numSolutionEvalute){
		long timeFirst = System.currentTimeMillis();

		bestSolution = executeAlgorithmNumSolution (randomGenerator, instance, numSolutionEvalute);

		long totalTime = System.currentTimeMillis() - timeFirst;
		// set time in solution
		bestSolution.setTimeFindSolution(totalTime);
	}
	
	/**
	 * Execute algorithm with random generator and local search (best improvement or first improvement)
	 * time 1 minute with local search
	 * @param randomGenerator generator random
	 * @param localSearch
	 */
	public Algorithm (RandomSolution randomGenerator, MMDPInstance instance, LocalSearch localSearch){
		// configure local search
		localSearch.setInstance(instance);
		
		long timeFirst = System.currentTimeMillis();

		bestSolution = executeAlgorithmLocalSearchTime (randomGenerator, instance, localSearch, Constants.TIME_ALGORITHM_MILISECONDS);
		
		long totalTime = System.currentTimeMillis() - timeFirst;
		// set time in solution
		bestSolution.setTimeFindSolution(totalTime);
	}
	
	
	/**
	 * Execute algorithm with set solutions
	 * @param randomGenerator generator random
	 * @param instance of problem
	 * @param numSolutionsEvalute number solutions to assess 
	 * @return
	 */
	private SolutionMMDP executeAlgorithmNumSolution (RandomSolution randomGenerator, MMDPInstance instance, int numSolutionsAssess){
		
		// Max solution
		double bestSolutionValue = Double.MIN_VALUE;
		SolutionMMDP analizingSolution = randomGenerator.createSolution();
		SolutionMMDP bestSolution = analizingSolution;

		for (int i = 0; i < numSolutionsAssess; i ++){
			double valueFuntionObjetive = instance.getValueObjetiveFunction(analizingSolution);
			
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
	private SolutionMMDP executeAlgorithmTime (RandomSolution randomGenerator, MMDPInstance instance, float timeAlgorithm){
		
		// Max solution
		double bestSolutionValue = Double.MIN_VALUE;
		SolutionMMDP analizingSolution = randomGenerator.createSolution();
		SolutionMMDP bestSolution = analizingSolution;

		long timeIni = System.currentTimeMillis();

		for (;;){
			double valueFuntionObjetive = instance.getValueObjetiveFunction(analizingSolution);
			
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
	 * @param instance
	 * @param timeAlgorithm time will be running the algorithm
	 * @return SolutionMMDP a solution feasible to the problem instance
	 */
	private SolutionMMDP executeAlgorithmLocalSearchTime (RandomSolution randomGenerator, MMDPInstance instance, LocalSearch localSearch, float timeAlgoritm){

		// Max solution
		double bestSolutionValue = Double.MIN_VALUE;
		SolutionMMDP analizingSolution = randomGenerator.createSolution();
		SolutionMMDP bestSolution = analizingSolution;

		long timeIni = System.currentTimeMillis();

		for (;;){
			// method improvement return solution improvement
			SolutionMMDP solutionImprovement = localSearch.executeLocalSearchAlgorithm(analizingSolution);
			
			double valueFuntionObjetive = instance.getValueObjetiveFunction(solutionImprovement);
			
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
	public SolutionMMDP getBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(SolutionMMDP bestSolution) {
		this.bestSolution = bestSolution;
	}
}