package backend;

import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class BackendSpielStub implements iBackendSpiel{
	private String url;
	private Client client=ClientBuilder.newClient();
	
	public BackendSpielStub(String url){
		if (url.endsWith("/"))
			this.url=url+"wom/spiel/";
		else
			this.url=url+"/wom/spiel/";
	}
	
	private String getXmlvonRest(String pfad){
		String s=client.target(url+pfad).request().accept("application/xml").get(String.class);
		return s;
	}

	@Override
	public String neuesSpiel(int id,int anzahlSpieler,int anzahlKarten) {
		return getXmlvonRest("neuesSpiel"+"/"+id+"/"+anzahlSpieler+"/"+anzahlKarten);
	}
	
	@Override
	public String hinzufuegenKarte(String pfad) {
		try {
			return getXmlvonRest("hinzufuegenKarte"+"/"+URLEncoder.encode(""+pfad,"ISO-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getRassen() {
		return getXmlvonRest("getRassen");
	}
	
	@Override
	public String getNationsArten(String rasse) {
		return getXmlvonRest("getNationsArten"+"/"+rasse);
	}

	@Override
	public String hinzufuegenSpieler(int id,String name,String rasse,String nation) {
		return getXmlvonRest("hinzufuegenSpieler"+"/"+id+"/"+name+"/"+rasse+"/"+nation);
	}
	
	@Override
	public String getAlleSpieler() {
		return getXmlvonRest("getAlleSpieler");
	}
	
	@Override
	public String starteSpiel() {
		return getXmlvonRest("starteSpiel");
	}


	@Override
	public String getKarte(int id) {
		return getXmlvonRest("getKarte"+"/"+id);
	}

	@Override
	public String getKartenArten() {
		return getXmlvonRest("getKartenArten");
	}

	@Override
	public String getErlaubteFeldArten(String kartenArt) {
		return getXmlvonRest("getErlaubteFeldArten"+"/"+kartenArt);
	}

	@Override
	public String getErlaubteRessourcenArten(String feldArt) {
		return getXmlvonRest("getErlaubteRessourcenArten"+"/"+feldArt);
	}
	
	@Override
	public String getKartenDaten(int id) {
		return getXmlvonRest("getKartenDaten"+"/"+id);
	}

	@Override
	public String getFeldDaten(int idKarte, int x, int y) {
		return getXmlvonRest("getFeldDaten"+"/"+idKarte+"/"+x+"/"+y);
	}
	
	@Override
	public String getEinheitDaten(int idKarte, int x, int y) {
		return getXmlvonRest("getEinheitDaten"+"/"+idKarte+"/"+x+"/"+y);
	}

	@Override
	public String getStadtDaten(int idKarte, int x, int y) {
		return getXmlvonRest("getStadtDaten"+"/"+idKarte+"/"+x+"/"+y);
	}

	@Override
	public String getSpielerDaten(int idSpieler) {
		return getXmlvonRest("getSpielerDaten"+"/"+idSpieler);
	}
	
	@Override
	public String bewegeEinheit(int idSpieler, int idKarte, int x, int y, int richtung) {
		return getXmlvonRest("bewegeEinheit"+"/"+idSpieler+"/"+idKarte+"/"+x+"/"+y+"/"+richtung);
	}
	
	@Override
	public String gruendeStadt(int idSpieler, int idKarte, int x, int y, String name) {
		return getXmlvonRest("gruendeStadt"+"/"+idSpieler+"/"+idKarte+"/"+x+"/"+y+"/"+name);
	}
	
	@Override
	public String update(int idSpieler,int idKarte) {
		return getXmlvonRest("update"+"/"+idSpieler+"/"+idKarte);
	}

	@Override
	public String speichernSpiel(String pfad) {
		try {
			URLEncoder.encode(""+pfad,"ISO-8859-1");
			return getXmlvonRest("speichernSpiel"+"/"+URLEncoder.encode(""+pfad,"ISO-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String ladenSpiel(String pfad) {
		try {
			URLEncoder.encode(""+pfad,"ISO-8859-1");
			return getXmlvonRest("ladenSpiel"+"/"+URLEncoder.encode(""+pfad,"ISO-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
