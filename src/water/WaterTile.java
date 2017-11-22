package water;

import java.util.Vector;

import org.lwjgl.util.vector.Vector3f;

public class WaterTile {
     
    public static final float TILE_SIZE = 20;
     
    private float height;
    private float x,z;

	private Vector3f position;

	private Vector3f normalVector;
     
    public WaterTile(float centerX, float centerZ, float height){
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
        this.position = new Vector3f(centerX,height,centerZ);
        this.normalVector = new Vector3f(0,0,-1);
    }
    
    public Vector3f getNormalVector()
    {
    	return normalVector;
    }
    
    
    public float getHeight() {
        return height;
    }
 
    public float getX() {
        return x;
    }
 
    public float getZ() {
        return z;

    }
}
 
 
 