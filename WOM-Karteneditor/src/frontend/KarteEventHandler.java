package frontend;

import interfaces.iBackendKarteneditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import daten.*;
import frontend.menu.MenuRechts;
import frontend.menu.MenuRechts.AktionMenuRechts;

public class KarteEventHandler implements MouseListener,MouseMotionListener{
	private Frontend frontend;
	private iBackendKarteneditor backendKarteneditor;
	private boolean dragging=false;
	
	
	public KarteEventHandler(Frontend frontend) {
		this.frontend=frontend;
		this.backendKarteneditor=frontend.getBackendKarteneditor();
	}

	@Override
	public void mouseMoved(MouseEvent ev) {
		Feld feld=(Feld)ev.getSource();
		frontend.setStatus(feld.toString());
	}

	@Override
	public void mouseDragged(MouseEvent ev) {
		Feld feld=(Feld)ev.getSource();
		aktion(feld);
		dragging=true;
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
		Feld feld=(Feld)ev.getSource();
		if (dragging) aktion(feld);
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		dragging=false;
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		Feld feld=(Feld)ev.getSource();
		aktion(feld);
	}
	
	private void aktion(Feld feld){
		AktionMenuRechts a=frontend.getMenuRechts().getAktion();
		switch (a){
		case FeldArtSetzen:
			feldSetzen(feld);
			break;
		case RessourceSetzen:
			ressourceSetzen(feld);
			break;
		case RessourceLoeschen:
			ressourceLoeschen(feld);
			break;
		case SpielerstartSetzen:
			spielerstartSetzen(feld);
			break;
		}
	}

	private void feldSetzen(Feld feld){
		MenuRechts menu=frontend.getMenuRechts();
		String fArtNeu=menu.getFeldart();
		backendKarteneditor.setFeldArt(feld.getPos()[0],feld.getPos()[1],fArtNeu);
		feld.zeichnen();
	}
	
	private void ressourceSetzen(Feld feld){
		dragging=false;
		D_Feld feldDaten=(D_Feld)Xml.toD(backendKarteneditor.getFeldDaten(feld.getPos()[0],feld.getPos()[1]));
		String feldArt=feldDaten.getString("feldArt");
		D_RessourcenArt feldRessourcenArten=(D_RessourcenArt)Xml.toD(backendKarteneditor.getErlaubteRessourcenArten(feldArt));
		ArrayList<String> feldRessourcenArtenEnum=feldRessourcenArten.getListe();
		Object[] optionen=new Object[feldRessourcenArtenEnum.size()];
		for (int i=0;i<feldRessourcenArtenEnum.size();i++){
			optionen[i]=feldRessourcenArtenEnum.get(i);
		}
		if (optionen.length==0) return;
	  int gewaehlt=JOptionPane.showOptionDialog(frontend,
	  		"Setzen Sie die Resource...", 
	  		"Resource fuer "+feld.getPosX()+"/"+feld.getPosY()+" vom Typ "+feldArt,
	    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
	    null, optionen, optionen[0]);
	  if (gewaehlt==-1) return;
	  backendKarteneditor.setRessource(feld.getPos()[0],feld.getPos()[1],""+optionen[gewaehlt]);
	  feld.zeichnen();
	}

	private void ressourceLoeschen(Feld feld){
		dragging=false;
		backendKarteneditor.delRessource(feld.getPos()[0],feld.getPos()[1]);
		feld.zeichnen();
	}
	
	private void spielerstartSetzen(Feld feld){
		dragging=false;
		MenuRechts menu=frontend.getMenuRechts();
		try{
			int spielernummer=menu.getSpielernummer();
			D_Position posAlt=(D_Position)Xml.toD(backendKarteneditor.getSpielerstart(spielernummer));
			int x=posAlt.getInt("x");
			int y=posAlt.getInt("y");
			if ((posAlt.getInt("x")!=0)&&(posAlt.getInt("y")!=0)){
				backendKarteneditor.setSpielerstart(x,y,0);
				frontend.getKarte().zeichneFeld(new int[]{x,y});
			}
			backendKarteneditor.setSpielerstart(feld.getPos()[0],feld.getPos()[1],spielernummer);
		}
		catch(RuntimeException e){
			frontend.log("FEHLER:"+e.getMessage());
		}
		feld.zeichnen();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}