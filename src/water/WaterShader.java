package water;
 
import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import shaders.StaticShader;
import toolbox.Maths;
import entiies.Camera;
import entiies.Light;
 
/**
 * Tilnærmet lik andre shadere for eksempel {@link StaticShader}
 * @author Marius
 * Skrevet fra ThinMatrix water tutorial 
 */
public class WaterShader extends ShaderProgram {
 
    private final static String VERTEX_FILE = "src/water/waterVertex.txt";
    private final static String FRAGMENT_FILE = "src/water/waterFragment.txt";
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectTex;
    private int location_refractTex;
    private int location_dudvmap;
    private int location_move;
    private int location_cameraPos;
    private int location_normalMap;
    private int location_lightColor;
    private int location_lightPos;
 
    public WaterShader() {
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
        location_reflectTex = getUniformLocation("reflectTex");
        location_refractTex = getUniformLocation("refractTex");
        location_dudvmap = getUniformLocation("DuDvMap");
        location_move = getUniformLocation("moveFactor");
        location_cameraPos = getUniformLocation("cameraPos");
        location_normalMap = getUniformLocation("normalMap");
        location_lightColor = getUniformLocation("lightColor");
        location_lightPos = getUniformLocation("lightPos");
    }
 
    public void connectTextureUnits() {
    	super.loadInt(location_reflectTex, 0);
    	super.loadInt(location_refractTex, 1);
    	super.loadInt(location_dudvmap, 2);
    	super.loadInt(location_normalMap, 3);
    }
    
    public void loadMoveFactor(float factor) {
    	super.loadFloat(location_move, factor);
    }
    
    public void loadLight(Light sun) {
    	super.loadVector(location_lightColor, sun.getColour());
    	super.loadVector(location_lightPos, sun.getPosition());
    }
    
    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }
     
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
 
        loadMatrix(location_viewMatrix, viewMatrix);
        super.loadVector(location_cameraPos, camera.getPosition());
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }
 
}