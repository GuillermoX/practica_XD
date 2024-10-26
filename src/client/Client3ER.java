import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class Client3ER
{
	
	public static final int MIDA_PAQUET = 10;

	public static final int P_CONNECT = 1;

	public static void main(String[] args)
	{
		if(args.length == 2)
		{
			boolean connectat = true;
			while (connectat) 
			{
				switch (menu()) 
				{
					case 1: // Enviar Paquet inicial de connexió
						//Es crea el socket per enviar i rebre els paquets del joc
						DatagramSocket socket = new DatagramSocket();
	
						//Es prepara l'adreça i el port del servidor
						InetAddress adrecaServidor = InetAddress.getByName(args[0]);
						int portServidor = Integer.parseInt(args[1]);

						// Paquet inicialitzador de Connexió entre Client/Servidor
						byte[] paquetIniciConnexio = new byte[1];
						Scanner scanner = new Scanner(System.in);
						do{
							byte[] prova = new byte[10];
							System.out.println("Intro array:");
							String array = scanner.nextLine();
							prova = array.getBytes();	
							fixArray(prova);
							DatagramPacket paquetEnviament = new DatagramPacket(prova, prova.length, adrecaServidor, portServidor);
							socket.send(paquetEnviament);
							System.out.println("Enviat");
						}while(true);
						break;

					case 2:
						connectat = false;
						break;
	
				}	
			}
		}
		else
		{
			System.out.println("Introdueix l'adreça i el port del servidor");
		}
	}
	/**
	 * 
	 * @param infoJoc
	 */
	private static void fixArray(byte[] infoJoc){
		for (int i = 0; i<infoJoc.length; i++){
			infoJoc[i] -= '0';
		}
	}


	/**
	 * * Métode que mostra un menú amb opcions a triar
	 * @return valor enter indicador de l'opció triada
	 */
	private static int menu() {
		int opcio;

		System.out.println("\n\n1- Iniciar Connexió Amb El Servidor");
		System.out.println("2- Finalitzar Estat De Connexió\n");

		System.out.println("Seleccioneu una opció");
		Scanner triar_opcio = new Scanner(System.in);
		do {
			opcio = triar_opcio.nextInt();
			if (opcio < 1 || opcio > 2) {
				opcio = -1;
				System.out.println("Indica una opció vàlida del menú");
			}
		}while(opcio == -1);
		triar_opcio.close();
		return opcio;
	}
}
