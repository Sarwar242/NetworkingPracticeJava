package networking;
import java.awt.*;
 import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
public class TicTacToeClient extends JFrame implements Runnable {

	
	private static final long serialVersionUID = -2491543050368649640L;
	private JTextField idField;
	private JTextArea displayArea; 
	private JPanel boardPanel; 
	private JPanel panel2; // panel to hold board
	private Square[][] board;
	 private Square currentSquare; // current square
	private Socket connection; // connection to server
	 private Scanner input; // input from server
	 private Formatter output; // output to server
	 private String ticTacToeHost; // host name for server
	private String myMark; // this client's mark
	private boolean myTurn; // determines which client's turn it is
	 private final String X_MARK = "X"; // mark for first client
	 private final String O_MARK = "O";
	 
	 public TicTacToeClient( String host ) {
		 ticTacToeHost = host; // set name of server
	 	displayArea = new JTextArea( 4, 30 ); // set up JTextArea
displayArea.setEditable( false );
	add( new JScrollPane( displayArea ), BorderLayout.SOUTH );
	 boardPanel = new JPanel(); // set up panel for squares in board
	boardPanel.setLayout( new GridLayout( 3, 3, 0, 0 ) );
	 board = new Square[ 3 ][ 3 ]; // create board
 for ( int row = 0; row < board.length; row++ )
	 {
		 for ( int column = 0; column < board[ row ].length; column++ )
	  {
	 board[ row ][ column ] = new Square("", row * 3 + column );
	  boardPanel.add( board[ row ][ column ] ); // add square
	 	} // end inner for
	 } // end outer for
	  idField = new JTextField(); // set up textfield
	 idField.setEditable( false );
	  add( idField, BorderLayout.NORTH );
	panel2 = new JPanel(); // set up panel to contain boardPanel
	panel2.add( boardPanel, BorderLayout.CENTER ); // add board panel
	  add( panel2, BorderLayout.CENTER ); // add container panel
	setSize( 400, 330 ); // set size of window
	 setVisible( true ); // show window
	  startClient();
	 }

public void startClient() {
	try {
		connection = new Socket(
				InetAddress.getByName( ticTacToeHost ), 12345 );
				input = new Scanner( connection.getInputStream() );
				output = new Formatter( connection.getOutputStream() );

} catch ( IOException ioException )
	 {
	 ioException.printStackTrace();
	 	} // end catch
// create and start worker thread for this client
	 ExecutorService worker = Executors.newFixedThreadPool( 1 );
	 worker.execute( this ); // execute client
	}
public void run() {
	myMark = input.nextLine();
	SwingUtilities.invokeLater(
			new Runnable()
			 {
			public void run() {
				idField.setText( "You are player \"" + myMark + "\"" );
			 } // end method run
			} ); // end call to SwingUtilities.invokeLater
			 myTurn = ( myMark.equals( X_MARK ) ); // determine if client's turn
			while ( true ){
			 if ( input.hasNextLine() )
					processMessage(input.nextLine() );
			}
			}
			private void processMessage( String message ) {
				if ( message.equals( "Valid move." ) )
					 {
					 displayMessage( "Valid move, please wait.\n" );
					 setMark( currentSquare, myMark ); // set mark in square
					} // end if
					else if ( message.equals( "Invalid move, try again" ) )
					{
						displayMessage( message + "\n" ); // display invalid move
					 myTurn = true; // still this client's turn
					 } // end else if
					 else if ( message.equals( "Opponent moved" ) )
					 {
					 int location = input.nextInt(); // get move location
					 input.nextLine(); // skip newline after int location
					int row = location / 3; // calculate row
					 int column = location % 3; // calculate column
						setMark( board[ row ][ column ],
					 ( myMark.equals( X_MARK ) ? O_MARK : X_MARK ) ); // mark move
					displayMessage( "Opponent moved. Your turn.\n" );
					myTurn = true; // now this client's turn
					}  else
					 displayMessage( message + "\n" );
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
			private void setMark( final Square squareToMark, final String mark ){
			SwingUtilities.invokeLater( new Runnable()
		 {
			public void run()
			{
				squareToMark.setMark( mark );  
				} 
			});
		}
			public void sendClickedSquare( int location )
			{
			if ( myTurn )
			{
				output.format( "%d\n", location ); // send location to server
				output.flush();
				myTurn = false; // not my turn any more
			}
			}
			public void setCurrentSquare( Square square )
			{
		 currentSquare = square; // set current square to argument
			} // end method setCurrentSquare
			
			
			private class Square extends JPanel{
				private static final long serialVersionUID = 1L;
				private String mark; // mark to be drawn in this square
			 private int location; // location of square
			public Square( String squareMark, int squareLocation ) {
				mark = squareMark; // set mark for this square
		 location = squareLocation; // set location of this square
			 addMouseListener(
					new MouseAdapter()
			 	{
			 public void mouseReleased( MouseEvent e ) {
				setCurrentSquare( Square.this ); 
			 sendClickedSquare( getSquareLocation() );
				} // end method mouseReleased
			} ); // end call to addMouseListener
		 } // end Square constructor
			 public Dimension getPreferredSize() {
				return new Dimension( 30, 30 ); // return preferred size
			}
			 public Dimension getMinimumSize() {
				 return getPreferredSize(); // return preferred size
			} // end method getMinimumSize
		 public void setMark( String newMark )
			 {
			 mark = newMark; // set mark of square
			 repaint(); // repaint square
			  } // end method setMark
		public int getSquareLocation()
			{
			 return location; // return location of square
			} // end method getSquareLocation
			  public void paintComponent( Graphics g )
			 {
			 super.paintComponent( g );
			 g.drawRect( 0, 0, 29, 29 ); // draw square
			 g.drawString( mark, 11, 20 ); // draw mark
		
			}// end class TicTacToeClient
		 }
}
