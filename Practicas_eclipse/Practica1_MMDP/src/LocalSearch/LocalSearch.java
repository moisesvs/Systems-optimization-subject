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
		}
		
		/**
		 * Execute local search algorithm to solution MMDP attribute
		 * @return SolutionMMDP not posible more local search this solution MMDP
		 */
		public abstract SolutionMMDP executeLocalSearchAlgorithm(SolutionMMDP solutionMMDP);
		
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
