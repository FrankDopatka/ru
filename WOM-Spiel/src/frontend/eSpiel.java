package frontend;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import frontend.menu.MenuSpiel;

public class eSpiel extends iEventhandler{
	
	public eSpiel(Frontend frontend) {
		super(frontend);
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		Feld feld=(Feld)ev.getSource();
		aktion(feld);
	}
	
	private void aktion(Feld feld){
		MenuSpiel menu=((MenuSpiel)frontend.getMenuTop());
		ActionEvent ev=new ActionEvent(feld,0,null);
		menu.actionPerformed(ev);
	}
}