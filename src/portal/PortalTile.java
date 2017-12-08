package portal;

/**
 * Modifisering av vann tutorial fra ThinMatrix
 */
import org.lwjgl.util.vector.Vector3f;

public class PortalTile {
     
    public static final float TILE_SIZE = 20;
    
    private float x,y,z;
	private Vector3f position;
	private Vector3f normalVector;
     
    public PortalTile(float centerX, float centerZ, float centerY){
        this.x = centerX;
        this.z = centerZ;
        this.y = centerY;
        this.position = new Vector3f(centerX,centerY,centerZ);
        //Slik at du kun kan se gjennom portalen fra en side. Andre siden blir da usynlig
        this.normalVector = new Vector3f(0,0,-1);
    }
    
    public Vector3f getNormalVector(){
    	return normalVector;
    }
    
    public Vector3f getPosition(){
    	return position;
    }
    
    public float getHeight() {
        return y;
    }
 
    public float getX() {
        return x;
    }
 
    public float getZ() {
        return z;
    }
}
 
 
 