package networking;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.*;
import javax.swing.*;
public class ServerUDP extends JFrame{
	private static final long serialVersionUID = 1L;
	private JTextArea displayArea;
	private DatagramSocket socket;
	
	public ServerUDP() {
		super("Server");
		displayArea = new JTextArea();
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );
		setSize(500,350);
		//setLocationRelativeTo(null);
		setVisible( true );
		try {
			socket = new DatagramSocket( 5000 );
		}catch (SocketException socketException) {
			socketException.printStackTrace();
			System.exit(1);
		}
		
	}
	public void waitForPackets() {
		while(true) {
			try {
				byte[] data = new byte[ 100 ]; // set up packet
				DatagramPacket receivePacket =
				new DatagramPacket( data, data.length );
				socket.receive( receivePacket );
				displayMessage( "\nPacket received:" + "\nFrom host: "+receivePacket.getAddress()+"\nHost port: "
				+receivePacket.getPort()+"\nLength: "
				+receivePacket.getLength()+"\nContaining:\n\t" +
				new String(receivePacket.getData(), 0 ,receivePacket.getLength()));
				sendPacketToClient( receivePacket );
			}catch (IOException ioException) {
				displayMessage( ioException + "\n" );
				ioException.printStackTrace();
			}
		}
	}
	
	private void sendPacketToClient(DatagramPacket receivePacket) throws IOException{
		displayMessage( "\n\nEcho data to client..." );
		DatagramPacket sendPacket = new DatagramPacket(
				receivePacket.getData(), receivePacket.getLength(),
				receivePacket.getAddress(), receivePacket.getPort() );
				socket.send( sendPacket ); 
				displayMessage( "Packet sent\n" );
	}
		
	private void displayMessage(final String messageToDisplay) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				displayArea.append( messageToDisplay );
				
			}
		});}
		
}
