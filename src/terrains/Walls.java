package terrains;

import models.RawModel;
import render.Loader;
import textures.ModelTexture;

public class Walls {

	private static final float SIZE = 100;
	private static final int VERTEX_COUNT = 64;
	
	private float x;
	private float y;
	private RawModel model;
	private ModelTexture texture;
	
	public Walls(int gridX, int gridY, Loader loader, ModelTexture texture){
		this.texture = texture;
		this.x = gridX * SIZE;
		this.y = gridY * SIZE;
		this.model = generateTerrain(loader);
		
	}


	// Copiert direkte fra ThinMatrix for � generere terrainet
	 private RawModel generateTerrain(Loader loader){
	        int count = VERTEX_COUNT * VERTEX_COUNT;
	        float[] vertices = new float[count * 3];
	        float[] normals = new float[count * 3];
	        float[] textureCoords = new float[count*2];
	        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
	        int vertexPointer = 0;
	        for(int i=0;i<VERTEX_COUNT;i++){
	            for(int j=0;j<VERTEX_COUNT;j++){
	                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
	                vertices[vertexPointer*3+1] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
	                vertices[vertexPointer*3+2] = 0;
	                normals[vertexPointer*3] = 0;
	                normals[vertexPointer*3+1] = 1;
	                normals[vertexPointer*3+2] = 0;
	                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
	                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
	                vertexPointer++;
	            }
	        }
	        int pointer = 0;
	        for(int gy=0;gy<VERTEX_COUNT-1;gy++){
	            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
	                int topLeft = (gy*VERTEX_COUNT)+gx;
	                int topRight = topLeft + 1;
	                int bottomLeft = ((gy+1)*VERTEX_COUNT)+gx;
	                int bottomRight = bottomLeft + 1;
	                indices[pointer++] = topLeft;
	                indices[pointer++] = bottomLeft;
	                indices[pointer++] = topRight;
	                indices[pointer++] = topRight;
	                indices[pointer++] = bottomLeft;
	                indices[pointer++] = bottomRight;
	            }
	        }
	        return loader.loadToVAO(vertices, indices, textureCoords,normals);
	    }
	
	
	
	// Get Set
	public float getX() {
		return x;
	}

	public float getZ() {
		return y;
	}

	public RawModel getModel() {
		return model;
	}

	public ModelTexture getTexture() {
		return texture;
	}

}
