package LocalSearch;

import Application.MMDPInstance;
import Application.SolutionMMDP;

public class BestImprovement extends LocalSearch {

		public BestImprovement (){
			super();
		}
	
		public BestImprovement (MMDPInstance instance){
			super(instance);
		}
		
		/**
		 * Execute local search algorithm to solution MMDP attribute
		 * @return SolutionMMDP not posible more local search this solution MMDP
		 */
		public SolutionMMDP executeLocalSearchAlgorithm(SolutionMMDP solutionMMDP){
			// if the solution has been improved
			boolean improve = false;
			
			boolean [] chooseNodesNeighbors = new boolean[instance.getNumNodes()];
			
			SolutionMMDP bestSolutionMMDP =  solutionMMDP;
			SolutionMMDP analizingSolutionMMDP = solutionMMDP;
			
			double bestSolutionValue = instance.getValueObjetiveFunction(solutionMMDP);
			// create one neighbor
			SolutionMMDP neighborhoodSolution = createNeighbor(solutionMMDP, chooseNodesNeighbors);			
			
			// execute algorithm
			while (true){
				
				// check if all neigbor visited
				if (exitNeighbor(analizingSolutionMMDP, chooseNodesNeighbors)){
					if (improve){
						improve = false;
						analizingSolutionMMDP = bestSolutionMMDP;
						resetNeighbor(chooseNodesNeighbors);
					} else {
						// not possible improve solution
						break;
					}
				}
				
				// create one neighbor
				neighborhoodSolution = createNeighbor(analizingSolutionMMDP, chooseNodesNeighbors);
				double otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);
				
				if (instance.bestValueObjetiveFunction(bestSolutionValue, otherValueSolution)){
					bestSolutionValue = otherValueSolution;
					bestSolutionMMDP = neighborhoodSolution;
					bestSolutionMMDP.setValueFunctionObjetive(bestSolutionValue);
					
					improve = true;
				}
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
			
			int numberRandomFill = Integer.MIN_VALUE;
			int numberRandomEmpthy = Integer.MIN_VALUE;

			// fill
			for(;;){
				// random number
				numberRandomFill = this.randomNumber.nextInt(numNodes);
				// choose node not change last
				if ((currentSolutionNodes[numberRandomFill]) && (!chooseNodesNeighbors[numberRandomFill])){
					chooseNodesNeighbors[numberRandomFill] = true;
					break;
				}
			}

			// empthy
			for(;;){
				// random number
				numberRandomEmpthy = this.randomNumber.nextInt(numNodes);
				if (!currentSolutionNodes[numberRandomEmpthy])
					break;		
			}

			// change values
			currentSolutionNodesCopy[numberRandomFill] = false;
			currentSolutionNodesCopy[numberRandomEmpthy] = true;
			
			SolutionMMDP solutionNeighbor = new SolutionMMDP(instance, currentSolutionNodesCopy);
			return solutionNeighbor;
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
				if (solutionArray[node] && (!chooseNodesNeighbors[node]))
					return true;
			}
			
			return false;
		}
		
		/**
		 * Reset the list nodes neighbors visited
		 * @param chooseNodesNeighbors
		 */
		private void resetNeighbor (boolean [] chooseNodesNeighbors){
			for (int node = 0; node < chooseNodesNeighbors.length; node ++)
				chooseNodesNeighbors[node] = false;
			
			return;
		}
}
