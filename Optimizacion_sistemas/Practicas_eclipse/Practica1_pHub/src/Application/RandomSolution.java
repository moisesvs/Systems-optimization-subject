package Application;

import java.util.Random;

public class RandomSolution {

	private PHubInstance instancePHub;
	
	private Random generator;
	
	private int numNodes;
	
	private int numNodesServers;
	
	private int capacityServers;
	
	public RandomSolution (PHubInstance instancePHub){
		this.instancePHub = instancePHub;
		this.numNodes = this.instancePHub.getNumberNode();
		this.numNodesServers = this.instancePHub.getNumberNodesServer();
		this.capacityServers = this.instancePHub.getCapacityServer();
		this.generator = new Random();
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	public SolutionPHub createSolution(){
		int [] listServerSolution = new int [numNodesServers];
		boolean [] listServerBoolSolution = new boolean [numNodes];
		boolean [] listNodesBoolSolution = new boolean [numNodes];
		int [][] matrixConnectionSolution = new int [numNodes][numNodes];
		
		Node [] nodesInstance = instancePHub.getListNode();
		
		SolutionPHub solution = new SolutionPHub(instancePHub, listServerSolution, listNodesBoolSolution, matrixConnectionSolution);
		
		while (!solution.isFactible()){
			
			solution.initSolution();
			
			// init list nodes solution aux
			for (int i = 0; i < listNodesBoolSolution.length; i++){
				listNodesBoolSolution[i] = false;
				listServerBoolSolution[i] = false;
			}
			
			// choose servers
			for (int server = 0; server < numNodesServers; server ++){
				// generate number random
				int numRandomServer = generator.nextInt(numNodes);
				
				// search if position occupied and find position free
				while (listServerBoolSolution[numRandomServer] == true)
					numRandomServer = generator.nextInt(numNodes);
				
				// put server
				listServerSolution[server] = numRandomServer;
				listServerBoolSolution[numRandomServer] = true;
				listNodesBoolSolution[numRandomServer] = true;
			}
			
			// connections clients with servers
			for (int client = 0; client < (numNodes - numNodesServers); client ++){
				// generate number random client
				int numRandomClient = generator.nextInt(numNodes);
				
				// search if position occupied and find position free
				while ((listNodesBoolSolution[numRandomClient]) || (listServerBoolSolution[numRandomClient]))
						numRandomClient = generator.nextInt(numNodes);
				
				// node instance choose
				Node nodeChoose = nodesInstance[numRandomClient];
				listNodesBoolSolution[numRandomClient] = true;
				
				// generate number random server
				int index = listServerSolution[generator.nextInt(numNodesServers)];
				// search if position occupied and find position free
				while ((matrixConnectionSolution[index][index] - nodeChoose.getDemand()) <= 0)
					index = listServerSolution[generator.nextInt(numNodesServers)];
								
//				for (int server = 0; server < numNodesServers; server ++){
//					int index = listServerSolution[server];
//					if ((matrixConnectionSolution[index][index] - nodeChoose.getDemand()) >= 0){
//						// set connection
//						matrixConnectionSolution[numRandomClient][index] = Constants.CONNECTION;
//						matrixConnectionSolution[index][numRandomClient] = Constants.CONNECTION;
//
//						matrixConnectionSolution[index][index] =  matrixConnectionSolution[index][index] - nodeChoose.getDemand();
//						break;
//
//					}
//				}
				
				// set connection
				matrixConnectionSolution[numRandomClient][index] = Constants.CONNECTION;
				matrixConnectionSolution[index][numRandomClient] = Constants.CONNECTION;
				matrixConnectionSolution[index][index] =  matrixConnectionSolution[index][index] - nodeChoose.getDemand();
			}	
		}
		
		return solution;
	}

	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	public SolutionPHub [] createSolutionsArray(int numSolutions){
		SolutionPHub [] solutionList = new SolutionPHub[numSolutions];
		
		for (int i = 0; i < solutionList.length; i++){		
			// set solution
			solutionList[i] = createSolution();
		}
		
		return solutionList;
	}

	/**
	 * @return the capacityServers
	 */
	public int getCapacityServers() {
		return capacityServers;
	}

	/**
	 * @param capacityServers the capacityServers to set
	 */
	public void setCapacityServers(int capacityServers) {
		this.capacityServers = capacityServers;
	}
}