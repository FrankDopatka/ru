package backend.karte.felder;

import backend.karte.Feld;

public class Tiefsee extends Feld {
	
	public Tiefsee(){
	}	
	public Tiefsee(int idKarte,int x,int y) {
		super(idKarte,x,y,"Tiefsee");
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
