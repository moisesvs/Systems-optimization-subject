package Constructive;

import Application.MMDPInstance;
import Application.SolutionMMDP;

public class RandomSolution extends Constructive{

	public RandomSolution (MMDPInstance instanceMMD){
		super(instanceMMD);
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	public SolutionMMDP createSolution(){
		boolean [] listBooleans = new boolean [numNodes];
		boolean [] listArraySolution = new boolean [numNodes];
			
		for (int j = 0; j < numNodesSelection; j ++){
			// generate number random
			int numRandom = generator.nextInt(numNodes);
			
			// search if position occupied and find position free
			while (listBooleans[numRandom] == true)
				numRandom = generator.nextInt(numNodes);

			listBooleans[numRandom] = true;
			listArraySolution[numRandom] = true;
		}
		
		return new SolutionMMDP(instanceMMDP, listArraySolution);
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	public SolutionMMDP [] createSolutionsArray(int numSolutions){
		SolutionMMDP [] solutionList = new SolutionMMDP[numSolutions];
		
		for (int i = 0; i < solutionList.length; i++){		
			// set solution
			solutionList[i] = createSolution();
		}
		
		return solutionList;
	}
}