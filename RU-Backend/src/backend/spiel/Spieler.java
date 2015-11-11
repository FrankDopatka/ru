package backend.spiel;


import java.util.ArrayList;

import backend.Parameter;
import backend.karte.Feld;
import daten.*;

public class Spieler {
	private D_Spieler d_Spieler=new D_Spieler();
	private ArrayList<Einheit> einheiten=new ArrayList<Einheit>();
	private ArrayList<Stadt> staedte=new ArrayList<Stadt>();
	private Spiel spiel;
	
	public Spieler(Spiel spiel,int id,String name,String rasse,String nation){
		this.spiel=spiel;
		d_Spieler.setInt("id",id);
		d_Spieler.setString("name",name);
		d_Spieler.setString("rasse",rasse);
		d_Spieler.setString("nation",nation);
	}
	public Spieler(Spiel spiel,D_Spieler daten){
		this.spiel=spiel;
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
			einheit.setId(spiel.getNaechsteEinheitID());
			feld.setEinheit(einheit);
			einheiten.add(einheit);
			return einheit;
		}
		catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void removeEinheit(Einheit einheit,Feld feld){
		if (!einheiten.contains(einheit)) return;
		einheiten.remove(einheit);
		feld.removeEinheit();
	}
	public ArrayList<Einheit> getEinheiten(){
		return einheiten;
	}
	
	public Einheit getEinheit(int idEinheit){
		if (einheiten.size()==0) return null;
		for(Einheit einheit:einheiten){
			if (einheit.getDaten().getInt("id")==idEinheit) return einheit;
		}
		return null;
	}
	
	public Stadt getStadt(int idStadt){
		if (staedte.size()==0) return null;
		for(Stadt stadt:staedte){
			if (stadt.getDaten().getInt("id")==idStadt) return stadt;
		}
		return null;
	}
	
	public int getIdNaechsteStadt(){
		int id=1;
		boolean vergeben=false;
		if (staedte.size()==0) return id;
		do{
			vergeben=false;
			for(Stadt stadt:staedte){
				if (stadt.getDaten().getInt("id")==id){
					vergeben=true;
					id++;
					break;
				}
			}
		}while (vergeben);
		return id;
	}
	
	public Stadt addStadt(Feld feld,String name){
		try{
			Einheit einheit=feld.getEinheit();
			if ((einheit==null)||(einheit.getIdSpieler()!=getId())||(!einheit.getDaten().getString("name").equals("Siedler")))
				throw new RuntimeException("Nur Siedler koennen Staedte gruenden!");
			D_Feld feldDaten=feld.getDaten();
			D_Stadt stadtDaten=new D_Stadt();
			stadtDaten.setString("name",name);
			stadtDaten.setInt("id",getIdNaechsteStadt());
			stadtDaten.setInt("idSpieler",getId());
			stadtDaten.setInt("idKarte",feldDaten.getInt("idKarte"));
			stadtDaten.setInt("x",feldDaten.getInt("x"));
			stadtDaten.setInt("y",feldDaten.getInt("y"));
			Stadt stadt=new Stadt(spiel,stadtDaten);
			einheit.setSpieler(getId());
			feld.setEinheit(null);
			feld.setStadt(stadt);
			einheiten.remove(einheit);
			staedte.add(stadt);
			return stadt;
		}
		catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Spiel getSpiel() {
		return spiel;
	}
	
	public ArrayList<Stadt> getStaedte(){
		return staedte;
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
