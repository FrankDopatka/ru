package backend.karte.felder;

import backend.karte.Feld;

public class Mond extends Feld {
	
	public Mond(){
	}	
	public Mond(int idKarte,int x,int y) {
		super(idKarte,x,y,"Mond");
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
