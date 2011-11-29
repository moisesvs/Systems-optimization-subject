package Algorithm;

import Application.Constants;
import Application.MMDPInstance;
import Application.SolutionMMDP;
import Constructive.RandomSolution;
import LocalSearch.LocalSearch;

public class VNS extends Algorithm {

	/**
	 * The parameter K the neighbors
	 */
	private int kNeighbors;
	
	/**
	 * Position origin
	 */
	private int positionOrigin1;
	
	/**
	 * Position origin
	 */
	private int positionOrigin2;
	
	/**
	 * Position destination
	 */
	private int positionDestination1;
	
	/**
	 * Position destination
	 */
	private int positionDestination2;
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 * @param numSolution
	 */
	public VNS (RandomSolution randomGenerator, MMDPInstance instance){
		super(randomGenerator,instance);
		
		// init algorithm
		this.kNeighbors = 0;
		
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

		// create neighborhood
		while (true){

			analizingSolution = executeLocalSearch(analizingSolution, timeIni);
			
			if (instance.bestValueObjetiveFunction(bestSolutionMMDP.getValueFunctionObjetive(), analizingSolution.getValueFunctionObjetive())){
				bestSolutionMMDP = analizingSolution;
				this.kNeighbors = 0;
			} else {
				increaseNumberNeighborhood();
			}
			
			// condition break
			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_ALGORITHM_MILISECONDS)
				break;
		}

		
		return bestSolutionMMDP;
	}

//	var
//	//Cada una de las N1 ,. . . , Nk representa una estructura de vecindad
//	x0,x1: TipoSolucion;
//	V : array [. . .] of TipoSolucion;
//	k:integer; //Vecindad
//	begin
//	{x0} := GenerarSolucionInicial ();
//	{V } := GenerarVecindad (N1);
//	repeat
//	k := 1;
//	repeat
//	x1 := BusquedaLocal (x0, V ); //Empieza una búsqueda en Nk
//	if fObjetivo (x1) > fObjetivo (x0) then
//	//Suponiendo que es mejor el mayor valor de fObjetivo, aunque se trate de minimizar
//	//Nuevo mínimo local
//	x0 := x1;
//	k := 1;
//	else
//	k := k + 1;
//	until k = kmax
//	until condicion_parada
//	end

	/**
	 * Create the neighborhood with a move in the solution
	 * @param currentSolution
	 * @return
	 */
	private SolutionMMDP executeLocalSearch (SolutionMMDP currentSolution, long timeIni){
		SolutionMMDP solutionAux = currentSolution;
		
		switch (kNeighbors){
		
			case 0:
					// change two nodes
					solutionAux = executeLocalSearchAlgorithm(currentSolution, timeIni);
					break;
			
			case 1:
					// change two nodes
					solutionAux = executeLocalSearchTwoChangeAlgorithm(currentSolution, timeIni);
					break;
					
		}

		return solutionAux;
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
	
	private void increaseNumberNeighborhood (){
		if (kNeighbors == 0)
			kNeighbors ++;
		else
			kNeighbors = 0;
	}
	
	/**
	 * Select the first node select current
	 * @param solution
	 */
	private void selectFirstOrigin (SolutionMMDP solution){
		boolean [] solutionAux = solution.getSolution();

		for (int i = 0; i < solutionAux.length; i++){
			if (solutionAux[i]){
				positionOrigin1 = i;
				return;
			}
		}
	}
	
	/**
	 * Select the first node select current
	 * @param solution
	 */
	private void selectTwoOrigin (SolutionMMDP solution){
		boolean [] solutionAux = solution.getSolution();
		boolean position1Choose = true;
		for (int i = 0; i < solutionAux.length; i++){
			if (solutionAux[i]){
				if (position1Choose){
					positionOrigin1 = i;
					position1Choose = false;
				} else {
					positionOrigin2 = i;
					return;
				}
			}
		}
	}
	
	/**
	 * Method that initialization algorithm
	 */
	private void initAlgorithm (SolutionMMDP solution, boolean [] chooseNodesNeighbors){
		// Select neighborhood
		increaseNumberNeighborhood();
		
		initChooseNodeNeighbors(solution, chooseNodesNeighbors);
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
				
				if (nodeOrigin == positionOrigin1){
					flagFind = true;
				} else if (flagFind){
					// change current node origin
					positionOrigin1 = nodeOrigin;
					// reset neigboors
					initChooseNodeNeighbors(solution, chooseNodesNeighbors);
					return false;
				}
			}
		}
		
		return true;
	}

	/**
	 * Execute local search algorithm to solution MMDP attribute
	 * @return SolutionMMDP not posible more local search this solution MMDP
	 */
	public SolutionMMDP executeLocalSearchAlgorithm(SolutionMMDP solutionMMDP, long initTime){
		
		boolean [] chooseNodesNeighbors = new boolean[instance.getNumNodes()];
		initChooseNodeNeighbors(solutionMMDP, chooseNodesNeighbors);

		SolutionMMDP bestSolutionMMDP =  solutionMMDP;
		double bestSolutionValue = instance.getValueObjetiveFunction(solutionMMDP);
		selectFirstOrigin(bestSolutionMMDP);

		// create one neighbor
		SolutionMMDP neighborhoodSolution = createNeighbor(solutionMMDP, chooseNodesNeighbors);			
		
		// execute algorithm
		while (true){

			double otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);
			if (instance.bestValueObjetiveFunction(bestSolutionValue, otherValueSolution)){
				
				bestSolutionValue = otherValueSolution;
				bestSolutionMMDP = neighborhoodSolution;
				
				bestSolutionMMDP.setValueFunctionObjetive(bestSolutionValue);
				
				resetNeighbor(neighborhoodSolution, chooseNodesNeighbors);
			}
			
			// check if all neighbor visited
			if ((exitNeighbor(bestSolutionMMDP, chooseNodesNeighbors)) || ((System.currentTimeMillis() - initTime) >= Constants.TIME_ALGORITHM_MILISECONDS))
				break;
			
			// create one neighbor
			neighborhoodSolution = createNeighbor(bestSolutionMMDP, chooseNodesNeighbors);
		}
		
		return bestSolutionMMDP;
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
			positionDestination1 = i;
			
			if (!chooseNodesNeighbors[positionDestination1]){
				chooseNodesNeighbors[positionDestination1] = true;
				break;
			}
		}

		// change values
		currentSolutionNodesCopy[positionOrigin1] = false;
		currentSolutionNodesCopy[positionDestination1] = true;
		
		SolutionMMDP solutionNeighbor = new SolutionMMDP(instance, currentSolutionNodesCopy);
		return solutionNeighbor;
	}

	/**
	 * Execute local search algorithm to solution MMDP attribute
	 * @return SolutionMMDP not posible more local search this solution MMDP
	 */
	public SolutionMMDP executeLocalSearchTwoChangeAlgorithm(SolutionMMDP solutionMMDP, long initTime){
		
		boolean [] chooseNodesNeighbors = new boolean[instance.getNumNodes()];
		initChooseNodeNeighbors(solutionMMDP, chooseNodesNeighbors);

		SolutionMMDP bestSolutionMMDP =  solutionMMDP;
		double bestSolutionValue = instance.getValueObjetiveFunction(solutionMMDP);
		selectTwoOrigin(bestSolutionMMDP);

		// create one neighbor
		SolutionMMDP neighborhoodSolution = createTwoChangeNeighbor(solutionMMDP, chooseNodesNeighbors);			
		
		// execute algorithm
		while (true){

			double otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);
			if (instance.bestValueObjetiveFunction(bestSolutionValue, otherValueSolution)){
				
				bestSolutionValue = otherValueSolution;
				bestSolutionMMDP = neighborhoodSolution;
				
				bestSolutionMMDP.setValueFunctionObjetive(bestSolutionValue);
				
				resetNeighborTwo(neighborhoodSolution, chooseNodesNeighbors);
			}
			
			// check if all neighbor visited
			if ((exitNeighborTwo(bestSolutionMMDP, chooseNodesNeighbors)) || ((System.currentTimeMillis() - initTime) >= Constants.TIME_ALGORITHM_MILISECONDS))
				break;
			
			// create one neighbor
			neighborhoodSolution = createTwoChangeNeighbor(bestSolutionMMDP, chooseNodesNeighbors);
		}
		
		return bestSolutionMMDP;
	}
	
	/**
	 * Create the neighborhood with a move in the solution
	 * @param currentSolution
	 * @return
	 */
	private SolutionMMDP createTwoChangeNeighbor (SolutionMMDP currentSolution, boolean [] chooseNodesNeighbors){
		// clone solution
		boolean [] currentSolutionNodes = currentSolution.getSolution();
		boolean [] currentSolutionNodesCopy = currentSolution.getSolution().clone();
		boolean position1Choose = true;
		
		// polity first improve change the first node find 
		for(int i = 0; i < currentSolutionNodes.length; i ++){
			
			if (position1Choose){			
				// choose node not change last
				positionDestination1 = i;
				
				if (!chooseNodesNeighbors[positionDestination1]){
					chooseNodesNeighbors[positionDestination1] = true;
					position1Choose = false;
				}
				
			} else {
				positionDestination2 = i;
				if (!chooseNodesNeighbors[positionDestination2]){
					chooseNodesNeighbors[positionDestination2] = true;
					break;
				}
			}
		}

		// change values
		currentSolutionNodesCopy[positionOrigin1] = false;
		currentSolutionNodesCopy[positionDestination1] = true;
		
		currentSolutionNodesCopy[positionOrigin2] = false;
		currentSolutionNodesCopy[positionDestination2] = true;
		
		SolutionMMDP solutionNeighbor = new SolutionMMDP(instance, currentSolutionNodesCopy);
		return solutionNeighbor;
	}
	
	/**
	 * If there are neighbors to visit
	 * @param solutionArray
	 * @param chooseNodesNeighbors
	 * @return if there are neighbors to visit
	 */
	private boolean exitNeighborTwo(SolutionMMDP solution, boolean [] chooseNodesNeighbors){
		// get solution array
		boolean [] solutionArray = solution.getSolution();

		for (int node = 0; node < solutionArray.length; node ++){
			if (!chooseNodesNeighbors[node])
				return false;
		}
		
		boolean flagFindNode1 = false;
		boolean flagFindNode2 = false;

		// search node origin change
		for (int nodeOrigin = 0; nodeOrigin < solutionArray.length; nodeOrigin ++){
			if (solutionArray[nodeOrigin]){
				
				if (nodeOrigin == positionOrigin1){
					flagFindNode1 = true;
				} else if (flagFindNode1){
					// change current node origin
					positionOrigin1 = nodeOrigin;
					// reset neigboors
				} else if (nodeOrigin == positionOrigin2){
					flagFindNode2 = true;
				} else if (flagFindNode2){
					// change current node origin
					positionOrigin2 = nodeOrigin;
					// reset neigboors
					initChooseNodeNeighbors(solution, chooseNodesNeighbors);
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Reset the list nodes neighbors visited
	 * @param chooseNodesNeighbors
	 */
	private void resetNeighbor (SolutionMMDP solution, boolean [] chooseNodesNeighbors){
		boolean [] solutionAux = solution.getSolution();
		
		for (int node = 0; node < chooseNodesNeighbors.length; node ++)
			if (solutionAux[node])
				chooseNodesNeighbors[node] = true;
			else
				chooseNodesNeighbors[node] = false;

		// set position ini
		selectFirstOrigin(solution);			
		return;
	}
	
	/**
	 * Reset the list nodes neighbors visited
	 * @param chooseNodesNeighbors
	 */
	private void resetNeighborTwo (SolutionMMDP solution, boolean [] chooseNodesNeighbors){
		boolean [] solutionAux = solution.getSolution();
		
		for (int node = 0; node < chooseNodesNeighbors.length; node ++)
			if (solutionAux[node])
				chooseNodesNeighbors[node] = true;
			else
				chooseNodesNeighbors[node] = false;

		// set position ini
		selectTwoOrigin(solution);			
		return;
	}
}