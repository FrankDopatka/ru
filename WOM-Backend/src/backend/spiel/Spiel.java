package backend.spiel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.ArrayList;

import daten.*;
import backend.Parameter;
import backend.karte.Feld;
import backend.karte.Karte;
import backend.karte.karten.Planet;
import backend.spiel.einheiten.Siedler;

public class Spiel {
	private D_Spiel d_Spiel=new D_Spiel();
	private ArrayList<Spieler> spieler=new ArrayList<Spieler>();
	private ArrayList<Karte> karten=new ArrayList<Karte>();
	
	public enum Bewegungsrichtung{
		NORD,NORDOST,OST,SUEDOST,SUED,SUEDWEST,WEST,NORDWEST;
		public static Bewegungsrichtung fromOrdinal(int n){
			return values()[n];
		}
	}
	
	public static Spiel spielLaden(String pfad) {
		BufferedReader br=null;
		try {
			pfad=URLDecoder.decode(""+pfad,"ISO-8859-1");
			StringBuffer spielXML=new StringBuffer();
			br=new BufferedReader(new FileReader(pfad));
			String zeile=br.readLine(); 
	    while (zeile!=null){
	    	spielXML.append(zeile+"/n");
	      zeile=br.readLine(); 
	    } 
	    ArrayList<D> spielDaten=Xml.toArray(spielXML.toString());
	    Spiel spiel=new Spiel();
	    int iDatensatz=0;
	    if ((spielDaten==null)||(spielDaten.size()==0)||(!(spielDaten.get(iDatensatz) instanceof D_Spiel)))
	    	throw new RuntimeException("Spiel spielLaden: Spieldaten D_Spiel im gespeicherten Spiel sind ungueltig!");
	    spiel.d_Spiel=(D_Spiel)spielDaten.get(iDatensatz);
    	iDatensatz++;
    	if (spielDaten.size()>iDatensatz){
  			try{
					while (spielDaten.get(iDatensatz) instanceof D_Karte){
						// Kartendaten sind vorhanden -> auslesen
						D_Karte dKarte=(D_Karte)spielDaten.get(iDatensatz);
						int x=dKarte.getInt("x");
						int y=dKarte.getInt("y");
						ArrayList<D> kartenDaten=new ArrayList<D>();
						kartenDaten.add(dKarte);
						iDatensatz++;
						int start=iDatensatz;
						for(int i=start;i<=start+(x*y)+1;i++){
							kartenDaten.add(spielDaten.get(i));
							iDatensatz++;
						}
						Karte karte=Karte.karteVonArray(kartenDaten);
						spiel.karten.add(karte);    				
					}
  			}
  			catch (Exception e){
  				throw new RuntimeException("Spiel spielLaden: Kartendaten D_Karte im gespeicherten Spiel sind ungueltig!");
  			}
				iDatensatz--;
				iDatensatz--;
				ArrayList<Spieler> spielerListe=new ArrayList<Spieler>(); 
  			while ((spielDaten.size()>iDatensatz)&&(spielDaten.get(iDatensatz)) instanceof D_Spieler){
  				// SPIELER
  				D_Spieler dSpieler=(D_Spieler)spielDaten.get(iDatensatz);
  				Spieler spieler=new Spieler(dSpieler);
  				iDatensatz++;
  				// EINHEITEN DES SPIELERS
					ArrayList<Einheit> einheiten=new ArrayList<Einheit>(); 
					while ((spielDaten.size()>iDatensatz)&&(spielDaten.get(iDatensatz)) instanceof D_Einheit){
						D_Einheit datenEinheit=(D_Einheit)spielDaten.get(iDatensatz);
						// Einheit generieren
						@SuppressWarnings("unchecked")
						Class<Einheit> c=(Class<Einheit>)Class.forName(Parameter.pfadKlassenEinheiten+datenEinheit.getString("einheitArt"));
						Einheit einheit=(Einheit)c.newInstance();
						einheit.setDaten(datenEinheit);
						einheiten.add(einheit);
						// Einheit auf dem Feld plazieren
						Karte karte=spiel.getKarte(datenEinheit.getInt("idKarte"));
						Feld feld=karte.getFeld(datenEinheit.getInt("x"),datenEinheit.getInt("y"));
						feld.setEinheit(einheit);
						iDatensatz++;
					}
					// Einheiten dem Spieler mitgeben
					spieler.setEinheiten(einheiten);
					// STAEDTE DES SPIELERS
					ArrayList<Stadt> stadte=new ArrayList<Stadt>(); 
					while ((spielDaten.size()>iDatensatz)&&(spielDaten.get(iDatensatz)) instanceof D_Stadt){
						// Stadt generieren
						D_Stadt datenStadt=(D_Stadt)spielDaten.get(iDatensatz);
						Stadt stadt=new Stadt(datenStadt);
						stadte.add(stadt);
						// Stadt auf dem Feld plazieren
						Karte karte=spiel.getKarte(datenStadt.getInt("idKarte"));
						Feld feld=karte.getFeld(datenStadt.getInt("x"),datenStadt.getInt("y"));
						feld.setStadt(stadt);
						iDatensatz++;
					}
					spieler.setStadte(stadte);
					// SPIELER DEM SPIEL HINZUFUEGEN
  				spielerListe.add(spieler);
				}
  			// SPIELERLISTE AN DAS SPIEL UEBERGEBEN
  			spiel.setSpieler(spielerListe);
    	}
	    return spiel;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spiel spielLaden:"+e.getMessage());
		}
		finally{
			try {
				br.close();
			} catch (Exception e) {}			
		}
	}
	
	public Spiel(){
	}
	
	public Spiel(int id,int spielerMax,int kartenMax){
		if (id<1)
			throw new RuntimeException("Spiel Konstruktor: Wert für die ID ist ungueltig!");
		if (spielerMax<1)
			throw new RuntimeException("Spiel Konstruktor: Wert für spielerMax ist ungueltig!");
		if (kartenMax<1)
			throw new RuntimeException("Spiel Konstruktor: Wert für kartenMax ist ungueltig!");
		d_Spiel.setInt("id",id);
		d_Spiel.setInt("spielerMax",spielerMax);
		d_Spiel.setInt("kartenMax",kartenMax);
	}

	public void addKarte(Karte karteNeu){
		if (d_Spiel.getBool("istGestartet"))
			throw new RuntimeException("Spiel addKarte: Eine neue Karte kann nicht ins laufende Spiel integriert werden!");			
		if (karten.contains(karteNeu)){
			throw new RuntimeException("Spiel addKarte: Eine Karte mit dieser ID ist bereits im Spiel!");			
		}
		if ((d_Spiel.getInt("kartenMax")!=0)&&(karten.size()==d_Spiel.getInt("kartenMax")))
			throw new RuntimeException("Spiel addKarte: Das Hinzufuegen einer weiteren Karte ist nicht erlaubt!");
		karten.add(karteNeu);
		d_Spiel.incInt("kartenAnzahl");
	}
	
	// Spieler hinzufuegen, bevor Spiel gestartet ist
	public void addSpieler(Spieler spielerNeu){ 
		if (spieler.contains(spielerNeu)){
			throw new RuntimeException("Spiel addSpieler: Ein Spieler mit dieser ID ist bereits im Spiel!");			
		}
		if ((d_Spiel.getInt("spielerMax")!=0)&&(spieler.size()==d_Spiel.getInt("spielerMax")))
			throw new RuntimeException("Spiel addSpieler: Das Hinzufuegen eines weiteren Spielers ist nicht erlaubt!");
		if (spielerNeu.getId()<=0){
			spielerNeu.setId(getNaechsteSpielerId());
		}
		spieler.add(spielerNeu);
		d_Spiel.incInt("spielerAnzahl");
	}
	public void setSpieler(ArrayList<Spieler> spieler){
		this.spieler=spieler;
	}

	public int getId(){
		return d_Spiel.getInt("id");
	}
	
	public D_Spiel getDaten(){
		return d_Spiel;
	}

	public Karte getKarte(int id) {
		for(Karte karte:karten){
			if (karte.getDaten().getInt("id")==id) return karte;
		}
		return null;
	}
	
	public int getNaechsteSpielerId(){
		if ((spieler==null)||(spieler.size()==0)) return 1;
		int x=1;
		boolean bereitsGesetzt=false;
		do{
			bereitsGesetzt=false;
			for(Spieler s:spieler){
				int id=s.getId();
				if (id==x){
					x++;
					bereitsGesetzt=true;
					break;
				}
			}
		} while (bereitsGesetzt);
		return x;
	}
	
	public ArrayList<Spieler> getAlleSpieler(){
		return spieler;
	}

	public boolean istGestartet() {
		return d_Spiel.getBool("istGestartet");
	}
	
	public void starten(){
		if (istGestartet()) throw new RuntimeException("Spiel starten: Das Spiel ist bereits gestartet!");
		if (spieler.size()==0) throw new RuntimeException("Spiel starten: Das Spiel hat noch keine Spieler!");
		if (karten.size()==0) throw new RuntimeException("Spiel starten: Das Spiel hat noch keine Karten!");
		try{
			for(Spieler s:spieler) spielerEinbinden(s);			
		}
		catch (Exception e){
			for(Spieler s:spieler) s.reset();
			d_Spiel.setInt("spielerAnzahl",0);
			throw e;
		}
		d_Spiel.setBool("istGestartet",true);
		d_Spiel.setInt("aktuelleRunde",1);
		d_Spiel.setInt("spielerAmZug",spieler.get(0).getDaten().getInt("id"));
	}
	
	// Spieler ins laufende Spiel einbinden
	public void spielerEinbinden(Spieler spielerNeu){
		if (!spieler.contains(spielerNeu)) addSpieler(spielerNeu);
		Karte karte=null;
		int[] pos=null;
		for (Karte k:karten){
			if (k instanceof Planet){
				pos=k.getSpielerstart(spielerNeu.getDaten().getInt("id"));
				if (pos!=null){
					karte=k;
					break;
				}
			}
		}
		if (pos==null){
			d_Spiel.decInt("spielerAnzahl");
			throw new RuntimeException("Spiel spielerEinbinden: Auf keiner Karte dieses Spiels ist eine Startposition fuer diesen Spieler mit ID "+spielerNeu.getDaten().getInt("id")+" vorgesehen!");			
		}
		Siedler siedler=(Siedler)spielerNeu.addEinheit("Siedler",karte.getFeld(pos));
		karte.getFeld(pos).setEinheit(siedler);
	}

	public D_Position bewegeEinheit(int idSpieler,int idKarte,int feldX,int feldY,int richtung) {
		// idSpieler: Spieler der gerade ziehen will
		Karte karte=getKarte(idKarte);
		Feld feldAlt=karte.getFeld(feldX,feldY);
		Einheit einheit=feldAlt.getEinheit();
		int feldXneu=feldX;
		int feldYneu=feldY;
		switch (Bewegungsrichtung.fromOrdinal(richtung)){
		case NORD:
			feldYneu--;
			break;
		case NORDOST:
			feldXneu++;
			feldYneu--;
			break;
		case OST:
			feldXneu++;
			break;
		case SUEDOST:
			feldXneu++;
			feldYneu++;
			break;
		case SUED:
			feldYneu++;
			break;
		case SUEDWEST:
			feldXneu--;
			feldYneu++;
			break;
		case WEST:
			feldXneu--;
			break;
		case NORDWEST:
			feldXneu--;
			feldYneu--;
			break;
		}
		if((feldYneu<1)||(feldYneu>karte.getGroesseY()))
			throw new RuntimeException("Spiel bewegeEinheit: Man kann den Kartenrand nicht verlassen!");
		if (feldXneu<1) feldXneu=karte.getGroesseX();
		if (feldXneu>karte.getGroesseX()) feldXneu=1;
		Feld feldNeu=karte.getFeld(feldXneu,feldYneu);
		feldAlt.setEinheit(null);
		feldNeu.setEinheit(einheit);
		D_Position posNeu=new D_Position();
		posNeu.setInt("x",feldXneu);
		posNeu.setInt("y",feldYneu);
		return posNeu;
	}

	public String toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_Spiel));
		for(Karte karte:karten){
			xml.append(karte.toXml());			
		}
		for(Spieler spieler:spieler){
			xml.append(spieler.toXml());			
		}
		return xml.toString();
	}
}
