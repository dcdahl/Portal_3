package water;
 
import java.util.List;
 
import models.RawModel;
 
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
 
import render.DisplayManager;
import render.Loader;
import toolbox.Maths;
import entiies.Camera;
import entiies.Light;
 
/**
 * Skrevet fra ThinMatrix water tutorial 
 * @author Marius
 *
 */
public class WaterRenderer {
 
	//Navnet på DuDv mappet
	private static final String DUDV_MAP = "waterDUDV";
	//Navnet på normal mappet
	private static final String NORMAL_MAP = "NormalMap";
	//Farten som move factoren skal endre seg på aka bølge farten
	private static final float WAVE_SPEED = 0.05f;
	
    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
    
    //ID til dudvmap og normal map
    private int dudvTexture;
    private int normalMap;
    
    private float moveFactor = 0;
 
    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        dudvTexture = loader.loadTexture(DUDV_MAP);
        normalMap = loader.loadTexture(NORMAL_MAP);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }
 
    public void render(List<WaterTile> water, Camera camera, Light sun) {
        prepareRender(camera, sun);  
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatric(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertextCount());
        }
        unbind();
    }
     
    private void prepareRender(Camera camera, Light sun){
        shader.start();
        shader.loadViewMatrix(camera);
        //Forvrengelsen/bølgene skal bevege seg en distanse per sekund
        moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
        //Looper tilbake til 0 når move faktoren når 1
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        shader.loadLight(sun);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        //Binder teksturene/maps   til de riktige texture unitsene
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
    }
     
    private void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
 
    private void setUpVAO(Loader loader) {
        //Kun x og z verdier for tilen her, vi setter y verdiene til 0 i vertex shaderer
    	//fordi vi skal ha en flat tile som ligger horisontalt
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
 
}