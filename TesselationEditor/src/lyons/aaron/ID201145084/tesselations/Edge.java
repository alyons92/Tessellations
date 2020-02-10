/**
 * 
 */
package lyons.aaron.ID201145084.tesselations;

/**
 * @author alyons
 *
 */
public class Edge extends Line {
	private Edge correspondingEdge;
	private Boolean rowEdge = false;
	private Boolean columnEdge = false;
	private Boolean rotationEdge = false;
	private Boolean alternateEdge = false;
	private int edgeNum;
	private int maxEdges;
	private final double THRESHOLD = 0.0001;
	
	public Edge(double x1, double y1, double x2, double y2, int edgeNum, int maxEdges) {
		super(x1, y1, x2, y2);
		this.edgeNum = edgeNum;
		this.maxEdges = maxEdges;
		sourceEdge = this;
		isEdge = true;
		
	}
	
	//------------------------------Getters and Setters Start----------------------------------------
	
	/**
	 * @return the edgeNum
	 */
	public int getEdgeNum() {
		return edgeNum;
	}

	/**
	 * @return the maxEdges
	 */
	public int getMaxEdges() {
		return maxEdges;
	}

	/**
	 * @param maxEdges the maxEdges to set
	 */
	public void setMaxEdges(int maxEdges) {
		this.maxEdges = maxEdges;
	}

	/**
	 * @param edgeNum the edgeNum to set
	 */
	public void setEdgeNum(int edgeNum) {
		this.edgeNum = edgeNum;
	}

	
	/**
	 * @return the correspondingEdge
	 */
	public Edge getCorrespondingEdge() {
		return correspondingEdge;
	}


	/**
	 * @param correspondingEdge the correspondingEdge to set
	 */
	public void setCorrespondingEdge(Edge correspondingEdge) {		
		this.correspondingEdge = correspondingEdge;	
		correspondingLine = correspondingEdge;
	}


	/**
	 * @return the rowEdge
	 */
	public Boolean getRowEdge() {
		return rowEdge;
	}


	/**
	 * @param rowEdge the rowEdge to set
	 */
	public void setRowEdge(Boolean rowEdge) {
		this.rowEdge = rowEdge;
	}


	/**
	 * @return the columnEdge
	 */
	public Boolean getColumnEdge() {
		return columnEdge;
	}


	/**
	 * @param columnEdge the columnEdge to set
	 */
	public void setColumnEdge(Boolean columnEdge) {
		this.columnEdge = columnEdge;
	}


	/**
	 * @return the rotationEdge
	 */
	public Boolean getRotationEdge() {
		return rotationEdge;
	}


	/**
	 * @param rotationEdge the rotationEdge to set
	 */
	public void setRotationEdge(Boolean rotationEdge) {
		this.rotationEdge = rotationEdge;
	}
		
	
	/**
	 * @return the alternateEdge
	 */
	public Boolean getAlternateEdge() {
		return alternateEdge;
	}

	/**
	 * @param alternateEdge the alternateEdge to set
	 */
	public void setAlternateEdge(Boolean alternateEdge) {
		this.alternateEdge = alternateEdge;
	}

	
	//------------------------------Getters and Setters End----------------------------------------
	
	
	
	public double calcxChange(Edge edge) {
		return (edge.xCoords.get(0)-xCoords.get(1));
	}
	
	public double calcyChange(Edge edge) {
		return (edge.yCoords.get(0)-yCoords.get(1));
	}
	
	public double calcAngle(Edge edge) {	
		double angle1 = Math.atan2(Math.abs(getChangeInY()), Math.abs(getChangeInX()));
		double angle2 = Math.atan2(Math.abs(edge.getChangeInY()), Math.abs(edge.getChangeInX()));		
		double resultAngle = Math.abs(angle1-angle2);
		if(Math.abs(angle1-angle2)<THRESHOLD) {
			resultAngle = Math.PI+(2*angle1);			
		}
		if(resultAngle>Math.PI) {
			resultAngle = 2*Math.PI-resultAngle;
		}
		if(maxEdges == 6) {
			resultAngle = Math.PI-resultAngle;
		}		
		if(edgeNum - edge.getEdgeNum() == 1 || edgeNum - edge.getEdgeNum() == 1 - maxEdges) {
			return resultAngle;
		}else {
			return -1*resultAngle;
		}	
	}

}