package models;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class BoundingBox {
	
	private Vector3f vecMin;
	private Vector3f vecMax;
	private Vector3f centerPosition;
	private static ArrayList<BoundingBox> AABBList = new ArrayList<BoundingBox>();
	
	public BoundingBox(Vector3f vecMin, Vector3f vecMax, Vector3f centerPosition) {
		this.vecMin = vecMin;
		this.vecMax = vecMax;
		this.centerPosition = centerPosition;
	}

	public boolean intersects(BoundingBox other){
		
		// Finner nåværende maksimum og minimum for AABB til spillern
		Vector3f max = Vector3f.add(vecMax, centerPosition, null);
		Vector3f min = Vector3f.add(vecMin, centerPosition, null);
		
		
		// Regner ut distansen mellom objektet sin minimum og spillern sin maks verdi. 
		// Og distansen mellom spillern sin minimum og objektet sin maks verdi
		Vector3f distance1 = Vector3f.sub(other.getMin(), max, null);
		Vector3f distance2 = Vector3f.sub(min, other.getMax(), null);
		
		
		// Finner den største verdien av distansene for hver akse
		float x = distance1.x > distance2.x ? distance1.x : distance2.x;
		float y = distance1.y > distance2.y ? distance1.y : distance2.y;
		float z = distance1.z > distance2.z ? distance1.z : distance2.z;

		
		// Finner den største verdien av x,y,z
		float big = x < y ? y : x;
		float biggest = big < z ? z : big;
		 
		// Hvis den største verdien er mindre enn 0, 
		// betyr det at det ikke er noen mellomrom mellom objektet og spillern på noen av aksene.
		if(biggest < 0)
			return true;

		return false;
	}
	
	

	public Vector3f getMin() {
		return vecMin;
	}
	public void setMin(Vector3f vecMin) {
		this.vecMin = vecMin;
	}
	public Vector3f getMax() {
		return vecMax;
	}
	public void setMax(Vector3f vecMax) {
		this.vecMax = vecMax;
	}
	public Vector3f getCenterPosition() {
		return centerPosition;
	}
	public void setCenterPosition(Vector3f centerPosition) {
		this.centerPosition = centerPosition;
	}
		public static ArrayList<BoundingBox> getAABBList() {
		return AABBList;
	}
	

}
