import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Jugador{
	
	private InetAddress address;
	private int port;

	
	public Jugador(){
		address = InetAddress.getLoopbackAddress();
		int port = 0;
	}

	public InetAddress getAddress(){
		byte[] cpyAddressRaw = address.getAddress();
		InetAddress cpyAddress;
		try {
		    	cpyAddress = InetAddress.getByAddress(cpyAddressRaw);
			return cpyAddress;
		} catch (UnknownHostException e) {
    			// Manejar la excepción
    			e.printStackTrace(); // Puedes cambiar esto según cómo quieras manejar el error
			return null;
		}	
	}

	public int getPort(){
		return port;
	}

	public void setAddress(InetAddress newAddress){
		address = newAddress;
	}

	public void setPort(int pt){
		port = pt;
	}



}
