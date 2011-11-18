package LocalSearch;

import Application.MMDPInstance;
import Application.SolutionMMDP;

public class FirstImprovement extends LocalSearch {

		/**
		 * if the random sort or lexicographic, true if random, false lexicographic
		 */
		private boolean typeFirstImprovement;
		
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
		public SolutionMMDP executeLocalSearchAlgorithm(SolutionMMDP solutionMMDP){
			
			boolean [] chooseNodesNeighbors = new boolean[instance.getNumNodes()];
			initChooseNodeNeighbors(solutionMMDP, chooseNodesNeighbors);

			SolutionMMDP bestSolutionMMDP =  solutionMMDP;
			double bestSolutionValue = instance.getValueObjetiveFunction(solutionMMDP);
			// create one neighbor
			SolutionMMDP neighborhoodSolution = createNeighbor(solutionMMDP, chooseNodesNeighbors);			
			
			// execute algorithm
			while (true){
				
				// check if all neighbor visited
				if (exitNeighbor(bestSolutionMMDP, chooseNodesNeighbors))
					break;
				

				double otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);
				
				if (instance.bestValueObjetiveFunction(bestSolutionValue, otherValueSolution)){
					bestSolutionValue = otherValueSolution;
					bestSolutionMMDP = neighborhoodSolution;
					bestSolutionMMDP.setValueFunctionObjetive(bestSolutionValue);
					
					resetNeighbor(neighborhoodSolution, chooseNodesNeighbors);
				}
				
				// create one neighbor
				neighborhoodSolution = createNeighbor(bestSolutionMMDP, chooseNodesNeighbors);
			}
			
			return bestSolutionMMDP;
		}
		
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

			int nNodeNotSelected = Integer.MIN_VALUE;
			int nNodeSelected = Integer.MIN_VALUE;

			for(;;){
				// random number
				nNodeSelected = this.randomNumber.nextInt(numNodes);
				if (currentSolutionNodes[nNodeSelected])
					break;		
			}

			// polity first improve change the first node find 
			for(int i = 0; i < currentSolutionNodes.length; i ++){
				// choose node not change last
				nNodeNotSelected = i;
				
				if (!chooseNodesNeighbors[nNodeNotSelected]){
					chooseNodesNeighbors[nNodeNotSelected] = true;
					break;
				}
			}
			
			// change values
			currentSolutionNodesCopy[nNodeSelected] = false;
			currentSolutionNodesCopy[nNodeNotSelected] = true;
			
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

			int nNodeNotSelected = Integer.MIN_VALUE;
			int nNodeSelected = Integer.MIN_VALUE;

			// empthy
			for(;;){
				// random number
				nNodeSelected = this.randomNumber.nextInt(numNodes);
				if (currentSolutionNodes[nNodeSelected])
					break;		
			}

			// polity first improve change the first node find random
			for(;;){
				// random number
				nNodeNotSelected = this.randomNumber.nextInt(numNodes);
				// choose node not change last
				if (!chooseNodesNeighbors[nNodeNotSelected]){
					chooseNodesNeighbors[nNodeNotSelected] = true;
					break;
				}
			}
			
			// change values
			currentSolutionNodesCopy[nNodeSelected] = false;
			currentSolutionNodesCopy[nNodeNotSelected] = true;
			
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

			return;
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