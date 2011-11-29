package Algorithm;

import java.util.ArrayList;

import Application.Constants;
import Application.CutWidthInstance;
import Application.SolutionCutWidth;
import LocalSearch.LocalSearch;

public class GRASP extends Algorithm {

	/**
	 * Alpha GRASP
	 */
	private float alpha;
	
	/**
	 * Execute algorithm with number solution with random generator
	 * @param randomGenerator
	 * @param numSolution
	 */
	public GRASP (CutWidthInstance instance, LocalSearch localSearch){
		super(instance, localSearch);
		
		// init algorithm
		this.alpha = Constants.ALPHA;;

		long timeFirst = System.currentTimeMillis();
		bestSolution = executeAlgorithm(instance);
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
	private SolutionCutWidth executeAlgorithm (CutWidthInstance instance){
		
		long timeIni = System.currentTimeMillis();

		// Max solution
		SolutionCutWidth bestSolutionCutWidth = createSolutionGRASP(instance);
		SolutionCutWidth analizingSolution = bestSolutionCutWidth;
		
		float bestSolutionValue = instance.getValueObjetiveFunction(bestSolutionCutWidth);
		bestSolutionCutWidth.setValueFunctionObjetive(bestSolutionValue);
		
		// create neighborhood
		while (true){

			analizingSolution = localSearch.executeLocalSearchAlgorithm(analizingSolution, timeIni);

			float otherValueSolution = instance.getValueObjetiveFunction(analizingSolution);

			if (instance.bestValueObjetiveFunction(bestSolutionValue, otherValueSolution)){
				analizingSolution.setValueFunctionObjetive(otherValueSolution);
				bestSolutionCutWidth = analizingSolution;
				bestSolutionValue = otherValueSolution;
			}
			
			// condition break
			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_ALGORITHM_MILISECONDS)
				break;
			
			analizingSolution = createSolutionGRASP(instance);
			
		}

		
		return bestSolutionCutWidth;
	}

	/**
	 * Create solution GRASP
	 */
	private SolutionCutWidth createSolutionGRASP(CutWidthInstance instance){
		int indexAux = 0;
		int tamSolution = instance.getLengthMatrixConnections();

		int [] bestSolutionGRASP = new int[numNodes1];
		boolean [] bestSolutionNodesChoose = new boolean [instance.getLengthMatrixConnections()];
		bestSolutionNodesChoose[0] = true;
		initSolution(bestSolutionGRASP);
		
		// First node
		int selectedNodeRandom = randomNumber.nextInt(tamSolution - 1) + 1;
		bestSolutionGRASP[indexAux] = selectedNodeRandom;
		bestSolutionNodesChoose[selectedNodeRandom] = true;
		
		indexAux ++;
		while (!solutionFill (bestSolutionGRASP)){
			
			int [] nodesAdjacentsRandom = instance.getNodesAdjacents()[selectedNodeRandom];
			nodesAdjacentsRandom = deleteNodesInSolution(bestSolutionGRASP, nodesAdjacentsRandom);
			int numNodesAdjacents = nodesAdjacentsRandom.length;
			
			if (numNodesAdjacents == 0){
//				nodesAdjacentsRandom = createNodesNotAdjacents(bestSolutionNodesChoose);
				nodesAdjacentsRandom = createNodeAdjacents(bestSolutionNodesChoose);
				numNodesAdjacents = nodesAdjacentsRandom.length;
			}
			
			float maxValueFunction = Float.MIN_VALUE;
			float minValueFunction = Float.MAX_VALUE;

			int [][] candidatesSolutions = new int [numNodesAdjacents][numNodesAdjacents];
			int [][] listSolutionRCL = new int [numNodesAdjacents][numNodesAdjacents];
			float [] valueCandidatesSolutions = new float [numNodesAdjacents];			
			
			// Calculate list candidates
			for (int i = 0; i < nodesAdjacentsRandom.length; i ++){

				bestSolutionGRASP[indexAux] = nodesAdjacentsRandom[i];
				float otherValueFunction = instance.getValueObjetiveFunctionArraySolution(bestSolutionGRASP,indexAux);
				
				candidatesSolutions[i] = bestSolutionGRASP.clone();
				valueCandidatesSolutions[i] = otherValueFunction;
				
				if (otherValueFunction < minValueFunction)
					minValueFunction = otherValueFunction;
				
				if (otherValueFunction > maxValueFunction)
					maxValueFunction = otherValueFunction;
			}
			
			// Calculate threshold
			float u = minValueFunction + alpha * (maxValueFunction - minValueFunction);
			
			// Calculate list RCL
			for (int i = 0; i < candidatesSolutions.length; i ++){
				
				if (valueCandidatesSolutions [i] <= u){
					listSolutionRCL[i] = candidatesSolutions[i];
				} else {
					listSolutionRCL[i] = null;
				}
			}
			
			// select node list RCL
			int numSolution = randomNumber.nextInt(listSolutionRCL.length);
			while (listSolutionRCL[numSolution] == null)
				numSolution = randomNumber.nextInt(listSolutionRCL.length);

			bestSolutionGRASP = listSolutionRCL[numSolution];
			selectedNodeRandom = bestSolutionGRASP[indexAux];
			bestSolutionNodesChoose[selectedNodeRandom] = true;
			
			indexAux ++;
		}
		
		SolutionCutWidth solution = new SolutionCutWidth(instance, bestSolutionGRASP);
		return solution;
	}
	
	/**
	 * if the solution grasp is fill
	 * @param solutionGRASP
	 */
	private boolean solutionFill (int [] solutionGRASP){
		for (int i = 0; i < solutionGRASP.length; i++){
			if (solutionGRASP[i] == Constants.NODES_NOT_CONNECTION)
				return false;
		}
		return true;
	}
	
	
	/**
	 * Initializing solution
	 * @param solution
	 */
	private void initSolution (int [] solution){
		for (int i = 0; i < solution.length; i ++){
			solution[i] = Constants.NODES_NOT_CONNECTION;
		}
	}
	
	private int [] deleteNodesInSolution(int [] solution, int [] nodesAdjacentsRandom){
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		
		for (int i = 0; i < nodesAdjacentsRandom.length; i++){
			boolean find = false;
			for (int j = 0; j < solution.length; j++){
				if (solution[j] == nodesAdjacentsRandom[i]){
					find = true;
					break;
				}
			}
			
			if (!find)
				nodes.add(nodesAdjacentsRandom[i]);
				
		}
		
		int [] nodesAdjacentsRandomAux = new int[nodes.size()];
		for (int i = 0; i < nodes.size(); i++){
			nodesAdjacentsRandomAux[i] = nodes.get(i);
		}
		
		return nodesAdjacentsRandomAux;
	}
	
	private int [] createNodesNotAdjacents (boolean [] solutionNodesNotChoose){
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		
		for (int i = 0; i < solutionNodesNotChoose.length; i++){
			if (!solutionNodesNotChoose[i])
				nodes.add(i);
		}
		
		int [] nodesAdjacentsAux = new int[nodes.size()];
		for (int i = 0; i < nodes.size(); i++){
			nodesAdjacentsAux[i] = nodes.get(i);
		}
		
		return nodesAdjacentsAux;
	}
	
	private int [] createNodeAdjacents (boolean [] solutionNodesNotChoose){
		int [] nodesAux = new int [1];
		
		int numNodes = randomNumber.nextInt(solutionNodesNotChoose.length);
		while (solutionNodesNotChoose[numNodes])
			numNodes = randomNumber.nextInt(solutionNodesNotChoose.length);
		
		nodesAux[0] = numNodes;
		return nodesAux;
	}
}
