package frontend.menu;

import frontend.Feld;
import frontend.Frontend;
import frontend.Karte;
import frontend.KarteEventHandler;
import interfaces.iBackendSpiel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	private Frontend frontend;
	private iBackendSpiel backendSpiel;
	private int spalten;
	private int zeilen;
	private JButton[] buttons;

	public MenuTop(Frontend frontend){
		this.frontend=frontend;
		this.backendSpiel=frontend.getBackend();
		spalten=6;
		zeilen=4;
		setPreferredSize(new Dimension(1200,100));
		setLayout(new GridLayout(zeilen,spalten));
		buttons=new JButton[spalten*zeilen];
		for(int i=0;i<spalten*zeilen;i++){
			buttons[i]=new JButton("");
			buttons[i].addActionListener(this);
			add(buttons[i]);
		}
		buttons[0].setText("Karte holen");
		buttons[1].setText("Autoupdate <AUS>");
		// Navigation
		buttons[9].setText("Nord-West");
		buttons[10].setText("Nord");
		buttons[11].setText("Nord-Ost");
		buttons[15].setText("West");
		buttons[17].setText("Ost");
		buttons[21].setText("Sued-West");
		buttons[22].setText("Sued");
		buttons[23].setText("Sued-Ost");
		buttons[16].setText("Aktion Einheit/Stadt");
		buttons[20].setText("Runde beenden");
//		holenKarte(1); // Karte mit der ID=1 standardmaessig holen
	//	buttons[1].doClick(); // Autoupdate aktivieren
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o=ev.getSource();
		if (o instanceof JButton){
			for(int i=0;i<spalten*zeilen;i++){
				if (o==buttons[i]){
					aktion(i);
					break;
				}
			}			
		}
		else if (o instanceof Feld){
			Feld feld=(Feld)o;
			frontend.log(feld.toString());
			frontend.setFeldGewaehlt(feld);
		}
	}

	private void aktion(int i) {
		switch (i){
		case 0:
			holenKarte(0);
			break;
		case 1:
			if (buttons[1].getText().equals("Autoupdate <AUS>")){
				if (autoUpdate(true)) buttons[1].setText("Autoupdate <AN>");
			}
			else{
				if (autoUpdate(false)) buttons[1].setText("Autoupdate <AUS>");				
			}
			break;
		case 16:
			aktionEinheitStadt();
			break;
		case 9:
			bewege(Frontend.Bewegungsrichtung.NORDWEST);
			break;
		case 10:
			bewege(Frontend.Bewegungsrichtung.NORD);
			break;
		case 11:
			bewege(Frontend.Bewegungsrichtung.NORDOST);
			break;
		case 15:
			bewege(Frontend.Bewegungsrichtung.WEST);
			break;
		case 17:
			bewege(Frontend.Bewegungsrichtung.OST);
			break;
		case 21:
			bewege(Frontend.Bewegungsrichtung.SUEDWEST);
			break;
		case 22:
			bewege(Frontend.Bewegungsrichtung.SUED);
			break;
		case 23:
			bewege(Frontend.Bewegungsrichtung.SUEDOST);
			break;
		case 20:
			beendenRunde(frontend.getIdSpieler());
			break;
		default:
				frontend.log("Button "+i+" wurde geklickt.");
		}
	}

	private void beendenRunde(int idSpieler) {
		String antwort=backendSpiel.beendenRunde(idSpieler);
		frontend.log("Der Spieler mit der ID="+idSpieler+" beendet die Runde...");
		if (Xml.toD(antwort) instanceof D_OK)
			frontend.log("OK");
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


	private void aktionEinheitStadt() {
		Feld feld=frontend.getFeldGewaehlt();
		if (feld==null) return;
		D_Einheit einheit=feld.getEinheit();
		D_Stadt stadt=feld.getStadt();
		if ((einheit==null)&&(stadt==null)) return;
		if (stadt!=null){
			
			
			// TODO STADTBILDSCHIRM ANZEIGEN
			
			
		} else if (einheit!=null){
			D_Spieler spieler=(D_Spieler)Xml.toD(backendSpiel.getSpielerDaten(einheit.getInt("idSpieler")));
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

	private void bewege(Frontend.Bewegungsrichtung bewegung) {
		Feld feld=frontend.getFeldGewaehlt();
		if ((bewegung==null)||(feld==null)||((feld.getEinheit()==null))) return;
		int idSpieler=frontend.getIdSpieler();
		int idKarte=feld.getDaten().getInt("idKarte");
		int xAlt=feld.getDaten().getInt("x");
		int yAlt=feld.getDaten().getInt("y");
		
		feld.getEinheit().getInt("idSpieler");
		try{
			frontend.log("Bewege Einheit...");
			if (feld.getEinheit().getInt("idSpieler")!=idSpieler)
				throw new RuntimeException("MenuTop bewege: Sie duerfen nur Ihre eigenen Einheiten bewegen!");
			D antwort=Xml.toD(backendSpiel.bewegeEinheit(idSpieler,idKarte,xAlt,yAlt,bewegung.ordinal()));
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
		catch (Exception e){
//			e.printStackTrace();
			frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
		}
	}
}