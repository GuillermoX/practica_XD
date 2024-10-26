import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class Client3ER
{
	// Constants indicadores dels tamanys dels paquets que ha de rebre/enviar (respectivament) la classe Client3ER
	public static final int MIDA_PAQUET = 10;

	public static final int P_CONNECT = 1;

	public static void main(String[] args)
	{
		if(args.length == 2) // Comprovació de paràmetres
		{
			boolean connectat = false;
			DatagramSocket socket = null; // Inicialització del Socket fora del bucle per assegurar una única instància + Assegurar tancament correcte
			try 
			{
				socket = new DatagramSocket(); // Inicialització del Socket per iniciar Comunicacions

				//Es prepara l'adreça i el port del servidor
				InetAddress adrecaServidor = InetAddress.getByName(args[0]);
				int portServidor = Integer.parseInt(args[1]);

				while (true) // Bucle infinit fins que no es tria l'opció de finalitzar connexió
				{ 
					switch (menu()) 
					{
						case 1: // Enviar Paquet inicial de connexió
					 		if (!connectat) 
							{ 
								// Paquet inicialitzador de Connexió entre Client/Servidor
								byte[] valorIniciConnexio = new byte[P_CONNECT];
								valorIniciConnexio[0] = 0;	// 0: Indicador d'intent de connexxió per part del Client
								DatagramPacket paquetIniciConnexio = new DatagramPacket(valorIniciConnexio, valorIniciConnexio.length, adrecaServidor, portServidor);
								
								// Enviem paquet d'intent de Connexió
								socket.send(paquetIniciConnexio); 
								System.out.println("Intent de Connexió en Tramesa. Espereu resposta...");

								// Paquet de Confirmació de Connexió que rebrem del Servidor3ER
								byte[] confirmacio = new byte[MIDA_PAQUET];
								DatagramPacket paquetConnexio = new DatagramPacket(confirmacio, confirmacio.length);

								socket.receive(paquetConnexio); // Rebem array del Servidor3ER

								if (confirmacio[0] == 0) // Confirmem manualment si s'ha establert connexió
								{
									connectat = true;
									System.out.println("Connexió establerta");
								}
								else
								{
									System.out.println("Connexió rebutjada pel servidor");
								}
								break;	
							}
							else 
							{
								System.out.println("Ja s'ha realitzat connexió");
							}
							
						case 2: // Realitzar jugada
							if (connectat) 
							{
								// Rebem per part de Servidor3ER l'estat actual del joc
								byte[] arrayEstatJoc = new byte[MIDA_PAQUET];
								DatagramPacket estatActualTauler = new DatagramPacket(arrayEstatJoc, arrayEstatJoc.length);
								System.out.println("Analitzant Estat Actual de la Partida des del Servidor...");
								socket.receive(estatActualTauler);
								imprimirTablero(arrayEstatJoc);

								// Comprovem estats
								if (arrayEstatJoc[0] == -1) { // Es pot realitzar Jugada
									// Escanejar Jugada
								}
								else 
								{
									if (arrayEstatJoc[0] == -2) { // S'ha guanyat
										System.out.println("Enhorabona! Has Guanyat");
										connectat = false;
									}
									else
									{
										if (arrayEstatJoc[0] == -3) { // S'ha perdut
											System.out.println("Llastima! Has Perdut");
											connectat = false;
										}
										else // Si no es detecta un dels estats de joc esperats
										{
											System.out.println("Error detectat en el paquet rebut des del servidor");
										}
									}
								}
								
							}
							else
							{
								System.out.println("Connexió Prèvia Requerida Per Jugar");
							}
							break;

						case 3: // Desconnexió del Client
							
								System.out.println("Finalitzant Ejecució...");
								connectat = false;
								return;
							
						default: // Cas per a opcions NO vàlides
							System.out.println("Opció no vàlida");
							break;
	
					}
				}		
			} 
			catch (Exception e) 
			{
				e.printStackTrace();	
			}
			finally 
			{
				socket.close();	// Tancament segur del Socket en sortir del bucle amb l'opció de desconnexió
			}
			
		}
		else // Cas de pas de paràmetres incorrectes
		{
			System.out.println("Introdueix l'adreça i el port del servidor");
		}
	}

	/**
	 * * Métode que mostra un menú amb opcions a triar
	 * @return valor enter indicador de l'opció triada
	 */
	private static int menu() {
		int opcio;

		System.out.println("\n\n1- Iniciar Connexió Amb El Servidor");
		System.out.println("2- Realitzar Jugada (Requereix Connexió Establerta Prèviament)");
		System.out.println("3- Finalitzar Estat De Connexió i/o Ejecució Del Programa\n");

		System.out.println("Seleccioneu una opció");
		Scanner triar_opcio = new Scanner(System.in);
		
		opcio = triar_opcio.nextInt();
		triar_opcio.close();
		if (opcio < 1 || opcio > 3) {
			opcio = -1;
		}
		return opcio;
	}

	public static void imprimirTablero(byte[] estadoTablero) {
		if (estadoTablero.length != 10) {
			System.out.println("Error: el array del tablero debe tener un tamaño de 10.");
			return;
		}
	
		System.out.println("Estado actual del tablero:");
	
		// Recorrer las posiciones del tablero 3x3 ignorando el índice 0
		for (int i = 1; i <= 9; i++) {
			char simbolo;
			
			// Determinar el símbolo a mostrar en función del estado de la casilla
			switch (estadoTablero[i]) {
				case 1:
					simbolo = 'X';  // Jugador 1
					break;
				case 2:
					simbolo = 'O';  // Jugador 2
					break;
				default:
					simbolo = ' ';  // Casilla vacía
					break;
			}
	
			// Imprimir la casilla con separador de línea o borde según la posición
			System.out.print(" " + simbolo + " ");
			if (i % 3 == 0 && i < 9) {
				System.out.println("\n---+---+---"); // Línea divisoria entre filas
			} else if (i % 3 != 0) {
				System.out.print("|"); // Separador entre columnas
			}
		}
		System.out.println(); // Nueva línea final para terminar el tablero
	}
	
		
}

