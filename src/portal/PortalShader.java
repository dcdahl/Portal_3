package portal;
 
import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import shaders.StaticShader;
import toolbox.Maths;
import entiies.Camera;
 
/**
 * Tilnærmet lik andre shadere for eksempel {@link StaticShader} og {@link WaterShader}
 * @author Marius
 *Modifisering av vann tutorial fra ThinMatrix
 */
public class PortalShader extends ShaderProgram {
 
    private final static String VERTEX_FILE = "src/portal/portalVertex.txt";
    private final static String FRAGMENT_FILE = "src/portal./portalFragment.txt";
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
 
    public PortalShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTexture = getUniformLocation("reflectionTexture");  
    }
    
    //Laster opp en int til sampler2Dene som ligger i fragmentshaderen
    //Her setter vi hvilken textureunit hver sampler skal sample fra
    public void connectTextureUnits() {
    	super.loadInt(location_reflectionTexture, 0);
    }
  
    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }
     
    public void loadViewMatrix(Camera camera){
    	
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }
 
}