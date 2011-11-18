package Application;
import java.io.File;

import Algorithm.Algorithm;
import Algorithm.TabuSearch;
import Constructive.RandomSolution;
import LocalSearch.BestImprovement;
import LocalSearch.FirstImprovement;
import LocalSearch.LocalSearch;
/* 
Construir una clase solución.
Construir una solución aleatoria quitando elementos.

Opciones:
	Tirar un dado y donde caiga si esta ocupado vuelvo a tirar hasta que salga libre
	Tirar un dado y donde me caiga le pongo a uno si eso me voy al siguiente 0
	Tirar un dado y quitar ese elemento de la lista hasta el siguiente elemento.

Evaluar la solución por z(x) para que me de algo = 37. Si tengo otra solución que me da	45. 45 es mejor solución al
tratarse de una solución mejor por que tengo que maximizar. Hay que recordar la mejor solución vista hasta el momento.

Construir un metodo que me diga si es solución factible
Construir un metodo que me diga lo que vale la función objetivo

La instancia es una clase instancia: Instancia que esta la clase MMDP y tiene las distancias en el constructor.
La clase soluciones tiene una referencia (getObjetiveFunction), tiene que tener el número de M y N. Tendra que tener un array
que será la solución.

boolean [] solution -- Tiene que ser factible
setObjetiveFunctionValue -- Podría ser que te dan el valor de la función objetivo

Se cosntuyen soluciones aleatorias, clase RamdomConstructive, y me genera 1000 solucciones aleatorias o lo que sean. En el constructor
debe de ir indicado el número de soluciones que van a ir. Esta clase va a tener una Solution y ademas va a tener un int con el bestSolution


Cutwidth un array de enteros la solución
Una instancia será una matrix de conexiones.


p-Hub 
La instancia me determina cuantos tienen que actuar como servidores y cuantos como nodos.  El objetivo es decidir a que servidor engancho cada
uno de los clientes. La estructura de la solución

La funcion objetivo es la suma de las distancia de cada cliente a su servidor. y minimizar las distancias.

Practica 3

Best improve evaluar todas las soluciones vecinas y me quedo con la mejor, por eso se llama best improve, tenemos que hacer la vecindad
con solamente un movimiento, no dos movimientos.

El best improve es un proceso iterativo voy evaluando vencindarios de todos y vendindarios siempre y siempre y voy haciendo así hasta que no
puedo ir mejorando con la vencidad que tengo. Esto es muy costoso, y cada una de esas tengo que evaluar la función objetivo y esto es muy
costoso. A veces llega a un callejon sin salida siempre va por la mejor la mejor, y te quedas ahí encajado.

El first improbe en ved de evaluar todo el vecindario tengo la solucón original y voy evaluando las soluciones vecinas y que es mejor que la
actual y me muevo a esa y así sucesivamente. En este caso no se si me mueveo a la mejor solución me muevo a una mejor, pero no se si la mejor
Esta estrategia es menos costosa. En este caso le estoy dando mas prioridad a las nodos más bajos, por que los estoy mirando más veces. 
En general cuando se utilizan y se generan los intercambios hay que valorar y mirar como se realizan los intercambios, ordén lexicografico, 
pero generalmente se intercambian con orden aleatorio. Tiro el dado y un orden lexicografico. Tiro un dado y un llevo un orden lexicografico.
Doy a todos la probabilidad de participar en un movimiento de intercambio.

mhAnalizer es mejor cuanto menor es la desviación

*/
public class Application {

	public static void main(String[] args) {
		String folder = "instancias/GKD-Ia";
//		String folder = "instancias/GKD-Ic";

		MMDPInstance[] instancesFile = loadingInstances(folder);
		
		String cadAllInstanceFirstImproveRandom = executeAllInstancesTabuSearch(instancesFile);
		Toolbox.writeStringToFile(Constants.NAME_FILE + "_FirstImproveRandom", cadAllInstanceFirstImproveRandom);
		
//		String cadAllInstanceFirstImproveRandom = executeAllInstancesFirstImproveRandom(instancesFile);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_FirstImproveRandom", cadAllInstanceFirstImproveRandom);
//		
//		String cadAllInstanceFirstImproveLexicographic = executeAllInstancesFirstImproveLexicographic(instancesFile);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_FirstImproveLexicographic", cadAllInstanceFirstImproveLexicographic);
		
//		String cadAllInstanceBestImprove = executeAllInstancesBestImprove(instancesFile);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_BestImprove", cadAllInstanceBestImprove);

//		String cadAllInstanceRandom = executeAllInstancesRandom(instancesFile);
//		Toolbox.writeStringToFile(Constants.NAME_FILE + "_Random", cadAllInstanceRandom);
//		
//		// print result
//		System.out.println(cadAllInstanceFirstImprove + "\n" + cadAllInstanceBestImprove + "\n" + cadAllInstanceRandom);
//		System.out.println(cadAllInstanceFirstImproveRandom + "\n" + cadAllInstanceFirstImproveLexicographic + "\n");

	}

	/**
	 * Loading all instance of the folder parameter
	 * @param folder find instance into folder
	 * @return list of the instance find
	 */
	private static MMDPInstance[] loadingInstances(String folder) {
		
		File dir = new File(folder);
		File[] instanciasFile = dir.listFiles();
		
		// Init MMDP Instante
		MMDPInstance [] instancesProblem = new MMDPInstance[instanciasFile.length];
		
		for (int i = 0; i < instanciasFile.length; i++) {
			
			MMDPInstance instance = new MMDPInstance(instanciasFile[i]);
			instance.loading();
			instancesProblem[i] = instance;
			
		}
		
		return instancesProblem;
	}
	
	/**
	 * Return the instance with name file of the parameter
	 * @param instancesFile
	 * @param nameFile
	 * @return the instance find with this name
	 */
	private static MMDPInstance getInstanceFile (MMDPInstance[] instancesFile, String nameFile){
		
		for (int i = 0; i < instancesFile.length; i ++){
			MMDPInstance instanceAux = instancesFile[i];
			if (instanceAux.getFile().getName().equals(nameFile))
				return instanceAux;
		}
		
		return null;
	}
	
	/**
	 * Execute algorithm with strategy first improve with instances mmdp parameters
	 * @param instances
	 * @return
	 */
	private static String executeAllInstancesFirstImproveLexicographic(MMDPInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(First Improve Lexicographic)" +"\n";
		
		for (int instance = 0; instance < instances.length; instance ++){

			MMDPInstance intance = instances[instance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(intance);
			LocalSearch localSearchFirstImprove = new FirstImprovement(intance, false);
			Algorithm algoritm = new Algorithm(random, intance, localSearchFirstImprove);
			SolutionMMDP solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();

		}
		
		return cadAux;
	}
	
	/**
	 * Execute algorithm with strategy first improve with instances mmdp parameters
	 * @param instances
	 * @return
	 */
	private static String executeAllInstancesFirstImproveRandom(MMDPInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(First Improve Random)" +"\n";
		
		for (int instance = 0; instance < instances.length; instance ++){

			MMDPInstance intance = instances[instance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(intance);
			LocalSearch localSearchFirstImprove = new FirstImprovement(intance, true);
			Algorithm algoritm = new Algorithm(random, intance, localSearchFirstImprove);
			SolutionMMDP solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();

		}
		
		return cadAux;
	}
	
	/**
	 * Execute algorithm with strategy best improve with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesBestImprove(MMDPInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(Best Improve)" + "\n";
		
		for (int instance = 0; instance < instances.length; instance ++){

			MMDPInstance intance = instances[instance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(intance);
			LocalSearch localSearchBestImprove = new BestImprovement(intance);
			Algorithm algoritm = new Algorithm(random, intance, localSearchBestImprove);
			SolutionMMDP solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();

		}
		
		return cadAux;
	}
	
	/**
	 * Execute algorithm with strategy random with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesRandom(MMDPInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(Random)" + "\n";
		
		for (int instance = 0; instance < instances.length; instance ++){

			MMDPInstance intance = instances[instance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(intance);
			Algorithm algoritm = new Algorithm(random, intance);
			SolutionMMDP solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();
			
		}
		
		return cadAux;
	}
	
	/**
	 * Execute algorithm with strategy tabu searh with instances mmdp parameters
	 * @param instances read of the file
	 * @return string to write file
	 */
	private static String executeAllInstancesTabuSearch(MMDPInstance [] instances){
		
		String cadAux = Constants.DESCRIPTION_ALGORITHM + "(Tabu Search)" + "\n";
		
		for (int instance = 0; instance < instances.length; instance ++){

			MMDPInstance intance = instances[instance];

			// Creator new random solution with new instance
			RandomSolution random = new RandomSolution(intance);
			Algorithm algoritm = new TabuSearch(random, intance, Constants.K_ITERATIONS_TABU);
			SolutionMMDP solution = algoritm.getBestSolution();
			cadAux += solution.printFormatMhAnalizer();
			
		}
		
		return cadAux;
	}
}