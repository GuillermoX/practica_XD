import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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


				byte[] prova = {1,0,0,0,0,0,0,0,0,1};
				DatagramPacket paquetEnviament = new DatagramPacket(prova, prova.length, adrecaServidor, portServidor);
				socket.send(paquetEnviament);
				System.out.println("Enviat");
				socket.close();


			} catch (Exception e){
				e.printStackTrace();
			}

		}
		else{
			System.out.println("Introdueix l'adreça i el port del servidor");
		}

	}


}
