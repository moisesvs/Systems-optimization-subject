package Constructive;

import java.util.Random;

import Application.MMDPInstance;
import Application.SolutionMMDP;

public abstract class Constructive {
	
	/**
	 * Instance problem MMDP
	 */
	protected MMDPInstance instanceMMDP;
	
	/**
	 * Instance random generator
	 */
	protected Random generator;

	/**
	 * Number nodes instance MMDP
	 */
	protected int numNodesSelection;

	/**
	 * Number nodes instance MMDP
	 */
	protected int numNodes;

	/**
	 * Default constructor MMDP
	 * @param instanceMMDP
	 */
	public Constructive (MMDPInstance instanceMMDP){
		this.instanceMMDP = instanceMMDP;
		this.numNodesSelection = this.instanceMMDP.getNumNodesSelection();
		this.numNodes = this.instanceMMDP.getNumNodes();
		this.generator = new Random();
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	protected abstract SolutionMMDP createSolution();
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	protected abstract SolutionMMDP [] createSolutionsArray(int numSolutions);
}