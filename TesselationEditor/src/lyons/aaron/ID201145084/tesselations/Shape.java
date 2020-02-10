/**
 * 
 */
package lyons.aaron.ID201145084.tesselations;
import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.*;

/**
 * @author alyons
 *
 */
public class Shape implements Serializable{
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private Color fillColour = Color.WHITE;
	private int numEdges;
	ArrayList<Integer> curvePositions;
	ArrayList<Double> curveXCoords;
	ArrayList<Double> curveYCoords;
	
	
	public Shape(int numEdges) {	
		this.numEdges = numEdges;
	}
	
	//constructor for triangle
	public Shape(double x1, double y1, double x2, double y2, double x3, double y3) {
		edges.add(new Edge(x1, y1, x2, y2, 1, 3));
		edges.add(new Edge(x2, y2, x3, y3, 2, 3));
		edges.add(new Edge(x3, y3, x1, y1, 3, 3));
		numEdges = 3;
	}
	
	//Constructor for square and rectangle 
	public Shape(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		//System.out.println("Making square");
		//System.out.println(x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3 + " " + x4 + " " + y4);		
		edges.add(new Edge(x1, y1, x2, y2, 1, 4));		
		edges.add(new Edge(x2, y2, x3, y3, 2, 4));
		edges.add(new Edge(x3, y3, x4, y4, 3, 4));
		edges.add(new Edge(x4, y4, x1, y1, 4, 4));
		numEdges = 4;
	}
	
	//Constructor for hexagon
	public Shape(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double x5, double y5, double x6, double y6) {
		edges.add(new Edge(x1, y1, x2, y2, 1, 6));
		edges.add(new Edge(x2, y2, x3, y3, 2, 6));
		edges.add(new Edge(x3, y3, x4, y4, 3, 6));
		edges.add(new Edge(x4, y4, x5, y5, 4, 6));
		edges.add(new Edge(x5, y5, x6, y6, 5, 6));
		edges.add(new Edge(x6, y6, x1, y1, 6, 6));
		numEdges = 6;
		System.out.println(edges.get(4).getxCoords() + " " + edges.get(4).getyCoords());
	}
	
	
	//------------------------------Getters and Setters Start----------------------------------------
	
	
	/**
	 * @return the edges
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	
	/**
	 * @return the fillColour
	 */
	public Color getFillColour() {
		return fillColour;
	}

	/**
	 * @param fillColour the fillColour to set
	 */
	public void setFillColour(Color fillColour) {
		this.fillColour = fillColour;
	}

	/**
	 * @return the numEdges
	 */
	public int getNumEdges() {
		return numEdges;
	}

	/**
	 * @param numEdges the numEdges to set
	 */
	public void setNumEdges(int numEdges) {
		this.numEdges = numEdges;
	}
	
	//------------------------------Getters and Setters End----------------------------------------

	public void addEdge(Edge edge) {
		edges.add(edge);
	}
	
	//Moves a shape by moving the coordinates of each line by the change in x/y
	public void moveShape(double xChange, double yChange) {
		for (Edge edge: edges) {
			edge.moveLines(xChange, yChange);
		}		
	}
	
	//rotates a shape by rotating each coordinate about the origin point by the angle given
	public void rotateShape(double xOrigin, double yOrigin, double angle) {
		for (Edge edge: edges) {
			edge.rotateLines(xOrigin, yOrigin, angle);
		}
	}

	//returns the edge that is a row type if there is one
	public Edge getRowEdge(){
		Edge foundEdge = null;
		for (Edge edge: edges) {			
			if (edge.getRowEdge()) {				
				foundEdge = edge;
			}			
		}
		return foundEdge;
	}
	
	//returns the edge that is a column type if there is one
	public Edge getColumnEdge() {
		Edge foundEdge = null;
		for (Edge edge: edges) {			
			if (edge.getColumnEdge()) {
				foundEdge = edge;
			}			
		}
		return foundEdge;
	}
	
	//returns the edge that is a rotation type if there is one
	public Edge getRotationEdge() {
		Edge foundEdge = null;
		for (Edge edge: edges) {
			if (edge.getRotationEdge()) {
				foundEdge = edge;
			}			
		}
		return foundEdge;
	}
	
	//checks if there is an edge of type rotation 
	public boolean checkRotation() {
		boolean rotation = false;
		for (Edge edge: edges) {
			if(edge.getRotationEdge()) {
				rotation = true;
			}
		}
		return rotation;
	}
	
	//removes the rotation type from all edges
	public void resetRotation() {
		for (Edge edge: edges) {
			edge.setRotationEdge(false);
		}
	}
	
	//returns an array of all the x coordinates for the shape
	public ArrayList<Double> getAllxCoords(){
		ArrayList<Double> allxCoords = new ArrayList<Double>();	
		for (Edge edge : edges) {
			edge.getLinexCoords(allxCoords);
		}
		return allxCoords;
	}
	
	//creates an array of y and x coordinates
	//creates an array of integers that represents the position of the line that a curve is found at
	public void getCurvePoints() {
		curvePositions = new ArrayList<Integer>();
		curveXCoords = new ArrayList<Double>();
		curveYCoords = new ArrayList<Double>();
		int curveCount = 0;
		for(Edge edge: edges) {			
			curveCount = edge.getCurveCoords(curveXCoords, curveYCoords, curvePositions, curveCount);
		}		
	}
	
	//returns an array of all the y coordinates for the shape
	public ArrayList<Double> getAllyCoords(){
		ArrayList<Double> allyCoords = new ArrayList<Double>();
		for (Edge edge : edges) {
			edge.getLineyCoords(allyCoords);
		}
		return allyCoords;
	}
	
	//returns the lowest x coordinate of all x coordinates of the shape
	public double getMinX() {
		ArrayList<Double> allEdgeX = new ArrayList<Double>();
		for (Edge edge : edges) {
			allEdgeX.addAll(edge.getxCoords());
		}
		double minX = allEdgeX.get(0);
		for(double x : allEdgeX) {
			if (x < minX) {
				minX = x;
			}
		}
		return minX;
	}
	
	//returns the highest x coordinate of all x coordinates of the shape
	public double getMaxX() {
		ArrayList<Double> allEdgeX = new ArrayList<Double>();
		for (Edge edge : edges) {
			allEdgeX.addAll(edge.getxCoords());
		}
		double maxX = allEdgeX.get(0);
		for(double x : allEdgeX) {
			if (x > maxX) {
				maxX = x;
			}
		}
		return maxX;
	}
	
	//returns the lowest y coordinate of all y coordinates of the shape
	public double getMinY() {
		ArrayList<Double> allEdgeY = new ArrayList<Double>();
		for (Edge edge : edges) {
			allEdgeY.addAll(edge.getyCoords());
		}
		double minY = allEdgeY.get(0);
		for(double y : allEdgeY) {
			if (y < minY) {
				minY = y;
			}
		}
		return minY;
	}
	
	//returns the highest y coordinate of all y coordinates of the shape
	public double getMaxY() {
		ArrayList<Double> allEdgeY = new ArrayList<Double>();
		for (Edge edge : edges) {
			allEdgeY.addAll(edge.getyCoords());
		}
		double maxY = allEdgeY.get(0);
		for(double y : allEdgeY) {
			if (y > maxY) {
				maxY = y;
			}
		}
		return maxY;
	}
	
	//creates a GeneralPath to draw the shape by traversing to each coordinate in sequence 
	public GeneralPath makePath() {
		getCurvePoints();
		ArrayList<Double> xCoords = getAllxCoords();
		ArrayList<Double> yCoords = getAllyCoords();
		int curveCount = 0;
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xCoords.size());
		polyline.moveTo(xCoords.get(0), yCoords.get(0));		
		for (int index = 1; index < xCoords.size(); index++){
			if(curvePositions.contains(index)) {
				polyline.quadTo(curveXCoords.get(curveCount), curveYCoords.get(curveCount), xCoords.get(index), yCoords.get(index));
				curveCount++;
			}else {
				polyline.lineTo(xCoords.get(index), yCoords.get(index));
			}
		}
		return polyline;
	}
	
	//checks if the shape contains the points received, returning true or false
	public boolean checkShapeContainsPoint(double x, double y) {
		GeneralPath path = makePath();
		if(path.contains(x, y)) {
			return true;
		}else {
			return false;
		}
	}
}