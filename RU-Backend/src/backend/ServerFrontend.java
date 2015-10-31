package backend;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class ServerFrontend extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JButton[] button=new JButton[8];
	private JTextField jIP=new JTextField();
	private JTextArea jLog=new JTextArea();
	private JScrollPane jTextScroller;
	private HttpServer server;

	public ServerFrontend() throws SecurityException, IOException{
		setTitle("RU Server, Version 0.80 REST");
		setLayout(new BorderLayout());
		JPanel jNorth=new JPanel();
		JPanel jSouth=new JPanel();
		jLog.setLineWrap(true);
		jTextScroller=new JScrollPane(jLog,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(jTextScroller,BorderLayout.CENTER);
		
		jNorth.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=1; gbc.weightx=0.1;
		jNorth.add(new JLabel("Server auf IP:"),gbc);
		gbc.gridx=1; gbc.gridy=0; gbc.gridwidth=3; gbc.weightx=0.3;
		jNorth.add(jIP,gbc);

		button[0]=new JButton("Host-IPs auslesen");
		button[1]=new JButton("");
		button[2]=new JButton("Log Speichern");
		button[3]=new JButton("Log Loeschen");
		button[4]=new JButton("");
		button[5]=new JButton("Server Start");
		button[6]=new JButton("Server Restart");
		button[7]=new JButton("Server Stop");
		jSouth.setLayout(new GridLayout(2,4));
		for(int i=0;i<button.length;i++){
			button[i].addActionListener(this);
			jSouth.add(button[i]);
		}
		
		add(jNorth,BorderLayout.NORTH);	
		add(jSouth,BorderLayout.SOUTH);
		setSize(1200,400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initLogging();
		button[0].doClick();
		button[5].doClick();
	}
	
	private void initLogging() throws SecurityException,IOException{
    PrintStream ausgabeStrom=new PrintStream(new LogOutputStream()); 
    System.setOut(ausgabeStrom);
    System.setErr(ausgabeStrom);
	}
	
	private class LogOutputStream extends OutputStream{
		@Override
		public void write(int b) throws IOException {
			jLog.append(String.valueOf((char)b));
			jLog.setCaretPosition(jLog.getDocument().getLength());
		}
	}
	
	@Override
	public void dispose(){
		button[7].doClick();
		System.exit(0);
	}

	public void loeschenLog(){
		jLog.setText(""); 
	}
	
	public void speichernLog(){
		PrintWriter pw=null; 
		try { 
			pw=new PrintWriter(new BufferedWriter(new FileWriter("wom-server-log.txt"))); 
			pw.println(jLog.getText()); 
		} 
		catch (Exception e) { 
			System.out.println("FEHLER: "+e.getMessage());
		} 
		finally { 
			if (pw!=null){ 
				pw.flush(); 
				pw.close(); 
			} 
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object geklicked=ev.getSource();
		if (button[0]==geklicked){
			// Host-IPs auslesen
			try {
				Enumeration<?> ifaces=NetworkInterface.getNetworkInterfaces();
				System.out.println("verfuegbare IP-Adressen des Host-PCs:");
		    while (ifaces.hasMoreElements()) {
		      NetworkInterface nic=(NetworkInterface)ifaces.nextElement();
		      Enumeration<?> addrs=nic.getInetAddresses();
		      while (addrs.hasMoreElements()) {
		        InetAddress ia = (InetAddress)addrs.nextElement();
		        String ip=ia.getHostAddress();
		        if ((!ip.contains(":"))&&(!ip.equals("127.0.0.1"))){
		        	System.out.println("  "+nic.getName()+": "+ip);
		        	jIP.setText(ip);
		        }
		      }
		    }
		    if (jIP.getText().length()==0) jIP.setText("127.0.0.1");
      	System.out.println("  127.0.0.1");
      	System.out.println("");
			} catch (Exception e) {
				System.out.println("FEHLER: "+e.getMessage());
			}
		}
		if (button[2]==geklicked){
			// Log speichern
			speichernLog();
		}
		if (button[3]==geklicked){
			// Log loeschen
			loeschenLog();
		}
		if (button[5]==geklicked){
			// Server Start
			try {
				URI uri=UriBuilder.fromUri("http://"+jIP.getText()+"/").port(8000).build();
		    System.out.println("Starte Server auf "+uri+"... ");	
		    final ResourceConfig rc=new ResourceConfig().packages("backend"); 
				server=GrizzlyHttpServerFactory.createHttpServer(uri,rc);
				System.out.println("OK: Server laueft.");
				System.out.println("Lade Startzustand ueber die Initialisierungsfunktion...");
				Initialisierung.ausfuehren();
				System.out.println("OK");
			} catch (Exception e) {
				System.out.println("FEHLER: "+e.getMessage());
			}
		}
		if (button[6]==geklicked){
			button[7].doClick();
			button[5].doClick();
		}
		if (button[7]==geklicked){
			// Server Stop
			System.out.println("Stoppe Server... ");
			if (server!=null){
				server.shutdownNow();
				server=null;
			}
			System.out.println("Server gestoppt.");
		}
	}
}
