package frontend.menu;

import frontend.Frontend;
import frontend.Karte;
import frontend.eKarteneditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import backend.iBackendKarteneditor;
import daten.*;

public class KarteNeu {
	public KarteNeu(final Frontend frontend){
		final iBackendKarteneditor backend=frontend.getBackend();
   	JSpinner spinnerId=new JSpinner(new SpinnerNumberModel(1,1,255,1));
  	JSpinner spinnerX=new JSpinner(new SpinnerNumberModel(10,5,120,1));
  	JSpinner spinnerY=new JSpinner(new SpinnerNumberModel(10,5,120,1));
  	final JComboBox<String> wahlKartenArt=new JComboBox<String>();
  	final JComboBox<String> wahlFeldArt=new JComboBox<String>();
  	
  	D_KartenArt kartenArten=(D_KartenArt)Xml.toD(backend.getKartenArten());
  	if (kartenArten!=null){
	  	for(String kartenArt:kartenArten.getListe()){
	  		wahlKartenArt.addItem(kartenArt);
	  	}
	  	D_FeldArt feldArten=(D_FeldArt)Xml.toD(backend.getErlaubteFeldArten(kartenArten.getListe().get(0)));
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
				D_FeldArt feldArten=(D_FeldArt)Xml.toD(backend.getErlaubteFeldArten(kartenArt));
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
    		String backendDaten=backend.neueKarte(id,x,y,kArt,fArt);
    		Karte k=frontend.neueKarte(backendDaten);
    		// Eventhandler der Frontend-Karte definieren:
    		k.setEventhandler(new eKarteneditor(frontend));
    		// rechtes Menu einblenden:
    		frontend.setMenuRechts(new MenuKarteneditor(frontend));
  		}
  		catch (Exception e){
      	JOptionPane.showMessageDialog(
      			frontend,"Ihre Eingaben sind ungueltig!","Neue Karte erzeugen...",
      			JOptionPane.WARNING_MESSAGE);
      	e.printStackTrace();
  		}
  	}
	}
}
