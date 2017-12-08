package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.JointTransform;
import entiies.Camera;
import entiies.Light;
import toolbox.Maths;

/**
 * Shader for animerte figurer
 * @author Jonatan Bårdsen
 *
 */
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
	private int location_attenuation[];
	
	private int location_shineDamper;
	private int location_reflectivity;
	
	/**
	 * Konstruktør
	 */
	public AnimatedShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	
	}

	/**
	 * Henter adressen i minnet for variabler som brukes i shaderen
	 */
	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		
		
		location_lightColour = new int[MAX_LIGHTS];
		location_lightPosition = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
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

	/**
	 * Laster lys-objekter til shaderen
	 * @param lights Liste med {@link Light}
	 */
	public void loadLights(List<Light> lights){
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if(i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColour());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}else{
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}		
		}
	}
	
	/**
	 * Laster transformasjon-matrisen til shaderen
	 * @param matrix Transformasjon-matrisen
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * Laster projeksjon-matrisen til shaderen
	 * @param projection
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**
	 * Laster en view-matrise fra et {@link Camera}
	 * @param camera Se {@link Camera}
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	/**
	 * Laster variabler for hvordan lys reflekteres til shaderen
	 * @param shineDamper
	 * @param reflectivity
	 */
	public void loadShineVariables(float shineDamper, float reflectivity)
	{
		super.loadFloat(location_reflectivity, reflectivity);
		super.loadFloat(location_shineDamper, shineDamper);
		
	}
	
	/**
	 * Laster en liste med {@link JointTransform} i matrise-form til shaderen
	 * @param matrices JointTransforms som matriser
	 */
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
