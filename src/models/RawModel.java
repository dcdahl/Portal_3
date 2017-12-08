package models;

import org.lwjgl.util.vector.Vector3f;

public class RawModel {
	
	//vertext array object
	
	private int vaoID;
	private int vertextCount;
	private Vector3f vecMax;
	private Vector3f vecMin;
	
	
	public RawModel(int vaoID, int vertextCount,Vector3f vecMin, Vector3f vecMax) {
		this.vaoID = vaoID;
		this.vertextCount = vertextCount;
		this.vecMin = vecMin;
		this.vecMax = vecMax;
	}
	
	
	
	   
	/*
	 * Get Set
	 */
	
	
	public int getVaoID() {
		return vaoID;
	}
	public Vector3f getVecMax() {
		return vecMax;
	}




	public void setVecMax(Vector3f vecMax) {
		this.vecMax = vecMax;
	}




	public Vector3f getVecMin() {
		return vecMin;
	}




	public void setVecMin(Vector3f vecMin) {
		this.vecMin = vecMin;
	}




	public int getVertextCount() {
		return vertextCount;
	}
	
}
