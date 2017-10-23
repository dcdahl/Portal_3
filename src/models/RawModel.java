package models;

public class RawModel {
	
	//vertext array object
	
	private int vaoID;
	private int vertextCount;
	
	
	public RawModel(int vaoID, int vertextCount) {
		this.vaoID = vaoID;
		this.vertextCount = vertextCount;
	}
	
	
	/*
	 * Get Set
	 */
	public int getVaoID() {
		return vaoID;
	}
	public int getVertextCount() {
		return vertextCount;
	}
	
}
