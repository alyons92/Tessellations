package lyons.aaron.ID201145084.tesselations;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class DrawingCanvas extends JPanel implements MouseListener, MouseMotionListener{

	private double startX, startY, endX, endY = 0;
	private TessellationModel tesModel;
	private DrawingView drawingView;
	private Line closestStartLine;
	private Edge closestStartEdge;
	private Edge closestEndEdge;
	private Line editLine;

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
	
	public void setTesModel(TessellationModel tesModel) {
		this.tesModel = tesModel;
	}

	public DrawingCanvas(TessellationModel t, DrawingView dv){
		tesModel = t;
		drawingView = dv;
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
	}
	
	public Boolean checkEndPoint() {
		double minDistance = Math.abs(closestStartLine.calcDistToPoint(endX, endY));		
		if(minDistance < 5) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public Boolean checkControlPoint(double x1, double y1, double x2, double y2) {
		double distance = Math.sqrt(Math.pow((y2-y1), 2) + Math.pow((x2-x1), 2));
		if (distance <= 2.5) {
			return true;
		}else {
			return false;
		}
	}
	
	public Edge getNearestEdge(double x0, double y0) {
		Edge nearestEdge = tesModel.getShapes().get(0).getEdges().get(0);
		double minDistance = nearestEdge.calcDistToPoint(x0, y0);	
		for (Edge edge : tesModel.getShapes().get(0).getEdges()) {
			double distance = edge.calcDistToPoint(x0, y0);
			if (distance < minDistance) {
				minDistance = distance;
				nearestEdge = edge;
			}
		}
		return nearestEdge;
	}

	public Line getNearestLine(double x0, double y0) {
		ArrayList<Line> allLines = new ArrayList<Line>();
		for(Edge edge : tesModel.getShapes().get(0).getEdges()) {
			edge.getAllLines(allLines);			
		}
		double minDistance = allLines.get(0).calcDistToPoint(x0, y0);
		Line nearestLine = allLines.get(0);
		for(int i=1; i<allLines.size(); i++) {
			double distance = allLines.get(i).calcDistToPoint(x0, y0);
			if (distance < minDistance) {
				minDistance = distance;
				nearestLine = allLines.get(i);
			}
		}
		return nearestLine;


	}

	public void setClosestPoints(Boolean start, Line line, double x0, double y0) {
		double a = line.getyCoords().get(1) - line.getyCoords().get(0);
		double b = (line.getxCoords().get(1) - line.getxCoords().get(0))*-1;
		double c = (line.getxCoords().get(1)*line.getyCoords().get(0))-(line.getyCoords().get(1)*line.getxCoords().get(0));
		if (start) {
			startX = (((b*((b*x0) - (a*y0)))-(a*c))/(Math.pow(a, 2) + Math.pow(b, 2))); 
			startY = (((a*(((0-b)*x0) + (a*y0)))-(b*c))/(Math.pow(a, 2) + Math.pow(b, 2)));			
		}else {
			endX = (((b*((b*x0) - (a*y0)))-(a*c))/(Math.pow(a, 2) + Math.pow(b, 2))); 
			endY = (((a*(((0-b)*x0) + (a*y0)))-(b*c))/(Math.pow(a, 2) + Math.pow(b, 2))); 			
		}		
	}

	public Dimension getPreferredSize(){
		return new Dimension(800, 600);
	}

	public void mouseClicked(MouseEvent e){
		if (tesModel.getToolState() == ADDINGLINES) {
			endX = e.getX();
			endY = e.getY();
			if (checkEndPoint()){
				setClosestPoints(false, closestStartLine, endX, endY);
				tesModel.addTempLine(closestStartLine, true, startX, startY, endX, endY);
			}else {
				tesModel.addTempLine(closestStartLine, false, startX, startY, endX, endY);
				tesModel.incrementLineNum();
				startX = endX;
				startY = endY;
			}			
		}
		if(tesModel.getToolState() == SINGLESELECT) {
			editLine = getNearestLine(e.getX(), e.getY());
			tesModel.createControlPoints(editLine);
			drawingView.repaint();			
		}
		if(tesModel.getToolState() == EDITINGLINE) {
			Boolean check1 = checkControlPoint(startX, startY, editLine.getxCoords().get(0), editLine.getyCoords().get(0));
			Boolean check2 = checkControlPoint(startX, startY, editLine.getxCoords().get(1), editLine.getyCoords().get(1));
			if (!check1 && !check2) {
				editLine = getNearestLine(e.getX(), e.getY());
				tesModel.createControlPoints(editLine);
				drawingView.repaint();
			}			
		}
		if(tesModel.getToolState() == FILL) {
			tesModel.checkFill(e.getX(), e.getY());
		}
		if(tesModel.getToolState() == DELETE) {
			tesModel.deleteShape(e.getX(), e.getY());
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		if (tesModel.getToolState() != ADDINGLINES) {
			endX = startX = e.getX();
			endY = startY = e.getY();
		}
		if (tesModel.getToolState() == DRAWLINE) {
			closestStartLine = getNearestLine(startX, startY);
			setClosestPoints(true, closestStartLine, startX, startY);
		}
		if (tesModel.getToolState() == SETEDGE){
			//set the startX and startY to closest point on the shape
			closestStartEdge = getNearestEdge(startX, startY);
			setClosestPoints(true, closestStartEdge, startX, startY);
		}
	}
	
	public void mouseDragged(MouseEvent e){
		endX = e.getX();
		endY = e.getY();
		if (tesModel.getToolState() == DRAWSHAPE){
			if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK) {
				tesModel.createShape(true, startX, startY, endX, endY);
			}else {
				tesModel.createShape(false, startX, startY, endX, endY);				
			}
		}
		if (tesModel.getToolState() == SETEDGE) {
			tesModel.createControlLine(startX, startY, endX, endY);
		}
		if (tesModel.getToolState() == DRAWLINE) {
			tesModel.addTempLine(closestStartLine, false, startX, startY, endX, endY);
		}
		if (tesModel.getToolState() == EDITSHAPE) {
			tesModel.moveAll(startX, startY, endX, endY);
			startX = e.getX();
			startY = e.getY();
		}
		if (tesModel.getToolState() == EDITINGLINE) {
			if(checkControlPoint(startX, startY, tesModel.getxControls().get(0), tesModel.getyControls().get(0))) {
				tesModel.updateLine(editLine, true, false, 0, endX, endY);
			}else if(checkControlPoint(startX, startY, tesModel.getxControls().get(1), tesModel.getyControls().get(1))) {
				tesModel.updateLine(editLine, true, false, 1, endX, endY);
			}else if(checkControlPoint(startX,startY, tesModel.getxControls().get(2), tesModel.getyControls().get(2))) {
				tesModel.updateLine(editLine, true, false, 2, endX, endY);
			}
			startX = e.getX();
			startY = e.getY();
		}		
		drawingView.repaint();

	}
	public void mouseReleased(MouseEvent e){
		endX = e.getX();
		endY = e.getY();		
		if (tesModel.getToolState() == DRAWSHAPE){
			if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK) {
				tesModel.createShape(true, startX, startY, endX, endY);
			}else {
				tesModel.createShape(false, startX, startY, endX, endY);				
			}				
		}
		if (tesModel.getToolState() == SETEDGE) {
			closestEndEdge = getNearestEdge(endX, endY);
			setClosestPoints(false, closestEndEdge, endX, endY);			
			tesModel.setCorrespondingEdge(closestStartEdge, closestEndEdge);
			tesModel.deleteControlLine();
		}
		if (tesModel.getToolState() == DRAWLINE) {		
			if (checkEndPoint()){
				setClosestPoints(false, closestStartLine, endX, endY);
				tesModel.addTempLine(closestStartLine, true, startX, startY, endX, endY);
			}else {
				tesModel.addTempLine(closestStartLine, false, startX, startY, endX, endY);
				tesModel.incrementLineNum();
				tesModel.setToolState(ADDINGLINES);
				startX = endX;
				startY = endY;
			}			
		}
		drawingView.repaint();
		//check if shape has been made
		//create shape with coords if not
		//add the line if it has
	}
	public void mouseMoved(MouseEvent e){
		endX = e.getX();
		endY = e.getY();
		if (tesModel.getToolState() == ADDINGLINES) {
			tesModel.addTempLine(closestStartLine, false, startX, startY, endX, endY);
			drawingView.repaint();
		}
	}
}