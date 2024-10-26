package addicional;

public class Tauler{
   byte[] tauler = new byte[9];

   public Tauler(){
        for(int i = 0; i<tauler.length; i++){
            tauler[i] = 0;
        }
   }
}