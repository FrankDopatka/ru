package backend.karte.felder;

import backend.karte.Feld;

public class Huegel extends Feld {
	
	public Huegel(){
	}	
	public Huegel(int idKarte,int x,int y) {
		super(idKarte,x,y,"Huegel");
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Aluminium");
		addErlaubteRessourcenArt("Eisen");
		addErlaubteRessourcenArt("Gold");
		addErlaubteRessourcenArt("Kohle");
		addErlaubteRessourcenArt("Salpeter");
		addErlaubteRessourcenArt("Tabak");
		addErlaubteRessourcenArt("Wein");
		addErlaubteRessourcenArt("Weihrauch");
		addErlaubteRessourcenArt("Zucker");
	}
}
