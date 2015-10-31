package backend.spiel;

import daten.D_Einheit;
import daten.Xml;

public abstract class Einheit {
	private D_Einheit d_einheit=new D_Einheit();
	
	public Einheit(){
	}
	
	public void setDaten(D_Einheit daten){
		this.d_einheit=daten;
	}
	
	public int getId(){
		return d_einheit.getInt("id");
	}
	public void setId(int id){
		d_einheit.setInt("id",id);
	}	
	public int getPosX(){
		return d_einheit.getInt("x");
	}
	public void getPosX(int x){
		d_einheit.setInt("x",x);
	}
	public int getPosY(){
		return d_einheit.getInt("y");
	}
	public void getPosY(int y){
		d_einheit.setInt("y",y);
	}
	
	public void setSpieler(int idSpieler){
		d_einheit.setInt("idSpieler",idSpieler);
	}
	
	public int getIdSpieler(){
		return d_einheit.getInt("idSpieler");
	}
	
	public D_Einheit getDaten(){
		return d_einheit;
	}
	
	public boolean istLandeinheit(){
		return d_einheit.getBool("istLandeinheit");
	}
	
	public boolean istFernkampfeinheit(){
		return d_einheit.getBool("istFernkampfeinheit");
		
	}
	
	public void resetBewegung(){
		d_einheit.setInt("bewegungAktuell",d_einheit.getInt("bewegungMaximal"));
	}
	
	public String toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_einheit));
		return xml.toString();
	}
	
	@Override
	public String toString(){
		return d_einheit.toString();
	}

	public int getProduktionskosten() {
		return d_einheit.getInt("kostenProduktion");
	}
}
