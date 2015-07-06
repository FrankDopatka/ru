package backend.spiel;

import daten.D_Stadt;
import daten.Xml;

public class Stadt {
	private Spiel spiel;
	private D_Stadt d_stadt=new D_Stadt();
	
	public Stadt(Spiel spiel){
		this.spiel=spiel;
	}

	public Stadt(Spiel spiel,D_Stadt daten){
		this(spiel);
		d_stadt=daten;
	}

	public D_Stadt getDaten(){
		return d_stadt;
	}
	
	public Object toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_stadt));
		return xml;
	}

	public void setProduktion(String zuProduzieren) {
		if (spiel.getRegelwerk().istEinheit(zuProduzieren)){
			d_stadt.setString("produziere",zuProduzieren);
			d_stadt.setInt("bereitsProduziert",0);
		}
		else
			throw new RuntimeException("Stadtproduktion von "+zuProduzieren+" ist nicht erlaubt!");
	}
}
