package backend.karte;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.ArrayList;

import backend.Parameter;
import backend.Updater;
import daten.D;
import daten.D_Feld;
import daten.D_FeldArt;
import daten.D_Karte;
import daten.Xml;

public abstract class Karte {
	private Feld[][] felder;
	private D_Karte d_Karte=new D_Karte();
	private D_FeldArt d_ErlaubteFeldArten=new D_FeldArt();
	private Updater updater;

	public Karte(){
		setErlaubteFeldArten();
		updater=new Updater();
	}
	
	public static String karteLaden(String pfad){
		BufferedReader br=null;
		try {
			pfad=URLDecoder.decode(""+pfad,"ISO-8859-1");
			StringBuffer karteXML=new StringBuffer();
			br=new BufferedReader(new FileReader(pfad));
			String zeile=br.readLine(); 
	    while (zeile!=null){
	    	karteXML.append(zeile+"/n");
	      zeile=br.readLine(); 
	    } 
	    return karteXML.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Karte karteLaden:"+e.getMessage());
		}
		finally{
			try {
				br.close();
			} catch (Exception e) {}			
		}
	}
	
	public static Karte karteVonXml(String xml){
		Karte karte=karteVonArray(Xml.toArray(xml));
		return karte;
	}

	public static Karte karteVonArray(ArrayList<D> xmlDaten){
		Karte karte=null;
		D_Karte datenKarte=null;
		D datenRoh=xmlDaten.get(0);
		if(!(datenRoh instanceof D_Karte))
			throw new RuntimeException("Karte karteVonXml: Die eingelesenden Rohdaten sind keine Karte!");
		try {
			datenKarte=(D_Karte)datenRoh;
			@SuppressWarnings("unchecked")
			Class<Karte> c=(Class<Karte>)Class.forName(Parameter.pfadKlassenKarten+datenKarte.getString("kartenArt"));
			karte=(Karte)c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Karte karteVonXml: "+e.getMessage());
		}
		karte.setDaten(datenKarte);
		karte.setGroesse(new int[] {datenKarte.getInt("x"),datenKarte.getInt("y")});
		for(D daten:xmlDaten){
			if (daten instanceof D_Feld){
				D_Feld df=(D_Feld)daten;
				karte.setFeld(df.getInt("idKarte"),df.getInt("x"),df.getInt("y"),df.getString("feldArt"),df.getString("ressource"),df.getInt("spielerstart"));
			}
		}
		return karte;
	}
	
	public void setId(int id){
		if (id<1)
			throw new RuntimeException("Karte setId: Wert für die ID ist ungueltig!");
		d_Karte.setInt("id",id);
	}
	public int getId(){
		return d_Karte.getInt("id");
	}
	public void setGroesse(int[] pos){
		if ((pos==null)||(pos.length!=2)||(pos[0]<1)||(pos[1]<1))
			throw new RuntimeException("Karte setGroesse: Wert für die x/y-Koordinaten ist ungueltig!");
		d_Karte.setInt("x",pos[0]);
		d_Karte.setInt("y",pos[1]);
		felder=new Feld[pos[0]+1][pos[1]+1];
	}
	public int[] getGroesse(){
		return new int[]{d_Karte.getInt("x"),d_Karte.getInt("y")};
	}
	public int getGroesseX(){
		return d_Karte.getInt("x");
	}
	public int getGroesseY(){
		return d_Karte.getInt("y");
	}
	public void setArt(String kartenArt){
		d_Karte.setString("kartenArt",kartenArt);
	}
	public String getArt(){
		return d_Karte.getString("kartenArt");
	}

	public void setDaten(D_Karte daten){
		d_Karte=daten;
	}
	
	public D_Karte getDaten(){
		return d_Karte;
	}

	public abstract void setErlaubteFeldArten();
	public void addErlaubteFeldArt(String feldArt){
		d_ErlaubteFeldArten.addString(""+d_ErlaubteFeldArten.getNachesteId(),feldArt);
	}
	public D_FeldArt getErlaubteFeldArten(){
		return d_ErlaubteFeldArten;
	}

	public void setFeld(int idKarte,int[] pos,String feldArt,String ressource,int spielerstart){
		if ((pos==null)||(pos.length!=2)) return;
		setFeld(idKarte,pos[0],pos[1],feldArt,ressource,spielerstart);
	}

	public void setFeld(int idKarte,int x,int y,String feldArt,String ressource,int spielerstart){
		if (!getErlaubteFeldArten().existValue(feldArt))
			throw new RuntimeException("Karte setFeld: "+feldArt+" ist in dieser Karte nicht erlaubt!");
		try {
			@SuppressWarnings("unchecked")
			Class<Feld> c=(Class<Feld>)Class.forName(Parameter.pfadKlassenFelder+feldArt); // Reflection
			Feld feld=(Feld)c.newInstance();
			feld.setIdKarte(idKarte);
			feld.setArt(feldArt);
			feld.setRessource(ressource);
			feld.setSpielerstart(spielerstart);
			feld.setPos(x,y);
			felder[x][y]=feld;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Karte setFeld: "+e.getMessage());
		}	
	}
	
	public void setAlleFelder(String feldArt){
		for(int i=1;i<=d_Karte.getInt("x");i++){
			for(int j=1;j<=d_Karte.getInt("y");j++){
				setFeld(d_Karte.getInt("id"),i,j,feldArt,null,0);
			}
		}
	}
	
	public Feld[][] getFelder(){
		return felder;
	}
	
	public Feld getFeld(int x,int y){
		return felder[x][y];
	}

	public Feld getFeld(int[] pos){
		if ((pos==null)||(pos.length!=2)) return null;
		return felder[pos[0]][pos[1]];
	}
	
	public String toXml(){
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_Karte));
		for(int i=1;i<=d_Karte.getInt("x");i++){
			for(int j=1;j<=d_Karte.getInt("y");j++){
				xml.append(felder[i][j].toXml());
			}
		}
		return xml.toString();
	}

	public String toXml(int x, int y, int reichweite) {
		StringBuffer xml=new StringBuffer();
		int xStart=x-reichweite;
		int yStart=y-reichweite;
		int xEnde=x+reichweite;
		int yEnde=y+reichweite;
		int xMax=d_Karte.getInt("x");
		int yMax=d_Karte.getInt("y");
		for(int i=xStart;i<=xEnde;i++){
			for(int j=yStart;j<=yEnde;j++){
				if ((i>=1)&&(i<=xMax)&&(j>=1)&&(j<=yMax)){
					xml.append(felder[i][j].toXml());
				}
			}
		}
		return xml.toString();
	}

	public void setSpielerstart(int[] pos,int spielerstart) {
		if (spielerstart>0){
			if (!getArt().equals("Planet"))
				throw new RuntimeException("Karte setSpielerstart: Spieler duerfen nur auf Planeten starten!");
			if (getFeld(pos).getArt().equals("Kueste")||getFeld(pos).getArt().equals("Meer"))
				throw new RuntimeException("Karte setSpielerstart: Spieler duerfen nicht auf Wasser starten!");
		}
		getFeld(pos).setSpielerstart(spielerstart);
	}
	
	public int[] getSpielerstart(int spielerstart) {
		if (!getArt().equals("Planet")) return null;
		for(int i=1;i<=d_Karte.getInt("x");i++){
			for(int j=1;j<=d_Karte.getInt("y");j++){
				if (getFeld(i,j).getDaten().getInt("spielerstart")==spielerstart){
					return new int[]{i,j};
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString(){
		return d_Karte.toString();
	}
	
	// hole alle Updates dieser Karte fuer diesen Spieler aus dem serverseitigen Updater-FiFo 
	public ArrayList<D> getUpdates(int idSpieler) {
		if (!updater.hatSpieler(idSpieler)) updater.addSpieler(idSpieler);
		return updater.get(idSpieler);
	}
	
	public void setUpdate(ArrayList<D> felddaten,int idSpieler) {
		updater.putFelddaten(felddaten,idSpieler);
	}
}
