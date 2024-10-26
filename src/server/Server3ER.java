import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import addicional.*;

public class Server3ER {
	
	public static final int MIDA_PAQUET = 10;

	public static final int P_CONNECT = 0;
	public static final int P_TORN = -1;
	public static final int P_GUANYAT = -2;
	public static final int P_PERDUT = -3;


	public static void main(String[] args) {

		if(args.length == 1) {
			try{	
				//Es crea el socket UDP per escoltar la petició del jugador 1
				DatagramSocket socket = new DatagramSocket(Integer.parseInt(args[0]));
				System.out.println("Servidor del joc operatiu al port" + args[0] + "!");
				
				Jugador jugador1 = new Jugador();
				Jugador jugador2 = new Jugador();


				byte[] infoJoc = new byte[MIDA_PAQUET];
				Tauler tauler = new Tauler();

				byte[] infoEntrada = new byte[1];
				//Paquet per rebre al socket
				DatagramPacket paquetEntrada = new DatagramPacket(infoEntrada, infoEntrada.length);
				
				
				boolean esperaJugadors = true;

				byte[] paquetRebut = new byte[MIDA_PAQUET];
				do{
					System.out.println("Esperant que el jugador 1 es connecti");
					
					//Es rep el paquet del jugador 1
					socket.receive(paquetEntrada);
					//S'obté la informació del paquet rebut
					paquetRebut = paquetEntrada.getData();
					//Es guarda l'adreça i port del jugador 1
					jugador1.setAddress(paquetEntrada.getAddress());	
					jugador1.setPort(paquetEntrada.getPort());	

					//Si el valor del primer byte no es el codi corresponent a la connexió d'un jugador
					//es torna a esperar a que entri un jugador
					printInfoJoc(paquetRebut);
				} while(paquetRebut[0] != P_CONNECT);

				System.out.println("Jugador 1 connectat");

				do{
					System.out.println("Esperant que el jugador 2 es connecti");
					
					//Es rep el paquet del jugador 1
					socket.receive(paquetEntrada);
					//S'obté la informació del paquet rebut
					paquetRebut = paquetEntrada.getData();
					//Es guarda l'adreça i port del jugador 1
					jugador2.setAddress(paquetEntrada.getAddress());	
					jugador2.setPort(paquetEntrada.getPort());	

					//Si el valor del primer byte no es el codi corresponent a la connexió d'un jugador
					//es torna a esperar a que entri un jugador
				} while(paquetRebut[0] != P_CONNECT /*|| (jugador2.getAddress().equals(jugador1.getAddress()))*/);
				
				System.out.println(jugador1.getAddress().getHostAddress());
				System.out.println(jugador2.getAddress().getHostAddress());
				System.out.println("Jugador 2 connectat");
				
				//S'inicialitza el joc amb tot 0
				for(int i = 0; i<infoJoc.length; i++){
					infoJoc[i] = 0;
				}

				tauler.setTablero(infoJoc);
				//Paquets per enviar pel soquet als destinataris
				jugador2.enviaPaquet(infoJoc, socket);
				//Al jugador 1 se li actualitza la informació per a que sapigui que és el seu torn
				

				int estatJoc = 0;

				while(estatJoc == 0){
					tauler.imprimirTablero(tauler);
					System.out.println("Esperant la jugada del jugador 1");
					do{
						
						infoJoc[0] = P_TORN;
						jugador1.enviaPaquet(infoJoc, socket);
						do{
							socket.receive(paquetEntrada);	
							//S'allargará la espera fins que es rebi la resposta del jugador 1
						} while(!(paquetEntrada.getAddress().equals(jugador1.getAddress())));
						infoJoc = paquetEntrada.getData();
						tauler.setTablero(infoJoc);
					}while(!tauler.jugada(1, infoJoc[0]));

					System.out.println("Jugada jugador 1:");
					//S'afegeix la informació del tauler a la informació general
					addTaulerAInfo(tauler, infoJoc);


					//Si la partida encara no ha acabat
					estatJoc = tauler.ganador();
					if(estatJoc == 0)
					{		
						tauler.imprimirTablero(tauler);
						System.out.println("Esperant la jugada del jugador 2");
						do{	
							infoJoc[0] = P_TORN;
							jugador2.enviaPaquet(infoJoc, socket);
							do{
								socket.receive(paquetEntrada);	
								//S'allargará la espera fins que es rebi la resposta del jugador 1
							} while(!(paquetEntrada.getAddress().equals(jugador2.getAddress())));
							infoJoc = paquetEntrada.getData();
							tauler.setTablero(infoJoc);
						}while(!tauler.jugada(2, infoJoc[0]));
					}
				}

				//Quan la partida acaba
				printInfoJoc(infoJoc);
				if(estatJoc == 1) {
					infoJoc[0] = P_GUANYAT;
					jugador1.enviaPaquet(infoJoc, socket);
					infoJoc[0] = P_PERDUT;
					jugador2.enviaPaquet(infoJoc, socket);
				}
				else{
					infoJoc[0] = P_GUANYAT;
					jugador2.enviaPaquet(infoJoc, socket);
					infoJoc[0] = P_PERDUT;
					jugador1.enviaPaquet(infoJoc, socket);
				}

		
			} catch(Exception e){
				e.printStackTrace();
			}	
		}
		else {
			System.out.println("El nombre de paràmetres no es correcte");
		}
	}


	private static void printInfoJoc(byte[] infoJoc){
		System.err.printf("\n");
		for (int i = 0; i<infoJoc.length; i++){
			System.out.printf("%d", infoJoc[i]);		
		}
		System.out.printf("\n");
	}

	private static int estatJoc(byte[] infoJoc){
		return 0;
	}

	private static void addTaulerAInfo(Tauler tauler, byte[] infoJoc){
		byte[] taulerRaw = tauler.rawTablero();
		for(int i = 1; i<infoJoc.length; i++){
			infoJoc[i] = taulerRaw[i-1];
		}
	}


}
