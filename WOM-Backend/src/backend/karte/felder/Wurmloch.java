package backend.karte.felder;

import backend.karte.Feld;

public class Wurmloch extends Feld {
	
	public Wurmloch(){
	}	
	public Wurmloch(int idKarte,int x,int y) {
		super(idKarte,x,y,"Wurmloch");
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
