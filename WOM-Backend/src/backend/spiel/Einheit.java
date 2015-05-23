package backend.spiel;

import daten.D_Einheit;
import daten.Xml;

public abstract class Einheit {
	private D_Einheit d_Einheit=new D_Einheit();
	
	public Einheit(){
	}
	
	public void setDaten(D_Einheit daten){
		this.d_Einheit=daten;
	}
	
	public int getId(){
		return d_Einheit.getInt("id");
	}
	public void setId(int id){
		d_Einheit.setInt("id",id);
	}
	
	public void setSpieler(int idSpieler){
		d_Einheit.setInt("idSpieler",idSpieler);
	}
	
	public int getIdSpieler(){
		return d_Einheit.getInt("idSpieler");
	}
	
	public D_Einheit getDaten(){
		return d_Einheit;
	}
	
	public String toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_Einheit));
		return xml.toString();
	}
}
