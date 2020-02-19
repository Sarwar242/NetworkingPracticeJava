package networking;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import javax.swing.*;

public class ClientUDP extends JFrame{
	private static final long serialVersionUID = 2L;
	private JTextField enterField;
	private JTextArea displayArea;
	private DatagramSocket socket;
	
	public ClientUDP() {
		super("Client");
		enterField = new JTextField( "Type message here" );
		enterField.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
					String message = event.getActionCommand();
					displayArea.append( "\nSending packet containing: " +message + "\n" );
					byte[] data = message.getBytes(); // convert to bytes
					DatagramPacket sendPacket = new DatagramPacket( data,
					data.length, InetAddress.getLocalHost(), 5000 );
					socket.send( sendPacket );
					displayArea.append( "Packet sent\n" );
					displayArea.setCaretPosition(displayArea.getText().length() );
         } catch ( IOException ioException ){
						displayMessage( ioException + "\n" );
						ioException.printStackTrace();
						}
				}
		});
		add( enterField, BorderLayout.NORTH );
		displayArea = new JTextArea();
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );
		setSize( 400, 300 );
		setVisible(true);
		setLocationRelativeTo(null);
		try {
			socket = new DatagramSocket(  );
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
			}catch (IOException ioException) {
				displayMessage( ioException + "\n" );
				ioException.printStackTrace();
			}
		}
	}

			private void displayMessage(final String messageToDisplay) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						displayArea.append( messageToDisplay );
						
					}
				});
				}
}
