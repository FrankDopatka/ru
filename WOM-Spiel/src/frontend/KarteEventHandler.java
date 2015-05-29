package frontend;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import frontend.menu.MenuTop;

public class KarteEventHandler implements MouseListener,MouseMotionListener{
	protected Frontend frontend;
	
	public KarteEventHandler(Frontend frontend) {
		this.frontend=frontend;
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		Feld feld=(Feld)ev.getSource();
		aktion(feld);
	}
	
	private void aktion(Feld feld){
		MenuTop menu=frontend.getMenuTop();
		ActionEvent ev=new ActionEvent(feld,0,null);
		menu.actionPerformed(ev);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}