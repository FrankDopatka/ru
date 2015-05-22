package frontend.menu;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import frontend.Frontend;

public class KarteSpeichern {
	public KarteSpeichern(final Frontend fr){
  	fr.log("Speichere Kartendatei...");
		JFileChooser chooser=new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Kartendaten (.map)","map"));
		if (chooser.showSaveDialog(fr)==JFileChooser.APPROVE_OPTION){
			fr.getBackend().speichernKarte(chooser.getSelectedFile().toString());
			fr.log("Kartendatei erfolgreich gespeichert.");
		}
		else
			fr.log("ABGEBROCHEN"); 
	}
}
