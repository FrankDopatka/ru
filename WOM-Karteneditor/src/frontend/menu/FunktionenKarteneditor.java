package frontend.menu;

import interfaces.iBackendKarteneditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import daten.D;
import daten.D_Fehler;
import daten.D_FeldArt;
import daten.D_KartenArt;
import daten.Xml;
import frontend.Frontend;
import frontend.Karte;
import frontend.KarteEventHandler;

public class FunktionenKarteneditor {
	private Frontend frontend;
	private iBackendKarteneditor backendKarteneditor;
	
	public FunktionenKarteneditor(Frontend frontend){
		this.frontend=frontend;
		this.backendKarteneditor=frontend.getBackendKarteneditor();
	}
	
	public void neueKarte(){
   	JSpinner spinnerId=new JSpinner(new SpinnerNumberModel(1,1,255,1));
  	JSpinner spinnerX=new JSpinner(new SpinnerNumberModel(10,5,120,1));
  	JSpinner spinnerY=new JSpinner(new SpinnerNumberModel(10,5,120,1));
  	final JComboBox<String> wahlKartenArt=new JComboBox<String>();
  	final JComboBox<String> wahlFeldArt=new JComboBox<String>();
  	
  	D_KartenArt kartenArten=(D_KartenArt)Xml.toD(backendKarteneditor.getKartenArten());
  	if (kartenArten!=null){
	  	for(String kartenArt:kartenArten.getListe()){
	  		wahlKartenArt.addItem(kartenArt);
	  	}
	  	D_FeldArt feldArten=(D_FeldArt)Xml.toD(backendKarteneditor.getErlaubteFeldArten(kartenArten.getListe().get(0)));
	  	if (feldArten!=null){
		  	for(String feldArt:feldArten.getListe()){
		  		wahlFeldArt.addItem(feldArt);
		  	}	  		
	  	}
  	}
		wahlKartenArt.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ev) {
				String kartenArt=(String)wahlKartenArt.getSelectedItem();
				wahlFeldArt.removeAllItems();
				D_FeldArt feldArten=(D_FeldArt)Xml.toD(backendKarteneditor.getErlaubteFeldArten(kartenArt));
				if (feldArten!=null){
					for(String feldArt:feldArten.getListe()){
			  		wahlFeldArt.addItem(feldArt);
			  	}					
				}
			}
		});
  	Object[] eingaben={
  			"ID der Karte",spinnerId,
  			"Kartengroesse X",spinnerX,
  			"KartengroesseY",spinnerY,
  			"Kartenart",wahlKartenArt,
				"Standard-Feldart",wahlFeldArt};
  	JOptionPane eingabe=new JOptionPane(eingaben,JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
  	eingabe.createDialog(frontend, "Neue Karte erzeugen...").setVisible(true);
  	if ((eingabe.getValue()!=null)&&(eingabe.getValue().equals(JOptionPane.OK_OPTION))){
  		try{
				int id=D.toInt(""+spinnerId.getValue());
				int x=D.toInt(""+spinnerX.getValue());
				int y=D.toInt(""+spinnerY.getValue());
    		String kArt=(String)wahlKartenArt.getSelectedItem();
    		String fArt=(String)wahlFeldArt.getSelectedItem();
    		frontend.log("Erstelle neue Karte ID: "+id+", X: "+x+" Felder, Y: "+y+" Felder, Kartentyp: "+kArt+", Standardfeld: "+fArt+"...");			
    		// Karten im Backend erzeugen:
    		String backendDaten=backendKarteneditor.neueKarte(id,x,y,kArt,fArt);
    		Karte k=frontend.neueKarte(backendDaten);
    		// Eventhandler der Frontend-Karte definieren:
    		k.setEventhandler(new KarteEventHandler(frontend));
    		// rechtes Menu einblenden:
    		frontend.setMenuRechts(new MenuRechts(frontend));
  		}
  		catch (Exception e){
      	JOptionPane.showMessageDialog(
      			frontend,"Ihre Eingaben sind ungueltig!","Neue Karte erzeugen...",
      			JOptionPane.WARNING_MESSAGE);
      	e.printStackTrace();
  		}
  	}
	}
	
	public void speichernKarte(){
		frontend.log("Speichere Kartendatei...");
		JFileChooser chooser=new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Kartendaten (.map)","map"));
		if (chooser.showSaveDialog(frontend)==JFileChooser.APPROVE_OPTION){
			D antwort=Xml.toD(backendKarteneditor.speichernKarte(chooser.getSelectedFile().toString()));
			if (antwort instanceof D_Fehler)
				frontend.log("FEHLER: "+antwort.getString("meldung"));
			else
				frontend.log("Kartendatei erfolgreich gespeichert.");
		}
		else
			frontend.log("ABGEBROCHEN"); 
	}
	
	public void ladenKarte(){
  	frontend.log("Lade Kartendatei...");
		JFileChooser chooser=new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Kartendaten (.map)","map"));
		if (chooser.showOpenDialog(frontend)==JFileChooser.APPROVE_OPTION) {
			try{
				ArrayList<D> antwort=Xml.toArray(backendKarteneditor.ladenKarte(chooser.getSelectedFile().toString()));
				if (antwort.get(0) instanceof D_Fehler)
					frontend.log("FEHLER: "+antwort.get(0).getString("meldung"));
				else{
					frontend.setMenuRechts(new MenuRechts(frontend));
					frontend.log("Kartendatei erfolgreich geladen.");
				}
			}
			catch (RuntimeException e){
				e.printStackTrace();
				frontend.log(e.getMessage());
			}
		}
		else
			frontend.log("ABGEBROCHEN");
	}
}
