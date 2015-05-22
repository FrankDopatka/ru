package frontend;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class iEventhandler implements MouseListener,MouseMotionListener{
	protected Frontend frontend;
	
	public iEventhandler(Frontend frontend){
		this.frontend=frontend;
	}
	
	@Override
	public void mouseDragged(MouseEvent ev) {}

	@Override
	public void mouseMoved(MouseEvent ev) {}

	@Override
	public void mouseClicked(MouseEvent ev) {}

	@Override
	public void mouseEntered(MouseEvent ev) {}

	@Override
	public void mouseExited(MouseEvent ev) {}

	@Override
	public void mousePressed(MouseEvent ev) {}

	@Override
	public void mouseReleased(MouseEvent ev) {}
}
