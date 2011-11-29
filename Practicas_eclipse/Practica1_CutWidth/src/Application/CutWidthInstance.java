package Application;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class CutWidthInstance {
	
	private File file;
	private int nodes1;
	private int nodes2;
	private int connections;
	private int [][] matrixConnection;
	private int [][] nodesAdjacents;

	public CutWidthInstance(File fichero) {
		
		this.file = fichero;
		
	}

	public void loading() {
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);
			nodes1 = Integer.parseInt(st.nextToken());
			nodes2 = Integer.parseInt(st.nextToken());
			connections = Integer.parseInt(st.nextToken());
			matrixConnection = new int[nodes2 + 1][nodes2 + 1];
			initMatrixConnection(Constants.NODES_NOT_CONNECTION);

			line = br.readLine();
			while(line != null) {
				
				st = new StringTokenizer(line);
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				matrixConnection[x][y] = Constants.NODES_CONNECTION;
				matrixConnection[y][x] = Constants.NODES_CONNECTION;
				
				// Increase number nodes index
				matrixConnection[x][x] += 1;
				matrixConnection[y][y] += 1;

				line = br.readLine();
			}
			br.close();

			nodesAdjacents();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public float getValueObjetiveFunction(SolutionCutWidth solution){
		 // Function objetive min distance between nodes
		 float valueObjetiveFunction = Float.MIN_VALUE;
		 boolean [] nodesVisited = new boolean [getLengthMatrixConnections()];
		 
		 if (!solution.isFactible()){
			 // Return the worse result objetive
			 return valueObjetiveFunction;
		 }
		 
		 int [] solutionAux = solution.getSolution();
		 // the node end not visited
		 for (int posNode = 0; posNode < nodes2; posNode ++){
			 
			 int numNode = solutionAux[posNode];

			 nodesVisited[numNode] = true;
			 int numConnectionNode = 0;
			 
			 // other nodes not visited
			 for (int posNodeVisited = 0;  posNodeVisited < nodesVisited.length; posNodeVisited ++){
				 if (nodesVisited[posNodeVisited]){
					 // check if other conexion
					 for (int otherConexion = 0;  otherConexion < getLengthMatrixConnections(); otherConexion ++){
						 
						 if (posNodeVisited != otherConexion && 
							(!nodesVisited[otherConexion]) &&
							(matrixConnection[posNodeVisited][otherConexion] == Constants.NODES_CONNECTION)){
							 
							 	numConnectionNode++;
						 }
					 }
				 }
			 }

			 if (valueObjetiveFunction < numConnectionNode)
				 valueObjetiveFunction = numConnectionNode;

		 }
		 
		 return valueObjetiveFunction;
	}
	

	public float getValueObjetiveFunctionArraySolution(int [] solutionAux, int fillArray){
		 // Function objetive min distance between nodes
		 float valueObjetiveFunction = Float.MIN_VALUE;
		 boolean [] nodesVisited = new boolean [getLengthMatrixConnections()];

		 // the node end not visited
		 for (int posNode = 0; posNode < fillArray; posNode ++){
			 
			 int numNode = solutionAux[posNode];

			 nodesVisited[numNode] = true;
			 int numConnectionNode = 0;
			 
			 // other nodes not visited
			 for (int posNodeVisited = 0;  posNodeVisited < nodesVisited.length; posNodeVisited ++){
				 if (nodesVisited[posNodeVisited]){
					 // check if other conexion
					 for (int otherConexion = 0;  otherConexion < getLengthMatrixConnections(); otherConexion ++){
						 
						 if (posNodeVisited != otherConexion && 
							(!nodesVisited[otherConexion]) &&
							(isNodeInSolution(solutionAux, otherConexion)) &&
							(matrixConnection[posNodeVisited][otherConexion] == Constants.NODES_CONNECTION)){
							 
							 	numConnectionNode++;
						 }
					 }
				 }
			 }

			 if (valueObjetiveFunction < numConnectionNode)
				 valueObjetiveFunction = numConnectionNode;

		 }
		 
		 return valueObjetiveFunction;
	}
	
	/**
	 * If the other value is the best that current objective value function
	 * @param valueBest the best value found
	 * @param otherValue other value calculate
	 * @return boolean
	 */
	public boolean bestValueObjetiveFunction (float valueBest, float otherValue){
		
		// Minimum value
		if (valueBest > otherValue)
			return true;
		
		return false;
	}
	
	private boolean isNodeInSolution (int [] solutionAux, int otherSolution ){
		for (int i = 0; i < solutionAux.length; i ++){
			if (solutionAux[i] == otherSolution)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Init matrix connections problem CutWidth
	 * @param indexInit
	 */
	private void initMatrixConnection(int indexInit){
		for (int i = 0; i < matrixConnection.length; i++){
			for (int j = 0; j < matrixConnection.length; j++){
				if (i != j)
					matrixConnection[i][j] = indexInit;
			}
		}
	}
	
	private void nodesAdjacents(){
		nodesAdjacents = new int [nodes2 + 1][];
		
		for (int i = 0; i < matrixConnection.length; i++){
			if (matrixConnection[i][i] > 0){
				nodesAdjacents[i] = new int [matrixConnection[i][i]];
			}
		}
		
		int numAdjacents = 0;
		for (int i = 0; i < matrixConnection.length; i ++){
			for (int j = 0; j < matrixConnection.length; j++){
				if ((i != j) && (matrixConnection[i][j] == Constants.NODES_CONNECTION)){
					nodesAdjacents[i][numAdjacents] = j;
					numAdjacents ++;
				}
			}
			numAdjacents = 0;
		}
	}
	
	/**
	 * Print nodes matrix connections
	 */
	public void print() {
		
		System.out.println(file.getName());
		System.out.println(nodes1 + " " + nodes2);
		
		for (int i = 0; i < matrixConnection.length; i++) {
			System.out.println("##### " + i + " " + matrixConnection[i][i] + " ARISTAS" + " #####");

			for (int j = 0; j < matrixConnection.length; j++) {

				if (matrixConnection[i][j] == Constants.NODES_CONNECTION){
					System.out.println(i + " " + j + " CONECTADOS");
				}
			}
		}
	}
	
	/**
	 * Print nodes adjacents
	 * @param nodeParameter
	 */
	public void printAdjacents (int nodeParameter){
		int [] adjacentsAux = null;

		if (nodeParameter <= nodes2){
			adjacentsAux = nodesAdjacents[nodeParameter];
			if (adjacentsAux == null){
				System.out.println("##### No existen adyacentes para ese nodo #####");
				return;
			}
		} else {
			System.out.println("##### No existen adyacentes para ese nodo #####");
			return;
		}
		
		System.out.println("##### Número de Adyacentes: " + matrixConnection[nodeParameter][nodeParameter] + " #####");
		for (int i = 0; i < adjacentsAux.length; i++){
			System.out.println(nodeParameter + " " + adjacentsAux[i] + " CONECTADOS");
		} 
	}
	
	// Gets and sets
	public int getNodes1(){
		return nodes1;
	}
	
	public int getNodes2(){
		return nodes2;
	}
	
	public int getConnections(){
		return connections;
	}
	
	public int [][] getMatrixConnections(){
		return matrixConnection;
	}
	
	public int [][] getNodesAdjacents(){
		return nodesAdjacents;
	}
	
	public int getNumNodesAdjacents (int nodeParameter){
		if (!(nodeParameter < nodes2))
			return -1;

		return matrixConnection[nodeParameter][nodeParameter];
	}
	
	public File getFile(){
		return file;
	}
	
	public int getLengthMatrixConnections () {
		return matrixConnection.length;
	}
}