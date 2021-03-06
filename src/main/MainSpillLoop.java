
package main;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.TextureLoader;

import colladaLoader.AnimationLoader;
import colladaLoader.ColladaLoader;
import colladaLoader.MyFile;
import dataStructures.AnimatedModelData;
import dataStructures.AnimationData;
import dataStructures.JointData;
import dataStructures.JointTransformData;
import dataStructures.KeyFrameData;
import dataStructures.MeshData;
import dataStructures.SkeletonData;
import entiies.*;
import models.AnimatedModel;
import models.BoundingBox;
import models.RawModel;
import models.TexturedModel;
import portal.PortalFrameBuffers;
import portal.PortalRenderer;
import portal.PortalShader;
import portal.PortalTile;
import render.DisplayManager;
import render.Loader;
import render.MasterRenderer;
import render.OBJLoader;
import render.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
import animation.*;

/**
 * Klasse som inneholder program-loopen
 * @author Daniel Celand Dahl
 *
 */
public class MainSpillLoop
{
	static List<Entity> staticObjects = new ArrayList<Entity>();
	private static final int MAX_WEIGHTS = 3;
	private static TerrainTexturePack texturePack;
	private static TerrainTexture blendMap;
	private static float lumen = 100.5f;

	/**
	 * Program-loop
	 * @param args
	 */
	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<AnimatedEntity> animatedObjects = new ArrayList<AnimatedEntity>();
		List<Light> lights = createLights();

		// Terreng
		loadTerrainPack(loader);
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "SluJkrJ2");
		terrains.add(terrain);
		
		// Building
		ModelTexture cubeTex = new ModelTexture(loader.loadTexture("wall_texture"));
		RawModel cubeRawModel = OBJLoader.loadObjModel("cube", loader);
		TexturedModel bcubetexturedModel = new TexturedModel(cubeRawModel, cubeTex);
		Entity cube = new Entity(bcubetexturedModel, new Vector3f(50, 5, 82), 0, 0, 0, 2f);
		ModelTexture cubespec = bcubetexturedModel.getTexture();
		cubespec.setShineDamper(500);
		cubespec.setReflectivity(0.4f);
		staticObjects.add(cube);

		// Supermann
		ModelTexture superTex = new ModelTexture(loader.loadTexture("Superman_Diff"));
		RawModel superRawModel = OBJLoader.loadObjModel("superman", loader);
		TexturedModel superMantexturedModel = new TexturedModel(superRawModel, superTex);
		Entity superMan = new Entity(superMantexturedModel, new Vector3f(235, -4, 160), 0, 270, 0, 2f);
		ModelTexture superManspec = superMantexturedModel.getTexture();
		superManspec.setShineDamper(500);
		superManspec.setReflectivity(0.4f);
		 staticObjects.add(superMan);

		// *************** Objects in front of sourcePortal
		float boxScale = 2f;
		ModelTexture kasse1Texture = new ModelTexture(loader.loadTexture("orange"));
		RawModel kasse1Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse1TexturedModel = new TexturedModel(kasse1Raw, kasse1Texture);
		Entity kasse1 = new Entity(kasse1TexturedModel, new Vector3f(195,0,30), 0, 0, 0, 1f);
		staticObjects.add(kasse1);

		ModelTexture kasse2Texture = new ModelTexture(loader.loadTexture("orange"));
		RawModel kasse2Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse2TexturedModel = new TexturedModel(kasse2Raw, kasse1Texture);
		Entity kasse2 = new Entity(kasse2TexturedModel, new Vector3f(175,0,30), 0, 0, 0, 4f);
		staticObjects.add(kasse2);
		
//*************** Objects in front of destination portal ******************************************************		
		Entity kasse3 = new Entity(kasse2TexturedModel, new Vector3f(155,0,30), 0, 0, 0, 4f);
		staticObjects.add(kasse3);

		ModelTexture kasse4Texture = new ModelTexture(loader.loadTexture("jorda"));
		RawModel kasse4Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse4TexturedModel = new TexturedModel(kasse4Raw, kasse4Texture);
		Entity kasse4 = new Entity(kasse4TexturedModel, new Vector3f(135,4,30), 0, 0, 0, 4f);
		staticObjects.add(kasse4);
		
		
		Vector3f kassePosition = new Vector3f(115,8,30);
		ModelTexture kasse5Texture = new ModelTexture(loader.loadTexture("jorda"));
		RawModel kasse5Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse5TexturedModel = new TexturedModel(kasse5Raw, kasse5Texture);
		Entity kasse5 = new Entity(kasse5TexturedModel, kassePosition, 0, 1, 00, boxScale);
		staticObjects.add(kasse5);
		
//********************PORTALS ************************************************************************
		PortalFrameBuffers portal1FBO = new PortalFrameBuffers();
		PortalShader portal1Shader = new PortalShader();
		PortalRenderer portal1Renderer = new PortalRenderer(loader, portal1Shader, renderer.getProjectionMatrix(), portal1FBO);
		PortalTile sourcePortal = new PortalTile(85, 125, 0);
		List<PortalTile> portal1List = new ArrayList<PortalTile>();
		portal1List.add(sourcePortal);

		PortalFrameBuffers portal2FBO = new PortalFrameBuffers();
		PortalShader portal2Shader = new PortalShader();
		PortalRenderer portal2Renderer = new PortalRenderer(loader, portal2Shader, renderer.getProjectionMatrix(), portal2FBO);
		List<PortalTile> portal2List = new ArrayList<PortalTile>();
		PortalTile destPortal = new PortalTile(180, 50, 0);
		portal2List.add(destPortal);
		// **************************************************************************************************
		// Animatert figur
		ModelTexture animatedTex = new ModelTexture(loader.loadTexture("diffuse"));
		animatedTex.setShineDamper(500);
		animatedTex.setReflectivity(0.4f);
		File model = new File("res/model.dae");

		MyFile modelFile = new MyFile(model);
		AnimatedModelData animatedModelData = ColladaLoader.loadColladaModel(modelFile, MAX_WEIGHTS);
		AnimationData animationData = ColladaLoader.loadColladaAnimation(modelFile);
		MeshData mesh = animatedModelData.getMeshData();
		
		RawModel rawModel = loader.loadToVAO(mesh.getVertices(), mesh.getIndices(), mesh.getTextureCoords(),
				mesh.getNormals(), mesh.getJointIds(), mesh.getVertexWeights());
		TexturedModel texModel = new TexturedModel(rawModel, animatedTex);
		AnimatedModel animatedModel = loadEntity(animatedModelData, texModel);
		Animation animation = loadAnimation(animationData);
		Animator animator = new Animator(animatedModel);
		animator.doAnimation(animation);
		
		createAABBs();
		
		Vector3f playerPosition = new Vector3f(205, 0, 30);
		BoundingBox playerBB = new BoundingBox(animatedModelData.getMeshData().getVecMin(), animatedModelData.getMeshData().getVecMax(), playerPosition);
		
		Player player = new Player(animatedModel,animator,animation , playerPosition, 0, -90, 0, 1, playerBB);
		animatedObjects.add(player);
		Camera camera = new Camera(player);
		// ********** PORTAL CAMERAS *************************************************
		PortalCamera sourcePortalCamera = new PortalCamera(destPortal);
		PortalCamera destinationPortalCamera = new PortalCamera(sourcePortal);
		
		
		// ***************************************************************************
		MousePicker picker = new MousePicker(renderer.getProjectionMatrix(), camera);
		Vector3f sourcePortalPosition = new Vector3f(sourcePortal.getX(), 1f, sourcePortal.getZ() + 10);
		Vector3f entryPortalPosition = new Vector3f(destPortal.getX(), 1f, destPortal.getZ() + 10);
		destinationPortalCamera.setPosition(entryPortalPosition);
		sourcePortalCamera.setPosition(sourcePortalPosition);

		
		//********* WATER ************************************************************
		WaterFrameBuffers waterFbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFbos);
		List<WaterTile> waterTiles = new ArrayList<>();
		WaterTile tile = new WaterTile(190, 180, -10);
		waterTiles.add(tile);
		Light sun = new Light(new Vector3f(190, 180,50), new Vector3f(Light.HVIT), new Vector3f(Light.SVAK));
		
		while (!Display.isCloseRequested()) {
			// Aktiverer bevegelse av kamera
			camera.move();
			// Spillerbevegelse
			player.move(terrain);

			picker.update();
			// System.out.println(picker.getCurrentRay());
			if (player.isMoving())
				player.getAnimator().update();
			else {
				player.getAnimator().resetAnimation();
			}
			
			//Vi bruker kun 1 clipping-plane s� vi bruker nr 0 
			//Enabler bruke av gl_ClipDistance s� vi kan kalkulere og bruke clipping planes i koden v�r
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

// **********************PORTALS *****************************************************
			portal1FBO.bindReflectionFBO();
			float angle = Vector3f.angle(camera.getPosition(), entryPortalPosition);

			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);
			
			//Enkel sjekk for � teleportere spilleren til den andre portalen n�r figuren er innenfor portalframen
			if(player.getPosition().x < 85 + 20 && player.getPosition().x > 85 - 20
					&& player.getPosition().z > 120 && player.getPosition().z < 130) {
				Vector3f newPost = new Vector3f(entryPortalPosition.x + 12, 0, entryPortalPosition.z - 15);
				player.setPosition(newPost);
				player.increaseRotation(0, 180, 0);
			}

			renderer.render(lights, destinationPortalCamera, new Vector4f(1, 0, 0, sourcePortal.getHeight()));
			portal1FBO.unbindFBO();

			portal2FBO.bindReflectionFBO();
			angle = Vector3f.angle(camera.getPosition(), entryPortalPosition);
			sourcePortalCamera.setPosition(sourcePortalPosition);
			sourcePortalCamera.rotate(angle);

			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);
			
			//Enkel sjekk for � teleportere spilleren til den andre portalen n�r figuren er innenfor portalframen
			if(player.getPosition().x < 180  + 20 && player.getPosition().x > 180 - 20 
					&& player.getPosition().z > 50 && player.getPosition().z < 60) {
				Vector3f newPost = new Vector3f(sourcePortalPosition.x + 12, 0, sourcePortalPosition.z - 15);
				player.increaseRotation(0, 180, 0);	
				player.setPosition(newPost);
			}
			
			renderer.render(lights, sourcePortalCamera, new Vector4f(1, 0, 0, destPortal.getHeight()));
			portal2FBO.unbindFBO();

			for(BoundingBox aabb: BoundingBox.getAABBList())
				playerBB.intersects(aabb);
			
//**************** WATER RENDERING ***********************************************************	
			//render relfection
			waterFbos.bindReflectionFBO();
			//Kalkulerer distansen vi m� flytte kameraet. Vi vil flyttet kameraet under vannet og rendre refleksjonen fra denne posisjonen
			float distance = 2 * (-camera.getPosition().y + tile.getHeight());
			//Setter kameraet i denne h�yden
			camera.getPosition().y -= distance;
			//Inverterer pitchen.
			//Hvis kameraet ser nedover n�r vi flytter den ned, vil vi n� se oppover n�r kameraet er under vannet. 
			camera.invertPitch();
			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terrain);
			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);
			//Rendrer alt over vann overflaten med clip plane som peker oppover, med distanse fra utgangspunktet som h�yden av "water tilen"
			renderer.render(lights, camera, new Vector4f(0, 1, 0, -tile.getHeight()));
			camera.getPosition().y += distance;
			//Resetter h�yde og pitch p� kameraet n�r vi er ferdig � rendre refleksjon
			camera.invertPitch();
			waterFbos.unbindFBO();
			//render refraction
			waterFbos.bindRefractionFBO();
			for (Entity entitys : staticObjects)
			// Rendrer objektene
			// Render terreng
				renderer.processEntity(entitys);
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);
			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);
			//Rendrer alt UNDER vann overflaten for rekfraksjon s� clip planet peker nedover 
			renderer.render(lights, camera, new Vector4f(0, -1, 0, tile.getHeight()));
			waterFbos.unbindFBO();
			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);

			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);

			//Vi setter clip planet til noe usannsylig h�yt, siden glDisable(GL_CLIP_DISTANCE0) har
			//en risiko � bli ignorert av noen drivere og jeg er ikke villig til � ta den risken. 
			//I dette tilfellet vil rendre ALT i scenen/verden
			renderer.render(lights, camera, new Vector4f(0, -1, 0, 150000000));
			waterRenderer.render(waterTiles, camera, sun);
			portal1Renderer.render(portal1List, camera);
			portal2Renderer.render(portal2List, camera);

			DisplayManager.updateDisplay();
		}
		waterFbos.cleanUp();
		waterShader.cleanUp();
		portal1FBO.cleanUp();
		portal2FBO.cleanUp();
		portal1Shader.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();

		DisplayManager.closeDisplay();

	}

/**
 * Lager en animert model utifra data om modellen og en tekstur
 * @param entityData Data om modellen. Se {@link AnimatedModelData}
 * @param texture Tekstur til en modell. Se {@link TexturedModel}
 * @return En animert modell. Se {@link AnimatedModel}
 */
	public static AnimatedModel loadEntity(AnimatedModelData entityData, TexturedModel texture) {
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(texture, headJoint, skeletonData.jointCount);
	}

	/**
	 * Lager et ledd i en modell basert på data om leddet
	 * @param data Data om leddet. Se {@link JointData}
	 * @return Et ledd. Se {@link Joint}
	 */
	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}

	/**
	 * Hentet ut av AnimationLoader. Klassen og metoden er laget av ThinMatrix.
	 * Kommentert av ThinMatrix
	 * 
	 * Creates a keyframe from the data extracted from the collada file.
	 * 
	 * @param data
	 *            - the data about the keyframe that was extracted from the collada
	 *            file.
	 * @return The keyframe.
	 */
	private static KeyFrame createKeyFrame(KeyFrameData data) {
		Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for (JointTransformData jointData : data.jointTransforms) {
			JointTransform jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time, map);
	}

	/**
	 * 
	 * Hentet ut av AnimationLoader. Klassen og metoden er laget av ThinMatrix.
	 * Kommentert av ThinMatrix
	 * 
	 * Creates a joint transform from the data extracted from the collada file.
	 * 
	 * @param data
	 *            - the data from the collada file.
	 * @return The joint transform.
	 */
	private static JointTransform createTransform(JointTransformData data) {
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = Quaternion.fromMatrix(mat);
		return new JointTransform(translation, rotation);
	}

	/**
	 * Loads up a collada animation file, and returns and animation created from the
	 * extracted animation data from the file.
	 * 
	 * @param colladaFile
	 *            - the collada file containing data about the desired animation.
	 * @return The animation made from the data in the file.
	 */
	public static Animation loadAnimation(AnimationData animationData) {
		KeyFrame[] frames = new KeyFrame[animationData.keyFrames.length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = createKeyFrame(animationData.keyFrames[i]);
		}
		return new Animation(frames, animationData.lengthSeconds);
	}

	/**
	 * Laster et Blend Map med multi-texturing ved bruk av en loader
	 * Inspirert av ThinMatrix
	 * @param loader Se {@link Loader}
	 */
	private static void loadTerrainPack(Loader loader) {
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/grass")); // Svart
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud2")); // R�d
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/grassFlowers")); // Gr�nn
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/test")); // Bl�
		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

		blendMap = new TerrainTexture(loader.loadTexture("terrain/blendMap"));

	}

	/**
	 * Metode for å lage lys i en 3D-verden
	 * @return Liste av {@link Light}
	 */
	private static List<Light> createLights() {
		List<Light> lights = new ArrayList<Light>();
		Vector3f attenuation = new Vector3f(1f, 0.01f, 0.002f);

		float lysstyrke = 0.4f;

		// Posisjoner i huset
		Vector3f en = new Vector3f(50, 10, 5);
		Vector3f to = new Vector3f(100, 10, 50);
		Vector3f tre = new Vector3f(150, 10, 50);

		// Posisjon / Farge / Styrke
		lights.add(new Light(new Vector3f(500, 1000, -7000), Light.HVIT));
		lights.add(new Light(en, Light.ROD, Light.SVAK));
		lights.add(new Light(to, Light.GUL, Light.MIDDELS));
		lights.add(new Light(tre, Light.ORANGE, Light.STERK));

		return lights;
		
	}

	
	/**
	 * Lager Axis Align Bounding Boxes rundt statiske objekter fra en statisk liste	 
	 * 
	 * */
	private static void createAABBs(){
		
		// Lager AABB basert p� posisjonene og st�rrelses skalaen. 
		for (Entity ent : staticObjects) {
			float scale = ent.getScale();
			Vector3f scaledMax = new Vector3f(
					ent.getModel().getRawModel().getVecMax().x * scale, 
					ent.getModel().getRawModel().getVecMax().y * scale, 
					ent.getModel().getRawModel().getVecMax().z * scale);
			Vector3f scaledMin = new Vector3f(
					ent.getModel().getRawModel().getVecMin().x * scale, 
					ent.getModel().getRawModel().getVecMin().y * scale, 
					ent.getModel().getRawModel().getVecMin().z * scale);
			
			// Adderer inn verdensposisjonen med maks/min punktene for � f� korrekte punkter i verden. 
			Vector3f Max = Vector3f.add(scaledMax, ent.getPosition(), null);
			Vector3f Min = Vector3f.add(scaledMin, ent.getPosition(), null);
			
			BoundingBox aabb = new BoundingBox(Min,Max, ent.getPosition());
			BoundingBox.getAABBList().add(aabb);
		}
	}
	

	
}
