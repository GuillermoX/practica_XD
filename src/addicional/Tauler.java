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
            if(tauler[i] == 0){
                System.out.print("   ");
            }
            else if (tauler[i] == 1){
                System.out.print(" X ");
            }
            else{
                System.out.print(" O ");
            }
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
        for(int i=0; i<7; i+=3){
            if((tauler[i] != 0) && (tauler[i] == tauler[i+1]) && (tauler[i+1] == tauler[i+2])){
                valor = tauler[i];
            }
        }
        for(int i=0; i<3; i++){
            if((tauler[i] != 0) && (tauler[i] == tauler[i+3]) && (tauler[i+3] == tauler[i+6])){
                valor = tauler[i];
            }
        }
        if((tauler[0] != 0) && (tauler[0] == tauler[4]) && (tauler[4] == tauler[8])){
            valor = tauler[0];
        }
        if((tauler[2] != 0) && (tauler[2] == tauler[4]) && (tauler[4] == tauler[6])){
            valor = tauler[2];
        }

        return valor;
    }
        
}