package frontend.menu;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import backend.iBackendSpiel;
import daten.*;
import frontend.*;

public class MenuSpiel extends MenuTop{
	private static final long serialVersionUID=1L;
	private JButton[] buttons;

	public MenuSpiel(Frontend frontend){
		super(frontend);
		setPreferredSize(new Dimension(1200,100));
		setLayout(new GridLayout(zeilen,spalten));
		buttons=new JButton[spalten*zeilen];
		for(int i=0;i<spalten*zeilen;i++){
			buttons[i]=new JButton("");
			buttons[i].addActionListener(this);
			add(buttons[i]);
		}
		buttons[0].setText("neues Spiel");
		buttons[1].setText("Karte hinzufuegen");
		buttons[2].setText("Spieler hinzufuegen");
		buttons[3].setText("ich bin Spieler <X>");
		buttons[4].setText("Spiel starten");
		buttons[5].setText("Karte holen");
		
		buttons[6].setText("Spiel speichern");
		buttons[12].setText("Spiel laden");
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
		case 0:
			neuesSpiel();
			break;
		case 1:
			hinzufuegenKarte();
			break;
		case 2:
			hinzufuegenSpieler();
			break;
		case 3:
			wahlSpieler();
			break;
		case 4:
			startenSpiel();
			break;
		case 5:
			holenKarte();
			break;
		case 6:
			speichernSpiel();
			break;
		case 12:
			ladenSpiel();
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

	private void ladenSpiel() {
		try{
			frontend.log("Spiel laden...");
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Server-Pfad zum Laden:");
			JTextField jPfad=new JTextField();
			jPfad.setText("/home/informatik/spiel01.wom");
			eingabeFelder.add(jPfad);
			MenuEingabe eingabe=new MenuEingabe(this,"Spiel laden",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				frontend.setFeldGewaehlt(null);
				String pfad=jPfad.getText();
				frontend.log("Lade das Spiel vom Server unter "+pfad+"...");
				String antwort=frontend.getBackend().ladenSpiel(pfad);
				if (Xml.toD(antwort) instanceof D_OK){
					frontend.log("OK");
					// Karte holen
					frontend.log("Hole Karte mit ID=1 vom Server...");
					String antwort2=frontend.getBackend().getKarte(1);
					try{
						Karte karte=frontend.neueKarte(antwort2);
						karte.setEventhandler(new eSpiel(frontend));
						frontend.log("OK");								
					}
					catch (Exception e){
						e.printStackTrace();
						frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
					}
				}
				else{
					frontend.log("FEHLGESCHLAGEN: "+Xml.toD(antwort).getString("meldung"));
				}
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

	private void speichernSpiel() {
		try{
			frontend.log("Spiel speichern...");
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Server-Pfad zum Speichern:");
			JTextField jPfad=new JTextField();
			jPfad.setText("/home/informatik/spiel01.wom");
			eingabeFelder.add(jPfad);
			MenuEingabe eingabe=new MenuEingabe(this,"Spiel speichern",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				String pfad=jPfad.getText();
				frontend.log("Speichere das Spiel auf dem Server unter "+pfad+"...");
				String antwort=frontend.getBackend().speichernSpiel(pfad);
				if (Xml.toD(antwort) instanceof D_OK)
					frontend.log("OK");
				else{
					frontend.log("FEHLGESCHLAGEN: "+Xml.toD(antwort).getString("meldung"));
				}
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

	private void neuesSpiel(){
		try{
			frontend.log("Starte neues Spiel auf dem Server...");
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Spiel ID:");
			JTextField jId=new JTextField();
			jId.setText("1");
			eingabeFelder.add(jId);
			eingabeBeschriftungen.add("max. Anzahl Spieler:");
			JTextField jAnzahlSpieler=new JTextField();
			jAnzahlSpieler.setText("2");
			eingabeFelder.add(jAnzahlSpieler);
			eingabeBeschriftungen.add("max. Anzahl Karten:");
			JTextField jAnzahlKarten=new JTextField();
			jAnzahlKarten.setText("2");
			eingabeFelder.add(jAnzahlKarten);
			MenuEingabe eingabe=new MenuEingabe(this,"neues Spiel",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				frontend.setFeldGewaehlt(null);
				int id=D.toInt(jId.getText());
				int anzahlSpieler=D.toInt(jAnzahlSpieler.getText());
				int anzahlKarten=D.toInt(jAnzahlKarten.getText());			
				String antwort=frontend.getBackend().neuesSpiel(id, anzahlSpieler, anzahlKarten);
				if (Xml.toD(antwort) instanceof D_OK)
					frontend.log("OK");
				else
					frontend.log("FEHLGESCHLAGEN: "+Xml.toD(antwort).getString("meldung"));
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
		
	private void hinzufuegenKarte() {
		try{
			frontend.log("Hinzufuegen einer Karte...");
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Server-Pfad zur Karte:");
			JTextField jPfad=new JTextField();
			jPfad.setText("/home/informatik/erde.map");
			eingabeFelder.add(jPfad);
			MenuEingabe eingabe=new MenuEingabe(this,"neues Spiel",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				String pfad=jPfad.getText();
				frontend.log("Fuege die Karte "+pfad+" dem Spiel hinzu...");
				String antwort=frontend.getBackend().hinzufuegenKarte(pfad);
				if (Xml.toD(antwort) instanceof D_OK)
					frontend.log("OK");
				else{
					frontend.log("FEHLGESCHLAGEN: "+Xml.toD(antwort).getString("meldung"));
				}
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

	private void hinzufuegenSpieler() {
		frontend.log("Hinzufuegen eines Spielers...");
		try{
			iBackendSpiel backend=frontend.getBackend();
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Spieler ID:");
			JTextField jId=new JTextField();
			jId.setText("0");
			eingabeFelder.add(jId);
			eingabeBeschriftungen.add("Name:");
			JTextField jNameSpieler=new JTextField();
			jNameSpieler.setText("Frank");
			eingabeFelder.add(jNameSpieler);
			eingabeBeschriftungen.add("Rasse:");
			D_SpielerRassenArt rassenArten=(D_SpielerRassenArt)Xml.toD(backend.getRassen());
			final JComboBox<String> jWahlRassenArt=new JComboBox<String>();
			final JComboBox<String> jWahlNationArt=new JComboBox<String>();
			if (rassenArten!=null){
		  	for(String rassenArt:rassenArten.getListe()){
		  		jWahlRassenArt.addItem(rassenArt);
		  	}
		  	jWahlRassenArt.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent ev) {
						String rassenArt=(String)jWahlRassenArt.getSelectedItem();
						jWahlNationArt.removeAllItems();
						Object o=Xml.toD(backend.getNationsArten(rassenArt));
						if (o instanceof D_SpielerNationArt){
							D_SpielerNationArt nationArten=(D_SpielerNationArt)o;
							if (nationArten!=null){
								for(String nationArt:nationArten.getListe()){
									jWahlNationArt.addItem(nationArt);
						  	}					
							}						
						}					
					}
				});
		  	jWahlRassenArt.setSelectedItem("Mensch");
	  	}
			eingabeFelder.add(jWahlRassenArt);
			eingabeBeschriftungen.add("Nation:");
			eingabeFelder.add(jWahlNationArt);
			MenuEingabe eingabe=new MenuEingabe(this,"Spieler hinzufuegen",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				int id=D.toInt(jId.getText());
				String name=jNameSpieler.getText();
				String rasse=(String)jWahlRassenArt.getSelectedItem();
				String nation=(String)jWahlNationArt.getSelectedItem();
				frontend.log("Fuege den Spieler "+name+" dem Spiel hinzu...");
				D antwort=Xml.toD(backend.hinzufuegenSpieler(id,name,rasse,nation));
				if (antwort instanceof D_OK)
					frontend.log("OK");
				else
					frontend.log("FEHLGESCHLAGEN: "+antwort.getString("meldung"));
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

	private void startenSpiel() {
		frontend.log("Starte das Spiel auf dem Server...");
		try{
			D antwort=Xml.toD(frontend.getBackend().starteSpiel());
			if (antwort instanceof D_OK)
				frontend.log("OK");
			else
				frontend.log("FEHLGESCHLAGEN: "+antwort.getString("meldung"));			
		}
		catch (Exception e){
			e.printStackTrace();
			frontend.log("FEHLGESCHLAGEN: "+e.getMessage());
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
				karte.setEventhandler(new eSpiel(frontend));
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
