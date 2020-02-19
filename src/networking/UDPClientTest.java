package networking;

import javax.swing.JFrame;

public class UDPClientTest {

	public static void main(String[] args) {
		ClientUDP application = new ClientUDP(); // create client
		application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		 application.waitForPackets();
	}
}