package frontend.menu;

import frontend.Frontend;
import frontend.Karte;
import frontend.KarteEventHandler;
import interfaces.iBackendKarteneditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import daten.D;
import daten.D_Fehler;
import daten.D_FeldArt;
import daten.D_Karte;
import daten.D_RessourcenArt;
import daten.Xml;

public class MenuRechts extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private iBackendKarteneditor backendKarteneditor;
	private JRadioButton[] aktionsButton=new JRadioButton[4];
	private JComboBox<String> wahlFeldArt=new JComboBox<String>();
	private JSpinner wahlSpielernummer=new JSpinner(new SpinnerNumberModel(1,1,99,1));
	private JComboBox<Integer> wahlZoom=new JComboBox<Integer>();
	private JButton[] spielButtons=new JButton[8];
	private String kartenArt=null;
	private AktionMenuRechts aktion;

	public enum AktionMenuRechts {
		FeldArtSetzen,RessourceSetzen,RessourceLoeschen,SpielerstartSetzen;
	}
	
	public MenuRechts(Frontend frontend){
		this.frontend=frontend;
		this.backendKarteneditor=frontend.getBackendKarteneditor();
		D datenKarte=Xml.toD(backendKarteneditor.getKartenDaten());
		if (!(datenKarte instanceof D_Fehler)) kartenArt=((D_Karte)datenKarte).getString("kartenArt");
		setLayout(new GridLayout(19,1));
		setPreferredSize(new Dimension(200,200));
		ButtonGroup aktionenGruppe=new ButtonGroup();
		// Aktionen
		addHeader("AKTION");
		aktionsButton[0]=addAktion(aktionenGruppe,"Feldart setzen");		
		aktionsButton[1]=addAktion(aktionenGruppe,"Resource setzen");		
		aktionsButton[2]=addAktion(aktionenGruppe,"Resource loeschen");		
		aktionsButton[3]=addAktion(aktionenGruppe,"Spielerstart setzen");
		if (kartenArt!=null){
			for (int i=0;i<aktionsButton.length;i++) aktionsButton[i].setEnabled(true);
			if (!kartenArt.equals("Planet")) aktionsButton[3].setEnabled(false);
			aktionsButton[0].doClick();			
		}
		else{
			for (int i=0;i<aktionsButton.length;i++) aktionsButton[i].setEnabled(false);			
		}
		// Feldart
		addHeader("FELDART");
		wahlFeldArt.setFont(new Font("Arial",Font.BOLD,11));
		if (kartenArt!=null){
			D datenFelder=Xml.toD(backendKarteneditor.getErlaubteFeldArten(kartenArt));
			D_FeldArt feldArten=(D_FeldArt)datenFelder;
			for(String feldArt:feldArten.getListe()) wahlFeldArt.addItem(feldArt);
			wahlFeldArt.addActionListener(this);
			wahlFeldArt.setEnabled(true);						
		}
		else{
			wahlFeldArt.setEnabled(false);			
		}
		add(wahlFeldArt);
		// Spielernummer
		addHeader("SPIELERNUMMER");
		if ((kartenArt!=null)&&(kartenArt.equals("Planet"))){
			wahlSpielernummer.setFont(new Font("Arial",Font.BOLD,11));
			wahlSpielernummer.setEditor(new JSpinner.DefaultEditor(wahlSpielernummer));
			wahlSpielernummer.setEnabled(true);			
		}
		else{
			wahlSpielernummer.setEnabled(false);			
		}
		add(wahlSpielernummer);
		// Zoom
		addHeader("ZOOM in %");
		if (kartenArt!=null){
			wahlZoom.setFont(new Font("Arial",Font.BOLD,11));
			for(int i=20;i<=200;i+=20) wahlZoom.addItem(i);
			wahlZoom.setSelectedItem(frontend.getZoomfaktor());
			wahlZoom.addActionListener(this);
			wahlZoom.setEnabled(true);
		}
		else{
			wahlZoom.setEnabled(false);			
		}
		add(wahlZoom);
		// Spiel-Buttons
		for(int i=0;i<spielButtons.length;i++){
			spielButtons[i]=new JButton();
			spielButtons[i].addActionListener(this);
			add(spielButtons[i]);
		}
		spielButtons[0].setText("");
		// Wenn eine Kartenart existiert, diese holen
		if (kartenArt!=null){
			Karte karte=frontend.neueKarte(backendKarteneditor.getKarte());
			karte.setEventhandler(new KarteEventHandler(frontend));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o=ev.getSource();
		if(o==aktionsButton[0]) // Feldart setzen
			addAktionsReaktion(true,false,
			"Jetzt koennen Sie die gewaehlte Feldart auf der Karte verbreiten.",AktionMenuRechts.FeldArtSetzen);
		if(o==aktionsButton[1]) // Resource setzen
			addAktionsReaktion(false,false,
			"Jetzt koennen Sie je eine passende Resource pro Feld verbreiten.",AktionMenuRechts.RessourceSetzen);
		if (o==aktionsButton[2]) // Resource entfernen
			addAktionsReaktion(false,false,
			"Jetzt koennen Sie Resourcen von der Karte entfernen.",AktionMenuRechts.RessourceLoeschen);
		if (o==aktionsButton[3]) // Spielerstart setzen
			addAktionsReaktion(false,true,
			"Jetzt koennen Sie Startposition der gewaehlten Spielernummer auf der Karte definieren.",AktionMenuRechts.SpielerstartSetzen);
		if (o==wahlFeldArt){ // Feldart auswaehlen
			String feldArt=(String)wahlFeldArt.getSelectedItem();
			D_RessourcenArt feldRessourcenArten=(D_RessourcenArt)Xml.toD(backendKarteneditor.getErlaubteRessourcenArten(feldArt));
			frontend.log("Gewaehlte Feldart: "+feldArt+" "+feldRessourcenArten.getListe());			
		}
		if (o==wahlSpielernummer){ // Spielerstart auswaehlen
			frontend.log("Gewaehlte Spielernummer: "+wahlSpielernummer.getValue());			
		}
		if (o==wahlZoom){ // Zoom auswaehlen
			int zoom=(Integer)wahlZoom.getSelectedItem();
			frontend.log("Zoomfaktor: "+zoom+"%");
			frontend.setZoomfaktor(zoom);
			frontend.zeichneFelder(null);
		}
		if (o==spielButtons[0]){
			// TODO Funktionalitaet einbauen
		}
	}
	
	public AktionMenuRechts getAktion(){
		return aktion;
	}
		
	public String getFeldart() {
		return ""+wahlFeldArt.getSelectedItem();
	}

	public int getSpielernummer(){
		return (int)wahlSpielernummer.getValue();
	}
	

	private void addHeader(String text){
		JLabel x=new JLabel(text);
		x.setFont(new Font("Aral", Font.BOLD, 14));
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
			String logText,AktionMenuRechts aktionArt){
		wahlFeldArt.setEnabled(wahlFeldArtEnabled);
		wahlSpielernummer.setEnabled(wahlSpielernummerEnabled);
		frontend.log(logText);
		aktion=aktionArt;
	}
}