package dataStructures;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

/**
 * Laget av ThinMatrix ( Se <a href="https://www.youtube.com/watch?v=z0jb1OBw45I">ThinMatrix, Skeleton animation video 4</a>)
 * This object contains all the mesh data for an animated model that is to be loaded into the VAO.
 * 
 * @author Karl
 *
 */
public class MeshData {

	private static final int DIMENSIONS = 3;

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private int[] jointIds;
	private float[] vertexWeights;
	
	private Vector3f vecMax;
	private Vector3f vecMin;

	public MeshData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
			int[] jointIds, float[] vertexWeights, Vector3f vecMin,Vector3f vecMax) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.jointIds = jointIds;
		this.vertexWeights = vertexWeights;
		this.vecMax = vecMax;
		this.vecMin = vecMin;
		
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

	public int[] getJointIds() {
		return jointIds;
	}
	
	public float[] getVertexWeights(){
		return vertexWeights;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getVertexCount() {
		return vertices.length / DIMENSIONS;
	}

}
