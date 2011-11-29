package Application;

public class Constants {

		/**
		 * Description algorithm
		 */
		public static final String DESCRIPTION_ALGORITHM = "Algorithm MMDP";
		
		/**
		 * Name file
		 */
		public static final String NAME_FILE = "./Solutions/Algorithm_MMDP";

		// Variables algorithm VNS (VND - Variant Neighborhood Descent)
		/**
		 * Number neighbord VNS
		 */
		public static final int K_NEIGHBORD_VNS = 3;
		
		// Variables algorithm Tabu Search
		/**
		 * Time in minutes that is execute algorithm
		 */
		public static final int NOT_IMPROVE = -1;
		
		/**
		 * Time in minutes that is execute algorithm
		 */
		public static final int TABU = 1;
		
		/**
		 * Time in minutes that is execute algorithm
		 */
		public static final int NOT_TABU = 0;
		
		/**
		 * Number the aspirations number to objective tabu search
		 */
		public static final int NOT_EVALUATE = -1;
		
		/**
		 * Number the aspirations number to objective tabu search
		 */
		public static final int ASPIRATIONS_NUMBER_OBJECTIVE = 0;
		
		// Variables algorithm First improve, Best improve and random
		/**
		 * Number solution generates to algorithm random
		 */
		public static final int NUM_SOLUTIONS = 4;
		
		/**
		 * Time in minutes that is execute algorithm
		 */
		public static final int K_ITERATIONS_TABU = 49;
		
		/**
		 * Time in minutes that is execute algorithm
		 */
		public static final float TIME_ALGORITHM = 1f;
		
		/**
		 * Time in milliseconds that is execute algorithm
		 */
		public static final float TIME_ALGORITHM_MILISECONDS = (TIME_ALGORITHM * 60) * 1000;

		/**
		 * Time restart algorithm tabu search
		 */
		public static final float TIME_RESTART_MILISECONDS = 1200000/*((TIME_ALGORITHM * 60) * 1000) / 2*/;
}
