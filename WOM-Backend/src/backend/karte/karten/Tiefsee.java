package backend.karte.karten;

import backend.karte.Karte;

public class Tiefsee extends Karte {

	public Tiefsee(){
		setArt("Tiefsee");
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Tiefsee");
	}
}
