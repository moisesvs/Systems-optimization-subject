package Application;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Toolbox {

		/**
		 * Write a string parameter in a file of the text
		 * @param nameFile
		 * @param cad
		 */
		public static void writeStringToFile (String nameFile, String cad){
			
			PrintWriter writer;
			try {
				writer = new PrintWriter(nameFile);
				// write string
				writer.print(cad);
				writer.close();
				
			} catch (FileNotFoundException e) {
				System.err.println("Error: problem write into file");
			}
		}
}
