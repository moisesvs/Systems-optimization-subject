package Application;

public class Node {

		private boolean isClient;
		private int id;
		private int coordX;
		private int coordY;
		private int demand;
		
		public Node(boolean isClient, int id, int coordX, int coordY, int demand){
			this.isClient = isClient;
			this.id = id;
			this.coordX = coordX;
			this.coordY = coordY;
			this.demand = demand;
		}

		/**
		 * @param isClient the isClient to set
		 */
		public void setClient(boolean isClient) {
			this.isClient = isClient;
		}

		/**
		 * @return the isClient
		 */
		public boolean isClient() {
			return isClient;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param coordX the coordX to set
		 */
		public void setCoordX(int coordX) {
			this.coordX = coordX;
		}

		/**
		 * @return the coordX
		 */
		public int getCoordX() {
			return coordX;
		}

		/**
		 * @param coordY the coordY to set
		 */
		public void setCoordY(int coordY) {
			this.coordY = coordY;
		}

		/**
		 * @return the coordY
		 */
		public int getCoordY() {
			return coordY;
		}

		/**
		 * @param demand the demand to set
		 */
		public void setDemand(int demand) {
			this.demand = demand;
		}

		/**
		 * @return the demand
		 */
		public int getDemand() {
			return demand;
		}
		
		public String toString(){
			String cad = "";
			cad += "########################################\n";
			cad += "Cliente: " + this.isClient + " \n";
			cad += "Id: " + this.id + " \n"; 
			cad += "Coordenada X: " + this.coordX + " \n"; 
			cad += "Coordenada Y: " + this.coordY + " \n"; 
			cad += "Demanda: " + this.demand + " \n";
			cad += "########################################\n";

			return cad;
		}
}