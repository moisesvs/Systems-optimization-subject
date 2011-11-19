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
	 * Local search algorithm
	 */
	private LocalSearch localSearch;
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 * @param numSolution
	 */
	public VNS (RandomSolution randomGenerator, MMDPInstance instance, LocalSearch localSearch){
		super(randomGenerator,instance);
		
		// init algorithm
		this.kNeighbors = Constants.K_NEIGHBORD_VNS;
		
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