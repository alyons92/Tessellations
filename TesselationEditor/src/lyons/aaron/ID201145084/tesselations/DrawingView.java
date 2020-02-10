package lyons.aaron.ID201145084.tesselations;
import java.awt.*;
import java.awt.event.*;

//import javax.management.timer.Timer;
import javax.swing.*;
import javax.swing.Timer;

import java.util.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class DrawingView extends JPanel implements ActionListener {
	
	//possible tool states
	private int SINGLESELECT = 1;
	private int AREASELECT = 2;
	private int DRAWSHAPE = 3;
	private int DRAWLINE = 4;
	private int EDITSHAPE = 5;
	private int SETEDGE = 6;
	private int FILL = 7;
	private int DELETE = 8;
	private int ADDINGLINES = 9;
	private int EDITINGLINE = 10;
	
	private BufferedImage bImage;
	private Graphics2D big;
	private TessellationModel tesModel;
	
	public BufferedImage getBImage() {
		return bImage;
	}

	public void setBi(BufferedImage bi) {
		this.bImage = bi;
	}

		
	public void setTesModel(TessellationModel tesModel) {
		this.tesModel = tesModel;
	}

	DrawingView(TessellationModel t) {
		System.out.println("Setting up Drawing view");
		tesModel = t;
		setBackground(Color.WHITE);
	
	}
	
	public void actionPerformed (ActionEvent e) {
		//System.out.println("action found");
		repaint();
	}
	
	public void printTest() {
		System.out.println("print test");
	}
	
	protected void paintComponent(Graphics g) {		
		super.paintComponent(g);
		if(tesModel.getShapeCreated()) {
			//System.out.println("drawing component");
			paintShape(g);
		}
	}
	
	private void paintShape(Graphics g) {
		
		Dimension dim = getSize();
	    int w = dim.width;
	    int h = dim.height;	
	    Rectangle area = new Rectangle(w, h);
	    bImage = (BufferedImage) createImage(w, h);
	    big = bImage.createGraphics();
	    big.setColor(Color.white);
	    big.fillRect(0, 0, area.width, area.height);
		Graphics2D g2 = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(4);
		big.setStroke(stroke);
		
		for(Shape currentShape: tesModel.getShapes()) {
			//g.setColor(Color.BLACK);
			//g2.draw(currentShape.makePath());
			//g.setColor(currentShape.getFillColour());
			//g2.fill(currentShape.makePath());
			//System.out.println("Shape Drawn ");
			big.setColor(Color.BLACK);
			big.draw(currentShape.makePath());
			big.setColor(currentShape.getFillColour());
			big.fill(currentShape.makePath());
			g2.drawImage(bImage, 0, 0, this);
			
		}
		if(tesModel.getLineStarted()) {
			g.setColor(Color.BLACK);
			Graphics2D tempLinesg = (Graphics2D) g;
			tempLinesg.setStroke(new BasicStroke(2));
			ArrayList<Double> originalxPoints = tesModel.getTempLinesOriginalxCoords();
			ArrayList<Double> originalyPoints = tesModel.getTempLinesOriginalyCoords();
			tempLinesg.draw(makePath(originalxPoints, originalyPoints));
			if (tesModel.getTempLinesMirrorxCoords().size()>0) {
				ArrayList<Double> mirrorxPoints = tesModel.getTempLinesMirrorxCoords();
				ArrayList<Double> mirroryPoints = tesModel.getTempLinesMirroryCoords();
				tempLinesg.draw(makePath(mirrorxPoints, mirroryPoints));
			}
		}
		
		if(tesModel.getControlLine() != null) {
			g.setColor(Color.BLACK);
			 float dash1[] = {10.0f};
			 BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
			 Graphics2D controlg = (Graphics2D) g;
			 controlg.setStroke(dashed);
			 ArrayList<Double> controlxPoints = tesModel.getControlLine().getxCoords();
			
			 ArrayList<Double> controlyPoints = tesModel.getControlLine().getyCoords();
			 controlg.draw(makePath(controlxPoints, controlyPoints));
			 
		}
		
		if(tesModel.getToolState() == EDITINGLINE) {
			g.setColor(Color.BLACK);
			Graphics2D controlPointg = (Graphics2D)g;
			//Line editLine = tesModel.getEditLine();
			for (int i = 0; i<tesModel.getxControls().size(); i++) {
				controlPointg.fill(new Ellipse2D.Double(tesModel.getxControls().get(i)-2.5, tesModel.getyControls().get(i)-2.5, 5, 5));				
			}
		}
		
		
	}
	
	public GeneralPath makePath(ArrayList<Double> xCoords, ArrayList<Double> yCoords) {
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xCoords.size());
		polyline.moveTo(xCoords.get(0), yCoords.get(0));

		for (int index = 1; index < xCoords.size(); index++){
			polyline.lineTo(xCoords.get(index), yCoords.get(index));
		}

		return polyline;
	}
}