package backend;

import interfaces.iBackendKarteneditor;

import java.io.FileWriter;
import java.io.PrintWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import daten.*;
import backend.karte.Feld;
import backend.karte.Karte;

@Path("ru/karteneditor")
public class BackendKarteneditor implements iBackendKarteneditor{
	private static Karte karte;
	
	public BackendKarteneditor(){
	}
	
	@GET
	@Path("neueKarte/{id}/{x}/{y}/{kartenArt}/{feldArt}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String neueKarte(
			@PathParam("id")int id,
			@PathParam("x")int x,
			@PathParam("y")int y,
			@PathParam("kartenArt")String kartenArt,
			@PathParam("feldArt")String feldArt){
		try {
			@SuppressWarnings("unchecked")
			Class<Karte> c=(Class<Karte>)Class.forName(Parameter.pfadKlassenKarten+kartenArt);
			Karte karte=(Karte)c.newInstance();
			karte.setId(id);
			karte.setGroesse(new int[]{x,y});
			karte.setAlleFelder(feldArt);
			BackendKarteneditor.karte=karte;
			return getKarte();
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("speichernKarte/{pfad}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String speichernKarte(
			@PathParam("pfad") String pfad){
		PrintWriter pw=null;
		try {
			if (!pfad.endsWith(".map")) pfad=pfad+".map";
			if (karte==null) throw new RuntimeException("BackendKarteneditor speichernKarte: Es existiert noch keine Karte!");
			pw=new PrintWriter(new FileWriter(pfad));
			pw.println(Xml.verpacken(karte.toXml()));
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
		finally{
			try{
				pw.close();							
			} catch (Exception e){}
		}
	}
	
	@GET
	@Path("ladenKarte/{pfad}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String ladenKarte(
			@PathParam("pfad") String pfad){
		try{
			String karteXML=Karte.karteLaden(pfad);
			karte=Karte.karteVonXml(karteXML);
			return karteXML;
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}		
	}
	
	@GET
	@Path("getKarte")
	@Produces("application/xml")
	@Override
	public String getKarte() {
		try{
			return Xml.verpacken(karte.toXml());
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
	@Path("getKartenDaten")
	@Produces("application/xml")
	@Override
	public String getKartenDaten() {
		try{
			if (karte==null) throw new RuntimeException("BackendKarteneditor getKartenDaten: Es existiert noch keine Karte!");
			return Xml.verpacken(Xml.fromD(karte.getDaten()));
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("getFeldDaten/{x}/{y}")
	@Produces("application/xml")
	@Override
	public String getFeldDaten(
			@PathParam("x") int x,
			@PathParam("y") int y) {
		try{
			return Xml.verpacken(Xml.fromD(karte.getFeld(new int[]{x,y}).getDaten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("setFeldArt/{x}/{y}/{feldArtNeu}")
	@Produces("application/xml")
	@Override
	public String setFeldArt(
			@PathParam("x") int x,
			@PathParam("y") int y,
			@PathParam("feldArtNeu") String feldArtNeu) {
		try{
			String ressource=karte.getFeld(new int[]{x,y}).getDaten().getString("ressource");
			if (!Xml.toD(getErlaubteRessourcenArten(feldArtNeu)).existValue(ressource)) ressource="";
			int spielerstart=karte.getFeld(new int[]{x,y}).getDaten().getInt("spielerstart");
			if (spielerstart>0){
				if (feldArtNeu.equals("Kueste")||feldArtNeu.equals("Meer")) spielerstart=0;
			}
			karte.setFeld(karte.getId(),new int[]{x,y},feldArtNeu,ressource,spielerstart);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("setSpielerstart/{x}/{y}/{spielerstart}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String setSpielerstart(
			@PathParam("x") int x,
			@PathParam("y") int y,
			@PathParam("spielerstart") int spielerstart) {
		try{
			karte.setSpielerstart(new int[]{x,y},spielerstart);		
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getSpielerstart/{spielerstart}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getSpielerstart(
			@PathParam("spielerstart") int spielerstart) {
		try{
			int[] pos=karte.getSpielerstart(spielerstart);
			D_Position datenPos=new D_Position();
			if (pos==null){
				datenPos.setInt("x",0);
				datenPos.setInt("y",0);			
			}
			else{
				datenPos.setInt("x",pos[0]);
				datenPos.setInt("y",pos[1]);			
			}
			return Xml.verpacken(Xml.fromD(datenPos));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("setRessource/{x}/{y}/{ressorce}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String setRessource(
			@PathParam("x") int x,
			@PathParam("y") int y,
			@PathParam("ressorce") String ressorce) {
		try{
			karte.getFeld(new int[]{x,y}).setRessource(ressorce);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("delRessource/{x}/{y}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String delRessource(
			@PathParam("x") int x,
			@PathParam("y") int y) {
		try{
			karte.getFeld(new int[]{x,y}).setRessource(null);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
}
