package backend.karte.felder;

import backend.karte.Feld;

public class Wiese extends Feld {
	
	public Wiese(){
	}	
	public Wiese(int idKarte,int x,int y) {
		super(idKarte,x,y,"Wiese");
	}
	
	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Pferde");
		addErlaubteRessourcenArt("Tabak");
		addErlaubteRessourcenArt("Rinder");
		addErlaubteRessourcenArt("Wein");
		addErlaubteRessourcenArt("Weizen");
	}
}