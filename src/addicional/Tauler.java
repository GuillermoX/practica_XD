package addicional;

public class Tauler{
   byte[] tauler = new byte[9];

   public Tauler(){
        for(int i = 0; i<tauler.length; i++){
            tauler[i] = 0;
        }
   }


   public void setTablero(byte[] infoJoc){
        
        for(int i = 0; i<tauler.length; i++){
            tauler[i] = infoJoc[i+1];
        }
    }

   public byte[] rawTablero(){
        return tauler.clone();
    }

    public Tauler cpyTauler(){
        Tauler copia = new Tauler();
        copia.tauler = tauler.clone(); 
        return copia;
    }


    public void imprimirTablero(){
        for(int i=0; i<tauler.length; i++) {
            System.out.print(" " + tauler[i] + " ");
			if ((i+1) % 3 != 0) {
                System.out.print("|");

			} else if (i<tauler.length-1) {
				System.out.println("\n----+----+----");  
			}
        }
        System.out.printf("\n");
    }    

   public boolean jugada(int jugador, int posicion){
        boolean jugadaCorrecta = false;

        if((posicion > 0) && (9 > posicion) && (tauler[posicion-1] == 0)){
            tauler[posicion-1] = (byte)jugador;
            jugadaCorrecta = true;
        }

        return jugadaCorrecta;
   }


   public int ganador(){
        int valor = 0;
        // tres en raya filas
        if (tauler[0] == tauler[1] && tauler[1] == tauler[2]) 
        valor = tauler[0]; // Fila 1: 1, 2, 3
        if (tauler[3] == tauler[4] && tauler[4] == tauler[5]) 
        valor = tauler[3]; // Fila 2: 4, 5, 6
        if (tauler[6] == tauler[7] && tauler[7] == tauler[8]) 
        valor = tauler[6]; // Fila 3: 7, 8, 9

        // tres en raya columnas
        if (tauler[0] == tauler[3] && tauler[3] == tauler[6]) 
        valor = tauler[0]; // Columna 1: 1, 4, 7
        if (tauler[1] == tauler[4] && tauler[4] == tauler[7]) 
        valor = tauler[1]; // Columna 2: 2, 5, 8
        if (tauler[2] == tauler[5] && tauler[5] == tauler[8]) 
        valor = tauler[2]; // Columna 3: 3, 6, 9

        // tres en raya diagonales
        if (tauler[0] == tauler[4] && tauler[4] == tauler[8]) 
        valor = tauler[0]; // Diagonal: 1, 5, 9
        if (tauler[2] == tauler[4] && tauler[4] == tauler[6]) 
        valor = tauler[2]; // Diagonal: 3, 5, 7 

        return valor;
    }
        
}