package textures;

import java.nio.ByteBuffer;

// Denne klassen er hentet direkte fra ThinMatrix
public class CubemapTexture {
	private int width;
    private int height;
    private ByteBuffer buffer;
     
    
    public CubemapTexture(ByteBuffer buffer, int width, int height){
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }
     
    
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public ByteBuffer getBuffer(){
        return buffer;
    }
}
