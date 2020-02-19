package networking;

import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client application;
		if ( args.length == 0 )
			application = new Client( "127.0.0.1" );
		else
			application = new Client( args[ 0 ] );
			application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			application.runClient();
	}

}
