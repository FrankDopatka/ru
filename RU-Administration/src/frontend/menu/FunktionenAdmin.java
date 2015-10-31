package frontend.menu;

import interfaces.iBackendSpielAdmin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import daten.D;
import daten.D_OK;
import daten.D_SpielerNationArt;
import daten.D_SpielerRassenArt;
import daten.Xml;
import frontend.Frontend;

public class FunktionenAdmin {
	private Frontend frontend;
	private iBackendSpielAdmin backendSpielAdmin;
	
	public FunktionenAdmin(Frontend frontend){
		this.frontend=frontend;
		this.backendSpielAdmin=frontend.getBackendSpielAdmin();
	}

	public void neuesSpiel(){
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
			MenuEingabe eingabe=new MenuEingabe(frontend,"neues Spiel",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				int id=D.toInt(jId.getText());
				int anzahlSpieler=D.toInt(jAnzahlSpieler.getText());
				int anzahlKarten=D.toInt(jAnzahlKarten.getText());			
				String antwort=backendSpielAdmin.neuesSpiel(id, anzahlSpieler, anzahlKarten);
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
	
	public void hinzufuegenKarte() {
		try{
			frontend.log("Hinzufuegen einer Karte...");
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Server-Pfad zur Karte:");
			JTextField jPfad=new JTextField();
			jPfad.setText("/home/informatik/Repository-RU/ru/RU-Backend/erde.map");
			eingabeFelder.add(jPfad);
			MenuEingabe eingabe=new MenuEingabe(frontend,"neues Spiel",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				String pfad=jPfad.getText();
				frontend.log("Fuege die Karte "+pfad+" dem Spiel hinzu...");
				String antwort=backendSpielAdmin.hinzufuegenKarte(pfad);
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
	
	public void hinzufuegenSpieler() {
		frontend.log("Hinzufuegen eines Spielers...");
		try{
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
			D_SpielerRassenArt rassenArten=(D_SpielerRassenArt)Xml.toD(backendSpielAdmin.getRassen());
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
						Object o=Xml.toD(backendSpielAdmin.getNationsArten(rassenArt));
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
			MenuEingabe eingabe=new MenuEingabe(frontend,"Spieler hinzufuegen",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				int id=D.toInt(jId.getText());
				String name=jNameSpieler.getText();
				String rasse=(String)jWahlRassenArt.getSelectedItem();
				String nation=(String)jWahlNationArt.getSelectedItem();
				frontend.log("Fuege den Spieler "+name+" dem Spiel hinzu...");
				D antwort=Xml.toD(backendSpielAdmin.hinzufuegenSpieler(id,name,rasse,nation));
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
	
	public void startenSpiel() {
		frontend.log("Starte das Spiel auf dem Server...");
		try{
			D antwort=Xml.toD(backendSpielAdmin.starteSpiel());
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

	public void ladenSpiel() {
		try{
			frontend.log("Spiel laden...");
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Server-Pfad zum Laden:");
			JTextField jPfad=new JTextField();
			jPfad.setText("/home/informatik/Repository-RU/ru/RU-Backend/spiel01.wom");
			eingabeFelder.add(jPfad);
			MenuEingabe eingabe=new MenuEingabe(frontend,"Spiel laden",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				String pfad=jPfad.getText();
				frontend.log("Lade das Spiel vom Server unter "+pfad+"...");
				String antwort=backendSpielAdmin.ladenSpiel(pfad);
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
	
	public void speichernSpiel() {
		try{
			frontend.log("Spiel speichern...");
			ArrayList<String> eingabeBeschriftungen=new ArrayList<String>();
			ArrayList<Object> eingabeFelder=new ArrayList<Object>();
			eingabeBeschriftungen.add("Server-Pfad zum Speichern:");
			JTextField jPfad=new JTextField();
			jPfad.setText("/home/informatik/Repository-RU/ru/RU-Backend/spiel01.wom");
			eingabeFelder.add(jPfad);
			MenuEingabe eingabe=new MenuEingabe(frontend,"Spiel speichern",eingabeBeschriftungen,eingabeFelder);
			if (eingabe.start()){
				String pfad=jPfad.getText();
				frontend.log("Speichere das Spiel auf dem Server unter "+pfad+"...");
				String antwort=backendSpielAdmin.speichernSpiel(pfad);
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
}
