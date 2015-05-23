package backend.spiel;


import java.util.ArrayList;

import backend.Parameter;
import backend.karte.Feld;
import daten.*;

public class Spieler {
	private D_Spieler d_Spieler=new D_Spieler();
	private ArrayList<Einheit> einheiten=new ArrayList<Einheit>();
	private ArrayList<Stadt> staedte=new ArrayList<Stadt>();
	
	public Spieler(int id,String name,String rasse,String nation){
		d_Spieler.setInt("id",id);
		d_Spieler.setString("name",name);
		d_Spieler.setString("rasse",rasse);
		d_Spieler.setString("nation",nation);
	}
	public Spieler(D_Spieler daten){
		d_Spieler=daten;
	}
	
	// verwendet beim Laden eines Spiels
	public void setEinheiten(ArrayList<Einheit> einheiten) {
		this.einheiten=einheiten;
	}
	// verwendet beim Laden eines Spiels
	public void setStadte(ArrayList<Stadt> staedte) {
		this.staedte=staedte;
	}
	
	public void setId(int id){
		d_Spieler.setInt("id",id);
	}
	public int getId(){
		return d_Spieler.getInt("id");
	}
	
	public void reset(){
		einheiten=new ArrayList<Einheit>();
		staedte=new ArrayList<Stadt>();
	}
	
	public D_Spieler getDaten(){
		return d_Spieler;
	}

	public Einheit addEinheit(String typ,Feld feld){
		try{
			@SuppressWarnings("unchecked")
			Class<Einheit> c=(Class<Einheit>)Class.forName(Parameter.pfadKlassenEinheiten+typ); // Reflection

			Einheit einheit=(Einheit)c.newInstance();
			einheit.setSpieler(getId());
			feld.setEinheit(einheit);
			einheiten.add(einheit);
			return einheit;
		}
		catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	public String toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_Spieler));
		for(Einheit einheit:einheiten){
			xml.append(einheit.toXml());			
		}
		for(Stadt stadt:staedte){
			xml.append(stadt.toXml());			
		}
		return xml.toString();
	}

	@Override
	public boolean equals(Object o){
		if (!(o instanceof Spieler)) return false;
		Spieler s=(Spieler)o;
		return this.getDaten().getInt("id")==s.getDaten().getInt("id");
	}

}
