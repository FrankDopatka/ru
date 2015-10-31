package backend.karte.felder;

import backend.karte.Feld;

public class Berg extends Feld {
	
	public Berg(){
		init();
	}	
	public Berg(int idKarte,int x,int y) {
		super(idKarte,x,y,"Berg");
		init();
	}
	
	public void init(){
		setBewegungspunkte(100);
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