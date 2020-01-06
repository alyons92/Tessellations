package lyons.aaron.ID201145084.tesselations;
import java.awt.Color;
import java.io.Serializable;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TessellationModel implements Serializable{
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
    private int shapeState = 5; //3 for triangle, 4 for square, 5 for rectangle,  6 for hexagon
	private boolean shapeCreated = false;
	private int toolState; 
	private Color mainColor = Color.WHITE;
	private Color secondaryColor = Color.BLACK;
	private boolean useMainColor = true;
	private boolean usePattern = false;

	
	//holds lines drawn until reconnected with starting line 
	private ArrayList<Line> tempLinesOriginal = new ArrayList<Line>();
	private ArrayList<Line> tempLinesMirror = new ArrayList<Line>();
	
	//Coordinates of control points
	private ArrayList<Double> xControls = new ArrayList<Double>();
	private ArrayList<Double> yControls = new ArrayList<Double>();
	
	private int lineNum = 0;	
	private boolean lineStarted = false;
	private Edge sourceEdge;
	private Line startLine;
	private Line startMirrorLine;
	private Line controlLine;
	private Line editLine;
	private double xOrigin;
	private double yOrigin;
	private final double THRESHOLD = 0.0001;
	
	//possible tool states
	private int DRAWLINE = 4;
	private int DELETE = 8;
	private int ADDINGLINES = 9;
	private int EDITINGLINE = 10;
     
    public TessellationModel() {
         
    }
    
    public Line getControlLine() {
    	return controlLine;
    }
    
     //sets the corresponding edge of each of the edges received to be each other
     //checks what kind of tessellation is allowed between the linked edges and updates the edge's type
     public void setCorrespondingEdge(Edge startEdge, Edge correspondingEdge) {

    	 if(startEdge != null && correspondingEdge != null) {
    		startEdge.setCorrespondingEdge(correspondingEdge);
    		correspondingEdge.setCorrespondingEdge(startEdge);
    		System.out.println("corresponding edge set");
    		double startXChange = Math.abs(startEdge.getChangeInX());
    		double startYChange = Math.abs(startEdge.getChangeInY());
    	
    		if ((Math.abs(startEdge.getEdgeNum() - correspondingEdge.getEdgeNum())==1) || (Math.abs(startEdge.getEdgeNum() - correspondingEdge.getEdgeNum()) == startEdge.getMaxEdges()-1)){
    			shapes.get(0).resetRotation();
    			startEdge.setRotationEdge(true);
    	 		correspondingEdge.setRotationEdge(true);    	 		
    		}else {
    	 		if (startXChange<THRESHOLD){
    	 			startEdge.setRowEdge(true);
    	 			correspondingEdge.setRowEdge(true);
    	 		}else if (startYChange<THRESHOLD) {
    	 			startEdge.setColumnEdge(true);
    	 			correspondingEdge.setColumnEdge(true);    	 			
    	 		}else {
    	 			startEdge.setAlternateEdge(true);
    	 			correspondingEdge.setAlternateEdge(true);    	 			
    	 		}    	 	    	
    	 	}
    	 }else {
    		 System.out.println("one of the points is not an edge");
    	 }
     }
     
     //creates the control points for a line 
     //control points allow the user to see where a line can be edited 
     public void createControlPoints(Line line) {
    	editLine = line;
    	xControls.clear();
    	yControls.clear();
    	for(int i=0; i<line.getxCoords().size(); i++) {
    		xControls.add(line.getxCoords().get(i));
    		yControls.add(line.getyCoords().get(i));
    	}
    	if (line.getLines().size()<2) {
    		 xControls.add(line.getxCurveControl());
    		 yControls.add(line.getyCurveControl());
    	 }
    	toolState = EDITINGLINE;    	 
     }
	
     //adds a temporary line to the shape 
     //the temporary line is continuously overwritten until set by the user  
     //does the same for the corresponding line of the shape if there is one
	public void addTempLine(Line sl, Boolean end, double x1, double y1, double x2, double y2){
		Boolean first = false;	
		Boolean mirror = false;
		Line newLine = new Line(x1, y1, x2, y2);
		
		if (lineStarted == false){
			this.startLine = sl;
			sourceEdge = startLine.getSourceEdge();				
			
			if (startLine.getCorrespondingLine() != null) {
				startMirrorLine = startLine.getCorrespondingLine();
			}
			first = true;
			lineStarted = true;
					
			if (tempLinesOriginal.size()>0) {				
				tempLinesOriginal.set(lineNum, newLine);
			}else {			;
				tempLinesOriginal.add(lineNum, newLine);
			}
		}else {		
			if (tempLinesOriginal.size()!=lineNum) {				
				tempLinesOriginal.set(lineNum, newLine);
			}else {				
				tempLinesOriginal.add(lineNum, newLine);
			}
		}
		
		newLine.setSourceEdge(sourceEdge);
		Line mirrorLine = null;
		if (sourceEdge.getCorrespondingEdge() != null) {
			mirror = true;
			Edge correspondingEdge = sourceEdge.getCorrespondingEdge();			

			if(sourceEdge.getRotationEdge()) {
				Double xOrigin;
				Double yOrigin;
				double angle = sourceEdge.calcAngle(correspondingEdge);
				System.out.println("angle: " + angle);
				if ((Math.abs(sourceEdge.getxCoords().get(0)-correspondingEdge.getxCoords().get(1))<THRESHOLD)&&(Math.abs(sourceEdge.getyCoords().get(0)-correspondingEdge.getyCoords().get(1))<THRESHOLD)){
					xOrigin = sourceEdge.getxCoords().get(0);
					yOrigin = sourceEdge.getyCoords().get(0);
				}else {
					xOrigin = sourceEdge.getxCoords().get(1);
					yOrigin = sourceEdge.getyCoords().get(1);
				}
				mirrorLine = new Line (x1, y1, x2, y2);
				mirrorLine.rotateLines(xOrigin, yOrigin, angle);	
			}else {
				double[] changes = calcChanges(sourceEdge);
				mirrorLine = new Line(x1+changes[0], y1+changes[1], x2+changes[0], y2+changes[1]);
			}

			if(!first && tempLinesMirror.size() != lineNum) {
				tempLinesMirror.set(lineNum, mirrorLine);
			}else {
				tempLinesMirror.add(lineNum, mirrorLine);
			}
			mirrorLine.setSourceEdge(correspondingEdge);
			newLine.setCorrespondingLine(mirrorLine);
			mirrorLine.setCorrespondingLine(newLine);							
		}
		
		//get the edge that the startLine is connected to
		//get corresponding edge of that edge
		//calc transformation to get from original edge to corresponding edge 
		//create newMirrorLine with the transformation applied to coords 
		//add newMirrorLine to arrar tempLinesMirror		
		if (end == true) {			
			lineStarted = false;
			lineNum = 0;
			toolState = DRAWLINE;			
			
			orderArray(tempLinesOriginal, startLine);
			startLine.attachLine(tempLinesOriginal.get(0));
			tempLinesOriginal.get(0).setStartAddition(true);
			tempLinesOriginal.get(tempLinesOriginal.size()-1).setEndAddition(true);
			tempLinesOriginal.get(0).setSourceLine(startLine);			
			if (mirror == true) {
				orderArray(tempLinesMirror, startMirrorLine);
				startMirrorLine.attachLine(tempLinesMirror.get(0));
				tempLinesMirror.get(0).setStartAddition(true);
				tempLinesMirror.get(tempLinesMirror.size()-1).setEndAddition(true);
				tempLinesMirror.get(0).setSourceLine(startMirrorLine);
			}
			for (int i=0; i<tempLinesOriginal.size()-1; i++) {
				tempLinesOriginal.get(i).attachLine(tempLinesOriginal.get(i+1));
				tempLinesOriginal.get(i+1).setSourceLine(tempLinesOriginal.get(i));
				if (mirror == true) {
					tempLinesMirror.get(i).attachLine(tempLinesMirror.get(i+1));
					tempLinesMirror.get(i+1).setSourceLine(tempLinesMirror.get(i));
				}
			}
			tempLinesOriginal.clear();
			tempLinesMirror.clear();
			
		}
		
		//check if coords of (x2, y2) are on startLine
			//if they are, add all temp lines to shapes.get(0), set lineStarted to false and lineNum to 0
			//if not, keep drawing - with lineNum controlled by the canvas
	}
	
	//calculates the distance between each coordinate of the connected lines
	public double[] calcChanges(Edge sourceEdge) {
		double[] changes = new double[2];
		double xChange = 0;
		double yChange = 0;
		Edge correspondingEdge = sourceEdge.getCorrespondingEdge();
		if (sourceEdge.getColumnEdge()) {			
			yChange = sourceEdge.calcyChange(correspondingEdge);				
		}else if(sourceEdge.getRowEdge()) {
			xChange = sourceEdge.calcxChange(correspondingEdge);
				
		}else if(sourceEdge.getAlternateEdge()) {
			yChange = sourceEdge.calcyChange(correspondingEdge);
			xChange = sourceEdge.calcxChange(correspondingEdge);
			
		}
		changes[0] = xChange;
		changes[1] = yChange;
		return changes;
	}
	
	//Order an array of lines based on distance
	public void orderArray(ArrayList<Line> lines, Line source) {
		Line firstLine = lines.get(0);
		Line lastLine = lines.get(lines.size()-1);
		double firstDist = calcDistBetweenPoints(firstLine.getxCoords().get(0), firstLine.getyCoords().get(0), source.getxCoords().get(0), source.getyCoords().get(0));
		double lastDist = calcDistBetweenPoints(lastLine.getxCoords().get(1), lastLine.getyCoords().get(1), source.getxCoords().get(0), source.getyCoords().get(0));
		if(lastDist<firstDist) {
			Collections.reverse(lines);
			for(Line line: lines) {
				Collections.reverse(line.getxCoords());
				Collections.reverse(line.getyCoords());
			}
		}
	}
	
	//calculates the distance between 2 coordinates 
	public double calcDistBetweenPoints(double x1, double y1, double x2, double y2) {
		double distance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
		return distance;
	}
	
	public void incrementLineNum(){
		lineNum++;
	}
	
	//creates a new copy of the shape received as a parameter 
	public Shape cloneShape(Shape shape) {
		Shape cloneShape = new Shape(shape.getNumEdges());
		for (Edge edge : shape.getEdges()) {
			Edge cloneEdge = new Edge(edge.getxCoords().get(0),edge.getyCoords().get(0), edge.getxCoords().get(1), edge.getyCoords().get(1), edge.getEdgeNum(), edge.getMaxEdges());
			cloneShape.addEdge(cloneEdge);
			
			cloneEdge.setLines(edge.cloneLines());
		}
		return cloneShape;
	}
	
	//Algorithm to tessellate the shape horizontally and/or vertically
    public void translationTessellate(int numRows, int numColumns) {
    	boolean hasAlternative = false;
    	for (Edge edge : shapes.get(0).getEdges()) {
    		if (edge.getAlternateEdge()) {
    			hasAlternative = true;
    		}
    	}
        double xChangeRow = shapes.get(0).getMaxX()-shapes.get(0).getMinX();
        double yChangeColumn = shapes.get(0).getMaxY()-shapes.get(0).getMinY();
        double xAlternative1 = 0;
    	double yAlternative1 = 0;
    	double xAlternative2 = 0;
    	double yAlternative2 = 0;
    	//alternative is for hexagon shapes
        if(hasAlternative) {
        	Edge alternateEdge1 = shapes.get(0).getEdges().get(0);
        	Edge alternateEdge2 = shapes.get(0).getEdges().get(shapes.get(0).getNumEdges()-1);
        	xAlternative1 = alternateEdge1.calcxChange(alternateEdge1.getCorrespondingEdge());
        	yAlternative1 = alternateEdge1.calcyChange(alternateEdge1.getCorrespondingEdge());
        	xAlternative2 = alternateEdge2.calcxChange(alternateEdge2.getCorrespondingEdge());
        	yAlternative2 = alternateEdge2.calcyChange(alternateEdge2.getCorrespondingEdge());
        }
        int shapeIndex = 1;
        for(int r=1; r<=numRows; r++) {
            for(int c=1; c<numColumns; c++) {            	
                //make copy of previous shape 
                //make sure this isn't referencing the same object
                shapes.add(cloneShape(shapes.get(shapeIndex-1)));               
                if(hasAlternative) {
                	if(c%2==0) {
                		shapes.get(shapeIndex).moveShape(xAlternative2, yAlternative2);
                	}else {
                		shapes.get(shapeIndex).moveShape(xAlternative1, yAlternative1);
                	}
                }else {
                	shapes.get(shapeIndex).moveShape(xChangeRow, 0);
                }
                shapeIndex++;
            }
            if(r<numRows) {            	
            	//make copy of first shape in row
            	shapes.add(cloneShape(shapes.get(shapeIndex-numColumns)));
            	shapes.get(shapeIndex).moveShape(0, yChangeColumn);
            	shapeIndex++;
            }
        }
        //sets the colors of the shapes 
        setShapeColors(); 
    }
    
    //checks if the use colors option has been checked, then alternates between the two colours to fill each shape
    public void setShapeColors() {
    	if (usePattern) {
    		shapes.get(0).setFillColour(mainColor);
    		for(int i = 1; i<shapes.size(); i++) {
    			if (i%2 == 0) {
    				shapes.get(i).setFillColour(mainColor);
    			}else {
    				shapes.get(i).setFillColour(secondaryColor);
    			}
    		}
    	}
    }
    
    //calculates the angle between 2 lines that share a coordinate 
    public double calcAngletoPoint(double x1, double y1, double x2, double y2, double x3, double y3) {
    	double xChange1 = x2-x1;
    	double yChange1 = y2-y1;
    	double xChange2 = x3-x1;
    	double yChange2 = y3-y1;
    	double angle1 = Math.atan2(-1*(yChange1), (xChange1));
		double angle2 = Math.atan2(-1*(yChange2), (xChange2));
		double resultAngle = (angle1-angle2);
  	
		return resultAngle;
    }
    
    //updates a coordinate of a line to a new point and updates the connected lines
    public void updateLine(Line updateLine, Boolean original, Boolean mirror, int coord, double x, double y) {    
    	//only allows changes to lines which are not an edge of the shape
    	if(!updateLine.getIsEdge()) {
    		//if coord is 2, then the update is to the curve control points 
    		if (coord == 2) {
    			updateLine.setCurve(true);
    			updateLine.setxCurveControl(x);
    			updateLine.setyCurveControl(y);    		
    		
    		}else {    			
    			double oldx = updateLine.getxCoords().get(coord);
    			double oldy = updateLine.getyCoords().get(coord);
    			double ox;
    			double oy;
    			if(coord == 0) {
    				ox = updateLine.getxCoords().get(1);
    				oy = updateLine.getyCoords().get(1);
    			}else {
    				ox = updateLine.getxCoords().get(0);
    				oy = updateLine.getyCoords().get(0);
    			}
    			//calculates the angle between the old and new coords
    			//rotates the connected lines by this angle
    			double angle = calcAngletoPoint(ox, oy, oldx, oldy, x, y);
    			for(int i = 0; i<updateLine.getLines().size(); i++) {
    				Line line = updateLine.getLines().get(i);
    				if (i != updateLine.getLines().size()-1) {
    					line.rotateLines(ox, oy, angle);
    				}
    			}
    			updateLine.rotateControlPoints(ox, oy, angle);
    			updateLine.getxCoords().set(coord, x);
    			updateLine.getyCoords().set(coord, y);

    			if(original){    				
    				if(coord == 0 && !updateLine.isStartAddition()){
    					updateLine(updateLine.getSourceLine(), false, mirror, 1, x, y);
    				}else if(coord == 1 && !updateLine.isEndAddition()){
    					ArrayList<Line> lines = updateLine.getLines();
    					updateLine(lines.get(lines.size()-1), false, mirror, 0, x, y);
    				}    		
    			}
    		}
    		//checks if there is still a corresponding edge that also needs updating
    		//calculates the new point of this corresponding edge and calls the method again to update the line
    		if(!mirror && updateLine.getCorrespondingLine() != null) {
    			Edge source = updateLine.getSourceEdge();
    			double newxCoord;
    			double newyCoord;
    			if(source.getRotationEdge()) {
    				setRotationOrigin();
    				double edgeAngle = source.calcAngle(source.getCorrespondingEdge()); 
    				newxCoord = (((x - xOrigin)*Math.cos(edgeAngle)) - ((y - yOrigin)*Math.sin(edgeAngle))) + xOrigin;
    				newyCoord = (((y - yOrigin)*Math.cos(edgeAngle)) + ((x - xOrigin)*Math.sin(edgeAngle))) + yOrigin;
    			}else {
    				double[] changes = calcChanges(source);
    				newxCoord = x+changes[0];
    				newyCoord = y+changes[1];
    			}
    			if(coord == 0 && !updateLine.isStartAddition()){
    				updateLine(updateLine.getCorrespondingLine(), true, true, 1, newxCoord, newyCoord);
    			}else if(coord == 1 && !updateLine.isEndAddition()){        			
    				updateLine(updateLine.getCorrespondingLine(), true, true, 0, newxCoord, newyCoord);
    			}else if(coord == 2){					
    				updateLine(updateLine.getCorrespondingLine(), true, true, 2, newxCoord, newyCoord);
    			}

    		}
    		createControlPoints(editLine);
    	}else {
    		System.out.println("cannot edit original shape");
    	}
    }
    
    //Algorithm to tesselate the shape via rotation
    public void rotationTessellate(int numTessellations, int direction) {
    	//checks that the shape has linked edges that share a coordinate and calculates the angle between them
    	if (shapes.get(0).checkRotation()) {
    		setRotationOrigin();
    		Edge  angleEdge = shapes.get(0).getRotationEdge();
    		double angle = angleEdge.calcAngle(angleEdge.getCorrespondingEdge());    		
    		if((direction == 0 && angle<0)||(direction == 1 && angle>0)) {
    			angle = angle*-1;
    		}
    		for (int i = 1; i<numTessellations; i++) {
    			shapes.add(cloneShape(shapes.get(i-1)));
    			shapes.get(i).rotateShape(xOrigin, yOrigin, angle);
    		}
    		setShapeColors();
    	}else {
    		JFrame frame = new JFrame();
    		JOptionPane.showMessageDialog(frame, "Rotation origin not found.");
    	}
    }
    
    //sets the rotation origin as the shared coodinates between connected lines
	public void setRotationOrigin() {
		Edge edge1 = shapes.get(0).getRotationEdge();
		Edge edge2 = edge1.getCorrespondingEdge();
		if(((Math.abs(edge1.getxCoords().get(0)-edge2.getxCoords().get(0))<THRESHOLD)&&(Math.abs(edge1.getyCoords().get(0)-edge2.getyCoords().get(0))<THRESHOLD))||((Math.abs(edge1.getxCoords().get(0)-edge2.getxCoords().get(1))<THRESHOLD)&&(Math.abs(edge1.getyCoords().get(0)-edge2.getyCoords().get(1))<THRESHOLD))) {
			xOrigin = edge1.getxCoords().get(0);
			yOrigin = edge1.getyCoords().get(0);
		}else {
			xOrigin = edge1.getxCoords().get(1);
			yOrigin = edge1.getyCoords().get(1);
		}
	}
	
	//moves all shapes the distance betweeen two sets of coordinates 
	public void moveAll(double x1, double y1, double x2, double y2) {
		double xChange = x2 - x1;
		double yChange = y2 - y1;
		for(Shape shape : shapes) {
			shape.moveShape(xChange, yChange);
		}
	}
    
        
    public void createControlLine(double x1, double y1, double x2, double y2) {
    	controlLine = new Line(x1, y1, x2, y2);
    }
    
    public void deleteControlLine() {
    	controlLine = null;
    }
    
    //Creates a new shape depending on the selected shapeState
    public void createShape(boolean shiftMod, double x1, double y1, double x2, double y2) {
    	Shape newShape = null;
    	if(shapeState == 6) {    		
    		if(!shiftMod) {
    			//creating a hexagon
    			newShape = new Shape(x1, y1+((y2-y1)/2), x1+((x2-x1)/4), y1, x2-((x2-x1)/4), y1, x2, y1+(y2-y1)/2, x2-((x2-x1)/4), y2, x1+((x2-x1)/4), y2);
    		}else {
    			//Creating equilateral hexagon
    			double radius = Math.min((x2-x1)/2, (y2-1)/2);
    			double xCentre = x1 + radius;
    			double yCentre = y1 + radius;
    			double[] xPoints = new double[6];
    			double[] yPoints = new double[6];
    			for (int i = 0; i<6; i++) {
    				xPoints[i] = xCentre + radius*Math.cos(i*60*Math.PI/180f);
    				yPoints[i] = yCentre + radius*Math.sin(i*60*Math.PI/180f);        			
    			}        		
    			newShape = new Shape(xPoints[3], yPoints[3], xPoints[4], yPoints[4], xPoints[5], yPoints[5], xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);

    		}    		
    	}else if(shapeState ==5){
    		//creating a rectangle
    		newShape = new Shape(x1, y1, x2, y1, x2, y2, x1, y2);
    	}else if(shapeState == 4){
    		//creating a square
    		double length = Math.min(x2-x1, y2-y1);
    		newShape = new Shape(x1, y1, (x1+length), y1, (x1+length), (y1+length), x1, (y1+length));
    	}else if(shapeState == 3){
    		if(!shiftMod) {
    			//creating a triangle
    			newShape = new Shape(x1, y2, x1+((x2-x1)/2), y1, x2, y2);
    		}else {
    			//Creating equilateral triangle
    			double radius = Math.min((x2-x1)/2, (y2-1)/2);
    			double xCentre = x1 + radius;
    			double yCentre = y1 + radius;
    			double[] xPoints = new double[3];
    			double[] yPoints = new double[3];
    			for (int i = 0; i<3; i++) {
    				xPoints[i] = xCentre + radius*Math.cos((30+(i*120))*Math.PI/180f);
    				yPoints[i] = yCentre + radius*Math.sin((30+(i*120))*Math.PI/180f);

    			}        		
    			newShape = new Shape(xPoints[1], yPoints[1], xPoints[2], yPoints[2], xPoints[0], yPoints[0]);
    		}        
    	}
    	if (shapeCreated) {
    		shapes.set(0,newShape);
    	}else {
    		shapes.add(0,newShape);
    	}        
    	shapeCreated = true; 
    }
    
    //fills a shape that contains the selected coordinates with the selected colour
    public void checkFill(double x, double y) {
    	Color fillColor = mainColor; 
    	if(!useMainColor) {
    		fillColor = secondaryColor;
    	}
    	int shapeCount = shapes.size()-1;
    	boolean found = false;
    	while(found == false && shapeCount>=0) {
    		if (shapes.get(shapeCount).checkShapeContainsPoint(x, y)) {
    			found = true;
    			shapes.get(shapeCount).setFillColour(fillColor);
    		}
    		shapeCount--;
    	}
    }
    
    //removes a shape that contains the selected coordinates
    public void deleteShape(double x, double y) {
    	int shapeCount = shapes.size()-1;
    	boolean found = false;
    	while(found == false && shapeCount>=0) {
    		if (shapes.get(shapeCount) != null) {
    			if (shapes.get(shapeCount).checkShapeContainsPoint(x, y)) {
    				found = true;
    				shapes.remove(shapeCount);
    			}
    		}
    		shapeCount--;
    	}
    }
    
    public ArrayList<Shape> getShapes(){
    	return shapes;
    }
	
	public boolean getShapeCreated(){
		return shapeCreated;
	}
 
    public void setShapeState(int state){
        shapeState = state;
    }
 
    public int getShapeState (){
        return shapeState;
    }
	
	public void setToolState(int state){
		toolState = state;
	}
	
	public int getToolState(){
		return toolState;
	}
	
	public void setMainColor(Color c) {
		mainColor = c;
	}
	
	public Color getMainColor() {
		return mainColor;
	}
	
	public void setSecondaryColor(Color c) {
		secondaryColor = c;
	}
	
	public Color getSecondaryColor() {
		return secondaryColor;
	}
	
	public void setUseMainColor(boolean useMainColor) {
		this.useMainColor = useMainColor;
	}
	
	public boolean getUseMainColor() {
		return useMainColor;
	}
	
	public void setUsePattern(boolean usePattern) {
		this.usePattern = usePattern;
	}
	
	public boolean getUsePattern() {
		return usePattern;
	}
	
	public ArrayList<Double> getTempLinesOriginalxCoords(){
		ArrayList<Double> xCoords = new ArrayList<Double>();
		for (Line line: tempLinesOriginal) {
			line.getLinexCoords(xCoords);
		}
		return xCoords;
	}
	
	public ArrayList<Double> getTempLinesOriginalyCoords(){
		ArrayList<Double> yCoords = new ArrayList<Double>();		
		for (Line line: tempLinesOriginal) {
			line.getLineyCoords(yCoords);
		}
		return yCoords;
	}
	
	public ArrayList<Double> getTempLinesMirrorxCoords(){
		ArrayList<Double> xCoords = new ArrayList<Double>();		
		for (Line line: tempLinesMirror) {
			line.getLinexCoords(xCoords);
		}
		return xCoords;
	}
	
	public ArrayList<Double> getTempLinesMirroryCoords(){
		ArrayList<Double> yCoords = new ArrayList<Double>();		
		for (Line line: tempLinesMirror) {
			line.getLineyCoords(yCoords);
		}
		return yCoords;
	}
	
	public ArrayList<Double> getxControls(){
		return xControls;
	}
	
	public ArrayList<Double> getyControls(){
		return yControls;
	}
	
	public boolean getLineStarted() {
		return lineStarted;
	}
	
	//clear all shapes except the original
	public void clearTessellations() {
		int numRemoves = shapes.size()-1;
		for(int i=0; i<numRemoves; i++) {
			shapes.remove(1);
		}
	}
	
	//clears all shapes and any temp lines
	public void clearAll() {
		shapeCreated = false;
		if(toolState == ADDINGLINES) {
			toolState = DRAWLINE;
			lineStarted = false;
			tempLinesOriginal.clear();
			tempLinesMirror.clear();
			lineNum = 0;
		}
		shapes.clear();
		
	}
	
	public Line getEditLine() {
		return editLine;
	}
 

}