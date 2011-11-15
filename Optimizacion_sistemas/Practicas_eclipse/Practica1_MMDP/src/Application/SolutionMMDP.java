package Application;

import java.util.concurrent.TimeUnit;

public class SolutionMMDP {
		
	/**
	 * Solution array
	 */
	private boolean [] solution;
	
	/**
	 * Instance problem MMDP
	 */
	private MMDPInstance instanceMMDP; 
	
	/**
	 * Instance problem MMDP
	 */
	private double valueFunctionObjetive; 
	
	/**
	 * Time in find this solution
	 */
	private float timeFindSolution; 
	
	public SolutionMMDP (MMDPInstance instance, boolean [] solution){
		this.instanceMMDP = instance;
		this.solution = solution;
	}
	
	public void setSolution (boolean [] solution){
		this.solution = solution;
	}
	
	public boolean [] getSolution(){
		return this.solution;
	}
	
	public String toString(){
		
		String cad = "[";
		
		for (int i = 0; i < this.solution.length; i ++)
			cad += this.solution[i] + "  ";
		
		cad += "]";
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
	 * Print solution
	 * @param totalTime
	 * @param instance
	 * @param bestSolution
	 */
	public void printSolution (){
		System.out.println("#####################################################################################################");
		System.out.println("Time(miliseconds): " + timeFindSolution + " ms");
		System.out.println("Time(minutes): " + TimeUnit.MILLISECONDS.toMinutes((long)timeFindSolution) + " m");
		System.out.println("Nodes choose (" + instanceMMDP.getFile().getName() + "): "  + this);
		System.out.println("Best Solution (" + instanceMMDP.getFile().getName() + "): "  + this.valueFunctionObjetive);
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
		return instanceMMDP.getFile().getName() + "\t" + this.valueFunctionObjetive + "\t" + (long)timeFindSolution + "\t" + this + "\n";
	}
}