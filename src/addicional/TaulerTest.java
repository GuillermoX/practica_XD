package addicional;

public class TaulerTest {

    public static void main(String[] args) {
        Tauler tablero = new Tauler();

        // Prueba de tablero vacío
        System.out.println("Tablero vacío:");
        tablero.imprimirTablero();

        // Prueba de jugada válida
        System.out.println("\nHaciendo una jugada en la posición 1 para el jugador 1 (X):");
        tablero.jugada(1, 1);
        tablero.imprimirTablero();

        // Prueba de jugada inválida (posición ya ocupada)
        System.out.println("\nIntentando jugar en una posición ya ocupada:");
        boolean jugadaValida = tablero.jugada(2, 5);
        System.out.println("¿Jugada válida? " + jugadaValida);
        tablero.imprimirTablero();

        // Jugadas adicionales para verificar ganador
        tablero.jugada(1, 2);
        tablero.jugada(1, 3);

        System.out.println("\nTablero después de tres jugadas de 'X':");
        tablero.imprimirTablero();

        // Verificar ganador
        int ganador = tablero.ganador();
        System.out.println("\nGanador: " + (ganador == 1 ? "1" : (ganador == 2 ? "2" : "Ninguno")));
    }
}
