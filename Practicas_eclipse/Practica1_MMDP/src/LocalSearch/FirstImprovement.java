package LocalSearch;

import Application.Constants;
import Application.MMDPInstance;
import Application.SolutionMMDP;

public class FirstImprovement extends LocalSearch {

		/**
		 * if the random sort or lexicographic, true if random, false lexicographic
		 */
		private boolean typeFirstImprovement;
		
		/**
		 * Position origin
		 */
		private int positionOrigin;
		
		/**
		 * Position destination
		 */
		private int positionDestination;

		
		/**
		 * Default constructor
		 * @param typeFirstImprovement
		 */
		public FirstImprovement (boolean typeFirstImprovement){
			super();
			this.typeFirstImprovement = typeFirstImprovement;
		}
		
		/**
		 * Constructor with instance
		 * @param instance
		 * @param typeFirstImprovement
		 */
		public FirstImprovement (MMDPInstance instance, boolean typeFirstImprovement){
			super(instance);
			this.typeFirstImprovement = typeFirstImprovement;
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
		 * Method determine neighbor 
		 * @param solution
		 * @param chooseNodesNeighbors
		 * @return
		 */
		private SolutionMMDP createNeighbor(SolutionMMDP solution, boolean [] chooseNodesNeighbors){
			if (this.typeFirstImprovement) 
				return createNeighborRandom(solution, chooseNodesNeighbors);
			else
				return createNeighborLexicographic(solution, chooseNodesNeighbors);
		}
		
		/**
		 * Create the neighborhood with a move in the solution
		 * @param currentSolution
		 * @return
		 */
		private SolutionMMDP createNeighborLexicographic (SolutionMMDP currentSolution, boolean [] chooseNodesNeighbors){
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
		 * Create the neighborhood with a move in the solution
		 * @param currentSolution
		 * @return
		 */
		private SolutionMMDP createNeighborRandom (SolutionMMDP currentSolution, boolean [] chooseNodesNeighbors){
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
		 * Select the first node select current
		 * @param solution
		 */
		private void selectFirstOrigin (SolutionMMDP solution){
			boolean [] solutionAux = solution.getSolution();

			if (typeFirstImprovement){
				// random
				for(;;){
					// random number
					positionOrigin = this.randomNumber.nextInt(numNodes);
					if (solutionAux[positionOrigin])
						break;		
				}
				
			} else {
				// lexicographic
				for (int i = 0; i < solutionAux.length; i++){
					if (solutionAux[i]){
						positionOrigin = i;
						return;
					}
				}
			}
		}
		
		/**
		 * @return the typeFirstImprovement
		 */
		public boolean isTypeFirstImprovement() {
			return typeFirstImprovement;
		}

		/**
		 * @param typeFirstImprovement the typeFirstImprovement to set
		 */
		public void setTypeFirstImprovement(boolean typeFirstImprovement) {
			this.typeFirstImprovement = typeFirstImprovement;
		}
}