package lyons.aaron.ID201145084.tesselations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
//import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

public class ColorPickerMenu extends JPanel implements ChangeListener {
	private TessellationModel tesModel;
	private OptionsController optionsPanel; 
	private JColorChooser colorPicker;
	
    String lightGrey = "#4d4b4b";        
    String darkGrey = "#232323";
    String mediumGrey = "#404040";
	
    
	public void setTesModel(TessellationModel tesModel) {
		this.tesModel = tesModel;
	}
	
	
	public ColorPickerMenu(TessellationModel t, OptionsController oc) {
		tesModel = t;
		optionsPanel = oc;
		colorPicker = new JColorChooser();
		AbstractColorChooserPanel[] panels = colorPicker.getChooserPanels();
		for (int i = 1; i < 5; i++) {
		   colorPicker.removeChooserPanel(panels[i]);		    
		}
		colorPicker.setPreviewPanel(new JPanel());
		add(colorPicker, BorderLayout.NORTH);		
		colorPicker.setBackground(Color.decode(darkGrey));
		colorPicker.setForeground(Color.decode(darkGrey));
		//colorPicker.foreground(Color.decode(darkGrey));
		colorPicker.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		//setPreferredSize(new Dimension(300,200));
		colorPicker.getSelectionModel().addChangeListener(this);
		
		
	}
	public void stateChanged(ChangeEvent e)  {
		
		Color newColor = colorPicker.getColor();
		if(tesModel.getUseMainColor()) {
			tesModel.setMainColor(newColor);
		}else {
			tesModel.setSecondaryColor(newColor);
		}
		optionsPanel.updateButtonColor(newColor, tesModel.getUseMainColor());
		//set other buttons to not active and the selected button to active 
		
		//rectangleButton.setActive();
		//tesModel.setShapeState(RECTANGLE);
		//toolPanel.switchShapeIcon(RECTANGLE);
	}

}
