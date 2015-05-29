package backend;

import interfaces.iBackendSpiel;

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
	public String getAlleSpieler() {
		return getXmlvonRest("getAlleSpieler");
	}

	@Override
	public String beendenRunde(int idSpieler) {
		return getXmlvonRest("beendenRunde"+"/"+idSpieler);
	}

	@Override
	public String getKarte(int id) {
		return getXmlvonRest("getKarte"+"/"+id);
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
}
