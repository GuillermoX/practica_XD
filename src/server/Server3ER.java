package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import addicional.Tauler;

public class Server3ER {
	
	public static final int MIDA_PAQUET = 10;

	public static final int P_CONNECT = 0;
	public static final int P_TORN = -1;
	public static final int P_GUANYAT = -2;
	public static final int P_PERDUT = -3;
	public static final int P_EMPAT = -4;

	public static final int ESTAT_JOC_EN_MARXA = 0;
	public static final int ESTAT_GUANYADOR_1 = 1;
	public static final int ESTAT_GUANYADOR_2 = 2;
	public static final int ESTAT_EMPAT = 3;


	public static void main(String[] args) {

		if(args.length == 1) {
			try{	
				//Es crea el socket UDP per escoltar la petició del jugador 1
				
				DatagramSocket socket = new DatagramSocket(Integer.parseInt(args[0]));
				
				while(true){
					System.out.println("\nServidor del joc operatiu al port " + args[0] + "!\n");
				
					Jugador jugador1 = new Jugador();
					Jugador jugador2 = new Jugador();

					//S'inicialitza un tauler tot a 0
					//Array de bytes que s'enviará al client
					byte[] infoSortida = new byte[MIDA_PAQUET];
					//Array de bytes que es rep del client
					byte[] infoEntrada = new byte[1];

					//Paquet que es rebrá del client
					DatagramPacket paquetEntrada = new DatagramPacket(infoEntrada, infoEntrada.length);
				
				
					boolean esperaJugadors = true;
					
					Tauler tauler = new Tauler();
	
					do{
						System.out.println("Esperant que el jugador 1 es connecti");

						//Es rep el paquet del jugador 1
						socket.receive(paquetEntrada);
						//S'obté la informació del paquet rebut
						infoEntrada = paquetEntrada.getData();
						//Es guarda l'adreça i port del jugador 1
						jugador1.setAddress(paquetEntrada.getAddress());	
						jugador1.setPort(paquetEntrada.getPort());	

						//Si el valor del primer byte no es el codi corresponent a la connexió d'un jugador
						//es torna a esperar a que entri un jugador
					} while(infoEntrada[0] != P_CONNECT);

					System.out.println("Jugador 1 connectat");

					do{
						System.out.println("Esperant que el jugador 2 es connecti");

						//Es rep el paquet del jugador 1
						socket.receive(paquetEntrada);
						//S'obté la informació del paquet rebut
						infoEntrada = paquetEntrada.getData();
						//Es guarda l'adreça i port del jugador 1
						jugador2.setAddress(paquetEntrada.getAddress());	
						jugador2.setPort(paquetEntrada.getPort());	

						//Si el valor del primer byte no es el codi corresponent a la connexió d'un jugador
						//es torna a esperar a que entri un jugador
					} while(infoEntrada[0] != P_CONNECT || ((jugador2.getAddress().equals(jugador1.getAddress())) && 
														     (jugador2.getPort() == jugador1.getPort())));

					System.out.println(jugador1.getAddress().getHostAddress());
					System.out.println(jugador2.getAddress().getHostAddress());
					System.out.println("Jugador 2 connectat");

					//S'inicialitza la informació de sortida amb tot 0
					for(int i = 0; i<infoSortida.length; i++){
						infoSortida[i] = 0;
					}

					//Al jugador 2 se li indica que ha d'esperar a que el jugador 1 faci la jugada primer
					jugador2.enviaPaquet(infoSortida, socket);


					int estatJoc = 0;

					while(estatJoc == ESTAT_JOC_EN_MARXA){
						tauler.imprimirTablero();
						System.out.println("Esperant la jugada del jugador 1");
						//Es prepara la informació de sortida pel jugador 1
						infoSortida[0] = P_TORN;
						addTaulerAInfo(tauler, infoSortida);
						do{

							jugador1.enviaPaquet(infoSortida, socket);
							infoEntrada[0] = jugador1.rebrePaquet(socket);	
						}while(!tauler.jugada(1, infoEntrada[0]));
						infoSortida[0] = 0;
						addTaulerAInfo(tauler, infoSortida);
						jugador1.enviaPaquet(infoSortida, socket);
						System.out.println("Jugada jugador 1:");

						//Si la partida encara no ha acabat
						estatJoc = tauler.ganador();
						if(estatJoc == ESTAT_JOC_EN_MARXA)
						{		
							tauler.imprimirTablero();
							System.out.println("Esperant la jugada del jugador 2");
							//Es prepara la informació de sortida pel jugador 2
							infoSortida[0] = P_TORN;
							addTaulerAInfo(tauler, infoSortida);
							do{	
								jugador2.enviaPaquet(infoSortida, socket);
								infoEntrada[0] = jugador2.rebrePaquet(socket);
							}while(!tauler.jugada(2, infoEntrada[0]));
							
							estatJoc = tauler.ganador();
							if(estatJoc == ESTAT_JOC_EN_MARXA)
							{
								infoSortida[0] = 0;
								addTaulerAInfo(tauler, infoSortida);
								jugador2.enviaPaquet(infoSortida, socket);
							}
						}
						System.out.println("Jugada jugador 2:");
						
					}

					//Quan la partida acaba
					tauler.imprimirTablero();
					addTaulerAInfo(tauler, infoSortida);
					if(estatJoc == ESTAT_GUANYADOR_1) {
						infoSortida[0] = P_GUANYAT;
						jugador1.enviaPaquet(infoSortida, socket);
						infoSortida[0] = P_PERDUT;
						jugador2.enviaPaquet(infoSortida, socket);
					}
					else if (estatJoc == ESTAT_GUANYADOR_2){
						infoSortida[0] = P_GUANYAT;
						jugador2.enviaPaquet(infoSortida, socket);
						infoSortida[0] = P_PERDUT;
						jugador1.enviaPaquet(infoSortida, socket);
					}
					else if(estatJoc == ESTAT_EMPAT){
						infoSortida[0] = P_EMPAT;
						jugador1.enviaPaquet(infoSortida, socket);
						jugador2.enviaPaquet(infoSortida, socket);
					}


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
