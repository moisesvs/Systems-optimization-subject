package LocalSearch;

import java.util.Random;

import Application.CutWidthInstance;
import Application.SolutionCutWidth;

public abstract class LocalSearch {
		
		/**
		 * instance CutWidth problem
		 */
		protected CutWidthInstance instance;
		
		/**
		 * Random number to create neighbors
		 */
		protected Random randomNumber;
		
		/**
		 * Number nodes 1 instance 
		 */
		protected int numNodes1;
		
		/**
		 * Number nodes 1 instance 
		 */
		protected int numNodes2;
		
		/**
		 * Number nodes selected instance
		 */
		protected int numNodesSelected;
		
		/**
		 * Matrix connections
		 */
		private int [][] matrixConnection;
		
		/**
		 * Nodes adjacents
		 */
		private int [][] nodesAdjacents;
		/**
		 * Constructor default LocalSearch
		 */
		public LocalSearch (){ 
			this.randomNumber = new Random();
		}
		
		/**
		 * Constructor LocalSearch
		 * @param instance
		 * @param solution
		 */
		public LocalSearch (CutWidthInstance instance){
			this.instance = instance;
			
			this.randomNumber = new Random();
			numNodes1 = this.instance.getNodes1();
			numNodes2 = this.instance.getNodes2();

			matrixConnection = this.instance.getMatrixConnections();
			nodesAdjacents = this.instance.getNodesAdjacents();

		}
		
		/**
		 * Execute local search algorithm to solution CutWidth attribute
		 * @param solutionCutWidth The solution current CutWidth
		 * @param initTime init time that begin this algorithm
		 * @return SolutionCutWidth not posible more local search this solution CutWidth
		 */
		public abstract SolutionCutWidth executeLocalSearchAlgorithm(SolutionCutWidth solutionCutWidth, long initTime);
		
		// get and sets
		public void setInstance (CutWidthInstance instance){
			this.instance = instance;
		}
		
		public void setRandom (Random random){
			this.randomNumber = random;
		}
		
		public CutWidthInstance getInstance (){
			return instance;
		}
		
		public Random getRandomNumber (){
			return randomNumber;
		}
}
