package networking;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class ReadServerFile extends JFrame {
	private static final long serialVersionUID = 1434291233482231729L;
	private JTextField enterField;
	private JEditorPane contentsArea;
	
	public ReadServerFile() {
		super("Simple Web Browser");
		
		enterField= new JTextField("Enter file URL here ");
		
		enterField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				getThePage( event.getActionCommand() );
				
			}
		});
		add( enterField, BorderLayout.NORTH );
		
		contentsArea = new JEditorPane(); // create contentsArea
		contentsArea.setEditable( false );
		contentsArea.addHyperlinkListener(
		new HyperlinkListener()
		{
		// if user clicked hyperlink, go to specified page

		@Override
		public void hyperlinkUpdate(HyperlinkEvent event) {
			// TODO Auto-generated method stub
			if ( event.getEventType() ==
					HyperlinkEvent.EventType.ACTIVATED )
					getThePage( event.getURL().toString() );
		}
		} // end inner class
		);
		add( new JScrollPane( contentsArea ), BorderLayout.CENTER );
		setSize( 500, 350 );
		setLocationRelativeTo(null);
		setVisible(true);
	}
	private void getThePage(String url) {
		// TODO Auto-generated method stub
		try {
			contentsArea.setPage( url );
			enterField.setText( url );
		}catch ( IOException ioException ) {
			JOptionPane.showMessageDialog(this,"Error retrieving specified URL", "Bad URL",JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
