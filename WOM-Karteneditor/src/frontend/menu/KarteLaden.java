package frontend.menu;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import backend.iBackendKarteneditor;
import frontend.Frontend;
import frontend.Karte;
import frontend.eKarteneditor;

public class KarteLaden {
	public KarteLaden(final Frontend frontend){
  	frontend.log("Lade Kartendatei...");
		JFileChooser chooser=new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Kartendaten (.map)","map"));
		if (chooser.showOpenDialog(frontend)==JFileChooser.APPROVE_OPTION) {
			try{
				// Verbindung zum Backend
				iBackendKarteneditor backend=frontend.getBackend();
				String backendDaten=backend.ladenKarte(chooser.getSelectedFile().toString());
				// Darstellung im Frontend
				Karte k=frontend.neueKarte(backendDaten);
    		// Eventhandler der Frontend-Karte definieren:
    		k.setEventhandler(new eKarteneditor(frontend));
    		frontend.setMenuRechts(new MenuKarteneditor(frontend));
    		frontend.log("Kartendatei erfolgreich geladen.");
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
