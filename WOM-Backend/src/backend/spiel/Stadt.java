package backend.spiel;

import daten.D_Stadt;
import daten.Xml;

public class Stadt {
	private D_Stadt d_stadt=new D_Stadt();
	
	public D_Stadt getDaten(){
		return d_stadt;
	}

	public Object toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_stadt));
		return xml;
	}
}
