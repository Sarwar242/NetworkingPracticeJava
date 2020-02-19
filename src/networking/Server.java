package networking;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import javax.swing.*;

public class Server extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField enterField; // inputs message from user
	private JTextArea displayArea; 
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection; 
	private int counter = 1;
	
	public Server() {
		super("Server");
		enterField = new JTextField();
		enterField.setEditable( false );
		enterField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				sendData(event.getActionCommand());
				enterField.setText("");
				
			}
		});
		add( enterField, BorderLayout.NORTH );
		displayArea=new JTextArea();
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );
		setSize(500,350);
		//setLocationRelativeTo(null);
		setVisible( true );
		
	}
	public void runServer()
	{
		try {
			server = new ServerSocket( 12345, 100 );
			while(true) {
				try {
					waitForConnection(); // wait for a connection
				    getStreams(); // get input & output streams
					processConnection();
				}
				catch ( EOFException eofException ) {
					displayMessage( "\nServer terminated connection" );
				}
				finally
				{
				closeConnection(); // close connection
				++counter;
				}
			}
		}
		catch (IOException ioException) {
			// TODO: handle exception
			ioException.printStackTrace();
		}
	}
	private void closeConnection() {
		// TODO Auto-generated method stub
		displayMessage( "\nTerminating connection\n" );
		setTextFieldEditable( false );
		try {
			output.close(); // close output stream
			input.close(); // close input stream
			connection.close();
		}catch (IOException ioException) {
			// TODO: handle exception
			displayArea.append( "\nError writing object" );
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
	private void processConnection() throws IOException {
		// TODO Auto-generated method stub
		String message = "Connection successful";
		sendData( message );
		setTextFieldEditable( true );
		
		do {
			try {
				message = ( String ) input.readObject();
				displayMessage( "\n" + message );
			}
			catch ( ClassNotFoundException classNotFoundException) {
				displayMessage( "\nUnknown object type received" );
			}
		}
		while(!message.equals( "CLIENT>>> TERMINATE" ));		
	}
	private void setTextFieldEditable(final boolean editable) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				enterField.setEditable( editable );
			}
		});	
	}
	private void getStreams() throws IOException {
		// TODO Auto-generated method stub
		output = new ObjectOutputStream( connection.getOutputStream() );
		output.flush();
		input = new ObjectInputStream( connection.getInputStream() );
		displayMessage( "\nGot I/O streams\n" );
		
	}
	private void waitForConnection() throws IOException{
		// TODO Auto-generated method stub
		displayMessage( "Waiting for connection\n" );
		connection = server.accept();
		displayMessage( "Connection " + counter + " received from: " +connection.getInetAddress().getHostName());
		
	}
	protected void sendData(String message) {
		// TODO Auto-generated method stub
		try{
			output.writeObject( "SERVER>>> " + message );
			output.flush();
			displayMessage( "\nSERVER>>> " + message );		
	}catch (IOException ioException) {
		// TODO: handle exception
		displayArea.append( "\nError writing object" );	
		}
	}
}
