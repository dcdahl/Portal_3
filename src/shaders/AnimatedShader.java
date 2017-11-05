package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entiies.Camera;
import entiies.Light;
import toolbox.Maths;

public class AnimatedShader extends ShaderProgram
{
	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE = "src/shaders/animationVertex";
	private static final String FRAGMENT_FILE = "src/shaders/animationFragment";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	
	
	
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

		//location_jointTransforms = super.getUniformLocation("jointTransforms");
		
		
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		
		
		location_lightColour = new int[MAX_LIGHTS];
		location_lightPosition = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
		}
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

	public void loadLights(List<Light> lights){
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
			}else{
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
			}		
		}
	}
	
	/*
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}*/
	
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
