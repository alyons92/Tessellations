package lyons.aaron.ID201145084.tesselations;
import java.io.Serializable;
import java.util.*;
/**
 * @author alyons
 *
 */
public class Line implements Serializable{
	protected ArrayList<Double> xCoords = new ArrayList<Double>();
	protected ArrayList<Double> yCoords = new ArrayList<Double>();
	protected ArrayList<Line> lines = new ArrayList<Line>();
	protected double xCurveControl;
	protected double yCurveControl;
	protected Boolean curve = false;
	protected int lineThickness;
	protected String lineColour;
	private final double THRESHOLD = 0.0001;
	protected boolean startAddition = false;
	protected boolean endAddition = false;
	protected Line sourceLine;
	protected Edge sourceEdge;
	protected Line correspondingLine;
	protected Boolean isEdge = false;
	
	public Line(double x1, double y1, double x2, double y2) {
		//System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);		
		this.xCoords.add(x1);		
		this.xCoords.add(x2);
		this.yCoords.add(y1);
		this.yCoords.add(y2);
		this.xCurveControl = x1 + (getChangeInX()/2);
		this.yCurveControl = y1 + (getChangeInY()/2);
		
	}
	
	//------------------------------Getters and Setters Start----------------------------------------
	/**
	 * @return the xCoords
	 */
	public ArrayList<Double> getxCoords() {
		return xCoords;
	}

	/**
	 * @param xCoords the xCoords to set
	 */
	public void setxCoords(ArrayList<Double> xCoords) {
		this.xCoords = xCoords;
	}

	/**
	 * @return the yCoords
	 */
	public ArrayList<Double> getyCoords() {
		return yCoords;
	}

	/**
	 * @param yCoords the yCoords to set
	 */
	public void setyCoords(ArrayList<Double> yCoords) {
		this.yCoords = yCoords;
	}

	/**
	 * @return the lines
	 */
	public ArrayList<Line> getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	/**
	 * @return the xCurveControl
	 */
	public double getxCurveControl() {
		return xCurveControl;
	}

	/**
	 * @param xCurveControl the xCurveControl to set
	 */
	public void setxCurveControl(double xCurveControl) {
		this.xCurveControl = xCurveControl;
	}

	/**
	 * @return the yCurveControl
	 */
	public double getyCurveControl() {
		return yCurveControl;
	}

	/**
	 * @param yCurveControl the yCurveControl to set
	 */
	public void setyCurveControl(double yCurveControl) {
		this.yCurveControl = yCurveControl;
	}

	/**
	 * @return the curve
	 */
	public Boolean getCurve() {
		return curve;
	}

	/**
	 * @param curve the curve to set
	 */
	public void setCurve(Boolean curve) {
		this.curve = curve;
	}

	/**
	 * @return the lineThickness
	 */
	public int getLineThickness() {
		return lineThickness;
	}

	/**
	 * @param lineThickness the lineThickness to set
	 */
	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}

	/**
	 * @return the lineColour
	 */
	public String getLineColour() {
		return lineColour;
	}

	/**
	 * @param lineColour the lineColour to set
	 */
	public void setLineColour(String lineColour) {
		this.lineColour = lineColour;
	}
	
	/**
	 * @param sourceEdge the sourceEdge to set
	 */
	public void setSourceEdge(Edge sourceEdge) {
		this.sourceEdge = sourceEdge;
	}
	
	/**
	 * @param the correspondingLine
	 */
	public Edge getSourceEdge() {
		return sourceEdge;
	}
	
	/**
	 * @param correspondingLine the correspondingLine to set
	 */
	public void setCorrespondingLine(Line correspondingLine) {
		this.correspondingLine = correspondingLine;
	}
	
	/**
	 * @param the correspondingLine
	 */
	public Line getCorrespondingLine() {
		return correspondingLine;
	}
	
	/**
	 * @param lineColour the lineColour to set
	 */
	public void setIsEdge(Boolean isEdge) {
		this.isEdge = isEdge;
	}
	
	/**
	 * @param the isEdge
	 */
	public Boolean getIsEdge() {
		return isEdge;
	}
	
	public boolean isStartAddition() {
		return startAddition;
	}

	public void setStartAddition(boolean startAddition) {
		this.startAddition = startAddition;
	}

	public boolean isEndAddition() {
		return endAddition;
	}

	public void setEndAddition(boolean endAddition) {
		this.endAddition = endAddition;
	}

	public Line getSourceLine() {
		return sourceLine;
	}

	public void setSourceLine(Line sourceLine) {
		this.sourceLine = sourceLine;
	}

	
	//------------------------------Getters and Setters End----------------------------------------
	
	public ArrayList<Line> getAllLines(ArrayList<Line> allLines){		
		allLines.add(this);
		if (lines.size() > 0) {
			for (Line line : lines) {				
				line.getAllLines(allLines);
			}
		}
		
		return allLines;
	}
	
	public double calcDistToPoint(double x0, double y0) {
		double y1 =getyCoords().get(0);
		double y2 = getyCoords().get(1);
		double x1 =getxCoords().get(0);
		double x2 = getxCoords().get(1);
		double distance = 1000;
		if(checkClosestPoint(x0, y0)) {
			distance = Math.abs((((y2-y1)*x0) - ((x2-x1)*y0) + (x2*y1) - (y2*x1)))/(Math.sqrt((Math.pow((y2-y1), 2))+(Math.pow((x2-x1), 2))));
		}
		return distance;
	}
	
	//checking if the perpendicular intersector of the point to the line is within the line bounds
	public boolean checkClosestPoint(double x0, double y0) {
		double y1 =getyCoords().get(0);
		double y2 = getyCoords().get(1);
		double x1 =getxCoords().get(0);
		double x2 = getxCoords().get(1);
	
		double a = y2 - y1;
		double b = (x2 - x1)*-1;
		double c = (x2*y1)-(y2*x1);
	
		double xPoint = (((b*((b*x0) - (a*y0)))-(a*c))/(Math.pow(a, 2) + Math.pow(b, 2))); 
		double yPoint = (((a*(((0-b)*x0) + (a*y0)))-(b*c))/(Math.pow(a, 2) + Math.pow(b, 2)));
		if(xPoint>=Math.min(x1, x2) && xPoint<=Math.max(x1, x2) && yPoint>=Math.min(y1, y2) && yPoint<=Math.max(y1, y2)) {
			return true;
		}else {
				return false;
		}			
	}
	
	public ArrayList<Line> cloneLines(){
		ArrayList<Line> newLines = new ArrayList<Line>();
		if(lines.size()>0) {
		
			for(Line line : lines) {
				Line newLine = new Line(line.getxCoords().get(0), line.getyCoords().get(0), line.getxCoords().get(1), line.getyCoords().get(1));
				newLine.setLines(line.cloneLines());
				if (line.getCurve()) {
					newLine.setxCurveControl(line.getxCurveControl());
					newLine.setyCurveControl(line.getyCurveControl());
					newLine.setCurve(true);
				}
				newLines.add(newLine);
			}
		}
		return newLines;
	}
	
	public double calcDistBetweenPoints(double x1, double y1, double x2, double y2) {
		double distance = Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
		return distance;
	}
	
	public void attachLine(Line line) {
		int count = 0;
		boolean positionFound = false;
		int position = 0;
		if(lines.size()>0) { 
			
			double newLineDist = calcDistBetweenPoints(getxCoords().get(0), getyCoords().get(0), line.getxCoords().get(0), line.getyCoords().get(0));
			while (positionFound == false && count < lines.size()) {
				double currentLineDist = calcDistBetweenPoints(getxCoords().get(0), getyCoords().get(0), lines.get(count).getxCoords().get(0), lines.get(count).getyCoords().get(0));
				if(newLineDist<currentLineDist) {
					positionFound = true;
					position = count;
				}	
				count++;
			}
		}
		
		if (positionFound == false) {
			position = count;
		}
		lines.add(position, line);
		
	}
	
	public double getChangeInY() {
		return this.yCoords.get(1)-this.yCoords.get(0);
	}
	
	public double getChangeInX() {
		return this.xCoords.get(1)-this.xCoords.get(0);
	}
	
	public double getGradient() {	
		if (Math.abs(getChangeInX())<THRESHOLD) {
			//no change in  x means a vertical line 
			return 0;
		}else {
			//if no change in y, will return 0, meaning a horizontal line
			return getChangeInY()/getChangeInX();
		}
	}
	
	//test if the line has a positive gradient 
	public boolean testPosGradient() {		
		if ((getGradient())>0) {
			return true; 
		}else {
			return false;
		}
	}

	//remove the connected lines of a line, then remove the line from it's owner 
	public void removeLine(Line lineOwner, int position) {		
		int numLines = lines.size();
		if(numLines > 0) {
			for (int i = 0; i<numLines; i++) {
				lines.get(i).removeLine(this, i);
				
			}			
		}		 
		lineOwner.getLines().remove(position);
	}
	
	public double calcLength() {	
		double x = xCoords.get(0) - xCoords.get(1);
		double y = yCoords.get(0) - yCoords.get(1);
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); 
	}
	
	public void moveLines(double xChange, double yChange) {		
		int numLines = lines.size();
		if(numLines > 0) {
			for (int i = 0; i<numLines; i++) {				
				lines.get(i).moveLines(xChange, yChange);
			}
		}
		for (int i=0; i<xCoords.size(); i++) {			
			double x = xCoords.get(i);
			x = x + xChange;
			xCoords.set(i, x);
			double y = yCoords.get(i);
			y = y + yChange;
			yCoords.set(i, y);			
		}
		xCurveControl = xCurveControl + xChange;
		yCurveControl = yCurveControl + yChange;
	}
	
	public void rotateLines(double xOrigin, double yOrigin, double angle) {
		int numLines = lines.size();
		if(numLines > 0) {
			for (int i = 0; i<numLines; i++) {
				lines.get(i).rotateLines(xOrigin, yOrigin, angle);
			}
		}
		for (int n = 0; n<2; n++) {
			double originalxCoord = xCoords.get(n);
			double originalyCoord = yCoords.get(n);
			double newxCoord = (((originalxCoord - xOrigin)*Math.cos(angle)) - ((originalyCoord - yOrigin)*Math.sin(angle))) + xOrigin;
			double newyCoord = (((originalyCoord - yOrigin)*Math.cos(angle)) + ((originalxCoord - xOrigin)*Math.sin(angle))) + yOrigin;
			xCoords.set(n, newxCoord);
			yCoords.set(n, newyCoord);
		}
		if(curve) {
			double xCurveOriginal = xCurveControl;
			double yCurveOriginal = yCurveControl;
			xCurveControl = (((xCurveOriginal - xOrigin)*Math.cos(angle)) - ((yCurveOriginal - yOrigin)*Math.sin(angle))) + xOrigin;
			yCurveControl = (((yCurveOriginal - yOrigin)*Math.cos(angle)) + ((xCurveOriginal - xOrigin)*Math.sin(angle))) + yOrigin;  
		}else {
			xCurveControl = xCoords.get(0) + (getChangeInX()/2);
			yCurveControl = yCoords.get(0) + (getChangeInY()/2);
		}
		
	}
	
	public void rotateControlPoints(double xOrigin, double yOrigin, double angle) {	
		if(curve) {
			double xCurveOriginal = xCurveControl;
			double yCurveOriginal = yCurveControl;
			xCurveControl = (((xCurveOriginal - xOrigin)*Math.cos(angle)) - ((yCurveOriginal - yOrigin)*Math.sin(angle))) + xOrigin;
			yCurveControl = (((yCurveOriginal - yOrigin)*Math.cos(angle)) + ((xCurveOriginal - xOrigin)*Math.sin(angle))) + yOrigin;  
		}else {
			xCurveControl = xCoords.get(0) + (getChangeInX()/2);
			yCurveControl = yCoords.get(0) + (getChangeInY()/2);
		}
	}
	
	public void getLinexCoords(ArrayList<Double> allxCoords) {
		allxCoords.add(xCoords.get(0));		
		int numLines = lines.size();		
		if(numLines > 0) {
			for (int i = 0; i<numLines; i++) {
				lines.get(i).getLinexCoords(allxCoords);				
			}
			if((Math.abs(lines.get(numLines-1).getyCoords().get(0)-yCoords.get(1))>THRESHOLD) ||(Math.abs(lines.get(numLines-1).getxCoords().get(0)-xCoords.get(1))>THRESHOLD)) {
				allxCoords.add(xCoords.get(1));
			}
		}else {		
			allxCoords.add(xCoords.get(1));
		}		
	}
	
	public int getCurveCoords(ArrayList<Double> curveXCoords, ArrayList<Double> curveYCoords, ArrayList<Integer> curvePositions, int curveCount) {
		curveCount++;
		if(getCurve()) {
			curvePositions.add(curveCount);
			curveXCoords.add(xCurveControl);
			curveYCoords.add(yCurveControl);
		}
		int numLines = lines.size();		
		if(numLines > 0) {
			for (int i = 0; i<numLines; i++) {
				curveCount = lines.get(i).getCurveCoords(curveXCoords, curveYCoords, curvePositions, curveCount);	
				if(lines.get(i).getLines().size()==0) {					
				}
			}
			if((Math.abs(lines.get(numLines-1).getyCoords().get(0)-yCoords.get(1))>THRESHOLD) ||(Math.abs(lines.get(numLines-1).getxCoords().get(0)-xCoords.get(1))>THRESHOLD)) {
				curveCount++;
			}
		}else {
			curveCount++;
		}		
		return curveCount;
	}
	
	
	public void getLineyCoords(ArrayList<Double> allyCoords) {
		allyCoords.add(yCoords.get(0));
		int numLines = lines.size();
		if(numLines > 0) {
			for (int i = 0; i<numLines; i++) {
				lines.get(i).getLineyCoords(allyCoords);
			}			
			if ((Math.abs(lines.get(numLines-1).getyCoords().get(0)-yCoords.get(1))>THRESHOLD) ||(Math.abs(lines.get(numLines-1).getxCoords().get(0)-xCoords.get(1))>THRESHOLD)) {
				allyCoords.add(yCoords.get(1));
			}			
		}else {		
			allyCoords.add(yCoords.get(1));
		}
	}
	
	public Boolean checkContains(double x1, double y1) {		
		//if the point is not between the bounds of the line, return false
		if (((xCoords.get(0)>x1) && (xCoords.get(1)>x1)) ||((xCoords.get(0)<x1) && (xCoords.get(1)<x1)) || ((yCoords.get(0)>y1) && (yCoords.get(1)>y1)) ||((yCoords.get(0)<y1) && (yCoords.get(1)<y1))) {
			return false;			
		}else if ((xCoords.get(0).equals(xCoords.get(1))) && (xCoords.get(0).equals(x1))){
			return true;
		}else if ((yCoords.get(0).equals(yCoords.get(1))) && (yCoords.get(0).equals(y1))) {
			return true;
		}else if (((y1 - yCoords.get(0))/(yCoords.get(1) - yCoords.get(0))) == ((x1 - xCoords.get(0))/(xCoords.get(1) - xCoords.get(0)))) {
			return true;
		}else {
			return false;
		}
	}
	
	
}