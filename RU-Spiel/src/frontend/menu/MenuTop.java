package frontend.menu;

import frontend.Feld;
import frontend.Frontend;
import frontend.Karte;
import frontend.KarteEventHandler;
import interfaces.iBackendSpiel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import daten.D;
import daten.D_Einheit;
import daten.D_Fehler;
import daten.D_OK;
import daten.D_Position;
import daten.D_Spieler;
import daten.D_Stadt;
import daten.Xml;

public class MenuTop extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static final int spalten=6;
	private static final int zeilen=4;
	private Frontend frontend;
	private iBackendSpiel backendSpiel;
	private JButton[] buttons;
	private JButton[] buttonsNavigation=new JButton[9];

	public MenuTop(Frontend frontend){
		this.frontend=frontend;
		this.backendSpiel=frontend.getBackend();
		this.setLayout(new BorderLayout());
		JPanel center=new JPanel();
		JPanel ost=new JPanel();
		center.setLayout(new GridLayout(zeilen,spalten));
		setPreferredSize(new Dimension(1200,100));
		buttons=new JButton[spalten*zeilen];
		for(int i=0;i<spalten*zeilen;i++){
			buttons[i]=new JButton("");
			buttons[i].addActionListener(this);
			center.add(buttons[i]);
		}
		add(center,BorderLayout.CENTER);
		ost.setLayout(new GridLayout(3,3));
		for(int i=0;i<buttonsNavigation.length;i++){
			buttonsNavigation[i]=new JButton();
			buttonsNavigation[i].setOpaque(true);
			buttonsNavigation[i].setBackground(new Color(255,255,255));
			buttonsNavigation[i].setFocusable(false);
			buttonsNavigation[i].addActionListener(this);
			BufferedImage bild=null;
			do{
				try{
					bild=ImageIO.read(new File("daten//navigation//"+i+".jpg"));
					if (bild!=null) buttonsNavigation[i].setIcon((new ImageIcon(bild)));				
				}
				catch (Exception e){};
			} while (bild==null);
			ost.add(buttonsNavigation[i]);
		}
		add(ost,BorderLayout.EAST);
		
		buttons[0].setText("getKarte");
		buttons[1].setText("Updater AUS");
		buttons[17].setText("Fernangriff");
		buttons[23].setText("Runde Ende");
		holenKarte(1); // Karte mit der ID=1 standardmaessig holen
		buttons[1].doClick(); // Autoupdate aktivieren
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o=ev.getSource();
		if (o instanceof JButton){
			for(int i=0;i<buttonsNavigation.length;i++){
				if (o==buttonsNavigation[i]){ // Bewegungen
					if (frontend.istImFernangriffsmodus()) frontend.setFernangriffsmodus(false);
					bewege(Frontend.BewegungsAktion.fromOrdinal(i));
					break;
				}
			}
			for(int i=0;i<spalten*zeilen;i++){
				if (o==buttons[i]){
					aktion(i);
					break;
				}
			}			
		}
		else if (o instanceof Feld){
			Feld feld=(Feld)o;
			if (frontend.istImFernangriffsmodus()){
				if (!feld.istMarkiert()){
					frontend.setFernangriffsmodus(false);
				}
				else{
					D_Einheit fernangriffsEinheit=frontend.getAngriffsEinheit();
					if (fernangriffsEinheit.getBool("istFernkampfeinheit")){
						D_Einheit einheitZielfeld=feld.getEinheit();
						D_Stadt stadtZielfeld=feld.getStadt();
						if ((einheitZielfeld==null)&&(stadtZielfeld==null))
							throw new RuntimeException("Auf dem Zielfeld ist weder eine Einheit, noch eine Stadt!");
						Karte karte=frontend.getKarte();
						int idKarte=karte.getId();
						int xAngreifer=fernangriffsEinheit.getInt("x");
						int yAngreifer=fernangriffsEinheit.getInt("y");
						int xVerteidiger=feld.getPosX();
						int yVerteidiger=feld.getPosY();
						backendSpiel.fernangriff(frontend.getIdSpieler(),1,
								fernangriffsEinheit.getInt("x"),
								fernangriffsEinheit.getInt("y"),
								feld.getPosX(),
								feld.getPosY());
						karte.updateFeld(xAngreifer,yAngreifer,Xml.toArray(backendSpiel.getFeldDaten(idKarte,xAngreifer,yAngreifer)));
						karte.updateFeld(xVerteidiger,yVerteidiger,Xml.toArray(backendSpiel.getFeldDaten(idKarte,xVerteidiger,yVerteidiger)));
						frontend.setFernangriffsmodus(false);
					}
				}
			}
			else{
				frontend.setFeldGewaehlt(feld);
				ArrayList<D> feldDaten=Xml.toArray(backendSpiel.getFeldDaten(frontend.getKarte().getId(),feld.getPosX(),feld.getPosY()));
				frontend.getKarte().updateFeld(feld.getPosX(),feld.getPosY(),feldDaten);
				frontend.log(""+feld);				
			}
		}
	}

	private void aktion(int i) {
		switch (i){
		case 0:
			holenKarte(0);
			break;
		case 1:
			if (buttons[1].getText().equals("Updater AUS")){
				if (autoUpdate(true)) buttons[1].setText("Updater AN");
			}
			else{
				if (autoUpdate(false)) buttons[1].setText("Updater AUS");				
			}
			break;
		case 17: // Angriffsmodus fuer Fernkampf
			if (!frontend.istImFernangriffsmodus()){
				Feld feld=frontend.getFeldGewaehlt();
				if (!feld.getEinheit().getBool("istFernkampfeinheit")) return;
				ArrayList<D> felder=Xml.toArray(backendSpiel.getAngriffsRadius(frontend.getIdSpieler(),1,feld.getPosX(),feld.getPosY()));
				if (felder.get(0) instanceof D_Fehler){
					System.out.println(felder.get(0).getString("meldung"));
					break;
				}
				frontend.getKarte().markiereFelder(felder);
				frontend.setFernangriffsmodus(true);
			} else{
				frontend.setFernangriffsmodus(false);
			}
			break;
		case 23: // Runde beenden
			beendenRunde(frontend.getIdSpieler());
			break;
		default:
				frontend.log("Button "+i+" wurde geklickt.");
		}
	}

	private void beendenRunde(int idSpieler) {
		String antwort=backendSpiel.beendenRunde(idSpieler);
		frontend.log("Der Spieler mit der ID="+idSpieler+" beendet die Runde...");
		if (Xml.toD(antwort) instanceof D_OK){
			if (frontend.istImFernangriffsmodus()) frontend.setFernangriffsmodus(false);
			frontend.log("OK");
		}
		else
			frontend.log("FEHLGESCHLAGEN: "+Xml.toD(antwort).getString("meldung"));
	}

	public boolean autoUpdate(boolean aktivieren) {
		try{
			if (aktivieren){
				frontend.log("Aktiviere Autoupdate...");
				frontend.setUpdater();				
				frontend.log("OK");
			}
			else{
				frontend.log("Deaktiviere Autoupdate...");
				if (frontend.getUpdater()!=null) frontend.killUpdater();
				frontend.log("OK");
			}		
			return true;
		}
		catch (Exception e){
			e.printStackTrace();
			frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
			return false;
		}
	}

	private void aktionEinheitStadt(D_Stadt stadt, D_Einheit einheit) {
		if ((einheit==null)&&(stadt==null)) return;
		D_Spieler spieler=(D_Spieler)Xml.toD(backendSpiel.getSpielerDaten(frontend.getIdSpieler()));
		if (stadt!=null){
			new StatusStadt(frontend,stadt,einheit,spieler);
		} else if (einheit!=null){
			new StatusEinheit(frontend,einheit,spieler);
		}
	}

	public void holenKarte(int idKarte) {
		frontend.log("Holen einer Karte vom Server...");
		if (idKarte==0){
			// keine Karten-ID vorgegeben -> nachfragen
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Karte ID:");
			JTextField jId=new JTextField();
			jId.setText("1");
			eingabeFelder.add(jId);
			MenuEingabe eingabe=new MenuEingabe(this,"Karte holen",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				idKarte=D.toInt(jId.getText());
			}
			else{
				frontend.log("ABGEBROCHEN");
				return;
			}
		}
		frontend.setFeldGewaehlt(null);
		try{
			String antwort=backendSpiel.getKarte(idKarte,frontend.getIdSpieler());
			Karte karte=frontend.neueKarte(antwort);
			karte.setEventhandler(new KarteEventHandler(frontend));
			frontend.log("OK");								
		}
		catch (Exception e){
			e.printStackTrace();
			frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
		}
	}

	private void bewege(Frontend.BewegungsAktion bewegungsAktion) {
		Feld feld=frontend.getFeldGewaehlt();
		if ((bewegungsAktion==null)||(feld==null)||((feld.getEinheit()==null)&&(feld.getStadt()==null))) return;
		int idSpieler=frontend.getIdSpieler();
		int idKarte=feld.getKartenId();
		int xAlt=feld.getPosX();
		int yAlt=feld.getPosY();
		ArrayList<D> datenAktuell=Xml.toArray(backendSpiel.getFeldDaten(idKarte,xAlt,yAlt));
		frontend.getKarte().updateFeld(xAlt,yAlt,datenAktuell);
		D_Einheit einheit=feld.getEinheit();
		D_Stadt stadt=feld.getStadt();
		if (einheit!=null)
			idSpieler=einheit.getInt("idSpieler");
		else if (stadt!=null) 
			idSpieler=stadt.getInt("idSpieler");
		if (idSpieler!=frontend.getIdSpieler()){
			frontend.log("Sie duerfen nur Ihre eigenen Einheiten bewegen!");
			return;
		}
		try{
			if (bewegungsAktion.equals(Frontend.BewegungsAktion.AKTION)){
				aktionEinheitStadt(stadt,einheit);
			}
			else{
				frontend.log("Bewege Einheit...");
				D antwort=Xml.toD(backendSpiel.bewegeEinheit(idSpieler,idKarte,xAlt,yAlt,bewegungsAktion.ordinal()));
				Karte karte=frontend.getKarte();
				if (antwort instanceof D_Fehler)
					throw new RuntimeException(antwort.getString("meldung"));
				D_Position posNeu=(D_Position)antwort;
				int xNeu=posNeu.getInt("x");
				int yNeu=posNeu.getInt("y");
				karte.updateFeld(xAlt,yAlt,Xml.toArray(backendSpiel.getFeldDaten(idKarte,xAlt,yAlt)));
				karte.updateFeld(xNeu,yNeu,Xml.toArray(backendSpiel.getFeldDaten(idKarte,xNeu,yNeu)));
				frontend.setFeldGewaehlt(karte.getFeld(xNeu,yNeu));
				frontend.log("OK");				
			}
		}
		catch (Exception e){
			frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
		}
	}
}