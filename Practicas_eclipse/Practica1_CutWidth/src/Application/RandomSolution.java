package Application;

import java.util.Random;

public class RandomSolution {

	private CutWidthInstance instanceCutdWidth;
	
	private Random generator;
	
	private int numConnections;
	
	private int numNodes;
	
	public RandomSolution (CutWidthInstance instanceCutdWidth){
		this.instanceCutdWidth = instanceCutdWidth;
		this.numConnections = this.instanceCutdWidth.getConnections();
		this.numNodes = this.instanceCutdWidth.getNodes1();
		this.generator = new Random();
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	public SolutionCutWidth createSolution(){
		boolean [] listBooleans = new boolean [instanceCutdWidth.getLengthMatrixConnections()];
		int [] listArraySolution = new int [instanceCutdWidth.getLengthMatrixConnections()];
		int tamSolution = instanceCutdWidth.getLengthMatrixConnections();
		
		for (int j = 0; j < tamSolution; j ++){
			// generate number random
			int numRandom = generator.nextInt(tamSolution);
			
			// search if position occupied and find position free
			while (listBooleans[numRandom] == true)
				numRandom = generator.nextInt(tamSolution);

			listBooleans[numRandom] = true;
			listArraySolution[j] = numRandom;
		}
		
		return new SolutionCutWidth(instanceCutdWidth, listArraySolution);
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