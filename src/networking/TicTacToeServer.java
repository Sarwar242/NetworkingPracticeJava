package networking;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.*;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;
public class TicTacToeServer extends JFrame{
	private static final long serialVersionUID = 1L;
	private String[] board = new String[ 9 ];
	private JTextArea outputArea;
	private Player[] players;
	private ServerSocket server;
	private int currentPlayer;
	private final static int PLAYER_X = 0;
	private final static int PLAYER_O = 1;
	private final static String[] MARKS = { "X", "O" };
	private ExecutorService runGame;
	private Lock gameLock;
	private Condition otherPlayerConnected;
	private Condition otherPlayerTurn;
	
	public TicTacToeServer() {
		super("Tic Tac Toe Server");
		runGame = Executors.newFixedThreadPool( 2 );
		gameLock = new ReentrantLock();
		otherPlayerConnected = gameLock.newCondition();
		otherPlayerTurn = gameLock.newCondition();
		for ( int i = 0; i < 9; i++ )
			board[ i ] = new String( "" );
		players = new Player[ 2 ];
		currentPlayer = PLAYER_X;
		try {
			server = new ServerSocket( 12345, 2 );
		}catch(IOException ioException) {
			ioException.printStackTrace();
			System.exit( 1 );
		}
		outputArea = new JTextArea();
		add( outputArea, BorderLayout.CENTER );
		outputArea.setText( "Server awaiting connections\n" );
		setSize( 400, 400 ); 
		setVisible( true );
		setLocationRelativeTo(null);
	}
	public void execute()
{
for ( int i = 0; i < players.length; i++ )
	 {
 try // wait for connection, create Player, start runnable
	 {
	 players[ i ] = new Player( server.accept(), i );
	 runGame.execute( players[ i ] );
	 }catch ( IOException ioException )
 		{
		  ioException.printStackTrace();
		  System.exit( 1 );
		  } 
	 }
gameLock.lock();
try
{
players[ PLAYER_X ].setSuspended( false ); // resume player X
otherPlayerConnected.signal(); // wake up player X's thread
} // end try
finally
 {
gameLock.unlock();
 } // end finally
 }
	private void displayMessage(final String messageToDisplay) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				outputArea.append( messageToDisplay );
				
			}
		});
		}
	public boolean validateAndMove( int location, int player )
	 {
		while ( player != currentPlayer ){
	gameLock.lock(); // lock game to wait for other player to go
	try
	{
	 otherPlayerTurn.await(); // wait for player's turn
	 } // end try
	catch ( InterruptedException exception )
	 {
	 exception.printStackTrace();
	 } // end catch
	 finally
 {
	 gameLock.unlock(); // unlock game after waiting
	 } // end finally
} // end while
 if ( !isOccupied( location ) )
	 {
	 board[ location ] = MARKS[ currentPlayer ]; // set move on board
	 currentPlayer = ( currentPlayer + 1 ) % 2; // change player
	 players[ currentPlayer ].otherPlayerMoved( location );
	 gameLock.lock(); // lock
	 try {
		 otherPlayerTurn.signal();
	 }
	 finally {
		 gameLock.unlock();
	}
	 return true;
	 }
 else 
	 return false;
 
	 }
	
	public boolean isOccupied( int location ) {
		if ( board[ location ].equals( MARKS[ PLAYER_X ] ) ||
				board [ location ].equals( MARKS[ PLAYER_O ] ) )
				return true; // location is occupied
				else
				return false;
	}
	public boolean isGameOver() {
		return false;
	}
	 
	private class Player implements Runnable{
private Socket connection; // connection to client
private Scanner input; // input from client
 private Formatter output; // output to client
 private int playerNumber; // tracks which player this is
private String mark; // mark for this player
private boolean suspended = true; // whether thread is suspended
		public Player( Socket socket, int number ){
	playerNumber = number; // store this player's number
	 mark = MARKS[ playerNumber ]; // specify player's mark
	 connection = socket; // store socket for client
	try {
		input = new Scanner( connection.getInputStream() );
		output = new Formatter( connection.getOutputStream() );
	}catch ( IOException ioException ) {
				ioException.printStackTrace();
					System.exit( 1 );
			}
		}
		public void otherPlayerMoved( int location ) {
			output.format( "Opponent moved\n" );
			output.format( "%d\n", location ); // send location of move
			output.flush();
		}
		
		public void run(){
			try{
			displayMessage( "Player " + mark + " connected\n" );
			output.format( "%s\n", mark ); // send player's mark
			output.flush();
		// if player X, wait for another player to arrive
		if ( playerNumber == PLAYER_X )
		{
			output.format( "%s\n%s", "Player X connected","Waiting for another player\n" );
			output.flush();
			gameLock.lock(); // lock game to wait for second player
			try{
				while( suspended ) {
					otherPlayerConnected.await(); // wait for player O
		 }
		} catch ( InterruptedException exception ) {
		exception.printStackTrace();
	}
			finally {
				gameLock.unlock(); // unlock game after second player
		 } 
			output.format( "Other player connected. Your move.\n" );
			output.flush();
	} // end if
		else{
	output.format( "Player O connected, please wait\n" );
	output.flush(); 
}
	 while ( !isGameOver() )
		 {
		int location = 0; // initialize move location
	
		if ( input.hasNext() )
			location = input.nextInt();
		
		if(validateAndMove(location, playerNumber)) {
			displayMessage("\nlocation: " + location);
			output.format( "Valid move.\n" ); // notify client
			output.flush();
		}else {
			output.format( "Invalid move, try again\n" );
			output.flush();
			}
		}
	}finally{
		try{
		connection.close(); // close connection to client
		 } // end try
		catch ( IOException ioException ){
		ioException.printStackTrace();
		System.exit( 1 );
			} // end catch
		} // end finally
 } //
	
		public void setSuspended( boolean status ){
			suspended = status; // set value of suspended
		}
		

		} 
}