package backend.karte.karten;

import backend.karte.Karte;

public class Mond extends Karte {

	public Mond(){
		setArt("Mond");
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Berg");
		addErlaubteFeldArt("Eis");
		addErlaubteFeldArt("Huegel");
		addErlaubteFeldArt("Wueste");
	}
}