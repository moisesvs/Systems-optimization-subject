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
	 * Number aspirantions
	 */
	private int aspirationsNumber;
	
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
	 * Four position if tabu or not tabu. 0 not tabu, 1 is tabu
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
		this.kIterationsTabu = instance.getNumNodes() - 1;
		this.aspirationsNumber = Constants.ASPIRATIONS_NUMBER_OBJECTIVE;
		this.tabuShortTermList = new int [instance.getNumNodes()];
		this.tabuLongTermList = new int [instance.getNumNodes()];
		this.tablaTabuFitnessNeighbors = new double [(instance.getNumNodes() * instance.getNumNodesSelection()) - 
		                                             instance.getNumNodesSelection() * instance.getNumNodesSelection()][4];
		
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

			markNodesTabu();
			
			int positionFitnessBestValue = bestNodeFitness();
			
			// condition break
			if (positionFitnessBestValue == Constants.NOT_IMPROVE){
				System.out.println("No se puede mejorar");
//				analizingSolution = resetAlgorithm(chooseNodesNeighbors);
				break;
			}
			
			// reset middle time
//			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_RESTART_MILISECONDS){
//				analizingSolution = resetAlgorithm(chooseNodesNeighbors);
//			}
			
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
		
		while (!(exitNeighbor(bestSolutionMMDP, chooseNodesNeighbors))){

			// create one neighbor
			SolutionMMDP neighborhoodSolution = createNeighbor(bestSolutionMMDP, chooseNodesNeighbors);
			double otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);

			// fill table fitness
			tablaTabuFitnessNeighbors[node][0] = positionOrigin;
			tablaTabuFitnessNeighbors[node][1] = positionDestination;
			tablaTabuFitnessNeighbors[node][2] = otherValueSolution;
			tablaTabuFitnessNeighbors[node][3] = Constants.NOT_TABU;

			node ++;
		}

	}
	
	/**
	 * Method delete nodes list tabu
	 */
	private void markNodesTabu (){
		for (int i = 0; i < tabuShortTermList.length; i ++){
			if (tabuShortTermList [i] > 0){
				// find delete nodes fitness
				for (int j = 0;  j < tablaTabuFitnessNeighbors.length; j ++){
					double [] tabuNode = tablaTabuFitnessNeighbors[j];
					if ((tabuNode[0] == i) || (tabuNode[1] == i)){
						// Fill tabu node
						tabuNode[3] = Constants.TABU;
					}
				}
			}
		}
	}
	
	/**
	 * Reset the tabu search with table short memory
	 * @return
	 */
	private SolutionMMDP resetAlgorithm (boolean [] chooseNodesNeighbors){
		boolean [] solutionConstruction = new boolean [instance.getNumNodes()];
		
		for (int i = 0; i < instance.getNumNodesSelection(); i ++){
			int node = getBestNodeMemoryShort(solutionConstruction);
			solutionConstruction[node] = true;
		}
		
		SolutionMMDP solutionAux = new SolutionMMDP(instance, solutionConstruction);
		initAlgorithm(solutionAux, chooseNodesNeighbors);
		
		return solutionAux;
	}
	
	/**
	 * Get the best node for solution construction
	 * @param solutionConstruction
	 * @return
	 */
	private int getBestNodeMemoryShort(boolean [] solutionConstruction){
		int maxNode = -1;
		int indexNode = -1;
		
		for (int i = 0; i < tabuLongTermList.length; i++){
			if (tabuLongTermList[i] > 0){
				maxNode = tabuLongTermList[i];
				indexNode = i;
			}
		}
		
		if (maxNode == -1){
			return randomNumber.nextInt(instance.getNumNodes());
		}
		
		tabuLongTermList[indexNode] = 0;
		return indexNode;
	}
	
	/**
	 * Method choose the best node fitness
	 */
	private int bestNodeFitness (){
		double bestValueFitness = Double.MIN_VALUE;
		int positionBestValue = Constants.NOT_IMPROVE;
		
		for (int i = 0; i < tablaTabuFitnessNeighbors.length; i ++){
			if ((instance.bestValueObjetiveFunction(bestValueFitness, tablaTabuFitnessNeighbors[i][2]))){
				
				if (tablaTabuFitnessNeighbors[i][3] == Constants.TABU){
					if (aspirationsNumber != 0){
						// find best nodes fitness
						bestValueFitness = tablaTabuFitnessNeighbors[i][2];
						positionBestValue = i;
					}
				} else {
					// set
					bestValueFitness = tablaTabuFitnessNeighbors[i][2];
					positionBestValue = i;
				}
			}
		}

		if ((positionBestValue != Constants.NOT_IMPROVE) && (tablaTabuFitnessNeighbors[positionBestValue][3] == Constants.TABU))
			aspirationsNumber --;
		
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
		
		// set frecuently node
		tabuLongTermList[positionDestinationChange] = tabuLongTermList[positionDestinationChange] + 1;
		
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
			tablaTabuFitnessNeighbors[i][3] = 0;
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