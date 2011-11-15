package Constructive;

import java.util.Random;

import Application.CutWidthInstance;
import Application.SolutionCutWidth;

public abstract class Constructive {
	
	/**
	 * Instance problem CutWidth
	 */
	protected CutWidthInstance instanceCutWidth;
	
	/**
	 * Instance random generator
	 */
	protected Random generator;

	/**
	 * Number nodes instance CutWidth
	 */
	protected int numNodes1;
	
	/**
	 * Number nodes instance CutWidth
	 */
	protected int numNodes2;
	
	/**
	 * Connections between nodes
	 */
	protected int connections;

	/**
	 * Default constructor CutWidth
	 * @param instanceCutWidth
	 */
	public Constructive (CutWidthInstance instanceCutWidth){
		this.instanceCutWidth = instanceCutWidth;
		this.numNodes1 = this.instanceCutWidth.getNodes1();
		this.numNodes2 = this.instanceCutWidth.getNodes2();
		this.connections = this.instanceCutWidth.getConnections();
		this.generator = new Random();
	}
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	protected abstract SolutionCutWidth createSolution();
	
	/**
	 * Create a list of the length parameter numSolutions
	 * @param numSolutions
	 * @return the list of the length numSolutions
	 */
	protected abstract SolutionCutWidth [] createSolutionsArray(int numSolutions);
}