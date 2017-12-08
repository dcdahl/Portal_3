package portal;
 
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
  * Modifisering av vann tutorial fra ThinMatrix
  * @author Marius
  *
  */
public class PortalRenderer {
 
    private RawModel quad;
    private PortalShader shader;
    private PortalFrameBuffers fbos;
 
    public PortalRenderer(Loader loader, PortalShader shader, Matrix4f projectionMatrix, PortalFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }
 
    public void render(List<PortalTile> portal, Camera camera) {
        prepareRender(camera);  
        for (PortalTile tile : portal) {
            Matrix4f modelMatrix = Maths.createTransformationMatric(
                    tile.getPosition(), 0, 0, 0,
                    PortalTile.TILE_SIZE);
            
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
    	//I dette tilfellet setter vi heller z verdien til 0 i vertexshaderen for å få en stående plate
    	// som er flat på de vertiakle aksene. 
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
 
}