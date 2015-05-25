package backend;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.glassfish.jersey.server.ResourceConfig;

import daten.*;
import backend.karte.Feld;
import backend.karte.Karte;
import backend.spiel.Einheit;
import backend.spiel.Spiel;
import backend.spiel.Spieler;
import backend.spiel.Stadt;

@Path("wom/spiel")
public class BackendSpiel extends ResourceConfig implements iBackendSpiel{
	private static Spiel spiel;
	
	public BackendSpiel(){
	}
	
	@GET
	@Path("neuesSpiel/{id}/{anzahlSpieler}/{anzahlKarten}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String neuesSpiel(
			@PathParam("id")int id,
			@PathParam("anzahlSpieler")int anzahlSpieler,
			@PathParam("anzahlKarten")int anzahlKarten){
		try {
			spiel=new Spiel(id,anzahlSpieler,anzahlKarten);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("hinzufuegenKarte/{pfad}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String hinzufuegenKarte(
			@PathParam("pfad") String pfad){
		try{
			String karteXML=Karte.karteLaden(pfad);
			Karte karte=Karte.karteVonXml(karteXML);
			spiel.addKarte(karte);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}		
	}
	
	@GET
	@Path("getRassen")
	@Produces("application/xml")
	@Override
	public String getRassen() {
		try{
			return Xml.verpacken(Xml.fromD(new D_SpielerRassenArt()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getNationsArten/{rasse}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getNationsArten(
			@PathParam("rasse")String rasse) {
		try{
			if (rasse.equals("Mensch"))
				return Xml.verpacken(Xml.fromD(new D_SpielerNationArt()));
			else
				return Xml.verpacken(Xml.fromD(new D_Fehler("Nur Menschen haben verschiedene Nationen")));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}	
	
	@GET
	@Path("hinzufuegenSpieler/{id}/{name}/{rasse}/{nation}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String hinzufuegenSpieler(
			@PathParam("id") int id,
			@PathParam("name") String name,
			@PathParam("rasse") String rasse,
			@PathParam("nation") String nation){
		try{
			Spieler spieler=new Spieler(id,name,rasse,nation);
			if (spiel.getDaten().getBool("istGestartet"))
				spiel.spielerEinbinden(spieler);
			else
				spiel.addSpieler(spieler);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}		
	}
	
	@GET
	@Path("getAlleSpieler")
	@Produces("application/xml")
	@Override
	public String getAlleSpieler() {
		try{
			ArrayList<Spieler> spieler=spiel.getAlleSpieler();
			ArrayList<D> d=new ArrayList<D>();
			for(Spieler s:spieler) d.add(s.getDaten());
			return Xml.verpacken(Xml.fromArray(d));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("starteSpiel")
	@Produces("application/xml")
	@Override
	public String starteSpiel() {
		try {
			spiel.starten();
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getKarte/{id}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getKarte(
			@PathParam("id")int id) {
		try{
			return Xml.verpacken(spiel.getKarte(id).toXml());
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("getKartenArten")
	@Produces("application/xml")
	@Override
	public String getKartenArten() {
		try{
			return Xml.verpacken(Xml.fromD(new D_KartenArt()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("getErlaubteFeldArten/{kartenArt}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getErlaubteFeldArten(
			@PathParam("kartenArt") String kartenArt) {
		try {
			@SuppressWarnings("unchecked")
			Class<Karte> c=(Class<Karte>)Class.forName(Parameter.pfadKlassenKarten+kartenArt);
			Karte karte=(Karte)c.newInstance();
			return Xml.verpacken(Xml.fromD(karte.getErlaubteFeldArten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("getErlaubteRessourcenArten/{feldArt}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getErlaubteRessourcenArten(
			@PathParam("feldArt") String feldArt) {
		try {
			@SuppressWarnings("unchecked")
			Class<Feld> c=(Class<Feld>)Class.forName(Parameter.pfadKlassenFelder+feldArt);
			Feld feld=(Feld)c.newInstance();
			return Xml.verpacken(Xml.fromD(feld.getErlaubteRessourcenArten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}	
	
	@GET
	@Path("getKartenDaten/{id}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getKartenDaten(
			@PathParam("id")int id){
		try{
			return Xml.verpacken(Xml.fromD(spiel.getKarte(id).getDaten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getFeldDaten/{idKarte}/{x}/{y}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getFeldDaten(
			@PathParam("idKarte")int idKarte,
			@PathParam("x")int x,
			@PathParam("y")int y){
		try{
			return Xml.verpacken(spiel.getKarte(idKarte).getFeld(x,y).toXml());
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("getEinheitDaten/{idKarte}/{x}/{y}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getEinheitDaten(
			@PathParam("idKarte")int idKarte,
			@PathParam("x")int x,
			@PathParam("y")int y){
		try{
			Einheit einheit=spiel.getKarte(idKarte).getFeld(x,y).getEinheit();
			if (einheit==null) throw new RuntimeException("Backend getEinheitDaten: Auf diesem Feld ist keine Einheit vorhanden!");
			return Xml.verpacken(Xml.fromD(einheit.getDaten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getStadtDaten/{idKarte}/{x}/{y}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getStadtDaten(
			@PathParam("idKarte")int idKarte,
			@PathParam("x")int x,
			@PathParam("y")int y){
		try{
			Stadt stadt=spiel.getKarte(idKarte).getFeld(x,y).getStadt();
			if (stadt==null) throw new RuntimeException("Backend getStadtDaten: Auf diesem Feld ist keine Stadt vorhanden!");
			return Xml.verpacken(Xml.fromD(stadt.getDaten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getSpielerDaten/{idSpieler}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getSpielerDaten(
			@PathParam("idSpieler")int idSpieler){
		try{
			Spieler spieler=spiel.getSpieler(idSpieler);
			if (spieler==null) throw new RuntimeException("Backend getSpielerDaten: Einen Spieler mit dieser ID gibt es nicht!");
			return Xml.verpacken(Xml.fromD(spieler.getDaten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("bewegeEinheit/{idSpieler}/{idKarte}/{x}/{y}/{richtung}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String bewegeEinheit(
			@PathParam("idSpieler")int idSpieler,
			@PathParam("idKarte")int idKarte,
			@PathParam("x")int x,
			@PathParam("y")int y,
			@PathParam("richtung")int richtung) {
		try{
			return Xml.verpacken(Xml.fromD(spiel.bewegeEinheit(idSpieler,idKarte,x,y,richtung)));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("gruendeStadt/{idSpieler}/{idKarte}/{x}/{y}/{name}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String gruendeStadt(
			@PathParam("idSpieler")int idSpieler, 
			@PathParam("idKarte")int idKarte, 
			@PathParam("x")int x, 
			@PathParam("y")int y, 
			@PathParam("name")String name) {
		try {
			spiel.gruendeStadt(idSpieler,idKarte,x,y,name);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("update/{idSpieler}/{idKarte}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String update(
			@PathParam("idSpieler")int idSpieler,
			@PathParam("idKarte")int idKarte){
		try {
			ArrayList<D> daten=spiel.getKarte(idKarte).getUpdates(idSpieler);
			if ((daten!=null)&&(daten.size()>0))
				return Xml.verpacken(Xml.fromArray(daten));
			else
				return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("speichernSpiel/{pfad}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String speichernSpiel(
			@PathParam("pfad")String pfad) {
		PrintWriter pw=null;
		try {
			if (!pfad.endsWith(".wom")) pfad=pfad+".wom";
			pw=new PrintWriter(new FileWriter(pfad));
			pw.println(Xml.verpacken(spiel.toXml()));
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
		finally{
			pw.close();			
		}
	}

	@GET
	@Path("ladenSpiel/{pfad}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String ladenSpiel(
			@PathParam("pfad")String pfad) {
		try{
			spiel=Spiel.spielLaden(pfad);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}		
	}
}
