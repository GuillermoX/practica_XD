import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import addicional.Tauler;
import server.Server3ER;




public class Client3ER
{
	// Constants indicadores dels tamanys dels paquets que ha de rebre/enviar (respectivament) la classe Client3ER
	public static final int MIDA_PAQUET_SERVER = 10;
	public static final int MIDA_PAQUET_CLIENT = 1;

	// Declaració de constants indicadores de l'estat de Joc
	public static final int P_CONNECT = 0;
	public static final int P_TORN = -1;
	public static final int P_GUANYAT = -2; 
	public static final int P_PERDUT = -3;

	static Scanner triar_opcio = new Scanner(System.in);

	public static void main(String[] args)
	{
		if(args.length == 2) // Comprovació de paràmetres
		{
			boolean connectat = false;
			DatagramSocket socket = null; // Inicialització del Socket fora del bucle per assegurar una única instància + Assegurar tancament correcte
			try 
			{
				socket = new DatagramSocket(); // Inicialització del Socket per iniciar Comunicacions
				Tauler tauler = new Tauler();

				//Es prepara l'adreça i el port del servidor
				InetAddress adrecaServidor = InetAddress.getByName(args[0]);
				int portServidor = Integer.parseInt(args[1]);


				byte[] arrayEstatJoc = new byte[MIDA_PAQUET_SERVER];

				while (!connectat) // Bucle infinit fins que no es tria l'opció de finalitzar connexió
				{ 
					switch (menu()) 
					{
						case 1: // Enviar Paquet inicial de connexió
								// Paquet inicialitzador de Connexió entre Client/Servidor
								byte[] valorIniciConnexio = new byte[MIDA_PAQUET_CLIENT];
								valorIniciConnexio[0] = 0;	// 0: Indicador d'intent de connexxió per part del Client
								DatagramPacket paquetIniciConnexio = new DatagramPacket(valorIniciConnexio, valorIniciConnexio.length, adrecaServidor, portServidor);
								
								// Enviem paquet d'intent de Connexió
								socket.send(paquetIniciConnexio); 
								System.out.println("Intent de Connexió en Tramesa. Espereu resposta...");

								// Paquet de Confirmació de Connexió que rebrem del Servidor3ER
								DatagramPacket paquetConnexio = new DatagramPacket(arrayEstatJoc, arrayEstatJoc.length);

								socket.receive(paquetConnexio); // Rebem array del Servidor3ER

								if (arrayEstatJoc[0] == 0 || arrayEstatJoc[0] == -1) // Confirmem manualment si s'ha establert connexió
								{
									connectat = true;
									System.out.println("Connexió establerta");
								}
								else
								{
									System.out.println("Connexió rebutjada pel servidor");
								}
								break;			

						case 2: // Desconnexió del Client
							
								System.out.println("Finalitzant Ejecució...");
								connectat = false;
								return;
							
						default: // Cas per a opcions NO vàlides
							System.out.println("Opció no vàlida");
							break;
	
					}
				}		
				while(true){

					
					DatagramPacket estatActualTauler = new DatagramPacket(arrayEstatJoc, arrayEstatJoc.length);

					tauler.imprimirTablero();

					// Comprovem estats
					if (arrayEstatJoc[0] == P_TORN) { // Es pot realitzar Jugada
						// Escanejar Jugada
						byte[] infoJugada = new byte[1];
						System.out.println("Fer jugada");
						infoJugada[0] = (byte)triar_opcio.nextInt();
						DatagramPacket paquetJugada = new DatagramPacket(infoJugada, infoJugada.length, adrecaServidor, portServidor);
						socket.send(paquetJugada);	
						System.out.println("Esperant confirmació de jugada");
						socket.receive(estatActualTauler);
					}
					else if (arrayEstatJoc[0] == P_GUANYAT) { // S'ha guanyat
						System.out.println("Enhorabona! Has Guanyat");
						break;
					}
					else if (arrayEstatJoc[0] == P_PERDUT) { // S'ha perdut
						System.out.println("Llastima! Has Perdut");
						break;
					}
					else if (arrayEstatJoc[0] == P_CONNECT) { // Connexió establerta, però és el torn de l'altre jugador
						// Rebem per part de Servidor3ER l'estat actual del joc
						System.out.println("Esperant a l'altre jugador");
						socket.receive(estatActualTauler);
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
		int opcio = -1;

		System.out.println("\n\n1- Iniciar Connexió Amb El Servidor");
		System.out.println("2- Finalitzar Estat De Connexió i/o Ejecució Del Programa\n");

		System.out.println("Seleccioneu una opció");

		opcio = triar_opcio.nextInt();
		if (opcio < 1 || opcio > 2) {
			opcio = -1;
		}
		return opcio;
	}

	
	
		
}

