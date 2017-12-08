
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

public class MainSpillLoop
{
	static List<Entity> staticObjects = new ArrayList<Entity>();
	private static final int MAX_WEIGHTS = 3;
	private static TerrainTexturePack texturePack;
	private static TerrainTexture blendMap;
	private static float lumen = 100.5f;

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		
		createJumpBoxes(loader);
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<AnimatedEntity> animatedObjects = new ArrayList<AnimatedEntity>();
		List<Light> lights = createLights();

		// Terreng
		loadTerrainPack(loader);
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "SluJkrJ2");
		// Terrain terrain2 = new Terrain(-1, -1, loader, new
		// ModelTexture(loader.loadTexture("grass")));
		terrains.add(terrain);
		// terrains.add(terrain2);
		
	
		/*
		// Building
		ModelTexture buildingTex = new ModelTexture(loader.loadTexture("wall_texture"));
		RawModel buildingRawModel = OBJLoader.loadObjModel("cube_uvmapped", loader);
		TexturedModel buildingtexturedModel = new TexturedModel(buildingRawModel, buildingTex);
		//Entity building = new Entity(buildingtexturedModel, new Vector3f(95, -2, 82), 0, 0, 0, 10f);
		Entity building = new Entity(buildingtexturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10f);
		ModelTexture buildingspec = buildingtexturedModel.getTexture();
		buildingspec.setShineDamper(500);
		buildingspec.setReflectivity(0.4f);
		//staticObjects.add(building);*/

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
		// *************************************************
		float boxScale = 2f;
		ModelTexture kasse1Texture = new ModelTexture(loader.loadTexture("orange"));
		RawModel kasse1Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse1TexturedModel = new TexturedModel(kasse1Raw, kasse1Texture);
		Entity kasse1 = new Entity(kasse1TexturedModel, new Vector3f(195,0,30), 0, 0, 0, 1f);
		staticObjects.add(kasse1);

		ModelTexture kasse2Texture = new ModelTexture(loader.loadTexture("blueXD"));
		RawModel kasse2Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse2TexturedModel = new TexturedModel(kasse2Raw, kasse1Texture);
		Entity kasse2 = new Entity(kasse2TexturedModel, new Vector3f(175,0,30), 0, 0, 0, 4f);
		staticObjects.add(kasse2);
//*************** Objects in front of destination portal ******************************************************		
		//ModelTexture kasse3Texture = new ModelTexture(loader.loadTexture("Moon"));
		//RawModel kasse3Raw = OBJLoader.loadObjModel("cube", loader);
		//TexturedModel kasse3TexturedModel = new TexturedModel(kasse3Raw, kasse3Texture);
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
		//Vector3f scaledMax = new Vector3f(kasse5Raw.getVecMax().x * boxScale,kasse5Raw.getVecMax().y * boxScale, kasse5Raw.getVecMax().z * boxScale);
		//BoundingBox kasseBB = new BoundingBox(kasse5Raw.getVecMin(),scaledMax, kassePosition);
		//Vector3f scaledMin = (Vector3f) kasse5Raw.getVecMax().scale(boxScale);
		//BoundingBox.getAABBList().add(kasseBB);
		
		
//********************WATER AKA PORTALS N STUFF************************************************************************
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
		
		//AnimatedEntity newAnimatedEntity = new AnimatedEntity(animatedModel, new Vector3f(100, 0, 100), 0, 0, 0, 1);
				//nimatedEntity newAnimatedEntity2 = new AnimatedEntity(animatedModel, new Vector3f(0, 0, 0), 1, 0, 0, 1);
				//AnimatedEntity newAnimatedEntity3 = new AnimatedEntity(animatedModel, new Vector3f(0, 0, 0), 1, 0, 0, 1);
				//animatedObjects.add(newAnimatedEntity);
				//animatedObjects.add(newAnimatedEntity2);
				//animatedObjects.add(newAnimatedEntity3);
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
			// Vector3f old = camera.getPosition();
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
			
			//Vi bruker kun 1 clipping-plane sï¿½ vi bruker nr 0 
			//Enabler bruke av gl_ClipDistance sï¿½ vi kan kalkulere og bruke clipping planes i koden vï¿½r
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			// **********************PORTALS
			portal1FBO.bindReflectionFBO();
			float angle = Vector3f.angle(camera.getPosition(), entryPortalPosition);
			//change.normalise();

			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);
			
			//Enkel sjekk for ï¿½ teleportere spilleren til den andre portalen nï¿½r figuren er innenfor portalframen
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

			// sourcePortalCamera.rotate(angle);
			// sourcePortalCamera.setPitch(0);

			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);
			
			//Enkel sjekk for ï¿½ teleportere spilleren til den andre portalen nï¿½r figuren er innenfor portalframen
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
			//Kalkulerer distansen vi mï¿½ flytte kameraet. Vi vil flyttet kameraet under vannet og rendre refleksjonen fra denne posisjonen
			float distance = 2 * (-camera.getPosition().y + tile.getHeight());
			//Setter kameraet i denne hï¿½yden
			camera.getPosition().y -= distance;
			//Inverterer pitchen.
			//Hvis kameraet ser nedover nï¿½r vi flytter den ned, vil vi nï¿½ se oppover nï¿½r kameraet er under vannet. 
			camera.invertPitch();
			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terrain);
			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);
			//Rendrer alt over vann overflaten med clip plane som peker oppover, med distanse fra utgangspunktet som hï¿½yden av "water tilen"
			renderer.render(lights, camera, new Vector4f(0, 1, 0, -tile.getHeight()));
			camera.getPosition().y += distance;
			//Resetter hï¿½yde og pitch pï¿½ kameraet nï¿½r vi er ferdig ï¿½ rendre refleksjon
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
			//Rendrer alt UNDER vann overflaten for rekfraksjon sï¿½ clip planet peker nedover 
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

			//Vi setter clip planet til noe usannsylig hï¿½yt, siden glDisable(GL_CLIP_DISTANCE0) har
			//en risiko ï¿½ bli ignorert av noen drivere og jeg er ikke villig til ï¿½ ta den risken. 
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
/*
	private static Vector3f SetPortalPosition(Camera camera, Vector3f portalNormal) {

		Vector3f difference = Vector3f.sub(camera.getPosition(), portalNormal, null);
		difference.normalise();
		Vector3f v = Vector3f.cross(portalNormal, difference, null);
		Matrix3f m = Maths.createMatrix3fFromVector3f(v);
		float sinus = v.length();
		float cosinus = Vector3f.dot(portalNormal, difference);

		return null;
	}
*/


	public static AnimatedModel loadEntity(AnimatedModelData entityData, TexturedModel texture) {
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(texture, headJoint, skeletonData.jointCount);
	}

	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}

	/**
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

	private static void loadTerrainPack(Loader loader) {
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/grass")); // Svart
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud2")); // Rï¿½d
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/grassFlowers")); // Grï¿½nn
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/test")); // Blï¿½
		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

		blendMap = new TerrainTexture(loader.loadTexture("terrain/blendMap"));

	}

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

	
	
	private static void createAABBs(){
		
		// Lager AABB basert på posisjonene og størrelses skalaen. 
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
			
			// Adderer inn verdensposisjonen med maks/min punktene for ï¿½ fï¿½ korrekte punkter i verden. 
			Vector3f Max = Vector3f.add(scaledMax, ent.getPosition(), null);
			Vector3f Min = Vector3f.add(scaledMin, ent.getPosition(), null);
			
			BoundingBox aabb = new BoundingBox(Min,Max, ent.getPosition());
			BoundingBox.getAABBList().add(aabb);
		}
	}
	
	
	private static void createJumpBoxes(Loader loader){
		ModelTexture kasse2Texture = new ModelTexture(loader.loadTexture("blueXD"));
		RawModel kasse2Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse2TexturedModel = new TexturedModel(kasse2Raw, kasse2Texture);

		Vector3f startPosition = new Vector3f(175,0,100);
		
		int heightIncrement = 4;
		int lenghIncrement = 20;
		int zIncrement = 0;
		float boxScale = 4f;
		int amount = 10;
		
		for (int i = 0; i < amount; i++) {
			staticObjects.add(new Entity(kasse2TexturedModel, startPosition, 0, 0, 0, boxScale));
			startPosition.setX(startPosition.x + lenghIncrement);
			startPosition.setY(startPosition.y + heightIncrement);
			startPosition.setZ(startPosition.z + zIncrement);
		}
		
		
	}
	
	
	
}
