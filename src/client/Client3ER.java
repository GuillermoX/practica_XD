import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class Client3ER{
	
	public static final int MIDA_PAQUET = 10;

	public static final int P_CONNECT = 1;

	public static void main(String[] args){
		if(args.length == 2){
			try {
				//Es crea el socket per enviar i rebre els paquets del joc
				DatagramSocket socket = new DatagramSocket();

				//Es prepara l'adreça i el port del servidor
				InetAddress adrecaServidor = InetAddress.getByName(args[0]);
				int portServidor = Integer.parseInt(args[1]);

				Scanner scanner = new Scanner(System.in);
				do{						
					byte[] provaEnviar = new byte[10];
					System.out.println("Intro array:");
					String array = scanner.nextLine();
					provaEnviar = array.getBytes();	
					fixArray(provaEnviar);
					DatagramPacket paquetEnviament = new DatagramPacket(provaEnviar, provaEnviar.length, adrecaServidor, portServidor);
					socket.send(paquetEnviament);
					System.out.println("Enviat");

					
					byte[] provaRebre = new byte[10];
					DatagramPacket paquetRebre = new DatagramPacket(provaRebre, provaRebre.length);
					socket.receive(paquetRebre);
					provaRebre = paquetRebre.getData();
					for(int i = 0; i<provaRebre.length; i++){
						System.out.printf("%d", provaRebre[i]);
					}
					System.out.printf("\n");
				}while(true);

			} catch (Exception e){
				e.printStackTrace();
			}

		}
		else{
			System.out.println("Introdueix l'adreça i el port del servidor");
		}

	}

	private static void fixArray(byte[] infoJoc){
		for (int i = 0; i<infoJoc.length; i++){
			infoJoc[i] -= '0';
		}
	}


}
