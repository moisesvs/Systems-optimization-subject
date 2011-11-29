package Application;

public class Constants {

		/**
		 * Description algorithm
		 */
		public static final String DESCRIPTION_ALGORITHM = "Algorithm CutWidth";
		
		/**
		 * Name file
		 */
		public static final String NAME_FILE = "./Solutions/Solution_Current/Algorithm_CutWidth";
		
		// Parameters algorithm GRASP
		/**
		 * Nodes connection
		 */
		public static float ALPHA = 0.5f;
		
		// Other parameters
		/**
		 * Nodes connection
		 */
		public static int NODES_NOT_CONNECTION = -1;
		
		/**
		 * Nodes without connection
		 */
		public static int NODES_CONNECTION = 0;
		
		/**
		 * Number solution generates to algorithm random
		 */
		public static final int NUM_SOLUTIONS = 1000;
		
		/**
		 * Time in minutes that is execute algorithm
		 */
		public static final float TIME_ALGORITHM = 1f;
		
		/**
		 * Time in milliseconds that is execute algorithm
		 */
		public static final float TIME_ALGORITHM_MILISECONDS = (TIME_ALGORITHM * 60) * 1000;
}
