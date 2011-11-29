package LocalSearch;

import java.util.Random;

import Application.MMDPInstance;
import Application.SolutionMMDP;

public abstract class LocalSearch {
		
		/**
		 * instance MMDP problem
		 */
		protected MMDPInstance instance;
		
		/**
		 * Random number to create neighbors
		 */
		protected Random randomNumber;
		
		/**
		 * Number nodes instance
		 */
		protected int numNodes;
		
		/**
		 * Number nodes selected instance
		 */
		protected int numNodesSelected;
		
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
		public LocalSearch (MMDPInstance instance){
			this.instance = instance;
			
			this.randomNumber = new Random();
			numNodes = this.instance.getNumNodes();
			numNodesSelected = this.instance.getNumNodesSelection();
		}
		
		/**
		 * Execute local search algorithm to solution MMDP attribute
		 * @param solutionMMDP The solution current MMDP
		 * @param initTime init time that begin this algorithm
		 * @return SolutionMMDP not posible more local search this solution MMDP
		 */
		public abstract SolutionMMDP executeLocalSearchAlgorithm(SolutionMMDP solutionMMDP, long initTime);
		
		// get and sets
		public void setInstance (MMDPInstance instance){
			this.instance = instance;
		}
		
		public void setRandom (Random random){
			this.randomNumber = random;
		}
		
		public MMDPInstance getInstance (){
			return instance;
		}
		
		public Random getRandomNumber (){
			return randomNumber;
		}
}
