package Application;

import java.util.concurrent.TimeUnit;

public class SolutionCutWidth {
		
	/**
	 * Solution array
	 */
	private int [] solution;
	
	/**
	 * Instance problem MMDP
	 */
	private CutWidthInstance instanceCutwidth; 
	
	/**
	 * Instance problem MMDP
	 */
	private float valueFunctionObjetive; 

	/**
	 * Time in find this solution
	 */
	private float timeFindSolution; 
	
	public SolutionCutWidth (CutWidthInstance instance, int [] solution){
		this.instanceCutwidth = instance;
		this.solution = solution;
	}
	
	public void setSolution (int [] solution){
		this.solution = solution;
	}
	
	public int [] getSolution(){
		return this.solution;
	}
	
	/**
	 * Solution is factible if the length array not higher that the number nodes M,
	 * and the number position true not higher that number nodes N
	 * @return
	 */
	public boolean isFactible(){
		
		// Check if not higher that num 
		if (this.solution.length != (this.instanceCutwidth.getLengthMatrixConnections()))
			return false;
				
		// Check if not higher that num connections
		// TODO: if the numbers nodes not repited
				
		return true;
	}
	
	public String toString(){
		
		String cad = "";
		
		for (int i = 0; i < this.solution.length; i ++)
			cad += this.solution[i] + "  ";
		
		return cad;
	}

	// Gets and sets
	/**
	 * @param valueFunctionObjetive the valueFunctionObjetive to set
	 */
	public void setValueFunctionObjetive(float valueFunctionObjetive) {
		this.valueFunctionObjetive = valueFunctionObjetive;
	}

	/**
	 * @return the valueFunctionObjetive
	 */
	public float getValueFunctionObjetive() {
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
	 * Print solution
	 * @param totalTime
	 * @param instance
	 * @param bestSolution
	 */
	public void printSolution (){
		System.out.println("#####################################################################################################");
		System.out.println("Time(miliseconds): " + timeFindSolution + " ms");
		System.out.println("Time(minutes): " + TimeUnit.MILLISECONDS.toMinutes((long)timeFindSolution) + " m");
		System.out.println("Nodes choose (" + instanceCutwidth.getFile().getName() + "): "  + this);
		System.out.println("Best Solution (" + instanceCutwidth.getFile().getName() + "): "  + this.valueFunctionObjetive);
		System.out.println("#####################################################################################################");
	}
	
	/**
	 * Print format program format mh analizer
	 * @param totalTime
	 * @param instance
	 * @param bestSolution
	 * @return
	 */
	public String printFormatMhAnalizer (){
		return instanceCutwidth.getFile().getName() + "\t" + this.valueFunctionObjetive + "\t" + (long)timeFindSolution + "\t" + this + "\n";
	}
}