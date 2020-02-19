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

public class Client extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField enterField; // enters information from user
	private JTextArea displayArea; // display information to user
	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	private String message = ""; // message from server
	private String chatServer;
	private Socket client;
	public Client( String host ) {
		super( "Client" );
		chatServer = host;
		enterField = new JTextField();
		enterField.setEditable( false );
		enterField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				sendData( event.getActionCommand() );
				enterField.setText( "" );
				
			}
		});
		add( enterField, BorderLayout.NORTH );
		
		displayArea = new JTextArea(); // create displayArea
		add( new JScrollPane( displayArea ), BorderLayout.CENTER );
		setLocationRelativeTo(null);
		setSize( 500, 350 ); // set size of window
		setVisible( true );
	}
	public void runClient()
	 {
	try {
	connectToServer(); // create a Socket to make connection
	getStreams(); // get the input and output streams
	processConnection(); // process connection
	}
	catch ( EOFException eofException )
	{
	displayMessage( "\nClient terminated connection" );
	}
	catch ( IOException ioException )
	{
		ioException.printStackTrace();
	} // end catch
	finally
	{
	    closeConnection(); 
	} 
 }
	private void closeConnection() {
		// TODO Auto-generated method stub
		displayMessage( "\nClosing connection" );
		setTextFieldEditable( false );
		try {
			output.close(); // close output stream
			input.close(); // close input stream
			client.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
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
		setTextFieldEditable( true );
		do {
			try {
				message = ( String ) input.readObject();
				displayMessage( "\n" + message );
			}catch ( ClassNotFoundException classNotFoundException)
			{
				displayMessage( "\nUnknown object type received" );
			}
		
	}while ( !message.equals( "SERVER>>> TERMINATE" ) );
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
	private void getStreams() throws IOException{
		// TODO Auto-generated method stub
		output = new ObjectOutputStream( client.getOutputStream() );
		output.flush();
		input = new ObjectInputStream( client.getInputStream() );
		displayMessage( "\nGot I/O streams\n" );
	}
	private void connectToServer() throws IOException{
		 
		displayMessage( "Attempting connection\n" );
		client = new Socket( InetAddress.getByName( chatServer ), 12345 );
		 displayMessage( "Connected to: " +client.getInetAddress().getHostName() );
		 }
	

	protected void sendData(String message) {
		// TODO Auto-generated method stub
		try{
			output.writeObject( "Client>>> " + message );
			output.flush();
			displayMessage( "\nClient>>> " + message );		
	}catch (IOException ioException) {
		// TODO: handle exception
		displayArea.append( "\nError writing object" );	
		}
	}
}

