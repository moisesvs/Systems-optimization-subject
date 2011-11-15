package Application;

public class SolutionPHub {
		
	/**
	 * Solution array server index server number
	 */
	private int [] solutionServers;
	
	/**
	 * Solution array clients
	 */
	private boolean [] solutionClients;

	/**
	 * Matrix connection whit servers node
	 */
	private int [][] solutionConnections;
	
	/**
	 * Instance problem pHub
	 */
	private PHubInstance instancePHub; 
	
	/**
	 * Value objetive function pHub
	 */
	private double valueFunctionObjetive; 
	
	/**
	 * Time in find this solution
	 */
	private float timeFindSolution;
	
	
	public SolutionPHub (PHubInstance instance, int [] solutionServers, boolean [] solutionClients, int [][] matrixConnections){
		this.instancePHub = instance;
		this.solutionServers = solutionServers;
		this.solutionClients = solutionClients;
		this.solutionConnections = matrixConnections;
	}
	
	/**
	 * If the solution is feasible or not
	 * @return true if the solution is feasible or false if not
	 */
	public boolean isFactible (){

		if (!((solutionServers != null) && (solutionClients != null) && (solutionConnections != null)))
			return false;
		
		for (int i = 0; i < this.solutionServers.length; i++){
			int index = this.solutionServers[i];
			
			// check bandwith server
			if (!(this.solutionConnections[index][index] >= 0))
				return false;
		}
		
		// count server
		if (this.instancePHub.getNumberNodesServer() == this.solutionServers.length){
			for (int i = 0; i < this.solutionServers.length; i++){
				if (this.solutionServers[i] > instancePHub.getNumberNode())
					return false;
			}
		}

		
		if (!(this.solutionServers.length == this.instancePHub.getNumberNodesServer()))
			return false;
		
		// count number servers
		int numClients = 0;
		for (int i = 0; i < this.solutionClients.length; i++)
			if (this.solutionClients[i])
				numClients ++;
		
		if (!(numClients == this.instancePHub.getNumberNode()))
			return false;		
		
		return true;
		
	}
	
	public void initSolution(){
		for (int i = 0; i < solutionClients.length; i++){
			solutionClients[i] = false;
		}
		
		for (int i = 0; i < solutionServers.length; i++){
			solutionServers[i] = Constants.NOT_CONNECTION;
		}
		
		for (int i = 0; i < solutionConnections.length; i++){
			for (int j = 0; j < solutionConnections.length; j++){
				solutionConnections[i][j] = Constants.NOT_CONNECTION;
			}
			solutionConnections[i][i] = instancePHub.getCapacityServer();
		}
	}
	
	// gets and sets
	public void setSolutionClients (boolean [] solutionClients){
		this.solutionClients = solutionClients;
	}
	
	public boolean [] getSolutionClients(){
		return this.solutionClients;
	}
	
	public void setSolutionServers (int [] solutionServers){
		this.solutionServers = solutionServers;
	}
	
	public int [] getSolutionServers(){
		return this.solutionServers;
	}
	
	public String toString(){
		String cad = "\n### NODOS CONECTADOS ###\n";
		cad += "[";
		
		for (int i = 0; i < this.solutionServers.length; i ++)
			cad += this.solutionServers[i] + "  ";
		
		cad += "]";
		cad += "\n";
		cad += "### CONEXIONES ###\n";

		for (int i = 0; i < this.solutionServers.length; i ++){
			int [] connections = this.solutionConnections[this.solutionServers[i]];
			cad += "Server: " + this.solutionServers[i] + "\n";

			for (int j = 0; j < connections.length; j ++){
				if ((i != j) && (connections[j] == Constants.CONNECTION))
					cad += "\t\t" + j + "\n";
			}
		}
		
		return cad;
	}

	// Gets and sets
	/**
	 * @param valueFunctionObjetive the valueFunctionObjetive to set
	 */
	public void setValueFunctionObjetive(double valueFunctionObjetive) {
		this.valueFunctionObjetive = valueFunctionObjetive;
	}

	/**
	 * @return the valueFunctionObjetive
	 */
	public double getValueFunctionObjetive() {
		return valueFunctionObjetive;
	}
	
	/**
	 * @return the timeFindSolution
	 */
	public float getTimeFindSolution() {
		return timeFindSolution;
	}

	/**
	 * @param timeFindSolution the timeFindSolution to set
	 */
	public void setTimeFindSolution(float timeFindSolution) {
		this.timeFindSolution = timeFindSolution;
	}

	/**
	 * @return the instancePHub
	 */
	public PHubInstance getInstancePHub() {
		return instancePHub;
	}

	/**
	 * @param instancePHub the instancePHub to set
	 */
	public void setInstancePHub(PHubInstance instancePHub) {
		this.instancePHub = instancePHub;
	}

	/**
	 * @return the solutionConnections
	 */
	public int [][] getSolutionConnections() {
		return solutionConnections;
	}

	/**
	 * @param solutionConnections the solutionConnections to set
	 */
	public void setSolutionConnections(int [][] solutionConnections) {
		this.solutionConnections = solutionConnections;
	}
	
//	/**
//	 * Print solution
//	 * @param totalTime
//	 * @param instance
//	 * @param bestSolution
//	 */
//	public void printSolution (){
//		System.out.println("#####################################################################################################");
//		System.out.println("Time(miliseconds): " + timeFindSolution + " ms");
//		System.out.println("Time(minutes): " + TimeUnit.MILLISECONDS.toMinutes((long)timeFindSolution) + " m");
//		System.out.println("Nodes choose (" + instanceMMDP.getFile().getName() + "): "  + this);
//		System.out.println("Best Solution (" + instanceMMDP.getFile().getName() + "): "  + this.valueFunctionObjetive);
//		System.out.println("#####################################################################################################");
//	}
	
//	/**
//	 * Print format program format mh analizer
//	 * @param totalTime
//	 * @param instance
//	 * @param bestSolution
//	 * @return
//	 */
//	public String printFormatMhAnalizer (){
//		return instanceMMDP.getFile().getName() + "\t" + this.valueFunctionObjetive + "\t" + (long)timeFindSolution + "\t" + this + "\n";
//	}
}