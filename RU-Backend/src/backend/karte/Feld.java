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
		if (einheit!=null){
			einheit.getDaten().setInt("idKarte",d_Feld.getInt("idKarte"));
			einheit.getDaten().setInt("x",d_Feld.getInt("x"));
			einheit.getDaten().setInt("y",d_Feld.getInt("y"));			
		}
		this.einheit=einheit;
	}
	public Einheit getEinheit(){
		return einheit;
	}
	public void removeEinheit(){
		setEinheit(null);
	}
	
	public void setStadt(Stadt stadt) {
		if (stadt!=null){
			stadt.getDaten().setInt("idKarte",d_Feld.getInt("idKarte"));
			stadt.getDaten().setInt("x",d_Feld.getInt("x"));
			stadt.getDaten().setInt("y",d_Feld.getInt("y"));			
		}
		this.stadt=stadt;
	}
	public Stadt getStadt(){
		return stadt;
	}
	
	public boolean istWasserfeld(){
		return d_Feld.getBool("istWasserfeld");
	}
	
	public int getBewegungspunkte(){
		//TODO komplexere Berechnung noetig bei Feldverbesserungen wie Strasse
		return d_Feld.getInt("bewegungspunkte");
	}
	
	public void setBewegungspunkte(int punkte){
		d_Feld.setInt("bewegungspunkte",punkte);
	}
	
	public String toXml(){
		return Xml.fromArray(toDatenArray());
	}
	
	public ArrayList<D> toDatenArray(){
		ArrayList<D> daten=new ArrayList<D>();
		daten.add(d_Feld);
		if(stadt!=null) daten.add(stadt.getDaten());
		if (einheit!=null) daten.add(einheit.getDaten());
		return daten;
	}
	
	@Override
	public String toString(){
		return "Feld "+d_Feld.getInt("x")+"/"+d_Feld.getInt("y")+" vom Typ "+d_Feld.getString("feldArt");
	}


}
