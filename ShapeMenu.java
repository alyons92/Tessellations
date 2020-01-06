package lyons.aaron.ID201145084.tesselations;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ShapeMenu extends JPanel {
	private TessellationModel tesModel;
	private ToolbarController toolPanel; 
	
	private String lightGrey = "#4d4b4b";
	private final int TRIANGLE = 3;
	private final int SQUARE = 4;
	private final int RECTANGLE = 5;
	private final int HEXAGON = 6;
	
	MyJButton[] buttons = new MyJButton[4];
	
	ImageIcon rectangleIcon = new ImageIcon(getClass().getResource("/rectangle.png"));
	ImageIcon squareIcon = new ImageIcon(getClass().getResource("/square.png"));
	ImageIcon triangleIcon = new ImageIcon(getClass().getResource("/triangle.png"));
	ImageIcon hexagonIcon = new ImageIcon(getClass().getResource("/hexagon.png"));
	
	MyJButton rectangleButton = new MyJButton(rectangleIcon);
	MyJButton squareButton = new MyJButton(squareIcon);
	MyJButton triangleButton = new MyJButton(triangleIcon);
	MyJButton hexagonButton = new MyJButton(hexagonIcon);
	
	public void setTesModel(TessellationModel tesModel) {
		this.tesModel = tesModel;
	}
	
	//creating buttons and adding them to a menu panel
	public ShapeMenu(TessellationModel t, ToolbarController tc) {
		tesModel = t;
		toolPanel = tc;
		setBackground(Color.decode(lightGrey));
		setPreferredSize(new Dimension(50,200));
        setLayout(new FlowLayout());
        add(rectangleButton);
        add(squareButton);
        add(triangleButton);
        add(hexagonButton);
        rectangleButton.setActive();
        
        buttons[0] = rectangleButton;
		buttons[1] = squareButton;
		buttons[2] = triangleButton;
		buttons[3] = hexagonButton;
		
		
		//set other buttons to not active and the selected button to active 
        rectangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				deactivateButtons();				
				rectangleButton.setActive();
				tesModel.setShapeState(RECTANGLE);
				toolPanel.switchShapeIcon(RECTANGLE);
			}
        });
        
        //set other buttons to not active and the selected button to active 
        squareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deactivateButtons();
				squareButton.setActive();
				tesModel.setShapeState(SQUARE);
				toolPanel.switchShapeIcon(SQUARE);
			}
        });
        
        //set other buttons to not active and the selected button to active 
        triangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deactivateButtons();
				triangleButton.setActive();
				tesModel.setShapeState(TRIANGLE);
				toolPanel.switchShapeIcon(TRIANGLE);
			}
        });
        
        //set other buttons to not active and the selected button to active 
        hexagonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deactivateButtons();
				hexagonButton.setActive();
				tesModel.setShapeState(HEXAGON);
				toolPanel.switchShapeIcon(HEXAGON);
			}
        });
		
	}
	
	//set all buttons to not active 
	public void deactivateButtons() {		
		for (MyJButton button: buttons){
			button.setNotActive();
		}
	}

}
