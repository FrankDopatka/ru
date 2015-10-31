package backend;

import interfaces.iBackendSpielAdmin;

import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class BackendSpielAdminStub implements iBackendSpielAdmin{
	private String url;
	private Client client=ClientBuilder.newClient();
	
	public BackendSpielAdminStub(String url){
		if (url.endsWith("/"))
			this.url=url+"ru/spiel/admin/";
		else
			this.url=url+"/ru/spiel/admin/";
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
	public String starteSpiel() {
		return getXmlvonRest("starteSpiel");
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
