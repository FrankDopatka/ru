package backend;
import interfaces.iBackendSpiel;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.glassfish.jersey.server.ResourceConfig;

import daten.*;
import backend.karte.Feld;
import backend.spiel.*;

@Path("ru/spiel")
public class BackendSpiel extends ResourceConfig implements iBackendSpiel{
	private static Spiel spiel;
	
	public static Spiel getSpiel(){
		return BackendSpiel.spiel;
	}
	
	public static void setSpiel(Spiel spiel){
		BackendSpiel.spiel=spiel;
	}
	
	public BackendSpiel(){
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
	@Path("beendenRunde/{idSpieler}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String beendenRunde(
			@PathParam("idSpieler")int idSpieler) {
		try{
			if (spiel.getSpielerAmZug()!=idSpieler)
				throw new RuntimeException("BackendSpiel beendenRunde: Sie sind gar nicht am Zug! Sie koennen die Runde nicht beenden!");
			spiel.incSpielerAmZug();
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getKarte/{idKarte}/{idSpieler}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getKarte(
			@PathParam("idKarte")int idKarte,
			@PathParam("idSpieler")int idSpieler) {
		try{
			spiel.resetUpdates(idSpieler);
			return Xml.verpacken(spiel.getKarte(idKarte).toXml());
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("getKartenUmgebung/{idKarte}/{idSpieler}/{x}/{y}/{reichweite}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getKartenUmgebung(
			@PathParam("idKarte")int idKarte,
			@PathParam("idSpieler")int idSpieler,
			@PathParam("x")int x,
			@PathParam("y")int y,
			@PathParam("reichweite")int reichweite) {
		try{
			return Xml.verpacken(spiel.getKarte(idKarte).toXml(x,y,reichweite));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("getKartenDaten/{idKarte}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getKartenDaten(
			@PathParam("idKarte")int idKarte){
		try{
			return Xml.verpacken(Xml.fromD(spiel.getKarte(idKarte).getDaten()));
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
	@Path("getProduzierbareEinheiten/{idSpieler}/{idStadt}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getProduzierbareEinheiten(
			@PathParam("idSpieler")int idSpieler,
			@PathParam("idStadt")int idStadt) {
		ArrayList<D> daten=spiel.getRegelwerk().getProduzierbareEinheiten(idSpieler,idStadt);
		return Xml.verpacken(Xml.fromArray(daten));
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
			// Updates des Spiels selbst und der geladenen Karte zusammengefuehrt zurueckgeben
			ArrayList<D> daten=spiel.getUpdates(idSpieler,idKarte); 
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
	@Path("produziere/{idSpieler}/{idStadt}/{zuProduzieren}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String produziere(
			@PathParam("idSpieler")int idSpieler, 
			@PathParam("idStadt")int idStadt, 
			@PathParam("zuProduzieren")String zuProduzieren) {
		try {
			Stadt stadt=spiel.getSpieler(idSpieler).getStadt(idStadt);
			stadt.setProduktion(zuProduzieren);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getAngriffsRadius/{idSpieler}/{idKarte}/{x}/{y}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getAngriffsRadius(
			@PathParam("idSpieler")int idSpieler,
			@PathParam("idKarte")int idKarte,
			@PathParam("x")int x, 
			@PathParam("y")int y) {
		try {
			Feld feld=spiel.getKarte(idKarte).getFeld(x,y);
			Einheit einheit=feld.getEinheit();
			if (einheit==null)
				throw new RuntimeException("Auf dem Feld "+x+"/"+y+" befindet sich keine Einheit!");
			if (einheit.getIdSpieler()!=idSpieler)
				throw new RuntimeException("Dies ist nicht Ihre Einheit!");
			
			if (einheit.getDaten().getInt("angriffAktuell")==0)
				return Xml.verpacken(spiel.getKarte(idKarte).toXml(x,y,0));
			else if (!einheit.istFernkampfeinheit())
				return Xml.verpacken(spiel.getKarte(idKarte).toXml(x,y,1));	
			else{
				int reichweite=einheit.getDaten().getInt("reichweiteFernkampf");
				return Xml.verpacken(spiel.getKarte(idKarte).toXml(x,y,reichweite));		
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("fernangriff/{idSpieler}/{idKarte}/{xAngreifer}/{yAngreifer}/{xVerteidiger}/{yVerteidiger}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String fernangriff(
			@PathParam("idSpieler")int idSpieler,
			@PathParam("idKarte")int idKarte,
			@PathParam("xAngreifer")int xAngreifer,
			@PathParam("yAngreifer")int yAngreifer,
			@PathParam("xVerteidiger")int xVerteidiger,
			@PathParam("yVerteidiger")int yVerteidiger) {
		try{
			spiel.fernkampf(idSpieler,idKarte,xAngreifer,yAngreifer,xVerteidiger,yVerteidiger);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getSpieldaten")
	@Produces("application/xml")
	@Override
	public String getSpieldaten() {
		try{
			return Xml.verpacken(Xml.fromD(spiel.getDaten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
}
