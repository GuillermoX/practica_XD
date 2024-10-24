import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server3ER {
	
	public static final int MIDA_PAQUET = 10;

	public static final int P_CONNECT = 1;

	public static void main(String[] args) {

		if(args.length == 1) {
			try{	
				//Es crea el socket UDP per escoltar la petició del jugador 1
				DatagramSocket socket = new DatagramSocket(Integer.parseInt(args[0]));
				System.out.println("Servidor del joc operatiu al port" + args[0] + "!");
				
				Jugador jugador1 = new Jugador();
				Jugador jugador2 = new Jugador();


				byte[] infoJoc = new byte[MIDA_PAQUET];
				DatagramPacket paquetInfoJoc = new DatagramPacket(infoJoc, infoJoc.length);
				
				boolean esperaJugadors = true;

				while(esperaJugadors){
					
					byte[] paquetRebut = new byte[MIDA_PAQUET];
					do{
						System.out.println("Esperant que el jugador 1 es connecti");
						
						//Es rep el paquet del jugador 1
						socket.receive(paquetInfoJoc);
						//S'obté la informació del paquet rebut
						paquetRebut = paquetInfoJoc.getData();
						//Es guarda l'adreça i port del jugador 1
						jugador1.setAddress(paquetInfoJoc.getAddress());	
						jugador1.setPort(paquetInfoJoc.getPort());	

						//Si el valor del primer byte no es el codi corresponent a la connexió d'un jugador
						//es torna a esperar a que entri un jugador
					} while(paquetRebut[0] != P_CONNECT);

					System.out.println("Jugador 1 connectat");

					do{
						System.out.println("Esperant que el jugador 2 es connecti");
						
						//Es rep el paquet del jugador 1
						socket.receive(paquetInfoJoc);
						//S'obté la informació del paquet rebut
						paquetRebut = paquetInfoJoc.getData();
						//Es guarda l'adreça i port del jugador 1
						jugador2.setAddress(paquetInfoJoc.getAddress());	
						jugador2.setPort(paquetInfoJoc.getPort());	

						//Si el valor del primer byte no es el codi corresponent a la connexió d'un jugador
						//es torna a esperar a que entri un jugador
					} while(paquetRebut[0] != P_CONNECT || (jugador2.getAddress().equals(jugador1.getAddress())));
					
					System.out.println(jugador1.getAddress().getHostAddress());
					System.out.println(jugador2.getAddress().getHostAddress());
					System.out.println("Jugador 2 connectat");
				}
		
			} catch(Exception e){
				e.printStackTrace();
			}	
		}
		else {
			System.out.println("El nombre de paràmetres no es correcte");
		}
	}


}
