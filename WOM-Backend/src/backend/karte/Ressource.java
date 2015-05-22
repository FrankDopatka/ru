package backend.karte;

import daten.D_RessourcenArt;

public class Ressource{
	private D_RessourcenArt d_Ressource=new D_RessourcenArt();
	
	public Ressource(String art){
		setArt(art);
	}
	
	public void setArt(String art) {
		d_Ressource.setString("ressourcenArt",art);
	}

	public String getArt() {
		return d_Ressource.getString("ressourcenArt");
	}
	
	@Override
	public String toString(){
		return getArt();
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Ressource)) return false;
		Ressource res=(Ressource)o;
		return res.getArt().equals(this.getArt());
	}
}
