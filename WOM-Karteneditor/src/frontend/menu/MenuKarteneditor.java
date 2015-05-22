package frontend.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import daten.*;
import frontend.Frontend;

public class MenuKarteneditor extends MenuRechts{
	private static final long serialVersionUID=1L;
	private JRadioButton[] aktionsButton=new JRadioButton[4];
	private JComboBox<String> wahlFeldArt=new JComboBox<String>();
	private JSpinner wahlSpielernummer=new JSpinner(new SpinnerNumberModel(1,1,99,1));
	private JComboBox<Integer> wahlZoom=new JComboBox<Integer>();
	private AktionKarteneditor aktion;
	private String kartenArt=null;
	
	public enum AktionKarteneditor {
		FeldArtSetzen,RessourceSetzen,RessourceLoeschen,SpielerstartSetzen;
	}
	
	public MenuKarteneditor(Frontend frontend){
		super(frontend);
		kartenArt=((D_Karte)Xml.toD(frontend.getBackend().getKartenDaten())).getString("kartenArt");
		setLayout(new GridLayout(11,1));
		setPreferredSize(new Dimension(200,200));
		ButtonGroup aktionenGruppe=new ButtonGroup();
		// Aktionen
		addHeader("AKTION");
		aktionsButton[0]=addAktion(aktionenGruppe,"Feldart setzen");		
		aktionsButton[1]=addAktion(aktionenGruppe,"Resource setzen");		
		aktionsButton[2]=addAktion(aktionenGruppe,"Resource loeschen");		
		aktionsButton[3]=addAktion(aktionenGruppe,"Spielerstart setzen");		
		for (int i=0;i<aktionsButton.length;i++)
			aktionsButton[i].setEnabled(true);
		aktionsButton[0].doClick();
		// Feldart
		addHeader("FELDART");
		wahlFeldArt.setFont(new Font("Arial",Font.BOLD,14));
		D_FeldArt feldArten=(D_FeldArt)Xml.toD(frontend.getBackend().getErlaubteFeldArten(kartenArt));
		for(String feldArt:feldArten.getListe()){
  		wahlFeldArt.addItem(feldArt);
  	}
		add(wahlFeldArt);
		wahlFeldArt.addActionListener(this);
		wahlFeldArt.setEnabled(true);
		// Spielernummer
		addHeader("SPIELERNUMMER");
		wahlSpielernummer.setFont(new Font("Arial",Font.BOLD,14));
		wahlSpielernummer.setEditor(new JSpinner.DefaultEditor(wahlSpielernummer));
		add(wahlSpielernummer);
		wahlSpielernummer.setEnabled(false);
		// Zoom
		addHeader("ZOOM in %");
		wahlZoom.setFont(new Font("Arial",Font.BOLD,14));
		for(int i=20;i<=200;i+=20) wahlZoom.addItem(i);
		wahlZoom.setSelectedItem(frontend.getZoomfaktor());
		wahlZoom.setEnabled(true);
		wahlZoom.addActionListener(this);
		add(wahlZoom);
	}
	
	public void aktivieren(){
		try{
			wahlFeldArt.removeAllItems();			
		}
		catch (Exception e){}
		D_FeldArt feldArten=(D_FeldArt)Xml.toD(frontend.getBackend().getErlaubteFeldArten(kartenArt));
		for(String feldArt:feldArten.getListe()){
  		wahlFeldArt.addItem(feldArt);
  	}
		for (int i=0;i<aktionsButton.length;i++)
			aktionsButton[i].setEnabled(true);
		wahlZoom.setEnabled(true);
		aktionsButton[0].doClick();
		wahlZoom.setSelectedItem(100);
	}
	
	public AktionKarteneditor getAktion(){
		return aktion;
	}
		
	public String getFeldart() {
		return ""+wahlFeldArt.getSelectedItem();
	}

	public int getSpielernummer(){
		return (int)wahlSpielernummer.getValue();
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o=ev.getSource();
		if(o==aktionsButton[0])
			addAktionsReaktion(true,false,
					"Jetzt koennen Sie die gewaehlte Feldart auf der Karte verbreiten.",AktionKarteneditor.FeldArtSetzen);
		if(o==aktionsButton[1])
			addAktionsReaktion(false,false,
					"Jetzt koennen Sie je eine passende Resource pro Feld verbreiten.",AktionKarteneditor.RessourceSetzen);
		if (o==aktionsButton[2])
			addAktionsReaktion(false,false,
					"Jetzt koennen Sie Resourcen von der Karte entfernen.",AktionKarteneditor.RessourceLoeschen);
		if (o==aktionsButton[3])
			addAktionsReaktion(false,true,
					"Jetzt koennen Sie Startposition der gewaehlten Spielernummer auf der Karte definieren.",AktionKarteneditor.SpielerstartSetzen);
		if (o==wahlFeldArt){
			String feldArt=(String)wahlFeldArt.getSelectedItem();
			D_RessourcenArt feldRessourcenArten=(D_RessourcenArt)Xml.toD(frontend.getBackend().getErlaubteRessourcenArten(feldArt));
			frontend.log("Gewaehlte Feldart: "+feldArt+" "+feldRessourcenArten.getListe());			
		}
		if (o==wahlSpielernummer){
			frontend.log("Gewaehlte Spielernummer: "+wahlSpielernummer.getValue());			
		}
		if (o==wahlZoom){
			int zoom=(Integer)wahlZoom.getSelectedItem();
			frontend.log("Zoomfaktor: "+zoom+"%");
			frontend.setZoomfaktor(zoom);
			frontend.zeichneFelder(null);
		}
	}

	private void addHeader(String text){
		JLabel x=new JLabel(text);
		x.setFont(new Font("Aral", Font.BOLD, 18));
		add(x);		
	}
	
	private JRadioButton addAktion(ButtonGroup gruppe,String text){
		JRadioButton b=new JRadioButton(text);
		b.addActionListener(this);
		gruppe.add(b); 
		add(b);
		return b;
	}
	
	private void addAktionsReaktion(
			boolean wahlFeldArtEnabled,boolean wahlSpielernummerEnabled,
			String logText,AktionKarteneditor aktionArt){
		wahlFeldArt.setEnabled(wahlFeldArtEnabled);
		wahlSpielernummer.setEnabled(wahlSpielernummerEnabled);
		frontend.log(logText);
		aktion=aktionArt;
	}
}
