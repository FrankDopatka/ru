package backend;

import interfaces.iBackendSpielAdmin;

import java.io.FileWriter;
import java.io.PrintWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.glassfish.jersey.server.ResourceConfig;

import backend.karte.Karte;
import backend.spiel.Spiel;
import backend.spiel.Spieler;
import daten.D_Fehler;
import daten.D_OK;
import daten.D_SpielerNationArt;
import daten.D_SpielerRassenArt;
import daten.Xml;

@Path("ru/spiel/admin")
public class BackendSpielAdmin extends ResourceConfig implements iBackendSpielAdmin{

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
			BackendSpiel.setSpiel(new Spiel(id,anzahlSpieler,anzahlKarten));
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
			BackendSpiel.getSpiel().addKarte(karte);
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
			Spiel spiel=BackendSpiel.getSpiel();
			Spieler spieler=new Spieler(spiel,id,name,rasse,nation);
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
	@Path("starteSpiel")
	@Produces("application/xml")
	@Override
	public String starteSpiel() {
		try {
			BackendSpiel.getSpiel().starten();
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
			pw.println(Xml.verpacken(BackendSpiel.getSpiel().toXml()));
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
			BackendSpiel.setSpiel(Spiel.spielLaden(pfad));
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}		
	}
}
