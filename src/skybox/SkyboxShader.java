package skybox;

import org.lwjgl.util.vector.Matrix4f;

import entiies.Camera;
import shaders.ShaderProgram;
import toolbox.Maths;
 
// Klassen er direkte kopiert fra ThinMatrix
public class SkyboxShader extends ShaderProgram{
 
    private static final String VERTEX_FILE = "src/shaders/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/skyboxFragmentShader.txt";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}