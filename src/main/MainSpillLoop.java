
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
import models.RawModel;
import models.TexturedModel;
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

public class MainSpillLoop {

	private static final int MAX_WEIGHTS = 3;
	private static TerrainTexturePack texturePack;
	private static TerrainTexture blendMap;
	private static float lumen = 100.5f;

	public static void main(String[] args) {

		DisplayManager.createDisplay();

		MasterRenderer renderer = new MasterRenderer();
		Loader loader = new Loader();
		List<Entity> staticObjects = new ArrayList<Entity>();
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

		// Building
		ModelTexture buildingTex = new ModelTexture(loader.loadTexture("wall_texture"));
		RawModel buildingRawModel = OBJLoader.loadObjModel("cube_uvmapped", loader);
		TexturedModel buildingtexturedModel = new TexturedModel(buildingRawModel, buildingTex);
		Entity building = new Entity(buildingtexturedModel, new Vector3f(95, -2, 82), 0, 0, 0, 10f);
		ModelTexture buildingspec = buildingtexturedModel.getTexture();
		buildingspec.setShineDamper(500);
		buildingspec.setReflectivity(0.4f);
		staticObjects.add(building);

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
		Entity superMan = new Entity(superMantexturedModel, new Vector3f(0, 0, 10), 0, 0, 0, 10f);
		ModelTexture superManspec = superMantexturedModel.getTexture();
		superManspec.setShineDamper(500);
		superManspec.setReflectivity(0.4f);
		// staticObjects.add(superMan);

		// *************** Objects in front of sourcePortal
		// *************************************************
		ModelTexture kasse1Texture = new ModelTexture(loader.loadTexture("orange"));
		RawModel kasse1Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse1TexturedModel = new TexturedModel(kasse1Raw, kasse1Texture);
		Entity kasse1 = new Entity(kasse1TexturedModel, new Vector3f(85, 0, 110), 0, 0, 0, 1f);
		staticObjects.add(kasse1);

		ModelTexture kasse2Texture = new ModelTexture(loader.loadTexture("blueXD"));
		RawModel kasse2Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse2TexturedModel = new TexturedModel(kasse2Raw, kasse1Texture);
		Entity kasse2 = new Entity(kasse2TexturedModel, new Vector3f(95, 0, 100), 0, 0, 0, 4f);
		staticObjects.add(kasse2);
		// *************** Objects in front of destination portal
		// ******************************************************
		ModelTexture kasse3Texture = new ModelTexture(loader.loadTexture("Moon"));
		RawModel kasse3Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse3TexturedModel = new TexturedModel(kasse3Raw, kasse3Texture);
		Entity kasse3 = new Entity(kasse3TexturedModel, new Vector3f(165, 0, 20), 0, 0, 0, 3f);
		staticObjects.add(kasse3);

		ModelTexture kasse4Texture = new ModelTexture(loader.loadTexture("jorda"));
		RawModel kasse4Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse4TexturedModel = new TexturedModel(kasse4Raw, kasse4Texture);
		Entity kasse4 = new Entity(kasse4TexturedModel, new Vector3f(195, 0, 4), 0, 0, 0, 6f);
		staticObjects.add(kasse4);

		ModelTexture kasse5Texture = new ModelTexture(loader.loadTexture("jorda"));
		RawModel kasse5Raw = OBJLoader.loadObjModel("cube", loader);
		TexturedModel kasse5TexturedModel = new TexturedModel(kasse5Raw, kasse5Texture);
		Entity kasse5 = new Entity(kasse5TexturedModel, new Vector3f(175, 0, 30), 0, 1, 0, 1f);
		staticObjects.add(kasse5);

		// ********************WATER AKA PORTALS N
		// STUFF************************************************************************
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile sourcePortal = new WaterTile(85, 125, 0);
		waters.add(sourcePortal);

		WaterFrameBuffers ok = new WaterFrameBuffers();
		WaterShader okShader = new WaterShader();
		WaterRenderer okRenderer = new WaterRenderer(loader, okShader, renderer.getProjectionMatrix(), ok);
		List<WaterTile> oks = new ArrayList<WaterTile>();
		WaterTile destPortal = new WaterTile(180, 50, 0);
		oks.add(destPortal);
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

		// AnimatedEntity newAnimatedEntity = new AnimatedEntity(animatedModel, new
		// Vector3f(100, 0, 100), 0, 0, 0, 1);
		// nimatedEntity newAnimatedEntity2 = new AnimatedEntity(animatedModel, new
		// Vector3f(0, 0, 0), 1, 0, 0, 1);
		// AnimatedEntity newAnimatedEntity3 = new AnimatedEntity(animatedModel, new
		// Vector3f(0, 0, 0), 1, 0, 0, 1);
		// animatedObjects.add(newAnimatedEntity);
		// animatedObjects.add(newAnimatedEntity2);
		// animatedObjects.add(newAnimatedEntity3);

		Player player = new Player(animatedModel, animator, animation, new Vector3f(100, 0, 100), 0, 0, 0, 1);
		animatedObjects.add(player);
		Camera camera = new Camera(player);
		// ********** PORTAL CAMERAS *************************************************
		PortalCamera sourcePortalCamera = new PortalCamera();
		PortalCamera destinationPortalCamera = new PortalCamera();

		// ***************************************************************************
		MousePicker picker = new MousePicker(renderer.getProjectionMatrix(), camera);
		Vector3f sourcePortalPosition = new Vector3f(sourcePortal.getX(), 1f, sourcePortal.getZ() + 10);
		Vector3f entryPortalPosition = new Vector3f(destPortal.getX(), 1f, destPortal.getZ() + 10);

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
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			// **********************PORTALS
			// ********************************************************
			fbos.bindReflectionFrameBuffer();

			float angle = Vector3f.angle(camera.getPosition(), entryPortalPosition);
			//change.normalise();

			
			destinationPortalCamera.setPosition(entryPortalPosition);
			destinationPortalCamera.rotate(angle);
			//destinationPortalCamera.setPitch(0);

			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);

			renderer.render(lights, destinationPortalCamera, new Vector4f(1, 0, 0, sourcePortal.getHeight()));
			// renderer.render(lights, destinationPortalCamera, new Vector4f(1, 0, 0,
			// destPortal.getHeight()));
			// xd.invertPitch();
			fbos.unbindCurrentFrameBuffer();

			ok.bindReflectionFrameBuffer();

			//change = Vector3f.sub(camera.getPosition(), sourcePortalPosition, null);
			angle = Vector3f.angle(camera.getPosition(), entryPortalPosition);
			//change.normalise();
			//sourcePortalPosition.x += change.x;
			//sourcePortalPosition.y += change.y;
			
			sourcePortalCamera.setPosition(sourcePortalPosition);
			sourcePortalCamera.rotate(angle);
			//sourcePortalCamera.setPitch(0);

			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);
			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);

			renderer.render(lights, sourcePortalCamera, new Vector4f(1, 0, 0, destPortal.getHeight()));
			// renderer.render(lights, destinationPortalCamera, new Vector4f(1, 0, 0,
			// destPortal.getHeight()));
			// xd.invertPitch();
			ok.unbindCurrentFrameBuffer();

			// *******************************************************************************************
			// TODO clipplane for terrain

			// Rendrer objektene
			for (Entity entitys : staticObjects)
				renderer.processEntity(entitys);

			// Render terreng
			for (Terrain terr : terrains)
				renderer.processTerrain(terr);

			for (AnimatedEntity animatedEntity : animatedObjects)
				renderer.processAnimatedEntity(animatedEntity);

			renderer.render(lights, camera, new Vector4f(0, -1, 0, 150000000));

			waterRenderer.render(waters, camera);

			okRenderer.render(oks, camera);

			DisplayManager.updateDisplay();
		}
		fbos.cleanUp();
		ok.cleanUp();
		waterShader.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();

		DisplayManager.closeDisplay();

	}

	/*
	 * Fors�k p� � f� jorden til � g� i bane rundt solen Work in progress :-)
	 */
	private static Vector3f calcOrbit(Vector3f posOrigin, Vector3f posOrbiter, float degree, int radius) {

		Double x = Math.cos(degree) * radius;
		Double z = Math.sin(degree) * radius;

		posOrbiter.x = posOrigin.x + x.floatValue();
		posOrbiter.z = posOrigin.z + z.floatValue();

		return new Vector3f(posOrbiter);
	}

	private static float i = 2f;

	private static float exp() {
		return i * i;

	}

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
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud2")); // R�d
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/grassFlowers")); // Gr�nn
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/test")); // Bl�
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

}
