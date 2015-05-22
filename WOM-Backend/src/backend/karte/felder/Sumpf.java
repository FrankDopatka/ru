package backend.karte.felder;

import backend.karte.Feld;

public class Sumpf extends Feld {
	
	public Sumpf(){
	}	
	public Sumpf(int idKarte,int x,int y) {
		super(idKarte,x,y,"Sumpf");
	}
	
	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Oel");
		addErlaubteRessourcenArt("Gummi");
		addErlaubteRessourcenArt("Wild");
	}
}
