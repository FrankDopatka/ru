package frontend.menu;

import frontend.Frontend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public abstract class MenuTop extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	protected Frontend frontend;
	protected int spalten;
	protected int zeilen;

	public MenuTop(Frontend frontend){
		this.frontend=frontend;
		spalten=6;
		zeilen=4;
	}
	
	@Override
	public abstract void actionPerformed(ActionEvent ev);
}