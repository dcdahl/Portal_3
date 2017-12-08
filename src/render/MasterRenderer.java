package render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entiies.AnimatedEntity;
import entiies.Camera;
import entiies.Entity;
import entiies.Light;
import entiies.PortalCamera;
import models.AnimatedModel;
import models.TexturedModel;
import shaders.AnimatedShader;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;

public class MasterRenderer {

	private static final float FOV = 120;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 10000f;
	private StaticShader shader = new StaticShader();
	private AnimatedShader animatedShader = new AnimatedShader();
	private EntityRenderer renderer;
	private SkeletonRenderer skeletonRenderer;
	private Matrix4f projectionMatrix;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private SkyboxRenderer skyboxRenderer;
	private static boolean polygonMode = false;
	
	// Hashmap med liste over de forskjellige entitetene for hver modell.
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private Map<AnimatedModel,List<AnimatedEntity>> animatedEntities = new HashMap<AnimatedModel,List<AnimatedEntity>>();
	
	/**
	 * I denne klassen er mye hentet direkte fra ThinMatrix
	 * @param loader
	 */
	public MasterRenderer(Loader loader){
		// Sørger for at ikke hele objektet ikke blir rendret ( fjerner bakdelen )
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skeletonRenderer = new SkeletonRenderer(animatedShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}
	
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane){
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		animatedShader.start();
		animatedShader.loadLights(lights);
		animatedShader.loadViewMatrix(camera);
		skeletonRenderer.render(animatedEntities);
		animatedShader.stop();
		skyboxRenderer.render(camera);
		
		terrains.clear();
		entities.clear();
		animatedEntities.clear();// må være der for å ikke lage mange hvert frame uten å slette de,
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	
	// Putter entiteter inn i hashmapen 
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	// Hentet fra ThinMatrix
	public void processAnimatedEntity(AnimatedEntity animatedEntity)
	{
		AnimatedModel entityModel = animatedEntity.getModel();
		List<AnimatedEntity> batch = animatedEntities.get(entityModel);
		if(batch!=null){
			batch.add(animatedEntity);
		}else{
			List<AnimatedEntity> newBatch = new ArrayList<AnimatedEntity>();
			newBatch.add(animatedEntity);
			animatedEntities.put(entityModel, newBatch);
		}
	}
	
	// Hentet fra ThinMatrix
	private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}
	
	
	public void prepare(){
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glClearColor(0.49f, 89f, 0.98f, 1);
		
		// Legger til mulighet for å se verteksene i spillet ved å trykke på X
		if(polygonMode)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		if(!polygonMode)
			GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_FILL );
				
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		animatedShader.cleanUp();
	}

	public static boolean isPolygonMode() {
		return polygonMode;
	}

	public static void setPolygonMode(boolean polyMode) {
		polygonMode = polyMode;
	}

	// Hentet fra ThinMatrix
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane,
			Matrix4f rotationMatrix) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		animatedShader.start();
		animatedShader.loadLights(lights);
		animatedShader.loadViewMatrix(camera);
		skeletonRenderer.render(animatedEntities);
		animatedShader.stop();
		
		terrains.clear();
		entities.clear();
		animatedEntities.clear();// mï¿½ vï¿½re der for ï¿½ ikke lage mange hvert frame uten ï¿½ slette de,
		
	}
	
	
	
}
