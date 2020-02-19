package networking;

import javax.swing.JFrame;

public class UDPServerTest {
public static void main(String[] args) {
	ServerUDP application = new ServerUDP();
	application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	application.waitForPackets();
}
}
