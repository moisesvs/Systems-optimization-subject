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
	public TabuSearch (RandomSolution randomGenerator, MMDPInstance instance, int kIterationsTabu){
		super(randomGenerator,instance);
		
		// init algorithm
		this.kIterationsTabu = kIterationsTabu;
		this.tabuShortTermList = new int [instance.getNumNodes()];
		this.tabuLongTermList = new int [instance.getNumNodes()];
		this.tablaTabuFitnessNeighbors = new double [instance.getNumNodes()][3];
		
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
		SolutionMMDP neighborhoodSolution = randomGenerator.createSolution();
		SolutionMMDP bestSolutionMMDP = neighborhoodSolution;
		double bestSolutionValue = instance.getValueObjetiveFunction(neighborhoodSolution);

		boolean [] chooseNodesNeighbors = new boolean[instance.getNumNodes()];
		initChooseNodeNeighbors(neighborhoodSolution, chooseNodesNeighbors);

		// create neighborhood
		int node = 0;
		while (true){
			
			// create one neighbor
			neighborhoodSolution = createNeighbor(bestSolutionMMDP, chooseNodesNeighbors);
			double otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);
			
			// fill table fitness
			tablaTabuFitnessNeighbors[node][0] = positionOrigin;
			tablaTabuFitnessNeighbors[node][1] = positionDestination;
			tablaTabuFitnessNeighbors[node][2] = otherValueSolution;

			// check if all neighbor visited
			if (exitNeighbor(bestSolutionMMDP, chooseNodesNeighbors))
				break;

			node ++;
		}
		
//		deleteNodesTabu();
		
//		for (;;){
//			double valueFuntionObjetive = instance.getValueObjetiveFunction(analizingSolution);
//			
//			if (instance.bestValueObjetiveFunction(bestSolutionValue, valueFuntionObjetive)){
//				bestSolutionValue = valueFuntionObjetive;
//				bestSolution = analizingSolution;
//				bestSolution.setValueFunctionObjetive(valueFuntionObjetive);
//			}
//			
//			analizingSolution = randomGenerator.createSolution();
//
//			// condition break
//			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_ALGORITHM_MILISECONDS)
//				break;
//		}
//		
		return bestSolution;
	}
	
//	Generar una solución inicial s0. 
//	- Inicializamos la solución y la lista tabú: sfinal = s0, T={s0}. 
//	- Fase de acercamiento: En cada iteración t: 
//	Repetir (hasta condición de parada): 
//	 1. Generar vecindario tipo 1, N(st), y eliminamos las soluciones tabú, N(st)=N(st)-T. 
//	 2. st+1 es seleccionado verificando f(st+1) = min{f(s) : sœN(st) }. 
//	 3. Si f(st+1) < f(sfinal), sfinal
//	 = st+1.  
//	 4. Actualizar T. 
//	Fin acercamiento. 
//	- Fase de Intensificación: 
//	Repetir (hasta condición de parada): En cada iteración t: 
//	 1. Generar un vecino de tipo 2, N(st).  
//	 2. Aplicamos una Fase de Acercamiento. 
//	Fin Intensificación.

	/**
	 * Create the neighborhood with a move in the solution
	 * @param currentSolution
	 * @return
	 */
	private SolutionMMDP createNeighbor (SolutionMMDP currentSolution, boolean [] chooseNodesNeighbors){
		// clone solution
		boolean [] currentSolutionNodes = currentSolution.getSolution();
		boolean [] currentSolutionNodesCopy = currentSolution.getSolution().clone();

		for(;;){
			// random number
			this.positionOrigin = this.randomNumber.nextInt(numNodes);
			if (currentSolutionNodes[positionOrigin])
				break;		
		}

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
		}
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