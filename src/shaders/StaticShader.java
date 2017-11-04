package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entiies.Camera;
import entiies.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/shaders/vertex.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragment.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_plane;
	
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		
	}

	
	// Binder attibuttene i VAOen til variabelnavnene som vi finner igjen i vertex shadern.
	
	@Override
	protected void bindAttributes() {
		// Binder attibutt nr 0 i VAOen til variabelnavnet position som vi finner igjen i vertex shadern.
		super.bindAttribute(0, "position");
		// Binder attibutt nr 1 i VAOen til variabelnavnet textureCoords som vi finner igjen i vertex shadern.
		super.bindAttribute(1, "textureCoords");
		// Binder attibutt nr 2 i VAOen til variabelnavnet normal som vi finner igjen i vertex shadern.
		super.bindAttribute(2, "normal");
	}


	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightColour = super.getUniformLocation("lightColour");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_plane = super.getUniformLocation("plane");
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}

	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	
	
}
