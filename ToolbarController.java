package lyons.aaron.ID201145084.tesselations;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class ToolbarController extends JPanel {
  
    private TessellationModel tesModel;    
	
	private String lightGrey = "#4d4b4b";        
   
	
	private int SINGLESELECT = 1;
	private int AREASELECT = 2;
	private int DRAWSHAPE = 3;
	private int DRAWLINE = 4;
	private int EDITSHAPE = 5;
	private int SETEDGE = 6;
	private int FILL = 7;
	private int DELETE = 8;
	
	private final int TRIANGLE = 3;
	private final int SQUARE = 4;
	private final int RECTANGLE = 5;
	private final int HEXAGON = 6;
	

	//Getting icons for buttons 
	ImageIcon selectIcon = new ImageIcon(getClass().getResource("/select.png"));
	ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/delete.png"));
	ImageIcon rectangleIcon = new ImageIcon(getClass().getResource("/rectangle.png"));
	ImageIcon lineIcon = new ImageIcon(getClass().getResource("/line.png"));
	ImageIcon moveIcon = new ImageIcon(getClass().getResource("/move.png"));
	ImageIcon setEdgeIcon = new ImageIcon(getClass().getResource("/setEdge.png"));
	ImageIcon fillIcon = new ImageIcon(getClass().getResource("/fill.png"));
	ImageIcon squareIcon = new ImageIcon(getClass().getResource("/square.png"));
	ImageIcon triangleIcon = new ImageIcon(getClass().getResource("/triangle.png"));
	ImageIcon hexagonIcon = new ImageIcon(getClass().getResource("/hexagon.png"));

	//Creating Buttons 
	MyJButton[] buttons = new MyJButton[7];

	MyJButton singleSelectButton = new MyJButton(selectIcon);	
	MyJButton shapeMenuButton = new MyJButton(rectangleIcon);
	MyJButton drawLineButton = new MyJButton(lineIcon);
	MyJButton editShapeButton = new MyJButton(moveIcon);
	MyJButton setEdgeButton = new MyJButton(setEdgeIcon);
	MyJButton fillButton = new MyJButton(fillIcon);
	MyJButton deleteButton = new MyJButton(deleteIcon);

	
	public void setTesModel(TessellationModel tesModel) {
		this.tesModel = tesModel;
	}

	public ToolbarController(TessellationModel t) {
		
		tesModel = t;
		//setting panel background 
		setBackground(Color.decode(lightGrey));		
         
        //Adding Buttons to panel
        setPreferredSize(new Dimension(50,800));
        setLayout(new FlowLayout());
        add(singleSelectButton);       
        add(shapeMenuButton);
        add(drawLineButton);
        add(editShapeButton);
        add(setEdgeButton);
        add(fillButton);
        add(deleteButton);
        
		buttons[0] = singleSelectButton;		
		buttons[1] = shapeMenuButton;
		buttons[2] = drawLineButton;
		buttons[3] = editShapeButton;
		buttons[4] = setEdgeButton;
		buttons[5] = fillButton;
		buttons[6] = deleteButton;
	
		
		singleSelectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set other buttons to not active and the selected button to active 
				for (MyJButton button: buttons){
					button.setNotActive();
				}
				singleSelectButton.setActive();
				tesModel.setToolState(SINGLESELECT);
			}
        });
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set other buttons to not active and the selected button to active 
				for (MyJButton button: buttons){
					button.setNotActive();
				}
				deleteButton.setActive();
				tesModel.setToolState(DELETE);
			}
        });	


		shapeMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set other buttons to not active and the selected button to active 
				for (MyJButton button: buttons){
					button.setNotActive();
				}
				shapeMenuButton.setActive();
				tesModel.setToolState(DRAWSHAPE);
			}
        });		

		drawLineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set other buttons to not active and the selected button to active 
				for (MyJButton button: buttons){
					button.setNotActive();
				}
				drawLineButton.setActive();
				tesModel.setToolState(DRAWLINE); 
			}
        });

		editShapeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set other buttons to not active and the selected button to active 
				for (MyJButton button: buttons){
					button.setNotActive();
				}
				editShapeButton.setActive();
				tesModel.setToolState(EDITSHAPE);
			}
        });

		setEdgeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set other buttons to not active and the selected button to active 
				for (MyJButton button: buttons){
					button.setNotActive();
				}
				setEdgeButton.setActive();
				tesModel.setToolState(SETEDGE);
			}
        });	

		fillButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//set other buttons to not active and the selected button to active 
				for (MyJButton button: buttons){
					button.setNotActive();
				}
				fillButton.setActive();
				tesModel.setToolState(FILL);
			}
        });	
	}
	
	public void switchShapeIcon(int shapeType) {
		if(shapeType == SQUARE) {
			shapeMenuButton.setIcon(squareIcon);
		}else if(shapeType == RECTANGLE) {
			shapeMenuButton.setIcon(rectangleIcon);
		}else if(shapeType == TRIANGLE) {
			shapeMenuButton.setIcon(triangleIcon);
		}else if(shapeType == HEXAGON) {
			shapeMenuButton.setIcon(hexagonIcon);
		}
	}
}

