package frontend.menu;

import frontend.Frontend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public abstract class MenuRechts extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	protected Frontend frontend;

	public MenuRechts(Frontend frontend){
		this.frontend=frontend;
	}
	
	@Override
	public abstract void actionPerformed(ActionEvent ev);
}