package networking;

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SiteSelector extends JApplet {
	private static final long serialVersionUID = 1L;
	private HashMap< Object , URL > sites;
	private ArrayList<String>siteNames;
	@SuppressWarnings("rawtypes")
	private JList siteChooser;
	
	    
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		public void init(){
	    	
	    sites = new HashMap< Object, URL >(); // create HashMap
	    siteNames = new ArrayList< String >(); // create ArrayList
	    
	    // obtain parameters from HTML document
	    getSitesFromHTMLParameters();
	    
	     // create GUI components and lay out interface
	     add( new JLabel( "Choose a site to browse" ), BorderLayout.NORTH );
	    
	     siteChooser = new JList( siteNames.toArray() ); 
	     siteChooser.addListSelectionListener(new ListSelectionListener() {
	    	  // go to site user selected
	    	  public void valueChanged( ListSelectionEvent event )
	    	  {
	    	 // get selected site name
	    	  Object object = siteChooser.getSelectedValue();
	    	 
	    	 
	    	 
	    	URL  newDocument = sites.get(object);
	    	
	    	AppletContext browser = getAppletContext();
	    	browser.showDocument( newDocument );
	    	
	    	  }
	    	 } );
	 
	     add(new JScrollPane(siteChooser), BorderLayout.CENTER);
	     setSize( 400, 300 );
			setVisible(true);
	     }
	     
	    private void getSitesFromHTMLParameters()
	    {
	    	String title ;
	    	String location;
	    	URL url;
	    	int counter = 0;
	    	
	    	title = getParameter("title"+ counter);
	    	while(title != null)
	    	{
	    		location = getParameter("location" +counter);
	    		try//place title /URL in Hashmap and title in ArrayList
	    		{
	    			url = new URL(location );
	    			sites.put(title, url);
	    			siteNames.add(title);
	    		
	    			
	    		}
	    		catch(MalformedURLException urlException)
	    		{
	    			urlException.printStackTrace();
	    		}
	    		counter++;
	    		title = getParameter("title"+counter);
	    	}
	    }
}
	