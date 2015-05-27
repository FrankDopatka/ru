package backend;

import java.util.ArrayList;

import backend.spiel.Einheit;
import backend.spiel.Spiel;

public class Regelwerk {
	public static final ArrayList<String> landeinheiten=new ArrayList<String>();
	public Spiel spiel=null;
	
	static{
		landeinheiten.add("Siedler");
		landeinheiten.add("Krieger");
	}
	
	public Regelwerk(Spiel spiel){
		this.spiel=spiel;
	}

	public boolean istLandeinheit(Einheit einheit){
		String art=einheit.getDaten().getString("einheitArt");
		return landeinheiten.contains(art);
	}
	
}
