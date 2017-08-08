package utilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JList;

public class popClickListener extends MouseAdapter
{
	private boolean leftMouse=false;
	
	public popClickListener() 
	{

	}

	public void mousePressed(MouseEvent e)
	{
		//left mouse click
		if (e.getButton()==MouseEvent.BUTTON1) 
			leftMouse=true;
		else
			leftMouse=false;
	}
	
	public boolean getLeftMouse(){
		return leftMouse;
	}
	
	public void doPop(MouseEvent e){
	
	}
}