package backend;

import java.net.URLEncoder;

import com.sun.jersey.api.client.Client;

public class BackendSpielStub implements iBackendSpiel{
	private String url;
	private Client client=Client.create();
	
	public BackendSpielStub(String url){
		if (url.endsWith("/"))
			this.url=url+"wom/spiel/";
		else
			this.url=url+"/wom/spiel/";
	}
	
	private String getXmlvonRest(String pfad){
		String s=client.resource(url+pfad).accept("application/xml").get(String.class);
//		System.out.println(s);
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
	public String bewegeEinheit(int idSpieler, int idKarte, int feldX, int feldY, int richtung) {
		return getXmlvonRest("bewegeEinheit"+"/"+idSpieler+"/"+idKarte+"/"+feldX+"/"+feldY+"/"+richtung);
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
