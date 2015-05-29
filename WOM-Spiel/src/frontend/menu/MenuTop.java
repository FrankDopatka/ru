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
import javax.swing.JComboBox;
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
	protected Frontend frontend;
	protected int spalten;
	protected int zeilen;
	private JButton[] buttons;

	public MenuTop(Frontend frontend){
		this.frontend=frontend;
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
		buttons[3].setText("ich bin Spieler <X>");
		buttons[5].setText("Karte holen");
		buttons[18].setText("Autoupdate <AUS>");
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
		case 3:
			wahlSpieler();
			break;
		case 5:
			holenKarte();
			break;
		case 18:
			if (buttons[18].getText().equals("Autoupdate <AUS>")){
				if (autoUpdate(true)) buttons[18].setText("Autoupdate <AN>");
			}
			else{
				if (autoUpdate(false)) buttons[18].setText("Autoupdate <AUS>");				
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
		String antwort=frontend.getBackend().beendenRunde(idSpieler);
		frontend.log("Der Spieler mit der ID="+idSpieler+" beendet die Runde...");
		if (Xml.toD(antwort) instanceof D_OK)
			frontend.log("OK");
		else
			frontend.log("FEHLGESCHLAGEN: "+Xml.toD(antwort).getString("meldung"));
	}

	private boolean autoUpdate(boolean aktivieren) {
		try{
			if (aktivieren){
				frontend.log("Aktiviere Autoupdate...");
				if (frontend.getIdSpieler()<1)
					throw new RuntimeException("autoUpdate: Sie muessen zuerst einen Spieler waehlen, den Sie ab jetzt spielen!");
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
			D_Spieler spieler=(D_Spieler)Xml.toD(frontend.getBackend().getSpielerDaten(einheit.getInt("idSpieler")));
			new StatusEinheit(frontend,einheit,spieler);
		}
	}
	
	private void wahlSpieler() {
		try{
			frontend.log("Auswahl der clientseitigen Steuerung eines Spielers...");
			ArrayList<D> spielerDaten=Xml.toArray(frontend.getBackend().getAlleSpieler());
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Spieler zum Steuern auswaehlen:");
			final JComboBox<String> jWahlSpieler=new JComboBox<String>();
			if ((spielerDaten!=null)&&(spielerDaten.size()>0)){
				for(D daten:spielerDaten){
					D_Spieler x=(D_Spieler)daten;
					if (x.getString("rasse").equals("Mensch"))
						jWahlSpieler.addItem(x.getInt("id")+" - "+x.getString("name")+","+x.getString("rasse")+","+x.getString("nation"));
					else
						jWahlSpieler.addItem(x.getInt("id")+" - "+x.getString("name")+","+x.getString("rasse"));
		  	}
			}
			eingabeFelder.add(jWahlSpieler);
			MenuEingabe eingabe=new MenuEingabe(this,"Spieler zur Steuerung waehlen",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				String wahl=(String)jWahlSpieler.getSelectedItem();
				String[] teile=wahl.split(" - ");
				frontend.setIdSpieler(Integer.parseInt(teile[0]));
				buttons[3].setText("ich bin Spieler <"+teile[0]+">");
				frontend.log("OK");
			}
			else{
				frontend.log("ABGEBROCHEN");
			}
		}
		catch (Exception e){
			frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
			e.printStackTrace();
		}
	}

	private void holenKarte() {
		frontend.log("Holen einer Karte vom Server...");
		ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
		ArrayList<Object> eingabeFelder=new ArrayList<Object>();
		eingabeBeschriftungen.add("Karte ID:");
		JTextField jId=new JTextField();
		jId.setText("1");
		eingabeFelder.add(jId);
		MenuEingabe eingabe=new MenuEingabe(this,"Karte holen",eingabeBeschriftungen,eingabeFelder);
		if (eingabe.start()){
			frontend.setFeldGewaehlt(null);
			int id=D.toInt(jId.getText());
			try{
				String antwort=frontend.getBackend().getKarte(id);
				Karte karte=frontend.neueKarte(antwort);
				karte.setEventhandler(new KarteEventHandler(frontend));
				frontend.log("OK");								
			}
			catch (Exception e){
				e.printStackTrace();
				frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
			}
		}
		else{
			frontend.log("ABGEBROCHEN");
		}
	}

	private void bewege(Frontend.Bewegungsrichtung bewegung) {
		Feld feld=frontend.getFeldGewaehlt();
		if ((bewegung==null)||(feld==null)||((feld.getEinheit()==null))) return;
		iBackendSpiel backend=frontend.getBackend();
		int idSpieler=frontend.getIdSpieler();
		int idKarte=feld.getDaten().getInt("idKarte");
		int xAlt=feld.getDaten().getInt("x");
		int yAlt=feld.getDaten().getInt("y");
		try{
			frontend.log("Bewege Einheit...");
			D antwort=Xml.toD(backend.bewegeEinheit(idSpieler,idKarte,xAlt,yAlt,bewegung.ordinal()));
			Karte karte=frontend.getKarte();
			if (antwort instanceof D_Fehler)
				throw new RuntimeException(antwort.getString("meldung"));
			D_Position posNeu=(D_Position)antwort;
			int xNeu=posNeu.getInt("x");
			int yNeu=posNeu.getInt("y");
			karte.updateFeld(xAlt,yAlt,Xml.toArray(frontend.getBackend().getFeldDaten(idKarte,xAlt,yAlt)));
			karte.updateFeld(xNeu,yNeu,Xml.toArray(frontend.getBackend().getFeldDaten(idKarte,xNeu,yNeu)));
			frontend.setFeldGewaehlt(karte.getFeld(xNeu,yNeu));
			frontend.log("OK");
		}
		catch (Exception e){
			e.printStackTrace();
			frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
		}
	}
}