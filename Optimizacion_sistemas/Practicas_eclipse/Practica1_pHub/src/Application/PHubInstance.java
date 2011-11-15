package Application;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class PHubInstance {
	
	/**
	 * File of the instance
	 */
	private File file;
	
	/**
	 * Number nodes instance
	 */
	private int numberNodes;
	
	/**
	 * Number node server
	 */
	private int numberNodeServers;
	
	/**
	 * Capacity servers
	 */
	private int capacityServer;
	
	/**
	 * List nodes
	 */
	private Node [] listNode;

	public PHubInstance(File fichero) {
		
		this.file = fichero;
		
	}

	public void loading() {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine().trim();
			StringTokenizer st = new StringTokenizer(line);
			numberNodes = Integer.parseInt(st.nextToken().trim());
			numberNodeServers = Integer.parseInt(st.nextToken().trim());
			capacityServer = Integer.parseInt(st.nextToken().trim());

			listNode = new Node[numberNodes];
			
			// read file
			line = br.readLine().trim();
			int numLine = 0;
			while(line != null) {
				if (line.equals(""))
					break;
				
				st = new StringTokenizer(line);
				int idNode = Integer.parseInt(st.nextToken());
				int coorX = Integer.parseInt(st.nextToken());
				int coorY = Integer.parseInt(st.nextToken());
				int capacityClient = Integer.parseInt(st.nextToken());
				listNode[numLine] = new Node (false, idNode, coorX, coorY, capacityClient);
				line = br.readLine().trim();
				
				numLine ++;
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}


	public double getValueObjetiveFunction(SolutionPHub solution){
		 // Function objetive min distance between nodes
		 float valueObjetiveFunction = Float.MAX_VALUE;
		 
		 if (!solution.isFactible()){
			 // Return the worse result objetive
			 return valueObjetiveFunction;
		 }
		 
		 Node [] instanceNodes = solution.getInstancePHub().getListNode();
		 boolean [] solutionClients = solution.getSolutionClients();
		 int [] solutionServers = solution.getSolutionServers();
		 int [][] solutionConnection = solution.getSolutionConnections();

		 valueObjetiveFunction = 0f;
		 
		 for (int server = 0; server < solutionServers.length; server ++){
			 int indexServer = solutionServers[server];
			 Node serverNode = instanceNodes[indexServer];
			 int [] connectionServer = solutionConnection[indexServer];
			 
			 for (int connectionWithServer = 0; connectionWithServer < solutionConnection.length; connectionWithServer ++){
				 
				 if ((connectionServer[connectionWithServer] == Constants.CONNECTION) && (indexServer != connectionWithServer) && (solutionClients[connectionWithServer])){
					 Node clientNode = instanceNodes[connectionWithServer];

					 valueObjetiveFunction += distanceEuclidean(clientNode, serverNode);
				 }
			 }
		 }
		 
		 return valueObjetiveFunction;
	}
	
	/**
	 * If the other value is the best that current objective value function
	 * @param valueBest the best value found
	 * @param otherValue other value calculate
	 * @return boolean
	 */
	public boolean bestValueObjetiveFunction (double valueBest, double otherValue){
		
		// Minimum value
		if (valueBest > otherValue)
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @param n1
	 * @param n2
	 * @return distance euclidean which two points
	 */
	public double distanceEuclidean (Node n1, Node otherNode){
		return Math.sqrt(Math.pow((n1.getCoordX() - otherNode.getCoordX()), 2) + Math.pow((n1.getCoordY() - otherNode.getCoordY()), 2));
	}
	
	public void print() {
		
		System.out.println(file.getName());
		System.out.println("Nodos: " + numberNodes + " servidores: " + numberNodeServers);
		
		for (int i = 0; i < listNode.length - 1; i++) {
			
			System.out.println(listNode[i]);
		}
	}

	// get and sets
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * @return the capacityServer
	 */
	public int getCapacityServer() {
		return capacityServer;
	}

	/**
	 * @param capacityServer the capacityServer to set
	 */
	public void setCapacityServer(int capacityServer) {
		this.capacityServer = capacityServer;
	}
	
	/**
	 * @return the capacityServer
	 */
	public int getNumberNodesServer() {
		return numberNodeServers;
	}

	/**
	 * @param capacityServer the capacityServer to set
	 */
	public void setNumberNodesServer(int numberNodesServer) {
		this.numberNodeServers = numberNodesServer;
	}

	/**
	 * @return the numberNode
	 */
	public int getNumberNode() {
		return numberNodes;
	}

	/**
	 * @param numberNode the numberNode to set
	 */
	public void setNumberNode(int numberNode) {
		this.numberNodes = numberNode;
	}
	
	/**
	 * @return the list node
	 */
	public Node [] getListNode (){
		return listNode;
	}
	
	/**
	 * @param listNode the list node to set
	 */
	public void setListNode(Node [] listNode) {
		this.listNode = listNode;
	}
}