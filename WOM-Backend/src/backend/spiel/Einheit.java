package backend.spiel;

import daten.D_Einheit;
import daten.Xml;
import backend.karte.Feld;

public abstract class Einheit {
	private D_Einheit d_Einheit=new D_Einheit();
	private Spieler spieler;
	private Feld feld;
	
	public Einheit(){
	}
	
	public int getId(){
		return d_Einheit.getInt("id");
	}

	public void setId(int id){
		d_Einheit.setInt("id",id);
	}
	
	public void setSpieler(Spieler spieler){
		if (spieler==null)
			throw new RuntimeException("Einheit setSpieler: Der Spieler darf nicht NULL sein!");
		this.spieler=spieler;
		d_Einheit.setInt("idSpieler",spieler.getId());
	}
	public Spieler getSpieler(){
		return spieler;
	}
	
	public D_Einheit getDaten(){
		return d_Einheit;
	}
	
	public void setFeld(Feld feld){
		this.feld=feld;
		if (this.feld!=null) this.feld.setEinheit(null);
		if (feld!=null){
			d_Einheit.setInt("idKarte",feld.getIdKarte());
			d_Einheit.setInt("x",feld.getPosX());
			d_Einheit.setInt("y",feld.getPosY());
			feld.setEinheit(this);
		}
	}
	
	public Feld getFeld(){
		return feld;
	}

	public String toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_Einheit));
		return xml.toString();
	}
}
