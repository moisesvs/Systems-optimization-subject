package Application;
import java.io.File;
import java.util.concurrent.TimeUnit;


public class Application {

	public static void main(String[] args) {
		
//		String folder = "instancias/phub_100_10";
//		loadingInstances(folder);
		
		String folder2 = "instancias/phub_50_5";
		loadingInstances(folder2);
		
		String folder = "instancias";
		PHubInstance [] instancesFile = loadingInstances(folder2);
		PHubInstance instanceChoose = getInstanceFile(instancesFile, "phub_50_5_2.txt");

		// Time execute algorithm
		long timeFirst = System.currentTimeMillis();
		SolutionPHub bestSolution = executeAlgorithmTime(instanceChoose, Constants.TIME_ALGORITHM_MILISECONDS);
		long totalTime = System.currentTimeMillis() - timeFirst;
		
		System.out.println("Time(miliseconds): " + totalTime + " ms");
		System.out.println("Time(minutes): " + TimeUnit.MILLISECONDS.toMinutes(totalTime) + " m");
		System.out.println("Nodes choose (" + instanceChoose.getFile().getName() + "): "  + bestSolution);
		System.out.println("Best Solution (" + instanceChoose.getFile().getName() + "): "  + bestSolution.getValueFunctionObjetive());
		
	}

	private static PHubInstance [] loadingInstances(String carpeta) {
		
		File dir = new File(carpeta);
		File[] instanciasFile = dir.listFiles();

		// Init PHubInstance list
		PHubInstance [] instancesProblem = new PHubInstance [instanciasFile.length];
		
		for(int i = 0; i < instanciasFile.length; i++) {
			
			PHubInstance instance = new PHubInstance(instanciasFile[i]);
			instance.loading();
			instancesProblem[i] = instance;
		}
		
		return instancesProblem;
	}
	
	private static PHubInstance getInstanceFile (PHubInstance [] instancesFile, String nameFile){
		
		for (int i = 0; i < instancesFile.length; i ++){
			PHubInstance instanceAux = instancesFile[i];
			if (instanceAux.getFile().getName().equals(nameFile))
				return instanceAux;
		}
		
		return null;
	}
	
	/**
	 * Execute algorithm with set solutions
	 * @param instance
	 * @param solutions
	 * @return
	 */
	private static SolutionPHub executeAlgorithmTime (PHubInstance instance, long timeAlgorithm){
		// Creator solutions
		RandomSolution random = new RandomSolution(instance);
		
		// Max solution
		double bestSolutionValue = Float.MAX_VALUE;
		SolutionPHub analizingSolution = random.createSolution();
		SolutionPHub bestSolution = analizingSolution;

		long timeIni = System.currentTimeMillis();
		for (;;){
			
			double valueFuntionObjetive = instance.getValueObjetiveFunction(analizingSolution);
			
			if (instance.bestValueObjetiveFunction(bestSolutionValue, valueFuntionObjetive)){
				bestSolutionValue = valueFuntionObjetive;
				bestSolution = analizingSolution;
				bestSolution.setValueFunctionObjetive(valueFuntionObjetive);
			}
			
			analizingSolution = random.createSolution();
			
			if ((System.currentTimeMillis() - timeIni) >= Constants.TIME_ALGORITHM_MILISECONDS)
				break;
		}
		
		return bestSolution;
	}
}