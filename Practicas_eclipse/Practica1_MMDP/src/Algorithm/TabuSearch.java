package Algorithm;

import Application.Constants;
import Application.MMDPInstance;
import Application.SolutionMMDP;
import Constructive.RandomSolution;
import LocalSearch.LocalSearch;

public class TabuSearch extends Algorithm {

	/**
	 * The parameter K iterations of the algorithm tabu search
	 */
	private int kIterationsTabu;
	
	/**
	 * Tabu short term list
	 */
	private int [] tabuShortTermList;
	
	/**
	 * Tabu large term list
	 */
	private int [] tabuLongTermList;
	
	/**
	 * Position origin
	 */
	private int positionOrigin;
	
	/**
	 * Position destination
	 */
	private int positionDestination;
	
	/**
	 * Tabu table exchanges fitness
	 * First position node origin change
	 * Second position node destination change
	 * Third position fitness value
	 */
	private double [][] tablaTabuFitnessNeighbors;
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 * @param numSolution
	 */
	public TabuSearch (RandomSolution randomGenerator, MMDPInstance instance){
		super(randomGenerator,instance);
		
		// init algorithm
		this.kIterationsTabu = instance.getNumNodesSelection();
		this.tabuShortTermList = new int [instance.getNumNodes()];
		this.tabuLongTermList = new int [instance.getNumNodes()];
		this.tablaTabuFitnessNeighbors = new double [(instance.getNumNodes() * instance.getNumNodesSelection()) - 
		                                             instance.getNumNodesSelection() * instance.getNumNodesSelection() ][3];
		
		long timeFirst = System.currentTimeMillis();

		bestSolution = executeAlgorithm(randomGenerator, instance);

		long totalTime = System.currentTimeMillis() - timeFirst;
		
		// set time in solution
		bestSolution.setTimeFindSolution(totalTime);
	}	
	
	/**
	 * Execute algorithm the time parameter
	 * @param randomGenerator generator random
	 * @param instance of problem
	 * @param timeAlgorithm time will be running the algorithm
	 * @return SolutionMMDP a solution feasible to the problem instance
	 */
	private SolutionMMDP executeAlgorithm (RandomSolution randomGenerator, MMDPInstance instance){
		
		long timeIni = System.currentTimeMillis();

		// Max solution
		SolutionMMDP bestSolutionMMDP = randomGenerator.createSolution();
		SolutionMMDP analizingSolution = bestSolutionMMDP;
		boolean [] chooseNodesNeighbors = new boolean[instance.getNumNodes()];

		// create neighborhood
		while (true){
			initAlgorithm(analizingSolution, chooseNodesNeighbors);

			createFitnessNeighborhood(analizingSolution, chooseNodesNeighbors);

			deleteNodesTabu();
			int positionFitnessBestValue = bestNodeFitness();
			
			// condition break
			if (positionFitnessBestValue == Constants.TABU)
				break;
			
			// set bestSolution
			analizingSolution = changeNodes(analizingSolution, positionFitnessBestValue);
			// decrease tabu nodes
			decreaseTabuNodes();
			
			if (instance.bestValueObjetiveFunction(bestSolutionMMDP.getValueFunctionObjetive(), analizingSolution.getValueFunctionObjetive())){
				bestSolutionMMDP = analizingSolution;
			}
			
			// condition break
			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_ALGORITHM_MILISECONDS)
				break;
		}

		
		return bestSolutionMMDP;
	}

	/**
	 * Method create fitness all neighborhood
	 * @param bestSolutionMMDP the solution best
	 * @param chooseNodesNeighbors the neighbors nodes visited
	 */
	private void createFitnessNeighborhood (SolutionMMDP bestSolutionMMDP, boolean [] chooseNodesNeighbors){
		
		int node = 0;
		
		while (! (exitNeighbor(bestSolutionMMDP, chooseNodesNeighbors))){

			// create one neighbor
			SolutionMMDP neighborhoodSolution = createNeighbor(bestSolutionMMDP, chooseNodesNeighbors);
			double otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);

			// fill table fitness
			tablaTabuFitnessNeighbors[node][0] = positionOrigin;
			tablaTabuFitnessNeighbors[node][1] = positionDestination;
			tablaTabuFitnessNeighbors[node][2] = otherValueSolution;
			
			node ++;
		}

	}
	
	/**
	 * Method delete nodes list tabu
	 */
	private void deleteNodesTabu (){
		for (int i = 0; i < tabuShortTermList.length; i ++){
			if (tabuShortTermList [i] > 0){
				// find delete nodes fitness
				for (int j = 0;  j < tablaTabuFitnessNeighbors.length; j ++){
					double [] tabuNode = tablaTabuFitnessNeighbors[j];
					if ((tabuNode[0] == i) || (tabuNode[1] == i)){
						// Fill tabu node
						tablaTabuFitnessNeighbors[j][0] = Constants.TABU;
						tablaTabuFitnessNeighbors[j][1] = Constants.TABU;
						tablaTabuFitnessNeighbors[j][2] = Constants.TABU;
					}
				}
			}
		}
	}
	
	/**
	 * Method choose the best node fitness
	 */
	private int bestNodeFitness (){
		double bestValueFitness = Double.MIN_VALUE;
		int positionBestValue = Constants.TABU;
		
		for (int i = 0; i < tablaTabuFitnessNeighbors.length; i ++){
			if ((tablaTabuFitnessNeighbors[i][2] != Constants.TABU) && instance.bestValueObjetiveFunction(bestValueFitness, tablaTabuFitnessNeighbors[i][2])){
				// find best nodes fitness
				bestValueFitness = tablaTabuFitnessNeighbors[i][2];
				positionBestValue = i;
			}
		}
		
		if (positionBestValue == Constants.TABU)
			System.out.println("TABU");
		return positionBestValue;
	}
	
	/**
	 * Change nodes in solution
	 * @param positionBestValue
	 */
	private SolutionMMDP changeNodes (SolutionMMDP analyzingSolution, int positionBestValue){

		// change node
		boolean [] currentSolutionNodes = analyzingSolution.getSolution().clone();
		
		double [] fitnessValues = tablaTabuFitnessNeighbors[positionBestValue];
		int positionOriginChange = (int)fitnessValues[0];
		int positionDestinationChange = (int)fitnessValues[1];
		double bestSolutionValue = fitnessValues[2];
		
		// set tabu node
		tabuShortTermList[positionOriginChange] = kIterationsTabu;
		
		currentSolutionNodes[positionOriginChange] = false;
		currentSolutionNodes[positionDestinationChange] = true;

		SolutionMMDP analyzingSolutionAux = new SolutionMMDP(instance, currentSolutionNodes);
		analyzingSolutionAux.setValueFunctionObjetive(bestSolutionValue);
		
		return analyzingSolutionAux;
	}
	
	
	private void decreaseTabuNodes (){
		for (int i = 0; i < tabuShortTermList.length; i++){
			if (tabuShortTermList[i] > 0)
				tabuShortTermList[i] -= 1;
		}
	}
	
	/**
	 * Create the neighborhood with a move in the solution
	 * @param currentSolution
	 * @return
	 */
	private SolutionMMDP createNeighbor (SolutionMMDP currentSolution, boolean [] chooseNodesNeighbors){
		// clone solution
		boolean [] currentSolutionNodes = currentSolution.getSolution();
		boolean [] currentSolutionNodesCopy = currentSolution.getSolution().clone();

		// polity first improve change the first node find 
		for(int i = 0; i < currentSolutionNodes.length; i ++){
			// choose node not change last
			positionDestination = i;
			
			if (!chooseNodesNeighbors[positionDestination]){
				chooseNodesNeighbors[positionDestination] = true;
				break;
			}
		}
		
		// change values
		currentSolutionNodesCopy[positionOrigin] = false;
		currentSolutionNodesCopy[positionDestination] = true;
		
		SolutionMMDP solutionNeighbor = new SolutionMMDP(instance, currentSolutionNodesCopy);
		return solutionNeighbor;
	}
	
	/**
	 * Init choose nodes neighbors not selections who neighbors
	 * @param solution
	 * @param chooseNeighbors
	 */
	private void initChooseNodeNeighbors(SolutionMMDP solution, boolean [] chooseNeighbors){
		boolean [] solutionAux = solution.getSolution();
		
		// init choose nodes neighbors not selections who neighbors
		for (int i = 0; i < solutionAux.length; i ++){
			if (solutionAux[i])
				chooseNeighbors[i] = true;
			else 
				chooseNeighbors[i] = false;

		}
	}
	
	/**
	 * Init table tabu fitness neighbors
	 */
	private void initTableTabuFitnessNeighbors(){	
		for (int i = 0; i < tablaTabuFitnessNeighbors.length; i ++){
			tablaTabuFitnessNeighbors[i][0] = 0;
			tablaTabuFitnessNeighbors[i][1] = 0;
			tablaTabuFitnessNeighbors[i][2] = 0;
		}
	}
	
	/**
	 * Select the first node select current
	 * @param solution
	 */
	private void selectFirstOrigin (SolutionMMDP solution){
		boolean [] solutionAux = solution.getSolution();

		for (int i = 0; i < solutionAux.length; i++){
			if (solutionAux[i]){
				positionOrigin = i;
				return;
			}
		}
	}
	
	/**
	 * Method that initialization algorithm
	 */
	private void initAlgorithm (SolutionMMDP solution, boolean [] chooseNodesNeighbors){
		selectFirstOrigin(solution);
		initChooseNodeNeighbors(solution, chooseNodesNeighbors);
		initTableTabuFitnessNeighbors();
	}
	
	/**
	 * If there are neighbors to visit
	 * @param solutionArray
	 * @param chooseNodesNeighbors
	 * @return if there are neighbors to visit
	 */
	private boolean exitNeighbor(SolutionMMDP solution, boolean [] chooseNodesNeighbors){
		// get solution array
		boolean [] solutionArray = solution.getSolution();

		for (int node = 0; node < solutionArray.length; node ++){
			if (!chooseNodesNeighbors[node])
				return false;
		}
		
		boolean flagFind = false;
		// search node origin change
		for (int nodeOrigin = 0; nodeOrigin < solutionArray.length; nodeOrigin ++){
			if (solutionArray[nodeOrigin]){
				
				if (nodeOrigin == positionOrigin){
					flagFind = true;
				} else if (flagFind){
					// change current node origin
					positionOrigin = nodeOrigin;
					// reset neigboors
					initChooseNodeNeighbors(solution, chooseNodesNeighbors);
					return false;
				}
			}
		}
		
		return true;
	}
	
	// get and sets
	/**
	 * @return the kIterationsTabu
	 */
	public int getkIterationsTabu() {
		return kIterationsTabu;
	}

	/**
	 * @param kIterationsTabu the kIterationsTabu to set
	 */
	public void setkIterationsTabu(int kIterationsTabu) {
		this.kIterationsTabu = kIterationsTabu;
	}

	/**
	 * @return the tabuShortTermList
	 */
	public int [] getTabuShortTermList() {
		return tabuShortTermList;
	}

	/**
	 * @param tabuShortTermList the tabuShortTermList to set
	 */
	public void setTabuShortTermList(int [] tabuShortTermList) {
		this.tabuShortTermList = tabuShortTermList;
	}

	/**
	 * @return the tabuLongTermList
	 */
	public int [] getTabuLongTermList() {
		return tabuLongTermList;
	}

	/**
	 * @param tabuLongTermList the tabuLongTermList to set
	 */
	public void setTabuLongTermList(int [] tabuLongTermList) {
		this.tabuLongTermList = tabuLongTermList;
	}
}