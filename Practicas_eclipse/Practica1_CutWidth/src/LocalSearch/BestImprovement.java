package LocalSearch;

import Application.Constants;
import Application.CutWidthInstance;
import Application.SolutionCutWidth;


public class BestImprovement extends LocalSearch {

		public BestImprovement (){
			super();
		}
	
		public BestImprovement (CutWidthInstance instance){
			super(instance);
		}
		
		/**
		 * Execute local search algorithm to solution CutWidth attribute
		 * @return SolutionCutWidth not possible more local search this solution CutWidth
		 */
		public SolutionCutWidth executeLocalSearchAlgorithm(SolutionCutWidth solutionCutWidth, long initTime){
			// if the solution has been improved
			boolean improve = false;
			
			boolean [] chooseNodesNeighbors = new boolean[instance.getNodes1() + 1];
			
			SolutionCutWidth bestSolutionCutWidth =  solutionCutWidth;
			SolutionCutWidth analizingSolutionCutWidth = solutionCutWidth;
			
			float bestSolutionValue = instance.getValueObjetiveFunction(solutionCutWidth);
			// create one neighbor
			SolutionCutWidth neighborhoodSolution = createNeighbor(solutionCutWidth, chooseNodesNeighbors);			
			
			// execute algorithm
			while (true){

				float otherValueSolution = instance.getValueObjetiveFunction(neighborhoodSolution);

				if (instance.bestValueObjetiveFunction(bestSolutionValue, otherValueSolution)){
					bestSolutionValue = otherValueSolution;
					bestSolutionCutWidth = neighborhoodSolution;
					bestSolutionCutWidth.setValueFunctionObjetive(bestSolutionValue);
					
					improve = true;
				}
				
				// check if all neighbor visited
				if (exitNeighbor(analizingSolutionCutWidth, chooseNodesNeighbors)){
					if (improve){
						improve = false;
						analizingSolutionCutWidth = bestSolutionCutWidth;
						resetNeighbor(chooseNodesNeighbors);
					} else {
						// not possible improve solution
						break;
					}
				}
				
				if ((System.currentTimeMillis() - initTime) >= Constants.TIME_ALGORITHM_MILISECONDS)
					break;
				
				// create one neighbor
				neighborhoodSolution = createNeighbor(analizingSolutionCutWidth, chooseNodesNeighbors);
			}
			
			return bestSolutionCutWidth;
		}
		
		/**
		 * Create the neighborhood with a move in the solution
		 * @param currentSolution
		 * @return
		 */
		private SolutionCutWidth createNeighbor (SolutionCutWidth currentSolution, boolean [] chooseNodesNeighbors){
			// clone solution
			int [] currentSolutionNodesCopy = currentSolution.getSolution().clone();
			
			int numberRandomPosition1 = Integer.MIN_VALUE;
			int numberRandomPosition2 = Integer.MIN_VALUE;

			// fill
			for(;;){
				// random number
				numberRandomPosition1 = this.randomNumber.nextInt(numNodes1 + 1);
				// choose node not change last
				if (!chooseNodesNeighbors[numberRandomPosition1]){
					chooseNodesNeighbors[numberRandomPosition1] = true;
					break;
				}
			}

			// empthy
			for(;;){
				// random number
				numberRandomPosition2 = this.randomNumber.nextInt(numNodes1 + 1);
				if (!chooseNodesNeighbors[numberRandomPosition2]){
					chooseNodesNeighbors[numberRandomPosition1] = true;
					break;
				}
			}

			// change values
			int valueNodeAux = currentSolutionNodesCopy[numberRandomPosition1];
			currentSolutionNodesCopy[numberRandomPosition1] = currentSolutionNodesCopy[numberRandomPosition2];
			currentSolutionNodesCopy[numberRandomPosition2] = valueNodeAux;

			SolutionCutWidth solutionNeighbor = new SolutionCutWidth(instance, currentSolutionNodesCopy);
			return solutionNeighbor;
		}
		
		/**
		 * If there are neighbors to visit
		 * @param solutionArray
		 * @param chooseNodesNeighbors
		 * @return if there are neighbors to visit
		 */
		private boolean exitNeighbor(SolutionCutWidth solution, boolean [] chooseNodesNeighbors){
			// get solution array
			int [] solutionArray = solution.getSolution();
			boolean position1 = false;
			for (int node = 0; node < solutionArray.length; node ++){
				if (!chooseNodesNeighbors[node]){
					if (position1){
						return false;
					} else {
						position1 = true;
					}
				}
			}
			
			return true;
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
