package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entiies.Camera;
import entiies.Light;
import toolbox.Maths;

public class AnimatedShader extends ShaderProgram
{

	private static final String VERTEX_FILE = "src/shaders/animationVertex";
	private static final String FRAGMENT_FILE = "src/shaders/animationFragment";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightColour;
	private int location_lightPosition;
	
	
	
	private int location_shineDamper;
	private int location_reflectivity;
	
	public AnimatedShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightColour = super.getUniformLocation("lightColour");
		location_lightPosition = super.getUniformLocation("lightPosition");
		//location_jointTransforms = super.getUniformLocation("jointTransforms");
		
		
		//location_shineDamper = super.getUniformLocation("shineDamper");
		//location_reflectivity = super.getUniformLocation("reflectivity");
	}

	@Override
	protected void bindAttributes()
	{
		// Binder attibutt nr 0 i VAOen til variabelnavnet position som vi finner igjen i vertex shadern.
		super.bindAttribute(0, "position");
		// Binder attibutt nr 1 i VAOen til variabelnavnet textureCoords som vi finner igjen i vertex shadern.
		super.bindAttribute(1, "textureCoords");
		// Binder attibutt nr 2 i VAOen til variabelnavnet normal som vi finner igjen i vertex shadern.
		super.bindAttribute(2, "normal");
		// Binder attibutt nr 3 i VAOen til variabelnavnet position som vi finner igjen i vertex shadern.
		super.bindAttribute(3, "jointIndicies");
		// Binder attibutt nr 4 i VAOen til variabelnavnet textureCoords som vi finner igjen i vertex shadern.
		super.bindAttribute(4, "weights");

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

	//Brukes ikke ennå
	public void loadShineVariables(float shineDamper, float reflectivity)
	{
		super.loadFloat(location_reflectivity, reflectivity);
		super.loadFloat(location_shineDamper, shineDamper);
		
	}
	
	public void loadJointTransforms(Matrix4f[] matrices)
	{
		
		for(int i = 0; i < matrices.length; i++)
		{
			int location_matrixArrayElement = super.getUniformLocation("jointTransforms[" + i + "]");
			super.loadMatrix(location_matrixArrayElement, matrices[i]);
			System.out.println(i);
		}
	}

}
