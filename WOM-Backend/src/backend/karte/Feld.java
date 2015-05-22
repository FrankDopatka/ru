package backend.karte;

import java.util.ArrayList;

import backend.spiel.Einheit;
import backend.spiel.Stadt;
import daten.D;
import daten.D_Feld;
import daten.D_RessourcenArt;
import daten.Xml;

public abstract class Feld {
	private D_Feld d_Feld=new D_Feld();
	private D_RessourcenArt d_ErlaubteRessourcenArten=new D_RessourcenArt();
	private Einheit einheit=null;
	private Stadt stadt=null;
	
	public Feld(){
		setErlaubteRessourcenArt();
	}
	public Feld(String feldArt){
		this();
		d_Feld.setString("feldArt",feldArt);
	}
	public Feld(int idKarte,int x,int y,String feldArt){
		this(feldArt);
		d_Feld.setInt("idKarte",idKarte);
		d_Feld.setInt("x",x);
		d_Feld.setInt("y",y);
	}
	
	public abstract void setErlaubteRessourcenArt();
	public void addErlaubteRessourcenArt(String ressourcenArt){
		d_ErlaubteRessourcenArten.addString(""+d_ErlaubteRessourcenArten.getNachesteId(),ressourcenArt);
	}
	public D_RessourcenArt getErlaubteRessourcenArten(){
		return d_ErlaubteRessourcenArten;
	}
	
	public void setRessource(String ressource){
		if ((ressource!=null)&&(!ressource.equals(""))){
			if (!getErlaubteRessourcenArten().existValue(ressource))
				throw new RuntimeException("Feld setRessource: "+ressource+" ist auf "+getArt()+" nicht erlaubt!");
			d_Feld.setString("ressource",ressource);			
		}
		else
			d_Feld.setString("ressource","");
	}
	public String getRessource(){
		return d_Feld.getString("ressource");
	}

	public void setArt(String feldArt) {
		d_Feld.setString("feldArt",feldArt);
	}

	public String getArt() {
		return d_Feld.getString("feldArt");
	}

	public void setSpielerstart(int spielerstart) {
		d_Feld.setInt("spielerstart",spielerstart);
	}
	
	public int istSpielerstart() {
		return d_Feld.getInt("spielerstart");
	}
	
	public int getPosX(){
		return d_Feld.getInt("x");
	}
	public int getPosY(){
		return d_Feld.getInt("y");
	}
	public int[] getPos(){
		return new int[]{getPosX(),getPosY()};
	}
	public void setPos(int x,int y){
		d_Feld.setInt("x",x);
		d_Feld.setInt("y",y);
	}

	public void setIdKarte(int idKarte){
		d_Feld.setInt("idKarte",idKarte);
	}
	public int getIdKarte(){
		return d_Feld.getInt("idKarte");
	}
		
	public D_Feld getDaten(){
		return d_Feld;
	}
	
	public void setEinheit(Einheit einheit){
		this.einheit=einheit;
		if ((einheit!=null)&&(einheit.getFeld()!=null)&&(!einheit.getFeld().equals(this))){
			einheit.setFeld(this);
		}
	}
	public Einheit getEinheit(){
		return einheit;
	}
	
	public String toXml(){
		ArrayList<D> daten=new ArrayList<D>();
		daten.add(d_Feld);
		if(stadt!=null) daten.add(stadt.getDaten());
		if (einheit!=null) daten.add(einheit.getDaten());
		return Xml.fromArray(daten);
	}
	
	@Override
	public String toString(){
		return "Feld "+d_Feld.getInt("x")+"/"+d_Feld.getInt("y")+" vom Typ "+d_Feld.getString("feldArt");
	}

}
