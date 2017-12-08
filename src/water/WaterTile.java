package water;

/**
 * Skrevet fra ThinMatrix water tutorial 
 * @author Marius
 *
 */
public class WaterTile {
     
	//St�rrelsen p� vannflisen
    public static final float TILE_SIZE = 600;
    
    //H�yden er det samme som y verdien
    private float height;
    private float x,z;
     
    public WaterTile(float centerX, float centerZ, float height){
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
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