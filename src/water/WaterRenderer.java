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
 
public class WaterRenderer {
 
    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
 
    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }
 
    public void render(List<WaterTile> water, Camera camera) {
        prepareRender(camera);  
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatric(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), -90, 0, 0,
                    WaterTile.TILE_SIZE);
            
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertextCount());
        }
        unbind();
    }
     
    private void prepareRender(Camera camera){
        shader.start();
        shader.loadViewMatrix(camera);
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
    }
     
    private void unbind(){ 
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
 
    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
 
}