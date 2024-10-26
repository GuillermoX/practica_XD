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


   public void imprimirTablero(){
        for(int i=0; i<tauler.length; i++) {
            System.out.printf("%d", tauler[i]);
        }
        System.out.printf("\n");
   }

   public boolean jugada(int jugador, int posicion){
        boolean jugadaCorrecta = false;

        if((posicion > 0) && (tauler[posicion-1] == 0)){
            tauler[posicion-1] = (byte)jugador;
            jugadaCorrecta = true;
        }

        return jugadaCorrecta;
   }


   public int ganador(){
        return 0;
   }

   public byte[] rawTablero(){
        return tauler.clone();
    }
}