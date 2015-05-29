package backend.karte.karten;

import backend.karte.Karte;

public class Mond extends Karte {

	public Mond(){
		setArt("Mond");
		getDaten().addInt("idZielkarte",0);
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Berg");
		addErlaubteFeldArt("Eis");
		addErlaubteFeldArt("Huegel");
		addErlaubteFeldArt("Wueste");
	}
}