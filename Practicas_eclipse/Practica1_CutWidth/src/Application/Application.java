package Application;
import java.io.File;

import Algorithm.GRASP;
import Constructive.RandomSolution;
import LocalSearch.BestImprovement;
import LocalSearch.FirstImprovement;
import LocalSearch.LocalSearch;

public class Application {

	public static void main(String[] args) {
	
		String folder = "instancias";
		CutWidthInstance[] instancesFile = loadingInstances(folder);
		
		String cadAllInstanceGRASP = executeAllInstancesGRASP(instancesFile);
		Toolbox.writeStringToFile(Constants.NAME_FILE + "_GRASP", cadAllInstanceGRASP);
		System.out.println(cadAllInstanceGRASP + "\n");
//		
//		String cadAllInstanceBestImprove = executeAllInstancesBestImprove(instancesFile);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_BestImprove", cadAllInstanceBestImprove);
//		System.out.println(cadAllInstanceBestImprove + "\n");
		
//		String cadAllInstanceFirstImprove = executeAllInstancesFirstImprove(instancesFile);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_FirstImprove", cadAllInstanceFirstImprove);
//		System.out.println(cadAllInstanceFirstImprove + "\n");

//		String cadAllInstanceRandom = executeAllInstancesRandom(instancesFile);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_Random", cadAllInstanceRandom);
		
//		String cadAllInstanceRandomNumSolutions = executeAllInstancesRandomNumSolutions(instancesFile, Constants.NUM_SOLUTIONS);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_RandomNumSolutions", cadAllInstanceRandom);
		
		// print result
//		System.out.println(cadAllInstanceRandom + "\n" + cadAllInstanceRandomNumSolutions);
//		System.out.println(cadAllInstanceRandom + "\n");

	}

	private static CutWidthInstance[] loadingInstances(String carpeta) {
		
		File dir = new File(carpeta);
		File[] instanciasFile = dir.listFiles();

		// Init CutWidth list instance
		CutWidthInstance [] instancesProblem = new CutWidthInstance [instanciasFile.length];
		
		for(int i = 0; i < instanciasFile.length; i++) {
			
			CutWidthInstance instance = new CutWidthInstance(instanciasFile[i]);
			instance.loading();
			instancesProblem[i] = instance;
		}
		
		return instancesProblem;
	}
	
	/**
	 * Execute algorithm with strategy random with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesRandom(CutWidthInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(Random)" + "\n";
		
		for (int instance = 0; instance < instances.length; instance ++){

			CutWidthInstance intance = instances[instance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(intance);
			Algorithm algoritm = new Algorithm(random, intance);
			SolutionCutWidth solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();
			
		}
		
		return cadAux;
	}
	
	/**
	 * Execute algorithm with strategy random with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesRandomNumSolutions(CutWidthInstance [] instances, int numSolutions){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(Random)" + "\n";
		
		for (int instance = 0; instance < instances.length; instance ++){

			CutWidthInstance intance = instances[instance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(intance);
			Algorithm algoritm = new Algorithm(random, intance, numSolutions);
			SolutionCutWidth solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();
			
		}
		
		return cadAux;
	}
	
	
	/**
	 * Execute algorithm with strategy random with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesBestImprove(CutWidthInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(BestImprove)" + "\n";
		
		for (int numInstance = 0; numInstance < instances.length; numInstance ++){

			CutWidthInstance instance = instances[numInstance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(instance);
			LocalSearch bestImproveLocalSearch = new BestImprovement(instance);
			Algorithm algoritm = new Algorithm(random, instance, bestImproveLocalSearch);
			SolutionCutWidth solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();
			
		}
		
		return cadAux;
	}
	
	/**
	 * Execute algorithm with strategy random with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesFirstImprove(CutWidthInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(FirstImprove)" + "\n";
		
		for (int numInstance = 0; numInstance < instances.length; numInstance ++){

			CutWidthInstance instance = instances[numInstance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(instance);
			LocalSearch bestImproveLocalSearch = new FirstImprovement(instance);
			Algorithm algoritm = new Algorithm(random, instance, bestImproveLocalSearch);
			SolutionCutWidth solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();
			
		}
		
		return cadAux;
	}
	
	/**
	 * Execute algorithm with strategy random with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesGRASP(CutWidthInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(GRASP)" + "\n";
		
		for (int numInstance = 0; numInstance < instances.length; numInstance ++){

			CutWidthInstance instance = instances[numInstance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(instance);
			LocalSearch firstImproveLocalSearch = new FirstImprovement(instance);
			GRASP algoritm = new GRASP(instance, firstImproveLocalSearch);
			SolutionCutWidth solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();
			
		}
		
		return cadAux;
	}
}
