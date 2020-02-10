package lyons.aaron.ID201145084.tesselations;
import javax.swing.*;

import java.awt.*;


public class MyJButton extends JButton{
	private String lightGrey = "#4d4b4b";        
    private String darkGrey = "#232323";
    private String mediumGrey = "#404040";
	private String defaultColour = lightGrey;
	private String activeColour = darkGrey;
	
	
	public MyJButton(Icon icon){		
		super(icon);			
		setBackground(Color.decode(defaultColour));		//decodes the hex color and sets the button background to it
		setForeground(Color.white);					//sets the text color of the buttons to white
		setPreferredSize(new Dimension(44,44));
		//setBorder(BorderFactory.createEmptyBorder());
		//setBorder(BorderFactory.createLineBorder(Color.white));
	}
	
	public MyJButton(String text) {
		super(text);
		setBackground(Color.decode(defaultColour));		//decodes the hex color and sets the button background to it
		setForeground(Color.white);					//sets the text color of the buttons to white
		setFont(new Font("Trebuchet MS", Font.BOLD, 14));
		//setBorder(new LineBorder(Color.decode(defaultColour)));
		//setBorder(new LineBorder(Color.WHITE));
		//setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void setActive() {
		setBackground(Color.decode(activeColour));
	}
	
	public void setNotActive() {
		setBackground(Color.decode(defaultColour));
	}
	
	
}
