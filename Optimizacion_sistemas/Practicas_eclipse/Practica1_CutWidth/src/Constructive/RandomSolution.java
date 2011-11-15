package Constructive;

import Application.CutWidthInstance;
import Application.SolutionCutWidth;

public class RandomSolution extends Constructive {

	public RandomSolution (CutWidthInstance instanceCutWidth){
		super(instanceCutWidth);
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	public SolutionCutWidth createSolution(){
		boolean [] listBooleans = new boolean [instanceCutWidth.getLengthMatrixConnections()];
		int [] listArraySolution = new int [instanceCutWidth.getLengthMatrixConnections()];
		int tamSolution = instanceCutWidth.getLengthMatrixConnections();
		
		for (int j = 0; j < tamSolution; j ++){
			// generate number random
			int numRandom = generator.nextInt(tamSolution);
			
			// search if position occupied and find position free
			while (listBooleans[numRandom] == true)
				numRandom = generator.nextInt(tamSolution);

			listBooleans[numRandom] = true;
			listArraySolution[j] = numRandom;
		}
		
		return new SolutionCutWidth(instanceCutWidth, listArraySolution);
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	public SolutionCutWidth [] createSolutionsArray(int numSolutions){
		SolutionCutWidth [] solutionList = new SolutionCutWidth[numSolutions];
		
		for (int i = 0; i < solutionList.length; i++){		
			// set solution
			solutionList[i] = createSolution();
		}
		
		return solutionList;
	}
}