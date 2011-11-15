package Application;
import java.io.File;

import Constructive.RandomSolution;

public class Application {

	public static void main(String[] args) {
	
		String folder = "instancias";
		CutWidthInstance[] instancesFile = loadingInstances(folder);

		String cadAllInstanceRandom = executeAllInstancesRandom(instancesFile);
		Toolbox.writeStringToFile(Constants.NAME_FILE + "_Random", cadAllInstanceRandom);
		
//		String cadAllInstanceRandomNumSolutions = executeAllInstancesRandomNumSolutions(instancesFile, Constants.NUM_SOLUTIONS);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_RandomNumSolutions", cadAllInstanceRandom);
		
		// print result
//		System.out.println(cadAllInstanceRandom + "\n" + cadAllInstanceRandomNumSolutions);
		System.out.println(cadAllInstanceRandom + "\n");

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
}
