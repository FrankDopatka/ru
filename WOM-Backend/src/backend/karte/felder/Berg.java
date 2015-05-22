package backend.karte.felder;

import backend.karte.Feld;

public class Berg extends Feld {
	
	public Berg(){
	}	
	public Berg(int idKarte,int x,int y) {
		super(idKarte,x,y,"Berg");
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Edelsteine");
		addErlaubteRessourcenArt("Eisen");
		addErlaubteRessourcenArt("Gold");
		addErlaubteRessourcenArt("Kohle");
		addErlaubteRessourcenArt("Salpeter");
		addErlaubteRessourcenArt("Uran");
	}
}