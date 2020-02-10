package lyons.aaron.ID201145084.tesselations;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class OptionsController extends JPanel{
	//types of tessellations
	private final int TRANSLATION = 0;
	private final int ROTATION = 1;
	private final int CLOCKWISE = 0;
	private final int ANTICLOCKWISE = 1;

	private int SINGLESELECT = 1;
	private int EDITINGLINE = 10;
	private TessellationModel tesModel;
	private DrawingView drawingView;

	private String lightGrey = "#4d4b4b";        

	private int tessellationType = TRANSLATION;
	private int direction = CLOCKWISE;
	private int numRows = 1;
	private int numColumns = 1;
	private int numRotations;
	//private int numRotations;

	private JButton colorButton1 = new JButton();
	private JButton colorButton2 = new JButton();

	Border border = new LineBorder(Color.WHITE, 1);
	

	String[] tessellationOptions = {"Translation", "Rotation"};
	String[] directionOptions = {"Clockwise", "Anti-Clockwise"};

	public void setTesModel(TessellationModel tesModel) {
		this.tesModel = tesModel;
	}

	public OptionsController(TessellationModel t, DrawingView dv) {
		tesModel = t;
		drawingView = dv;
		setBackground(Color.decode(lightGrey));		

		MyJButton tessellateButton = new MyJButton("Tessellate");
		MyJButton clearTessellations = new MyJButton("Clear Tessellations");
		MyJButton clearAll = new MyJButton("Clear All");
		JComboBox<String> tessList = new JComboBox<String>(tessellationOptions);
		JComboBox<String> directionList = new JComboBox<String>(directionOptions);
		JCheckBox colorCheckBox = new JCheckBox("Use Colors");
		
		colorCheckBox.setBackground(Color.decode(lightGrey));
		colorCheckBox.setForeground(Color.WHITE);
		colorCheckBox.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

		tessList.setBackground(Color.decode(lightGrey));
		tessList.setForeground(Color.WHITE);
		tessList.setSelectedIndex(0);
		tessList.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

		directionList.setBackground(Color.decode(lightGrey));
		directionList.setForeground(Color.WHITE);
		directionList.setSelectedIndex(0);
		directionList.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

		MyJLabel typeLabel = new MyJLabel("Type of Tessellation:");
		MyJLabel rowLabel = new MyJLabel("     Rows:");
		MyJLabel columnLabel = new MyJLabel("     Columns:");
		MyJLabel rotationsLabel = new MyJLabel("     Rotations:");
		MyJLabel directionLabel = new MyJLabel("     Direction:");
		MyJLabel colorLabel1 = new MyJLabel("     Color 1:");
		MyJLabel colorLabel2 = new MyJLabel("  Color 2:");
		
		JTextField rowField = new JTextField(2);
		JTextField columnsField = new JTextField(2);
		JTextField rotationsField = new JTextField(2); 

		//Adding Buttons to panel
		setPreferredSize(new Dimension(50,35));
		setLayout(new FlowLayout(FlowLayout.LEADING));
		add(typeLabel);
		add(tessList);
		add(directionLabel);
		add(directionList);
		add(rotationsLabel);
		add(rotationsField);
		add(rowLabel);
		add(rowField);
		add(columnLabel);
		add(columnsField);
		add(tessellateButton);
		add(clearTessellations);
		add(clearAll);		
		
		colorButton1.setPreferredSize(new Dimension(35,25));
		colorButton2.setPreferredSize(new Dimension(35,25));
		colorButton1.setBorder(border);
		colorButton2.setBorder(border);
		colorButton2.setBorderPainted(false);
		
		updateButtonColor(tesModel.getMainColor(), true);
		updateButtonColor(tesModel.getSecondaryColor(), false);
		
		add(colorLabel1);
		add(colorButton1);
		add(colorLabel2);
		add(colorButton2);
		add(colorCheckBox);

		//rotationsLabel.setVisible(false);
		rotationsLabel.setVisible(false);
		rotationsField.setVisible(false);
		directionLabel.setVisible(false);
		directionList.setVisible(false);

		tessellateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tesModel.getToolState() == EDITINGLINE) {
					tesModel.setToolState(SINGLESELECT);
				}
				if(tessellationType == TRANSLATION) {
					tesModel.translationTessellate(numRows, numColumns);
					drawingView.repaint();
				}else if(tessellationType == ROTATION) {
					tesModel.rotationTessellate(numRotations, direction);
					drawingView.repaint();
				}
			}
		});

		clearTessellations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(tesModel.getShapes().size()>1) {
					tesModel.clearTessellations();
					drawingView.repaint();
				}
			}
		});

		clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tesModel.clearAll();
				drawingView.repaint();
			}
		});
		
		colorCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
					
				tesModel.setUsePattern(colorCheckBox.isSelected());
				
			}
		});

		tessList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb =(JComboBox)e.getSource();
				String selected = (String)cb.getSelectedItem();
				if (selected == "Translation") {
					tessellationType = TRANSLATION;
					rotationsLabel.setVisible(false);
					rotationsField.setVisible(false);
					directionLabel.setVisible(false);
					directionList.setVisible(false);
					rowLabel.setVisible(true);
					rowField.setVisible(true);
					columnLabel.setVisible(true);
					columnsField.setVisible(true);
				}else if(selected == "Rotation") {
					tessellationType = ROTATION;
					rotationsLabel.setVisible(true);
					rotationsField.setVisible(true);
					directionLabel.setVisible(true);
					directionList.setVisible(true);
					rowLabel.setVisible(false);
					rowField.setVisible(false);
					columnLabel.setVisible(false);
					columnsField.setVisible(false);
				}
			}
		});

		directionList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb =(JComboBox)e.getSource();
				String selected = (String)cb.getSelectedItem();
				if (selected == "Clockwise") {
					direction = CLOCKWISE;

				}else if(selected == "Anti-Clockwise") {
					direction = ANTICLOCKWISE;

				}
			}
		});

		rowField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = rowField.getText();
				numRows = Integer.parseInt(input);
				System.out.println("numRows: " + numRows);
			}
		});

		columnsField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = columnsField.getText();
				numColumns = Integer.parseInt(input);
				System.out.println("numColumns: " + numColumns);
			}
		});

		rotationsField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = rotationsField.getText();
				numRotations = Integer.parseInt(input);
				System.out.println("numRotations: " + numRotations);
			}
		});
		
		colorButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tesModel.setUseMainColor(true); 				
				colorButton1.setBorderPainted(true);
				colorButton2.setBorderPainted(false);
			}
		});
		
		colorButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tesModel.setUseMainColor(false); 				
				colorButton2.setBorderPainted(true);
				colorButton1.setBorderPainted(false);
			}
		});

	}

	
	public void updateButtonColor(Color color, boolean mainButton) {
		if (mainButton) {
			colorButton1.setBackground(color);
		}else {
			colorButton2.setBackground(color);
		}
	}
	

}
